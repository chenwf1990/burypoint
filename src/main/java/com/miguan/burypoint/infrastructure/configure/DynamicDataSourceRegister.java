package com.miguan.burypoint.infrastructure.configure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zc.lin
 * @Date 2019-11-21
 * @Description 注册动态数据源
 * 初始化数据源和提供了执行动态切换数据源的工具类
 * EnvironmentAware（获取配置文件配置的属性值）
 */
@Slf4j
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    //指定默认数据源(springboot2.0默认数据源是hikari如何想使用其他数据源可以自己配置)
    private static final String DATASOURCE_TYPE_DEFAULT = "com.zaxxer.hikari.HikariDataSource";
    //默认数据源
    private DataSource defaultDataSource;
    //用户自定义数据源
    private Map<String, DataSource> slaveDataSources = new HashMap<>();

    @Override
    public void setEnvironment(Environment environment) {
        initDefaultDataSource(environment);
        initslaveDataSources(environment);
    }

    private void initDefaultDataSource(Environment env) {
        // 读取主数据源
        Map<String, Object> dsMap = new HashMap<>();
        dsMap.put("driver", env.getProperty("spring.datasource.driverClassName"));
        dsMap.put("url", env.getProperty("spring.datasource.url"));
        dsMap.put("username", env.getProperty("spring.datasource.username"));
        dsMap.put("password", env.getProperty("spring.datasource.password"));
        defaultDataSource = buildDataSource(dsMap);
    }


    private void initslaveDataSources(Environment env) {
        // 读取配置文件获取更多数据源
        String[] dsPrefixs = env.getProperty("spring.datasource.names", String[].class);
        if (dsPrefixs == null) {
            return;
        }
        for (String dsPrefix : dsPrefixs) {
            final String url = env.getProperty("spring.datasource." + dsPrefix + ".jdbc-url");
            if (StringUtils.isBlank(url)) {
                continue;
            }
            Map<String, Object> dsMap = new HashMap<>();
            dsMap.put("url", url);
            // 多个数据源
            dsMap.put("driver", env.getProperty("spring.datasource." + dsPrefix + ".driver-class-name"));
            dsMap.put("username", env.getProperty("spring.datasource." + dsPrefix + ".username"));
            dsMap.put("password", env.getProperty("spring.datasource." + dsPrefix + ".password"));
            DataSource ds = buildDataSource(dsMap);
            if (ds != null) {
                slaveDataSources.put(dsPrefix, ds);
            }
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        final Map<Object, Object> targetDataSources = new HashMap<>();
        //添加默认数据源
        targetDataSources.put("dataSource", this.defaultDataSource);
        //添加其他数据源
        targetDataSources.putAll(slaveDataSources);

        //创建DynamicDataSource
        final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        final MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        //注册 - BeanDefinitionRegistry
        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);

        DynamicDataSourceContextHolder.DATA_SOURCE_CONTAINS.add("dataSource");
        DynamicDataSourceContextHolder.DATA_SOURCE_CONTAINS.addAll(slaveDataSources.keySet());
        log.info("Dynamic DataSource Registry");
    }

    @SuppressWarnings("unchecked")
    private DataSource buildDataSource(Map<String, Object> dataSourceMap) {
        Object type = dataSourceMap.get("type");
        if (type == null) {
            type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource
        }
        final String driverClassName = dataSourceMap.get("driver").toString();
        final String url = dataSourceMap.get("url").toString();
        final String username = dataSourceMap.get("username").toString();
        final String password = dataSourceMap.get("password").toString();
        try {
            // 自定义DataSource配置
            final Class<?> dataSourceClass = Class.forName(type.toString());
            if (!DataSource.class.isAssignableFrom(dataSourceClass)) {
                return null;
            }
            return DataSourceBuilder.create().type((Class<? extends DataSource>) dataSourceClass).driverClassName(driverClassName).url(url).username(username).password(password).build();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
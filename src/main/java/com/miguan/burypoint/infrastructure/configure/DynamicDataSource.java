package com.miguan.burypoint.infrastructure.configure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源 AbstractRoutingDataSource(每执行一次数据库，动态获取DataSource)
 * @Author zc.lin
 * @Date 2019-11-21
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        final String dataSourceType = DynamicDataSourceContextHolder.THREAD_LOCAL_DATA_SOURCE.get();
        if (StringUtils.isNotBlank(dataSourceType)) {
            //log.info("使用数据源：{}", dataSourceType);
        } else {
            //log.info("使用默认数据源");
        }
        return dataSourceType;
    }
}
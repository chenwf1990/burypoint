package com.miguan.burypoint.infrastructure.configure;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author zc.lin
 * @Description 动态数据源通知
 * @Date 2019-11-21
 */
@Aspect
@Order(-1)//保证在@Transactional之前执行
@Component
@Slf4j
public class DynamicDataSourceAspect {

    //改变数据源
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        String dataSourceName = targetDataSource.name();
        if (DynamicDataSourceContextHolder.hasDataSource(dataSourceName)) {
            DynamicDataSourceContextHolder.THREAD_LOCAL_DATA_SOURCE.set(dataSourceName);
        }
    }

    @After("@annotation(targetDataSource)")
    public void clearDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        DynamicDataSourceContextHolder.THREAD_LOCAL_DATA_SOURCE.remove();
    }
}
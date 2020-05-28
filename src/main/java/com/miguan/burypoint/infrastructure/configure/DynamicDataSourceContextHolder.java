package com.miguan.burypoint.infrastructure.configure;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zc.lin
 * @Date 2019-11-21
 * @Description 动态数据源上下文管理
 */
@Slf4j
public class DynamicDataSourceContextHolder {

    //存放当前线程使用的数据源类型信息
    static final ThreadLocal<String> THREAD_LOCAL_DATA_SOURCE = new ThreadLocal<>();
    //存放数据源id
    static final List<String> DATA_SOURCE_CONTAINS = new ArrayList<>();

    //判断当前数据源是否存在
    static boolean hasDataSource(String dataSourceName) {
        return DATA_SOURCE_CONTAINS.contains(dataSourceName);
    }

}
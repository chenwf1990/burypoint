package com.miguan.burypoint.domain.service;

import com.miguan.burypoint.domain.vo.AdvBuryingLogVO;
import com.miguan.burypoint.domain.vo.UserBuryingPointVo;

import java.util.Map;

public interface UserBuriedPointService {

    void insert(UserBuryingPointVo userBuryingPointVo,String flag);

    /**
     * 动态保存map数据
     * @param tableName 表名
     * @param data 数据
     */
    void insertMap(String tableName, Map<String, Object> data);

    /**
     * 判断表是否有这个字段
     * @param tableName 表名
     * @param column 字段名
     * @return Boolean
     */
    boolean hasColumns(String tableName, String column);

    void initTables();
    void initTableIsEmpty();

    void saveAdvBuryingLog(AdvBuryingLogVO advBuryingLogVO);
}

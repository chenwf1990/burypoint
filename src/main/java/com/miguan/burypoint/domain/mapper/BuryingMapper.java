package com.miguan.burypoint.domain.mapper;

import com.miguan.burypoint.domain.entity.UserBuryingPointColumns;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
@Mapper
public interface BuryingMapper {

    /**
     * 动态添加数据
     * @param tableName 表
     * @param data 数据
     */
    @Insert("<script>insert into ${tn} <foreach collection=\"data.keys\" item=\"key\" open=\"(\" close=\")\" separator=\",\"> ${key} </foreach> " +
            "values <foreach collection=\"data.values\" item=\"value\" open=\"(\" close=\")\" separator=\",\"> #{value} </foreach></script>")
    void insertDynamic(@Param("tn") String tableName, @Param("data") Map<String, Object> data);

    /**
     * 获取表的全部字段
     * @return
     */
    @Select("SELECT distinct `column_name`,`table_name` FROM information_schema.COLUMNS  WHERE TABLE_SCHEMA = database()")
    List<UserBuryingPointColumns> getColumns();
}

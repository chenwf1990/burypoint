package com.miguan.burypoint.infrastructure.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.miguan.burypoint.domain.entity.UserBuryingPointColumns;
import com.miguan.burypoint.domain.mapper.BuryingMapper;
import com.miguan.burypoint.domain.repositories.AdvBuryingLogRepository;
import com.miguan.burypoint.domain.service.UserBuriedPointService;
import com.miguan.burypoint.domain.vo.AdvBuryingLogVO;
import com.miguan.burypoint.domain.vo.UserBuryingPointVo;
import com.miguan.burypoint.mongodb.AdvBuryingLogMongodb;
import com.miguan.burypoint.mongodb.UserBuryingPointMongodb;
import com.miguan.burypoint.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserBuriedPointServiceImpl implements UserBuriedPointService {

    private static Map<String, Set<String>> tableColumns = new HashMap<>();

    @Resource
    private BuryingMapper buryingMapper;

    @Resource
    private AdvBuryingLogRepository advBuryingLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String is_True = "true是1";
    private static final String is_false = "false否0";

    private static final String ADV_BURYING_LOG_MONGODB = "advBuryingLogMongodb_";
    private static final String USER_BURYING_POINT_MONGODB = "userBuryingPointMongodb_";


    @Override
    @Transactional
    public void insert(UserBuryingPointVo userBuryingPointVo,String flag) {
        final Map<String, Object> datas = new ConcurrentHashMap<>(100);
        //设置一些默认属性
        this.setDefaultValue(userBuryingPointVo);
        //判断是否是新用户 并且赋值
        userBuryingPointVo.setIsNew(Integer.valueOf(flag));
        String jsonStr = JSONObject.toJSONString(userBuryingPointVo);
        Map<String,Object> jsonMap = JSONObject.parseObject(jsonStr);
        jsonMap.put("creatTime",userBuryingPointVo.getCreatTime());
        jsonMap.keySet().forEach(e -> datas.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e),jsonMap.get(e)));
        buryingMapper.insertDynamic("xy_burying_point",datas);

        UserBuryingPointMongodb userBuryingPointMongodb = new UserBuryingPointMongodb();
        BeanUtils.copyProperties(userBuryingPointVo,userBuryingPointMongodb);
        userBuryingPointMongodb.setCreatTime(userBuryingPointVo.getCreatTime());
        mongoTemplate.save(userBuryingPointMongodb,USER_BURYING_POINT_MONGODB+ DateUtils.parseDateToStr(userBuryingPointVo.getCreatTime(),DateUtils.DATEFORMAT_STR_011));
        //buryingMapper.insertDynamic("xy_burying_point_month",datas);
        // buryingMapper.insertDynamic("xy_burying_point_day",datas);
    }

    public void setDefaultValue(UserBuryingPointVo userBuryingPointVo){
        //是否播放完成
        if(StringUtils.isNotBlank(userBuryingPointVo.getIsPlayOver())){
            if (is_True.contains(userBuryingPointVo.getIsPlayOver())){
                userBuryingPointVo.setIsPlayOver("1");
            }else if ( is_false.contains(userBuryingPointVo.getIsPlayOver())){
                userBuryingPointVo.setIsPlayOver("0");
            }
        }
        if(StringUtils.isNotBlank(userBuryingPointVo.getIsFullscreen())){
            //是否开启全屏
            if (is_True.contains(userBuryingPointVo.getIsFullscreen())){
                userBuryingPointVo.setIsFullscreen("1");
            }
            if (is_false.contains(userBuryingPointVo.getIsFullscreen())){
                userBuryingPointVo.setIsFullscreen("0");
            }
        }
        if(StringUtils.isNotBlank(userBuryingPointVo.getSetResult())){
            //是否设置成功
            if (is_True.contains(userBuryingPointVo.getSetResult())){
                userBuryingPointVo.setSetResult("1");
            }
            if (is_false.contains(userBuryingPointVo.getSetResult())){
                userBuryingPointVo.setSetResult("0");
            }
        }
        if(StringUtils.isNotBlank(userBuryingPointVo.getIsCollection())){
            //是否合集
            if (is_True.contains(userBuryingPointVo.getIsCollection())){
                userBuryingPointVo.setIsCollection("1");
            }
            if (is_false.contains(userBuryingPointVo.getIsCollection())){
                userBuryingPointVo.setIsCollection("0");
            }
        }
        if(StringUtils.isNotBlank(userBuryingPointVo.getIsDifferentiaAdv())){
            //是否阶梯广告
            if (is_True.contains(userBuryingPointVo.getIsDifferentiaAdv())){
                userBuryingPointVo.setIsDifferentiaAdv("1");
            }
            if (is_false.contains(userBuryingPointVo.getIsDifferentiaAdv())){
                userBuryingPointVo.setIsDifferentiaAdv("0");
            }
        }
    }

    /**
     * 动态保存map数据
     *
     * @param tableName 表名
     * @param data      数据
     */
    @Override
    public void insertMap(String tableName, Map<String, Object> data) {
        buryingMapper.insertDynamic(tableName, data);
    }

    /**
     * 判断表是否有这个字段
     *
     * @param tableName 表名
     * @param column    字段名
     * @return Boolean
     */
    @Override
    public boolean hasColumns(String tableName, String column) {
        final Set<String> columns = tableColumns.get(tableName);
        return columns != null && columns.contains(column);
    }

    public void initTables() {
        final List<UserBuryingPointColumns> columns = this.buryingMapper.getColumns();
        for (UserBuryingPointColumns column : columns) {
            Set<String> columnSet = tableColumns.get(column.getTableName());
            if (columnSet == null) {
                columnSet = new HashSet<>();
            }
            columnSet.add(column.getColumnName());
            tableColumns.put(column.getTableName(), columnSet);
        }
        log.info("初始化读取表结构结果：{}", tableColumns);
    }

    public void initTableIsEmpty() {
        if (tableColumns.isEmpty()) {
            this.initTables();
        }
    }

    @Override
    @Transactional
    public void saveAdvBuryingLog(AdvBuryingLogVO advBuryingLogVO) {
        //2020年5月7日14:29:50  新增mongodb操作
        AdvBuryingLogMongodb advBuryingLogMongodb = new AdvBuryingLogMongodb();
        BeanUtils.copyProperties(advBuryingLogVO,advBuryingLogMongodb);
        mongoTemplate.save(advBuryingLogMongodb,ADV_BURYING_LOG_MONGODB+ DateUtils.parseDateToStr(advBuryingLogVO.getCreateTime(),DateUtils.DATEFORMAT_STR_011));
        advBuryingLogRepository.save(advBuryingLogVO);
    }
}

package com.miguan.burypoint.infrastructure.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.miguan.burypoint.domain.mapper.BuryingMapper;
import com.miguan.burypoint.domain.service.LdBuryingPointActivityService;
import com.miguan.burypoint.domain.vo.LdBuryingPointActivityVo;
import com.miguan.burypoint.mongodb.LdUserBpActivityMongodb;
import com.miguan.burypoint.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenwf
 * @date 2020/5/27
 */
@Service
@Transactional
public class LdBuryingPointActivityServiceImpl implements LdBuryingPointActivityService {
    private static final String LD_USER_BP_ACTIVITY_MONGODB = "ldUserBpMongodb_";

    @Resource
    private BuryingMapper buryingMapper;
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 活动埋点写入
     *
     * @param ldBuryingPointActivityVo
     */
    @Override
    public void insert(LdBuryingPointActivityVo ldBuryingPointActivityVo) {
        final Map<String, Object> datas = new ConcurrentHashMap<>(100);
        String jsonStr = JSONObject.toJSONString(ldBuryingPointActivityVo);
        Map<String,Object> jsonMap = JSONObject.parseObject(jsonStr);
        jsonMap.put("createDate",ldBuryingPointActivityVo.getCreateDate());
        jsonMap.keySet().forEach(e -> datas.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e),jsonMap.get(e)));
        buryingMapper.insertDynamic("ld_burying_point_activity",datas);
        //mongdb
        LdUserBpActivityMongodb ldUserBpActivityMongodb = new LdUserBpActivityMongodb();
        BeanUtils.copyProperties(ldBuryingPointActivityVo,ldUserBpActivityMongodb);
        mongoTemplate.save(ldUserBpActivityMongodb,LD_USER_BP_ACTIVITY_MONGODB+ DateUtils.parseDateToStr(ldBuryingPointActivityVo.getCreateDate(),DateUtils.DATEFORMAT_STR_011));
    }
}

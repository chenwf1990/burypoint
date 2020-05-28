package com.miguan.burypoint.application.mq;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.miguan.burypoint.infrastructure.configure.TargetDataSource;
import com.miguan.burypoint.domain.service.UserBuriedPointService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tool.util.DateUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * 异步处理埋点
 *
 * @author xujinbang
 * @date 2019-11-7
 */
@Slf4j
@Component
public class BuryPointProcessor {
    private static final String QUEUE = "send.burypoint.queue";
    public static final String EXCHANGE = "send.burypoint.exchange";
    public static final String RUTE_KEY = "send.burypoint.rutekey";
    @Resource
    private UserBuriedPointService userBuriedPointService;

    /**
     * Process order.
     *
     * @param jsonMsg the data
     * @param queue   the queue
     */
    @TargetDataSource(name = "stepsDataSource")
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = QUEUE), exchange = @Exchange(value = EXCHANGE, autoDelete = "true"), key = RUTE_KEY)})
    public void processor(String jsonMsg, @Header(AmqpHeaders.CONSUMER_QUEUE) String queue) {
        //log.info("从队列【{}】收到消息：{}", queue, jsonMsg);
        this.userBuriedPointService.initTableIsEmpty();
        try {
            final JSONObject jsonObject = JSONObject.parseObject(jsonMsg);
            String pri = "";
            final String appKey = "appKey";
            if (jsonObject.containsKey(appKey)) {
                pri = jsonObject.getString(appKey) + "_";
            }

            final Set<String> keySet = jsonObject.keySet();
            for (String key : keySet) {
                if (key.equals(appKey))  {
                    continue;
                }
                final String tableName = pri + caseKey(key);
                final JSONObject data = jsonObject.getJSONObject(key);
                userBuriedPointService.insertMap(tableName, caseMap(tableName, data));
            }
        } catch (Exception e) {
            log.error("错误日志：队列【{}】收到消息：{}", queue, jsonMsg);
            log.error(e.getMessage(), e);
        }
    }

    private Map<String, Object> caseMap(String tableName,JSONObject data) {
        final HashMap<String, Object> datas = new HashMap<>();
        final Set<String> set = data.keySet();
        for (String key : set) {
            final String ck = caseKey(key);
            final Object value = data.get(key);
            if (userBuriedPointService.hasColumns(tableName, ck) && value != null && StringUtils.isNotBlank(value.toString())) {
                datas.put(ck, value);
            }
        }
        final String creatTime = data.getString("creat_time");
        if (StringUtils.isBlank(creatTime)) {
            final Date date = new Date();
            datas.put("creat_time", date);
            datas.put("create_date", DateUtil.dateStr2(date));
        } else {
            final Date date = new Date(Long.valueOf(creatTime));
            datas.put("creat_time", date);
            datas.put("create_date", DateUtil.dateStr2(date));
        }
        datas.put("point_type", data.getString("point_type").toLowerCase());
        return datas;
    }
    /**
     * 驼峰转下划线
     * @param key 需要转成下划线的key
     */
    private static String caseKey(String key) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
    }
}

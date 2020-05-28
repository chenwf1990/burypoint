package com.miguan.burypoint.application.mq;

import com.alibaba.fastjson.JSON;
import com.miguan.burypoint.domain.service.UserBuriedPointService;
import com.miguan.burypoint.domain.vo.AdvBuryingLogVO;
import com.miguan.burypoint.domain.vo.UserBuryingPointVo;
import com.miguan.burypoint.infrastructure.configure.TargetDataSource;
import com.miguan.burypoint.infrastructure.constants.BuriedPointConstant;
import com.miguan.burypoint.utils.VersionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 异步处理埋点
 * @author xujinbang
 * @date 2019-11-7
 */
@Slf4j
@Component
public class BuryPointMqProcessor {

    private static final String QUEUE = "xy.new.send.burypoint.queue";
    private static final String EXCHANGE = "xy.new.send.burypoint.exchange";
    private static final String RUTE_KEY = "xy.new.send.burypoint.rutekey";

    private static final String LOG_KEY = "UserBuryingPointVo_";//日志搜索关键字

    @Resource
    private UserBuriedPointService userBuriedPointService;

    /**
     * Process order.
     *
     * @param jsonMsg  the data
     * @param queue the queue
     */
    @TargetDataSource(name = "xyDataSource")
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = QUEUE), exchange = @Exchange(value = EXCHANGE, autoDelete = "true"),
                    key = RUTE_KEY)
    })
    public void processor(String jsonMsg, @Header(AmqpHeaders.CONSUMER_QUEUE) String queue) {
        try{
            if (StringUtils.isNotBlank(jsonMsg) && jsonMsg.contains(BuriedPointConstant._MQ_)) {
                String[] msg_arrA = jsonMsg.split(BuriedPointConstant._MQ_);
                String flag = msg_arrA[0];
                String jsonObject = msg_arrA[1];
                if(msg_arrA.length > 2){
                    jsonObject = buffer(msg_arrA);
                }
                UserBuryingPointVo userBuryingPointVo = JSON.parseObject(jsonObject, UserBuryingPointVo.class);
                String deviceId = userBuryingPointVo.getDeviceId();
                if(StringUtils.isBlank(deviceId)) {
                    log.error(LOG_KEY+"没有DeviceId"+jsonMsg);
                } else if (deviceId.length() > 254){
                    log.error(LOG_KEY+"DeviceId长度大于255的数据："+jsonMsg);
                } else if(StringUtils.isNotBlank(userBuryingPointVo.getOtherSource()) && userBuryingPointVo.getOtherSource().length() > 4){
                    log.error(LOG_KEY+"OtherSource长度大于5的数据" + jsonMsg);
                } else{
                    userBuriedPointService.insert(userBuryingPointVo,flag);
                }
            } else {
                log.error(LOG_KEY+"数据异常:"+jsonMsg);
            }
        }catch (Exception e){
            log.error(LOG_KEY+"jsonMsg",jsonMsg);
            log.error(e.getMessage(),e);
        }
    }

    public static String buffer(String[] msg_arrA) {
        StringBuffer sb = new StringBuffer("");
        for(int i = 1;i<msg_arrA.length;i++){
            sb.append(msg_arrA[i]).append(BuriedPointConstant._MQ_);
        }
        String buffer = sb.toString();
        return buffer.substring(0,buffer.length()-1);
    }

}

package com.miguan.burypoint.mongodb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
@Document(collection="advBuryingLogMongodb")
public class AdvBuryingLogMongodb {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "app_package")
    private String appPackage;

    //2020年4月24日17:31:28   广告埋点
    @ApiModelProperty("展示类型 0广点通 1穿山甲 2:98类型")
    @Column(name = "plat_key")
    private String platKey;

    @ApiModelProperty("广告位Id")
    @Column(name = "position_id")
    private String positionId;

    @ApiModelProperty("广告代码位")
    @Column(name = "ad_id")
    private String adId;

    @ApiModelProperty("广告类型：全屏，banner")
    @Column(name = "type_key")
    private String typeKey;

    @ApiModelProperty("手机类型 1ios 2Android")
    @Column(name = "mobile_type")
    private String mobileType;

    @ApiModelProperty("SDK版本")
    @Column(name = "ad_sdk")
    private String adSdk;

    @ApiModelProperty("成功失败")
    @Column(name = "state")
    private String state;

    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

}

package com.miguan.burypoint.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "xy_burying_point_month")
public class UserBuryingPointMonth{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private String userId;

    private String deviceId;

    private String appVersion;

    private String osVersion;

    private Date creatTime;

    private String actionId;

    private int isNew;

    private String channelId;

    private int isFirstView;

    private int isFirstLoad;

    private int resumeFromBack;

    private int iconType;

    /**
     * 短视频播放：1.首次加载,2.刷新首页，
     * 短视频详情点击：10-主页（或列表）点击，20-详情内推荐，30-观看历史点击，40-我的收藏点击
     * 小视频播放: 1-小视频列表,2-详情切换,3-观看历史,4-我的收藏
     * */
    private int source;

    private Long catid;

    private Long videoId;

    private int shareWay;

    private String picId;

    private String videoPlayTime;

    private String videoTime;

    private Double videoRate;

    private String systemVersion;

    private String createDate;

    private String adZoneId;

}
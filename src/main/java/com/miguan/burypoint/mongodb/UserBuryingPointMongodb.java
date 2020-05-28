package com.miguan.burypoint.mongodb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection="userBuryingPointMongodb")
public class UserBuryingPointMongodb {

  @Id
  private String id;

  private String phone;

  private String userId;

  private String deviceId;

  private String appVersion;

  private String osVersion;

  private Date creatTime;

  private String actionId;

  private String channelId;

  private int isFirstView;

  private int isFirstLoad;

  private int resumeFromBack;

  private int iconType;

  /**
   * 短视频播放：1.首次加载,2.刷新首页， 短视频详情点击：10-主页（或列表）点击，20-详情内推荐，30-观看历史点击，40-我的收藏点击 小视频播放:
   * 1-小视频列表,2-详情切换,3-观看历史,4-我的收藏
   */
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

  private String fileName;

  private String adZoneId;

  private int isNew = 20;

  private String imei;
  // 包名
  private String appPackage;

  // HYL 2020年3月10日15:03:29  新增西柚视频埋点2.2.0

  // 是否完整播放
  private String isPlayOver;

  // 是否展开全屏
  private String isFullscreen;

  // 合集ID
  private String videoCollectionId;

  // 合集名称
  private String videoCollectionName;

  // 合集点击来源
  //private String playSource;

  // 提示弹窗点击
  private String clickResult;

  // 是否设置成功
  private String setResult;

  // 广告id
  private String xId;

  // 广告展示是否成功  0：成功  1：失败
  private String state;

  //2020年4月20日10:10:41  HYL  新增V2.5.0  埋点
  //是否合集
  private String isCollection;

  //具体来源
  private String otherSource;

  //是否是阶梯广告
  private String isDifferentiaAdv;

  @ApiModelProperty("视频类型 10-短视频；20-小视频")
  private Integer type;

}

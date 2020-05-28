package com.miguan.burypoint.domain.service;

import com.miguan.burypoint.domain.vo.LdBuryingPointActivityVo;

/**
 * @author chenwf
 * @date 2020/5/27
 */
public interface LdBuryingPointActivityService {
    /**
     * 活动埋点写入
     * @param ldBuryingPointActivityVo
     */
    void insert(LdBuryingPointActivityVo ldBuryingPointActivityVo);
}

package com.miguan.burypoint.infrastructure.service;

import com.miguan.burypoint.domain.repositories.AdvBuryingLogRepository;
import com.miguan.burypoint.domain.service.AdvBuryingLogService;
import com.miguan.burypoint.domain.vo.AdvBuryingLogVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdvBuryingLogServiceImpl implements AdvBuryingLogService {

    @Resource
    AdvBuryingLogRepository advBuryingLogRepository;

    @Override
    public void save(AdvBuryingLogVO advBuryingLogVO) {
        advBuryingLogRepository.save(advBuryingLogVO);
    }
}

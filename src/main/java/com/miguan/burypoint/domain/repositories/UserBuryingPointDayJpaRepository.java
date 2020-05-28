package com.miguan.burypoint.domain.repositories;

import com.miguan.burypoint.domain.entity.UserBuryingPointDay;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserBuryingPointDayJpaRepository extends JpaRepository<UserBuryingPointDay, Long> {
    
}

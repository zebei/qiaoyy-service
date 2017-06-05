package com.qiaoyy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiaoyy.model.GameStoneLog;


@Repository
public interface GameStoneRepository extends JpaRepository<GameStoneLog, Long> {
    
}

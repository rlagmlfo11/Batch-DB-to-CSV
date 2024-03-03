package com.sample.project.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sample.project.entity.MovedPlayer;

public interface MovedPlayerRepository extends JpaRepository<MovedPlayer, Long> {

}

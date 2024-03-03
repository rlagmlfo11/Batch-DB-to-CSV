package com.sample.project.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sample.project.entity.MovedPlayer;
import com.sample.project.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

	@Query("SELECT MIN(p.receivedDate) FROM Player p")
	String findOldDate();

	@Query("SELECT MAX(p.receivedDate) FROM Player p")
	String findNewDate();

}

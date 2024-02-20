package com.sample.project.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sample.project.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {

//	@Query("SELECT p.name FROM Player p")
//	List<String> findByNAME();

	@Query("SELECT p.mail FROM Player p WHERE p.name = :name")
	String findMailByMail(@Param("name") String name);

	@Query("SELECT p.wage FROM Player p WHERE p.name = :name")
	String findMailByWage(@Param("name") String name);

//	@Query("SELECT p.mail, p.wage FROM Player p WHERE p.name = :name")
//	Object[] findMailAndWageByName(@Param("name") String name);

}

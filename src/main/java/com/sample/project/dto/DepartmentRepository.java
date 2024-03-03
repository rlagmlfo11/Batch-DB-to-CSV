//package com.sample.project.dto;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import com.sample.project.entity.Department;
//
//public interface DepartmentRepository extends JpaRepository<Department, String> {
//
//	@Query("SELECT d.division FROM Department d WHERE d.code = :code")
//	List<String> findDivisions(@Param("code") String code);
//
//}

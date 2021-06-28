package com.nphc.hr.repository;

import com.nphc.hr.dto.EmployeeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeDto, String> {

    @Query(value = "select u.login from employee u where u.id != :id", nativeQuery = true)
    List<String> getLoginExpIdList(@Param("id") String id);

    @Query(value = "select u.id from employee u", nativeQuery = true)
    List<String> getIdList();

    @Query(value = "select * from employee u where u.salary >= :minSalary and u.salary < :maxSalary order by u.id", nativeQuery = true)
    List<EmployeeDto> getFetchList(@Param("minSalary") double minSalary, @Param("maxSalary") double maxSalary);

}

package com.sau.hadoopassignment.Repositories;

import com.sau.hadoopassignment.DTOs.EmployeeDTO;
import com.sau.hadoopassignment.Entites.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employees, Integer> {
    @Query("SELECT new com.sau.hadoopassignment.DTOs.EmployeeDTO(e.empno, e.ename, e.job, " +
            "COALESCE(e.manager.ename, null), " +
            "e.hiredate, e.sal, e.comm, d.dname, e.img) " +
            "FROM Employees e " +
            "LEFT JOIN e.department d " +
            "LEFT JOIN e.manager m")
    List<EmployeeDTO> findAllEmployeesWithDetails();

    Employees findByEname(String ename);
}
package com.sau.hadoopassignment.Repositories;

import com.sau.hadoopassignment.Entites.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Integer> {

    Departments findByDname(String dname); // Custom method to find department by name

    // Custom method to retrieve all department names
}

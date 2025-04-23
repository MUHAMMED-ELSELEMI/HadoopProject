package com.sau.hadoopassignment.Services;

import com.sau.hadoopassignment.DTOs.EmployeeDTO;
import com.sau.hadoopassignment.Entites.Departments;
import com.sau.hadoopassignment.Repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    public List<Departments> getAll() {
        List<Departments> departments = departmentRepository.findAll();
        // Optionally set full image path if needed
        return departments;
    }
}

package com.sau.hadoopassignment.Controllers;

import com.sau.hadoopassignment.Entites.Departments;
import com.sau.hadoopassignment.Repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public List<Departments> getAllDepartments() {
        return departmentRepository.findAll();
    }
}

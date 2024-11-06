package com.sau.hadoopassignment.Controllers;
import com.sau.hadoopassignment.DTOs.EmployeeDTO;
import com.sau.hadoopassignment.Services.EmployeeService;
import com.sau.hadoopassignment.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{empno}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Integer empno) {
        Optional<EmployeeDTO> employee = employeeService.getEmployeeById(empno);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        ResponseEntity<EmployeeDTO> response;

        try {
            EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
            response = ResponseEntity.ok(createdEmployee);
        } catch (Exception exception) {
            response = ResponseEntity.badRequest().body(null);
        }

        return response;
    }

    @PutMapping("/{empno}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer empno, @RequestBody EmployeeDTO employeeDTO) {
        try {
            Optional<EmployeeDTO> updatedEmployee = employeeService.updateEmployee(empno, employeeDTO);
            return updatedEmployee.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{empno}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer empno) {
        employeeService.deleteEmployee(empno);
        return ResponseEntity.noContent().build();
    }
}


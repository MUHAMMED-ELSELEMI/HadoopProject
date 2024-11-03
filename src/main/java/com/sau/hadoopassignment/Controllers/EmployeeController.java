package com.sau.hadoopassignment.Controllers;

import com.sau.hadoopassignment.DTOs.EmployeeDTO;
import com.sau.hadoopassignment.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.ok(createdEmployee);
    }
    @PutMapping("/{empno}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer empno, @RequestBody EmployeeDTO employeeDTO) {
        try {
            Optional<EmployeeDTO> updatedEmployee = employeeService.updateEmployee(empno, employeeDTO);
            return updatedEmployee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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

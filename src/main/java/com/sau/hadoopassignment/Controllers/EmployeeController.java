package com.sau.hadoopassignment.Controllers;
import com.sau.hadoopassignment.DTOs.EmployeeDTO;
import com.sau.hadoopassignment.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Show Employee List
    @GetMapping
    public String showEmployeeList(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "index"; // This maps to index.html
    }

    // Show Add Employee Form
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        return "add"; // This maps to add.html
    }

    // Handle Add Employee Form Submission
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employee") EmployeeDTO employeeDTO, @RequestParam("file") MultipartFile file) throws IOException {
        // Handle image upload
        String imageName = employeeService.uploadEmployeeImage(file);
        employeeDTO.setImg(imageName); // Set the image name in employeeDTO
        employeeService.createEmployee(employeeDTO); // Save employee
        return "redirect:/employees"; // Redirect back to the employee list
    }

    // Show Edit Employee Form
    @GetMapping("/edit/{empno}")
    public String showEditEmployeeForm(@PathVariable("empno") Integer empno, Model model) {
        EmployeeDTO employee = employeeService.getEmployeeById(empno).orElseThrow(() -> new RuntimeException("Employee not found"));
        model.addAttribute("employee", employee);
        return "edit"; // This maps to edit.html
    }

    // Handle Edit Employee Form Submission
    @PostMapping("/edit/{empno}")
    public String editEmployee(@PathVariable("empno") Integer empno, @ModelAttribute("employee") EmployeeDTO employeeDTO, @RequestParam("file") MultipartFile file) throws IOException {
        // Handle image upload
        if (!file.isEmpty()) {
            String imageName = employeeService.uploadEmployeeImage(file);
            employeeDTO.setImg(imageName); // Set the new image name if provided
        }
        employeeService.updateEmployee(empno, employeeDTO); // Update employee
        return "redirect:/employees"; // Redirect back to the employee list
    }

    // Delete Employee
    @PostMapping("/delete/{empno}")
    public String deleteEmployee(@PathVariable("empno") Integer empno) {
        employeeService.deleteEmployee(empno); // Delete employee
        return "redirect:/employees"; // Redirect back to the employee list
    }
}

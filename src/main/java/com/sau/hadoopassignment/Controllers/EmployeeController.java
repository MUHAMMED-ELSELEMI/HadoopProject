package com.sau.hadoopassignment.Controllers;

import com.sau.hadoopassignment.DTOs.EmployeeDTO;
import com.sau.hadoopassignment.Services.DepartmentService;
import com.sau.hadoopassignment.Services.EmployeeService;
import com.sau.hadoopassignment.Services.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ImageService imageService;


    // Show Employee List
    @GetMapping
    public String showEmployeeList(Model model) {
        // Get the list of employees
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "index"; // This maps to index.html
    }

    // Show Add Employee Form
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("departments", departmentService.getAll()); // Pass departments list

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
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("employee", employee);
        return "edit"; // This maps to edit.html
    }

    // Handle Edit Employee Form Submission
    @PostMapping("/edit/{empno}")
    public String editEmployee(
            @PathVariable("empno") Integer empno,
            @ModelAttribute("employee") EmployeeDTO employeeDTO,
            @RequestParam("file") MultipartFile file) throws IOException {

        // Check if a new image is uploaded
        if (!file.isEmpty()) {
            // Retrieve the current employee to get the old image name
            Optional<EmployeeDTO> existingEmployee = employeeService.getEmployeeById(empno);
            if (existingEmployee.isPresent()) {
                String oldImageName = existingEmployee.get().getImg();

                // Delete the old image if it exists
                imageService.deleteImage(oldImageName);

                // Upload the new image
                String newImageName = employeeService.uploadEmployeeImage(file);
                employeeDTO.setImg(newImageName); // Set the new image name
            } else {
                throw new RuntimeException("Employee not found");
            }
        } else {
            // Preserve the current image if no new image is uploaded
            Optional<EmployeeDTO> existingEmployee = employeeService.getEmployeeById(empno);
            if (existingEmployee.isPresent()) {
                employeeDTO.setImg(existingEmployee.get().getImg());
            } else {
                throw new RuntimeException("Employee not found");
            }
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

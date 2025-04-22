package com.sau.hadoopassignment.Services;

import com.sau.hadoopassignment.DTOs.EmployeeDTO;
import com.sau.hadoopassignment.Entites.Departments;
import com.sau.hadoopassignment.Entites.Employees;
import com.sau.hadoopassignment.Repositories.DepartmentRepository;
import com.sau.hadoopassignment.Repositories.EmployeeRepository;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import com.sau.hadoopassignment.SparkConfig.SparkConfig;
import static org.apache.spark.sql.functions.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.spark.sql.Dataset;
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ImageService hdfsService;  // Service to handle HDFS interactions

    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> employees = employeeRepository.findAllEmployeesWithDetails();

        // Fetch total expenses from Spark
        Map<Integer, Double> expenseMap = fetchTotalExpenses();

        employees = employees.stream()
                .filter(emp -> emp.getEmpno() != null)
                .peek(emp -> emp.setTotal_expense(expenseMap.getOrDefault(emp.getEmpno(), 0.0)))
                .collect(Collectors.toList());


        return employees;
    }
    public Map<Integer, Double> fetchTotalExpenses() {
        try {
            SparkSession spark = SparkConfig.getSparkSession();
            Dataset<Row> expenses = spark.read()
                    .format("org.apache.spark.sql.cassandra")
                    .option("keyspace", "my_keyspace")
                    .option("table", "expenses")
                    .load();

            Dataset<Row> totalExpenses = expenses.groupBy("empno")
                    .agg(sum("payment").alias("total_expense"));

            return totalExpenses.collectAsList().stream()
                    .collect(Collectors.toMap(
                            row -> row.getInt(0),
                            row -> row.getDouble(1)
                    ));
        } catch (Exception e) {
            // Log the error and return an empty map
            System.err.println("Failed to fetch expenses: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public Optional<EmployeeDTO> getEmployeeById(Integer empno) {
        return employeeRepository.findById(empno)
                .map(this::convertToDTO)
                .map(emp -> {
                    emp.setImg(emp.getImg());
                    return emp;
                });
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) throws IOException {
        Employees employee = convertToEntity(employeeDTO);
        employeeRepository.save(employee);
        return convertToDTO(employee);
    }

    // New method to handle image upload
    public String uploadEmployeeImage(MultipartFile file) throws IOException {
        // Check if the file is provided
        if (file != null && !file.isEmpty()) {
            // Upload image to HDFS and return the image name
            return hdfsService.uploadImage(file);
        }
        throw new IllegalArgumentException("File must not be null or empty");
    }

    public void updateEmployeeImage(Integer empno, String imageName) {
        employeeRepository.findById(empno).ifPresent(existingEmployee -> {
            existingEmployee.setImg(imageName); // Update image name
            employeeRepository.save(existingEmployee); // Save the changes
        });
    }

    public Optional<EmployeeDTO> updateEmployee(Integer empno, EmployeeDTO employeeDTO) throws IOException {
        return employeeRepository.findById(empno).map(existingEmployee -> {
            // Update existing employee fields
            existingEmployee.setEname(employeeDTO.getEname());
            existingEmployee.setJob(employeeDTO.getJob());
            existingEmployee.setHiredate(employeeDTO.getHiredate());
            existingEmployee.setSal(employeeDTO.getSal());
            existingEmployee.setComm(employeeDTO.getComm());
            existingEmployee.setImg(employeeDTO.getImg());
            // Set department by name
            if (employeeDTO.getDept() != null) {
                Departments department = departmentRepository.findByDname(employeeDTO.getDept());
                if (department != null) {
                    existingEmployee.setDepartment(department);
                } else {
                    throw new RuntimeException("Department not found");
                }
            }

            // Set manager by name if exists
            if (employeeDTO.getMgr() != null) {
                Employees manager = employeeRepository.findByEname(employeeDTO.getMgr());
                if (manager != null) {
                    existingEmployee.setManager(manager);
                } else {
                    throw new RuntimeException("Manager not found");
                }
            }

            employeeRepository.save(existingEmployee);
            return convertToDTO(existingEmployee);
        });
    }
    // New method to handle image upload


    public void deleteEmployee(Integer empno) {
        employeeRepository.findById(empno).ifPresent(employee -> {
            // Delete the image associated with this employee
            String imageName = employee.getImg();
            if (imageName != null) {
                try {
                    hdfsService.deleteImage(imageName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            // Now delete the employee record
            employeeRepository.delete(employee);
        });
    }

    private EmployeeDTO convertToDTO(Employees employee) {
        EmployeeDTO dto = new EmployeeDTO(
                employee.getEmpno(),
                employee.getEname(),
                employee.getJob(),
                employee.getManager() != null ? employee.getManager().getEname() : null,
                employee.getHiredate(),
                employee.getSal(),
                employee.getComm(),
                employee.getDepartment() != null ? employee.getDepartment().getDname() : null,
                employee.getImg()
        );

        // Do NOT set total_expense here — it's handled in the service layer after Spark fetch
        return dto;
    }


    private Employees convertToEntity(EmployeeDTO employeeDTO) {
        Employees employee = new Employees();
        employee.setEmpno(employeeDTO.getEmpno());
        employee.setEname(employeeDTO.getEname());
        employee.setJob(employeeDTO.getJob());
        employee.setHiredate(employeeDTO.getHiredate());
        employee.setSal(employeeDTO.getSal());
        employee.setComm(employeeDTO.getComm());
        employee.setImg(employeeDTO.getImg()); // Set image path

        if (employeeDTO.getMgr() != null) {
            Employees manager = employeeRepository.findByEname(employeeDTO.getMgr());
            employee.setManager(manager); // Set the found manager
        }

        // Lookup department by name if provided
        if (employeeDTO.getDept() != null) {
            Departments department = departmentRepository.findByDname(employeeDTO.getDept());
            employee.setDepartment(department); // Set the found department
        }

        return employee;
    }
}

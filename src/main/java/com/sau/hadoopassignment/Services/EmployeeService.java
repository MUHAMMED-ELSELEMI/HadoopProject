package com.sau.hadoopassignment.Services;

import com.sau.hadoopassignment.DTOs.EmployeeDTO;
import com.sau.hadoopassignment.Entites.Departments;
import com.sau.hadoopassignment.Entites.Employees;
import com.sau.hadoopassignment.Repositories.DepartmentRepository;
import com.sau.hadoopassignment.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAllEmployeesWithDetails();
    }

    public Optional<EmployeeDTO> getEmployeeById(Integer empno) {
        return employeeRepository.findById(empno).map(this::convertToDTO);
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employees employee = convertToEntity(employeeDTO);
        employeeRepository.save(employee);
        return convertToDTO(employee);
    }

    public Optional<EmployeeDTO> updateEmployee(Integer empno, EmployeeDTO employeeDTO) {
        return employeeRepository.findById(empno).map(existingEmployee -> {
            existingEmployee.setEname(employeeDTO.getEname());
            existingEmployee.setJob(employeeDTO.getJob());
            existingEmployee.setHiredate(employeeDTO.getHiredate());
            existingEmployee.setSal(employeeDTO.getSal());
            existingEmployee.setComm(employeeDTO.getComm());
            existingEmployee.setImg(employeeDTO.getImg());

            // Set department
            Departments department = departmentRepository.findById(Integer.parseInt(employeeDTO.getDept()))
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            existingEmployee.setDepartment(department);

            // Set manager if exists
            if (employeeDTO.getMgr() != null) {
                Employees manager = employeeRepository.findById(Integer.parseInt(employeeDTO.getMgr()))
                        .orElse(null);
                existingEmployee.setManager(manager);
            }

            employeeRepository.save(existingEmployee);
            return convertToDTO(existingEmployee);
        });
    }

    public void deleteEmployee(Integer empno) {
        employeeRepository.deleteById(empno);
    }

    private EmployeeDTO convertToDTO(Employees employee) {
        return new EmployeeDTO(
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
    }

    private Employees convertToEntity(EmployeeDTO employeeDTO) {
        Employees employee = new Employees();
        employee.setEmpno(employeeDTO.getEmpno());
        employee.setEname(employeeDTO.getEname());
        employee.setJob(employeeDTO.getJob());
        employee.setHiredate(employeeDTO.getHiredate());
        employee.setSal(employeeDTO.getSal());
        employee.setComm(employeeDTO.getComm());
        employee.setImg(employeeDTO.getImg());

        // Set department
        Departments department = departmentRepository.findById(Integer.parseInt(employeeDTO.getDept()))
                .orElseThrow(() -> new RuntimeException("Department not found"));
        employee.setDepartment(department);

        // Set manager if exists
        if (employeeDTO.getMgr() != null) {
            Employees manager = employeeRepository.findById(Integer.parseInt(employeeDTO.getMgr()))
                    .orElse(null);
            employee.setManager(manager);
        }

        return employee;
    }
}

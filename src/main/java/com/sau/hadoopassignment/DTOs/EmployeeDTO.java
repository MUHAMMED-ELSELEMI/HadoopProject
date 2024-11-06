package com.sau.hadoopassignment.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDTO {

    private Integer empno;
    private String ename;
    private String job;
    private String mgr;
    private String hiredate;
    private Double sal;
    private Integer comm;
    private String dept;
    private String img;
    private static final String HDFS_BASE_PATH = "localhost:9870/explorer.html#/user/hadoop/images/";

    // Existing constructors, getters, and setters

    // New method to get the full image path
    public EmployeeDTO(Integer empno, String ename, String job, String mgr,
                       String hiredate, Double sal, Integer comm, String dept, String img) {
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hiredate = hiredate;
        this.sal = sal;
        this.comm = comm;
        this.dept = dept;
        this.img = img;
    }

    public String getFullImagePath() {
        return HDFS_BASE_PATH + this.img; // Concatenate the base path with the image name
    }
}

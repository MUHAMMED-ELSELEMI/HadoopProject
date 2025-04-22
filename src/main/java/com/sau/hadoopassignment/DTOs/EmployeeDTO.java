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
    private Double total_expense;
    private String img;
    private static final String HDFS_BASE_PATH = "hdfs://localhost:9000/user/hadoop/images/";


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

    public Double getTotal_expense() {
        return total_expense;
    }

    public void setTotal_expense(Double total_expense) {
        this.total_expense = total_expense;
    }

    public String getFullImagePath() {
        return HDFS_BASE_PATH + this.img;
    }
}

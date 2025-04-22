package com.sau.hadoopassignment.Entites;


import javax.persistence.*;

import lombok.*;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "employees")
public class Employees  {

    @Id
    private Integer empno;

    private String ename;
    private String job;
    @ManyToOne
    @JoinColumn(name = "mgr", referencedColumnName = "empno")
    private Employees manager;

    @OneToMany(mappedBy = "manager")
    private List<Employees> subordinates;


    private String hiredate;
    private Double sal;
    private Integer comm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deptno", nullable = false)
    private Departments department;
    private String img;

    // getters and setters
}

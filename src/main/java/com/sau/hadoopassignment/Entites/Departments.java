package com.sau.hadoopassignment.Entites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "departments")
public class Departments {

    @Id
    private Integer deptno;
    private String dname;
    private String loc;

    @OneToMany(mappedBy = "department")
    private List<Employees> employees;

}

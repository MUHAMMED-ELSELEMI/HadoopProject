package com.sau.hadoopassignment.Entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Departments {

    @Id
    private Integer deptno;
    private String dname;
    private String loc;

    //@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Employees> employees ;

    // getters and setters
}

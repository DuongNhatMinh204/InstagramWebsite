package com.nminh.websiteinstagram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    @Column(name = "phone" ,nullable = false)
    private String phone ;

    @Column(name = "email" ,nullable = false)
    private String email ;

    @Column(name = "password" , nullable = false)
    private String password ;

    @Column(name = "full_name" , nullable = false)
    private String fullName ;

    @Column(name = "date" , nullable = false)
    private Date birthday ;


}

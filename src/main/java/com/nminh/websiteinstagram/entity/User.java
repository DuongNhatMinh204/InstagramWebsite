package com.nminh.websiteinstagram.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nminh.websiteinstagram.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column(name = "nick_name" , nullable = false)
    private String nickName ;

    @Column(name = "date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    @Column(name = "gender")
    private String gender ;

    @Column(name = "avatar_url")
    private String avatarUrl ;

    @Enumerated(EnumType.STRING)
    private Role role ;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date created ;

    // Danh sách người mình đang theo dõi
    @OneToMany(mappedBy = "follower")
    @JsonIgnore
    private List<Follow> following;

    // Danh sách người đang theo dõi mình
    @OneToMany(mappedBy = "following")
    @JsonIgnore
    private List<Follow> followers;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<Message> sentMessage = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Message> receivedMessage = new ArrayList<>();


}

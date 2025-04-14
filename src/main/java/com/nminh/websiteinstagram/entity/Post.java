package com.nminh.websiteinstagram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "content")
    private String content ;

    @Column(name = "image_url")
    private String imageUrl ;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user ;

    @Column(name = "total_like")
    private Integer totalLikes = 0 ;

    @OneToMany(mappedBy = "post")
    private List<Like> likes ;

    @Column(name = "total_cmt")
    private Integer totalComments =0;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments ;
}

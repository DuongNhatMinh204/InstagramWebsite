package com.nminh.websiteinstagram.model.response;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private String nickName ;
    private String content ;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date created_at;

}

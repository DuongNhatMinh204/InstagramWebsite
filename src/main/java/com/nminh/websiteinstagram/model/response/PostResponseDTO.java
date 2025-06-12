package com.nminh.websiteinstagram.model.response;

import com.nminh.websiteinstagram.entity.Comment;
import com.nminh.websiteinstagram.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private Long id ;
    private String url_avatar;
    private String nickname;
    private String content ;
    private String imageUrl ;
    private Integer totalLikes = 0 ;
    private List<LikeResponseDTO> likes ;
    private Integer totalComments = 0 ;
    private List<Comment> comments ;
    private String createdAt;
}

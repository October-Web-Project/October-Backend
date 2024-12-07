package com.october.back.entity;

import com.october.back.error.ErrorCode;
import com.october.back.error.exception.ClientException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseEntity {

    @Column(nullable = false)
    private String name; //사용자 이름(동명이인 있을 수 있고 고유 식별자로 사용 불가능)

    @Column(unique = true, nullable = false)
    private String nickName; // 사용자 닉네임(중복 불가, 고유 식별 가능)

    @Column(unique = true, nullable = false)
    private String email; // OAuth 로그인 시 가져올 사용자 이메일(중복 불가, 고유 식별 가능)

//    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Review> reviewList; //사용자가 쓴 리뷰 목록
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Comment> commentList; //사용자가 쓴 댓글 목

    public void changeNickname(String newNickname) {
        if (newNickname == null || newNickname.trim().isEmpty()) {
            throw new ClientException(ErrorCode.PARAMETER_VALID_EXCEPTION);
        }
        this.nickName = newNickname;
    }

}

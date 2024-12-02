package com.october.back.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Users extends BaseEntity {

    @Column(nullable = false)
    private String name; //사용자 이름(동명이인 있을 수 있고 고유 식별자로 사용 불가능)

    @Column(unique = true, nullable = false)
    private String nickName; // 사용자 닉네임(중복 불가, 고유 식별 가능)

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviewList; //사용자가 쓴 리뷰 목록

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList; //사용자가 쓴 댓글 목록

}

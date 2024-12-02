package com.october.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Image extends BaseEntity {
    @Column(nullable = false)
    private String s3Url;
}

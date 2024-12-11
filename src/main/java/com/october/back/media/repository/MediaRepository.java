package com.october.back.media.repository;

import com.october.back.media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
}

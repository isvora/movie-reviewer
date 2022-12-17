package com.isvora.moviereviewer.repositories;

import com.isvora.moviereviewer.database.MovieEntity;
import com.isvora.moviereviewer.database.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findByMovieEntityAndPlatform(MovieEntity movieEntity, String platform);

    List<ReviewEntity> findAllByMovieEntity(MovieEntity movieEntity);
}

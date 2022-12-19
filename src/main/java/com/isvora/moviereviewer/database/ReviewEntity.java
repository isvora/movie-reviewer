package com.isvora.moviereviewer.database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "score")
    private double score;

    @Column(name = "platform")
    private String platform;

    @Column(name = "source")
    private String source;

    @ManyToOne
    @JoinColumn(name = "reviews", nullable = false)
    private MovieEntity movieEntity;

    public ReviewEntity() {

    }

    public ReviewEntity(double score, String platform, String source, MovieEntity movieEntity) {
        this.score = score;
        this.platform = platform;
        this.source = source;
        this.movieEntity = movieEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public MovieEntity getMovieEntity() {
        return movieEntity;
    }

    public void setMovieEntity(MovieEntity movieEntity) {
        this.movieEntity = movieEntity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewEntity that = (ReviewEntity) o;
        return Double.compare(that.score, score) == 0 && Objects.equals(id, that.id) && Objects.equals(platform, that.platform) && Objects.equals(source, that.source) && Objects.equals(movieEntity, that.movieEntity);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return super.hashCode();
        }
    }

    @Override
    public String toString() {
        return "ReviewEntity{"
                + "id = " + id
                + ", score = " + score
                + ", platform = '" + platform + '\''
                + ", source = '" + source + '\''
                + '}';
    }
}

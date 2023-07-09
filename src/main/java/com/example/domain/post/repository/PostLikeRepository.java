package com.example.domain.post.repository;

import com.example.domain.post.entity.PostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import static com.example.util.QueryFactory.*;

@RequiredArgsConstructor
@Repository
public class PostLikeRepository {

    private final EntityManager em;

    public PostLike save(PostLike postLike) {
        if(postLike.getPostId() == null) {
            em.persist(postLike);
        }

        throw new UnsupportedOperationException("Timeline은 갱신을 지원하지 않습니다.");
    }

    public Long count(Long postId) {
        return (long) em.createQuery("select pl from PostLike pl where pl.postId = :postId", PostLike.class)
                .setParameter("postId", postId)
                .getResultList().size();
    }

}

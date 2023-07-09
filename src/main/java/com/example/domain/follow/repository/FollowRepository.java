package com.example.domain.follow.repository;

import com.example.domain.follow.entity.Follow;
import static com.example.util.QueryFactory.findAllByA;

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
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FollowRepository {

    private final EntityManager em;

    public List<Follow> findAllByFromMemberId(Long fromMemberId) {
        return em.createQuery("select f from Follow f where f.fromMemberId = :fromMemberId", Follow.class)
                .setParameter("fromMemberId", fromMemberId)
                .getResultList();
    }

    public List<Follow> findAllByToMemberId(Long toMemberId) {
        return em.createQuery("select f from Follow f where f.toMemberId = :toMemberId", Follow.class)
                .setParameter("toMemberId", toMemberId)
                .getResultList();
    }

    public Follow save(Follow follow) {
        if(follow.getId() == null) {
            em.persist(follow);
            return follow;
        }

        throw new UnsupportedOperationException("Follow는 갱신을 지원하지 않습니다.");
    }
}

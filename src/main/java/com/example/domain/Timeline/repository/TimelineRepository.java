package com.example.domain.Timeline.repository;

import com.example.domain.Timeline.domain.Timeline;
import com.example.domain.post.entity.Post;
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

import static com.example.util.QueryFactory.bulkInsertTimeLine;
import static com.example.util.QueryType.*;

@RequiredArgsConstructor
@Repository
public class TimelineRepository {

    private final EntityManager em;
    private static final String TABLE = "TIMELINE";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static RowMapper<Timeline> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Timeline.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .postId(resultSet.getLong("postId"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<Timeline> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size) {
        return em.createQuery("select t from Timeline t where t.memberId = :memberId order by t.id desc LIMIT :size", Timeline.class)
                .setParameter("memberId", memberId)
                .setParameter("size", size)
                .getResultList();
    }

    public List<Timeline> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId, int size) {
        return em.createQuery("select t from Timeline t where t.memberId = :memberId and t.id < :id order by t.id desc LIMIT :size", Timeline.class)
                .setParameter("memberId", memberId)
                .setParameter("id", id)
                .setParameter("size", size)
                .getResultList();
    }

    public Timeline save(Timeline timeline) {
        if(timeline.getId() == null) {
            em.persist(timeline);
            return timeline;
        }

        throw new UnsupportedOperationException("Timeline은 갱신을 지원하지 않습니다.");
    }

    public void bulkInsert(List<Timeline> timelines) {
        String sql = bulkInsertTimeLine(timelines, TABLE);

        SqlParameterSource[] params = timelines.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, params);

    }
}

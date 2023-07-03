package com.example.domain.post.repository;

import com.example.domain.post.dto.DailyPostCount;
import com.example.domain.post.dto.DailyPostCountRequest;
import com.example.domain.post.entity.Post;
import com.example.util.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.util.QueryFactory.*;
import static com.example.util.QueryType.*;

@RequiredArgsConstructor
@Repository
public class PostRepository {

    private static final String TABLE = "POST";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final EntityManager em;

    private final static RowMapper<Post> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Post.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .contents(resultSet.getString("contents"))
            .likes(resultSet.getLong("likes"))
            .version(resultSet.getLong("version"))
            .createdDate(resultSet.getObject("createdDate", LocalDate.class))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    private final static RowMapper<DailyPostCount> DAILY_POST_COUNT_MAPPER = (ResultSet resultSet, int rowNum) -> new DailyPostCount(
            resultSet.getLong("memberId"),
            resultSet.getObject("createdDate", LocalDate.class),
            resultSet.getLong("count")
    );


    public Post save(Post post) {
        if(post.getId() == null) {
            em.persist(post);
        }else {
            em.merge(post);
        }

        return post;
    }

    public void bulkInsert(List<Post> posts) {
        String sql = bulkInsertQuery(TABLE, "memberId, contents, createdDate, createdAt", ":memberId, :contents, :createdDate, :createdAt");

        Query query = em.createQuery(sql, Post.class);

        for(Post post : posts) {
            query.setParameter("memberId", post.getMemberId())
                .setParameter("content", post.getContents())
                .setParameter("createdDate", post.getCreatedDate())
                .setParameter("createdAt", post.getCreatedAt())
                .executeUpdate();
        }

        em.getTransaction().commit();
    }

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {
        return em.createQuery("select dpc.createDate, dpc.memberId, count(dpc.id) " +
                        "from DailyPostCount dpc " +
                        "where dpc.memberId = :memberId " +
                        "and dpc.createdDate between :firstDate and :lastDate group by dpc.memberId, dpc.createdDate", DailyPostCount.class)
                .setParameter("memberId", request.memberId())
                .setParameter("firstDate", request.firstDate())
                .setParameter("lastDate", request.lastDate())
                .getResultList();
    }

    public Long getCount(Long memberId) {
        return (long) em.createQuery("select coun(dpc.id) from DailyPostCount dpc where dpc.memberId = :memberId", DailyPostCount.class)
                .setParameter("memberId", memberId)
                .getResultList().size();
    }

    public Optional<Post> findById(Long postId, Boolean requiredLock) {
        String sql = findAllByA(TABLE, "id", "postId");

        if(requiredLock) {
            sql += " FOR UPDATE";
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("postId", postId);

        Post post = namedParameterJdbcTemplate.queryForObject(sql, params, ROW_MAPPER);

        return Optional.ofNullable(post);
    }

    public List<Post> findAllByInIds(List<Long> ids) {
        if(ids.isEmpty())
            return List.of();

        return em.createQuery("select p from POST p where p.id in (:ids)", Post.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
        List<Post> posts = em.createQuery("select p from Post p where p.memberId = :memberId order by :memberId limit :size offset :offset", Post.class)
                .setParameter("memberId", memberId)
                .setParameter("size", pageable.getPageSize())
                .setParameter("offset", pageable.getOffset())
                .getResultList();

        return new PageImpl<>(posts, pageable, getCount(memberId));
    }

    public List<Post> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size) {
        return em.createQuery("select p from Post p where p.memberId = :memberId order by :id desc limit :size", Post.class)
                .setParameter("memberId", memberId)
                .setParameter("size", size)
                .getResultList();
    }

    public List<Post> findAllByInMemberIdAndOrderByIdDesc(List<Long> memberIds, int size) {
        if(memberIds.isEmpty()) {
            return List.of();
        }

        return em.createQuery("select p from Post p where p.memberId in (:memberIds) order by :id desc limit :size", Post.class)
                .setParameter("memberIds", memberIds)
                .setParameter("size", size)
                .getResultList();
    }

    public List<Post> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId, int size) {
        return em.createQuery("select p from Post p where p.memberId = :memberId and p.id < :id order by :id desc limit :size", Post.class)
                .setParameter("memberId", memberId)
                .setParameter("id", id)
                .setParameter("size", size)
                .getResultList();
    }

    public List<Post> findAllByLessThanIdAndInMemberIdsAndOrderByIdDesc(Long id, List<Long> memberIds, int size) {
        if(memberIds.isEmpty()) {
            return List.of();
        }

        return em.createQuery("select p from Post p where p.memberId in (:memberIds) and p.id < :id order by :id limit :size", Post.class)
                .setParameter("memberIds", memberIds)
                .setParameter("id", id)
                .setParameter("size", size)
                .getResultList();
    }
}

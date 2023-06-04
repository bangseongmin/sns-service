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

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PostRepository {

    private static final String TABLE = "POST";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
            return insert(post);
        }

        return update(post);
    }

    public void bulkInsert(List<Post> posts) {
        String sql = String.format("""
                INSERT INTO `%s` (memberId, contents, createdDate, createdAt)
                VALUES (:memberId, :contents, :createdDate, :createdAt)
                """, TABLE);

        SqlParameterSource[] params = posts.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, params);

    }

    private Post insert(Post post) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private Post update(Post post) {
        String sql = String.format("""
                UPDATE %s
                SET memberId = :memberId,
                    contents = :contents,
                    likes = :likes,
                    version = :version + 1,
                    createdDate = :createdDate,
                    createdAt = :createdAt
                WHERE id = :id AND version = :version
                """, TABLE);

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        int updatedRowCount = namedParameterJdbcTemplate.update(sql, params);
        System.out.println("업데이트 Row 수: " +  updatedRowCount);

        if(updatedRowCount == 0) {
            throw new RuntimeException("갱신 실패");
        }

        return post;
    }

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {
        String sql = String.format("""
                SELECT createdDate, memberId, count(id) as count
                FROM %s
                WHERE memberId = :memberId and createdDate between :firstDate and :lastDate
                GROUP BY memberId, createdDate
                """, TABLE);

        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(request);

        return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_MAPPER);
    }

    public Long getCount(Long memberId) {
        String sql = String.format("""
                SELECT count(id)
                FROM %s
                WHERE memberId = :memberId
                """, TABLE);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public Optional<Post> findById(Long postId, Boolean requiredLock) {
        String sql = String.format("SELECT * FROM %s WHERE id = :postId", TABLE);

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

        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE id in (:ids)
                """, TABLE);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ids", ids);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY %s
                LIMIT :size
                OFFSET :offset
                """, TABLE, PageHelper.orderBy(pageable.getSort()));

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        List<Post> posts = namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);

        return new PageImpl<Post>(posts, pageable, getCount(memberId));
    }

    public List<Post> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size) {
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY id desc
                LIMIT :size
                """, TABLE);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByInMemberIdAndOrderByIdDesc(List<Long> memberIds, int size) {
        if(memberIds.isEmpty()) {
            return List.of();
        }

        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId in (:memberIds)
                ORDER BY id desc
                LIMIT :size
                """, TABLE);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId, int size) {
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId and id < :id
                ORDER BY id desc
                LIMIT :size
                """, TABLE);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByLessThanIdAndInMemberIdsAndOrderByIdDesc(Long id, List<Long> memberIds, int size) {
        if(memberIds.isEmpty()) {
            return List.of();
        }

        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId in (:memberIds) and id < :id
                ORDER BY id desc
                LIMIT :size
                """, TABLE);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }
}

package com.example.domain.member.repository;

import com.example.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String TABLE = "member";

    public Optional<Member> findById(Long id) {
        /**
         * SELECT *
         * FROM Member
         * WHERE id = :id
         */

        String sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
        MapSqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

        RowMapper<Member> rowMapper = (ResultSet resultSet, int rowNum) -> Member.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .nickname(resultSet.getString("nickname"))
                .birthday(resultSet.getObject("birthday", LocalDate.class))
                .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
                .build();

        Member member = namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(member);
    }

    public Member save(Member member) {
        /**
         * member id를 보고 갱신 또는 삽입을 정함
         * 반환 값은 id를 담아서 반환한다.
         */
        if(member.getId() == null) {
            return insert(member);
        }

        return update(member);
    }

    private Member insert(Member member) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("Member")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return Member.builder()
                .id(id)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .createdAt(member.getCreatedAt())
                .build();
    }

    private Member update(Member member) {
        // TODO: implemented

        return member;
    }
}
package com.example.domain.member.repository;

import com.example.domain.member.entity.MemberNicknameHistory;
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

import static com.example.util.QueryFactory.findAllByA;
import static com.example.util.QueryType.FIND_ALL_BY_A;

@RequiredArgsConstructor
@Repository
public class MemberNicknameHistoryRepository {

    private final EntityManager em;

    static final RowMapper<MemberNicknameHistory> rowMapper = (ResultSet resultSet, int rowNum) -> MemberNicknameHistory.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .nickname(resultSet.getString("nickname"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<MemberNicknameHistory> findAllByMemberId(Long memberId) {
        return em.createQuery("select mnh from MemberNicknameHistory mnh where mnh.memberId = :memberId", MemberNicknameHistory.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public MemberNicknameHistory save(MemberNicknameHistory history) {
        if(history.getId() == null) {
            em.persist(history);
            return history;
        }else {
            throw new UnsupportedOperationException("MemberNicknameHistoryRepository는 갱신을 지원하지 않습니다.");
        }
    }
}

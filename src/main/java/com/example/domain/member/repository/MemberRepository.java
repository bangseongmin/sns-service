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

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.util.QueryFactory.*;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final EntityManager em;

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAllByIdInIds(List<Long> ids) {
        return em.createQuery("select m from Member m where m.id in (:ids)", Member.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public Member save(Member member) {
        if(member.getId() == null) {
            em.persist(member);
        }else {
            em.merge(member);
        }

        return member;
    }
}

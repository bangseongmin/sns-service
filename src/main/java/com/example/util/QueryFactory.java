package com.example.util;

import com.example.domain.Timeline.domain.Timeline;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.util.QueryType.*;

@RequiredArgsConstructor
public class QueryFactory {

    public static String findAllByA(String table, String from, String to) {
        return String.format(FIND_ALL_BY_A.getQuery(), table, from, to);
    }

    public static String findAllByAInList(String table, String A, String list) {
        return String.format(FIND_ALL_BY_IN_LIST.getQuery(), table, A, list);
    }

    public static String updateMember(String table) {
        return String.format(UPDATE_MEMBER.getQuery(), table);
    }

    public static String countByA(String table, String A, String B) {
        return String.format(COUNT.getQuery(), table, A, B);
    }

    public static String bulkInsertQuery(String table, String from, String to) {
        return String.format(BULK_INSERT.getQuery(), table, from, to);
    }

    public static String updatePost(String table) {
        return String.format(UPDATE_POST.getQuery(), table);
    }

    public static String findByAAndB_BetweenCAndD(String table) {
        return String.format(GROUP_CREATED_DATE.getQuery(), table);
    }

    public static String findAllByA(String table, Pageable pageable) {
        return String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY %s
                LIMIT :size
                OFFSET :offset
                """, table, PageHelper.orderBy(pageable.getSort()));
    }

    public static String findAllByAAndOrderByBOrderDir(String table, String A, String B, String C, String D) {
        return String.format(FIND_ALL_BY_A_AND_ORDER_BY_B_DESC_HAS_LIMIT.getQuery(), table, A, B, C, D);
    }

    public static String findAllByInAAndOrderByBOrderDir(String table, String A, String B, String C, String D) {
        return String.format(FIND_ALL_BY_A_AND_ORDER_BY_B_DESC_HAS_LIMIT.getQuery(), table, A, B, C, D);
    }

    public static String findAllByLessThanAAndBAndOrderByCDesc(String table, String fromA, String toA, String fromB, String toB, String C, String D) {
        return String.format(FIND_ALL_BY_LESS_THAN_A_AND_B_AND_ORDER_BY_C_DESC.getQuery(), table, fromA, toA, fromB, toB, C, D);
    }

    public static String findAllByLessThanAAndInBListAndOrderByCDesc(String table, String fromA, String toA, String B, String toB, String C, String D) {
        return String.format(FIND_ALL_BY_LESS_THAN_A_AND_IN_LIST_AND_ORDER_BY_C_DESC.getQuery(),
                table, fromA, toA, B, toB, C, D);
    }

    public static String bulkInsertTimeLine(List<Timeline> timelines, String table) {
        return String.format(BULK_INSERT.getQuery(), table, "memberId, postId, createdAt", ":memberId, :postId, :createdAt");
    }
}

package com.example.util;

import lombok.Getter;

public enum QueryType {
    FIND_ALL_BY_A("SELECT * FROM %s WHERE %s = :%s"),
    FIND_ALL_BY_IN_LIST("SELECT * FROM %s WHERE %s IN (:%s)"),
    FIND_ALL_BY_A_AND_ORDER_BY_B_DESC_HAS_LIMIT("SELECT * FROM %s WHERE %s = :%s ORDER BY %s desc LIMIT :%s"),
    FIND_ALL_BY_IN_LIST_AND_ORDER_BY_B_DESC_HAS_LIMIT("SELECT * FROM %s WHERE %s IN (:%s) ORDER BY %s desc LIMIT :%s"),
    FIND_ALL_BY_LESS_THAN_A_AND_B_AND_ORDER_BY_C_DESC("SELECT * FROM %s WHERE %s = :%s AND %s < :%s ORDER BY %s DESC LIMIT :%s"),
    FIND_ALL_BY_LESS_THAN_A_AND_IN_LIST_AND_ORDER_BY_C_DESC("SELECT * FROM %s WHERE %s IN (:%s) AND %s < :%s ORDER BY %s DESC LIMIT :%s"),
    COUNT("SELECT COUNT(id) FROM %s WHERE %s = :%s"),
    BULK_INSERT("INSERT INTO `%s` (%s) VALUES (%s)"),
    UPDATE_MEMBER("UPDATE %s SET email = :email, nickname = :nickname, birthday = :birthday WHERE id = :id"),
    UPDATE_POST("UPDATE %s SET memberId = :memberId, contents = :contents, likes = :likes, version = :version + 1, createdDate = :createdDate, createdAt = :createdAt WHERE id = :id AND version = :version"),
    GROUP_CREATED_DATE("SELECT createdDate, memberId, count(id) as count FROM %s WHERE memberId = :memberId and createdDate between :firstDate and :lastDate GROUP BY memberId, createdDate")
    ;

    @Getter private final String query;

    QueryType(String query) { this.query = query; }
}

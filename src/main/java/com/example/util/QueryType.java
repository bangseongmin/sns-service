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
    BULK_INSERT("INSERT INTO `%s` (%s) VALUES (%s)")
    ;

    @Getter private final String query;

    QueryType(String query) { this.query = query; }
}

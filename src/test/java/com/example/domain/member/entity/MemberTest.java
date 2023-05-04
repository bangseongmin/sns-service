package com.example.domain.member.entity;

import com.example.domain.member.util.MemberFixtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;


class MemberTest {

    @Test
    void easyRandomEx() {
        LongStream.range(0, 10)
                .mapToObj(MemberFixtureFactory::create)
                .forEach(member -> {
                    System.out.println(member.getNickname());
                });
    }

    @DisplayName("회원은 닉네임을 변경할 수 있다.")
    @Test
    void testChangeName() {
        Member member = MemberFixtureFactory.create();
        String expected = "daniel";

        member.changeNickname(expected);

        Assertions.assertEquals(expected, member.getNickname());
    }

    @DisplayName("회원의 닉네임은 10자를 초과할 수 없다.")
    @Test
    void testNicknameMaxLength() {
        Member member = MemberFixtureFactory.create();
        String expected = "danieldaniel";

        Assertions.assertThrows(IllegalArgumentException.class, () -> member.changeNickname(expected));
    }
}
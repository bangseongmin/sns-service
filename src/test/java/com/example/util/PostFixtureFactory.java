package com.example.util;

import com.example.domain.post.entity.Post;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;

public class PostFixtureFactory {

    /**
     * randomize를 이용하여 memberId를 고정시키고
     * dateRange를 이용해 랜덤 값 삽입
     */
    public static EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lastDate) {
        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        Predicate<Field> memberIdPredicate = named("memberId")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        EasyRandomParameters params = new EasyRandomParameters()
                .excludeField(idPredicate)
                .dateRange(firstDate, lastDate)
                .randomize(memberIdPredicate, () -> memberId);

        return new EasyRandom(params);
    }
}

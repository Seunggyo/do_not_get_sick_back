package com.example.prj2be.utill;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class AppUtil {

    public static String getAgo(LocalDateTime a) {
        LocalDateTime b = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        if (a.isBefore(b.minusYears(1))) {
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.YEARS) + "년 전";
        } else if (a.isBefore(b.minusMonths(1))) {
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.MONTHS) + "달 전";
        } else if (a.isBefore(b.minusDays(1))) {
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.DAYS) + "일 전";
        } else if (a.isBefore(b.minusHours(1))) {
            Duration between = Duration.between(a, b);
            return (between.getSeconds() / 60 / 60) + "시간 전";
        } else if (a.isBefore(b.minusMinutes(1))) {
            Duration between = Duration.between(a, b);
            return (between.getSeconds() / 60) + "분 전";
        } else {
            Duration between = Duration.between(a, b);
            return between.getSeconds() + "초 전";
        }
    }
}

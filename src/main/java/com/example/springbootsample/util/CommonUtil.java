package com.example.springbootsample.util;

import java.util.concurrent.ThreadLocalRandom;

public class CommonUtil {

    public static int generateRandomNumber(int digit) {
        if (digit <= 0) {
            throw new IllegalArgumentException("Digit must be a positive integer.");
        }

        // 범위 계산을 위해 10의 거듭제곱을 구함
        int minBound = (int) Math.pow(10, digit - 1);
        int maxBound = (int) Math.pow(10, digit);

        // ThreadLocalRandom을 사용하여 랜덤 객체 생성
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // 범위 내의 랜덤 숫자 생성
        return random.nextInt(minBound, maxBound);
    }
}

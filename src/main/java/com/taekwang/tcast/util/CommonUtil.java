package com.taekwang.tcast.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taekwang.tcast.model.dto.ErrorResponse;
import com.taekwang.tcast.model.enums.UserError;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    // UserError 를 json 형태로 response
    public static void printErrorMessage(HttpServletResponse response, @NonNull UserError userError) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        int statusCode;

        if (userError.getHttpStatus() != null) {
            statusCode = userError.getHttpStatus().value();
        } else {
            statusCode = HttpStatus.UNAUTHORIZED.value();
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.of(userError);

        String errorJson = objectMapper.writeValueAsString(errorResponse);
        PrintWriter writer = response.getWriter();
        writer.println(errorJson);
    }

    // 10 ~ 12자리 랜덤 비밀번호 생성
    // 영어소문자, 대문자, 숫자, 특수문자(1~2개)
    public static String generatePassword() {
        final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String NUMBERS = "0123456789";
        final String SPECIALCHARS = "!@#$%&";
        final SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();
        List<String> charsPool = new ArrayList<>();

        // 각 카테고리에서 임의의 문자 추가
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        password.append(SPECIALCHARS.charAt(random.nextInt(SPECIALCHARS.length())));

        // 추가적인 특수 문자를 0~1개 무작위로 추가
        for (int i = 0; i < random.nextInt(2); i++) {
            password.append(SPECIALCHARS.charAt(random.nextInt(SPECIALCHARS.length())));
        }

        // 모든 문자 풀을 결합하여 나머지 비밀번호 길이를 채움
        charsPool.add(LOWERCASE);
        charsPool.add(UPPERCASE);
        charsPool.add(NUMBERS);
        charsPool.add(SPECIALCHARS);

        // 10~12자리 중 남은 길이를 계산하여 랜덤 문자로 채움
        int remainingLength = 10 + random.nextInt(3) - password.length();
        for (int i = 0; i < remainingLength; i++) {
            String selectedPool = charsPool.get(random.nextInt(charsPool.size()));
            password.append(selectedPool.charAt(random.nextInt(selectedPool.length())));
        }

        // 비밀번호 문자 위치를 무작위로 섞음
        List<Character> passwordChars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            passwordChars.add(c);
        }
        Collections.shuffle(passwordChars);

        // 최종 비밀번호 생성
        StringBuilder finalPassword = new StringBuilder();
        for (char c : passwordChars) {
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }
}

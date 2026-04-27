package com.pknu26.studygroup.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pknu26.studygroup.security.CustomUserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    
    private final SecretKey secretKey;
    private final long accessTokenExpiration;

    public JwtProvider(@Value("${jwt.secret}") String secret,
                       @Value("${jwt.expiration}") long accessTokenExpiration) {
        // jwt.secret에 설정된 평문암호 설정값을 암호화해서 할당.
        // HMAC-SHA(Hash-based Message Authentication Code with SHA)
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String createAccessToken(CustomUserDetail userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpiration); // 현시간부터 1시간 이후까지 만료기간 설정

        return Jwts.builder()      // 빌더 디자인패턴으로 새 객체 생성
                .subject(userDetails.getUsername())          // 토큰 제목지정
                .claim("userId", userDetails.getUserId())    // 사용자 식별자값(PK)
                .claim("name", userDetails.getName())        // 사용자 이름
                .claim("role", userDetails.getRole())        // 사용자 권한
                .issuedAt(now)                               // 토큰 발급시간
                .expiration(expiration)                      // 토큰 만료시간
                .signWith(secretKey)                         // 비밀키로 서명
                .compact();                                  // 토큰 생성 후 문자열로 반환(JSON 문자열)
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // 서명 검증
                .build()
                .parseSignedClaims(token) // 토큰 파싱 서명 검증
                .getPayload(); // 페이로드 형태 데이터 반환
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token); // 데이터 가져와지면
            return true; // 성공
        } catch (Exception e) {
            return false; // 예외 발생하면 실패
        }
    }

    public String getLoginId(String token) {
        return getClaims(token).getSubject(); // 클레임의 제목은 사용자 아이디. .subject(userDetails.getUsername()) 
    }

}

package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class PasswordHasher {
    /**
     * Oracle STANDARD_HASH('값', 'SHA256')와 동일한 결과 생성
     */
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // ✅ 반드시 UTF-8 인코딩 사용
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // ✅ 16진수 대문자로 변환
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘 지원 안 됨", e);
        }
    }

    /**
     * Oracle STANDARD_HASH 검증
     */
    public static boolean verify(String inputPassword, String storedHash) {
        String inputHash = hash(inputPassword);
        return inputHash.equals(storedHash);
    }
}

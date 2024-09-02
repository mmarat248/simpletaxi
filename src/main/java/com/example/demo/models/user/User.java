package com.example.demo.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    private static final int PBKDF2_ITERATIONS = 10000;

    private static final int PBKDF2_SALT_LENGTH = 16;

    private static final SecureRandom RANDOM = new SecureRandom();

    public User(String username, char[] password) {
        this.username = username;
        this.passwordHash = hashPassword(password);
    }

    public boolean checkPassword(char[] password) {
        return validatePassword(password, this.passwordHash);
    }

    private String hashPassword(char[] password) {
        byte[] salt = generateSalt();
        byte[] hash = pbkdf2(password, salt, 64);
        return salt.length + ":" + toBase64(salt) + ":" + toBase64(hash);
    }

    private boolean validatePassword(char[] password, String passwordHash) {
        String[] parts = passwordHash.split(":");
        // int saltLength = Integer.parseInt(parts[0]);
        byte[] salt = fromBase64(parts[1]);
        byte[] hash = fromBase64(parts[2]);
        byte[] testHash = pbkdf2(password, salt, hash.length);
        return MessageDigest.isEqual(hash, testHash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int bytes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, User.PBKDF2_ITERATIONS, bytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Failed to hash password", e);
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[PBKDF2_SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return salt;
    }

    private static String toBase64(byte[] array) {
        return Base64.getEncoder().encodeToString(array);
    }

    private static byte[] fromBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}

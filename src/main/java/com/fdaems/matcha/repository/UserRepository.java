package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.GenderType;
import com.fdaems.matcha.record.SexualPreferenceType;
import com.fdaems.matcha.record.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password_hash"),
            rs.getString("gender") != null ? GenderType.valueOf(rs.getString("gender")) : null,
            rs.getDate("birthday") != null ? rs.getDate("birthday").toLocalDate() : null,
            rs.getString("phone_number"),
            rs.getString("bio"),
            rs.getString("sexual_preference") != null ? SexualPreferenceType.valueOf(rs.getString("sexual_preference")) : null,
            rs.getInt("rating"),
            rs.getTimestamp("last_connection") != null ? rs.getTimestamp("last_connection").toLocalDateTime() : null,
            rs.getBoolean("is_verified"),
            rs.getString("verification_token"),
            rs.getString("reset_token")
    );

    public void save(User user) {
        String sql = """
            INSERT INTO users (
                first_name, last_name, username, email, password_hash,
                gender, birthday, phone_number, bio, sexual_preference
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.update(sql,
                user.firstName(),
                user.lastName(),
                user.username(),
                user.email(),
                user.passwordHash(),
                user.gender() != null ? user.gender().name() : null,
                user.birthday(),
                user.phoneNumber(),
                user.bio(),
                user.sexualPreference() != null ? user.sexualPreference().name() : null
        );
    }

    public Optional<User> findById(Long id){
        String sql = "SELECT * FROM users WHERE id = ?";
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, id));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email){
        String sql = "SELECT * FROM users WHERE email = ?";
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, email));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String username){
        String sql = "SELECT * FROM users WHERE username = ?";
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, username));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public Optional<User> findByVerificationToken(String token){
        String sql = "SELECT * FROM users WHERE verification_token = ?";
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, token));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public Optional<User> findByResetToken(String token){
        String sql = "SELECT * FROM users WHERE reset_token = ?";
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, token));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public void updateLastConnection(Long id, LocalDateTime time){
        String sql = "UPDATE users SET last_connection = ? WHERE id = ?";
        jdbcTemplate.update(sql, time, id);
    }

    public void verifyAccount(Long id){
        String sql = "UPDATE users SET is_verified = true, verification_token = NULL WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updatePassword(Long id, String newHash){
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        jdbcTemplate.update(sql, newHash, id);
    }

    public void updateName(Long id, String firstName, String lastName){
        String sql = "UPDATE users SET first_name = ?, last_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, firstName, lastName, id);
    }

    //TODO: check if this needs to change, if not i can maybe user username as primary key
    public void updateUsername(Long id, String username){
        String sql = "UPDATE users SET username = ? WHERE id = ?";
        jdbcTemplate.update(sql, username, id);
    }

    public void updateProfile(Long id, String bio, GenderType gender, SexualPreferenceType sexualPreference){
        String sql = "UPDATE users SET bio = ?, gender = ?, sexual_preference = ? WHERE id = ?";
        jdbcTemplate.update(sql, bio, gender.name(), sexualPreference.name(), id);
    }

    public void updateEmail(Long id, String email){
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        jdbcTemplate.update(sql, email, id);
    }

    public void updateFameRating(Long id, int amount){
        String sql = "UPDATE users SET rating = rating + ? WHERE id = ?";
        jdbcTemplate.update(sql, amount, id);
    }

}

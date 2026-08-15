package com.ahsen.contactmanagement.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ahsen.contactmanagement.config.JpaAuditingConfig;
import com.ahsen.contactmanagement.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailAndExistsByEmail() {
        userRepository.save(user("one@example.com", null));

        assertThat(userRepository.findByEmail("one@example.com")).isPresent();
        assertThat(userRepository.existsByEmail("one@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("missing@example.com")).isFalse();
    }

    @Test
    void findByPhoneAndExistsByPhone() {
        userRepository.save(user(null, "+15551234567"));

        assertThat(userRepository.findByPhone("+15551234567")).isPresent();
        assertThat(userRepository.existsByPhone("+15551234567")).isTrue();
        assertThat(userRepository.existsByPhone("+15550000000")).isFalse();
    }

    @Test
    void duplicateEmailIsRejected() {
        userRepository.saveAndFlush(user("dup@example.com", null));

        assertThatThrownBy(() -> userRepository.saveAndFlush(user("dup@example.com", "+15557654321")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void duplicatePhoneIsRejected() {
        userRepository.saveAndFlush(user(null, "+15551234567"));

        assertThatThrownBy(() -> userRepository.saveAndFlush(user("other@example.com", "+15551234567")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void timestampsArePopulated() {
        User saved = userRepository.saveAndFlush(user("time@example.com", null));

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    private User user(String email, String phone) {
        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash("$2a$10$hashedpasswordvalueforrepositorytestsxx");
        return user;
    }
}

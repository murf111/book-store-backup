package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.model.User;
import com.epam.rd.autocode.spring.project.repo.UserRepository;
import com.epam.rd.autocode.spring.project.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService{ // Rename class/implements as needed

    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_DURATION_MINUTES = 15;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void loginFailed(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (isLocked(user)) {
                return;
            }

            if (hasLockExpired(user)) {
                user.setFailedAttempts(0);
                user.setLockTime(null);
            }

            // Handle NULL safely. If null, treat as 0.
            int currentAttempts = (user.getFailedAttempts() == null) ? 0 : user.getFailedAttempts();

            int newAttempts = currentAttempts + 1;
            user.setFailedAttempts(newAttempts);

            if (newAttempts >= MAX_ATTEMPTS) {
                user.setLockTime(LocalDateTime.now());
            }
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void loginSucceeded(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            // Handle NULL safely here too
            Integer attempts = user.getFailedAttempts();
            boolean hasAttempts = (attempts != null && attempts > 0);

            if (hasAttempts || user.getLockTime() != null) {
                user.setFailedAttempts(0);
                user.setLockTime(null);
                userRepository.save(user);
            }
        });
    }

    @Override
    public int getRemainingAttempts(String email) {
        return userRepository.findByEmail(email)
                             .map(user -> Math.max(0, MAX_ATTEMPTS - user.getFailedAttempts()))
                             .orElse(MAX_ATTEMPTS);
    }

    @Override
    public long getRemainingLockTime(String email) {
        return userRepository.findByEmail(email)
                             .map(user -> {
                                 if (user.getLockTime() == null) return 0L;
                                 LocalDateTime unlockTime = user.getLockTime().plusMinutes(LOCK_DURATION_MINUTES);
                                 long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), unlockTime);
                                 return minutes > 0 ? minutes : 0L;
                             })
                             .orElse(0L);
    }

    @Override
    public boolean isBlocked(User user) {
        if (user.getLockTime() == null) {
            return false;
        }
        return !hasLockExpired(user);
    }

    private boolean isLocked(User user) {
        return user.getLockTime() != null && !hasLockExpired(user);
    }

    private boolean hasLockExpired(User user) {
        if (user.getLockTime() == null) return false;
        return user.getLockTime().plusMinutes(LOCK_DURATION_MINUTES).isBefore(LocalDateTime.now());
    }
}
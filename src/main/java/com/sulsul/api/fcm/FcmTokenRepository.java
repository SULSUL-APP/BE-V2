package com.sulsul.api.fcm;

import com.sulsul.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    /**
     * 유저의 FcmToken 조회
     *
     * @param user FcmToken을 조회할 유저
     * @return FcmToken 반환
     */
    Optional<FcmToken> findByUser(User user);
}
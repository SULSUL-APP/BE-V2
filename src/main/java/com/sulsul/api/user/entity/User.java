package com.sulsul.api.user.entity;

import com.sulsul.api.common.BaseEntity;
import com.sulsul.api.common.type.DType;
import com.sulsul.api.common.type.EType;
import com.sulsul.api.common.type.LoginType;
import com.sulsul.api.common.type.UType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "유저 이름은 필수 값입니다.")
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(length = 1000)
    private String profileImage; // 프로필 이미지 경로

    @Column(length = 1000)
    private String catchPhrase;

    @Enumerated(EnumType.STRING)
    private UType userType;

    @Enumerated(EnumType.STRING)
    private EType essayType;

    @Enumerated(EnumType.STRING)
    private DType userState;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role userRole;

    public User update(String name, String picture) {
        this.name = name;
        this.profileImage = picture;

        return this;
    }

    public void delete() {
        this.email = "알수없음";
        this.profileImage = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg";
        this.name = "알수없음";
        this.userState = DType.DELETE;
    }

    public void updateUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateUType(UType uType) {
        this.userType = uType;
    }

    public void updateEType(EType eType) {
        this.essayType = eType;
    }

    public boolean isTeacher() {
        return userType.equals(UType.TEACHER);
    }
}
package com.sulsul.api.exception.profile;

import com.sulsul.api.exception.ResourceNotFoundException;

import java.util.Map;

public class ProfileNotFoundException extends ResourceNotFoundException {
    public ProfileNotFoundException(long profileId) {
        super("PROFILE_01", "해당 프로필을 찾을 수 없습니다.", Map.of("profileId", String.valueOf(profileId)));
    }
}

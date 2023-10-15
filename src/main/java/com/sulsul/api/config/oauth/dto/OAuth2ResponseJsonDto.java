package com.sulsul.api.config.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuth2ResponseJsonDto {
    private String name;
    private String email;
    private String isGuest;
}
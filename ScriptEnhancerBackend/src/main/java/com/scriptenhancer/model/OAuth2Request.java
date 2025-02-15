package com.scriptenhancer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class OAuth2Request {

    private final String code;
    private final String codeVerifier;

}
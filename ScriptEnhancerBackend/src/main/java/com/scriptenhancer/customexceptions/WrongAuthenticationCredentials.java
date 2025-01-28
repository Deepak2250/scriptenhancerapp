package com.scriptenhancer.customexceptions;

public class WrongAuthenticationCredentials extends RuntimeException {

    private static final long serialVersionUID = 3489985341011675203L;

    public WrongAuthenticationCredentials() {
        super("Wrong Authetication Credentials");
    }

}

package com.scriptenhancer.customexceptions;

public class AlreadyUsedEmail extends RuntimeException {
    public AlreadyUsedEmail(String message) {
        super(message);
    }

}

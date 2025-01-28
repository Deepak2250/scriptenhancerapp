package com.scriptenhancer.customexceptions;

public class UsersNotFound extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsersNotFound(String message) {
        super(message);
    }

}

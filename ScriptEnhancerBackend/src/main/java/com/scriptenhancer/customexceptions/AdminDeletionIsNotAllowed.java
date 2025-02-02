package com.scriptenhancer.customexceptions;

public class AdminDeletionIsNotAllowed extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdminDeletionIsNotAllowed(String message) {
        super(message);
    }

}

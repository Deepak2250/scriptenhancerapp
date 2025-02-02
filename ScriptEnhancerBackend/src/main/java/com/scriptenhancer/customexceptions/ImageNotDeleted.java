package com.scriptenhancer.customexceptions;

public class ImageNotDeleted extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageNotDeleted(String message) {
        super(message);
    }

}

package com.scriptenhancer.customexceptions;

public class ImageNotFound extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageNotFound(String message) {
        super(message);
    }

}

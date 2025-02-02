package com.scriptenhancer.customexceptions;

public class ImageNotSaved extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageNotSaved(String message) {
        super(message);
    }

}

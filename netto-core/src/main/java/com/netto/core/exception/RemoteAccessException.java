package com.netto.core.exception;

public class RemoteAccessException extends RuntimeException  {
    


    /**
	 * 
	 */
	private static final long serialVersionUID = 9020595371228596841L;


	/**
     * Constructor for RemoteAccessException.
     * @param msg the detail message
     */
    public RemoteAccessException(String msg) {
        super(msg);
    }


    public RemoteAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

package com.netto.core.exception;

public class NettoIOException extends RuntimeException  {
    


    /**
	 * 
	 */
	private static final long serialVersionUID = -1657386537294159601L;


	/**
     * Constructor for RemoteAccessException.
     * @param msg the detail message
     */
    public NettoIOException(String msg) {
        super(msg);
    }


    public NettoIOException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

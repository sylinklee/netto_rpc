package com.netto.core.exception;

import io.netty.handler.codec.DecoderException;

public class NettoDecoderException extends DecoderException {

    private static final long serialVersionUID = -1995801950698951640L;

    /**
     * Creates a new instance.
     */
    public NettoDecoderException() {
    }

    /**
     * Creates a new instance.
     */
    public NettoDecoderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     */
    public NettoDecoderException(String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     */
    public NettoDecoderException(Throwable cause) {
        super(cause);
    }
}

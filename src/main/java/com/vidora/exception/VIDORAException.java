package com.vidora.exception;

public class VIDORAException extends Exception {
    
    public VIDORAException(String message) {
        super(message);
    }
    
    public VIDORAException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public VIDORAException(Throwable cause) {
        super(cause);
    }
}

package se.knowit.bookitregistration.service.exception;

public class ConflictingEntityException extends Exception {
    static final long serialVersionUID = 42L;
    
    public ConflictingEntityException() {
    }
    
    public ConflictingEntityException(String message) {
        super(message);
    }
    
    public ConflictingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ConflictingEntityException(Throwable cause) {
        super(cause);
    }
    
    public ConflictingEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

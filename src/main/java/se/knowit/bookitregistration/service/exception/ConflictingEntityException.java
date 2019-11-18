package se.knowit.bookitregistration.service.exception;

public class ConflictingEntityException extends Exception {

    private String message;

    public ConflictingEntityException() {}
    public ConflictingEntityException(Throwable throwable)
    {
        super.initCause(throwable);
    }
    public ConflictingEntityException(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

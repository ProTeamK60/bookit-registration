package se.knowit.bookitregistration.service.exception;

public class MaxNumberOfRegistrationExceededException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public MaxNumberOfRegistrationExceededException() {

  }

  public MaxNumberOfRegistrationExceededException(String message) {

    super(message);
  }

  public MaxNumberOfRegistrationExceededException(String message, Throwable cause) {
    super(message, cause);
  }

  public MaxNumberOfRegistrationExceededException(Throwable cause) {
    super(cause);
  }

  public MaxNumberOfRegistrationExceededException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

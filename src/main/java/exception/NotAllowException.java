/**
 * 
 */
package exception;

/**
 * Thrown when User is <b>not</b> owner of resource
 * 
 * @author TriLe
 */
public class NotAllowException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 594248027136829863L;

    /**
     * 
     */
    public NotAllowException() {
        super();
    }

    /**
     * @param message
     */
    public NotAllowException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NotAllowException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NotAllowException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public NotAllowException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

/**
 * 
 */
package exception;

/**
 * Thrown when Token invalid
 * 
 * @author TriLe
 */
public class UnauthorizedException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 4110850606842136065L;

    /**
     * 
     */
    public UnauthorizedException() {
        super();
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public UnauthorizedException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

}

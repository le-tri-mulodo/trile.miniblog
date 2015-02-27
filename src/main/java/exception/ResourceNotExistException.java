/**
 * 
 */
package exception;

/**
 * Thrown when resource not exist in Db
 * 
 * @author TriLe
 */
public class ResourceNotExistException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = -4634183233746996321L;

    /**
     * To indicate type of resource with not exist
     */
    private Resource resourceType;

    /**
     * 
     */
    public ResourceNotExistException() {
        super();
        resourceType = Resource.NONE;
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public ResourceNotExistException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public ResourceNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public ResourceNotExistException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ResourceNotExistException(Throwable cause) {
        super(cause);
    }

    /**
     * @param resourceType
     */
    public ResourceNotExistException(Resource resourceType) {
        super();
        this.resourceType = resourceType;
    }

    /**
     * @return the resourceType
     */
    public Resource getResourceType()
    {
        return resourceType;
    }

    /**
     * @param resourceType
     *            the resourceType to set
     */
    public void setResourceType(Resource resourceType)
    {
        this.resourceType = resourceType;
    }

//    @Override
//    public String getMessage()
//    {
//        return resourceType.toString() + "not exist";
//    }

    public enum Resource {
        NONE, USER, TOKEN, POST, COMMENT;
    }
}

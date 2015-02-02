/**
 * 
 */
package com.mulodo.miniblog.message;

/**
 * @author TriLe
 *
 */
public class SuccessMessage extends MetaMessage {
    private String message;

    /**
     * @param code
     * @param message
     */
    public SuccessMessage(int code, String message) {
	super(code);
	this.message = message;
    }

    /**
     * @return the message
     */
    public String getMessage() {
	return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
	this.message = message;
    }
}

/**
 * 
 */
package com.mulodo.miniblog.message;

import java.util.List;

/**
 * @author TriLe
 *
 */
public class ErrorMessage extends MetaMessage {
    private String description;

    private List<String> messages;

    /**
     * @param code
     * @param description
     * @param messages
     */
    public ErrorMessage(int code, String description, List<String> messages) {
	super(code);
	this.description = description;
	this.messages = messages;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @return the messages
     */
    public List<String> getMessages() {
	return messages;
    }

    /**
     * @param messages
     *            the messages to set
     */
    public void setMessages(List<String> messages) {
	this.messages = messages;
    }
}

package com.mulodo.miniblog.message;

import java.util.List;

public class MetaMessage {
    private int code;

    private String description;

    private String message;

    private List<String> messages;

    public MetaMessage() {
    }

    public MetaMessage(int code) {
        this.code = code;
    }

    /**
     * @param code
     * @param message
     */
    public MetaMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @param code
     * @param description
     * @param messages
     */
    public MetaMessage(int code, String description, List<String> messages) {
        this.code = code;
        this.description = description;
        this.messages = messages;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(int code) {
        this.code = code;
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

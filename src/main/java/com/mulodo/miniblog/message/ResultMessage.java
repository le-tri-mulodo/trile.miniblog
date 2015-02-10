package com.mulodo.miniblog.message;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultMessage<T> {

    private MetaMessage meta;

    private T data;

    public ResultMessage() {
    }

    /**
     * @param meta
     * @param data
     */
    public ResultMessage(MetaMessage meta, T data) {
        super();
        this.meta = meta;
        this.data = data;
    }

    /**
     * Create Error message
     * 
     * @param code
     *            Response code
     * @param description
     *            Response description
     */
    public ResultMessage(int code, String description) {
        super();
        this.meta = new MetaMessage(code, description);
        this.data = null;
    }

    /**
     * Create Error message
     * 
     * @param code
     *            Error code
     * @param description
     *            Error description
     * @param messages
     *            List error message
     */
    public ResultMessage(int code, String description, List<String> messages) {
        super();
        this.meta = new MetaMessage(code, description, messages);
        this.data = null;
    }

    /**
     * Create Error message
     * 
     * @param code
     *            Error code
     * @param description
     *            Error description
     * @param message
     *            Stand-alone error message
     */
    public ResultMessage(int code, String description, String message) {
        super();
        // Create array list with 1 element and add message
        List<String> messages = new ArrayList<String>(1);
        messages.add(message);
        this.meta = new MetaMessage(code, description, messages);
        this.data = null;
    }

    /**
     * Create Success message
     * 
     * @param code
     *            Result code
     * @param message
     *            Success message
     * @param data
     *            Data
     */
    public ResultMessage(int code, String message, T data) {
        super();
        this.meta = new MetaMessage(code, message);
        this.data = data;
    }

    /**
     * @return the meta
     */
    public MetaMessage getMeta() {
        return meta;
    }

    /**
     * @param meta
     *            the meta to set
     */
    public void setMeta(MetaMessage meta) {
        this.meta = meta;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(T data) {
        this.data = data;
    }
}

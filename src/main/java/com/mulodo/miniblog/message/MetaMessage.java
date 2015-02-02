package com.mulodo.miniblog.message;

public class MetaMessage {
    private int code;

    public MetaMessage(int code) {
	this.code = code;
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
}

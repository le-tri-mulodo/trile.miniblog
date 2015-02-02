package com.mulodo.miniblog.message;

public class ResultMessage {

    private MetaMessage meta;

    private Object data;

    /**
     * @param meta
     * @param data
     */
    public ResultMessage(MetaMessage meta, Object data) {
	super();
	this.meta = meta;
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
    public Object getData() {
	return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Object data) {
	this.data = data;
    }
}

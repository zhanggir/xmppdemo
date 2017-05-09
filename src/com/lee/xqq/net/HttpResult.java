package com.lee.xqq.net;

import java.io.InputStream;

public class HttpResult {

	protected String response;
	protected byte[] data;
	protected InputStream stream;
	protected int code;
	protected String exception;
	protected static final int HTTP_OK = 200;

	protected HttpResult() {
		super();
		response = "";
		exception = "";
	}

	public String getResponse() {
		return response;
	}

	protected void setResponse(String response) {
		this.response = response;
	}

	public byte[] getData() {
		return data;
	}

	protected void setData(byte[] data) {
		this.data = data;
	}

	public InputStream getStream() {
		return stream;
	}

	protected void setStream(InputStream stream) {
		this.stream = stream;
	}

	public int getCode() {
		return code;
	}

	protected void setCode(int code) {
		this.code = code;
	}

	public String getException() {
		return exception;
	}

	protected void setException(String exception) {
		this.exception = exception;
	}

}

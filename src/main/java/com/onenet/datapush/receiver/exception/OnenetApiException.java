package com.onenet.datapush.receiver.exception;

public class OnenetApiException extends RuntimeException {
	 private String message = null;
	    public String getMessage() {
		return message;
	}
		public OnenetApiException(String message) {
	        this.message = message;
	    }
}

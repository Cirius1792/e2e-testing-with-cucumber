package com.clt.common.error;

/**
 * Defines a base exception to be used in case of errors returned by external services
 */
public class ExternalServiceError extends RuntimeException{
    
	private int statusCode;
    private String description;

	/**
	 *
	 * @param errorCode funcional error code
	 * @param description functional error description
	 * @param statusCode http status code of the failed error
	 */
	public ExternalServiceError (String errorCode, String description, int statusCode) {
		super(errorCode);
		this.statusCode = statusCode;
        this.description = description;
	}

	public int getStatusCode() {
		return statusCode;
	}

    public String getDescription(){
        return description;
    }
}

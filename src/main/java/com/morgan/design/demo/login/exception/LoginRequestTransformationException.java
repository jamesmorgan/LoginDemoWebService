package com.morgan.design.demo.login.exception;

import static java.lang.String.format;

public class LoginRequestTransformationException extends LoginRequestException {

	/** */
	private static final long serialVersionUID = -4473850341075842091L;

	private LoginRequestTransformationException(final String message) {
		super(message);
	}

	public static LoginRequestTransformationException create() {
		return new LoginRequestTransformationException(format("Failed to transform incoming request"));
	}

}

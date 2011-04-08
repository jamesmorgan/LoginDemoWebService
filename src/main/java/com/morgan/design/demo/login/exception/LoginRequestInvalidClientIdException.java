package com.morgan.design.demo.login.exception;

import static java.lang.String.format;

public class LoginRequestInvalidClientIdException extends LoginRequestException {

	/** */
	private static final long serialVersionUID = 1042593774138665495L;

	private LoginRequestInvalidClientIdException(final String message) {
		super(message);
	}

	public static LoginRequestInvalidClientIdException create(final String clientId) {
		return new LoginRequestInvalidClientIdException(format("Invalid client ID: %s", clientId));
	}

}

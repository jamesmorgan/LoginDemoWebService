package com.morgan.design.demo.login.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.SERVER)
public class LoginRequestException extends Exception {

	/** */
	private static final long serialVersionUID = 4850097068571927700L;

	protected LoginRequestException(final String message) {
		super(message);
	}

	public static LoginRequestException create() {
		return new LoginRequestException("A server fault has occured");
	}
}

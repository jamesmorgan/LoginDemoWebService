package com.morgan.design.demo.login;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.morgan.design.demo.login.exception.LoginRequestInvalidClientIdException;
import com.morgan.design.demo.login.exception.LoginRequestTransformationException;
import com.morgan.design.demo.login.ws.generated.LoginRequest;
import com.morgan.design.demo.login.ws.generated.LoginResponse;

/**
 * @author james.morgan
 */
@Endpoint
public class LoginRequestEndpoint {

	private final Logger log = Logger.getLogger(this.getClass());

	public static String APPLICATION_UUID = "bcf7d786-61b3-11e0-a8cf-643150372d03";

	public static final String NAMESPACE_URI = "http://morgan-design.com/ws/schema/2010";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "LoginRequest")
	public @ResponsePayload
	LoginResponse handleLoginRequest(@RequestPayload final LoginRequest loginRequest, @RequestPayload final Element element)
			throws Throwable {

		// Validate Login Application Code -> this could be/should be DB driven in a live environment
		if (!APPLICATION_UUID.equals(loginRequest.getApplicationUUID())) {
			throw LoginRequestInvalidClientIdException.create(loginRequest.getApplicationUUID());
		}

		// Convert element to string for purposes of audit logging etc, this is optional
		final String xml = convertPayloadToString(element);

		// Generate a new UUID, this should be saved to the DB for the current session of the client application
		final String uuid = UUID.randomUUID()
			.toString();

		// Return a login response to the client
		return createLoginResponse(uuid, 1);
	}

	/**
	 * @param element the Raw {@link Element} SOAP Element
	 * @return the XML in {@link String} form
	 * @throws LoginRequestTransformationException
	 */
	private String convertPayloadToString(final Element element) throws LoginRequestTransformationException {
		try {
			final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			return outputter.outputString(element);
		}
		catch (final Throwable e) {
			this.log.error("Error converting Login Request: ", e);
			throw LoginRequestTransformationException.create();
		}
	}

	/**
	 * @param authenticationID a generated {@link UUID} code
	 * @param responseCode the success, failure response code
	 * @return a constructed {@link LoginResponse}
	 */
	private LoginResponse createLoginResponse(final String authenticationID, final int responseCode) {
		final LoginResponse loginResponse = new LoginResponse();
		loginResponse.setAuthenticationID(authenticationID);
		loginResponse.setResponseCode(responseCode);
		if (this.log.isDebugEnabled()) {
			this.log.debug(String.format("Creating Login Response, Authentication Code [%s], Response Code [%s] ", authenticationID,
					responseCode));
		}
		return loginResponse;
	}
}

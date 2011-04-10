package com.morgan.design.demo.login;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

import javax.xml.transform.Source;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

/**
 * @author James Edward Morgan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/spring-loginWebService.xml", "classpath:/spring/spring.xml" })
public class LoginRequestEndpointIntTest {

	@Autowired
	private ApplicationContext applicationContext;

	private MockWebServiceClient mockClient;

	private static final String STATIC_UUID = "6988aa21-289f-11e0-9f9b-a2436b4d405b";
	private static final String APPLICATION_UUID = "bcf7d786-61b3-11e0-a8cf-643150372d03";
	private static final String CLIENT_ID = "be4c3204-28a0-11e0-9f9b-a2436b4d405b";

	@Before
	public void setUp() throws Exception {
		this.mockClient = MockWebServiceClient.createClient(this.applicationContext);
		Mockit.setUpMock(new MockUUID(STATIC_UUID));
	}

	@After
	public void tearDown() throws Exception {
		Mockit.tearDownMocks();
	}

	@Test
	public void shouldAcceptValidSoapRequest() {
		final Source requestPayload = createLoginPayload(APPLICATION_UUID, CLIENT_ID, "user", "pass");
		final Source responsePayload = createReturnPayload();
		this.mockClient.sendRequest(withPayload(requestPayload))
			.andExpect(payload(responsePayload));
	}

	private Source createReturnPayload() {
		//@formatter:off
		//<ns2:LoginResponse xmlns:ns2="http://morgan-design.com/ws/schema/2010">
		//	<ns2:AuthenticationID>93cca04c-f225-44f3-9e4a-2dc5dc9865b0</ns2:AuthenticationID>
		//  <ns2:ResponseCode>1</ns2:ResponseCode>
		//</ns2:LoginResponse>
		final StringBuilder builder = new StringBuilder();
		builder.append("<ns2:LoginResponse  xmlns:ns2='http://morgan-design.com/ws/schema/2010'>");
			builder.append("<ns2:AuthenticationID>").append(STATIC_UUID).append("</ns2:AuthenticationID>");
			builder.append("<ns2:ResponseCode>").append(1).append("</ns2:ResponseCode>");
		builder.append("</ns2:LoginResponse>");
		//@formatter:on
		return new StringSource(builder.toString());
	}

	private Source createLoginPayload(final String applicationUUID, final String deviceID, final String userName, final String password) {
		//@formatter:off
		//<ns:LoginRequest>
		//	<ns:ApplicationUUID>?</ns:ApplicationUUID>
		//  <ns:DeviceID>?</ns:DeviceID>
		//  <ns:UserName>?</ns:UserName>
		//  <ns:Password>?</ns:Password>
		//</ns:LoginRequest>
		final StringBuilder builder = new StringBuilder();
		builder.append("<ns:LoginRequest xmlns:ns='http://morgan-design.com/ws/schema/2010'>");
			builder.append("<ns:ApplicationUUID>").append(applicationUUID).append("</ns:ApplicationUUID>");
			builder.append("<ns:DeviceID>").append(deviceID).append("</ns:DeviceID>");
			builder.append("<ns:UserName>").append(userName).append("</ns:UserName>");
			builder.append("<ns:Password>").append(password).append("</ns:Password>");
		builder.append("</ns:LoginRequest>");
		//@formatter:on
		return new StringSource(builder.toString());
	}

}

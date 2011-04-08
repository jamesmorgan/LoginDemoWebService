package com.morgan.design.demo.login;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

import javax.xml.transform.Source;

import mockit.Mockit;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/spring-loginWebService.xml" })
public class LoginRequestEndpointIntTest {

	@Autowired
	private ApplicationContext applicationContext;

	private MockWebServiceClient mockClient;

	private static final String STATIC_UUID = "6988aa21-289f-11e0-9f9b-a2436b4d405b";
	private static final String CLIENT_ID = "ac783d8f-2931-11e0-9744-81b5090d28a2";
	private static final String TRANSACTION_ID = "be4c3204-28a0-11e0-9f9b-a2436b4d405b";

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
		final Source requestPayload = createIncomingPayload("25/02/2011 16:30:10", "1", "123.45", TRANSACTION_ID, CLIENT_ID);
		final Source responsePayload = createReturnPayload();
		this.mockClient.sendRequest(withPayload(requestPayload))
			.andExpect(payload(responsePayload));
	}

	// @Test
	// public void shouldFailDueToInvalidClientID() {
	// final String invalidClientId = "ac783d8f-2931-11e0-9744-81b5090d28a1";
	// final Source requestPayload = createIncomingPayload("25/02/2011 16:30:10", "1", "123.45", TRANSACTION_ID, invalidClientId);
	// this.mockClient.sendRequest(withPayload(requestPayload))
	// .andExpect(serverOrReceiverFault("Invalid client ID: ac783d8f-2931-11e0-9744-81b5090d28a1"));
	// }
	//
	// @Test
	// @RequiresTestData(locations = { "shouldAcceptValidSoapRequest" })
	// public void shouldFailDueToInvalidTransactionID() {
	// final String invalidTransactionId = "6988aa41-918f-23e0-9f9b-a2436b4d405b";
	// final Source requestPayload = createIncomingPayload("25/02/2011 16:30:10", "1", "123.45", invalidTransactionId, CLIENT_ID);
	// this.mockClient.sendRequest(withPayload(requestPayload))
	// .andExpect(serverOrReceiverFault("Transaction ID 6988aa41-918f-23e0-9f9b-a2436b4d405b is invalid"));
	// }
	//
	// @Test
	// @RequiresTestData(locations = { "shouldAcceptValidSoapRequest" })
	// public void shouldFailDueToExistingMiDataAlreadyProcessed() {
	// final String alreadyProcessedId = "b3f41832-2a2d-11e0-9357-7131a5a6509c";
	// final Source requestPayload = createIncomingPayload("25/02/2011 16:30:10", "1", "123.45", alreadyProcessedId, CLIENT_ID);
	// this.mockClient.sendRequest(withPayload(requestPayload))
	// .andExpect(serverOrReceiverFault("MI for transaction b3f41832-2a2d-11e0-9357-7131a5a6509c has already been processed."));
	// }
	//
	// @Test
	// @RequiresTestData(locations = { "shouldAcceptValidSoapRequest" })
	// public void shouldFailDueToInvalidClaimNumber() {
	// final Source requestPayload = createIncomingPayload("25/02/2011 16:30:10", "10", "123.45", TRANSACTION_ID, CLIENT_ID);
	// this.mockClient.sendRequest(withPayload(requestPayload))
	// .andExpect(serverOrReceiverFault("Incorrent number of claims.  Expected 1, received 10"));
	// }
	//
	// @Test
	// @NoTestData
	// public void shouldRejectInvalidSoapRequestWithBadDateTime() {
	// final Source requestPayload = createIncomingPayload("invalid-date", "10", "123.45", TRANSACTION_ID, CLIENT_ID);
	// this.mockClient.sendRequest(withPayload(requestPayload))
	// .andExpect(clientOrSenderFault("Validation error"));
	// }
	//
	// @Test
	// @NoTestData
	// public void shouldRejectInvalidSoapRequestWithBadNumOfClaims() {
	// final Source requestPayload =
	// createIncomingPayload("25/02/2011 16:30:10", "bad-num-of-claims", "123.45", TRANSACTION_ID, CLIENT_ID);
	// this.mockClient.sendRequest(withPayload(requestPayload))
	// .andExpect(clientOrSenderFault("Validation error"));
	// }
	//
	// @Test
	// @NoTestData
	// public void shouldRejectInvalidSoapRequestWithBadProductionCosts() {
	// final Source requestPayload = createIncomingPayload("25/02/2011 16:30:10", "10", "bad-decimal", TRANSACTION_ID, CLIENT_ID);
	// this.mockClient.sendRequest(withPayload(requestPayload))
	// .andExpect(clientOrSenderFault("Validation error"));
	// }

	private Source createReturnPayload() {
		//@formatter:off
		//<ns2:DispatchResponse xmlns:ns2="http://claims.theclaimsguys.co.uk/schema/2010">
		//  <ns2:TransactionID>08a6fff4-0127-4f9c-bb4e-b8547fd9780f</ns2:TransactionID>
		//</ns2:DispatchResponse>
		final StringBuilder builder = new StringBuilder();
		builder.append("<ns2:DispatchResponse  xmlns:ns2='http://claims.theclaimsguys.co.uk/schema/2010'>");
			builder.append("<ns2:TransactionID>").append(STATIC_UUID).append("</ns2:TransactionID>");
		builder.append("</ns2:DispatchResponse>");
		//@formatter:on
		return new StringSource(builder.toString());
	}

	private Source createIncomingPayload(final String dateTime, final String numOfClaims, final String productionCosts,
			final String transcationId, final String clientId) {
		//@formatter:off
		//<ns:Envelope xmlns:ns="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://morgan-design.com/ws/schema/2010">
		//   <ns:Header/>
		//   <ns:Body>
		//      <ns:LoginRequest>
		//         <!--You may enter the following 4 items in any order-->
		//         <ns:ApplicationUUID>?</ns:ApplicationUUID>
		//         <ns:DeviceID>?</ns:DeviceID>
		//         <ns:UserName>?</ns:UserName>
		//         <ns:Password>?</ns:Password>
		//      </ns:LoginRequest>
		//   </ns:Body>
		//</ns:Envelope>
		//</ns:DispatchRequest>
		final StringBuilder builder = new StringBuilder();
		builder.append("<ns:DispatchRequest xmlns:ns='http://claims.theclaimsguys.co.uk/schema/2010'>");
			builder.append("<ns:ClientID>").append(clientId).append("</ns:ClientID>");
			builder.append("<ns:TransactionUID>").append(transcationId).append("</ns:TransactionUID>");
			builder.append("<ns:DateTimeDispatched>").append(dateTime).append("</ns:DateTimeDispatched>");
			builder.append("<ns:NumberOfClaims>").append(numOfClaims).append("</ns:NumberOfClaims>");
			builder.append("<ns:ProductionCost>").append(productionCosts).append("</ns:ProductionCost>");
		builder.append("</ns:DispatchRequest>");
		//@formatter:on
		return new StringSource(builder.toString());
	}

}

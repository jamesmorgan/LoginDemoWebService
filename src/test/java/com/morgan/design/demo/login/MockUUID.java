/**
 *
 */
package com.morgan.design.demo.login;

import java.util.UUID;

import mockit.Mock;
import mockit.MockClass;

/**
 * @author James Edward Morgan
 *
 */
@MockClass(realClass = UUID.class)
public final class MockUUID { 
	private final String key;

	public MockUUID(final String key) {
		this.key = key;
	}

	@Mock
	public UUID randomUUID() {
		return UUID.fromString(this.key);
	}
}

/*
 * The contents of this file are subject to the Mozilla Public 
 * License Version 1.1 (the "License"); you may not use this 
 * file except in compliance with the License. You may obtain 
 * a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an 
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express 
 * or implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 *
 * The Original Code is OIOSAML Java Service Provider.
 * 
 * The Initial Developer of the Original Code is Trifork A/S. Portions 
 * created by Trifork A/S are Copyright (C) 2008 Danish National IT 
 * and Telecom Agency (http://www.itst.dk). All Rights Reserved.
 * 
 * Contributor(s):
 *   Joakim Recht <jre@trifork.com>
 *   Rolf Njor Jensen <rolf@trifork.com>
 *
 */
package dk.gov.oio.saml.session.inmemory;

import java.time.Duration;
import java.time.Instant;

/**
 * In memory session timeout functionality, enabling cleanup session data that has expired/timed out
 * @param <T> Add timeout functionality to this class
 */
public class TimeOutWrapper<T> {
	private final T object;
	private Instant accesstime;

	public TimeOutWrapper(T object) {
		this.object = object;
		this.accesstime = Instant.now();
	}

	public T getObject() {
		return object;
	}

	public boolean isExpired(Duration delay) {
		return (Instant.now().isAfter(accesstime.plus(delay)));
	}

	public void setAccesstime() {
		accesstime = Instant.now();
	}
	
	@Override
	public String toString() {
		return "TO: " + accesstime + ", obj: " + object;
	}
}

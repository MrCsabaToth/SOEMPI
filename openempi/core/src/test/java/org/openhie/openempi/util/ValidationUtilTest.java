/**
 * 
 *  Copyright (C) 2013 Vanderbilt University <csaba.toth, b.malin @vanderbilt.edu>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.openhie.openempi.util;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ValidationUtilTest extends TestCase {
	//~ Instance fields ========================================================

	private final Log log = LogFactory.getLog(ValidationUtilTest.class);

	//~ Constructors ===========================================================

	public ValidationUtilTest(String name) {
		super(name);
	}

	public void testIsNormal() {
		boolean isValid;
		isValid = ValidationUtil.isNormal("abcdefghijklmnopqrstuvwxyz1234567890", true, true, true, false);
		assertEquals(isValid, true);
		isValid = ValidationUtil.isNormal("bla_bla", true, true, true, false);	// underscore is allowed
		assertEquals(isValid, true);
		isValid = ValidationUtil.isNormal("bla_bla!!!", true, true, true, false);	// underscore is allowed but it shouldn't end inspection of the string
		assertEquals(isValid, false);
		isValid = ValidationUtil.isNormal("1blabla", true, true, true, false);	// digit is allowed as leading character also
		assertEquals(isValid, true);
		isValid = ValidationUtil.isNormal("_blabla", true, true, true, false);	// but not as leading character
		assertEquals(isValid, false);
		isValid = ValidationUtil.isNormal("!@#$%&*()", true, true, true, false);
		assertEquals(isValid, false);
		isValid = ValidationUtil.isNormal("; DROP TABLE blabla; --", true, true, true, false);
		assertEquals(isValid, false);
		isValid = ValidationUtil.isNormal(" ", true, true, true, false);
		assertEquals(isValid, false);
		isValid = ValidationUtil.isNormal(" a ", true, true, true, false);
		assertEquals(isValid, false);
		isValid = ValidationUtil.isNormal("\t", true, true, true, false);
		assertEquals(isValid, false);
		isValid = ValidationUtil.isNormal("\ta", true, true, true, false);
		assertEquals(isValid, false);
		isValid = ValidationUtil.isNormal("\n", true, true, true, false);
		assertEquals(isValid, false);
		isValid = ValidationUtil.isNormal("a\n", true, true, true, false);
		assertEquals(isValid, false);
	}

	public void testSanityCheckFieldName() {
		boolean gotException = false;
		ValidationUtil.sanityCheckFieldName("abcdefghijklmnopqrstuvwxyz1234567890");
		assertEquals(gotException, false);
		gotException = false;
		ValidationUtil.sanityCheckFieldName("bla_bla");
		assertEquals(gotException, false);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName("bla_bla!!!");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		ValidationUtil.sanityCheckFieldName("1blabla");
		assertEquals(gotException, false);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName("_blabla");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName("!@#$%&*()");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName("; DROP TABLE blabla; --");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName(" ");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName(" a ");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName("\t");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName("\ta");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName("\n");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckFieldName("a\n");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
	}

	public void testSanityCheckSessionKey() {
		boolean gotException = false;
		ValidationUtil.sanityCheckSessionKey("A5674CBEF99809");
		assertEquals(gotException, false);
		gotException = false;
		ValidationUtil.sanityCheckSessionKey("1A5674CBEF99809");
		assertEquals(gotException, false);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey("1a5674CBEF99809");	// lower case letter
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey("1A5674C_BEF99809");	// underscore
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey("!@#$%&*()");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey("; DROP TABLE blabla; --");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey(" ");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey(" a ");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey("\t");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey("\ta");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey("\n");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckSessionKey("a\n");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
	}

	public void testIsIPAddress() {
		boolean isIPAddress;
		isIPAddress = ValidationUtil.isIPAddress("blabla");
		assertEquals(isIPAddress, false);
		isIPAddress = ValidationUtil.isIPAddress("aaa.bbb.ccc.ddd");
		assertEquals(isIPAddress, false);
		isIPAddress = ValidationUtil.isIPAddress("111.222.333.444");
		assertEquals(isIPAddress, false);
		isIPAddress = ValidationUtil.isIPAddress("999.999.999.999");
		assertEquals(isIPAddress, false);
		isIPAddress = ValidationUtil.isIPAddress("1.1.1.256");
		assertEquals(isIPAddress, false);
		isIPAddress = ValidationUtil.isIPAddress("-1.1.1.1");
		assertEquals(isIPAddress, false);
		isIPAddress = ValidationUtil.isIPAddress("1.2.3");
		assertEquals(isIPAddress, false);
		isIPAddress = ValidationUtil.isIPAddress("1.2.3.4.5");
		assertEquals(isIPAddress, false);
		isIPAddress = ValidationUtil.isIPAddress("; DROP TABLE blabla; --");
		assertEquals(isIPAddress, false);
		isIPAddress = ValidationUtil.isIPAddress("1.2.3.4");
		assertEquals(isIPAddress, true);
		isIPAddress = ValidationUtil.isIPAddress("111.111.111.111");
		assertEquals(isIPAddress, true);
		isIPAddress = ValidationUtil.isIPAddress("192.168.1.0");
		assertEquals(isIPAddress, true);
		isIPAddress = ValidationUtil.isIPAddress("172.16.0.1");
		assertEquals(isIPAddress, true);
		isIPAddress = ValidationUtil.isIPAddress("10.151.10.47");
		assertEquals(isIPAddress, true);
		isIPAddress = ValidationUtil.isIPAddress("160.39.129.169");
		assertEquals(isIPAddress, true);
	}

	public void testSanityCheckIPAddress() {
		boolean gotException = false;
		try {
			ValidationUtil.sanityCheckIPAddress("blabla");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckIPAddress("aaa.bbb.ccc.ddd");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckIPAddress("111.222.333.444");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckIPAddress("999.999.999.999");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckIPAddress("1.1.1.256");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckIPAddress("-1.1.1.1");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckIPAddress("1.2.3");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckIPAddress("1.2.3.4.5");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		try {
			ValidationUtil.sanityCheckIPAddress("a\n");
		}
		catch(IllegalArgumentException e) {
			gotException = true;
		}
		assertEquals(gotException, true);
		gotException = false;
		ValidationUtil.sanityCheckIPAddress("1.2.3.4");
		assertEquals(gotException, false);
		gotException = false;
		ValidationUtil.sanityCheckIPAddress("111.111.111.111");
		assertEquals(gotException, false);
		gotException = false;
		ValidationUtil.sanityCheckIPAddress("192.168.1.0");
		assertEquals(gotException, false);
		gotException = false;
		ValidationUtil.sanityCheckIPAddress("172.16.0.1");
		assertEquals(gotException, false);
		gotException = false;
		ValidationUtil.sanityCheckIPAddress("10.151.10.47");
		assertEquals(gotException, false);
		gotException = false;
		ValidationUtil.sanityCheckIPAddress("160.39.129.169");
		assertEquals(gotException, false);
	}

	public void testIsHostName() {
		boolean isHostName;
		isHostName = ValidationUtil.isHostName("blabla");
		assertEquals(isHostName, true);
		isHostName = ValidationUtil.isHostName("aaa.bbb.ccc.ddd");
		assertEquals(isHostName, true);
		isHostName = ValidationUtil.isHostName("111.222.333.444");
		assertEquals(isHostName, false);
		isHostName = ValidationUtil.isHostName("999.999.999.999");
		assertEquals(isHostName, false);
		isHostName = ValidationUtil.isHostName("1.1.1.256");
		assertEquals(isHostName, false);
		isHostName = ValidationUtil.isHostName("-1.1.1.1");
		assertEquals(isHostName, false);
		isHostName = ValidationUtil.isHostName("1.2.3");
		assertEquals(isHostName, false);
		isHostName = ValidationUtil.isHostName("1.2.3.4.5");
		assertEquals(isHostName, false);
		isHostName = ValidationUtil.isHostName("; DROP TABLE blabla; --");
		assertEquals(isHostName, false);
		isHostName = ValidationUtil.isHostName("1.2.3.4");
		assertEquals(isHostName, false);
		isHostName = ValidationUtil.isHostName("hiplab.mc.vanderbilt.edu");
		assertEquals(isHostName, true);
		isHostName = ValidationUtil.isHostName("tower.dhcp.mc.vanderbilt.edu");
		assertEquals(isHostName, true);
		isHostName = ValidationUtil.isHostName("node-1.dhcp.mc.vanderbilt.edu");	// should accept dash
		assertEquals(isHostName, true);
		isHostName = ValidationUtil.isHostName("-node-1.dhcp.mc.vanderbilt.edu");	// no leading dash
		assertEquals(isHostName, false);
	}

	public void testIsAlnumRegex() {
		boolean isAlnum;
		isAlnum = ValidationUtil.isAlnumRegex("abcdefghijklmnopqrstuvwxyz1234567890");
		assertEquals(isAlnum, true);
		isAlnum = ValidationUtil.isAlnumRegex("bla_bla");
		assertEquals(isAlnum, false);
		isAlnum = ValidationUtil.isAlnumRegex("!@#$%&*()");
		assertEquals(isAlnum, false);
		isAlnum = ValidationUtil.isAlnumRegex("; DROP TABLE blabla; --");
		assertEquals(isAlnum, false);
		isAlnum = ValidationUtil.isAlnumRegex(" ");
		assertEquals(isAlnum, false);
		isAlnum = ValidationUtil.isAlnumRegex(" a ");
		assertEquals(isAlnum, false);
		isAlnum = ValidationUtil.isAlnumRegex("\t");
		assertEquals(isAlnum, false);
		isAlnum = ValidationUtil.isAlnumRegex("\ta");
		assertEquals(isAlnum, false);
		isAlnum = ValidationUtil.isAlnumRegex("\n");
		assertEquals(isAlnum, false);
		isAlnum = ValidationUtil.isAlnumRegex("a\n");
		assertEquals(isAlnum, false);
	}

}

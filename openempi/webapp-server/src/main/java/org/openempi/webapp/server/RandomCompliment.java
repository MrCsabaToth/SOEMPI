/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openempi.webapp.server;

/*
 * Java5+ Class, showing how to run Java5 server side code.. and Java1.4 GWT Client code simultaneously in GWT-Maven
 */
public class RandomCompliment {

	/*
	 * Java5+ enum
	 */
	public static enum compliment {
		GREAT, EXCELLENT, FANTASTIC, AWESOME, L337
	}

	/**
	 * Just use the static method to get a random value
	 */
	private RandomCompliment() {
	};

	/**
	 * Randomly select a compliment from the Java5+ enum
	 * 
	 * @return a random compliment
	 */
	public static String get() {
		return "" + compliment.values()[(int) getRandomIndex()];
	}

	/**
	 * Generates a random index number, to select a random compliment
	 * 
	 * @return random index number to the compliment.values array
	 */
	private static int getRandomIndex() {
		// (int) float : will round DOWN, so [ 0 <= RETURN_VAL <
		// compliment.length ]
		return (int) (Math.random() * ((double) compliment.values().length));
	}

}

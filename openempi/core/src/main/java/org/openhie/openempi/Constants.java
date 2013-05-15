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
package org.openhie.openempi;

import java.nio.charset.Charset;

/**
 * Constant values used throughout the application.
 * 
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class Constants {

    //~ Static fields/initializers =============================================

    /**
     * The name of the ResourceBundle used in this application
     */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /**
     * File separator from System properties
     */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /**
     * User home from System properties
     */
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

    /**
     * OpenEMPI home directory
     */
    public static final String OPENEMPI_HOME = "openempi.home";
    
    public static final String OPENEMPI_HOME_VALUE = System.getProperty(OPENEMPI_HOME);

	public static final String OPENEMPI_HOME_ENV_VARIABLE = "OPENEMPI_HOME";
    
	public static final String OPENEMPI_HOME_ENV_VALUE = System.getenv(OPENEMPI_HOME_ENV_VARIABLE);
	
    /**
     * System parameter name for list of extension contexts
     */
    public static final String OPENEMPI_EXTENSION_CONTEXTS = "openempi.extension.contexts";
    
    /**
     * Name for extension contexts property file name
     */
    public static final String OPENEMPI_EXTENSION_CONTEXTS_PROPERTY_FILENAME = "openempi-extension-contexts.properties";
    	
    /**
     * Default upload file directory
     */
    public static final String DEFAULT_FILE_REPOSITORY_DIRECTORY = "fileRepository";
    
    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig";

    /**
     * Session scope attribute that holds the locale set by the user. By setting this key
     * to the same one that Struts uses, we get synchronization in Struts w/o having
     * to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

    /**
     * The request scope attribute under which an editable user form is stored
     */
    public static final String USER_KEY = "userForm";

    /**
     * The request scope attribute that holds the user list
     */
    public static final String USER_LIST = "userList";

    /**
     * The request scope attribute for indicating a newly-registered user
     */
    public static final String REGISTERED = "registered";

    /**
     * The name of the Administrator role, as specified in web.xml
     */
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    /**
     * The name of the User role, as specified in web.xml
     */
    public static final String USER_ROLE = "ROLE_USER";

    /**
     * The name of the user's role list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String USER_ROLES = "userRoles";

    /**
     * The name of the available roles list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String AVAILABLE_ROLES = "availableRoles";

    /**
     * The username of the default Administrator user
     */
    public static final String DEFAULT_ADMIN_USERNAME = "admin";

    /**
     * The password of the default Administrator user
     */
    public static final String DEFAULT_ADMIN_PASSWORD = DEFAULT_ADMIN_USERNAME;

    /**
     * The name of the CSS Theme setting.
     */
    public static final String CSS_THEME = "csstheme";

    /**
     * The weight that is assigned to a pair of records when they are merged intentionally and not by the matching algorithm
     */
	public static final Double MERGE_RECORDS_WEIGHT = 1.0;

	/**
	 * Probabilistic matching (with binary scores) service's name
	 */
	public static final String PROBABILISTIC_MATCHING_SERVICE_WITH_BINARY_SCORES_NAME = "ProbabilisticMatchingWithBinaryScores";

	/**
	 * Probabilistic matching  (with scaled scores) service's name
	 */
	public static final String PROBABILISTIC_MATCHING_SERVICE_WITH_SCALED_SCORES_NAME = "ProbabilisticMatchingWithScaledScores";

	/**
	 * CBF scoring service's name
	 */
	public static final String CBF_SCORING_SERVICE_NAME = "CBFScoringService";

	/**
	 * File name of the serialized FellegiSunterConfiguration information file
	 */
	public static final String FELLEGI_SUNTER_CONFIG_FILE_NAME = "FellegiSunterConfiguration.ser";

	/**
	 * File name of the record pair marginal probability list
	 */
	public static final String MARGINAL_PROBABILITIES_FILE_NAME = "marginalProbs.csv";

	/**
	 * File name of the record pair weight list
	 */
	public static final String WEIGHTS_FILE_NAME = "weights.csv";

	/**
	 * Number of fields if we want to get just limited number of them
	 */
	public static final Integer FIELD_NUMBER_LIMIT = 10;

	/**
	 * Basic blocking (with distinct bins) service's name
	 */
	public static final String BASIC_BLOCKING_DISTINCT_BINS_SERVICE_NAME = "BasicBlockingDistincBins";

	/**
	 * Basic blocking (with overlapping bins) service's name
	 */
	public static final String BASIC_BLOCKING_OVERLAPPING_BINS_SERVICE_NAME = "BasicBlockingOverlappingBins";

	/**
	 * Blocking bypass service's name
	 */
	public static final String BLOCKING_BYPASS_SERVICE_NAME = "BlockingBypass";

	/**
	 * LSH with CBF blocking service's name
	 */
	public static final String PPB_WITH_CRYPTO_RANDOM_BITS_SERVICE_NAME = "PrivacyPreservingBlockingWithCryptoRandomBits";

	/**
	 * LSH with CBF blocking service's name
	 */
	public static final String LSH_WITH_CBF_MULTI_PARTY_SERVICE_NAME = "LSHWithCBFMultiParty";

	/**
	 * LSH with FBF blocking service's name
	 */
	public static final String LSH_WITH_FBF_MULTI_PARTY_SERVICE_NAME = "LocalitySensitiveHashingWithCompositeBloomFilters";

	/**
	 * Three third party FBF protocol's name
	 */
	public static final String THREE_THIRD_PARTY_FBF_PROTOCOL_NAME = "ThreeThirdPartyFBFProtocol";

	/**
	 * Three third party CBF protocol's name
	 */
	public static final String THREE_THIRD_PARTY_CBF_PROTOCOL_NAME = "ThreeThirdPartyCBFProtocol";

	/**
	 * Distance in case of the left or right (or both) attribute is null
	 */
	public static final Double DEFAULT_DISTANCE = 0.0;

    /**
     * The length of a salt data, in bytes.
     */
    public static final Integer SALT_LENGTH = 512 / 8;

    /**
     * The length of the public key data, in bytes.
     */
    public static final Integer PUBLIC_KEY_PART_LENGTH = 2048 / 8;

    /**
     * The length of the private key data, in bytes.
     */
    public static final Integer PRIVATE_KEY_PART_LENGTH = 2048 / 8;

    /**
     * The default date(+time) format string.
     */
    public static final String DEFAULT_DATE_TIME_FORMAT_STRING = "yyyyMMddHHmmssSSS";

    /**
     * The date(+time) format string w/o year.
     */
    public static final String DATE_TIME_FORMAT_STRING_WO_YEAR = "MMddHHmmssSSS";

    /**
     * Default RMI port (at least for JBoss).
     */
    public static final String RMI_DEFAULT_PORT_NUMBER = "1099";

    /**
     * Default page size.
     * TODO: make it configurable?
     */
    public static final Integer PAGE_SIZE = 500;

    /**
     * personManagerService bean name.
     */
    public static final String PERSONMANAGERSERVICE_BEAN_NAME = "personManagerService";

    /**
     * personQueryService bean name.
     */
    public static final String PERSONQUERYSERVICE_BEAN_NAME = "personQueryService";

    /**
     * "Not available" string. Used for example as a value with on-line received dataset filenames.
     */
    public static final String NOT_AVAILABLE = "N/A";

    /**
     * Standard sleep time in milliseconds.
     */
    public static final Integer STANDARD_SLEEP_TIME = 10000;

    /**
     * Localhost IP address.
     */
    public static final String LOCALHOST_IP_ADDRESS = "127.0.0.1";

    /**
     * Localhost name.
     */
    public static final String LOCALHOST_NAME = "localhost";

    /**
     * encoding used when converting strings to byte arrays
     * TODO: make it configurable?
     */
	public static final Charset charset = Charset.forName("UTF-8");

    /**
     * Default bloom filter function name
     * TODO: make it configurable?
     */
	public static final String DEFAULT_BLOOM_FILTER_FUNCTION_NAME = "FastBloomFilterFunctionUnpaddedNgrams";

    /**
     * Default HMAC function name
     * TODO: make it configurable?
     */
	public static final String DEFAULT_HMAC_FUNCTION_NAME = "HMACSHA256Function";

    /**
     * name of the signing key extra parameter needed for HMAC type transformation functions
     */
	public static final String SIGNING_KEY_HMAC_PARAMETER_NAME = "signingKey";

    /**
     * name of the transformation which signals that the given field meant to not participate in the
     * record linkage, it should be just carried over as is
     */
	public static final String NO_COMPARISON_JUST_TRANSFER_FUNCTION_NAME = "NoComparisonJustTransfer";

}

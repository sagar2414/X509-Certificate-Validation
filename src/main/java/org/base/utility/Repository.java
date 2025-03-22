package org.base.utility;

public class Repository {
	private static final String PRIVATE_KEY_FILE = "key.pem";
	private static final String CERTIFICATE_FILE = "cert.pem";
	private static final String CERTIFICATE_DESTINATION = "./src/main/resources/certificatesDestination";
	private static final String SUBJECT_NAME = "/C=US/ST=California/L=San Francisco/O=MyCompany/OU=QA/CN=mydomain.com";
	private static final String EXPECTED_SUBJECT = "CN=mydomain.com,OU=QA,O=MyCompany,L=San Francisco,ST=California,C=US";
	private static final String EXPECTED_CN = "CN=mydomain.com";
	private static final String EXPECTED_ORG_NAME = "O=MyCompany";

	public static String getPrivateKeyPath() {
		return CERTIFICATE_DESTINATION + "/" + PRIVATE_KEY_FILE;
	}

	public static String getCertificatePath() {
		return CERTIFICATE_DESTINATION + "/" + CERTIFICATE_FILE;
	}

	public static String getCertificateFolder() {
		return CERTIFICATE_DESTINATION;
	}

	public static String getSubjectName() {
		return SUBJECT_NAME;
	}

	public static String getExpectedCommonName() {
		return EXPECTED_CN;
	}

	public static String getExpectedSubject() {
		return EXPECTED_SUBJECT;
	}

	public static String getExpectedOrgName() {
		return EXPECTED_ORG_NAME;
	}

}

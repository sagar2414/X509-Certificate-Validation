package org.testcases;

import java.util.Date;

import org.base.utility.Repository;
import org.testng.annotations.DataProvider;

public class BaseDataProvider {

	@DataProvider(name = "commonName")
	public Object[][] getCommonName() {
		Object[][] data = new Object[1][2];
		data[0][0] = Repository.getExpectedCommonName();
		data[0][1] = Repository.getExpectedOrgName();
		return data;
	}

	@DataProvider(name = "issuer")
	public Object[][] getIssuer() {
		return new Object[][] { { Repository.getExpectedOrgName() } };
	}

	@DataProvider(name = "validity")
	public Object[][] getValidityDetails() {
		Date currentDate = new Date();
		return new Object[][] { { currentDate } };
	}

	@DataProvider(name = "signatureAlgorithm")
	public Object[][] getSignatureAlgorithm() {
		return new Object[][] { { "SHA256", "RSA" } };
	}

	@DataProvider(name = "subject")
	public Object[][] getExpectedSubject() {
		return new Object[][] { { Repository.getExpectedSubject() } };
	}
}

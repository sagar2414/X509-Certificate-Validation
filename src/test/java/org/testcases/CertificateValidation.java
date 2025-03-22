package org.testcases;

import java.io.File;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.base.utility.CertificateUtility;
import org.base.utility.Repository;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CertificateValidation {

	private CertificateUtility certificateUtility;
	private static X509Certificate certificate;

	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		certificateUtility = new CertificateUtility();
		certificateUtility.generateCertificate();
		certificate = certificateUtility.loadCertificate(Repository.getCertificatePath());
	}

	@Test(priority = 1, dataProvider = "commonName", dataProviderClass = BaseDataProvider.class, description = "validation of common name from the certificate.", groups = {
			"positiveTestcases" })
	public void validateCommonName(String expectedCommonName, String expectOrgName) throws Exception {
		String subjectName = certificateUtility.getSubjectName(certificate);
		System.out.println(
				"***** TestCaseName : validateCommonName ***** \n Subject name extracted from certificate is : "
						+ subjectName + "\n");
		Assert.assertTrue(subjectName.contains(expectedCommonName), "Common name does not match!!!");
		Assert.assertTrue(subjectName.contains(expectOrgName), "Organization does not match!!!");
	}

	@Test(priority = 2, dataProvider = "issuer", dataProviderClass = BaseDataProvider.class, description = "validation of Issuer from the certificate.", groups = {
			"positiveTestcases" })
	public void validateIssuer(String expectedIssuerName) {
		String issuerName = certificateUtility.getIssuer(certificate);
		if (issuerName != null)
			System.out.println("***** TestCaseName : validateIssuer ***** \n Issuer found for the certificate : "
					+ issuerName + "\n");
		Assert.assertTrue(issuerName.contains(expectedIssuerName),
				"Issuer does not match with expected organisation name!!!");
	}

	@Test(priority = 3, dataProvider = "validity", dataProviderClass = BaseDataProvider.class, description = "validation of Expiration date of the certificate.", groups = {
			"positiveTestcases", "negativeTestcases" })
	public void validateExpiryCertificate(Date currentDate) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = certificate.getNotBefore();
		Date expiryDate = certificate.getNotAfter();
		if (currentDate.before(expiryDate)) {
			System.out.println("***** TestCaseName : validateExpiryCertificate ***** \n Ceritificate is not Expired.");
		}
		certificateUtility.checkCertificateValidity(certificate);
		Assert.assertTrue(currentDate.after(startDate),
				"Certificate is not yet active, as the start date is in future");
		Assert.assertTrue(currentDate.before(expiryDate), "Certificate is expired, as the start date is in future");

		System.out.println("Expiration Date of the ceritificate is : " + simpleDateFormat.format(expiryDate) + "\n");
	}

	@Test(priority = 4, dataProvider = "signatureAlgorithm", dataProviderClass = BaseDataProvider.class, description = "validation of signature algorithm of the certificate.", groups = {
			"positiveTestcases" })

	public void validateSignatureAlgorithm(String hashFunction, String encryptionAlgorithm) {
		String signatureAlgorithm = certificateUtility.getSignatureAlgorithm(certificate);
		if (signatureAlgorithm != null)
			System.out.println(
					"***** TestCaseName : validateSignatureAlgorithm ***** \n Signature Algorithm of the certificate is : "
							+ signatureAlgorithm + "\n");
		Assert.assertTrue(signatureAlgorithm.contains(hashFunction),
				"Signature algorithm doesn't include expected \"Cryptographic Hash Function\"!!!");
		Assert.assertTrue(signatureAlgorithm.contains(encryptionAlgorithm),
				"Signature algorithm doesn't include expected \"Asymmetric Encryption Algorithm\"!!!");
	}

	@Test(priority = 5, description = "verification of the certificate signature.", groups = { "positiveTestcases" })
	public void verifySignature() {
		System.out.println("***** TestCaseName : verifySignature *****");
		certificateUtility.verifySignature(certificate);
	}

	@Test(priority = 6, description = "verification of the self signed certificate.", groups = { "negativeTestcases" })
	public void verifySelfSignedCertificate() {
		if (certificateUtility.getSubjectName(certificate).equals(certificateUtility.getIssuer(certificate))) {
			System.out.println(
					"***** TestCaseName : verifySelfSignedCertificate ***** \n This is a self-signed certificate. \n");
		} else {
			System.out.println(
					"***** TestCaseName : verifySelfSignedCertificate ***** \n This is not a self-signed certificate.\n");
		}
	}

	@Test(priority = 7, dataProvider = "signatureAlgorithm", dataProviderClass = BaseDataProvider.class, description = "validation of the certificate if it is malformed.", groups = {
			"negativeTestcases" })
	public void verifyMalformedCertificate(String hashFunction, String encryptionAlgorithm) {
		try {
			if (certificate != null && certificateUtility.getIssuer(certificate) != null
					&& certificateUtility.getSubjectName(certificate) != null
					&& certificateUtility.getSignatureAlgorithm(certificate).contains(encryptionAlgorithm))
				System.out.println(
						"***** TestCaseName : verifyMalformedCertificate ***** \n The certificate is not malformed!!! \n");
			Assert.assertTrue(certificateUtility.getSignatureAlgorithm(certificate).contains(encryptionAlgorithm),
					"Certificate is malformed as the signature algorithm doesn't match with encryption algorithm.");
		} catch (Exception e) {
			System.out.println("The certificate is malformed!!!");
		}
	}

	@Test(priority = 8, description = "verification of public key.", groups = { "positiveTestcases" })

	public void verifyPublicKeyStrength() {
		PublicKey publicKey = certificateUtility.getPublicKey(certificate);
		PublicKey reconstructedKey = certificateUtility.getConstructedPublicKey(certificate, publicKey);
		Assert.assertEquals(publicKey, reconstructedKey, "Public key verification failed!");
		System.out
				.println("***** TestCaseName : verifyPublicKeyStrength ***** \n Public Key Verified Successfully! \n");
	}

	@Test(priority = 0, description = "Validate that the generated certificate is a valid X509 certificate", groups = {
			"positiveTestcases" })
	public void validateCertificateIsX509() {
		File certificate = certificateUtility.getFile(Repository.getCertificatePath());
		Assert.assertTrue(certificate.exists(), "Certificate file should exist");
		if (certificate != null && CertificateValidation.certificate != null)
			System.out
					.println("***** TestCaseName : validateCertificateIsX509 ***** \n Certificate is a valid X509 \n");
		assert certificate != null && CertificateValidation.certificate != null : " Certificate should not be null";
	}

	@Test(priority = 9, dataProvider = "subject", dataProviderClass = BaseDataProvider.class, description = "verify the subject of the certificate", groups = {
			"positiveTestcases" })

	public void verifyCertificateSubject(String expectedSubject) {
		String subjectName = certificateUtility.getSubjectName(certificate);
		if (subjectName.equals(expectedSubject))
			System.out.println(
					"***** TestCaseName : verifyCertificateSubject ***** \n Certificate subject is as expected!!! \n");
		Assert.assertEquals(subjectName, expectedSubject,
				"Subject Distinguished Name does't match with the expected value");
	}

	@Test(priority = 10, description = "Validate the serial number of the certificate", groups = {
			"positiveTestcases" })
	public void validateSerialNumber() {
		BigInteger serialNumber = certificateUtility.getSerialNumber(certificate);
		System.out.println("***** TestCaseName : validateSerialNumber ***** \n Serial number of the certificate is : "
				+ serialNumber + "\n");
		assert serialNumber.compareTo(BigInteger.ZERO) > 0;
	}
}
package org.base.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CertificateUtility {

	CertificateFactory certificateFactory;

	public void generateCertificate() {
		File certificateDestination;
		String[] command = { "openssl", "req", "-x509", "-newkey", "rsa:2048", "-keyout",
				Repository.getPrivateKeyPath(), "-out", Repository.getCertificatePath(), "-days", "365", "-nodes",
				"-subj", Repository.getSubjectName() };

		try {
			certificateDestination = getFile(Repository.getCertificateFolder());
			if (!certificateDestination.exists()) {
				certificateDestination.mkdirs();
			}

			// Create a ProcessBuilder instance
			ProcessBuilder processBuilder = new ProcessBuilder(command);

			// Redirect error messages to the console
			processBuilder.redirectErrorStream(true);

			// Start the process and wait for it to finish
			Process process = processBuilder.start();
			int exitCode = process.waitFor();

			if (exitCode == 0) {
				System.out.println("Certificate generated successfully. cert.pem and key.pem files created.");
			} else {
				System.out.println("Failed to generate certificate. Error code: " + exitCode);
			}
		} catch (IOException | InterruptedException e) {
			System.err.println("Error generating certificate: " + e.getMessage());
		}
	}

	public X509CRL loadCRL(String crlFile) throws Exception {
		try (FileInputStream fis = new FileInputStream(crlFile)) {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			return (X509CRL) certFactory.generateCRL(fis);
		}
	}

	public X509Certificate loadCertificate(String certFile) {
		X509Certificate certificate = null;
		try (FileInputStream fileInputStream = new FileInputStream(certFile)) {
			certificateFactory = CertificateFactory.getInstance("X.509");
			certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
		} catch (IOException | CertificateException e) {
			e.printStackTrace();
		}
		return certificate;
	}

	public void checkCertificateValidity(X509Certificate certificate) {
		Date currentDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			certificate.checkValidity();
			System.out.println("Certificate is valid as on : " + simpleDateFormat.format(currentDate));
		} catch (CertificateNotYetValidException | CertificateExpiredException e) {
			e.printStackTrace();
		}
	}

	public String getSubjectName(X509Certificate certificate) {
		return certificate.getSubjectX500Principal().getName();
	}

	public String getIssuer(X509Certificate certificate) {
		return certificate.getIssuerX500Principal().getName();
	}

	public String getSignatureAlgorithm(X509Certificate certificate) {
		return certificate.getSigAlgName();
	}

	public PublicKey getPublicKey(X509Certificate certificate) {
		return certificate.getPublicKey();
	}

	public BigInteger getSerialNumber(X509Certificate certificate) {
		return certificate.getSerialNumber();
	}

	public PublicKey getConstructedPublicKey(X509Certificate certificate, PublicKey publicKey) {
		KeyFactory keyFactory = null;
		PublicKey reconstructedKey = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
			reconstructedKey = keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return reconstructedKey;
	}

	public void verifySignature(X509Certificate certificate) {
		try {
			certificate.verify(certificate.getPublicKey());
			System.out.println("certificate is verified!!! \n");
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			e.printStackTrace();
		}
	}

	public void cleanupFiles() {
		deleteFile(Repository.getPrivateKeyPath());
		deleteFile(Repository.getCertificatePath());
		System.out.println("Certificate generated are cleaned-up successfully for upcoming execution.");
	}

	private void deleteFile(String filePath) {
		File file = getFile(filePath);
		if (file.exists()) {
			boolean deleted = file.delete();
			if (!deleted) {
				throw new RuntimeException("Failed to delete the file at : " + filePath);
			}
		}
	}

	public File getFile(String filePath) {
		return new File(filePath);
	}
}
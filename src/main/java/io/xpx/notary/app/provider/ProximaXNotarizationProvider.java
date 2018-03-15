package io.xpx.notary.app.provider;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import craterdog.primitives.Tag;
import craterdog.notary.CertificateAttributes;
import craterdog.notary.DocumentCitation;
import craterdog.notary.Notarization;
import craterdog.notary.NotaryCertificate;
import craterdog.notary.NotaryKey;
import craterdog.notary.NotarySeal;
import craterdog.notary.SealAttributes;
import craterdog.notary.ValidationException;
import craterdog.notary.Watermark;
import craterdog.notary.mappers.NotaryModule;
import craterdog.security.MessageCryptex;
import craterdog.security.RsaAesMessageCryptex;
import craterdog.smart.SmartObject;
import craterdog.utils.Base32Utils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * The Class SmartProofNotarizationProvider.
 */
public final class ProximaXNotarizationProvider implements Notarization {

	/** The Constant logger. */
	static private final XLogger logger = XLoggerFactory.getXLogger(ProximaXNotarizationProvider.class);

	/** The Constant cryptex. */
	static private final MessageCryptex cryptex = new RsaAesMessageCryptex();

	/**
	 * The hashing algorithm used to generate hash values for the documents.
	 */
	public final String hashingAlgorithm = cryptex.getHashAlgorithm();

	/**
	 * The signing algorithm used to sign and verify the documents.
	 */
	public final String signingAlgorithm = cryptex.getAsymmetricSignatureAlgorithm();

	/**
	 * The major version number of the implementation of this digital notary.
	 */
	public final int majorVersion = 1;

	/**
	 * The minor version number of the implementation of this digital notary.
	 */
	public final int minorVersion = 0;

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#generateWatermark(int)
	 */
	@Override
	public Watermark generateWatermark(int secondsToLive) {
		logger.entry(secondsToLive);
		Watermark watermark = new Watermark();
		watermark.hashingAlgorithm = hashingAlgorithm;
		watermark.signingAlgorithm = signingAlgorithm;
		watermark.majorVersion = majorVersion;
		watermark.minorVersion = minorVersion;
		watermark.creationTimestamp = DateTime.now();
		watermark.expirationTimestamp = watermark.creationTimestamp.plusSeconds(secondsToLive);
		logger.exit(watermark);
		return watermark;
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#validateWatermark(craterdog.notary.Watermark, java.util.Map)
	 */
	@Override
	public void validateWatermark(Watermark watermark, Map<String, Object> errors) {
		logger.entry(watermark, errors);
		if (watermark == null) {
			logger.error("The watermark is missing...");
			errors.put("watermark.is.missing", watermark);
		} else {
			if (watermark.hashingAlgorithm == null) {
				logger.error("The watermark hashing algorithm is missing...");
				errors.put("watermark.hashing.algorithm.is.missing", watermark);
			}
			if (watermark.signingAlgorithm == null) {
				logger.error("The watermark signing algorithm is missing...");
				errors.put("watermark.signing.algorithm.is.missing", watermark);
			}
			if (watermark.creationTimestamp == null) {
				logger.error("The watermark creation timestamp is missing...");
				errors.put("watermark.creation.timestamp.is.missing", watermark);
			}
			if (watermark.expirationTimestamp == null) {
				logger.error("The watermark expiration timestamp is missing...");
				errors.put("watermark.expiration.timestamp.is.missing", watermark);
			} else if (watermark.expirationTimestamp.isBeforeNow()) {
				logger.error("The watermark has expired...");
				errors.put("watermark.has.expired", watermark);
			}
		}
		logger.exit(errors);
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#generateDocumentCitation(java.net.URI, java.lang.String)
	 */
	@Override
	public DocumentCitation generateDocumentCitation(URI location, String document) {
		logger.entry(location, document);
		DocumentCitation citation = new DocumentCitation();
		citation.documentLocation = location;
		citation.documentHash = hashDocument(document);
		logger.exit(citation);
		return citation;
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#validateDocumentCitation(craterdog.notary.DocumentCitation, java.lang.String, java.util.Map)
	 */
	@Override
	public void validateDocumentCitation(DocumentCitation citation, String document, Map<String, Object> errors) {
		logger.entry(citation, document, errors);
		if (citation == null) {
			logger.error("The document citation is missing...");
			errors.put("citation.is.missing", citation);
		} else {
			if (citation.documentLocation == null) {
				logger.error("The document citation location is missing...");
				errors.put("citation.location.is.missing", citation);
			}
			if (citation.documentHash == null || citation.documentHash.isEmpty()) {
				logger.error("The document citation hash is missing...");
				errors.put("citation.hash.is.missing", citation);
			}
			if (!citation.documentHash.equals(hashDocument(document))) {
				logger.error("The document citation hash does not match the document hash...");
				errors.put("citation.hash.is.invalid", citation);
				errors.put("cited.document.does.not.match", document);
			}
		}
		logger.exit(errors);
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#generateNotaryKey(java.net.URI)
	 */
	@Override
	public NotaryKey generateNotaryKey(URI baseUri) {
		logger.entry(baseUri);
		NotaryKey notaryKey = generateNotaryKey(baseUri, null, null);
		logger.exit(notaryKey);
		return notaryKey;
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#generateNotaryKey(java.net.URI, java.util.Map)
	 */
	@Override
	public NotaryKey generateNotaryKey(URI baseUri, Map<String, Object> additionalAttributes) {
		logger.entry(baseUri, additionalAttributes);
		NotaryKey notaryKey = generateNotaryKey(baseUri, additionalAttributes, null);
		logger.exit(notaryKey);
		return notaryKey;
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#generateNotaryKey(java.net.URI, craterdog.notary.NotaryKey)
	 */
	@Override
	public NotaryKey generateNotaryKey(URI baseUri, NotaryKey previousKey) {
		logger.entry(baseUri, previousKey);
		NotaryKey notaryKey = generateNotaryKey(baseUri, null, previousKey);
		logger.exit(notaryKey);
		return notaryKey;
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#generateNotaryKey(java.net.URI, java.util.Map, craterdog.notary.NotaryKey)
	 */
	@Override
	public NotaryKey generateNotaryKey(URI baseUri, Map<String, Object> additionalAttributes, NotaryKey previousKey) {
		logger.entry(baseUri, additionalAttributes, previousKey);

		logger.debug("Generating a new RSA key pair...");
		KeyPair keyPair = cryptex.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		logger.debug("Creating the watermark...");
		Watermark watermark = generateWatermark(VALID_FOR_ONE_YEAR);

		logger.debug("Wrapping the verification key in a certificate...");
		NotaryCertificate certificate = generateNotaryCertificate(baseUri, publicKey, privateKey, additionalAttributes,
				watermark, previousKey);

		logger.debug("Creating a document citation to the verification certificate...");
		URI documentLocation = certificate.attributes.myLocation;
		String document = certificate.toString();
		DocumentCitation citation = generateDocumentCitation(documentLocation, document);

		logger.debug("Assembling the notary key...");
		NotaryKey notaryKey = new NotaryKey();
		notaryKey.watermark = watermark;
		notaryKey.signingKey = privateKey;
		notaryKey.verificationCertificate = certificate;
		notaryKey.verificationCitation = citation;

		logger.exit(notaryKey);
		return notaryKey;
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#serializeNotaryKey(craterdog.notary.NotaryKey, char[])
	 */
	@Override
	public String serializeNotaryKey(NotaryKey notaryKey, char[] password) {
		logger.entry(notaryKey);

		logger.debug("Marshalling the notary key into a JSON string...");
		String json;
		try {
			ObjectMapper mapper = SmartObject.createMapper(new NotaryModule(password));
			json = mapper.writeValueAsString(notaryKey);
		} catch (Exception e) {
			RuntimeException exception = new RuntimeException(
					"An unexpected exception occurred while attempting to serialize a notary key.", e);
			throw logger.throwing(exception);
		}

		logger.exit(json);
		return json;
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#deserializeNotaryKey(java.lang.String, char[])
	 */
	@Override
	public NotaryKey deserializeNotaryKey(String json, char[] password) throws IOException {
		logger.entry(json);

		logger.debug("Unmarshalling the notary key from a JSON string...");
		NotaryKey notaryKey;
		try {
			ObjectMapper mapper = SmartObject.createMapper(new NotaryModule(password));
			notaryKey = mapper.readValue(json, NotaryKey.class);
		} catch (JsonMappingException e) {
			String messageTag = "invalid.notary.key.password";
			Map<String, Object> errors = new LinkedHashMap<>();
			errors.put("json.string", json);
			logger.error("The notary key password is invalid for the following notary key: {}", json);
			throw new ValidationException(messageTag, errors);
		} catch (Exception e) {
			RuntimeException exception = new RuntimeException(
					"An unexpected exception occurred while attempting to deserialize a notary key: " + json, e);
			throw logger.throwing(exception);
		}

		logger.debug("Validating the notary key...");
		Map<String, Object> errors = new LinkedHashMap<>();
		validateNotaryKey(notaryKey, errors);
		throwExceptionOnErrors("invalid.serialized.notary.key", errors);

		logger.exit(notaryKey);
		return notaryKey;
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#validateNotaryCertificate(craterdog.notary.NotaryCertificate, craterdog.notary.NotaryCertificate, java.util.Map)
	 */
	@Override
	public void validateNotaryCertificate(NotaryCertificate certificate, NotaryCertificate previousCertificate,
			Map<String, Object> errors) {
		logger.entry(certificate, previousCertificate);
		int errorCount = errors.size(); // record it to see if it changes

		logger.debug("Validating the certificate attributes...");
		validateNotaryCertificate(certificate, errors);

		logger.debug("Validating the certificate attributes...");
		validateNotaryCertificate(previousCertificate, errors);

		if (errors.size() == errorCount) {
			// no new errors, so parameters should be valid
			logger.debug("Validating the certificate seal...");
			validateNotarySeal(certificate.certificationSeal, previousCertificate, errors);
		}

		logger.exit();
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#notarizeDocument(java.lang.String, java.lang.String, craterdog.notary.NotaryKey)
	 */
	@Override
	public NotarySeal notarizeDocument(String documentType, String document, NotaryKey notaryKey) {
		logger.entry(documentType, document, notaryKey);

		logger.debug("Verifying that the notary key has not expired...");
		Map<String, Object> errors = new LinkedHashMap<>();
		Watermark watermark = notaryKey.watermark;
		validateWatermark(watermark, errors);
		throwExceptionOnErrors("notary.key.has.expired", errors);

		logger.debug("Creating the notary seal attributes...");
		SealAttributes attributes = new SealAttributes();
		attributes.documentType = documentType;
		attributes.documentHash = hashDocument(document);
		attributes.verificationCitation = notaryKey.verificationCitation;
		attributes.watermark = generateWatermark(Notarization.VALID_FOR_FOREVER);

		logger.debug("Signing the notary seal...");
		NotarySeal seal = new NotarySeal();
		PrivateKey signingKey = notaryKey.signingKey;
		seal.attributes = attributes;
		seal.selfSignature = generateDocumentSignature(attributes.toString(), signingKey);

		logger.exit(seal);
		return seal;
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#validateDocument(java.lang.String, craterdog.notary.NotarySeal, craterdog.notary.NotaryCertificate, java.util.Map)
	 */
	@Override
	public void validateDocument(String document, NotarySeal seal, NotaryCertificate certificate,
			Map<String, Object> errors) {
		logger.entry(document, seal, certificate, errors);
		int errorCount = errors.size(); // record it to see if it changes

		logger.debug("Validating the notary certificate...");
		validateNotaryCertificate(certificate, errors);

		logger.debug("Validating the digital seal...");
		validateNotarySeal(seal, certificate, errors);

		if (document == null || document.isEmpty()) {
			logger.error("The document to be validated is missing...");
			errors.put("document.is.missing", document);
		}
		if (errorCount == errors.size()) {
			// no new errors, so parameters should be valid
			logger.debug("Validating the hash of the document...");
			String documentHash = seal.attributes.documentHash;
			if (!documentHash.equals(hashDocument(document))) {
				logger.error("The document hash does not match the hash in the notary seal...");
				errors.put("document.hash.is.invalid", document);
			}
		}

		logger.exit(errors);
	}

	/* (non-Javadoc)
	 * @see craterdog.notary.Notarization#throwExceptionOnErrors(java.lang.String, java.util.Map)
	 */
	@Override
	public void throwExceptionOnErrors(String messageTag, Map<String, Object> errors) throws ValidationException {
		logger.entry(messageTag, errors);
		if (!errors.isEmpty()) {
			logger.error("A validation exception \"" + messageTag + "\" was thrown with the following errors: {}",
					errors);
			throw new ValidationException(messageTag, errors);
		}
		logger.exit();
	}

	/**
	 * Validate notary key.
	 *
	 * @param notaryKey the notary key
	 * @param errors the errors
	 */
	private void validateNotaryKey(NotaryKey notaryKey, Map<String, Object> errors) {
		int errorCount = errors.size(); // record it to see if it changes

		logger.debug("Validating the watermark for the notary key...");
		validateWatermark(notaryKey.watermark, errors);

		logger.debug("Validating the signing key for the notary key...");
		validateSigningKey(notaryKey.signingKey, errors);

		logger.debug("Validating the verification certificate for the notary key...");
		validateNotaryCertificate(notaryKey.verificationCertificate, errors);

		if (errorCount == errors.size()) {
			// no new errors, so parameters should be valid
			logger.debug("Validating the certificate citation for the notary key...");
			DocumentCitation citation = notaryKey.verificationCitation;
			String document = notaryKey.verificationCertificate.toString();
			validateDocumentCitation(citation, document, errors);
		}
	}

	/**
	 * Validate signing key.
	 *
	 * @param signingKey the signing key
	 * @param errors the errors
	 */
	private void validateSigningKey(PrivateKey signingKey, Map<String, Object> errors) {
		if (signingKey == null) {
			logger.error("The signing key is missing...");
			errors.put("signing.key.is.missing", signingKey);
		}
	}

	/**
	 * Validate notary seal.
	 *
	 * @param seal the seal
	 * @param certificate the certificate
	 * @param errors the errors
	 */
	private void validateNotarySeal(NotarySeal seal, NotaryCertificate certificate, Map<String, Object> errors) {
		if (seal == null) {
			logger.error("The notary seal is missing...");
			errors.put("seal.is.missing", seal);
		} else {
			int errorCount = errors.size(); // record it to see if it changes
			String selfSignature = seal.selfSignature;
			if (selfSignature == null) {
				logger.error("The notary seal self signature is missing...");
				errors.put("seal.self.signature.is.missing", seal);
			}
			SealAttributes attributes = seal.attributes;
			if (attributes == null) {
				logger.error("The notary seal attributes are missing...");
				errors.put("seal.attributes.are.missing", seal);
			} else {
				String documentType = attributes.documentType;
				if (documentType == null || documentType.isEmpty()) {
					logger.error("The notary seal document type is missing...");
					errors.put("seal.document.type.is.missing", seal);
				}
				String documentHash = attributes.documentHash;
				if (documentHash == null || documentHash.isEmpty()) {
					logger.error("The notary seal document hash is missing...");
					errors.put("seal.document.hash.is.missing", seal);
				}
				Watermark watermark = attributes.watermark;
				if (watermark == null) {
					logger.error("The notary seal watermark is missing...");
					errors.put("seal.watermark.is.missing", seal);
				}
				DocumentCitation verificationCitation = attributes.verificationCitation;
				if (verificationCitation == null) {
					logger.error("The notary seal verification citation is missing...");
					errors.put("seal.verification.citation.is.missing", seal);
				}
			}
			if (errors.size() == errorCount) {
				// no new errors, so parameters should be valid
				PublicKey verificationKey = certificate.attributes.verificationKey;
				String document = seal.attributes.toString();
				validateDocumentSignature(document, selfSignature, verificationKey, errors);
				DocumentCitation verificationCitation = seal.attributes.verificationCitation;
				validateDocumentCitation(verificationCitation, certificate.toString(), errors);
			}
		}
	}

	/**
	 * Generate notary certificate.
	 *
	 * @param baseUri the base uri
	 * @param publicKey the public key
	 * @param privateKey the private key
	 * @param additionalAttributes the additional attributes
	 * @param watermark the watermark
	 * @param previousKey the previous key
	 * @return the notary certificate
	 */
	private NotaryCertificate generateNotaryCertificate(URI baseUri, PublicKey publicKey, PrivateKey privateKey,
			Map<String, Object> additionalAttributes, Watermark watermark, NotaryKey previousKey) {
		CertificateAttributes attributes = new CertificateAttributes();
		try {
			attributes.myLocation = new URI(baseUri + "/certificate/" + new Tag());
			if (previousKey != null) {
				// the identity already exists so use it
				attributes.identityLocation = previousKey.verificationCertificate.attributes.identityLocation;
				attributes.sequenceNumber = previousKey.verificationCertificate.attributes.sequenceNumber + 1;
			} else {
				// this is a new identity
				attributes.identityLocation = new URI(baseUri + "/identity/" + new Tag());
				attributes.sequenceNumber = 1; // first one in the list
			}
		} catch (URISyntaxException e) {
			RuntimeException exception = new RuntimeException(
					"An unexpected exception occurred while attempting to create location URIs from base URI: "
							+ baseUri,
					e);
			throw logger.throwing(exception);
		}
		attributes.verificationKey = publicKey;
		if (additionalAttributes != null) {
			for (Map.Entry<String, Object> attribute : additionalAttributes.entrySet()) {
				attributes.put(attribute.getKey(), attribute.getValue());
			}
		}
		attributes.watermark = watermark;
		NotaryCertificate certificate = new NotaryCertificate();
		certificate.attributes = attributes;
		certificate.selfSignature = generateDocumentSignature(attributes.toString(), privateKey);
		if (previousKey != null) {
			String documentType = "Self Signature";
			String document = certificate.selfSignature;
			certificate.certificationSeal = notarizeDocument(documentType, document, previousKey);
		}
		return certificate;
	}

	/**
	 * Validate notary certificate.
	 *
	 * @param certificate the certificate
	 * @param errors the errors
	 */
	private void validateNotaryCertificate(NotaryCertificate certificate, Map<String, Object> errors) {
		if (certificate == null) {
			logger.error("The notary certificate is missing...");
			errors.put("certificate.is.missing", certificate);
		} else {
			int errorCount = errors.size(); // record it to see if it changes
			CertificateAttributes attributes = certificate.attributes;
			if (attributes == null) {
				logger.error("The notary certificate attributes are missing...");
				errors.put("certificate.attributes.are.missing", certificate);
			} else {
				if (attributes.myLocation == null) {
					logger.error("The notary certificate location is missing...");
					errors.put("certificate.location.is.missing", certificate);
				}
				if (attributes.identityLocation == null) {
					logger.error("The notary certificate identity location is missing...");
					errors.put("certificate.identity.location.is.missing", certificate);
				}
				if (attributes.watermark == null) {
					logger.error("The notary certificate watermark is missing...");
					errors.put("certificate.watermark.is.missing", certificate);
				}
				if (attributes.verificationKey == null) {
					logger.error("The notary certificate verification key is missing...");
					errors.put("certificate.verification.key.is.missing", certificate);
				}
			}
			if (errors.size() == errorCount) {
				// the error count did not change so the parameters should be
				// valid
				String document = certificate.attributes.toString();
				String signature = certificate.selfSignature;
				PublicKey verificationKey = certificate.attributes.verificationKey;
				validateDocumentSignature(document, signature, verificationKey, errors);
			}

			// We cannot valid the certification seal without the previous
			// certificate
			// and we cannot pass in the previous certificate since this ends up
			// being
			// recursive. It is up to the higher level services to validate the
			// chain.
		}
	}

	/**
	 * Generate document signature.
	 *
	 * @param document the document
	 * @param signingKey the signing key
	 * @return the string
	 */
	private String generateDocumentSignature(String document, PrivateKey signingKey) {
		try {
			byte[] documentBytes = document.getBytes("UTF-8");
			byte[] signatureBytes = cryptex.signBytes(signingKey, documentBytes);
			String signature = Base32Utils.encode(signatureBytes);
			return signature;
		} catch (Exception e) {
			RuntimeException exception = new RuntimeException(
					"An unexpected exception occurred while attempting to notarize the following document: " + document,
					e);
			throw logger.throwing(exception);
		}
	}

	/**
	 * Validate document signature.
	 *
	 * @param document the document
	 * @param signature the signature
	 * @param verificationKey the verification key
	 * @param errors the errors
	 */
	private void validateDocumentSignature(String document, String signature, PublicKey verificationKey,
			Map<String, Object> errors) {
		try {
			byte[] documentBytes = document.getBytes("UTF-8");
			byte[] signatureBytes = Base32Utils.decode(signature);
			if (!cryptex.bytesAreValid(verificationKey, documentBytes, signatureBytes)) {
				logger.error("The document signature is not valid...");
				errors.put("document.is.not.valid", document);
				errors.put("document.signature.is.not.valid", signature);
				errors.put("document.verification.key.does.not.match", verificationKey);
			}
		} catch (UnsupportedEncodingException e) {
			RuntimeException exception = new RuntimeException(
					"An unexpected exception occurred while attempting to validate the following document: " + document,
					e);
			throw logger.throwing(exception);
		}
	}

	/**
	 * Hash document.
	 *
	 * @param document the document
	 * @return the string
	 */
	private String hashDocument(String document) {
		try {
			byte[] bytes = document.getBytes();
			MessageDigest hasher = MessageDigest.getInstance("SHA-256");
			byte[] hash = hasher.digest(bytes);
			String hashString = Base32Utils.encode(hash);
			return hashString;
		} catch (NoSuchAlgorithmException e) {
			RuntimeException exception = new RuntimeException(
					"An unexpected exception occurred while attempting to hash the following document: " + document, e);
			throw logger.throwing(exception);
		}
	}
}

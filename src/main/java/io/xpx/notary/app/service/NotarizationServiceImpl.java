package io.xpx.notary.app.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;
import craterdog.notary.Notarization;
import craterdog.notary.NotaryCertificate;
import craterdog.notary.NotaryKey;
import craterdog.notary.NotarySeal;
import io.xpx.notary.app.provider.ProximaXNotarizationProvider;


/**
 * The Class NotarizationServiceImpl.
 */
public class NotarizationServiceImpl implements NotarizationService {

	/** The instance. */
	private static NotarizationServiceImpl instance;

	/**
	 * Instantiates a new notarization service impl.
	 */
	private NotarizationServiceImpl() {}

	/**
	 * Gets the single instance of NotarizationServiceImpl.
	 *
	 * @return single instance of NotarizationServiceImpl
	 */
	public static NotarizationServiceImpl getInstance() {
		if (instance == null) {
			instance = new NotarizationServiceImpl();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see io.nem.spfs.core.notarization.service.NotarizationService#extractPublicNotaryCertificate(craterdog.notary.NotaryKey)
	 */
	@Override
	public NotaryCertificate extractPublicNotaryCertificate(NotaryKey notaryKey) {
		NotaryCertificate certificate = notaryKey.verificationCertificate;
		return certificate;
	}

	/* (non-Javadoc)
	 * @see io.nem.spfs.core.notarization.service.NotarizationService#initializeDefaultNotarizationProvider()
	 */
	@Override
	public Notarization initializeDefaultNotarizationProvider() {
		return new ProximaXNotarizationProvider(); // alternative to V1NotarizationProvider
	}

	/* (non-Javadoc)
	 * @see io.nem.spfs.core.notarization.service.NotarizationService#generateNotaryKey(craterdog.notary.Notarization, java.lang.String)
	 */
	@Override
	public NotaryKey generateNotaryKey(Notarization notarization, String baseUri) throws URISyntaxException {
		URI baseURI = new URI(baseUri);
		NotaryKey notaryKey = notarization.generateNotaryKey(baseURI);
		return notaryKey;
	}

	/* (non-Javadoc)
	 * @see io.nem.spfs.core.notarization.service.NotarizationService#serializeNotaryKey(craterdog.notary.Notarization, craterdog.notary.NotaryKey, java.lang.String)
	 */
	@Override
	public String serializeNotaryKey(Notarization notarization, NotaryKey notaryKey, String password) {
		String json = notarization.serializeNotaryKey(notaryKey, password.toCharArray());
		return json;
	}

	/* (non-Javadoc)
	 * @see io.nem.spfs.core.notarization.service.NotarizationService#deserializeNotaryKey(craterdog.notary.Notarization, java.lang.String, java.lang.String)
	 */
	@Override
	public NotaryKey deserializeNotaryKey(Notarization notarization, String json, String password) throws IOException {
		NotaryKey copy = notarization.deserializeNotaryKey(json, password.toCharArray());
		return copy;
	}

	/* (non-Javadoc)
	 * @see io.nem.spfs.core.notarization.service.NotarizationService#notarizeDocument(craterdog.notary.Notarization, java.lang.String, java.lang.String, craterdog.notary.NotaryKey)
	 */
	@Override
	public NotarySeal notarizeDocument(Notarization notarization, String documentType, String document,
			NotaryKey notaryKey) {
		NotarySeal seal = notarization.notarizeDocument(documentType, document, notaryKey);
		return seal;
	}

	/* (non-Javadoc)
	 * @see io.nem.spfs.core.notarization.service.NotarizationService#renewNotaryKey(craterdog.notary.Notarization, craterdog.notary.NotaryKey, java.lang.String)
	 */
	@Override
	public NotaryKey renewNotaryKey(Notarization notarization, NotaryKey notaryKey, String baseUri)
			throws URISyntaxException {
		NotaryKey renewedNotaryKey = notarization.generateNotaryKey(new URI(baseUri), notaryKey);
		return renewedNotaryKey;
	}

	/* (non-Javadoc)
	 * @see io.nem.spfs.core.notarization.service.NotarizationService#validateNotarySeal(craterdog.notary.Notarization, java.lang.String, craterdog.notary.NotarySeal, craterdog.notary.NotaryCertificate)
	 */
	@Override
	public Map<String, Object> validateNotarySeal(Notarization notarization, String document, NotarySeal seal,
			NotaryCertificate certificate) {
		Map<String, Object> errors = new LinkedHashMap<>();
		notarization.validateDocument(document, seal, certificate, errors);
		return errors;
	}

}

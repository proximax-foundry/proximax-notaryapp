package io.xpx.notary.app.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import craterdog.notary.Notarization;
import craterdog.notary.NotaryCertificate;
import craterdog.notary.NotaryKey;
import craterdog.notary.NotarySeal;


/**
 * The Interface NotarizationService.
 */
public interface NotarizationService {
	
	/**
	 * Initialize default notarization provider.
	 *
	 * @return the notarization
	 */
	public Notarization initializeDefaultNotarizationProvider();

	/**
	 * Generate notary key.
	 *
	 * @param notarization the notarization
	 * @param baseUri the base uri
	 * @return the notary key
	 * @throws URISyntaxException the URI syntax exception
	 */
	public NotaryKey generateNotaryKey(Notarization notarization, String baseUri) throws URISyntaxException;

	/**
	 * Serialize notary key.
	 *
	 * @param notarization the notarization
	 * @param notaryKey the notary key
	 * @param password the password
	 * @return the string
	 */
	public String serializeNotaryKey(Notarization notarization, NotaryKey notaryKey, String password);

	/**
	 * Deserialize notary key.
	 *
	 * @param notarization the notarization
	 * @param json the json
	 * @param password the password
	 * @return the notary key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public NotaryKey deserializeNotaryKey(Notarization notarization, String json, String password) throws IOException;
	
	/**
	 * Extract public notary certificate.
	 *
	 * @param notaryKey the notary key
	 * @return the notary certificate
	 */
	public NotaryCertificate extractPublicNotaryCertificate(NotaryKey notaryKey);
	
	/**
	 * Notarize document.
	 *
	 * @param notarization the notarization
	 * @param documentType the document type
	 * @param document the document
	 * @param notaryKey the notary key
	 * @return the notary seal
	 */
	public NotarySeal notarizeDocument(Notarization notarization, String documentType, String document,
			NotaryKey notaryKey);

	/**
	 * Renew notary key.
	 *
	 * @param notarization the notarization
	 * @param notaryKey the notary key
	 * @param baseUri the base uri
	 * @return the notary key
	 * @throws URISyntaxException the URI syntax exception
	 */
	public NotaryKey renewNotaryKey(Notarization notarization, NotaryKey notaryKey, String baseUri)
			throws URISyntaxException;

	/**
	 * Validate notary seal.
	 *
	 * @param notarization the notarization
	 * @param document the document
	 * @param seal the seal
	 * @param certificate the certificate
	 * @return the map
	 */
	public Map<String, Object> validateNotarySeal(Notarization notarization, String document, NotarySeal seal,
			NotaryCertificate certificate);
}

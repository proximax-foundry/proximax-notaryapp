package io.xpx.notary.app.factory;

import io.xpx.notary.app.service.NotarizationServiceImpl;

/**
 * A factory for creating CoreService objects.
 */
public class CoreServiceFactory {


	/**
	 * Initialize notarization service.
	 *
	 * @return the notarization service impl
	 */
	public static NotarizationServiceImpl initializeNotarizationService() {
		return NotarizationServiceImpl.getInstance();
	}

}

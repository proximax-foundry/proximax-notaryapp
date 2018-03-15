package io.xpx.notary.app.factory;

import io.xpx.notary.app.service.NotarizationServiceImpl;
import io.xpx.notary.app.service.ProximaXServiceImpl;

/**
 * A factory for creating CoreService objects.
 */
public class CoreServiceFactory {


	public static NotarizationServiceImpl initializeNotarizationService() {
		return NotarizationServiceImpl.getInstance();
	}
	
	public static ProximaXServiceImpl initializeProximaXService() {
		return ProximaXServiceImpl.getInstance();
	}

}

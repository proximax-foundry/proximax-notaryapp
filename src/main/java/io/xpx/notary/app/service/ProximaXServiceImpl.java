package io.xpx.notary.app.service;

public class ProximaXServiceImpl implements ProximaXService {

	private static ProximaXServiceImpl instance;

	private ProximaXServiceImpl() {}


	public static ProximaXServiceImpl getInstance() {
		if (instance == null) {
			instance = new ProximaXServiceImpl();
		}
		return instance;
	}
}

package io.xpx.notary.app.process;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompHeaders;
import io.nem.monitor.handler.TransactionMonitorHandler;

public class NemConfirmedTransactionMonitor extends TransactionMonitorHandler {
	@Override
	public Type getPayloadType(StompHeaders headers) {
		return String.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		System.out.println(payload.toString()); // handle the payload.
	}
}

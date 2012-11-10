package de.uni.due.paluno.casestudy.cep;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import de.uni.due.paluno.casestudy.services.cosm.COSMWebSocketEngine;

public class COSMWebSocketEngineTest {

	@Test
	public void testEngine() throws IOException, ExecutionException,
			InterruptedException {
		Set<String> set = new HashSet<String>();
		set.add("/feeds/42055");

		COSMWebSocketEngine engine = new COSMWebSocketEngine(set);
		engine.start();

		Thread.sleep(15000000);
	}
}

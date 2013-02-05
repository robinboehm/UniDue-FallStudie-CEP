package de.uni.due.paluno.casestudy.cep;

import de.uni.due.paluno.casestudy.services.cosm.impl.COSMWebSocketEngineImpl;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class COSMWebSocketEngineTest {


	@Test
	public void testEngine() throws IOException, ExecutionException,
			InterruptedException {
		Set<String> set = new HashSet<String>();
		set.add("/feeds/42055");

		COSMWebSocketEngineImpl engine = new COSMWebSocketEngineImpl(set);
		engine.start();

		Thread.sleep(10000);
	}
}

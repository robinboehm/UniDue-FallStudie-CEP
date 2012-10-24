package de.uni.due.paluno.casestudy.cep;

import de.uni.due.paluno.casestudy.cep.events.command.WaypointTemperatureDumper;
import de.uni.due.paluno.casestudy.delivery.cosm.COSMWebSocketEngine;

import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CosmEsperIntegrationTest {

    @Test
    public void testCosmEsperIntegration() throws IOException, ExecutionException, InterruptedException {

        EsperCOSMAdapter etf = new EsperCOSMAdapter();
        etf.addToConfig(new WaypointTemperatureDumper());
        etf.createTriggers();

        COSMWebSocketEngine engine = createEngine();
        engine.addAdListener(etf);
        engine.start();

        Thread.sleep(1200000000);

    }

    private COSMWebSocketEngine createEngine() throws IOException, ExecutionException, InterruptedException {
        List<String> list = new LinkedList<String>();
        list.add("/feeds/80263");
        list.add("/feeds/80263");
        list.add("/feeds/42055");

        COSMWebSocketEngine engine = new COSMWebSocketEngine(list);
        engine.start();
        return engine;
    }
}

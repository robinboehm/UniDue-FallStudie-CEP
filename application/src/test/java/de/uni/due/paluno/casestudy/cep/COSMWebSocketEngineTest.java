package de.uni.due.paluno.casestudy.cep;

import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class COSMWebSocketEngineTest {

    @Test
    public void testEngine() throws IOException, ExecutionException, InterruptedException {
        List<String> list = new LinkedList<String>();
        list.add("/feeds/42055/datastreams/Strom_Gesamtverbrauch");

        COSMWebSocketEngine engine = new COSMWebSocketEngine(list);
        engine.start();

        Thread.sleep(15000);
    }
}

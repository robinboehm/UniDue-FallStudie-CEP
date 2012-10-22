package de.uni.due.paluno.casestudy.cep;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.due.paluno.casestudy.cep.model.World;
import de.uni.due.paluno.casestudy.cep.model.mock.MockWorld;
import org.junit.Test;

public class MockParseTest {

    @Test
    public void parseMockWorldToJSON() {
        System.out.println(new MockWorld());
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        try {
            System.out.println(objectMapper.writeValueAsString((World)new MockWorld()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println(e.getClass() + " : " + e.getMessage());
        }

    }
}

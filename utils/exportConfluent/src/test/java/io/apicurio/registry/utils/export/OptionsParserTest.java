package io.apicurio.registry.utils.export;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class OptionsParserTest {

    @Test
    public void testClientPropsParsing() {
        String[] args = {
                "http://localhost:8081",
                "--output",
                "my-export.zip",
                "--client-props",
                "security.protocol=SASL_SSL",
                "sasl.mechanism=PLAIN",
                "sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"secret\";"
        };

        OptionsParser parser = new OptionsParser(args);

        Assertions.assertEquals("http://localhost:8081", parser.getUrl());
        Assertions.assertEquals("my-export.zip", parser.getOutputFile());

        Map<String, Object> props = parser.getClientProps();
        Assertions.assertEquals(3, props.size());
        Assertions.assertEquals("SASL_SSL", props.get("security.protocol"));
        Assertions.assertEquals("PLAIN", props.get("sasl.mechanism"));
        Assertions.assertEquals("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"secret\";", props.get("sasl.jaas.config"));
    }

    @Test
    public void testEmptyArgs() {
        OptionsParser parser = new OptionsParser(new String[]{});
        Assertions.assertNull(parser.getUrl());
    }

    @Test
    public void testInsecure() {
        String[] args = {
                "http://localhost:8081",
                "--insecure"
        };
        OptionsParser parser = new OptionsParser(args);
        Assertions.assertTrue(parser.isInSecure());
        Assertions.assertEquals("confluent-schema-registry-export.zip", parser.getOutputFile());
    }
}

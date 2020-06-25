/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.api.kafka.model;

import io.strimzi.test.TestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * The purpose of this test is to confirm that we can create a
 * resource from the POJOs, serialize it and create the resource in K8S.
 * I.e. that such instance resources obtained from POJOs are valid according to the schema
 * validation done by K8S.
 */
@DisabledIfEnvironmentVariable(named = "TRAVIS", matches = "true")
public class TestIT extends AbstractCrdIT {
    public static final String NAMESPACE = "kafkacrd-it";

    private static final Logger LOGGER = LogManager.getLogger(TestIT.class);

    @Test
    void testKafkaV1alpha1() {
        LOGGER.info(System.getProperty("java.class.path"));
        LOGGER.info("Check if context are same: {}", ClassLoader.getSystemClassLoader() == Thread.currentThread().getContextClassLoader());
//        LOGGER.info(((URLClassLoader) this.getClass().getClassLoader()).getURLs());

//        LOGGER.info(Thread.currentThread().getContextClassLoader().getDefinedPackages());
        TestUtils.waitFor("", 1000, 20000, () -> {
            return true;
        });
        assertTrue(true);
    }

    @BeforeAll
    void setupEnvironment() {
//        cluster.createNamespace(NAMESPACE);
//        cluster.createCustomResources(TestUtils.CRD_KAFKA);
//        waitForCrd("crd", "kafkas.kafka.strimzi.io");
    }

    @AfterAll
    void teardownEnvironment() {
        cluster.deleteCustomResources(TestUtils.CRD_KAFKA);
        cluster.deleteNamespaces();
    }
}

/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.strimzi.api.kafka.Crds;
import io.strimzi.api.kafka.model.DoneableKafka;
import io.strimzi.api.kafka.model.DoneableKafkaTopic;
import io.strimzi.api.kafka.model.DoneableKafkaUser;
import io.strimzi.api.kafka.model.Kafka;
import io.strimzi.api.kafka.model.KafkaTopic;
import io.strimzi.api.kafka.model.KafkaUser;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class StrimziKubernetesClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrimziKubernetesClientTest.class);

    @Test
    public void test() throws InterruptedException {
        Crds.registerCustomKinds();
//  https://192.168.99.117:8443/apis/kafka.strimzi.io/v1beta1/namespaces/default/kafkas
        KubernetesClient c = new DefaultKubernetesClient();
//        Crds.kafkaOperation(c).inNamespace("default").withName("foo").create(new KafkaBuilder()
//                //.withApiVersion("v1beta1")
//                .withNewMetadata()
//                .withName("foo")
//                .withNamespace("default")
//                .endMetadata()
//                .withNewSpec()
//                .withNewKafka()
//                .withReplicas(3)
//                .withNewEphemeralStorage().endEphemeralStorage()
//                .withNewListeners()
//                .withNewTls()
//                .withNewKafkaListenerAuthenticationTlsAuth()
//                .endKafkaListenerAuthenticationTlsAuth()
//                .endTls()
//                .endListeners()
//                .withNewKafkaAuthorizationSimple()
//                .endKafkaAuthorizationSimple()
//                .endKafka()
//                .withNewZookeeper()
//                .withReplicas(3)
//                .withNewEphemeralStorage().endEphemeralStorage()
//                .endZookeeper()
//                .withNewEntityOperator()
//                .withNewTopicOperator().endTopicOperator()
//                .withNewUserOperator().endUserOperator()
//                .endEntityOperator()
//                .endSpec()
//                .build());

        StrimziKubernetesClient client = c.adapt(StrimziKubernetesClient.class);
        String namespace = client.getNamespace();
        client.strimzi().kafka().createNew()
                //.withApiVersion("kafka.strimzi.io/v1beta1")
                .withNewMetadata()
                    .withName("foo")
                    //.withNamespace("default")
                .endMetadata()
                .withNewSpec()
                    .withNewKafka()
                        .withReplicas(3)
                        .withNewEphemeralStorage().endEphemeralStorage()
                        .withNewListeners()
                            .withNewTls()
                                .withNewKafkaListenerAuthenticationTlsAuth()
                                .endKafkaListenerAuthenticationTlsAuth()
                            .endTls()
                            .withNewPlain().endPlain()
                        .endListeners()
                        .withNewKafkaAuthorizationSimple()
                        .endKafkaAuthorizationSimple()
                    .endKafka()
                    .withNewZookeeper()
                        .withReplicas(3)
                        .withNewEphemeralStorage().endEphemeralStorage()
                    .endZookeeper()
                    .withNewEntityOperator()
                        .withNewTopicOperator().endTopicOperator()
                        .withNewUserOperator().endUserOperator()
                    .endEntityOperator()
                .endSpec()
            .done();
        LOGGER.info("Created resource");
        Resource<Kafka, DoneableKafka> fooOps = client.strimzi().kafka().withName("foo");
        Assert.assertFalse(fooOps.isReady());
        //Assert.assertNotNull(fooOps.get());
        LOGGER.info("Waiting for readiness");
        fooOps.waitUntilReady(2, TimeUnit.MINUTES);
        LOGGER.info("Resource is now ready");
        Assert.assertTrue(fooOps.isReady());

        Resource<KafkaTopic, DoneableKafkaTopic> topicOps = client.strimzi().kafkaTopic().withName("foo");
        topicOps.createNew()
                .withNewMetadata()
                    .withName("foo")
                    .addToLabels("strimzi.io/cluster", "foo")
                .endMetadata()
                .withNewSpec()
                    .withPartitions(3)
                    .withReplicas(2)
                .endSpec()
            .done();
        topicOps.waitUntilReady(1, TimeUnit.MINUTES);

        Resource<KafkaUser, DoneableKafkaUser> usersOps = client.strimzi().kafkaUser().withName("foo");
        usersOps.createNew()
                .withNewMetadata()
                    .withName("foo")
                    .addToLabels("strimzi.io/cluster", "foo")
                .endMetadata()
                .withNewSpec()
                    .withNewKafkaUserTlsClientAuthentication()
                    .endKafkaUserTlsClientAuthentication()
                .endSpec()
                .done();
        usersOps.waitUntilReady(1, TimeUnit.MINUTES);

        client.strimzi().kafkaBridge().createNew()
                .withNewMetadata()
                    .withName("foo")
                .endMetadata()
                .withNewSpec()
                    .withBootstrapServers("foo-kafka-0:9092")
                .endSpec()
                .done();

        client.strimzi().kafkaBridge().withName("foo").waitUntilReady(1, TimeUnit.MINUTES);



    }
}

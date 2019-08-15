/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.strimzi.api.kafka.model.DoneableKafka;
import io.strimzi.api.kafka.model.DoneableKafkaTopic;
import io.strimzi.api.kafka.model.DoneableKafkaUser;
import io.strimzi.api.kafka.model.Kafka;
import io.strimzi.api.kafka.model.KafkaTopic;
import io.strimzi.api.kafka.model.KafkaUser;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class StrimziKubernetesClientTest {

    @Test
    public void test() throws InterruptedException {
        KubernetesClient c = new DefaultKubernetesClient();
        StrimziKubernetesClient client = c.adapt(StrimziKubernetesClient.class);
        client.strimzi().kafka().createNew()
                .withNewMetadata()
                    .withName("foo")
                    .withNamespace("default")
                .endMetadata()
                .withNewSpec()
                    .withNewKafka()
                        .withReplicas(3)
                        .withNewListeners()
                            .withNewTls()
                                .withNewKafkaListenerAuthenticationTlsAuth()
                                .endKafkaListenerAuthenticationTlsAuth()
                            .endTls()
                        .endListeners()
                        .withNewKafkaAuthorizationSimple()
                        .endKafkaAuthorizationSimple()
                    .endKafka()
                    .withNewZookeeper()
                        .withReplicas(3)
                    .endZookeeper()
                    .withNewEntityOperator()
                        .withNewTopicOperator().endTopicOperator()
                        .withNewUserOperator().endUserOperator()
                    .endEntityOperator()
                .endSpec()
            .done();
        Resource<Kafka, DoneableKafka> fooOps = client.strimzi().kafka().inNamespace("default").withName("foo");
        Assert.assertFalse(fooOps.isReady());
        Assert.assertNotNull(fooOps.get());
        fooOps.waitUntilReady(2, TimeUnit.MINUTES);
        Assert.assertTrue(fooOps.isReady());

        Resource<KafkaTopic, DoneableKafkaTopic> topicOps = client.strimzi().kafkaTopic().inNamespace("default").withName("foo");
        topicOps.createNew()
                .withNewSpec()
                    .withPartitions(3)
                    .withReplicas(2)
                .endSpec()
            .done();
        topicOps.waitUntilReady(2, TimeUnit.MINUTES);

        Resource<KafkaUser, DoneableKafkaUser> usersOps = client.strimzi().kafkaUser().inNamespace("default").withName("foo");
        usersOps.createNew()
                .withNewSpec()
                    .withNewKafkaUserTlsClientAuthentication()
                    .endKafkaUserTlsClientAuthentication()
                .endSpec()
                .done();
        usersOps.waitUntilReady(2, TimeUnit.MINUTES);



    }
}

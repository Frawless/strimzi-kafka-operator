/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.systemtest.utils;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.strimzi.systemtest.Constants;
import io.strimzi.systemtest.kafkaclients.KafkaClientOperations;
import io.strimzi.systemtest.kafkaclients.internalClients.InternalKafkaClient;
import io.strimzi.systemtest.resources.ResourceManager;
import io.strimzi.systemtest.templates.crd.KafkaTopicTemplates;
import io.strimzi.systemtest.utils.kafkaUtils.KafkaTopicUtils;
import io.strimzi.test.TestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.Duration;
import java.util.Random;

import static io.strimzi.systemtest.resources.ResourceManager.kubeClient;

/**
 * ClientUtils class, which provides static methods for the all type clients
 * @see io.strimzi.systemtest.kafkaclients.externalClients.OauthExternalKafkaClient
 * @see io.strimzi.systemtest.kafkaclients.externalClients.TracingExternalKafkaClient
 * @see io.strimzi.systemtest.kafkaclients.externalClients.BasicExternalKafkaClient
 * @see io.strimzi.systemtest.kafkaclients.internalClients.InternalKafkaClient
 */
public class ClientUtils {

    private static final Logger LOGGER = LogManager.getLogger(ClientUtils.class);
    private static final String CONSUMER_GROUP_NAME = "my-consumer-group-";
    private static Random rng = new Random();

    // ensuring that object can not be created outside of class
    private ClientUtils() {}

    public static void waitUntilClientReceivedMessagesTls(KafkaClientOperations kafkaClient, int exceptedMessages) throws Exception {
        for (int tries = 1; ; tries++) {
            int receivedMessages = kafkaClient.receiveMessagesTls(Constants.GLOBAL_CLIENTS_TIMEOUT);

            if (receivedMessages == exceptedMessages) {
                LOGGER.info("Consumer successfully consumed {} messages for the {} time", exceptedMessages, tries);
                break;
            }
            LOGGER.warn("Client not received excepted messages {}, instead received only {}!", exceptedMessages, receivedMessages);

            if (tries == 3) {
                throw new RuntimeException(String.format("Consumer wasn't able to consume %s messages for 3 times", exceptedMessages));
            }
        }
    }

    public static void waitTillContinuousClientsFinish(String producerName, String consumerName, String namespace, int messageCount) {
        LOGGER.info("Waiting till producer {} and consumer {} finish", producerName, consumerName);
        TestUtils.waitFor("continuous clients finished", Constants.GLOBAL_POLL_INTERVAL, timeoutForClientFinishJob(messageCount),
            () -> kubeClient().getJobStatus(producerName) && kubeClient().getJobStatus(consumerName));
    }

    public static void waitForClientSuccess(String jobName, String namespace, int messageCount) {
        LOGGER.info("Waiting for producer/consumer:{} will be finished", jobName);
        TestUtils.waitFor("job finished", Constants.GLOBAL_POLL_INTERVAL, timeoutForClientFinishJob(messageCount),
            () ->
            {
                LOGGER.info("Job {} in namespace {}, has status {}", jobName, namespace, kubeClient().namespace(namespace).getJobStatus(jobName));
                return kubeClient().namespace(namespace).getJobStatus(jobName);
            });
    }

    private static long timeoutForClientFinishJob(int messagesCount) {
        // need to add at least 2minutes for finishing the job
        return (long) messagesCount * 1000 + Duration.ofMinutes(2).toMillis();
    }

    public static Deployment waitUntilClientsArePresent(Deployment resource) {
        Deployment[] deployment = new Deployment[1];
        deployment[0] = resource;

        TestUtils.waitFor(" for resource: " + resource + " to be present", Constants.GLOBAL_POLL_INTERVAL, Constants.GLOBAL_TIMEOUT, () -> {
            deployment[0] = ResourceManager.kubeClient().getDeployment(ResourceManager.kubeClient().getDeploymentBySubstring("clients"));
            LOGGER.info("Resource is present: {}", deployment[0] != null);
            return deployment[0] != null;
        });

        return deployment[0];
    }

    public static void waitUntilProducerAndConsumerSuccessfullySendAndReceiveMessages(ExtensionContext extensionContext,
                                                                                     InternalKafkaClient internalKafkaClient) {
        // loop client...
        InternalKafkaClient client = internalKafkaClient.toBuilder().build();

        LOGGER.info("Sending messages to - topic {}, cluster {} and message count of {}",
            internalKafkaClient.getTopicName(), internalKafkaClient.getClusterName(), internalKafkaClient.getMessageCount());

        TestUtils.waitFor("Until producer and consumer successfully send and receive messages", Constants.GLOBAL_CLIENTS_POLL, Constants.GLOBAL_CLIENTS_TIMEOUT,
            () -> {
                String topicName = KafkaTopicUtils.generateRandomNameOfTopic();
                ResourceManager.getInstance().createResource(extensionContext, KafkaTopicTemplates.topic(client.getClusterName(), topicName).build());

                InternalKafkaClient loopClient = client.toBuilder()
                    .withConsumerGroupName(ClientUtils.generateRandomConsumerGroup())
                    .withTopicName(topicName)
                    .build();

                int sent = loopClient.sendMessagesTls();
                int received = loopClient.receiveMessagesTls();

                LOGGER.info("Sent {} and received {}", sent, received);

                return sent == received;
            });
    }

    /**
     * Method which generates random consumer group name
     * @return consumer group name with pattern: my-consumer-group-*-*
     */
    public static String generateRandomConsumerGroup() {
        int salt = rng.nextInt(Integer.MAX_VALUE);

        return CONSUMER_GROUP_NAME + salt;
    }
}


/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.strimzi.api.kafka.KafkaList;
import io.strimzi.api.kafka.model.DoneableKafka;
import io.strimzi.api.kafka.model.Kafka;
import okhttp3.OkHttpClient;

public class KafkaOperationsImpl extends StrimziReadyOperationsImpl<Kafka, KafkaList, DoneableKafka, Resource<Kafka, DoneableKafka>>
        implements Resource<Kafka, DoneableKafka> {

    public KafkaOperationsImpl(OkHttpClient client, Config config) {
        this((new OperationContext()).withOkhttpClient(client).withConfig(config));
    }

    public KafkaOperationsImpl(OperationContext context) {
        super(context.withApiGroupName(Kafka.RESOURCE_GROUP)
                .withApiGroupVersion(Kafka.V1BETA1)
                .withPlural(Kafka.RESOURCE_PLURAL));
        this.apiGroupName = Kafka.RESOURCE_GROUP;
        this.apiVersion = Kafka.RESOURCE_GROUP + "/" + Kafka.V1BETA1;
        this.type = Kafka.class;
        this.listType = KafkaList.class;
        this.doneableType = DoneableKafka.class;
    }

    @Override
    public KafkaOperationsImpl newInstance(OperationContext context) {
        return new KafkaOperationsImpl(context);
    }

    @Override
    protected boolean isReady(Kafka resource) {
        return resource != null
                && resource.getStatus() != null
                && resource.getStatus().getConditions() != null
                && resource.getStatus().getConditions().stream().anyMatch(containsReadyCondition());

    }

}

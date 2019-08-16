/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.strimzi.api.kafka.KafkaTopicList;
import io.strimzi.api.kafka.model.DoneableKafkaTopic;
import io.strimzi.api.kafka.model.KafkaTopic;
import okhttp3.OkHttpClient;

public class KafkaTopicOperationsImpl extends StrimziReadyOperationsImpl<KafkaTopic, KafkaTopicList, DoneableKafkaTopic, Resource<KafkaTopic, DoneableKafkaTopic>>
        implements Resource<KafkaTopic, DoneableKafkaTopic> {

    public KafkaTopicOperationsImpl(OkHttpClient client, Config config) {
        this((new OperationContext()).withOkhttpClient(client).withConfig(config));
    }

    public KafkaTopicOperationsImpl(OperationContext context) {
        super(context.withApiGroupName(KafkaTopic.RESOURCE_GROUP)
                .withApiGroupVersion(KafkaTopic.V1BETA1)
                .withPlural(KafkaTopic.RESOURCE_PLURAL));
        this.apiGroupName = KafkaTopic.RESOURCE_GROUP;
        this.apiVersion = KafkaTopic.RESOURCE_GROUP + "/" + KafkaTopic.V1BETA1;
        this.type = KafkaTopic.class;
        this.listType = KafkaTopicList.class;
        this.doneableType = DoneableKafkaTopic.class;
    }

    @Override
    public KafkaTopicOperationsImpl newInstance(OperationContext context) {
        return new KafkaTopicOperationsImpl(context);
    }

    @Override
    protected boolean isReady(KafkaTopic resource) {
        return resource != null
                && resource.getStatus() != null
                && resource.getStatus().getConditions() != null
                && resource.getStatus().getConditions().stream().anyMatch(containsReadyCondition());
    }

}

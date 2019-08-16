/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.strimzi.api.kafka.KafkaConnectS2IList;
import io.strimzi.api.kafka.model.DoneableKafkaConnectS2I;
import io.strimzi.api.kafka.model.KafkaConnectS2I;
import okhttp3.OkHttpClient;

public class KafkaConnectS2IOperationsImpl extends StrimziReadyOperationsImpl<KafkaConnectS2I, KafkaConnectS2IList, DoneableKafkaConnectS2I, Resource<KafkaConnectS2I, DoneableKafkaConnectS2I>>
        implements Resource<KafkaConnectS2I, DoneableKafkaConnectS2I> {

    public KafkaConnectS2IOperationsImpl(OkHttpClient client, Config config) {
        this((new OperationContext()).withOkhttpClient(client).withConfig(config));
    }

    public KafkaConnectS2IOperationsImpl(OperationContext context) {
        super(context.withApiGroupName(KafkaConnectS2I.RESOURCE_GROUP)
                .withApiGroupVersion(KafkaConnectS2I.V1BETA1)
                .withPlural(KafkaConnectS2I.RESOURCE_PLURAL));
        this.apiGroupName = KafkaConnectS2I.RESOURCE_GROUP;
        this.apiVersion = KafkaConnectS2I.RESOURCE_GROUP + "/" + KafkaConnectS2I.V1BETA1;
        this.type = KafkaConnectS2I.class;
        this.listType = KafkaConnectS2IList.class;
        this.doneableType = DoneableKafkaConnectS2I.class;
    }

    @Override
    public KafkaConnectS2IOperationsImpl newInstance(OperationContext context) {
        return new KafkaConnectS2IOperationsImpl(context);
    }

    @Override
    protected boolean isReady(KafkaConnectS2I resource) {
        return resource != null
                && resource.getStatus() != null
                && resource.getStatus().getConditions() != null
                && resource.getStatus().getConditions().stream().anyMatch(containsReadyCondition());
    }

}

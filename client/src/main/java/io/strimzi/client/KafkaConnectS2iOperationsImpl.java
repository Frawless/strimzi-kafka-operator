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

public class KafkaConnectS2iOperationsImpl extends StrimziReadyOperationsImpl<KafkaConnectS2I, KafkaConnectS2IList, DoneableKafkaConnectS2I, Resource<KafkaConnectS2I, DoneableKafkaConnectS2I>>
        implements Resource<KafkaConnectS2I, DoneableKafkaConnectS2I> {

    public KafkaConnectS2iOperationsImpl(OkHttpClient client, Config config) {
        this((new OperationContext()).withOkhttpClient(client).withConfig(config));
    }

    public KafkaConnectS2iOperationsImpl(OperationContext context) {
        super(context.withApiGroupName("kafka.strimzi.io")
                .withApiGroupVersion("v1beta1")
                .withPlural("kafkaconnects2is"));
        this.type = KafkaConnectS2I.class;
        this.listType = KafkaConnectS2IList.class;
        this.doneableType = DoneableKafkaConnectS2I.class;
    }

    @Override
    public KafkaConnectS2iOperationsImpl newInstance(OperationContext context) {
        return new KafkaConnectS2iOperationsImpl(context);
    }

    @Override
    protected boolean isReady(KafkaConnectS2I resource) {
        return resource.getStatus().getConditions().stream().anyMatch(containsReadyCondition());
    }

}

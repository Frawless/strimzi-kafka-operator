/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.strimzi.api.kafka.KafkaConnectList;
import io.strimzi.api.kafka.model.DoneableKafkaConnect;
import io.strimzi.api.kafka.model.KafkaConnect;
import okhttp3.OkHttpClient;

public class KafkaConnectOperationsImpl extends StrimziReadyOperationsImpl<KafkaConnect, KafkaConnectList, DoneableKafkaConnect, Resource<KafkaConnect, DoneableKafkaConnect>>
        implements Resource<KafkaConnect, DoneableKafkaConnect> {

    public KafkaConnectOperationsImpl(OkHttpClient client, Config config) {
        this((new OperationContext()).withOkhttpClient(client).withConfig(config));
    }

    public KafkaConnectOperationsImpl(OperationContext context) {
        super(context.withApiGroupName(KafkaConnect.RESOURCE_GROUP)
                .withApiGroupVersion(KafkaConnect.V1BETA1)
                .withPlural(KafkaConnect.RESOURCE_PLURAL));
        this.apiGroupName = KafkaConnect.RESOURCE_GROUP;
        this.apiVersion = KafkaConnect.RESOURCE_GROUP + "/" + KafkaConnect.V1BETA1;
        this.type = KafkaConnect.class;
        this.listType = KafkaConnectList.class;
        this.doneableType = DoneableKafkaConnect.class;
    }

    @Override
    protected boolean isReady(KafkaConnect resource) {
        return resource != null
                && resource.getStatus() != null
                && resource.getStatus().getConditions() != null
                && resource.getStatus().getConditions().stream().anyMatch(containsReadyCondition());
    }

    @Override
    public KafkaConnectOperationsImpl newInstance(OperationContext context) {
        return new KafkaConnectOperationsImpl(context);
    }

}

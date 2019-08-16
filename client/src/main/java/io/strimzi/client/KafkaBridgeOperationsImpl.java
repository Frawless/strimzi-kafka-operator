/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.strimzi.api.kafka.KafkaBridgeList;
import io.strimzi.api.kafka.model.DoneableKafkaBridge;
import io.strimzi.api.kafka.model.KafkaBridge;
import okhttp3.OkHttpClient;

public class KafkaBridgeOperationsImpl extends StrimziReadyOperationsImpl<KafkaBridge, KafkaBridgeList, DoneableKafkaBridge, Resource<KafkaBridge, DoneableKafkaBridge>>
        implements Resource<KafkaBridge, DoneableKafkaBridge> {

    public KafkaBridgeOperationsImpl(OkHttpClient client, Config config) {
        this((new OperationContext()).withOkhttpClient(client).withConfig(config));
    }

    public KafkaBridgeOperationsImpl(OperationContext context) {
        super(context.withApiGroupName(KafkaBridge.RESOURCE_GROUP)
                .withApiGroupVersion(KafkaBridge.V1ALPHA1)
                .withPlural(KafkaBridge.RESOURCE_PLURAL));
        this.apiGroupName = KafkaBridge.RESOURCE_GROUP;
        this.apiVersion = KafkaBridge.RESOURCE_GROUP + "/" + KafkaBridge.V1ALPHA1;
        this.type = KafkaBridge.class;
        this.listType = KafkaBridgeList.class;
        this.doneableType = DoneableKafkaBridge.class;
    }

    @Override
    public KafkaBridgeOperationsImpl newInstance(OperationContext context) {
        return new KafkaBridgeOperationsImpl(context);
    }

    @Override
    protected boolean isReady(KafkaBridge resource) {
        return resource != null
                && resource.getStatus() != null
                && resource.getStatus().getConditions() != null
                && resource.getStatus().getConditions().stream().anyMatch(containsReadyCondition());
    }

}

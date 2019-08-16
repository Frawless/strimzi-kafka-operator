/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.strimzi.api.kafka.KafkaMirrorMakerList;
import io.strimzi.api.kafka.model.DoneableKafkaMirrorMaker;
import io.strimzi.api.kafka.model.KafkaMirrorMaker;
import okhttp3.OkHttpClient;

public class KafkaMirrorMakerOperationsImpl extends StrimziReadyOperationsImpl<KafkaMirrorMaker, KafkaMirrorMakerList, DoneableKafkaMirrorMaker, Resource<KafkaMirrorMaker, DoneableKafkaMirrorMaker>>
        implements Resource<KafkaMirrorMaker, DoneableKafkaMirrorMaker> {

    public KafkaMirrorMakerOperationsImpl(OkHttpClient client, Config config) {
        this((new OperationContext()).withOkhttpClient(client).withConfig(config));
    }

    public KafkaMirrorMakerOperationsImpl(OperationContext context) {
        super(context.withApiGroupName(KafkaMirrorMaker.RESOURCE_GROUP)
                .withApiGroupVersion(KafkaMirrorMaker.V1BETA1)
                .withPlural(KafkaMirrorMaker.RESOURCE_PLURAL));
        this.apiGroupName = KafkaMirrorMaker.RESOURCE_GROUP;
        this.apiVersion = KafkaMirrorMaker.RESOURCE_GROUP + "/" + KafkaMirrorMaker.V1BETA1;
        this.type = KafkaMirrorMaker.class;
        this.listType = KafkaMirrorMakerList.class;
        this.doneableType = DoneableKafkaMirrorMaker.class;
    }

    @Override
    public KafkaMirrorMakerOperationsImpl newInstance(OperationContext context) {
        return new KafkaMirrorMakerOperationsImpl(context);
    }

    @Override
    protected boolean isReady(KafkaMirrorMaker resource) {
        return resource != null
                && resource.getStatus() != null
                && resource.getStatus().getConditions() != null
                && resource.getStatus().getConditions().stream().anyMatch(containsReadyCondition());
    }

}

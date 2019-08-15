/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.OperationContext;
import io.strimzi.api.kafka.KafkaUserList;
import io.strimzi.api.kafka.model.DoneableKafkaUser;
import io.strimzi.api.kafka.model.KafkaUser;
import okhttp3.OkHttpClient;

public class KafkaUserOperationsImpl extends StrimziReadyOperationsImpl<KafkaUser, KafkaUserList, DoneableKafkaUser, Resource<KafkaUser, DoneableKafkaUser>>
        implements Resource<KafkaUser, DoneableKafkaUser> {

    public KafkaUserOperationsImpl(OkHttpClient client, Config config) {
        this((new OperationContext()).withOkhttpClient(client).withConfig(config));
    }

    public KafkaUserOperationsImpl(OperationContext context) {
        super(context.withApiGroupName("kafka.strimzi.io")
                .withApiGroupVersion("v1beta1")
                .withPlural("kafkausers"));
        this.type = KafkaUser.class;
        this.listType = KafkaUserList.class;
        this.doneableType = DoneableKafkaUser.class;
    }

    @Override
    public KafkaUserOperationsImpl newInstance(OperationContext context) {
        return new KafkaUserOperationsImpl(context);
    }

    @Override
    protected boolean isReady(KafkaUser resource) {
        return resource.getStatus().getConditions().stream().anyMatch(containsReadyCondition());
    }

}

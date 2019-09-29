/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import okhttp3.OkHttpClient;

public class DefaultStrimziKubernetesClient
        extends DefaultKubernetesClient
        implements StrimziKubernetesClient {

    public DefaultStrimziKubernetesClient() throws KubernetesClientException {
    }

    public DefaultStrimziKubernetesClient(String masterUrl) throws KubernetesClientException {
        super(masterUrl);
    }

    public DefaultStrimziKubernetesClient(Config config) throws KubernetesClientException {
        super(config);
    }

    public DefaultStrimziKubernetesClient(OkHttpClient httpClient, Config config) throws KubernetesClientException {
        super(httpClient, config);
    }

    @Override
    public StrimziKubernetesAPIGroupDSL strimzi() {
        return adapt(StrimziAPIGroupClient.class);
    }
}

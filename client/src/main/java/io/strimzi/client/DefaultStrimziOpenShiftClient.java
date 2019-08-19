/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftConfig;
import okhttp3.OkHttpClient;

public class DefaultStrimziOpenShiftClient
        extends DefaultOpenShiftClient
        implements StrimziOpenShiftClient {

    public DefaultStrimziOpenShiftClient() throws KubernetesClientException {
    }

    public DefaultStrimziOpenShiftClient(String masterUrl) throws KubernetesClientException {
        super(masterUrl);
    }

    public DefaultStrimziOpenShiftClient(Config config) throws KubernetesClientException {
        super(config);
    }

    public DefaultStrimziOpenShiftClient(OkHttpClient httpClient, Config config) throws KubernetesClientException {
        super(httpClient, OpenShiftConfig.wrap(config));
    }

    @Override
    public StrimziOpenShiftAPIGroupDSL strimzi() {
        return adapt(StrimziOpenShiftAPIGroupDSL.class);
    }

}

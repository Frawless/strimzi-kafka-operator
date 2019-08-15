/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Client;
import io.fabric8.kubernetes.client.ExtensionAdapter;
import io.fabric8.openshift.client.OpenShiftConfig;
import okhttp3.OkHttpClient;

public class StrimziKubernetesExtensionAdapter implements ExtensionAdapter<StrimziKubernetesClient> {


    @Override
    public Class<StrimziKubernetesClient> getExtensionType() {
        return StrimziKubernetesClient.class;
    }

    @Override
    public Boolean isAdaptable(Client client) {
        // TODO check that the Strimzi CRDs are installed
        return true;
    }


    @Override
    public StrimziKubernetesClient adapt(Client client) {
        if (!isAdaptable(client)) {
            throw new StrimziNotAvailableException("Strimzi CRDs are not available.");
        }
        return new DefaultStrimziKubernetesClient(client.adapt(OkHttpClient.class), OpenShiftConfig.wrap(client.getConfiguration()));
    }
}

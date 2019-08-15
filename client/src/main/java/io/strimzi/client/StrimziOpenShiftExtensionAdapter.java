/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.Client;
import io.fabric8.kubernetes.client.ExtensionAdapter;
import io.fabric8.openshift.client.OpenShiftConfig;
import io.fabric8.openshift.client.OpenshiftAdapterSupport;
import okhttp3.OkHttpClient;

public class StrimziOpenShiftExtensionAdapter implements ExtensionAdapter<StrimziOpenShiftClient> {


    @Override
    public Class<StrimziOpenShiftClient> getExtensionType() {
        return StrimziOpenShiftClient.class;
    }

    @Override
    public Boolean isAdaptable(Client client) {
        // TODO check that the Strimzi S2I CRDs are installed
        return new OpenshiftAdapterSupport().isAdaptable(client);
    }


    @Override
    public StrimziOpenShiftClient adapt(Client client) {
        if (!isAdaptable(client)) {
            throw new StrimziNotAvailableException("Strimzi CRDs are not available.");
        }
        return new DefaultStrimziOpenShiftClient(client.adapt(OkHttpClient.class), OpenShiftConfig.wrap(client.getConfiguration()));
    }
}

/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.APIGroupExtensionAdapter;
import io.fabric8.kubernetes.client.Client;
import io.fabric8.kubernetes.client.KubernetesClient;

public class StrimziKubernetesAPIGroupExtensionAdapter extends APIGroupExtensionAdapter<StrimziAPIGroupClient> {

    @Override
    protected String getAPIGroupName() {
        return "strimzi";
    }

    @Override
    public Class<StrimziAPIGroupClient> getExtensionType() {
        return StrimziAPIGroupClient.class;
    }

    @Override
    protected StrimziAPIGroupClient newInstance(Client client) {
        return new StrimziAPIGroupClient((KubernetesClient) client, client.getConfiguration());
    }

}

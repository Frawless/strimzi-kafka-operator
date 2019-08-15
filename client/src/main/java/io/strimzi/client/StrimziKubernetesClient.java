/*
 * Copyright 2019, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.client;

import io.fabric8.kubernetes.client.KubernetesClientTimeoutException;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.internal.readiness.Readiness;
import io.fabric8.kubernetes.client.internal.readiness.ReadinessWatcher;

import java.util.concurrent.TimeUnit;

public interface StrimziKubernetesClient extends NamespacedKubernetesClient {

    StrimziKubernetesAPIGroupDSL strimzi();
}

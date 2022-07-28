/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * Interfaces for defining {@link io.opentelemetry.context.propagation.ContextPropagators} for
 * allowing context propagation across process boundaries, for example when sending context to a
 * remote server.
 */
@ParametersAreNonnullByDefault
@Export
package io.opentelemetry.context.propagation;

import org.osgi.annotation.bundle.Export;
import javax.annotation.ParametersAreNonnullByDefault;

/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.sdk.trace.testbed.clientserver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Span.Kind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.testing.junit5.OpenTelemetryExtension;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.testbed.TestUtils;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class TestClientServerTest {

  @RegisterExtension
  static final OpenTelemetryExtension otelTesting = OpenTelemetryExtension.create();

  private final Tracer tracer =
      otelTesting.getOpenTelemetry().getTracer(TestClientServerTest.class.getName());
  private final ArrayBlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);
  private Server server;

  @BeforeEach
  void before() {
    server = new Server(queue, tracer);
    server.start();
  }

  @AfterEach
  void after() throws InterruptedException {
    server.interrupt();
    server.join(5_000L);
  }

  @Test
  void test() throws Exception {
    Client client = new Client(queue, tracer);
    client.send();

    await()
        .atMost(15, TimeUnit.SECONDS)
        .until(TestUtils.finishedSpansSize(otelTesting), equalTo(2));

    List<SpanData> finished = otelTesting.getSpans();
    assertEquals(2, finished.size());

    finished = TestUtils.sortByStartTime(finished);
    assertThat(finished.get(0).getTraceId()).isEqualTo(finished.get(1).getTraceId());
    assertThat(finished.get(0).getKind()).isEqualTo(Kind.CLIENT);
    assertThat(finished.get(1).getKind()).isEqualTo(Kind.SERVER);

    assertThat(Span.current()).isSameAs(Span.getInvalid());
  }
}
plugins {
  id("otel.java-conventions")
  id("otel.publish-conventions")
}

description = "OpenTelemetry SDK Auto-configuration SPI"
otelJava.moduleName.set("io.opentelemetry.sdk.autoconfigure.spi")

dependencies {
  api(project(":sdk:all"))

  compileOnly("org.osgi:osgi.annotation")

  // implementation dependency to require users to add the artifact directly to their build to use
  // SdkMeterProviderBuilder.
  implementation(project(":sdk:metrics"))
  // implementation dependency to require users to add the artifact directly to their build to use
  // SdkLogEmitterProviderBuilder.
  implementation(project(":sdk:logs"))
}

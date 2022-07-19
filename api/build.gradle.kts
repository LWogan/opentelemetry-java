plugins {
  id("biz.aQute.bnd.builder")
}

subprojects {
  // Workaround https://github.com/gradle/gradle/issues/847
  group = "io.opentelemetry.api"
  val proj = this
  plugins.withId("java") {
    configure<BasePluginExtension> {
      archivesName.set("opentelemetry-api-${proj.name}")
    }
  }
  apply{
    plugin("biz.aQute.bnd.builder")
  }
}

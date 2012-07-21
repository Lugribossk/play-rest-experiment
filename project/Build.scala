import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "WebTourn"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "com.google.guava" % "guava" % "12.0",
      "org.easytesting" % "fest-assert-core" % "2.0M6",
      // Play with Guava 12 requires this newer version
      "org.reflections" % "reflections" % "0.9.8",
      "org.mockito" % "mockito-core" % "1.9.0",
      "org.jooq" % "joor" % "0.9.3",
      "org.projectlombok" % "lombok" % "0.11.2",
      "org.mindrot" % "jbcrypt" % "0.3m"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}

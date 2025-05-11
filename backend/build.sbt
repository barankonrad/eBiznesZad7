name := "zad7"
version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, SonarPlugin)

scalaVersion := "2.13.16"
val playVersion = "3.0.6"

libraryDependencies ++= Seq(
  guice,

  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "org.scalatestplus" %% "mockito-4-6" % "3.2.15.0" % Test,
  "org.playframework" %% "play-test" % playVersion % Test,
  "org.playframework" %% "play-specs2" % playVersion % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
  "org.assertj" % "assertj-core" % "3.25.1" % Test,
  "junit" % "junit" % "4.13.2" % Test,
  "com.github.sbt" % "junit-interface" % "0.13.3" % Test
)

sonarProperties ++= Map(
  "sonar.projectKey" -> "barankonrad_eBiznesZad7",
  "sonar.organization" -> "barankonrad",
  "sonar.host.url" -> "https://sonarcloud.io",
  "sonar.sources" -> "app",
  "sonar.tests" -> "test",
  "sonar.sourceEncoding" -> "UTF-8",
  "sonar.scala.coverage.reportPaths" -> "target/scala-2.13/scoverage-report/scoverage.xml",
  "sonar.exclusions" -> "**/*.js,**/*.css,**/*.html,**/*.xml,**/*.json",
  "sonar.verbose" -> "true"
)
import sbt._
import Keys._
import org.scalatra.sbt._
import sbtbuildinfo.{BuildInfoKeys, BuildInfoPlugin}
import com.typesafe.sbt.{GitVersioning, SbtGit}
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object TinyLegendWebBuild extends Build {
  val Organization = "com.tinylegend"
  val Name = "Tiny Legend Web"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.8"
  val ScalatraVersion = "2.5.0"
  val ScalaMetricsVersion = "3.1.2"

  lazy val commonDeps = Seq(
    "commons-lang" % "commons-lang" % "2.6",
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "org.apache.commons" % "commons-lang3" % "3.0.1",
    "org.scalactic" %% "scalactic" % "3.0.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )

  lazy val commonSettings = Seq (
    organization := Organization,
    version := Version,
    scalaVersion := ScalaVersion,
    resolvers += Classpaths.typesafeReleases,
    resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
    libraryDependencies ++= commonDeps
  )


  lazy val Common =
    Project(
      id = "common",
      base = file("common"))
      .settings(commonSettings: _*)

  lazy val DataTier =
    Project(
      id = "dataTier",
      base = file("dataTier"))
      .settings(commonSettings: _*)
      .dependsOn(
        Common
      )


  lazy val DomainTier =
    Project(
      id = "domainTier",
      base = file("domainTier"))
      .settings(commonSettings: _*)
      .dependsOn(
        DataTier,
        Common
      )

  lazy val webApp = Project (
    "tiny-legend-web",
    file("."),
    settings = ScalatraPlugin.scalatraSettings ++ scalateSettings ++ Seq(
      name := Name,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided;compile",
        "org.scalatra" % "scalatra-metrics_2.11" % "2.4.1",
        "io.dropwizard.metrics" % "metrics-core" % ScalaMetricsVersion,
        "io.dropwizard.metrics" % "metrics-servlet" % ScalaMetricsVersion,
        "io.dropwizard.metrics" % "metrics-servlets" % ScalaMetricsVersion,
        "io.dropwizard.metrics" % "metrics-healthchecks" % ScalaMetricsVersion,
        "io.dropwizard.metrics" % "metrics-graphite"% ScalaMetricsVersion,
        "io.dropwizard.metrics" % "metrics-annotation" % ScalaMetricsVersion,
        "io.dropwizard.metrics" % "metrics-json" % ScalaMetricsVersion,
        "io.dropwizard.metrics" % "metrics-jvm"  % ScalaMetricsVersion,
        "org.json4s" %% "json4s-jackson" % "3.5.1",
        "org.json4s" %% "json4s-core" % "3.5.1",
        "org.scalatra" %% "scalatra-json" % "2.5.0"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
          Seq(
            TemplateConfig(
              base / "webapp" / "WEB-INF" / "templates",
              Seq.empty,  /* default imports should be added here */
              Seq(
                Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
              ),  /* add extra bindings here */
              Some("templates")
            )
          )
        }
      )
    )
    .enablePlugins(BuildInfoPlugin)
    .enablePlugins(GitVersioning)
    .settings(commonSettings: _*)
    .settings(BuildInfoKeys.buildInfoObject:= "ServerBuildInfo",
      BuildInfoKeys.buildInfoKeys += BuildInfoKeys.buildInfoBuildNumber,
      SbtGit.git.baseVersion := Version,
      BuildInfoKeys.buildInfoKeys += SbtGit.git.formattedShaVersion,
      BuildInfoKeys.buildInfoKeys += SbtGit.git.formattedDateVersion
  )
    .aggregate(DomainTier, DataTier, Common)
}

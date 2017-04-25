# miscellaneou #

## Home page ##

Now visit '/' will landing to Scalatra default home. We want to have it route to '/app'. 
To do it, we can modify the code in LegendServlet.scala

Solution 1: Simple redirect '/' to '/app' so use '/app' as home page.
```aidl

class BhServlet extends BhHomeAppStack with MetricsSupport{

  get("/") {
    redirect("/app/")
  }

}
```

Or you may implement the default home page here and has menu link to other ember apps. (We will do it later.)

## ping ##

Usually the site should have a '/ping' page to indicate if it is still working. The purpose is for the load balance ping.

For this link, we do not recommend it to be in Ember APPs, instead, we should do it in Scalatra Api Service to make sure API is live.
 
- In LegendServlet.scala, add ping.
```aidl
class LegendServlet extends TinyLegendWebStack with MetricsSupport{

  get("/ping") {
    s"You are from: $request.remoteAddress"
  }
}

```

## Versioning ##

Add SBT Build Info plugin. See [git:sbt-buildinfo](https://github.com/sbt/sbt-buildinfo)

In build.scala, enable build info plugin:

```aidl
import sbtbuildinfo.{BuildInfoKeys, BuildInfoPlugin}

lazy val webApp = Project (
 "tiny-legend-web",
    file("."),
    ...
  )
  .enablePlugins(BuildInfoPlugin)
  .settings(BuildInfoKeys.buildInfoObject:= "ServerBuildInfo",
      BuildInfoKeys.buildInfoKeys += BuildInfoKeys.buildInfoBuildNumber )
```

Then we add the buildInfo to return build Info:
```aidl
import buildinfo.ServerBuildInfo

class LegendServlet extends TinyLegendWebStack with MetricsSupport{

  get("/buildInfo") {
    contentType = "application/json"
    ServerBuildInfo
  }
}
```

After rebuild, you will see the file 'buildinfo.properties' was created with 
```aidl
#Mon Apr 24 13:47:46 PDT 2017
buildnumber=2
```

This buildnumber will be maintained by the sbt buildinfo plugin.

NOTE: you don't need to check in this buildinfo.properties. 

## Git Version ##

We may interest on the git change version so we knew what change it is based on for this build. Let's use this:
[Git Version](https://github.com/sbt/sbt-git)

```aidl
// In tiny-legend-web/project/plugins.sbt
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.2")
```

Add GitVersioning

```aidl
// tiny-legend-web/project/build.scala
import com.typesafe.sbt.GitVersioning

object TinyLegendWebBuild extends Build {

val Version = "0.1.0-SNAPSHOT"

lazy val webApp = Project (
    "tiny-legend-web",
    file(".")
    )
    .enablePlugins(GitVersioning)
    .settings(SbtGit.git.baseVersion := Version)

```

For adding the git version into buildInfo, do this:

```aidl
// in the project seeting, add more key like this:
lazy val webApp = Project (
    "tiny-legend-web",
    file(".")
    )
    .enablePlugins(BuildInfoPlugin)
    .enablePlugins(GitVersioning)
    .settings(BuildInfoKeys.buildInfoObject:= "ServerBuildInfo",
      BuildInfoKeys.buildInfoKeys += BuildInfoKeys.buildInfoBuildNumber,
      SbtGit.git.baseVersion := Version,
      BuildInfoKeys.buildInfoKeys += SbtGit.git.formattedShaVersion,
      BuildInfoKeys.buildInfoKeys += SbtGit.git.formattedDateVersion
  )
```
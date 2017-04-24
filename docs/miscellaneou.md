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

## IsWorking ##

Usually the site should have a '/isWorking' page to indicate if it is still working. The purpose is for the load balance ping.

For this link, we do not recommend it to be in Ember APPs, instead, we should do it in Scalatra Api Service.
 
- In LegendServlet.scala, add isWorking.
```aidl
class LegendServlet extends TinyLegendWebStack with MetricsSupport{

  get("/isWorking") {
    "Hi, I am alive!"
  }
}

```

## Versioning ##

Add SBT Build Info plugin. See [git:sbt-buildinfo](https://github.com/sbt/sbt-buildinfo)

In build.scala, enable build info plugin:

```aidl
lazy val webApp = Project (
 "tiny-legend-web",
    file("."),
    ...
  )
  .enablePlugins(BuildInfoPlugin)
  .settings(BuildInfoKeys.buildInfoObject:= "ServerBuildInfo",
      BuildInfoKeys.buildInfoKeys += BuildInfoKeys.buildInfoBuildNumber )
```

Then we update the isWorking to return build Info:
```aidl
class LegendServlet extends TinyLegendWebStack with MetricsSupport{

  get("/isWorking") {
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


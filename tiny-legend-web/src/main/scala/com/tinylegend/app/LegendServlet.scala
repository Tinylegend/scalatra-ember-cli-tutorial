package com.tinylegend.app

import buildinfo.ServerBuildInfo

import org.scalatra.metrics.MetricsSupport

class LegendServlet extends TinyLegendWebStack with MetricsSupport{

  get("/") {
    redirect("/app/")
  }


  get("/ping") {
    s"You are from: ${request.remoteAddress}"
  }

  get("/buildInfo") {

    ServerBuildInfo
  }
}

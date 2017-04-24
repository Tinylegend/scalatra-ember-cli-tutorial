package com.tinylegend.app

import buildinfo.ServerBuildInfo

import org.scalatra.metrics.MetricsSupport

class LegendServlet extends TinyLegendWebStack with MetricsSupport{

  get("/") {
    redirect("/app/")
  }

  get("/isWorking") {
    contentType = "application/json"
    ServerBuildInfo
  }
}

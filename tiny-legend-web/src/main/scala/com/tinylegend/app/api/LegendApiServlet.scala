package com.tinylegend.app.api

import com.tinylegend.app.TinyLegendWebStack
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.metrics.MetricsSupport

trait LegendApiServlet extends TinyLegendWebStack with MetricsSupport with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats // if need bigDecimal: DefaultFormats.withBigDecimal

  before() {
    contentType="application/vnd.api+json"
  }

}

package com.tinylegend.app

import org.scalatra._
import scalate.ScalateSupport

import org.slf4j.LoggerFactory

trait TinyLegendWebStack extends ScalatraServlet with ScalateSupport {

  val logger = LoggerFactory.getLogger(this.getClass)

  notFound {
    // remove content type in case it was set through an action
    contentType = null
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }

}

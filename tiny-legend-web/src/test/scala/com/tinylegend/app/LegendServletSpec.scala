package com.tinylegend.app

import org.scalatra.test.specs2._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class LegendServletSpec extends ScalatraSpec { def is =
  "GET / on LegendServlet"                     ^
    "should return status 302, it was redirected to /app"                  ! root200^
                                                end

  addServlet(classOf[LegendServlet], "/*")

  def root200 = get("/") {
    status must_== 302
    header.get("Location").get.contains("/app") must equalTo(true)
  }
}

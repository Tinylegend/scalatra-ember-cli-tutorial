package com.tinylegend.app.api.products

import org.apache.http.HttpStatus
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.mockito.MockitoSugar
import org.scalatra.test.scalatest.ScalatraSuite


class ProductsServletTests extends FunSuite with MockitoSugar with ScalatraSuite with Matchers{

  val productServlet = new ProductsServlet()

  addServlet(productServlet, "/*")

  test("get product list") {
    get("/") {
      status shouldBe (HttpStatus.SC_OK)
      header.get("Content-Type") shouldBe Some("application/vnd.api+json; charset=UTF-8")
      body shouldBe ("""List(Product(T1,Lingcod 1,Test Detail 1,/static/images/lingcod1.jpeg), Product(T2,Lingcod 2,Test Detail 2,/static/images/lingcod2.jpeg))""")
    }
  }
}

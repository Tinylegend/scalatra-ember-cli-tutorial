package com.tinylegend.app.api.products

import com.tinylegend.app.api.LegendApiServlet
import com.tinylegend.app.api.products.model.Product

class ProductsServlet extends LegendApiServlet {
  get("/") {

    logger.info("Request for products from:" + request.remoteAddress )
    val products = Seq[Product](
      Product("T1", "Lingcod 1", "Test Detail 1", "/static/images/lingcod1.jpeg"),
      Product("T2", "Lingcod 2", "Test Detail 2", "/static/images/lingcod2.jpeg")
    )

    products
  }
}

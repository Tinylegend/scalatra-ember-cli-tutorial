# First API #

## Step 1 : Setup project ##

### Add logging support. ###

- Open TinyLegendWebStack.scala, add 

```aidl
val logger = LoggerFactory.getLogger(this.getClass)
```
 
### Upgrade to scala test. ###
 
- See here: http://www.scalatest.org/user_guide/using_scalatest_with_sbt
- In TinyLegendWebBuild.scala, add scalatest to commonDeps so all models can use scalatest.
- In the future, you are free to use either Spec2 or Scalatest for your test code.

## Step 2 : Prepare API support ### 

- Add JSON dependency to tiny-legend-web project. Note: for domain/data layer, we don't need JSON yet.
```aidl
        "org.json4s" %% "json4s-jackson" % "3.5.1",
        "org.json4s" %% "json4s-core" % "3.5.1",
        "org.scalatra" %% "scalatra-json" % "2.5.0",
        "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test" // for scalatest
```
- Create API base class LegendApiServlet.scala
```aidl
trait LegendApiServlet extends TinyLegendWebStack with MetricsSupport with JacksonJsonSupport {

  // this is required for JacksonJsonSupport
  protected implicit val jsonFormats: Formats = DefaultFormats // if need bigDecimal: DefaultFormats.withBigDecimal

  // Return the JSON content type for all APIs. If need other type, please generate another base class.
  before() {
    contentType="application/vnd.api+json"
  }

}
```

### Step 3 : Add Product API ###

The first API we will create is HTTP GET: '/api/projects' which will return all products as list.

First we need to have a model for JSON API. In each of API, I will create the model under API folder level. It is not a sharing model here. (Knowing that many people thinking about sharing model classes, it is you choice.).

```aidl
// api/products/model/Product.scala
case class Product (id: String, title: String, detail: String, image: String)

// api/products/ProductsServlet.scala
class ProductsServlet extends LegendApiServlet {
  get("/") {

// Let's use fake data for now.
    logger.info("Request for products from:" + request.remoteAddress )
    val products = Seq[Product](
      Product("T1", "Lingcod 1", "Test Detail 1", "/static/images/lingcod1.jpeg"),
      Product("T2", "Lingcod 2", "Test Detail 2", "/static/images/lingcod2.jpeg")
    )

    products
  }
}

```

### Step 4 : Add Product API test ###

Add ProductsServletTests in test / scala under com.tinylegend.app.api.products.

```aidl

class ProductsServletTests extends FunSuite with MockitoSugar with ScalatraSuite with Matchers{

  val productServlet = new ProductsServlet()

  addServlet(productServlet, "/*")

  test("get product list") {
    get("/") {
      status shouldBe (HttpStatus.SC_OK)

      body shouldBe ("""List(Product(T1,Lingcod 1,Test Detail 1,/static/images/lingcod1.jpeg), Product(T2,Lingcod 2,Test Detail 2,/static/images/lingcod2.jpeg))""")
    }
  }
}

```

Let's run 'sbt test' in commandline, the test should be passed for now. ! 

### Step 5 : Hookup the product api into ScalatraBookstrap

```aidl
// ScalatraBootstrap.scala
context.mount(new ProductsServlet(), "/api/products")

```

Now let's run sbt ~jetty:start and visit http://localhost:8080/api/products, you should see the result of product list like:
```
List(Product(T1,Lingcod 1,Test Detail 1,/static/images/lingcod1.jpeg), Product(T2,Lingcod 2,Test Detail 2,/static/images/lingcod2.jpeg))
```

Yep, we are done in first API!


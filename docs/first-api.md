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


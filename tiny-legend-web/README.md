# Tiny Legend Web #

This porject was generated from RHome/scalatra-sbt.g8 template. For more information about the template, please check: [RHome/scalatra-sbt.g8](https://github.com/RHome/scalatra-sbt.g8).

## Build & Run ##

```sh
$ cd Tiny_Legend_Web
$ ./sbt
> jetty:start
> browse
```

If `browse` does not launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

Note: for sandbox run, the jetty debug was always turn on at port 5005. After start jetty:start, you should be able to attach java remote debugger to port 5005.
For the setting, please see build.sbt file. For more setting, you can refer to [xsbt-web-plugin](https://github.com/earldouglas/xsbt-web-plugin/blob/master/docs/2.0.md).

## What do you get ##

### Init-Scalatra ###

- Blank Scalatra project.
- jetty:start with debug.
- Scalatra jade template.

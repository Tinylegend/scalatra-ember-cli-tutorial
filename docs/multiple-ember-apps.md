# Multiple Ember Apps #

Till now, we have one backend service is running and also one ember UI is running in the same service container. 
It works just like charms. So far so good!

Now we have '/app'. We can use it for list products, sale products etc. How about the business management (/admin)?

In usually, '/admin' are used for managing the product, monitoring the sale, and doing report etc. 
In most case, I will use admin site in desktop browser. By default, I already setup a schema for /app. 
This schema may not fit for the /admin. If I want to have the '/app' and the '/admin' in different schema, how can I do it?

I don't want to make the site too complex by mixing '/app' and '/admin' in one ember application. 

In another angle, people who use '/app' usually do not need to visit '/admin', or same at opposite way.
From performance view, we also don't want to mix these two apps as well to generate unnecessary traffic.

## Solution: Create another ember APP and make it work in one service container ##

Oh wait! Should we go micro-service?

Yep, I agree it could be another solution for going to micro-service. 
But for now, by adding another ember app, since two ember apps are still standalone, 
you are free to deploy it into one service container, or deploy it into multiple service container. 
It is not hard to combine or separate them. 
By the way, ember are static content, it is only one times downloading in client side. I would not have too much concern on the server side loading at this point. 
In stead, I am more worry about the backend API performance.

For current goal, just want to make sites work together at sandbox level but leave flexible ability in distribution. In another word, current design goal is make it to be flexible in distribution and make the distribution selection easier later. 


Another goal for the design: I want to have these sites work together just looks like they are one server under one domain. Like http://mydomain.com/. 
When client visit http://mydomain.com/app or http://mydomain.com/admin, they don't need to know if the sites are in one container or two containers.

When deploy the two sites into two different containers, we can use Nginx or Apache to route the URL to different target servers. 
That is whole different conversation. We can support it, but we will discuss it in advance topic.

## Now let's start the new ember APP ##

- Create 'ember-admin' at URL of '/admin'
```aidl
// under scalatra-ember-cli-projects folder.
$ ember new ember-admin
```
- Create 'ember-user' at URL of '/user'
```aidl
// under scalatra-ember-cli-projects folder.
$ ember new ember-user
```

Now we have two new ember APPs. As you knew already, we need to bind it into Scalatra as static url '/admin' and '/user'.
Let's modify the files:
- web.xml
```aidl
// tiny-legend-web/src/main/webapp/WEB-INF/web.xml

  <servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>/admin/*</url-pattern>
  </servlet-mapping>


  <servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>/user/*</url-pattern>
  </servlet-mapping>

```
- Admin configure
```aidl
// ember-adin/config/environment.js
rootURL: '/admin',
```
- User configure
```aidl
// ember-adin/config/environment.js
rootURL: '/user',
```
- Go to ember-admin and ember-user, build it with `ember build`.

- Create link folder
```aidl
$ cd scalatra-ember-cli-projects/tiny-legend-web/src/main/webapp
$ ln -s ../../../../ember-admin/dist admin
$ ln -s ../../../../ember-user/dist user
```

Now when you run the scalatra service, you can open [http://localhost:8080/admin/](http://localhost:8080/admin/) and [http://localhost:8080/user/](http://localhost:8080/user/) to visit them. 
You should see default ember welcome page at this point!

## Check it in ##

Remember to remove '.git' folder in each of new apps, so you can add it to ROOT git repository. 
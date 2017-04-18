# Init Ember APP #

## Install Ember ##

Following these guide to setup your machine.
[Install Ember](https://guides.emberjs.com/v2.12.0/getting-started/quick-start/)

## Create a new project ##

We will create new ember application call 'ember-app' under ROOT folder. (note: Ember does not support 'app' as application name. So use 'ember-app')

```aidl
// Under scalatra-ember-cli-projects folder
$ cd scalatra-ember-cli-projects
$ ember new ember-app
```

After ember-app was created, cd into the folder, then you can run the ember server.

```aidl
$ cd ember-app
$ ember build
$ ember server
```

now open [http://localhost:4200/](http://localhost:4200/) you should see ember default welcome page.

## Link the ember into Scalatra server ##

Wait, what it means? Ember run in port 4200 and Scalatra Backend Server is running in port 8080. 
Ideally we love to use Ember for UI design. Use Scalatra as service design, but I wish they are running in same tomcat instance. How do we do it?

From Ember, you could see all ember content are 'static content' like in web service. So we just need to add [ember build](../ember-app/dist) into server webapp and make it service as static content. That is it.

To do it, simply do following step setup then you will run ember in Scalatra.

- link the ember-app [ember build](../ember-app/dist) to scalatra webapp as 'app' folder
```aidl
$ cd tiny-legend-web/src/main/webapp/
$ ln -s ../../../../ember-app/dist/ app
```
- In scalatra webapp/WEB-INF, open web.xml, add DefaultServlet mapping to configure ember app as static content. 
```aidl
  <servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>/app/*</url-pattern>
  </servlet-mapping>
```

- Since ember default url is '/', we want to change it into '/app'. To do it, we can make configure change at ember-app/config/environment.js
 
 From:
 ```aidl
 rootURL: '/',
``` 
 change it to: 
 ```aidl
 rootURL: '/app',
 ```
 
 Now you can run Scalatra server and use [http://localhost:8080/app/](http://localhost:8080/app/) to visit your ember-app UI.
 
## Run and test Ember ##

To Run Ember UI, you have two way to do it.

- Run in Scalatra: 

When you run Scalatra service, the ember is service as static content in '/app'. So you can visit it and test your UI.

When each time the ember source code was updated, you can rebuild ember without stop scalatra server. When ember build was change, next time when you visit the UI, the content will be reloaded (since static content file was changed).

- Run in ember:

You can still run `ember server` in ember-app folder. it will start ember server at port 4200. 

But it has problem: You cannot connect backend /api! What was happened?

For backend API, we assume it is in /api/* which will be located in same server. When run ember server, it should be in 'http://localhost:4200/api' but ember server does not have '/api/*' at 4200 port. 
The '/api/*' is actually in 'http://localhost:8080/api/*'. To work around this, you can start ember server and proxy backend server to http://localhost:8080.
```aidl
$ ember server -proxy http://localhost:8080
```
Then you can start Scalatra server at port 8080 and test ember UI at port 4200. All /api call from ember will be proxy to port 8080. So the UI behave should be same as you are run ember in Scalatra server.

### Note ###
- When you start Scaltra service, it will always with the last time ember build result.
- When Scalatra is running, you can run ember server parallel in proxy model. So you can visit http://localhost:8080/app/ and http://localhost:4200/app/ with same result.
- When you run ember server in proxy model, you also can visit scalatra server from ember, check what you see at: http://localhost:4200, you will see scalatra server home page. Supprise?
 
## Add ember to git ##

Notice when you want to add new ember project source into git, it did not show any files for you as 'new'. Where is the files?

When ember create new application, it also created `.git` folder and already add the files into git. 
But since we did not use ember project as git root, so this .git repository does not have 'remote' yet.
We want to have the ember source code be maintained at same git repository as the 'ROOT' one, so let's do it.
```aidl
$ cd ember-app
$ rm -rf .git
$ git status
```
After cleaned .git folder, you then see all ember files in git as new files. You can do normal git command to add them into your root git repository.
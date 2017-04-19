# Ember location type #

We noticed one issue, when we visit top URL like http://localhost:8080/app/ it works.
 After click 'about', the URL become http://localhost:8080/app/about. It is normal.
 But, the issue is: When you refresh page from browser, it got page not found (404) at http://localhost:8080/app/about.
 
Test same behave in ember server http://localhost:4200/app/about, it can be refresh without problem.

When it is running in Scalatra server, http://localhost:8080/app/about will try to find default document (index.html), 
but for sure it is not there. 

After reading this [Specifying the URL Type](https://guides.emberjs.com/v2.9.0/configuring-ember/specifying-url-type/), 
I think the locationType=auto is not working at this case. We should use either hash or none.

## Solution ##

in the config/environment.js, update
```
locationType=auto
```

to :
```
locationType=none
```
You can use `hash` as well.

## Additional configure change ##

Since we touch configure change now, there are soem more piece may interest to be change at this time.


- [Ember CLI port](https://guides.emberjs.com/v2.9.0/configuring-ember/configuring-ember-cli/): 
```
// .ember-cli
"port": 4201
``` 

You can assign different port for each of ember apps, so they can run at the same time in sandbox with 'ember server'.


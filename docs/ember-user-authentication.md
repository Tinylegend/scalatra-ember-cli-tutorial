# ember-user-authentication : ember-simple-auth #

Now we can have multiple ember apps under same domain and work together as well. It is all for free access now.
That come to another question for me: If I want to add authentication, should I add it to each of ember apps? 

If the answer is 'yes', then it would be a nightmare for me! 

That trigger me to do research on how I can just use one of ember app to take care of user login, user management etc. 
and the other ember apps can share the authentication token.

Authentication can be done by ember-simple-auth. That is easy. How about the sharing?

After google it, found some of article talking about using ember-simple-auth.
One thing I noticed is the session persist data default is LocalStorageStore. It also has CookieStore.
Since I will run all these Ember APPs under same domain, then the CookieStore may help me for sharing authentication token with same domain name.

## Start the journey ##

We will do user authentication first in this section. We will do token sharing across ember apps in next section.

- First: I need a authentication server. I found this is simple one and can be direct use without any issue:
[Nodejs-JWT-Authentcation-Sample](https://github.com/auth0-blog/nodejs-jwt-authentication-sample). So clone it and start the service. It works great!

Note: you can use any of authentication server or implement your own. Current this authentication sample just for test purpose. 
In future product version we will update it to use other authentication servers.

- Second: Add ember-simple-auth to ember-user app.
```aidl
$ cd ember-user
$ ember install ember-simple-auth
```

### Configure user authentication ###

- Add session to the application controller so we can use session.

```aidl
$ ember g controller application
installing controller
  create app/controllers/application.js
installing controller-test
  create tests/unit/controllers/application-test.js
```
In the app/controllers/application.js, enable session:
```aidl
// app/controllers/application.js

export default Ember.Controller.extend({
  session: Ember.inject.service('session')
});

```
In the ember-user/app/templates/application.hbs, add something to see if we can read session:
Replace the original code with following:
```aidl
{{#if session.isAuthenticated}}
  You are logged in.
  {{outlet}}
{{else}}
  Please login.
{{/if}}
```

After run the app, visit '/user' I got `Please login`. Not too bad. It seems the session was recognized. 

Now we need to login!

- Add login form

First create a new route 'login'
```aidl
$  ember g route login
  installing route
    create app/routes/login.js
    create app/templates/login.hbs
  updating router
    add route login
  installing route-test
    create tests/unit/routes/login-test.js
```

In the app/routes/login.js

Since in login page, we may not only shows login part, we may load some more information into the same page as well. 
Also the login form may be used in somewhere else as well. 
So for login form which allow user to enter user/password, let's put it into a component and call it 'login-form'.
 
```aidl
$ ember g component login-form
installing component
  create app/components/login-form.js
  create app/templates/components/login-form.hbs
installing component-test
  create tests/integration/components/login-form-test.js
```

In the app/components/login-form.js, let's do the login:
```aidl
// app/components/login-form.js
import Ember from 'ember';
export default Ember.Component.extend({
  session: Ember.inject.service(),
  actions: {
    authenticate: function() {
      var credentials = this.getProperties('identification', 'password');
      this.get('session').authenticate('authenticator:user', credentials)
        .catch((message) => {
        this.set('errorMessage', message);
    });
    }
  }
});

```

And in the app/templates/components/login-form.hbs, let's create a form for input data:
```aidl
<!-- app/templates/components/login-form.hbs -->
<div class = "ui container">
<form {{action 'authenticate' on='submit'}} class="ui form">
  <div class="field">
    <label for="identification">User</label>
    {{input value=identification placeholder='Enter Login' class='form-control'}}
  </div>
  <div class="field">
    <label for="password">Password</label>
    {{input value=password placeholder='Enter Password' class='form-control' type='password'}}
  </div>
  <button type="submit" class="ui button">Login</button>
</form>
{{#if errorMessage}}
  <div class="ui error message">
    <div class="header">Login Failed:</div>
    <p>{{errorMessage}}</p>
  </div>
{{/if}}
</div>
```

Note: You can following by [ember-semantic-ui](ember-semantic-ui.md) to install semantic-ui to make login-form looks better. 

- Implement login

Notice in the login-form.js, it will call 'authenticator:user' for login the user.
This is the actually authenticator which call to the backend user service and get user token. We need to implement it.
 
First, add authenticator:
```aidl
$ ember g authenticator user
```

Let's look at the default class that ember was created:
```aidl
import Base from 'ember-simple-auth/authenticators/base';

export default Base.extend({
  restore(data) {
  },

  authenticate(/*args*/) {
  },

  invalidate(data) {
  }
});
```

Notice that it has 3 methods are required to be override.
 Let's use ember-simple-auth and the [Nodejs-JWT-Authentcation-Sample](https://github.com/auth0-blog/nodejs-jwt-authentication-sample) 
 which we mentioned in beginning. (Please keep the nodejs sample running.)
```aidl
import Ember from 'ember';

import BaseAuthenticator from 'ember-simple-auth/authenticators/base';

export default BaseAuthenticator.extend({
  tokenEndpoint: 'http://localhost:3001/sessions/create',
  restore: function(data) {
    return new Ember.RSVP.Promise(function(resolve, reject) {
      if (!Ember.isEmpty(data.token)) {
        resolve(data);
      } else {
        reject();
      }
    });
  },
  authenticate: function(options) {
    return new Ember.RSVP.Promise((resolve, reject) => {
        Ember.$.ajax({
        url: this.tokenEndpoint,
        type: 'POST',
        data: JSON.stringify({
          username: options.identification,
          password: options.password
        }),
        contentType: 'application/json;charset=utf-8',
        dataType: 'json'
      }).then(function(response) {
        Ember.run(function() {
          resolve({
            token: response.id_token
          });
        });
      }, function(xhr, status, error) {
        var response = xhr.responseText;
        Ember.run(function() {
          reject(response);
        });
      });
  });
  },
  invalidate: function() {
    console.log('invalidate...');
    return Ember.RSVP.resolve();
  }
});

```

Now let's build the ember-user then run the Servlet service. Visit http://localhost:8080/user,
we can see the login form. After enter random user/password and click 'login', we got login failure message.

 ![](images/login-failure.png?raw=true)

Cool! That is actually a good sign. Since we don't have a valid user yet, we should see this error.

- Create a user at nodejs authentication sample site. 

Since there is no user in the sample site yet, so we need to create a user for testing purpose. I use postman:
 ```aidl
Method: "POST"
URL: http://localhost:3001/users
Header: Content-Type=application/json
body:
{
	"username": "rocky",
	"password": "password",
	"extra": "Test Data"
}
```

If you see something like this was returned, then the user was created succeed.
```aidl
{
  "id_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InJvY2t5IiwiZXh0cmEiOiJUZXN0IERhdGEiLCJpZCI6MiwiaWF0IjoxNDkyNTc3NDUzLCJleHAiOjE0OTI1OTU0NTN9.VdIb5m4DgBctnwfJLvxJrHd4MteTUSolV5IPYa3pnPw"
}
```

- Let's test it again. After enter username and password, click login, I see: `You are logged in. Thanks.` ! Yee !

- Logout the user.

To let user logout, we will sue ApplicationRouteMixin in application route.

Let's create a new file under ember-user/app/routes/application.js
```aidl
//ember-user/app/routes/application.js
import Ember from 'ember';
import ApplicationRouteMixin from 'ember-simple-auth/mixins/application-route-mixin';
export default Ember.Route.extend(ApplicationRouteMixin, {
  actions: {
    logout: function() {
      this.get('session').invalidate();
    }
  }
});
```

In this route, add an action call 'logout'.
 
In the application.hbs file, when user was logged in, we will display a button for user to logout.
This button will call 'logout' action when user click it.

```aidl
{{#if session.isAuthenticated}}
    <button {{action 'logout'}}>Logout</button>
  <div class="ui container">
    {{outlet}}
  </div>
  {{else}}
    {{login-form}}
  {{/if}}
```

Let's test it. 

When user was not login, it shows a login form. 

When user login, it shows logout button. 

When click logout button, it turn to show login form again.

Sound everything is working fine now! 


### about the login route ###

Some reader may already noticed, in the user application.hbs, 
we direct use `{{login-form}}` for embedded the login-form into main page, 
so looks like the login route seems does not been used.

The answer is : Yes. When user reach to user home page '/user', we use login-form component to let user login without go to '/user/login' page. 

There are a couple reason for it:
1. In most of other sites, the home page do not need user to login. 
But in here, /user is a sub URL which only function is take care user's login business. 
I did not see any reason user need land to /user home page and click another login again.
2. This is a sub path. '/user'. When we shared user login across other ember apps, user will be redirected to '/user' for login. 
 that means, when user landing in this page, the only business is to login. We should just show login-form here.
3. When other ember app redirect to here, we cannot use '/user/login' URL, we can only use '/user' URL. (Since ember route is in client side.)
if we don't have login form in '/user' page, then user need to click another link to jump to login page. This is not the behave that we wanted to see.

But it does not mean the 'login' route is not necessary to be exist. 
In some scenario when ember detected user was not logged in, then ember will redirect to login route. So we still need to keep a 'login' route. 
In later section, we will add some page which may used 'login' route. 



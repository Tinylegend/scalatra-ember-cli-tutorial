# ember-semantic-ui #

It looks like semantic UI is easy to be fit into ember. I found ember-semantic-ui is flexible for UI design. 
It has a good demo site for me to quick start, copy and paste etc. So I select to integration ember-semantic-ui to this project.

## Improve home page ##

- Add contact page.
- Add about page.
- Add main menu in UI.
- Add responsive page.

## Start improvement ##

- Creating all necessary routes (contact, about):
```aidl
$ ember g route contact
$ ember g route about
```

- Update main page: application.hbs
```aidl
<div class="ui container">
  <div class="ui stackable menu">

    {{#link-to 'index' class='header item'}}
      <img src="{{rootURL}}../static/images/bh-logo.png">
    {{/link-to}}

    {{#link-to 'about' class='item'}}
      About
    {{/link-to}}

    {{#link-to 'contact' class='item'}}
      Contact
    {{/link-to}}

    <div class="item right">
      <div class="ui action left icon input">
        <i class="search icon"></i>
        <input type="text" placeholder="Search">
        <button class="ui button">go</button>
      </div>
    </div>

  </div>

  <div class="body">
    {{outlet}}
  </div>

</div>
```

- Now we see the about, contact and two products in the home page. But there is not format was done yet.  The CSS does not work for now.

To enable the UI style, we will install ember-semantic-ui component.

```
$ ember install ember-semantic-ui
```

- Update home page to enable semantic

In the index.html, we should include the semantic into the page.
```aidl
   <link rel="stylesheet" href="{{rootURL}}assets/semantic.min.css">
   ...
   <script src="{{rootURL}}assets/semantic.min.js"></script>
```
- Also I will copy the static file (images) into Scalatra web under src/main/webapp/static.

### Refresh page, now we have a [nice looking page](images/first-formated-ui.png). ###

![](images/first-formated-ui.png?raw=true)

## About semantic UI ##

In the home page we added semantic UI css and js file. You have two ways to use it.

- First, you can just direct use the semantic class name, like I did in application.hbs.
- Second, you can use ember to try the semantic (provided by ember-semantic-ui). I did a little bit in 'about.hbs' and 'contact.hbs'. You are free to play around in these two files, add something interest.

Resources: 

- [Ember-semantic-ui](http://wecatch.me/ember-semantic-ui/demo/)
- [Semantic-UI](https://semantic-ui.com/)
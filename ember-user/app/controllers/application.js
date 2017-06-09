import Ember from 'ember';

export default Ember.Controller.extend({
  session: Ember.inject.service('keycloak-session'),
  currentUrl: null,
  onPathChanged: Ember.observer('currentPath', function() {
    Ember.run.next(this, function() {
      this.set('currentUrl', window.location.href);
    });
  })
});

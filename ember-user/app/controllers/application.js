import Ember from 'ember';
const {  Logger } = Ember;

export default Ember.Controller.extend({
  session: Ember.inject.service('keycloak-session'),
  currentUrl: null,
  onPathChanged: Ember.observer('currentPath', function() {
    Ember.run.next(this, function() {
      this.set('currentUrl', window.location.href);
      Logger.debug('currentUrl is ' + window.location.href);
    });
  })
});

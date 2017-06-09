import Ember from 'ember';

export default Ember.Controller.extend({
  session: Ember.inject.service('keycloak-session'),
  actions: {
    login: function () {
      this.get('session').login('http://www.ecom.net:8080/user/');
    },

    logout: function () {
      this.get('session').logout('http://www.ecom.net:8080/user/');
    }
  }
});

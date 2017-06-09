import Ember from 'ember';
import layout from '../templates/components/keycloak-session-link';

const { inject, Component } = Ember;

export default Component.extend({

  layout,

  session: inject.service('keycloak-session'),
  actions: {
    login(url) {
      this.get('session').login(url);
    },
    logout(url) {
      this.get('session').logout(url);
    },
  },
});

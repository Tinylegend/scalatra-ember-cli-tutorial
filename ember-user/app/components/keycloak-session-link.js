import Ember from 'ember';
import layout from '../templates/components/keycloak-session-link';

const { inject, Component, Logger } = Ember;

export default Component.extend({

  layout,

  session: inject.service('keycloak-session'),
  actions: {
    login(url) {
      Logger.debug('login url is: ' + url);
      this.get('session').login(url);
    },
    logout(url) {
      Logger.debug('logout url is: ' + url);
      this.get('session').logout(url);
    },
  },
});

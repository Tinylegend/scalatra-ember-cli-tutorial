import Ember from 'ember';
import layout from '../templates/components/keycloak-session-status';

const { inject, Component, Logger } = Ember;

export default Component.extend({

  layout,

  session: inject.service('keycloak-session'),

  actions: {
    refresh() {
      this.get('session').updateToken().then(result => {
        Logger.debug(result);
      });
    },
    login(url) {
      this.get('session').login(url);
    },
    logout(url) {
      this.get('session').logout(url);
    },
  },
});

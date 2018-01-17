import Ember from 'ember';
export default Ember.Route.extend( {
  session: Ember.inject.service('keycloak-session'),

  beforeModel: function () {

    this._super(...arguments);

    var session = this.get('session');

    // Keycloak constructor arguments as described in the keycloak documentation.
    var options = {
      'url': 'http://www.ecom.net:9080/auth',
      'realm': 'ecom-net',
      'clientId': 'ecom-net-site'
    };

    // this will result in a newly constructed keycloak object
    session.installKeycloak(options);

    // set any keycloak init parameters where defaults need to be overidden
    session.set('responseMode', 'fragment');
    session.set('onLoad', 'login-required'); // login-required or check-sso

    // finally init the service and return promise to pause router.
    return session.initKeycloak();

  },

  actions: {
    logout: function() {
      this.get('session').logout('http://www.ecom.net:8080/admin/');
    }
  }

});

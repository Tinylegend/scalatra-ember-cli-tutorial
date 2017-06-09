import Ember from 'ember';
export default Ember.Route.extend( {
  actions: {
    logout: function() {
      this.get('session').invalidate();
    },
    login: function() {
      window.location.replace("/user/");
    }
  }

});

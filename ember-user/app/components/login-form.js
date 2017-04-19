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

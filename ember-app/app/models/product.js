import DS from 'ember-data';

export default DS.Model.extend({
  title: DS.attr('string'),
  detail: DS.attr('string'),
  image: DS.attr('string')
});

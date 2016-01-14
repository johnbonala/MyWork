var product_details_steps = function () {

	this.When(/^I select an investment$/, function (callback) {
    callback.pending();
  });

	this.Then(/^I should be taken to the fund performance screen for that investment$/, function (callback) {
    callback.pending();
  });
	
	this.Then(/^I should see my ISA in the Additional items section$/, function (callback) {
    callback.pending();
  });
	
	this.Then(/^I select my ISA$/, function (callback) {
    callback.pending();
  });

	this.Given(/^I am logged in as a user with Regular payments$/, function (callback) {
    callback.pending();
  });

	this.Given(/^I am logged in as a user without Regular payments$/, function (callback) {
    callback.pending();
  });
	
	this.When(/^I select 'Top up'$/, function (callback) {
    callback.pending();
  });

};

module.exports = product_details_steps;
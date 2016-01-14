var terms_and_conditions_steps = function () {
	this.Given(/^I have submitted successfull credentials for a user who has used the app already$/, function (screen, callback) {
    callback.pending();
  });
	this.Then(/^I should be presented with the Product List$/, function (screen, callback) {
    callback.pending();
  });

	this.Given(/^I have submitted successfull credentials for a first time user$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should be asked weather or not I want the app to Save my User ID$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I allow the app to save my User ID$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I disallow the app to save my User ID$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I Disagree with the Terms and Conditions$/, function (screen, callback) {
    callback.pending();
  });
	
	this.Then(/^I agree with the Terms and Conditions$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should not see the terms and conditions$/, function (screen, callback) {
    callback.pending();
  });
};

module.exports = terms_and_conditions_steps;
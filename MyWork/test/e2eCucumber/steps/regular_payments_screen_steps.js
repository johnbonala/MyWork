var regular_payments_screen_steps = function () {
	this.Then(/^I should see my current amount within the Payment input field$/, function (link, callback) {
    callback.pending();
  });

	this.Then(/^I submit a number between 32$/, function (link, callback) {
    callback.pending();
  });

  this.Then(/^my current payment amount$/, function (link, callback) {
    callback.pending();
  });

	this.When(/^I enter 100 in the Payment amount field$/, function (link, callback) {
    callback.pending();
  });

	this.Then(/^I should see 120.00 as the calculated Tax Releif$/, function (link, callback) {
    callback.pending();
  });

	this.When(/^I enter a value between my current payment amount and 140000$/, function (link, callback) {
    callback.pending();
  });
};

module.exports = regular_payments_screen_steps;

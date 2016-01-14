var confirming_payment_steps = function () {
		
	this.Given(/^the user has provided the details to Top Up their fund$/, function (callback) {
    callback.pending();
  });

	this.Given(/^the user has provided the details to adjust their regular payment$/, function (callback) {
    callback.pending();
  });

	this.When(/^the payment is accepted$/, function (callback) {
    callback.pending();
  });

	this.When(/^the payment is rejected$/, function (callback) {
    callback.pending();
  });

	this.Then(/^the user should see the Payment failed screen$/, function (callback) {
    callback.pending();
  });

	this.Then(/^the user should see the Payment Success screen$/, function (callback) {
    callback.pending();
  });
	
};

module.exports = confirming_payment_steps;

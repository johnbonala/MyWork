var regular_payment_investment_details_steps = function () {
	
	this.When(/^I open the Direct Debit Guarantee link$/, function (screen, callback) {
    callback.pending();
  });
	
	this.When(/^I tap the 'Change investment instructions' button$/, function (screen, callback) {
    callback.pending();
  });
	
	this.Then(/^I should not see my Debit Card details$/, function (screen, callback) {
    callback.pending();
  });

	
	this.Then(/^I should a break down of how my funds will be spread$/, function (screen, callback) {
    callback.pending();
  });

};

module.exports = regular_payment_investment_details_steps;
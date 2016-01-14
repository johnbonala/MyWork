var top_up_screen_steps = function () {

	this.Then(/^I should see nothing within the Payment input field$/, function (callback) {
    callback.pending();
  });

	this.When(/^I enter 100 in the Payment amount field$/, function (callback) {
    callback.pending();
  });

	this.When(/^I enter a value between my current payment amount and 140000$/, function (callback) {
    callback.pending();
  });

	this.When(/^I tap the <amount> button$/, function (callback) {
    callback.pending();
  });

	this.Then(/^I should see <amount> in the Payment amount field$/, function (callback) {
    callback.pending();
  });
	// Example:
	// 	| amount 	|
	// 	| 100 		|
	// 	| 250 		|
	// 	| 500 		|
};

module.exports = top_up_screen_steps;
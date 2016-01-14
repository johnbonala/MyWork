var fund_performance_steps = function () {

	this.Given(/^I am on the Fund performance page for a Investment$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should see a graph displaying$/, function (screen, callback) {
    callback.pending();
  });
	// | Fund name 						|
	// | Dates on x axis 			|
	// | % movement on y axis 	|

	this.When(/^I tap on a point on the graph$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should see details of the point I have clicked$/, function (screen, callback) {
    callback.pending();
  });

	this.When(/^I select the <date> underneath the graph$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should see the graph change to represent <date>$/, function (screen, callback) {
    callback.pending();
  });

	// | 3 months 	|
	// | 1 year 		|
	// | 3 years		|
	// | 5 years 	|

	this.Then(/^I should see the Performance summary underneath the graph$/, function (screen, callback) {
    callback.pending();
  });

	this.Then(/^I should see the total fund changes section$/, function (screen, callback) {
    callback.pending();
  });
	//FMC
	//Additional expenses
	// Total annual charge

	};
module.exports = fund_performance_steps;
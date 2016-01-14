var launch_steps = function () {

	this.When(/^I have launched the application$/, function (callback) {
    callback.pending();
  });
	
	this.When(/^the Splash screen is dissmissed$/, function (callback) {
    callback.pending();
  });
	

	this.Then(/^I see the Splach Screen displayed$/, function (callback) {
    callback.pending();
  });

	this.Then(/^I should be presented with the 'app unavailable' message$/, function (callback) {
    callback.pending();
  });

};

module.exports = launch_steps;
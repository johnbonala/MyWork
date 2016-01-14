var server_responses_steps = function () {
    
  this.When(/^the server returns 'emegency error'$/, function (callback) {
    callback.pending();
  });

  this.Then(/^I should  see the 'app unavailable' error message$/, function (callback) {
    callback.pending();
  });

  this.Then(/^I should see the 'out of hours' error message$/, function (callback) {
    callback.pending();
  });

  this.When(/^the server returns nothing$/, function (callback) {
    callback.pending();
  });

};

module.exports = server_responses_steps;
module.exports = function () {
  this.AfterScenario(function (event, callback) {
    browser.executeScript('window.localStorage.clear();');
    callback();
  });

  this.After(function (scenario, callback) {
    if (scenario.isFailed()) {
      browser.takeScreenshot().then(function (png) {
        var decodedImage = new Buffer(png, 'base64').toString('binary');
        scenario.attach(decodedImage, 'image/png');

        callback();
      });
    }
    else {
      callback();
    }
  });

};
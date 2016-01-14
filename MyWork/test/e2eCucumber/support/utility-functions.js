module.exports = {
  
  baseUrl: 'http://localhost:8100/#',

  bootstrap: function () {
    browser.get(this.baseUrl);
    browser.executeScript('window.localStorage.clear();');
    browser.waitForAngular();
  },
  
  goToLogin: function () {
    browser.get(this.baseUrl + '/');
    browser.waitForAngular();
  }

};

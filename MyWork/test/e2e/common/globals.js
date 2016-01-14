function applicationWait(time) {
    if (time != null && time != undefined) {
        browser.driver.sleep(time);
    } else {
        browser.driver.sleep(1000);
    }
}


disableCssAnimations = function() {
  return browser.executeScript("document.body.className += ' notransition';");
};


exports.globals = {
	"baseUrl": "http://localhost:8100",
	"screenshotsLocation" : "screenshots"
}

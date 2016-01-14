'use strict';

var globals = require('../common/globals').globals;
var users = require('../common/users').users;

//Wait for elements to appear for a bit if they dont already
browser.driver.manage().timeouts().implicitlyWait(500);

var TermsOfUsePage = function() {
    browser.get(globals.baseUrl).then(function() {
        element(by.model('loginData.username')).sendKeys(protractor.Key.TAB).then(function() {
            element(by.id('login_termsOfUse')).click();
        });
    });
};

TermsOfUsePage.prototype = Object.create({}, {

    //Title
    title: {
        get: function() {
            return element(by.id('termsofuse_title')).getText();
        }
    },

    //buttons
    closeButton: {
        get: function() {
            return element(by.id('termsofuse_close'));
        }
    },

    //Links
    termsOfUseforOnlineServicing: {
        get: function() {
            return element.all(by.css('.terms-of-use')).all(by.tagName('a')).get(0);
        }
    },
    privacyPolicy: {
        get: function() {
            return element.all(by.css('.terms-of-use')).all(by.tagName('a')).get(1);

        }
    },
    cookiePolicy: {
        get: function() {
            return element.all(by.css('.terms-of-use')).all(by.tagName('a')).get(2)
        }
    },

    //actions
    clickTermsOfUseforOnlineService: {
        value: function() {
            this.termsOfUseforOnlineServicing.click();
        }
    },
    closeTermsofUsePage: {
        value: function() {
            this.closeButton.click().then(function() {
                browser.driver.sleep(1500);
            });
        }
    },
    clickPrivacyPolicy: {
        value: function() {
            this.privacyPolicy.click();
        }
    },
    clickCookiePolicy: {
        value: function() {
            this.cookiePolicy.click();
        }
    }
});
module.exports = TermsOfUsePage;
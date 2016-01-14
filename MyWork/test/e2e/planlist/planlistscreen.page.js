var LoginPage = require('../login/login.page');
var loginPage;
//Wait for elements to appear for a bit if they dont already
browser.driver.manage().timeouts().implicitlyWait(500);

var PlanListScreen = function() {
    loginPage = new LoginPage();
    loginPage.loginAs('tester');
    loginPage.agreeButton.click().then(function() {
        browser.driver.sleep(1500);
    });
}

PlanListScreen.prototype = Object.create({}, {

    //buttons
    moreButton: {
        get: function() {
            element(by.css('.popup-buttons .button')).isDisplayed().then(function() {
                element(by.css('.popup-buttons .button')).click();
            });
            return element(by.css('.settings-button'));
        }
    },

    //biolerplate
    logo: {
        get: function() {
            return element(by.css('.header-item'));
        }
    },

    //Plan Section
    plan: {
        get: function() {
            return element.all(by.repeater('product in productList.data.products')).first();
        }
    },
    planName: {
        get: function() {
            return this.plan.all(by.tagName('h2')).first();
        }
    },
    planNumber: {
        get: function() {
            return this.plan.all(by.css('.sl-micro-font')).first();
        }
    },
    planValue: {
        get: function() {
            return this.plan.all(by.css('.sl-prime-font')).first();
        }
    },

    //ISA section
    stockandSharesISA: {
        get: function() {
            return element(by.id('shares_ISA'));
        }
    },
    numberOfDaysinTaxYear: {
        get: function() {
            return element(by.css('.circleMainText'));
        }
    },
    moreMenu: {
        get: function() {
            return element(by.css('.settings-button'));
        }
    },
    moreMenuScreen: {
        get: function() {
            return element(by.css('.modal-menu'));
        }
    },

    //actions
    openMoreMenu: {
        get: function() {
            return this.moreMenu.click().then(function() {
                browser.driver.sleep(500);
            });
        }
    },
    openISAAdvert: {
        value: function() {
            this.stockandSharesISA.click().then(function() {
                browser.driver.sleep(500);
            });
        }
    },
    openPlanDetails:{
        get:function(){
            return this.plan.click().then(function(){
                console.log('plan detialis');
                browser.driver.sleep(500);
            });
        }
    }

});
module.exports = PlanListScreen;
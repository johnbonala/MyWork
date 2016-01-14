'use strict';

var globals = require('../common/globals').globals;

//Wait for elements to appear for a bit if they dont already
browser.driver.manage().timeouts().implicitlyWait(200);

function getHeaderBar(){
	return element.all(by.css('.nav-bar-block')).get(0);
}

var FundPage = function () {
};

FundPage.prototype = Object.create({}, {

	//inputs

    //buttons
    topupButton: { get: function () { return element(by.id('topup')); }},
    regularPaymentsButton: { get: function () { return element(by.id('regular_payments')); }},
    importantInfoButton: { get: function () { return element(by.id('fund_important_info')); }},

    fundList: { get: function () { return element.all(by.repeater('fund in productDetail.currentValueSummary.investmentPolicy.funds.fund')); }},
    fundListCount: { get: function () { console.log('========Checking count'); return this.fundList.count(); }},
    fundListAt: { get: function (idx) { return this.fundList.get(idx); }},

    //actions
    chooseFund: { value: function (idx) {
        this.fundList.get(idx).click();
    }},
});

module.exports = FundPage;

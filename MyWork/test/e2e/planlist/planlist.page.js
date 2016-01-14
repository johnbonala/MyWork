'use strict';

var globals = require('../common/globals').globals;

//Wait for elements to appear for a bit if they dont already
browser.driver.manage().timeouts().implicitlyWait(200);

var PlanListPage = function () {
};

PlanListPage.prototype = Object.create({}, {
    planList: { get: function () { return element.all(by.repeater('product in productList.data.products')); }},
    planListAt: { get: function (idx) { return this.planList.get(idx); }},

    //actions
    choosePlan: { value: function (idx) {
       this.planList.get(idx).click();
    }},
});

module.exports = PlanListPage;
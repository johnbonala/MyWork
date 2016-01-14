var globals = require('../common/globals').globals;
var UserPage = require('../login/login.page');
var PlanListPage = require('../planlist/planlist.page');
var FundPage = require('../fund/fund.page');
var user, planlistpage, fundpage;

// at the top of the test spec:
var fs = require('fs');
var screenShotDirectory = 'test/screenshots';

fs.existsSync(screenShotDirectory) || fs.mkdirSync(screenShotDirectory);

// ... other code

// abstract writing screen shot to a file
function writeScreenShot(data, filename) {
    var stream = fs.createWriteStream(screenShotDirectory + '/' +  filename);

    stream.write(new Buffer(data, 'base64'));
    stream.end();
}

beforeAll(function(){
    user = new UserPage();
    planlistpage = new PlanListPage();
    fundpage = new FundPage();
    disableCssAnimations();
    browser.executeScript('window.sessionStorage.clear();');
    browser.executeScript('window.localStorage.clear();');
})

//KMOB-424
describe('Happy path through app', function() {

    it('should have the correct URL on the landing page', function(next) {
		expect(browser.getCurrentUrl()).toEqual('http://localhost:8100/#/login');
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '1.login_screen.png');
            next();
        });
	});

    it('should login ok', function(next) {
    	user.enterUserId();
        browser.sleep(1000);
    	user.nextButton.click();
        browser.sleep(1000);
    	user.enterPassword();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '2.pre_login.png');
            user.loginButton.click();
            next();
        });
     });

    it('should be able to accept terms and conditions', function(next){
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '3.terms_and_conditions.png');
            user.agreeButton.click();
            next();
        });
    });

    it('should show the plan list screen', function(next){
        browser.sleep(1000);
        element(by.css('.popup-buttons .button')).click();
        browser.sleep(1000);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '4.plan_list.png');
            expect(browser.getCurrentUrl()).toContain('planlist');
            next();
        });
    });

    it('should show the user menu', function(next){
        element(by.css('.settings-button')).click();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '5.user_menu.png');
            //Close the menu
            next();
        });
    });
    

    it('should show the FAQ page', function(next){
        element(by.id('settings_faqs')).click();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '5a.faq.png');
            //Close the menu
            element(by.css('.modal.active #settings_backbutton')).click();
            next();
        });
    });

    it('should show the Terms page', function(next){
        element(by.id('settings_terms')).click();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '5b.terms.png');
            //Close the menu
            element(by.css('.modal.active #settings_backbutton')).click();
            next();
        });
    });

    it('should show the About page', function(next){
        element(by.id('settings_about')).click();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '5c.about.png');
            //Close the menu
            element(by.css('.modal.active #settings_backbutton')).click();
            next();
        });
    });

    it('Close the settings popup', function(next){
        element(by.css('button#settings_close')).click();
        next();
    });

    it('should allow click through to the plan detail', function(next){
        browser.sleep(500);
        planlistpage.choosePlan(0);
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '6.plan_details.png');
            expect(browser.getCurrentUrl()).toContain('plandetail');
            expect(fundpage.fundListCount).toEqual(3);
            next();
        });
    });

    it('should show the fund imortant info popup', function(next){
        element(by.id('fund_important_info')).click();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '6.fund_important_info.png');
            //Close the menu
            element(by.css('.modal.active #settings_backbutton')).click();
            next();
        });
    });

    //regular_payments
    it('should show the regular payments screen', function(next){
        element(by.id('regular_payments')).click();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '7.regular_payments.png');
            //Close the menu
            next();
        });
    });

     //error no amount chosen
    it('should show the no payment entered warning', function(next){
        element(by.css('.topup-amount-payment button')).click();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '7c.regular_payment_error.png');
            //Close the menu
            next();
        });
    });

    //direct debut and investment details
    it('should show the direct debut and investment details screen', function(next){
        element(by.css('.topup-amount-payment a')).click();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '7a.direct_debit_details.png');
            //Close the menu
            next();
        });
    });

    //direct debut gaurantee
    it('should show the direct debit gaurantee', function(next){
        element(by.css('.investment-details a')).click();
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '7b.direct_debit_gaurantee.png');
            //Close the menu
            element(by.css('.modal.active #settings_backbutton')).click();
    
            browser.sleep(500);
            element(by.css('.bar-header button')).click();
            next();
        });
    });

    //should accept a valid amount in the regular payment screen
    it('should show the direct debut and investment details screen', function(next){
        element(by.css('input[name="netAmount"')).sendKeys("500");
        browser.sleep(500);
        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '7c.amount_entered.png');
            //Close the menu
            element(by.css('button[type="submit"]')).click();
            next();
        });
    });

    //should  show the terms and conditions from the confirm payment screen
    it('should show the terms_and_conditions screen', function(next){

        expect(element(by.css('a[name="terms_and_conditions"')).isPresent()).toBe(true);
        expect(element(by.css('a[name="keyfacts"')).isPresent()).toBe(true);
        expect(element(by.css('a[name="keyfeatures"')).isPresent()).toBe(true);

        element(by.css('a[name="terms_and_conditions"')).click();
        browser.sleep(500);

        browser.takeScreenshot().then(function (png) {
            writeScreenShot(png, '7e.confirm_payment_terms_and_conditions.png');
            //Close the menu
            element(by.css('.modal.active #settings_backbutton')).click();
            next();
        });
    });

    it('should show an image representing the transaction success or fail ', function(next){

        var btn = element(by.css('button[type="submit"]'));

        browser.executeScript(function () {
            arguments[0].scrollIntoView(); }, btn.getWebElement());

            browser.sleep(500);
            element(by.css('button[name="confirm_payment"')).click();
            browser.sleep(500);

            expect(element(by.css('.topup-payment-submitted img')).isPresent()).toBe(true);
            browser.takeScreenshot().then(function (png) {
                writeScreenShot(png, '8.payment_success_fail.png');
                //Close the menu
                element(by.css('button[name="close"]')).click();
                browser.sleep(500);
                expect(browser.getCurrentUrl()).toEqual('http://localhost:8100/#/planlist');
                next();
            });

    });

/////Plan detail
    it('should allow click through to the plan detail', function(next){
        browser.sleep(500);
        planlistpage.choosePlan(0);
        browser.sleep(500);
        fundpage.chooseFund(0);
            browser.takeScreenshot().then(function (png) {
                writeScreenShot(png, '9.fund_performance.png');
                next();
            });
    });

/////Plan detail
    it('should show the About fund performance modal', function(next){
        browser.sleep(500);
            element(by.id('fundDetails_fundPerformance')).click();

            browser.takeScreenshot().then(function (png) {
                writeScreenShot(png, '9a.about_fund_performance.png');
                //Close the menu
                element(by.css('.back-button')).click();
                next();
            });
    });


}); //describe

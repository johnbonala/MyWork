var PropertiesReader;
var properties;
var firstTest;
var testStep = 0;
var fs;
var testCaseName;
var previousTestCaseName;
var angular;
var userId;
var password;
var excecutionfinished;
var usercount;
var screenShotfileName;
var isReadyToRun = false;
//var fileName;
/*describe('E2E test: my-portfolio  login - ', function() {
    PropertiesReader = require('properties-reader');
    properties = PropertiesReader('test/e2e/parameters/testInputs.properties');
    firstTest = true;
    fs = require('fs');
    var users = properties.get('users');
    console.log('all users', users);
    var userArray = users.split(',');
     usercount= 0;
	 makeDirectory(properties.get('scrennshotsFolder'));
		
    //}
    beforeEach(function() {


        if (firstTest) {
		browser.driver.sleep(10000);
            firstTest = false;
            browser.driver.get('http://localhost:8100/');
            browser.driver.sleep(5000);
            browser.driver.findElement(by.id('platform-web-default')).then(function(rippleElement) {
                rippleElement.click();
            });
        } else {
            //browser.driver.sleep(10000);
            browser.driver.get('http://localhost:8100/');
            browser.driver.sleep(5000);
            browser.ignoreSynchronization = true;
            browser.driver.switchTo().frame(0);
        }
        browser.driver.sleep(5000);


    });
    //for(var usercount=0;usercount<userArray.length;usercount++){
    console.log('userArray' + userArray[0]);
    userArray.map(function(testSpec) {

        var userDetails = properties.get(userArray[usercount]).split(',');
        //usercount++;
        console.log('userDetails' + userDetails);
        userId = userDetails[0];
        password = userDetails[1];
        usercount = usercount + 1;
        it('Should validate the Terms of Use page and browse back to Landing page by clicking the Terms of Use link in the landing page.', function() {
            browser.controlFlow().execute(clickOnTermsOfUse).then(function() {
                browser.controlFlow().execute(validateTermsOfUse).then(function() {
                    validateLandingPage();
                });

            });

        });
        it('should allow the user to log in and validate the products and other elements and then log out for user -' + userId, function() {
		if (firstTest) {
browser.driver.sleep(10000);
}		
            excecutionfinished = false;
            testStep = 0;
            testCaseName = 'LoginValidation';  
			
            browser.controlFlow().execute(validateLandingPage).then(function() {
                browser.controlFlow().execute(userIdAndNext).then(function() {
                    browser.controlFlow().execute(setPasswordAndLogin).then(function() {
                        applicationWait(2000)
                        browser.controlFlow().execute(validateProductsPage).then(function() {
                            browser.controlFlow().execute(clickOnSettings).then(function() {
                                logOutFromApplication();
                                //console.log(testSpec);
                            });
                        });
                    });

                });
                // browser.controlFlow().execute(validateProductsPage);
                applicationWait(2000);
            });

        });
        it('should allow user to check the fund Value of the  all products and validate product Details Page and then log out', function() {
        // firstTest = false;
        testStep = 0;
        testCaseName = 'ProductsPageJourney';
        browser.controlFlow().execute(userIdAndNext).then(function() {

            browser.controlFlow().execute(setPasswordAndLogin).then(function() {

                browser.controlFlow().execute(clickProductsAndValidateProductDetails).then(function() {
                    browser.controlFlow().execute(clickOnSettings).then(function() {
                        logOutFromApplication();
                    });
                });

            });
            //console.log('test');
        });
    });
    it('Should click on settings and validate the setting page and then logout', function() {
        testStep = 0;
        testCaseName = 'SettingsPageValidation';
        browser.controlFlow().execute(userIdAndNext).then(function() {
            browser.controlFlow().execute(setPasswordAndLogin).then(function() {

                browser.controlFlow().execute(clickOnSettings).then(function() {
                    applicationWait(3000);
                    browser.controlFlow().execute(validateSettingsMenu).then(function() {
                        logOutFromApplication();
                        applicationWait(2000);
                    });
                });
            });
        });
    });
    it('Should Click on Terms and Privacy in the Settings Menu and verify the page and browse back and logout', function() {
        testStep = 0;
        testCaseName = 'TermsAndPrivacyValidation';
        browser.controlFlow().execute(userIdAndNext).then(function() {
            browser.controlFlow().execute(setPasswordAndLogin).then(function() {

                browser.controlFlow().execute(clickOnSettings).then(function(flow) {
                    // applicationWait(1000);
                    console.log('wahat is flow' + flow);
                    browser.controlFlow().execute(clickOnTermsAndPrivacy).then(function() {
                        browser.controlFlow().execute(validateTermsAndPrivacy).then(function() {
                            browser.controlFlow().execute(backButtonTeamSite).then(function() {
                                //applicationWait(3000);					
                                logOutFromApplication();

                            });
                        });
                    });
                });
            });
        });
    });

    it('Should Click on Frequently Asked Questions in the Settings Menu and verify the page and browse back and logout', function() {
        testStep = 0;
        testCaseName = 'FAQSValidation';
        browser.controlFlow().execute(userIdAndNext).then(function() {
            browser.controlFlow().execute(setPasswordAndLogin).then(function() {

                browser.controlFlow().execute(clickOnSettings).then(function() {
                    //applicationWait(1000);
                    //console.log('wahat is flow'+flow);
                    browser.controlFlow().execute(clickOnFAQs).then(function() {
                        browser.controlFlow().execute(validateFAQsPage).then(function() {
                            browser.controlFlow().execute(backButtonTeamSite).then(function() {
                                //applicationWait(3000);					
                                logOutFromApplication();

                            });
                        });
                    });
                });
            });
        });
    });

    it('Should Click on Contact in the Settings Menu and verify the page and browse back and logout', function() {
        testStep = 0;
        testCaseName = 'ContactPageValidation';
        browser.controlFlow().execute(userIdAndNext).then(function() {
            browser.controlFlow().execute(setPasswordAndLogin).then(function() {

                browser.controlFlow().execute(clickOnSettings).then(function() {
                    browser.controlFlow().execute(clickOnContacts).then(function() {
                        browser.controlFlow().execute(validateContactPage).then(function() {
                            browser.controlFlow().execute(backButtonTeamSite).then(function() {

                                logOutFromApplication();

                            });
                        });
                    });
                });
            });
        });
    });

    it('Should Click on About in the Settings Menu and verify the page and browse back and logout.', function() {
        testStep = 0;
        testCaseName = 'AboutPageValidation';
        browser.controlFlow().execute(userIdAndNext).then(function() {
            browser.controlFlow().execute(setPasswordAndLogin).then(function() {

                browser.controlFlow().execute(clickOnSettings).then(function() {
                    browser.controlFlow().execute(clickOnAbout).then(function() {
                        browser.controlFlow().execute(validateAboutPage).then(function() {
                            browser.controlFlow().execute(backButtonTeamSite).then(function() {
                                logOutFromApplication();
                            });
                        });
                    });
                });
            });
        });
    });
    /*it('Should Switch on save User Id on About in the Settings Menu and verify the page and browse back,logout and validate userId is saved.',function(){
	testStep = 0;
	testCaseName='SaveUserIdValidation';
        browser.controlFlow().execute(userIdAndNext).then(function() {
            browser.controlFlow().execute(setPasswordAndLogin).then(function() {

                browser.controlFlow().execute(clickOnSettings).then(function() {				
					browser.controlFlow().execute(switchSaveUser).then(function(){															
					logOutFromApplication();				
					});
					});
					});
					});
	});
    /*it('should allow user to vlaidate the fund performace Page',function(){
	testCaseName='FundPerformancePageValidation';
	 browser.controlFlow().execute(userIdAndNext).then(function() {
            console.log('succesfully entered username');
            browser.controlFlow().execute(setPasswordAndLogin).then(function() {
			browser.controlFlow().execute(clickOnProducts).then(function(){
			clickInvestmentsandValidateFundPerformance();
			});
			});
	});

});*/

    });
    //};
});*/

function userIdAndNext() {
    var usernameInput = element(by.id(properties.get('userIdFeildId')));
    usernameInput.sendKeys(userId).then(function() {
        displayTestStep('Entered userID as ::::', userId);
        var nextButton = browser.driver.findElement(by.id('next_button'));
        nextButton.click();
        displayTestStep('clicked on Next Button', '');

        expect(usernameInput.getAttribute('value')).toEqual(userId);
        writeScreenShot('UserIdDetails.png');
    });


}

function validateLandingPage() {
    writeScreenShot('LandingPage.png');
    var header = element(by.id('login_header'));
    displayTestStep('header displayed in the Login Page::', header.isDisplayed());
    expect(header.isDisplayed()).toBe(true);
    var usernameInput = browser.driver.findElement(by.id(properties.get('userIdFeildId')));
    usernameInput.getAttribute('placeholder').then(function(text) {
        //console.log('uaser' + text);
        displayTestStep('Text in the userId feild should be Enter User ID:::', text);
        expect(text).toEqual(properties.get('userIdPlaceHolder'));
        // userIdAndNext();
        console.log('Landing page has been validated');
    });


    //});


}

function setPasswordAndLogin() {
    displayTestStep('Enter password ' + '');
    var sparse1 = browser.driver.findElement(by.id('sparse1'));
    var sparse2 = browser.driver.findElement(by.id('sparse2'));
    var sparse3 = browser.driver.findElement(by.id('sparse3'));
    // var password = properties.get('password').toString();
    sparse1.getAttribute('placeholder').then(function(text) {
        text = text.substring(0, text.length - 2);
        //console.log('character1' + password[parseInt(text) - 1]);
        sparse1.sendKeys(password[parseInt(text) - 1]);
    });
    sparse2.getAttribute('placeholder').then(function(text) {
        text = text.substring(0, text.length - 2);
        // console.log('character2' + password[parseInt(text) - 1]);
        sparse2.sendKeys(password[parseInt(text) - 1]);
    });
    sparse3.getAttribute('placeholder').then(function(text) {
        text = text.substring(0, text.length - 2);
        // console.log('character3' + password[parseInt(text) - 1]);
        sparse3.sendKeys(password[parseInt(text) - 1]);
    });
    writeScreenShot('PasswordAndLogin.png');
    var loginButton = browser.driver.findElement(by.id('login_button'));
    expect(loginButton.isDisplayed()).toBe(true);
    loginButton.click();
    displayTestStep('Clicked on Login button' + '');
    applicationWait(2000);
}

function validateProductsPage() {
    writeScreenShot('ProductsPage.png');
    var settingsButton = element(by.id('settings_button'));
    expect(settingsButton.isDisplayed()).toBe(true);
    var productsLists = element.all(by.repeater('product in productList.data.products'));
    productsLists.then(function(result) {
        var noOfProducts = result.length;
        console.log('length of products::::::::::::::' + result);
        for (var i = 0; i < noOfProducts; i++) {
            expect(element.all(by.repeater('product in productList.data.products')).get(i).all(by.tagName('h2')).get(0).isDisplayed()).toBe(true);
            expect(element.all(by.repeater('product in productList.data.products')).get(i).all(by.tagName('div')).get(0).isDisplayed()).toBe(true);
            expect(element.all(by.repeater('product in productList.data.products')).get(i).all(by.tagName('div')).get(1).isDisplayed()).toBe(true);
            expect(element.all(by.repeater('product in productList.data.products')).get(i).all(by.tagName('i')).get(0).isDisplayed()).toBe(true);
        }
    });
    var isaSection = element(by.id('shares_ISA'));
    isaSection.all(by.tagName('h2')).get(0).getText().then(function(heading) {
        console.log(heading);
    });
    isaSection.all(by.tagName('div')).get(0).getText().then(function(innerEles) {
        console.log(innerEles);
    });
    isaSection.all(by.tagName('div')).get(1).getText().then(function(innerEles) {
        console.log(innerEles);
    });
    isaSection.all(by.tagName('div')).get(4).getText().then(function(innerEles) {
        console.log(innerEles);
    });
    isaSection.all(by.tagName('div')).get(5).getText().then(function(innerEles) {
        console.log(innerEles);
    });
    isaSection.all(by.tagName('div')).get(6).getText().then(function(innerEles) {
        console.log(innerEles);
    });
    expect(isaSection.isDisplayed()).toBe(true);
}

function clickProductsAndValidateProductDetails() {
    var productsLists = element.all(by.repeater('product in productList.data.products'));
    applicationWait(2000);
    productsLists.then(function(result) {
        var noOfProducts = result.length;
        var productRepeater = properties.get('productsRepeater');
        for (var i = 0; i < noOfProducts; i++) {
            var baseProduct = element.all(by.repeater(productRepeater)).get(i);
            var productName;
            var fundValue;
            var planNumber;
            element.all(by.repeater(productRepeater)).get(i).all(by.tagName('h2')).get(0).getText().then(function(prodName) {
                productName = prodName;
            });
            element.all(by.repeater(productRepeater)).get(i).all(by.tagName('div')).get(0).getText().then(function(fundVal) {
                fundValue = fundVal;
            });
            element.all(by.repeater(productRepeater)).get(i).all(by.tagName('div')).get(1).getText().then(function(PlanNo) {
                planNumber = PlanNo.substring(6, PlanNo.length);
            });
            
            baseProduct.isDisplayed().then(function(product) {
			writeScreenShot('Product-' + productName + '.png');
                baseProduct.click().then(function() {
                    applicationWait(2000);
                    expect(element(by.id('productdetails_fundvalue')).getText()).toBe(fundValue);
                    expect(element(by.id('productdetails_name')).getText()).toBe(productName);
                    element(by.id('productdetails_planno')).getText().then(function(plan) {
                        expect(plan.substring(13, plan.length)).toBe(planNumber);
                    });
                    var investment_Details = element.all(by.repeater(properties.get('investmentRepeater')));
                    investment_Details.then(function(result) {
                        validateInvestmentDetails(result.length);
                    });

                    //back button
                    //console.log(element(by.className('.button.back-button.buttons.button-clear.header-item')));
                    // additinal Investments 
                    element.all(by.repeater('source in productDetail.additionalInvestmentList.sources')).then(function(adtlInvst) {
                        console.log('Additional Investments:::::::::' + adtlInvst);
                        for (i = 0; i < adtlInvst.length; i++) {
                            expect(element.all(by.repeater('source in productDetail.additionalInvestmentList.sources')).get(i).isDisplayed()).toBe(true);
                        }
                    });
                    //recent Payments
                    expect(element(by.id('productDetails_recentpayments')).isDisplayed()).toBe(true);
                    //Regular Payments
                    expect(element(by.id('regularpayments_button')).getText()).toBe('Regular payments');
                    //Top Up
                    expect(element(by.id('topup_button')).getText()).toBe('Top up');
                    applicationWait(1000);
                    //back button
                    //browser.driver.findElement(by.css('.nav-bar-block button'))[0].click();
                    browser.navigate().back();
                });
            });
            //applicationWait(1000);
        }
    });
}
/**
 *This function will validate whether the investment details have been displayed properly or not.
 *
 **/
function validateInvestmentDetails(noOfInvestments) {

    for (var i = 0; i < noOfInvestments; i++) {
        console.log(i);
        expect(element.all(by.repeater('fund in productDetail.currentValueSummary.investmentPolicy.funds.fund')).get(i).isDisplayed()).toBe(true);
        /*.all(by.tagName('div')).then(
        function(header){
        console.log('testing:::::::::::::::::::::'+header.length);
	
        });*/
    }

}

function clickInvestmentsandValidateFundPerformance() {
    var investment_Details = element.all(by.repeater(properties.get('investmentRepeater')));
    investment_Details.then(function(result) {
        for (var i = 0; i < result.length; i++) {
            var investment = element.all(by.repeater(properties.get('investmentRepeater'))).get(i);
            investment.isDisplayed().then(function(invest) {
                investment.click().then(function() {
                    writeScreenShot('Investment-' + i + '.png');
                    applicationWait(2000);
                    browser.navigate().back();
                });

            });
        }
    });
}

function clickOnProducts() {
    var productRepeater = properties.get('productsRepeater');
    var productsLists = element.all(by.repeater(productRepeater));
    productsLists.then(function(result) {
        for (var i = 0; i < result.length; i++) {
            var baseProduct = element.all(by.repeater(productRepeater)).get(i);
            baseProduct.isDisplayed().then(function(product) {
                baseProduct.click().then(function() {
				applicationWait(1000);
                    writeScreenShot('ProdutsDetailsPage-' + i + '.png');
                    applicationWait(2000);
                    browser.controlFlow().execute(clickInvestmentsandValidateFundPerformance).then(function() {
                        browser.navigate().back();
                    });
                });
            });

        }
    });
}

function clickOnSettings() {
    applicationWait(1000);
    var settingsButton = element(by.id('settings_button'));
    settingsButton.click();
    applicationWait(1000);
    writeScreenShot('SettingsMenu.png');
    displayTestStep('Click on settings button', 'success');
}

function validateSettingsMenu() {
    applicationWait(2000);
    expect(element(by.id('settings_contact')).isDisplayed()).toBe(true);
    expect(element(by.id('settings_about')).isDisplayed()).toBe(true);
    expect(element(by.id('settings_terms')).isDisplayed()).toBe(true);
    expect(element(by.id('settings_blog')).isDisplayed()).toBe(true);
    expect(element(by.id('settings_saveusername')).isDisplayed()).toBe(true);
    expect(element(by.id('settings_logout')).isDisplayed()).toBe(true);
    // applicationWait(2000);    
}

function logOutFromApplication() {
    applicationWait(2000);
    element(by.id('settings_logout')).click();
    displayTestStep('Logout From Application', 'success');
    excecutionfinished = true;
}

function applicationWait(time) {
    if (time != null && time != undefined) {
        browser.driver.sleep(time);
    } else {
        browser.driver.sleep(1000);
    }
}

function displayTestStep(description, value) {
    testStep = testStep + 1;
    if (null != value && value != undefined) {
        console.log(testStep + '-' + description + '-' + value);
    } else {
        console.log(testStep + description);
    }
}

function browseback() {
    //backButton();
    applicationWait(2000);
    browser.navigate().back();
    displayTestStep('Browse Back', 'success');
}

function backButtonTeamSite() {
    console.log('back buttons');
    applicationWait(2000);
    element(by.id('settings_backbutton')).click();
    displayTestStep('Browse Back', 'success');

}

function clickOnTermsAndPrivacy() {
    applicationWait(2000);
    var terms = element(by.id('settings_terms'));
    terms.click();
    displayTestStep('Click on Terms of use button in settings menu', 'success');
}

function validateTermsAndPrivacy() {
    //applicationWait(2000);	
	//screenshotforSeetingsItems('TermsAndPrivacyPage.png');
    expect(element(by.id(properties.get('settingmenu_header'))).getText()).toEqual(properties.get('termsTitle'));
    element.all(by.className('terms-of-use')).isDisplayed().then(function(terms) {
	console.log('terms as ::::::::'+terms);

    });
	writeScreenShot('TermsAndPrivacyPage.png');
}

function clickOnFAQs() {
    applicationWait(2000);
    var FAQs = element(by.id('settings_faqs'));
    FAQs.click();
    displayTestStep('Click on Frequently asked questions button in settings menu', 'success');
}

function validateFAQsPage() {
    expect(element(by.id(properties.get('settingmenu_header'))).getText()).toEqual(properties.get('faqtitle'));
    writeScreenShot('FAQsPage.png');
    displayTestStep('Validate Frequently asked questions', 'success');
}

function clickOnContacts() {
    applicationWait(2000);
    var contact = element(by.id(properties.get('contactid')));
    contact.click();
    displayTestStep('Click on contact button in settings menu', 'success');
}

function validateContactPage() {
    expect(element(by.id(properties.get('settingmenu_header'))).getText()).toEqual(properties.get('contacttitle'));
    writeScreenShot('ContactsPage.png');
    displayTestStep('Validate Contacts Page', 'success');
}

function clickOnAbout() {
    applicationWait(2000);
    var contact = element(by.id(properties.get('aboutid')));
    contact.click();
    displayTestStep('Click on about button in settings menu', 'success');
}

function validateAboutPage() {
    expect(element(by.id(properties.get('settingmenu_header'))).getText()).toEqual(properties.get('abouttitle'));
    writeScreenShot('AboutPage.png');
    displayTestStep('Validate Contacts Page', 'success');
}

function switchSaveUser() {
    var userswitch = element(by.id(properties.get('saveusertoggle')));
    var switchstatus;
    userswitch.getAttribute('checked').then(function(checked) {
        console.log('checked value is' + checked);
        switchstatus = checked;
    });
    userswitch.click();
    if (switchstatus == null || switchstatus == undefined) {

        applicationWait(2000);
        displayTestStep('Switched On save user id ', 'success');
    } else {
        displayTestStep('Switched Off save user id ', 'success');
    }
}

function writeScreenShot(fileName) {
makeDirectory(properties.get('scrennshotsFolder') + testCaseName );
    var directoryPath = properties.get('scrennshotsFolder') + testCaseName + '/' + userId + '/';
    if (previousTestCaseName == null || previousTestCaseName != testCaseName) {       
       makeDirectory(directoryPath);
        previousTestCaseName = testCaseName;
    }
    console.log('previousTestCaseName' + previousTestCaseName);
    fileName = directoryPath + '/' + fileName;
    browser.driver.takeScreenshot().then(function(png) {
        // writeScreenShot(png, 'exception.png');

        var stream = fs.createWriteStream(fileName);
        stream.write(new Buffer(png, 'base64'));
        stream.end();
    });
}

function clickOnTermsOfUse() {
    var header = element(by.id('login_header'));
    header.click().then(function() {
        element(by.id(properties.get('termsOfUseid'))).click();
        displayTestStep('clicked on Terms of Use Link from Landing Page', 'success');
    });

}

function validateTermsOfUse() {
    applicationWait(2000);
    var closeButton = element(by.id(properties.get('closeterms')));
    closeButton.isDisplayed().then(function(displayed) {
        expect(closeButton.isDisplayed()).tobe(true);
        expect(closeButton.getText()).toEqual('Close');
    });
    element(by.id(properties.get('termsofuseTitleid')).getText()).toEqual(properties.get('termsofuseTitle'));
    closeButton.click();
    displayTestStep('Validated Terms of Use Page and clicked on close', 'success');
}
function makeDirectory(directoryPath){
 fs.mkdir(directoryPath, function(error) {
           // console.log('error in creating directory' + error);
			});
}

function fetchUserSession(){
isReadyToRun = false;
  if (firstTest) {
		browser.driver.sleep(10000);
            firstTest = false;
            browser.driver.get('http://localhost:8100/');
            browser.driver.sleep(5000);
            browser.driver.findElement(by.id('platform-web-default')).then(function(rippleElement) {
                rippleElement.click();
				isReadyToRun =true;
            });
        } else {
            //browser.driver.sleep(10000);
            browser.driver.get('http://localhost:8100/');
            browser.driver.sleep(5000);
            browser.ignoreSynchronization = true;
            browser.driver.switchTo().frame(0);
			isReadyToRun = true;
        }
        browser.driver.sleep(5000);
		return isReadyToRun;
}




//}
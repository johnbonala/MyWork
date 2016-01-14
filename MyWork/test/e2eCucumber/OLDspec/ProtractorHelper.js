exports.setUp = function(test) {
    return ('Set up is here ' + test);
}
var invoked = false;

exports.waitUntilReadyToRun = function(invokedFirstTest) {
    if (!invokedFirstTest && !invoked) {
        browser.driver.sleep(9000).then(function() {
            browser.driver.get("http://localhost:8100/#/login/").then(function() {
                browser.driver.sleep(3000).then(function() {
                    browser.driver.findElement(by.id('platform-web-default')).then(function(rippleElement) {
                        rippleElement.click().then(function() {
                            invokedFirstTest = true;
                            invoked = true;
                            return invokedFirstTest;

                        });
                    });
                });
            });
        });


    } else {
        browser.driver.navigate().refresh().then(function() {
            browser.driver.sleep(1500).then(function() {
                browser.driver.get("http://localhost:8100/#/login/").then(function() {
                    browser.driver.sleep(1500).then(function() {
                        browser.ignoreSynchronization = true;
                        browser.driver.switchTo().frame(0).then(function() {
                            invokedFirstTest = true;
                            invoked = true;
                            return invokedFirstTest;
                        });
                    });
                });
            });
        });
    }


}

exports.userIdTextDisplayed = function() {

    return userIdFeild().getAttribute('placeholder');
}

function userIdFeild() {
    return element(by.id('loginData_username'));
}
exports.userIdFeildDisplayed = function() {
    return browser.driver.sleep(1000).then(function() {
        return userIdFeild().isDisplayed();
    });
}

exports.loginAs = function(user) {
    login(user);
}


function login(user) {
    //invoked = false;
    var usernameInput = element(by.id('loginData_username'));
    usernameInput.sendKeys(user.id).then(function() {
        var nextButton = element(by.id('next_button'));
        nextButton.click().then(function() {
            setPasswordAndLogin(user.password);
        });
    });

}
exports.planName = function() {
    return browser.driver.sleep(1000).then(function() {
        var product = element.all(by.repeater('product in productList.data.products')).get(0);
        //product.all(byTagName())
        return product.all(by.tagName('h2')).get(0).getText();
    });
}

exports.planNumber = function() {
    return browser.driver.sleep(1000).then(function() {
        return element.all(by.repeater('product in productList.data.products')).get(0).all(by.tagName('div')).get(1).getText().then(function(PlanNo) {
            return PlanNo.substring(6, PlanNo.length);
        });
    });
}
exports.fundValue = function() {
    return browser.driver.sleep(1000).then(function() {
        return element.all(by.repeater('product in productList.data.products')).get(0).all(by.tagName('div')).get(0).getText().then(function(fundVal) {
            return fundVal.substring(1, fundVal.length);
        });
    });
}



exports.applyReadyToRun = function(isReadyToRun) {

    return browser.driver.sleep(2000).then(function() {
        return true;
    });


}

exports.additionalInvestmentsDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return additionalInvestments().count().then(function(addInvestments) {
            if (addInvestments > 0) {
                return true;
            } else {
                return false;
            }
        });
    });

}
exports.clickOnRecentPaymentsLink = function() {
    return browser.driver.sleep(1500).then(function() {
        recentPaymentsLink().click();
    });
}
exports.activeRecentPayments = function() {
    return browser.driver.sleep(1500).then(function() {
        return element.all(by.css('.button.active-button')).get(0).getText();
    });

}
exports.clickOnCurrentTaxYearTab = function() {
    browser.driver.sleep(1500).then(function() {
        currentTaxYearTab().click();
    });
}

function currentTaxYearTab() {
    return element(by.id('CurrenttaxyearTab'));
}
exports.numberOfAdditionalInvestments = function() {
    return browser.driver.sleep(1500).then(function() {
        return additionalInvestments().count();
    });

}

function additionalInvestments() {
    return element.all(by.repeater('source in productDetail.additionalInvestmentList.sources'));
}
exports.selectPlanToViewInvestments = function() {
    browser.driver.sleep(1500).then(function() {
        element.all(by.repeater('product in productList.data.products')).get(0).isDisplayed().then(function() {
            element.all(by.repeater('product in productList.data.products')).get(0).click();
        });
    });
}

exports.headerTitle = function() {
    return browser.driver.sleep(1500).then(function() {
        return element.all(by.css('.title.title-center.header-item')).get(0).getText();
    });
}
exports.backButtonDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return backButtonDisplay();
    });
}
exports.actualFMC = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('FMCValue')).getText();
    });
}
exports.actualAdditionalExpenses = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('additionalExpenseValue')).getText();
    });
}
exports.actualTotalFundCharge = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('totalFundChargeValue')).getText();
    });
}

function backButtonDisplay() {
    var backButon = element.all(by.css('.nav-bar-block')).get(0).all(by.tagName('button')).get(0);
    return backButon.getAttribute('style').then(function(dis) {
        if (dis == null || dis == '') {
            return true;
        } else {
            return false;
        }
    });
}
function getHeaderBar(){
return element.all(by.css('.nav-bar-block')).get(0);
}
exports.numberOfInvestments = function() {
    return browser.driver.sleep(1500).then(function() {
        return investments().count();
    });
}
exports.investmentsDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return investments().count().then(function(investmentCount) {
            if (investmentCount > 0) {
                return true;
            } else {
                return false;
            }
        });
    });

}
exports.selectAnInvestment = function() {
    browser.driver.sleep(1500).then(function() {
        investments().get(0).getText().then(function(text) {});
        investments().get(0).click();
    });
}

function investments() {
    return element.all(by.repeater('fund in productDetail.currentValueSummary.investmentPolicy.funds.fund'));
}
exports.topUpButtonDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return topUpButton().isDisplayed();
    });
}
exports.recentPaymentsLinkDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return recentPaymentsLink().isDisplayed();
    });
}
exports.selectedGraphTable = function() {
    return browser.driver.sleep(1500).then(function() {
        return element.all(by.css('.button.active1')).get(0).all(by.tagName('p')).get(0).getText();
    });
}
exports.aboutFundPerformanceLinkDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return aboutFundPerformance().isDisplayed();
    });
}
exports.fundFactSheetLinkDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return fundFactSheet().isDisplayed();
    });
}
exports.select3MonthsTab = function() {
    browser.driver.sleep(1500).then(function() {
        getTab('3months').click();
    });
}
exports.select1YearTab = function() {
    browser.driver.sleep(1500).then(function() {
        getTab('1year').click();
    });
}
exports.select3YearsTab = function() {
    browser.driver.sleep(1500).then(function() {
        getTab('3year').click();
    });
}

exports.select5YearsTab = function() {
    browser.driver.sleep(1500).then(function() {
        getTab('5year').click();
    });
}
exports.clickOnAboutFundPerformance = function() {
    browser.driver.sleep(1500).then(function() {
        aboutFundPerformance().click();
    });
}
exports.fundFactSheetLinkDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return fundFactSheetLink().isDisplayed();
    });
}

exports.clickOnFundFactSheetLink = function() {
    browser.driver.sleep(1500).then(function() {
        fundFactSheetLink().click();
    });
}

function fundFactSheetLink() {
    return element(by.id('fundDetails_fundFactsheet'));
}
exports.aboutFundPerformancePageTitle = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('aboutFundPerformance_title')).getText();
    });
}

function fundFactSheet() {
    return element(by.id('fundDetails_fundFactsheet'));
}

function getTab(tabvalue) {
    selectedTab = tabvalue + "_fundperformance";
    return element(by.id(selectedTab));
}

function aboutFundPerformance() {
    return element(by.id('fundDetails_fundPerformance'));
}

function recentPaymentsLink() {
    return element(by.id('productDetails_recentpayments'));
}
exports.regularPaymentButtonDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return regularPaymentsButton().isDisplayed();
    });
}

function regularPaymentsButton() {
    return element(by.id('regularpayments_button'));
}

function topUpButton() {
    return element(by.id('topup_button'));
}
exports.enterUserIdAs = function(userId) {
    var usernameInput = element(by.id('loginData_username'));
    // usernameInput.clear();
    usernameInput.sendKeys(userId);
}

exports.clickOnNextButton = function() {
    return browser.driver.sleep(1000).then(function() {
        var nextButton = element(by.id('next_button'));
        nextButton.click();
    });
}

exports.sparePassword1Displayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('sparse1')).isDisplayed();
    });
}
exports.sparePassword2Displayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('sparse2')).isDisplayed();
    });
}
exports.sparePassword3Displayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('sparse3')).isDisplayed();
    });
}

exports.forgottenLoginDetails = function() {
    return element(by.id('forgottenLogin'));
}

exports.termsOfUse = function() {
    element(by.id('login_header')).click();
    applicationWait(500);
    return element(by.id('login_termsOfUse'));

}
exports.registerHere = function() {
    return element(by.id('login_header')).click();
    return browser.driver.sleep(500).then(function() {
        return element(by.id('login_registerHere'));
    });
}

exports.clickOnTermsofUse = function() {
    element(by.id('login_header')).click().then(function() {
        protractor.userSession.termsOfUse().click();
    });
}
exports.termsOfUseTitle = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('termsofuse_title')).getText();
    });

}

exports.standardLifeSavingImage = function() {
    return element(by.id('login_header'));

}
exports.enterPasswordAs = function(password) {
    setPasswordAndLogin(password);
}
exports.logout = function() {
    browser.driver.sleep(1500).then(function() {
        browser.controlFlow().execute(clickOnSettings).then(function() {
            logOutFromApplication();
        });
    });

}

exports.closeTermsOfUse = function() {
    browser.driver.sleep(1500).then(function() {
        element(by.id('termsofuse_close')).click();
    });
}



exports.clickSettingsMenu = function() {
    browser.driver.sleep(2500).then(function() {
        browser.controlFlow().execute(clickOnSettings);
    });
}
exports.closeSettingsMenu = function() {
    browser.driver.sleep(1500).then(function() {
        element(by.id('settings_close')).click();
    });
}
exports.closeButtoninSettingsMenuDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('settings_close')).isDisplayed();
    });
}
exports.closeButtoninTermsofUseDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return element(by.id('termsofuse_close')).isDisplayed();
    });

}
exports.frequentlyAskedQuestionsLinkDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return frequentlyAskedQuestionsLink().isDisplayed();
    });
}
exports.clickFrequentlyAskedQuestions = function() {
    browser.driver.sleep(1500).then(function() {
        frequentlyAskedQuestionsLink().click();
    });
}
exports.PageTitle = function() {
    return browser.driver.sleep(1500).then(function() {
        return teamSiteTitle();
    });
}
exports.contactLinkDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return contactsLink().isDisplayed();
    });
}
exports.clickContactLink = function() {
    browser.driver.sleep(1500).then(function() {
        contactsLink().click();
    });
}
exports.aboutLinkDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return aboutLink().isDisplayed();
    });
}
exports.clickAboutLink = function() {
    browser.driver.sleep(1500).then(function() {
        aboutLink().click();
    });
}
exports.moneyplusBlogDisplayed = function() {
    return browser.driver.sleep(1500).then(function() {
        return moneyPlusBlog().isDisplayed();
    });
}
exports.clickMoneyPlusBlog = function() {
    return browser.driver.sleep(1500).then(function() {
        return moneyPlusBlog().click();
    });
}

exports.turnOnSaveUserId = function() {
    browser.driver.sleep(1500).then(function() {
        saveUserId();
    });
}

function saveUserId() {
    element.all(by.tagName('input')).count().then(function(coun) {

        for (var i = 0; i < coun; i++) {
            element.all(by.tagName('input')).get(i).isSelected().then(function(text) {

                element.all(by.tagName('input')).get(i).isSelected = true;
            });

        }
    });
}

function moneyPlusBlog() {
    return element(by.id('settings_blog'));
}

function aboutLink() {
    return element(by.id('settings_about'));
}

function teamSiteTitle() {
    return element(by.id('teamsiteTitle_title')).getText();
}

function logOutFromApplication() {
    browser.driver.sleep(1000).then(function() {
        element(by.id('settings_logout')).click().then(function() {
            browser.driver.sleep(1000).then(function() {
                expect(userIdFeild().isDisplayed()).toBe(true);
            });
        });
    });
}

function frequentlyAskedQuestionsLink() {
    return element(by.id('settings_faqs'));
}

function setPasswordAndLogin(password) {
    var sparse1 = element(by.id('sparse1'));
    var sparse2 = element(by.id('sparse2'));
    var sparse3 = element(by.id('sparse3'));
    var loginButton = element(by.id('login_button'));

    sparse1.getAttribute('placeholder').then(function(text) {
        text = text.substring(0, text.length - 2);
        sparse1.sendKeys(password[parseInt(text) - 1]).then(function() {
            sparse2.getAttribute('placeholder').then(function(text1) {
                text1 = text1.substring(0, text1.length - 2);
                sparse2.sendKeys(password[parseInt(text1) - 1]).then(function() {
                    sparse3.getAttribute('placeholder').then(function(text2) {
                        text2 = text2.substring(0, text2.length - 2);
                        sparse3.sendKeys(password[parseInt(text2) - 1]).then(function() {
                            browser.driver.sleep(500).then(function() {
                                loginButton.click().then(function() {
                                    browser.driver.sleep(1000).then(function() {
                                        element(by.id('terms_accept')).isDisplayed().then(function(display) {
                                            if (display) {
                                                element(by.id('terms_accept')).click().then(function() {
                                                    browser.driver.sleep(1000);
                                                });
                                            }
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    });
}

function writeScreenShot(fileName) {
    makeDirectory(properties.get('scrennshotsFolder') + testCaseName);
    var directoryPath = properties.get('scrennshotsFolder') + testCaseName + '/' + userId + '/';
    if (previousTestCaseName == null || previousTestCaseName != testCaseName) {
        makeDirectory(directoryPath);
        previousTestCaseName = testCaseName;
    }
    fileName = directoryPath + '/' + fileName;
    browser.driver.takeScreenshot().then(function(png) {
        var stream = fs.createWriteStream(fileName);
        stream.write(new Buffer(png, 'base64'));
        stream.end();
    });
}

function applicationWait(time) {
    if (time != null && time != undefined) {
        browser.driver.sleep(time);
    } else {
        browser.driver.sleep(1000);
    }
}

function clickOnSettings() {
    var settingsButton = element(by.id('settings_button'));
    settingsButton.click();
}

function contactsLink() {
    return element(by.id('settings_contact'));
}

function user() {
    return protractor.testData.User()[0];
}

exports.clickOnTopUpButton = function(){
browser.driver.sleep(1000).then(function() {
element(by.id('topup_button')).click();
});
}
exports.paymentAmountTextDisplayed =function(){
return browser.driver.sleep(1000).then(function() {
return getAmountLabel().getText();
});
}
exports.nextButtonDisplayed = function(){
return browser.driver.sleep(500).then(function(){
return nextButtonInTopAmountForm().isDisplayed();
});
}
function nextButtonInTopAmountForm(){
return getTopUpLayout().element(by.tagName('button'));
}
function getAmountLabel(){
return getTopUpLayout().all(by.tagName('div')).get(1);
}
function getTopUpLayout(){
return element.all(by.css('.topup-amount-payment')).get(0);
}
exports.cancelButtonDisplayed =function(){
return browser.driver.sleep(500).then(function(){
return backButtonDisplay();
});
}
exports.titleDisplayed = function(){
return browser.driver.sleep(500).then(function(){
getHeaderBar().all(by.tagName('div')).get(0).getText().then(function(text){
console.log('First'+text);
});

return getHeaderBar().all(by.css('.title.title-center.header-item')).get(0).getText();
});
}
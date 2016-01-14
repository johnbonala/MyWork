'use strict';

module.exports = {

    clickNext: function() {
        return element(by.id('login'));
    },
    clickLogin: function() {
        this.loginButton.click();
    },

    nextButton: element(by.id('.next-button')),

    loginButton: element(by.id('.login-button')),

    userId: element(by.model('loginData.username')),

    sparsePassword1: element(by.model('loginData.sparseAnswers[0]')),
    sparsePassword2: element(by.model('loginData.sparseAnswers[1]')),
    sparsePassword3: element(by.model('loginData.sparseAnswers[2]')),

    registerHere: element(by.id('login_registerHere')),

    termsOfUse: element(by.id('login_termsOfUse')),

    logo: element(by.id('login_header')),

    loginIntro: element(by.class('.login-intro')),

    forgotDetails: element(by.class('.forgot-details'))

};


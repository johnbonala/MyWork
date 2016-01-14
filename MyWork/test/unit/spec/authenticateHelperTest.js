//starter
'use strict';



describe('authenticate service', function () {

    var authenticateHelper, $httpBackend, ENV, loginErrorMessages;

    beforeEach(function () {

        module('authenticateHelper');

        inject(function (_authenticateHelper_, _$httpBackend_, _ENV_, _loginErrorMessages_) {
            authenticateHelper = _authenticateHelper_;
            $httpBackend = _$httpBackend_;
            ENV = _ENV_;
            loginErrorMessages = _loginErrorMessages_;

        });

    });

    afterEach(function () {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });


    it('should have authenticateUser function', function () {

        expect(angular.isFunction(authenticateHelper.authenticateUser)).toBe(true);

    });


    it('should parse the correct login data fields for username "tester" ', function () {

        $httpBackend.expectGET(ENV.apiEndpointSecured + 'authenticate/', {
            'Authorization': 'sls VEVTVEVS',
            'Accept': 'application/json',
            'ClientVersion': 'Standard Life:iPhone:1.3'
        }).respond(401, '2nd Call', {
            'WWW-authenticate': 'sls sparse="4,14,15", nonce="nQhCvw039sQacVbaWaVNimwbrWOKLPki0wTwmzM39DQ="'
        });

        var loginData = {

            'username': 'tester'

        };
        authenticateHelper.authenticateUser(loginData);


        $httpBackend.flush();

        expect(loginData.username).toBe('tester');
        expect(loginData.sparsePositions).toEqual({
            0: '4',
            1: '14',
            2: '15'
        });
        expect(loginData.nonce).toEqual('nQhCvw039sQacVbaWaVNimwbrWOKLPki0wTwmzM39DQ=');

    });


    it('should authenticate the user when the correct information is provided', function () {

        whenUserLogsIn();

    });


    it('should allow the user to log in and back out again', function () {


        whenUserLogsIn();

        //Logout call
        $httpBackend.expectGET(ENV.apiEndpointSecured + 'logout/', {
            'Accept': 'application/json',
            'ClientVersion': 'Standard Life:iPhone:1.3',
            'Cookie': 'rememberMe=deleteMe; Path=/secure/my-portfolio; Max-Age=0; Expires=Wed, 08-Apr-2015 22:22:24 GMT, rememberMe=deleteMe; Path=/secure/my-portfolio; Max-Age=0; Expires=Wed, 08-Apr-2015 22:22:25 GMT, MyPortfolioAuth="usSWfP9Arv8WXJ7HZeuSBhZShaXBtR/JRgLwmGvun5k=:Dt/4iamFRVCAbdepQa+6tw=="; Version=1; Secure; HttpOnly, TS01f9bbd9=0136e224fd9c0c6f778f43b31f161628f90d9cb7f97906f0bee935505ed114fefb95d786676b785e538e003e82d1039a1bc966edb2; Path=/, TS01b2a24f=0136e224fda6c1cb5c64be8baf10d971a407ced9d8f9b30e2efc23b5cbca9bdc210ea15bfa754cf48c76ef7e1f0c8627e83a5b67a0; path=/secure/my-portfolio'
        }).respond(200, 'User is logged out');


        authenticateHelper.resetAuthentication();

        $httpBackend.flush();


        expect(authenticateHelper.isAuthenticated()).toEqual(false);


    });


    it('should call for an error message when wrong password entered', function () {

        // ERROR CODE 1 echo-back unauthorized response
        $httpBackend.expectGET(ENV.apiEndpointSecured + 'authenticate/', {
            'Authorization': 'sls VEVTVEVSOjQyOjE0MjoxNTI6blFoQ3Z3MDM5c1FhY1ZiYVdhVk5pbXdicldPS0xQa2kwd1R3bXpNMzlEUT0=',
            'Accept': 'application/json',
            'ClientVersion': 'Standard Life:iPhone:1.3'
        }).respond(401, '3rd Call', {
            'WWW-Authenticate': 'slsk 7fR+dVKwXcnNvu0ULbnE24ccdXHz0EyBXkOemomMDTePX3iCF36bb0kg9uOSXBMp8zjCvsdGx2OorCWcDQtczw==:AJvQ9KsoQdPeqDrdkJ9/YgWRONG'
        });

        $httpBackend.expectGET(ENV.apiEndpointSecured + 'authenticate/', {
            'Authorization': 'slsk 7fR+dVKwXcnNvu0ULbnE24ccdXHz0EyBXkOemomMDTePX3iCF36bb0kg9uOSXBMp8zjCvsdGx2OorCWcDQtczw==:AJvQ9KsoQdPeqDrdkJ9/YgWRONG',
            'Accept': 'application/json',
            'ClientVersion': 'Standard Life:iPhone:1.3'
        }).respond(401, {
            'messages': [{
                'code': '1',
                'text': loginErrorMessages.MESSAGE_ID_PASSWORD_INCORRECT
                    }]
        });

        var loginData = {

            username: 'tester',
            sparsePositions: {
                0: '4',
                1: '14',
                2: '15'
            },
            sparseAnswers: {
                0: '2',
                1: '2',
                2: '2'
            },
            nonce: 'nQhCvw039sQacVbaWaVNimwbrWOKLPki0wTwmzM39DQ='
        };

        authenticateHelper.authenticateUser(loginData);

        $httpBackend.flush();

        expect(authenticateHelper.isAuthenticated()).toEqual(false);

    });

    it('should call for an error message when account suspended', function () {

        // ERROR CODE 2
        // Username and password call with suspended account:
        // username: tester
        // sparse: 3 3 3
        $httpBackend.expectGET(ENV.apiEndpointSecured + 'authenticate/', {
            'Authorization': 'sls VEVTVEVSOjQzOjE0MzoxNTM6blFoQ3Z3MDM5c1FhY1ZiYVdhVk5pbXdicldPS0xQa2kwd1R3bXpNMzlEUT0=',
            'Accept': 'application/json',
            'ClientVersion': 'Standard Life:iPhone:1.3'
        }).respond(401, '3rd Call', {
            'WWW-Authenticate': 'slsk 7fR+dVKwXcnNvu0ULbnE24ccdXHz0EyBXkOemomMDTePX3iCF36bb0kg9uOSXBMp8zjCvsdGx2OorCWcDQtczw==:AJvQ9KsoQdPeqDrdkJ9/YgLOCKEDOUT'
        });


        // ERROR CODE 2 echo-back unauthorized response
        $httpBackend.expectGET(ENV.apiEndpointSecured + 'authenticate/', {
            'Authorization': 'slsk 7fR+dVKwXcnNvu0ULbnE24ccdXHz0EyBXkOemomMDTePX3iCF36bb0kg9uOSXBMp8zjCvsdGx2OorCWcDQtczw==:AJvQ9KsoQdPeqDrdkJ9/YgLOCKEDOUT',
            'Accept': 'application/json',
            'ClientVersion': 'Standard Life:iPhone:1.3'
        }).respond(401, {
            'messages': [{
                'code': '2',
                'text': loginErrorMessages.MESSAGE_ACCOUNT_SUSPENDED
            }]
        });

        var loginData = {

            username: 'tester',
            sparsePositions: {
                0: '4',
                1: '14',
                2: '15'
            },
            sparseAnswers: {
                0: '3',
                1: '3',
                2: '3'
            },
            nonce: 'nQhCvw039sQacVbaWaVNimwbrWOKLPki0wTwmzM39DQ='
        };

        authenticateHelper.authenticateUser(loginData);

        $httpBackend.flush();

        expect(authenticateHelper.isAuthenticated()).toEqual(false);


    });

    it('should call for an error message when using temporary details', function () {


        // ERROR CODE 3
        // Username and password call with temporary details:
        // username: tester
        // sparse: 4 4 4
        $httpBackend.expectGET(ENV.apiEndpointSecured + 'authenticate/', {
            'Authorization': 'sls VEVTVEVSOjQ0OjE0NDoxNTQ6blFoQ3Z3MDM5c1FhY1ZiYVdhVk5pbXdicldPS0xQa2kwd1R3bXpNMzlEUT0=',
            'Accept': 'application/json',
            'ClientVersion': 'Standard Life:iPhone:1.3'
        }).respond(401, '3rd Call', {
            'WWW-Authenticate': 'slsk 7fR+dVKwXcnNvu0ULbnE24ccdXHz0EyBXkOemomMDTePX3iCF36bb0kg9uOSXBMp8zjCvsdGx2OorCWcDQtczw==:AJvQ9KsoQdPeqDrdkJ9/YgTEMPORARY'
        });

        // ERROR CODE 3 echo-back unauthorized response
        $httpBackend.expectGET(ENV.apiEndpointSecured + 'authenticate/', {
            'Authorization': 'slsk 7fR+dVKwXcnNvu0ULbnE24ccdXHz0EyBXkOemomMDTePX3iCF36bb0kg9uOSXBMp8zjCvsdGx2OorCWcDQtczw==:AJvQ9KsoQdPeqDrdkJ9/YgTEMPORARY',
            'Accept': 'application/json',
            'ClientVersion': 'Standard Life:iPhone:1.3'
        }).respond(401, {
            'messages': [{
                'code': '3',
                'text': loginErrorMessages.MESSAGE_TEMPORARY_DETAILS
            }]
        });


        var loginData = {

            username: 'tester',
            sparsePositions: {
                0: '4',
                1: '14',
                2: '15'
            },
            sparseAnswers: {
                0: '4',
                1: '4',
                2: '4'
            },
            nonce: 'nQhCvw039sQacVbaWaVNimwbrWOKLPki0wTwmzM39DQ='
        };

        authenticateHelper.authenticateUser(loginData);

        $httpBackend.flush();

        expect(authenticateHelper.isAuthenticated()).toEqual(false);
    });


    var whenUserLogsIn = function () {

        //3rd Call
        $httpBackend.expectGET(ENV.apiEndpointSecured + 'authenticate/', {
            'ClientVersion': 'Standard Life:iPhone:1.3',
            'Authorization': 'sls VEVTVEVSOjQxOjE0MToxNTE6blFoQ3Z3MDM5c1FhY1ZiYVdhVk5pbXdicldPS0xQa2kwd1R3bXpNMzlEUT0=',
            'Accept': 'application/json'
        }).respond(401, '3rd Call', {
            'WWW-Authenticate': 'slsk 7fR+dVKwXcnNvu0ULbnE24ccdXHz0EyBXkOemomMDTePX3iCF36bb0kg9uOSXBMp8zjCvsdGx2OorCWcDQtczw==:AJvQ9KsoQdPeqDrdkJ9/Yg=='
        });

        //Echo-back call
        $httpBackend.expectGET(ENV.apiEndpointSecured + 'authenticate/', {
            'ClientVersion': 'Standard Life:iPhone:1.3',
            'Authorization': 'slsk 7fR+dVKwXcnNvu0ULbnE24ccdXHz0EyBXkOemomMDTePX3iCF36bb0kg9uOSXBMp8zjCvsdGx2OorCWcDQtczw==:AJvQ9KsoQdPeqDrdkJ9/Yg==',
            'Accept': 'application/json'
        }).respond(200, '4th Call, authenticated :)', {
            'Set-Cookie': 'rememberMe=deleteMe; Path=/secure/my-portfolio; Max-Age=0; Expires=Wed, 08-Apr-2015 22:22:24 GMT, rememberMe=deleteMe; Path=/secure/my-portfolio; Max-Age=0; Expires=Wed, 08-Apr-2015 22:22:25 GMT, MyPortfolioAuth="usSWfP9Arv8WXJ7HZeuSBhZShaXBtR/JRgLwmGvun5k=:Dt/4iamFRVCAbdepQa+6tw=="; Version=1; Secure; HttpOnly, TS01f9bbd9=0136e224fd9c0c6f778f43b31f161628f90d9cb7f97906f0bee935505ed114fefb95d786676b785e538e003e82d1039a1bc966edb2; Path=/, TS01b2a24f=0136e224fda6c1cb5c64be8baf10d971a407ced9d8f9b30e2efc23b5cbca9bdc210ea15bfa754cf48c76ef7e1f0c8627e83a5b67a0; path=/secure/my-portfolio'
        });

        var loginData = {

            username: 'tester',
            sparsePositions: {
                0: '4',
                1: '14',
                2: '15'
            },
            sparseAnswers: {
                0: '1',
                1: '1',
                2: '1'
            },
            nonce: 'nQhCvw039sQacVbaWaVNimwbrWOKLPki0wTwmzM39DQ='
        };
        authenticateHelper.authenticateUser(loginData);

        $httpBackend.flush();

        expect(authenticateHelper.isAuthenticated()).toEqual(true);

    };

});
'use strict';

describe('Controllers', function () {

    describe('LoginCtrl', function () {
        var $scope,
            contentMapper,
            serviceAvailability,
            createController;
        beforeEach(module('starter.controllers', 'config'));
        
        beforeEach(inject(function ($controller, $rootScope) {
            $scope = $rootScope.$new();
            contentMapper = 'd';
            serviceAvailability = {data:
                {
	                weeklyOpeningHours: 'Monday 07:00 - Monday 23:59\nTuesday 07:00 - Tuesday 23:59\nWednesday 07:00 - Wednesday 23:59\nThursday 07:00 - Thursday 23:59\nFriday 07:00 - Friday 23:59\nSaturday 07:00 - Saturday 22:59\nSunday 07:00 - Sunday 22:59\n',
	                open: true,
                    closedText: 'The application is closed'
                }};
            
            createController = function () {
                return $controller('LoginCtrl', {
            //    $http: $httpBackend,
                    $scope: $scope,
                    contentMapper: contentMapper,
                    serviceAvailability: serviceAvailability
                });
            };
        }));
    

        it('has correct initial values', function () {
            createController();
            expect($scope.loginData.sparsePositions).toBe(null);
            expect($scope.loginData.sparseAnswers).toBe(null);
            expect($scope.userIdFocus).toBe(false);
            expect($scope.passwordFocus[0]).toBe(false);
            expect($scope.passwordFocus[1]).toBe(false);
            expect($scope.passwordFocus[2]).toBe(false);
            expect($scope.passwordFocus.length).toBe(3);
            expect($scope.isShowUserIdBtn).toBe(true);
        });
        
        it('should check 3 sparse password answers are given ', function () {
            createController();
            expect($scope.isMinPassword()).toBe(false);
            $scope.loginData.sparseAnswers = ['A'];
            expect($scope.isMinPassword()).toBe(false);
            $scope.loginData.sparseAnswers = ['A', 'B'];
            expect($scope.isMinPassword()).toBe(false);
            $scope.loginData.sparseAnswers = ['A', 'B', 'C'];
            expect($scope.isMinPassword()).toBe(true);
            $scope.loginData.sparseAnswers = ['A', 'B', 'C', 'D'];
            expect($scope.isMinPassword()).toBe(false);
            $scope.loginData.sparseAnswers = ['A', '', 'C'];
            expect($scope.isMinPassword()).toBe(false);
            $scope.loginData.sparseAnswers = ['A', null, 'C'];
            expect($scope.isMinPassword()).toBe(false);
        });
    });
});
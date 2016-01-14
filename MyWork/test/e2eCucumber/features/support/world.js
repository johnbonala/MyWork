'use strict';

var Login = require('./login.js');
var chai = require('chai');
var chaiAsPromised = require('chai-as-promised');

module.exports = function() {

    this.World = function World(callback) {

        chai.use(chaiAsPromised);
        this.expect = chai.expect;
        this.login = new Login();
        this.prop = 'Hello from the World!'; // this property will be available in step definitions

        this.greetings = function(name, callback) {
            console.log('\n----Hello ' + name);
            callback();
        };

        callback(); // tell Cucumber we're finished and to use 'this' as the world instance
    };
};


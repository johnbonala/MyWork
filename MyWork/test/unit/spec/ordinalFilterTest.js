'use strict';

describe('ordinal filter', function () {

    beforeEach(module('ordinal'));

    var ordinal;

    beforeEach(inject(function ($filter) {

        ordinal = $filter('ordinal');

    }));

    it('returns correct values for 1-10', function () {
        expect(ordinal(1)).toEqual('1st');
        expect(ordinal(2)).toEqual('2nd');
        expect(ordinal(3)).toEqual('3rd');
        expect(ordinal(4)).toEqual('4th');
        expect(ordinal(5)).toEqual('5th');
        expect(ordinal(6)).toEqual('6th');
        expect(ordinal(7)).toEqual('7th');
        expect(ordinal(8)).toEqual('8th');
        expect(ordinal(9)).toEqual('9th');
        expect(ordinal(10)).toEqual('10th');
    });

    it('returns correct values for the teens (11-19)', function () {

        expect(ordinal(11)).toEqual('11th');
        expect(ordinal(12)).toEqual('12th');
        expect(ordinal(13)).toEqual('13th');
        expect(ordinal(14)).toEqual('14th');
        expect(ordinal(15)).toEqual('15th');
        expect(ordinal(16)).toEqual('16th');
        expect(ordinal(17)).toEqual('17th');
        expect(ordinal(18)).toEqual('18th');
        expect(ordinal(19)).toEqual('19th');

    });

    it('handles non-numerical input gracefully', function () {

        expect(ordinal('test')).toEqual('test');

    });

    it('handles negative numbers by defaulting to \'th\' ', function () {

        expect(ordinal(-2)).toEqual('-2th');

    });

    it('returns correct values for a few select high numbers', function () {

        expect(ordinal(1001)).toEqual('1001st');
        expect(ordinal(789)).toEqual('789th');
        expect(ordinal(113)).toEqual('113th');

    });
    


});
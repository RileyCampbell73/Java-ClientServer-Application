/* routes.js
 * Used to setup routes for partial pages and match the page with 
 * the correct controller
 */
(function(app) {
    app.config(['$routeProvider', function($routeProvider) {
            $routeProvider
                    .when('/', {
                        controller: '',
                        templateUrl: 'partials/home.html'
                    })
                    .when('/vendors', {
                        controller: 'VendorCtrl',
                        templateUrl: 'partials/vendors.html'
                    })
                    .when('/products', {
                        controller: 'ProductCtrl',
                        templateUrl: 'partials/products.html'
                    })
                    .when('/generator', {
                        controller: 'GeneratorCtrl',
                        templateUrl: 'partials/generator.html'
                    })
                    .otherwise({redirectTo: '/'});
        }]);
})(angular.module('case1'));
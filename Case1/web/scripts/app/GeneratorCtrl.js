(function(app) {
    var GeneratorCtrl = function($scope, RESTFactory){
        var baseurl = 'webresources/generator';
        var retVal = {operations: '', productcode: -1, numOfRows: -1};
        var productsArray;
        $scope.status = "Vendors Retrieved!";
        $scope.productsArray;
        //init function
        var init = function(){
            //load data for page from WEBapi
            $scope.vendors = RESTFactory.restCall('get', 'webresources/vendor', -1, '').then(function(vendors){
                if (vendors.length > 0) {
                    $scope.vendors = vendors;
                }
                else{
                    $scope.status = 'Vendors not retrieved code - ' + vendors;
                }
            },
            function(reason) {
                $scope.status = 'Vendors not retrieved ' + reason;
            });
        }; //init
        init();
        //function for when the vendor is changed
        $scope.vendorchanged = function(){
            $scope.products = RESTFactory.restCall('get', 'webresources/product',  $scope.product.vendorno, '').then(function(products){
                $scope.products = products;
                $scope.productsArray = [];
                $scope.pickedVendor = true;
                for (var i = 0; i < products.length; i++) {
                    products[i].quantity = 0;
                    $scope.productsArray.push(products[i]);
                }
                $scope.status = 'Products Retrieved';
                
            },
            function(reason) {
                $scope.status = 'Vendors not retrieved ' + reason;
            });
        };       
        //add item to PO
        $scope.add = function(){
            $scope.subtotal = 0;
            for (var i = 0; i < $scope.productsArray.length; i++) {
                    if ($scope.productsArray[i].productcode === $scope.productcode){
                        if ($scope.quantity === "EOQ"){
                            $scope.quantity = $scope.productsArray[i].eoq;
                            $scope.productsArray[i].quantity = $scope.productsArray[i].eoq;
                        }
                        $scope.productsArray[i].quantity = $scope.quantity;
                    }
                    if($scope.productsArray[i].quantity > 0){
                        $scope.subtotal += parseFloat(($scope.productsArray[i].costprice * parseInt($scope.productsArray[i].quantity)).toFixed(2));
                    }
                    $scope.status = "Item Added!";
                }
       };
       // create the PO
        $scope.createPO = function(){
            $scope.status = "Wait....";
            var PODTO = new Object();
            PODTO.total = ($scope.subtotal * 1.13).toFixed(2);
            PODTO.vendorno = $scope.product.vendorno;
            PODTO.items = JSON.parse(JSON.stringify($scope.productsArray));
            
            $scope.PO = RESTFactory.restCall('post', 'webresources/po',
                                           $scope.product.vendorno,
                                            PODTO).then(function(results){
                 if(results.length > 0){
                     $scope.status = results;
                     $scope.notcreated = false;
                     var r = /\d+/;
                     $scope.poNum = results.match(r);
                     $scope.generated = true;
                     document.getElementById('addpo').style.visibility = 'hidden';
                     document.getElementById('viewpdf').style.visibility = 'visible';
                     //$scope.generatorForm.$setPristine();
                 }
                 else{
                     $scope.status = 'PO not create - ' + results;
                 }
            }, function(reason){
               $scope.status = 'PO not created - ' + reason; 
            });
        };//createPO
        $scope.viewPdf = function(){
             window.location.href = 'PDFSample?po=' + $scope.poNum;
         };
    };
    

    app.controller('GeneratorCtrl', ['$scope','RESTFactory', GeneratorCtrl]);
}(angular.module('case1')));

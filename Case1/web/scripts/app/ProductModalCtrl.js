(function(app) {
    var ProductModalCtrl = function($scope, RESTFactory, $modalInstance){
        var baseurl = 'webresources/product';
        var retVal = {operations: '', productcode: -1, numOfRows: -1};
        $scope.status = "";
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
        //adding a vendor
        $scope.add = function(){
            var products = $scope.products;
            var keyExists = false;
            angular.forEach(products, function(products, product) {
                if ($scope.product.productcode === products.productcode)
                    keyExists = true;
               
            });
            if (keyExists){
                $scope.status = "Cannot create a duplicate Product Code!";
            }
            //check vendor number for duplicates
            else if ($scope.product.vendorno === undefined){
                $scope.status = "Must Choose a Vendor Number!";
            }
            //check product code for duplicates
            else if ($scope.product.productcode.length > 5){
                $scope.status = "Productcode is too long! It should be no longer then 5 characters.";
            }
            //check vendorSKU for duplicates
            else if ($scope.product.vendorsku.length > 10){
                $scope.status = "VendorSKU is too long! It should be no longer then 10 characters.";
            }
            else{
                //ajax call
             RESTFactory.restCall('post', baseurl, -1, $scope.product).then(function(results){
                if (results.substring){ 
                    if(parseInt(results) > 0){
                       // $scope.product.productcode = parseInt(results);
                        $scope.products.push($scope.product);
                        retVal.numOfRows = 1;
                        retVal.operation = 'add';
                        retVal.productcode = $scope.product.productcode;
                        $modalInstance.close(retVal);
                    }
                    else{
                        retVal = 'Product was not added! - system error ' + results;
                        $modalInstance.close(parseInt(results));
                    }
                }
                else{
                    retVal = 'Product was not added! - system error ' + results;
                    $modalInstance.close(parseInt(results));
                }
             },function(reason){
                 retVal = 'Product was not added! - system error ' + reason;
                 $modalInstance.close(parseInt(reason));
             });
         }
         };//scope add
         //update product
         $scope.update = function(){
             //ajax call
            RESTFactory.restCall('put', baseurl, -1,$scope.product).then(function(results){
                retVal.operation = 'update';
                retVal.productcode = $scope.product.productcode;
                
                if(results.substring){
                    retVal.numOfRows = parseInt(results);
                }
                else{
                    retVal.numOfRows = -1;
                }
                
                $modalInstance.close(retVal);
            }, function(reason){//error
                retVal = 'Product was not updated! - system error - ' + reason;
                $modalInstance.close(retVal);
            });
        };//scope update
        //delete product
        $scope.del = function(){
              RESTFactory.restCall('delete', baseurl,$scope.product.productcode, '').then(function(results){
                retVal.operation = 'delete';
                retVal.productcode = $scope.product.productcode;
                
                if (results.substring){
                    retVal.numOfRows = parseInt(results);
                }
                else{
                    retVal.numOfRows = -1;
                }
                $modalInstance.close(retVal);
              }, function(){ //error
                  retVal.numOfRows = -1;
                  $modalInstance.close(retVal);
              });
          };//scope delete
    };
    
    app.controller('ProductModalCtrl', ['$scope','RESTFactory','$modalInstance', ProductModalCtrl]);
}(angular.module('case1')));


(function(app) {

    var ProductCtrl = function($scope, $modal, RESTFactory, $filter){
        var baseurl = 'webresources/product';
        //init 
        var init = function(){
            //load data for page from WEBapi
            $scope.status = 'Loading Products...';
            $scope.products = RESTFactory.restCall('get', baseurl, -1, '').then(function(products){
                if (products.length > 0) {
                    $scope.products = products;
                    $scope.status = 'Products Retrieved';
                }
                else{
                    $scope.products = [];
                    $scope.status = 'Products not retrieved code - ' + products;
                }
            },
            function(reason) {
                $scope.status = 'Products not retrieved ' + reason;
            });
            $scope.product = $scope.products[0];
        }; //init
        init();
        //select row function
        $scope.selectRow = function(row, product){
            if (row < 0){
                $scope.todo = 'add';
                $scope.product = new Object();
            }
            else{
                $scope.product = product;
                $scope.selectedRow = row;
                $scope.todo = 'update';
            }
            
            var modalInstance = $modal.open({
               templateUrl: 'partials/productModal.html',
               controller: 'ProductModalCtrl',
               scope: $scope
            });
            
            //modal returns results here
            modalInstance.result.then(function(results){
                if (results.operation === 'update'){

                    if (results.numOfRows ===1){
                        $scope.status = 'Product ' + results.productcode + ' Updated!';
                    }
                    else{
                        $scope.status = 'Product Not Updated!';
                    }
                }
                else if (results.operation === 'add'){
                     if (results.numOfRows ===1){
                        $scope.status = 'Product ' + results.productcode + ' Added!';
                    }
                    else{
                        $scope.status = 'Product Not Added!';
                    }
                }
                else if (results.operation === 'delete'){
                    for (var i = 0; i < $scope.products.length; i++){
                        if($scope.products[i].productcode === results.productcode){
                            $scope.products.splice(i, 1);
                            break;
                        }
                    }
                    if (results.numOfRows ===1){
                        $scope.status = 'Product ' + results.productcode + ' Deleted!';
                    }
                    else{
                        $scope.status = 'Product Not Deleted!';
                    }
                }
            },function(){
                 $scope.status = 'Product Not Updated!';
            }, function(reason){
                $scope.status = reason;
            });
        }
    };
    app.controller('ProductCtrl', ['$scope', '$modal', 'RESTFactory', '$filter', ProductCtrl]);
}(angular.module('case1')));
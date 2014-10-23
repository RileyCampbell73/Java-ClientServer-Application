(function(app) {

    var VendorCtrl = function($scope, $modal, RESTFactory, $filter){
        var baseurl = 'webresources/vendor';
        var init = function(){
            //load data for page from WEBapi
            $scope.status = 'Loading Vendors...';
            $scope.vendors = RESTFactory.restCall('get', baseurl, -1, '').then(function(vendors){
                if (vendors.length > 0) {
                    $scope.vendors = vendors;
                    $scope.status = 'Vendors Retrieved';
                }
                else{
                     $scope.vendors = [];
                    $scope.status = 'Vendors not retrieved code - ' + vendors;
                }
            },
            function(reason) {
                $scope.status = 'Vendors not retrieved ' + reason;
            });
            $scope.vendor = $scope.vendors[0];
        }; //init
        init();
        //function for row selected
        $scope.selectRow = function(row, vendor){
            if (row < 0){
                $scope.todo = 'add';
                $scope.vendor = new Object();
            }
            else{
                $scope.vendor = vendor;
                $scope.selectedRow = row;
                $scope.todo = 'update';
            }
            
            var modalInstance = $modal.open({
               templateUrl: 'partials/vendorModal.html',
               controller: 'VendorModalCtrl',
               scope: $scope
            });
            
            //modal returns results here
            modalInstance.result.then(function(results){
                if (results.operation === 'update'){

                    if (results.numOfRows ===1){
                        $scope.status = 'Vendor ' + results.vendorno + ' Updated!';
                    }
                    else{
                        $scope.status = 'Vendor Not Updated!';
                    }
                }
                else if (results.operation === 'add'){
                     if (results.numOfRows ===1){
                        $scope.status = 'Vendor ' + results.vendorno + ' Added!';
                    }
                    else{
                        $scope.status = 'Vendor Not Added!';
                    }
                }
                else if (results.operation === 'delete'){
                    for (var i = 0; i < $scope.vendors.length; i++){
                        if($scope.vendors[i].vendorno === results.vendorno){
                            $scope.vendors.splice(i, 1);
                            break;
                        }
                    }
                    if (results.numOfRows ===1){
                        $scope.status = 'Vendor ' + results.vendorno + ' Deleted!';
                    }
                    else{
                        $scope.status = 'Vendor Not Deleted!';
                    }
                }
            },function(){//error
                 $scope.status = 'Vendor Not Updated!';
            }, function(reason){//error
                $scope.status = reason;
            });
        }


    };
    app.controller('VendorCtrl', ['$scope', '$modal', 'RESTFactory', '$filter', VendorCtrl]);
}(angular.module('case1')));
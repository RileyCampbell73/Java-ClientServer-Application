
(function(app) {

    var VendorModalCtrl = function($scope, RESTFactory, $modalInstance){
        var baseurl = 'webresources/vendor';
        var retVal = {operations: '', vendorno: -1, numOfRows: -1};
        //adding a vendor
         $scope.add = function(){
             //ajax call
             RESTFactory.restCall('post', baseurl, -1, $scope.vendor).then(function(results){
                if (results.substring){ 
                    if(parseInt(results) > 0){
                        $scope.vendor.vendorno = parseInt(results);
                        $scope.vendors.push($scope.vendor);
                        retVal.numOfRows = 1;
                        retVal.operation = 'add';
                        retVal.vendorno = parseInt(results);
                        $modalInstance.close(retVal);
                    }
                    else{
                        retVal = 'Vendor was not added! - system error ' + results;
                        $modalInstance.close(parseInt(results));
                    }
                }
                else{
                    retVal = 'Vendor was not added! - system error ' + results;
                    $modalInstance.close(parseInt(results));
                }
             },function(reason){ //error
                 retVal = 'Vendor was not added! - system error ' + results;
                 $modalInstance.close(parseInt(results));
             });
         };//scope add
         //delete vendor
         $scope.del = function(){
             //ajax call
              RESTFactory.restCall('delete', baseurl,$scope.vendor.vendorno, '').then(function(results){
                retVal.operation = 'delete';
                retVal.vendorno = $scope.vendor.vendorno;
                
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
          //update vendor
        $scope.update = function(){
            //ajax call
            RESTFactory.restCall('put', baseurl, -1,$scope.vendor).then(function(results){
                retVal.operation = 'update';
                retVal.vendorno = $scope.vendor.vendorno;
                
                if(results.substring){
                    retVal.numOfRows = parseInt(results);
                }
                else{
                    retVal.numOfRows = -1;
                }
                
                $modalInstance.close(retVal);
            }, function(reason){//error
                retVal = 'Vendor was not updated! - system error - ' + reason;
                $modalInstance.close(retVal);
            });
        };//scope update
    };
        app.controller('VendorModalCtrl', ['$scope','RESTFactory','$modalInstance', VendorModalCtrl]);
}(angular.module('case1')));
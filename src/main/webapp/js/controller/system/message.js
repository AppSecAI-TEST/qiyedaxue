/*
 * 消息管理模块
 * */
angular.module('ele.message', [])
//消息管理列表控制器
.controller('MessageController',['$scope', '$http', '$state', '$timeout', 'dialogService', function ($scope, $http, $state, $timeout, dialogService) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fd', cmn: '消息管理'};
    $scope.url = "/enterpriseuniversity/services/message/findAll?pageNum=";
    $scope.paginationConf = {
		responsedError:false,
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 13,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
        	$scope.$emit('isLoading', true);
            if ($scope.messageTitle != undefined && $scope.messageTitle != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&messageTitle=" + $scope.messageTitle
            		.replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
            	if(response.result=='error'){
            		$scope.$emit('isLoading', false);
            		$scope.page = {};
            		$scope.paginationConf.responsedError = true;
            		dialogService.setContent("列表数据查询异常啦！").setShowDialog(true);
            	}else{
            		$scope.$emit('isLoading', false);
            		$scope.paginationConf.responsedError = false;
                    $scope.page = response;
                    $scope.paginationConf.totalItems = response.count;
                    $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            	}
            });
        }
    };
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
    //消息内容
    $scope.doDetails = function(){
        $scope.go('/system/messageDetails','messageDetails',null);
    }
    //推送消息
    $scope.doAdd = function(){
        $scope.go('/system/addMessage','addMessage',null);
    }
    //推送消息
    $scope.doSet = function(){
        $scope.go('/system/messageSet','messageSet',null);
    }
    $scope.doView = function(message){
        $scope.go('/system/viewMessage','viewMessage',{id:message.messageId});
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.messageId = item.messageId; 
    }
}])
.directive('requiredMessageemps', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
		      var customValidator = function (value) {
		    	  var validity = null;
		    	  scope.$watch(function () {
		    		  return value.length;
		          }, function(){
		        	  validity = value!=undefined? value.length <1 : true;
		              ngModel.$setValidity("requiredMessageemps", !validity);
		          });
		          return !validity ? value : undefined;
		      };
		      ngModel.$formatters.push(customValidator);
		      ngModel.$parsers.push(customValidator);
	      }
	  };
 }])
.directive('requiredSendway', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
		      var customValidator = function (value) {
		    	  var validity = null;
		    	  scope.$watch(function () {
		    		  return value.length;
		          }, function(){
		        	  validity = value!=undefined ? value.length <1 : true;
		              ngModel.$setValidity("requiredSendway", !validity);
		          });
		          return !validity ? value : [];
		      };
		      ngModel.$formatters.push(customValidator);
		      ngModel.$parsers.push(customValidator);
	      }
	  };
}])
//消息推送
.controller('AddMessageController', ['$scope', '$http', '$timeout', 'dialogService', 'saveDialog', function  ($scope, $http,$timeout, dialogService, saveDialog) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fd', cmn: '消息管理', gcmn: '消息推送'};
    $scope.message = $scope.formData={empIds:[]};
    $scope.sendWay = [];
    $scope.message.sendToApp = "N";
	$scope.message.sendToEmail = "N";
	$scope.message.sendToPhone = "N";
    $scope.toggleSendToApp = function(){
    	if($scope.message.sendToApp=="Y"){
    		$scope.message.sendToApp = "N";
    		$scope.sendWay.splice(0,1);
    	}else{
    		$scope.message.sendToApp = "Y";
    		$scope.sendWay.push(1);
    	}
    }
    $scope.toggleSendToEmail = function(){
    	if($scope.message.sendToEmail=="Y"){
    		$scope.message.sendToEmail = "N";
    		$scope.sendWay.splice(0,1);
    	}else{
    		$scope.message.sendToEmail = "Y";
    		$scope.sendWay.push(1);
    	}
    }
    $scope.toggleSendToPhone = function(){
    	if($scope.message.sendToPhone=="Y"){
    		$scope.message.sendToPhone = "N";
    		$scope.sendWay.splice(0,1);
    	}else{
    		$scope.message.sendToPhone = "Y";
    		$scope.sendWay.push(1);
    	}
    }
    
    //保存按钮
    $scope.doSave = function(){
    	$scope.messageForm.$submitted = true;
    	if($scope.messageForm.$invalid)
    	{
    		dialogService.setContent("表单存在填写不合法的字段,请按提示修改后重试").setShowDialog(true);
    		return;
    	}
    	saveDialog.saving();
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/message/sendMessage',
    		data : $.param($scope.message),
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
    	})
    	.success(function(data) {
    		saveDialog.saved();
    		if(data=="error"){
    			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
        		$timeout(function(){
        			dialogService.sureButten_click(); 
        		},2000);
    		}else{  		
    		dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    			$scope.go('/system/messageList','messageList',null);
    		},2000);
    		}
    	})
    	.error(function(){
    		saveDialog.saved();
    		dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    		},2000);
    	});
    } 
    //返回按钮
    $scope.doReturn = function(){
    	$scope.go('/system/messageList','messageList',null);
    };
}])
//查看
.controller('ViewMessageController', ['$scope', '$http', '$stateParams', '$q','$timeout', 'dialogService', function  ($scope, $http, $stateParams, $q,$timeout, dialogService) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fd', cmn: '消息管理', gcmn: '查看消息'};
    $scope.deferred = $q.defer();
    if ($stateParams.id) 
    {
        $http.get("/enterpriseuniversity/services/message/findOne?messageId=" + $stateParams.id).success(function (data)  {
        	$scope.deferred.resolve(data);
    	}).error(function(data){
    		$scope.deferred.reject(data); 
    	});
    } 
    else 
    {
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.message = $scope.formData = data; 
    }, function(data) {  // 错误回调
        dialogService.setContent("页面数据初始化异常").setShowDialog(true);
        return;
    });
    //返回按钮
    $scope.doReturn = function(){
    	$scope.go('/system/messageList','messageList',null);
    };
}])
//系统消息设置
.controller('SystemMessageSetController', ['$scope', '$http', '$q','dialogService', '$timeout','saveDialog', function  ($scope, $http, $q, dialogService,$timeout,  saveDialog) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fd', cmn: '消息管理', gcmn: '消息设置'};
    $scope.deferred = $q.defer();
    $scope.sendWay = [];
    $http.get("/enterpriseuniversity/services/message/findOne?messageId=0" ).success(function (data)  {
    	$scope.deferred.resolve(data);
	}).error(function(data){
		$scope.deferred.reject(data); 
	});
    $scope.deferred.promise.then(function(data) {
    	$scope.messageSet = $scope.formData = data; // 成功回调
    	$scope.messageSet.messageId=0;
    	if($scope.messageSet.sendToApp=="Y"){
    		$scope.sendWay.push(1);
    	}else if($scope.messageSet.sendToEmail=="Y"){
    		$scope.sendWay.push(1);
    	}else if($scope.messageSet.sendToPhone=="Y"){
    		$scope.sendWay.push(1);
    	}
    }, function(data) {  // 错误回调
        dialogService.setContent("页面数据初始化异常").setShowDialog(true);
        return;
    });
    $scope.toggleSendToApp = function(){
    	if($scope.messageSet.sendToApp=="Y"){
    		$scope.messageSet.sendToApp = "N";
    		$scope.sendWay.splice(0,1);
    	}else{
    		$scope.messageSet.sendToApp = "Y";
    		$scope.sendWay.push(1);
    	}
    }
    $scope.toggleSendToEmail = function(){
    	if($scope.messageSet.sendToEmail=="Y"){
    		$scope.messageSet.sendToEmail = "N";
    		$scope.sendWay.splice(0,1);
    	}else{
    		$scope.messageSet.sendToEmail = "Y";
    		$scope.sendWay.push(1);
    	}
    }
    $scope.toggleSendToPhone = function(){
    	if($scope.messageSet.sendToPhone=="Y"){
    		$scope.messageSet.sendToPhone = "N";
    		$scope.sendWay.splice(0,1);
    	}else{
    		$scope.messageSet.sendToPhone = "Y";
    		$scope.sendWay.push(1);
    	}
    }
    
    //保存按钮
    $scope.doSave = function(){
    	$scope.messageSetForm.$submitted = true;
    	if($scope.messageSetForm.$invalid)
    	{
    		dialogService.setContent("表单存在填写不合法的字段,请按提示修改后重试").setShowDialog(true);
    		return;
    	}
    	saveDialog.saving();
    	$http({
    		method : 'PUT',
    		url  : '/enterpriseuniversity/services/message/updateSysMessage',
    		data : $.param($scope.messageSet),
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
    	})
    	.success(function(data) {
    		saveDialog.saved();
    		dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    			$scope.go('/system/messageList','messageList',null);
    		},2000);
    	})
    	.error(function(){
    		saveDialog.saved();
    		dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    		},2000);
    	});
    } 
    //返回按钮
    $scope.doReturn = function(){
    	$scope.go('/system/messageList','messageList',null);
    };
}])
//消息内容
.controller('MessageDetailsController', ['$scope', '$http', '$stateParams','$q','$timeout', 'dialogService', 'saveDialog', function  ($scope, $http,$stateParams,$q,$timeout, dialogService, saveDialog) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fd', cmn: '消息管理', gcmn: '消息内容'};
    $scope.data = {
    		//status:'Y',
    }
    $scope.deferred = $q.defer();
        $http.get("/enterpriseuniversity/services/messageDetails/findById" ).success(function (data)  {
        	$scope.deferred.resolve(data);
    	}).error(function(data){
    		$scope.deferred.reject(data); 
    	});
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.messageDetails = $scope.formData = data; 
    }, function(data) {  // 错误回调
        dialogService.setContent("页面数据初始化异常").setShowDialog(true);
        return;
    });
  
    //保存按钮
    $scope.doSave = function(){
    	$scope.data.messageDetails=$scope.messageDetails;	 
    	$scope.messageDetailsForm.$submitted = true;
    	if($scope.messageDetailsForm.$invalid)
    	{
    		dialogService.setContent("表单存在填写不合法的字段,请按提示修改后重试").setShowDialog(true);
    		return;
    	}
    	saveDialog.saving();
    	$http({
    		method : 'PUT',
    		url  : '/enterpriseuniversity/services/messageDetails/update',
    		data :$scope.data,
    		headers : {'Content-Type': 'application/json' } 
    	})
    	.success(function(data) {
    		saveDialog.saved();
    		if(data=="error"){
    			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
        		$timeout(function(){
        			dialogService.sureButten_click(); 
        		},2000);
    		}else{  		
    		dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    			$scope.go('/system/messageList','messageList',null);
    		},2000);
    		}
    	})
    	.error(function(){
    		saveDialog.saved();
    		dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    		},2000);
    	});
    } 
    //返回按钮
    $scope.doReturn = function(){
    	$scope.go('/system/messageList','messageList',null);
    };
}])
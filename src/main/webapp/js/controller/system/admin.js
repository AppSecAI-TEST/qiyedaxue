/*
 * 权限管理
 * */
angular.module('ele.admin', [])
.controller('AdminController',['$scope', '$http', '$state','dialogService','$timeout', function ($scope, $http, $state,dialogService,$timeout) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fc', cmn: '权限管理'};
    $scope.url = "/enterpriseuniversity/services/admin/findpage?pageNum=";
    $scope.paginationConf = {
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
            if ($scope.adminName != undefined && $scope.adminName != "") {

            	$scope.$httpPrams = $scope.$httpPrams + "&name=" 
            		+ $scope.adminName.replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");

            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
            	+ $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
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
    //新增
    $scope.doAdd = function(){
        $scope.go('/system/addAdmin','addAdmin',null);
    }
    //删除
    $scope.doDelete = function (admin) {
    	
    	dialogService.setContent("确定删除管理员"+admin.name+"?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
    	.doNextProcess = function(){
        $http({
            url: "/enterpriseuniversity/services/admin/deleteByPrimaryKey?adminId=" + admin.adminId,
            method: "DELETE"
        }).success(function (response) {
        	dialogService.setContent("删除成功").setShowSureButton(false).setShowDialog(true);
        	$timeout(function(){        		
        		dialogService.sureButten_click(); 
        		$scope.search();
	    	},2000);
        }).error(function(){
        	dialogService.setContent("删除失败请稍后再试").setShowSureButton(false).setShowDialog(true);
        	$timeout(function(){
        		dialogService.sureButten_click(); 
        	},2000);
        });
    	}
    }
    // 禁用
    $scope.doUpdate = function (admin) {
    	dialogService.setContent(admin.status == "Y"?"确定禁用管理员?":"确定启用管理员?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
     	.doNextProcess = function(){
        $http({
            url: "/enterpriseuniversity/services/admin/updateStatus?adminId=" + admin.adminId + "&status=" + (admin.status == "Y" ? "N" : "Y"),
            method: "PUT"
        }).success(function (response) {
        	$timeout(function(){
        	if(admin.status == "Y"){
        		dialogService.setContent("禁用成功").setShowSureButton(false).setShowDialog(true);
        		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.search();
				},2000);
        	}else{
        		dialogService.setContent("启用成功").setShowSureButton(false).setShowDialog(true);
        		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.search();
				},2000);
        	}
         	},200);
        }).error(function(){
        	$timeout(function(){
        		if(admin.status == "Y"){
            		dialogService.setContent("禁用失败，请稍后再试").setShowSureButton(false).setShowDialog(true);
            		$timeout(function(){
    					dialogService.sureButten_click(); 
    				},2000);
            	}else{
            		dialogService.setContent("启用失败，请稍后再试").setShowSureButton(false).setShowDialog(true);
            		$timeout(function(){
    					dialogService.sureButten_click(); 
    				},2000);
            	}
        	},200);
        });
    	}
    }
    //编辑
    $scope.doEdit = function (admin) {
        $scope.go('/system/editAdmin', 'editAdmin', {id: admin.adminId});
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(admin){
        $scope.currHighLightRow.adminId = admin.adminId; 
    }  
}])
.directive('requiredAdminemp', [function () {
	return {
		require: "ngModel",
		link: function (scope, element, attr, ngModel) {
			var customValidator = function (value) {	
				var validity = null;
				scope.$watch(function () {
					return value;
				}, function(){
					validity = (value!=undefined&&value!="") ? false : true;
					ngModel.$setValidity("requiredAdminemp", !validity);
				});
				return !validity ? value : undefined;
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])
.directive('requiredAdminrole', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = (value!=undefined&&value!="") ? (value.split(",").length < 1 ? true : false) : true;
	                  ngModel.$setValidity("requiredAdminrole", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
//添加管理员函数
.controller('AddAdminController', ['$scope', '$http', '$timeout', 'dialogService', function  ($scope, $http, $timeout, dialogService) {
	$scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fc', cmn: '权限管理', gcmn: '新增管理员'};
	$scope.admin = $scope.formData={tomrole:[]};
    $http.get("/enterpriseuniversity/services/role/findAll").success(function(response) {
    	$scope.roleList = response;
	}).error(function(){
		dialogService.setContent("查询角色数据响应错误").setShowDialog(true);
	});
    $scope.itemArr = {
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.checked) {
                item.checked = undefined;
                for(var i in $scope.admin.tomrole){
                	if($scope.admin.tomrole[i].roleId==item.roleId){
                		$scope.admin.tomrole.splice(i, 1);
                		break;
                	}
                }
            } else {
            	item.checked = true;
            	$scope.admin.tomrole.push(item);
            }
            //生成表单提交数据
            $scope.admin.roleId = "";
        	for(var i in $scope.admin.tomrole){
            	if(i<$scope.admin.tomrole.length-1)
        		{
        			$scope.admin.roleId = $scope.admin.roleId + $scope.admin.tomrole[i].roleId+",";
        		}else
        		{
        			$scope.admin.roleId = $scope.admin.roleId + $scope.admin.tomrole[i].roleId;
        		}
        	}
        } 
    };
    //保存按钮
    $scope.doSave = function(){
    	$scope.adminForm.$submitted = true;
    	if(!$scope.adminForm.$invalid)
   	 	{
    		$http({
    			method : 'POST',
    			url  : '/enterpriseuniversity/services/admin/insertSelective',
    			data : $.param($scope.admin),
    			headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
			})
			.success(function(data) {
				dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   			$scope.go('/system/adminList','adminList',null);
		   		},2000);
			})
			.error(function(){
				dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   		},2000);
			});
   	 	}
    }
    //返回按钮
	$scope.doReturn = function(){
		$scope.go('/system/adminList','adminList',null);
	};
}])
//编辑管理员控制器
.controller('EditAdminController',['$scope', '$http', '$stateParams', '$q', '$timeout', 'dialogService', function ($scope, $http, $stateParams, $q, $timeout, dialogService) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fc', cmn: '权限管理', gcmn: '编辑管理员'};
    $scope.deferred = $q.defer();
    $scope.deferredAll = $q.defer();
    if ($stateParams.id) 
    {
        $http.get("/enterpriseuniversity/services/admin/selectByAdminId?adminId=" + $stateParams.id).success(function (data) {
            $scope.deferred.resolve(data);
        }).error(function(data){
        	$scope.deferred.reject(data); 
        });
    } 
    else
    {
        //未传参数
    	dialogService.setContent("获取参数错误").setShowDialog(true);
    	return;
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.admin = $scope.formData = data; 
    	//查询角色数据
        $http.get("/enterpriseuniversity/services/role/findAll").success(function(data) {
        	$scope.deferredAll.resolve(data);
		}).error(function(){
			$scope.deferredAll.reject(data); 
		});
    }, function(data) {  // 错误回调
    	dialogService.setContent("查询管理员数据异常").setShowDialog(true);
    }); 
        
    $scope.deferredAll.promise.then(function(data) {  // 成功回调
    	$scope.roleList  = data; 
    	$scope.itemArr.initChoosedItemArrStatus();
    	$scope.itemArr.buildRoleid();
    }, function(data) {  // 错误回调
        dialogService.setContent("查询角色数据异常").setShowDialog(true);
    }); 
        
    $scope.itemArr = {
		initChoosedItemArrStatus: function () {
            if ($scope.admin.tomrole) {
                for (var i in $scope.admin.tomrole) {
                	for(var j in $scope.roleList){
                		 if($scope.roleList[j].roleId==$scope.admin.tomrole[i].roleId||$scope.roleList[j].checked){	                		
                			 $scope.roleList[j].checked = true;
                		 }
                		 else
                		 {
                			 $scope.roleList[j].checked = undefined;
                		 }
                	}
                }
            }
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.checked) {
            	item.checked = undefined;
                for(var i in $scope.admin.tomrole){
                	if($scope.admin.tomrole[i].roleId==item.roleId){
                		$scope.admin.tomrole.splice(i, 1);
                	}
                }
            } else {
            	item.checked = true;
            	$scope.admin.tomrole.push(item);
            }
            $scope.itemArr.buildRoleid();
        },
        buildRoleid : function () {
            $scope.admin.roleId = "";
	    	for(var i in $scope.admin.tomrole){
	    		if(i<$scope.admin.tomrole.length-1)
	    		{
	    			$scope.admin.roleId = $scope.admin.roleId + $scope.admin.tomrole[i].roleId+",";
	    		}else
	    		{
	    			$scope.admin.roleId = $scope.admin.roleId + $scope.admin.tomrole[i].roleId;
	    		}
	    	}
        }
    };
    //保存按钮
    $scope.doSave = function(){
    	$scope.adminForm.$submitted = true;
    	if($scope.adminForm.$invalid)
    	{
			//  dialogService.setContent("表单填写不完整").setShowDialog(true);
    		return;
   	 	}
	    	
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/admin/updateByPrimaryKey',
    		data : $.param($scope.admin),
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
		})
		.success(function(data) {
			dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
				$timeout(function(){
	   			dialogService.sureButten_click(); 
	   			$scope.go('/system/adminList','adminList',null);
	   		},2000);
		})
		.error(function(){
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   		},2000);
		});
    }
    $scope.doReturn = function(){
    	$scope.go('/system/adminList','adminList',null);
    };
}])
//模态框函数
.controller('AdminModalController',['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
    $scope.url = "/enterpriseuniversity/services/admin/selectAdmin?pageNum=";
    $scope.openModalDialog = false;
    $scope.isInitUpdatePageData = false;
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 7,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	if(!($scope.isInitUpdatePageData||$scope.openModalDialog)){
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
            if ($scope.adminName != undefined && $scope.adminName != "") 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&name=" 
            		+ $scope.adminName.replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
            	+ $scope.$httpPrams;
            $scope.$emit('isLoading', true); 
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                //初始化模态框选中状态
                if ($scope.itemArr.initCurrChoosedItemArrStatus&&$scope.isBuildOldChoosedItems) 
                {
                    $scope.itemArr.initCurrChoosedItemArrStatus();
                } 
                if($scope.itemArr.buildOldChoosedItemArr&&!$scope.isBuildOldChoosedItems)
                {
                	$scope.itemArr.buildOldChoosedItemArr();
                }
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("管理员数据初始化异常").setShowDialog(true);
            });
        }
    };
    $scope.search = function () {
    	if($scope.isBuildOldChoosedItems){
    		$scope.paginationConf.currentPage = 1;
        	$scope.paginationConf.itemsPerPage = 20 ;
    	}else
    	{
    		$scope.paginationConf.currentPage = 1;
    		//查询全部数据用于buildOldChoosedItemArr
        	$scope.paginationConf.itemsPerPage = "全部" ;
    	}
    	$scope.paginationConf.onChange();
    };
        
    $scope.isBuildOldChoosedItems = false;
    /*
     * 模态框容器
     * */
    $scope.itemArr = {
    	isChooseAllItem: false,
    	oldChooseItemArr: [],
        choosedItemArr: [],
        currChoosedItemArr: [],
        //选中choosedItemArr中的项           
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true); 
            for (var i in $scope.itemArr.currChoosedItemArr) {
            	for(var j in $scope.page.data){
            		 if($scope.page.data[j].adminId==$scope.itemArr.currChoosedItemArr[i].adminId){
            			 $scope.page.data[j].checked = true;
            		 }
            	}
            }
            $scope.$emit('isLoading', false); 
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.checked) 
            {
                item.checked = undefined;
                for(var i in $scope.itemArr.currChoosedItemArr)
                {
                	if(item.adminId==$scope.itemArr.currChoosedItemArr[i].adminId)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                	}
                }
            } 
            else 
            {
                item.checked = true;
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        //删除操作
        deleteItem : function (item) {
            item.checked = undefined;
            for(var i in $scope.itemArr.choosedItemArr){
            	if(item.adminId==$scope.itemArr.choosedItemArr[i].adminId){
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            	}
            }
            $scope.buildAdmins();
        },
        //全选
        chooseAllItem : function(){
        	//...
        	if(this.isChooseAllItem){
        		this.isChooseAllItem = false;
        		
        		
        	}else{
        		this.isChooseAllItem = false;
        		
        		
        	}
        },
        //编辑页面数据回显
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true); 
        	if ($scope.page.data) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var adminId = $scope.itemArr.oldChooseItemArr[i];
	            	for (var j in $scope.page.data) 
	            	{
	            		if($scope.page.data[j])
	            		{
	            			if($scope.page.data[j].adminId==adminId)
	            			{
	            				if($scope.itemArr.choosedItemArr.length<1)
	            				{
	            					$scope.itemArr.choosedItemArr.push($scope.page.data[j]);
	            					continue;
	            				}
	            				var isContained = false;
	            				for(var k in $scope.itemArr.choosedItemArr)
	            				{
	            					if($scope.itemArr.choosedItemArr[k].adminId==$scope.page.data[j].adminId)
	            					{
	            						isContained = true;
	            						break;
    	            				} 
	            				}
	            				if(!isContained)
	            				{
	            					$scope.itemArr.choosedItemArr.push($scope.page.data[j]);
	            				}
    		            	}
	            		}
		            }
	            }
            }
        	$scope.buildAdmins();//删除系统中失效的管理员项
        	$scope.$emit('isLoading', false); 
        }
    };
    //初始化管理员项
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	var adminIdArr = [];
	    	if($scope.formData.admins)
	    	{
	    		var adminIdArr = $scope.formData.admins.split(",");
	    	}
	    	console.log("admins",adminIdArr);
    		for(var i = 0;i<adminIdArr.length;i++)
    		{
        		if(adminIdArr[i]!=""&&$scope.itemArr.oldChooseItemArr.indexOf(adminIdArr[i])==-1)
        		{
        			$scope.itemArr.oldChooseItemArr.push(adminIdArr[i]);
        		}
        	}
    		$scope.isInitUpdatePageData = true;
	    	$scope.search(); 
        }, function(data) {}); 
    }
    // admins  字符串
    $scope.buildAdmins = function () {
        var adminsArry = [];
        for (var i in $scope.itemArr.choosedItemArr) {
            adminsArry.push($scope.itemArr.choosedItemArr[i].adminId);
        }
        if(adminsArry.length>0){
        	$scope.$parent.$parent.formData.admins = ","+adminsArry.join(",")+",";
        }else{
        	$scope.$parent.$parent.formData.admins = "";
        }
    }
    //显示模态框
    $scope.$parent.$parent.doOpenAdminModal = function () {
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.isBuildOldChoosedItems = true;
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
        //清空查询条件
        $scope.adminName = "";
        $scope.paginationConf.currentPage = 1;
        //初始化每页显示条数
        $scope.paginationConf.itemsPerPage = 20;
        //重新查询模态框中的数据
        $scope.search();
    }
    //确定按钮
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        //每次点击确定按钮，生成新的  admins
        $scope.buildAdmins();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭按钮
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
//必选管理员
.directive('requiredAdmin', [function () {
	return {
		require: "ngModel",
		link: function (scope, element, attr, ngModel) {
			var customValidator = function (value) {
				var validity = null;
				scope.$watch(function () {
					return value;
				}, function(){
					validity = (value!=undefined&&value!="") ? (value.substr(1,value.length-2).split(",").length < 1 ? true : false):true;
					ngModel.$setValidity("requiredAdmin", !validity);
				});
				return !validity ? value : undefined;
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
      	}
	};
}]);
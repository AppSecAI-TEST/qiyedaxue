angular.module('ele.role', [])
.controller('RoleController', ['$scope', '$http', '$state', '$timeout', 'dialogService', function  ($scope, $http, $state, $timeout, dialogService) {
	$scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fb', cmn: '角色管理'};
	$scope.url = "/enterpriseuniversity/services/role/findpage?pageNum=";
	$http.get("/enterpriseuniversity/services/authoritiesTypeRest/findAll").success(function (response) {
        $scope.authorities = response;
    }).error(function () {
    	dialogService.setContent("角色权限下拉菜单初始化错误").setShowDialog(true)
    });
	$http.get("/enterpriseuniversity/services/role/findAllScope").success(function (response) {
	    $scope.scope = response;
	}).error(function () {
		dialogService.setContent("角色范围下拉菜单初始化错误").setShowDialog(true)
	});
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
				if ($scope.roleName != undefined&& $scope.roleName != "") {
					$scope.$httpPrams =$scope.$httpPrams+ "&roleName=" 
						+ $scope.roleName.replace(/\%/g,"%25").replace(/\#/g,"%23")
	        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
	        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
				}
				if ($scope.selectedScope != undefined&& $scope.selectedScope != "") {
					$scope.$httpPrams =$scope.$httpPrams+ "&scope=" + $scope.selectedScope;
				}
				if ($scope.selectedAuthorities != undefined&& $scope.selectedAuthorities != "") {
					$scope.$httpPrams =$scope.$httpPrams+ "&authority_id=" + $scope.selectedAuthorities;
				}
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" + 
					($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function (response) {
		        	$scope.$emit('isLoading', false);
		            $scope.page = response;
		            $scope.paginationConf.totalItems = response.count;
		            $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
		        }).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("角色数据查询异常！").setShowDialog(true);
	            });
            }
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1 ;
		$scope.paginationConf.itemsPerPage = 20 ;
		$scope.paginationConf.onChange();
	};
	//回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
	//查询下拉菜单改变后查询列表数据
	$scope.changeSeclectOption = function(){
    	$scope.search();
    }
	// 新增
	$scope.doAdd = function() {
		$scope.go('/system/addRole', 'addRole', null);
	}
	/*
	 * 删除角色 特有方法
	 */
	$scope.doDelete = function(role) {
		dialogService.setContent("确定删除角色："+role.roleName+"?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
		$http({
			url : "/enterpriseuniversity/services/role/deleteByPrimaryKey?roleId=" + role.roleId,
			method : "delete"
		}).success(function(response) {
		 	$timeout(function(){
			if(response.result=="success"){
				dialogService.setContent("删除角色成功").setShowSureButton(false).setShowDialog(true);
				$timeout(function(){        		
	        		dialogService.sureButten_click(); 
	        		$scope.search();
		    	},2000);
			}else if(response.result=="wrong"){
				dialogService.setContent("有管理员使用这个角色，不能删除").setShowSureButton(false).setShowDialog(true);
				$timeout(function(){        		
	        		dialogService.sureButten_click(); 
	        		$scope.search();
		    	},2000);
			}},200);
		}).error(function(){
			dialogService.setContent("删除角色失败").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
			
    		dialogService.sureButten_click(); 
			},2000);
		});
		}
	
	}
	// 编辑
	$scope.doEdit = function(role) {
		$scope.go('/system/editRole', 'editRole', { id : role.roleId });
	}
	/*
	 * 禁用角色 特有方法
	 */
	$scope.doUpdate = function(role) {
		dialogService.setContent(role.status == "Y"?"确定禁用角色?":"确定启用角色?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
    		.doNextProcess = function(){
			$http({
				url : "/enterpriseuniversity/services/role/updateStatus?roleid=" + role.roleId + "&status=" + (role.status == 'Y' ? 'N' : 'Y'),
				method : "PUT"
			}).success(function(response) {
				if(response.result=="success"){
					if(role.status == "Y"){
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
					sure = function(){$scope.search();}
				}else if(response.result=="wrong"){
					dialogService.setContent("有管理员使用这个角色，不能禁用").setShowSureButton(false).setShowDialog(true);
					$timeout(function(){        		
		        		dialogService.sureButten_click(); 
		        		$scope.search();
			    	},2000);
				}
			}).error(function(){
				if(role.status == "Y"){
					dialogService.setContent("禁用失败").setShowSureButton(false).setShowDialog(true);
					$timeout(function(){        		
		        		dialogService.sureButten_click(); 
			    	},2000);
            	}else{
            		dialogService.setContent("启用失败").setShowSureButton(false).setShowDialog(true);
            		$timeout(function(){        		
                		dialogService.sureButten_click(); 
        	    	},2000);
            	}
			});
		}
	}
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(role){
        $scope.currHighLightRow.roleId = role.roleId; 
    }  
}])
.directive('requiredRolescope', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  /*validity = ngModel.$isEmpty(value) ;*/
	            	  validity = value!=undefined&&value!="" ? (new String(value).split(",").length < 0 ? true : false):true;
	                  ngModel.$setValidity("requiredRolescope", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])				
.directive('requiredRoleauthority', [function () {
	  return {
	      require: "ngModel",
	  link: function (scope, element, attr, ngModel) {
	      var customValidator = function (value) {
	    	  var validity = null;
	    	  scope.$watch(function () {
	              return value;
	          }, function(){
	        	  /*validity = ngModel.$isEmpty(value) ;*/
	        	  validity = value!=undefined&&value!="" ? (new String(value).split(",").length < 0 ? true : false):true;
	              ngModel.$setValidity("requiredRoleauthority", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
// 添加角色页面控制器
.controller('AddRoleController', ['$scope', '$http', '$q', '$state','dialogService','$timeout', function($scope, $http ,$q, $state,dialogService,$timeout) {
	$scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fb', cmn: '角色管理', gcmn :'添加角色'};
	$scope.role = {};
	$scope.queryDeptRoleScope = function(){
		//查询所有角色范围树  部门级  $scope.role.roleType = "2"
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/orggroups/selectByScope").success(function(response) {	
			$scope.$emit('isLoading', false);
			$scope.dataList = response;
		}).error(function(){
			$scope.$emit('isLoading', false);
			dialogService.setContent("查询部门级权限范围错误").setShowDialog(true);
		});
	}
	$scope.queryCompanyRoleScope = function(){
		//查询所有角色范围树  公司级  $scope.role.roleType = "1"
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/orggroups/selectByScope2").success(function(response) {
			$scope.$emit('isLoading', false);
			$scope.dataList = response;
		}).error(function(){
			$scope.$emit('isLoading', false);
			dialogService.setContent("查询 公司级权限范围错误").setShowDialog(true);
		});
	}
	// 获取所有权限
	$scope.getAuthorityTreeData = function(){
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/authoritiesRest/selectByRole").success(function(response) {
			$scope.authorityList = response;
			$scope.$emit('isLoading', false);
		}).error(function(){
			$scope.$emit('isLoading', false);
		});
	}
	$scope.getAuthorityTreeData();
	$scope.deferred = $q.defer();
	$http.get("/enterpriseuniversity/services/role/findType").success(function(response) {
		$scope.deferred.resolve(response);
	}).error(function(response){
		$scope.deferred.reject(response); 
	});
	$scope.deferred.promise.then(function(data) {  // 成功回调
		if(!data.result){
			$scope.type = data;
			if($scope.type==1){
				$scope.roleTypeOptions = [{name:"公司级",value:"1"},{name:"部门级",value:"2"}];
				$scope.role.roleType = "1";
				$scope.queryCompanyRoleScope();
			}else{
				$scope.roleTypeOptions = [{name:"部门级",value:"2"}];
				$scope.role.roleType = "2";
				$scope.queryDeptRoleScope();
			}
		}else{
			dialogService.setContent("角色类型数据查询异常！").setShowDialog(true);
		}
    }, function(data) {  // 错误回调
        dialogService.setContent("角色类型数据查询异常！").setShowDialog(true);
    }); 
	
	$scope.doSave = function() {
		$scope.addRoleForm.$submitted = true;
		if($scope.addRoleForm.$invalid)
		{
			// dialogService.setContent("表单填写不完整").setShowDialog(true);
			return;
		}
		$scope.$emit('isLoading', true);
		$http({
			method : 'POST',
			url : '/enterpriseuniversity/services/role/insert',
			data : $.param($scope.role),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
			$scope.$emit('isLoading', false);
			dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   			$scope.go('/system/roleList', 'roleList', null);
	   		},2000);			
		}).error(function() {
			$scope.$emit('isLoading', false);
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   		},2000);
		});
	}
	$scope.switchData = function(){
		$scope.role.roleScope = "";
		$scope.itemArr.choosedItemArr=[];
		if($scope.role.roleType==1)
		{
			$scope.queryCompanyRoleScope();
		}
		else
		{
			$scope.queryDeptRoleScope();
		}
	}
	$scope.buildRoleScope = function(){
		var roleScopeId = [];
		for(var i in $scope.itemArr.choosedItemArr){
			roleScopeId.push($scope.itemArr.choosedItemArr[i].code);
		}
		$scope.role.roleScope = roleScopeId.join(",");
	}
	$scope.buildRoleAuthority = function(){
		var roleAuthorityId = [];
		for(var i in $scope.itemArr2.choosedItemArr){
			roleAuthorityId.push($scope.itemArr2.choosedItemArr[i].authorityTypeId);
		}
		$scope.role.roleAuthority  = roleAuthorityId.join(",");
	}
	$scope.itemArr = {
		choosedItemArr: [],
		chooseItem : function (item) {
            if (item.selected) 
            {
            	for(var i in $scope.itemArr.choosedItemArr)
            	{
                	if($scope.itemArr.choosedItemArr[i].code==item.code)
                	{
                		$scope.itemArr.choosedItemArr.splice(i, 1);
                		$scope.buildRoleScope();
                	}
                }
            } 
            else
            {
                $scope.itemArr.choosedItemArr.push(item);
                $scope.buildRoleScope();
            }
       }			   
	};
	$scope.itemArr2 = {
		choosedItemArr: [],	
		indexOf:function(itemArry,item,mapKey){
			if(!itemArry){
				return -1;
			}
			for(var i in itemArry){
				if(itemArry[i][mapKey]==item[mapKey]){
					return i;
				}
			}
			return -1;
		},
		chooseItem : function (item) {
            if (item.selected) 
            {
            	for(var i in $scope.itemArr2.choosedItemArr)
            	{
                	if($scope.itemArr2.choosedItemArr[i].authorityTypeId==item.authorityTypeId)
                	{
                		$scope.itemArr2.choosedItemArr.splice(i, 1);
                		$scope.buildRoleAuthority();
                		break;
                	}
                }
            	//勾选父节点or取消勾选父节点
            	var parentNode = null;
            	for(var i in $scope.authorityList){
            		var listItem = $scope.authorityList[i];
            		if(listItem.list&&listItem.list.length>0){
            			if(-1!=$scope.itemArr2.indexOf(listItem.list,item,'authorityTypeId')){
            				parentNode = listItem;
            				break;
            			};
            		}
            	} 
            	if(parentNode){
            		var containedItem = false;
            		for(var i in $scope.itemArr2.choosedItemArr){
            			if(-1!=$scope.itemArr2.indexOf(parentNode.list,$scope.itemArr2.choosedItemArr[i],'authorityTypeId')){
            				containedItem = true;
            				break;
            			}
            		}
            		if(!containedItem){
            			parentNode.selected = undefined;
            		}
            	}
            } 
            else
            {
                $scope.itemArr2.choosedItemArr.push(item);
                $scope.buildRoleAuthority();
                //勾选父节点or取消勾选父节点
            	var parentNode = null;
            	for(var i in $scope.authorityList){
            		var listItem = $scope.authorityList[i];
            		if(listItem.list&&listItem.list.length>0){
            			if(-1!=$scope.itemArr2.indexOf(listItem.list,item,'authorityTypeId')){
            				parentNode = listItem;
            				break;
            			};
            		}
            	} 
            	if(!parentNode.selected){
            		parentNode.selected = 'selected';
            	}
            }
       },
       chooseParentItem : function(parentItem){
    	   if(parentItem.selected){
    		   if(parentItem.list&&parentItem.list.length>0){
    			   for(var j in parentItem.list){
    				   for(var i in $scope.itemArr2.choosedItemArr){
        				   if($scope.itemArr2.choosedItemArr[i].authorityTypeId==parentItem.list[j].authorityTypeId)
        				   {
    	               			$scope.itemArr2.choosedItemArr.splice(i, 1);
    	               			parentItem.list[j].selected = undefined;
    	               			$scope.buildRoleAuthority();
    	               			break;
        				   }
                       }
    				   /*$scope.itemArr2.chooseItem(parentItem.list[j]);*/
    			   }
    		   }
    		   parentItem.selected = undefined;
    	   }else{
    		   if(parentItem.list&&parentItem.list.length>0){
    			   for(var j in parentItem.list){
    				   var isContained = false;
    				   for(var i in $scope.itemArr2.choosedItemArr){
        				   if($scope.itemArr2.choosedItemArr[i].authorityTypeId==parentItem.list[j].authorityTypeId)
        				   {
        					   isContained = true;
        					   break;
        				   } 
                       }
    				   if(!isContained){
    					   $scope.itemArr2.choosedItemArr.push(parentItem.list[j]);
    					   parentItem.list[j].selected = 'selected';
    				   }
    			   }
    			   $scope.buildRoleAuthority();
			   }
    		   parentItem.selected = 'selected';
    	   }
       }
	};
	$scope.doReturn = function(){
    	$scope.go('/system/roleList','roleList',null);
    };
}])
// 编辑角色控制器
.controller('EditRoleController',['$scope', '$http', '$state', '$q', '$stateParams', '$timeout', 'dialogService', function($scope, $http, $state, $q, $stateParams, $timeout, dialogService) {
	$scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fb', cmn: '角色管理', gcmn :'编辑角色'};
	$scope.role ={};
	/**
	$scope.deferred = $q.defer();
	$scope.deferred.resolve(response);
	$scope.deferred.reject(response); 
	*/
	$scope.queryDeptRoleScope = function(){
		//查询所有角色范围树  部门级  $scope.role.roleType = "2"
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/orggroups/selectByScope").success(function(response) {	
			$scope.$emit('isLoading', false);
			$scope.dataList = response;
			$scope.itemArr.buildOldChoosedItemArr();
			$scope.itemArr.initCurrChoosedItemArrStatus();
			$scope.buildRoleScope();
		}).error(function(){
			$scope.$emit('isLoading', false);
			dialogService.setContent("查询部门级权限范围数据响应异常,请重新登陆后重试！").setShowDialog(true);
		});
	}
	$scope.queryCompanyRoleScope = function(){
		//查询所有角色范围树  公司级  $scope.role.roleType = "1"
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/orggroups/selectByScope2").success(function(response) {
			$scope.$emit('isLoading', false);
			$scope.dataList = response;
			$scope.itemArr.buildOldChoosedItemArr();
			$scope.itemArr.initCurrChoosedItemArrStatus();
			$scope.buildRoleScope();
		}).error(function(){
			$scope.$emit('isLoading', false);
			dialogService.setContent("查询公司级权限范围数据响应异常,请重新登陆后重试！").setShowDialog(true);
		});
	}
	$scope.defer1 = $q.defer();
	$scope.defer2 = $q.defer();
	$scope.defer3 = $q.defer();
	$scope.defer4 = $q.defer();
	
	if ($stateParams.id) {
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/role/selectByPrimaryKey?role_id="+ $stateParams.id).success(function(response) {
			$scope.defer1.resolve(response);
		}).error(function(response){
			$scope.defer1.reject(response); 
		});
	}
	$scope.defer1.promise.then(function(data) {  // 成功回调
		$scope.$emit('isLoading', false);
		$scope.role = data; 
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/role/selectAuthoritiesByRoilId?role_id="+ $stateParams.id)
		.success(function(data) {
			$scope.defer2.resolve(data); 
		}).error(function(data){
			$scope.defer2.reject(data); 
        })
        $scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/role/selectScopeByRoilId?role_id="+ $stateParams.id).success(function(data) {
        	$scope.defer4.resolve(data); 
		}).error(function(data){
			$scope.defer4.reject(data);	 
        })
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("角色数据查询异常！").setShowDialog(true);
    }); 
	
	$scope.defer2.promise.then(function(data) {  // 成功回调
		$scope.$emit('isLoading', false);
		$scope.role.roleAuthority = data;
		for(var i in $scope.role.roleAuthority){
			$scope.itemArr2.oldChooseItemArr.push($scope.role.roleAuthority[i].authorityTypeId);
		}
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/authoritiesRest/selectByRole").success(function(data) {
			$scope.defer3.resolve(data); 
		}).error(function(data){
			$scope.defer3.reject(data); 
		});
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("查询角色权限项数据响应异常！").setShowDialog(true);
    });
	
	$scope.defer3.promise.then(function(data) {  // 成功回调
		$scope.$emit('isLoading', false);
		$scope.authorityList = data;		
		$scope.itemArr2.buildOldChoosedItemArr();
		$scope.itemArr2.initCurrChoosedItemArrStatus();
		$scope.buildRoleAuthority();
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("查询权限项数据响应异常！").setShowDialog(true);
    });
	
	$scope.defer4.promise.then(function(data) {  // 成功回调
		$scope.$emit('isLoading', false);
		$scope.role.roleScope = data;
		$scope.itemArr.oldChooseItemArr = $scope.role.roleScope.roleScope.split(",");									
		if($scope.role.roleType==1){
			$scope.queryCompanyRoleScope();
		}else if($scope.role.roleType==2){
			$scope.queryDeptRoleScope();
		}else{
			dialogService.setContent("角色类型数据异常").setShowDialog(true);
		} 
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("查询管理员数据响应错误！").setShowDialog(true);
    });
	
	/*$q.all([$scope.defer2.promise,$scope.defer4.promise]).then(function(){
		console.log("====","????");
		$scope.$emit('isLoading', false);
	})*/
	
	/*if ($stateParams.id) {
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/role/selectByPrimaryKey?role_id="+ $stateParams.id).success(function(data) {
			$scope.role = data;
			$http.get("/enterpriseuniversity/services/role/selectAuthoritiesByRoilId?role_id="+ $stateParams.id).success(function(data) {
				$scope.role.roleAuthority = data;
				for(var i in $scope.role.roleAuthority){
					$scope.itemArr2.oldChooseItemArr.push($scope.role.roleAuthority[i].authorityTypeId);
				}
				$http.get("/enterpriseuniversity/services/authoritiesRest/selectByRole").success(function(response) {
					$scope.authorityList = response;		
					$scope.itemArr2.buildOldChoosedItemArr();
					$scope.itemArr2.initCurrChoosedItemArrStatus();
					$scope.buildRoleAuthority();
				}).error(function(){
					dialogService.setContent("查询权限项数据响应异常！").setShowDialog(true);
				});	
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("查询角色权限项数据响应异常！").setShowDialog(true);
            });
				
			$http.get("/enterpriseuniversity/services/role/selectScopeByRoilId?role_id="+ $stateParams.id).success(function(data) {
				$scope.role.roleScope = data;
				$scope.itemArr.oldChooseItemArr = $scope.role.roleScope.roleScope.split(",");									
				if($scope.role.roleType==1){
					$scope.queryCompanyRoleScope();
				}else if($scope.role.roleType==2){
					$scope.queryDeptRoleScope();
				}else{
					dialogService.setContent("角色类型数据异常").setShowDialog(true);
				} 
			}).error(function(){
				dialogService.setContent("查询管理员数据响应错误").setShowDialog(true);
            })
		}).error(function(){
			$scope.$emit('isLoading', false);
			dialogService.setContent("角色数据查询异常！").setShowDialog(true);
		})				
	}*/
	//权限范围容器
	$scope.itemArr = {
		oldChooseItemArr:[],
        choosedItemArr: [],
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.selected) 
            {
            	for(var i in $scope.itemArr.choosedItemArr)
            	{
                	if($scope.itemArr.choosedItemArr[i].code==item.code)
                	{
                		$scope.itemArr.choosedItemArr.splice(i, 1);
                		$scope.buildRoleScope();
                	}
                }
            } 
            else
            {
                $scope.itemArr.choosedItemArr.push(item);
                $scope.buildRoleScope();
            }
        },
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.choosedItemArr) 
            {
            	$scope.itemArr.nodeLoopStatus($scope.dataList,$scope.itemArr.choosedItemArr[i].code,'P',[]);
            }
            $scope.$emit('isLoading', false);
        },
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.dataList) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var code = $scope.itemArr.oldChooseItemArr[i]; 
	            	$scope.itemArr.nodeLoop($scope.dataList,code); 
	            }
            }
        	$scope.$emit('isLoading', false);
        },
        nodeLoop:function(LoopNode,code){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].statuss!="1")
        		{//非集团节点
            		if(LoopNode[i].code==code)
            		{
            			if($scope.itemArr.choosedItemArr.length<1)
        				{
        					$scope.itemArr.choosedItemArr.push(LoopNode[i]);
        					continue;
        				}
        				var isContained = false;
        				for(var j in $scope.itemArr.choosedItemArr)
        				{
        					if($scope.itemArr.choosedItemArr[j].code==LoopNode[i].code)
        					{
        						isContained = true;
        						break;
            				} 
        				}
        				if(!isContained)
        				{
        					$scope.itemArr.choosedItemArr.push(LoopNode[i]);
        				}
            		}
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoop(LoopNode[i].children,code);
            	}
        	}
    	},
        nodeLoopStatus:function(LoopNode,code,loopType,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if($scope.role.roleType=="1"){//公司级
        			if(LoopNode[i].statuss!="1"&&LoopNode[i].statuss!="3")
            		{//非集团&非部门
	            		if(LoopNode[i].code==code)
	            		{
	            			LoopNode[i].selected = "selected"; 
	            			if(LoopNode[i].fathercode){//祖先节点fathercode为null
	            				parentNodeArr.push(LoopNode[i].fathercode)
	            			}
	            			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.dataList,LoopNode[i].fathercode,parentNodeArr);
	            			break;
	            		}
            		}
        		}
        		else
        		{//部门级
        			if(LoopNode[i].statuss!="2"&&LoopNode[i].statuss!="1")
            		{
	            		if(LoopNode[i].code==code)
	            		{
	            			LoopNode[i].selected = "selected"; 
	            			parentNodeArr.push(LoopNode[i].fathercode);
	            			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.dataList,LoopNode[i].fathercode,parentNodeArr);
	            			break;
	            		}
            		}
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopStatus(LoopNode[i].children,code,'C',parentNodeArr);
            	}
        	}
        	if(loopType=='P'){ 
        		for(var j in parentNodeArr){ 
        			if(parentNodeArr[j]!='0'){ 
        				$scope.itemArr.nodeLoopForCollapsed(LoopNode,parentNodeArr[j]);  
        			} 
        		} 
        	}
        } ,
        //初始化化树的折叠状态 //>
        nodeLoopForCollapsed:function(LoopNode,fatherCode){ 
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==fatherCode)
        		{
        			LoopNode[i].collapsed = true;
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopForCollapsed(LoopNode[i].children,fatherCode);
            	}
        	}
        },
        //获取selected节点的所有父节点  //输出：parentNodeArr
        nodeLoopForParentNodeFatherCode :function(LoopNode,fathercode,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==fathercode)
        		{
        			parentNodeArr.push(LoopNode[i].fathercode); 
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.dataList,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopForParentNodeFatherCode(LoopNode[i].children,fathercode,parentNodeArr);//>C 子节点 LoopNode[i].children
            	}
        	}
        }
    };

	$scope.switchData = function(){
		$scope.role.roleScope = "";
		$scope.itemArr.oldChooseItemArr = [];
		$scope.itemArr.choosedItemArr = [];
		if($scope.role.roleType==1){
			$scope.queryCompanyRoleScope();
		}else
		{
			$scope.queryDeptRoleScope();
		}
	}
	//权限项数据容器
	$scope.itemArr2 = {
		oldChooseItemArr:[],
        choosedItemArr: [],
        indexOf:function(itemArry,item,mapKey){
			if(!itemArry){
				return -1;
			}
			for(var i in itemArry){
				if(itemArry[i][mapKey]==item[mapKey]){
					return i;
				}
			}
			return -1;
		},
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.selected) 
            {
            	for(var i in $scope.itemArr2.choosedItemArr)
            	{
                	if($scope.itemArr2.choosedItemArr[i].authorityTypeId==item.authorityTypeId)
                	{
                		$scope.itemArr2.choosedItemArr.splice(i, 1);
                		$scope.buildRoleAuthority();
                		break;
                	}
                }
            	//取消勾选父节点
            	var parentNode = null;
            	for(var i in $scope.authorityList){
            		var listItem = $scope.authorityList[i];
            		if(listItem.list&&listItem.list.length>0){
            			if(-1!=$scope.itemArr2.indexOf(listItem.list,item,'authorityTypeId')){
            				parentNode = listItem;
            				break;
            			};
            		}
            	} 
            	if(parentNode){
            		var containedItem = false;
            		for(var i in $scope.itemArr2.choosedItemArr){
            			if(-1!=$scope.itemArr2.indexOf(parentNode.list,$scope.itemArr2.choosedItemArr[i],'authorityTypeId')){
            				containedItem = true;
            				break;
            			}
            		}
            		if(!containedItem){
            			parentNode.selected = undefined;
            		}
            	}
            } 
            else
            {
                $scope.itemArr2.choosedItemArr.push(item);
                $scope.buildRoleAuthority();
                //勾选父节点or取消勾选父节点
            	var parentNode = null;
            	for(var i in $scope.authorityList){
            		var listItem = $scope.authorityList[i];
            		if(listItem.list&&listItem.list.length>0){
            			if(-1!=$scope.itemArr2.indexOf(listItem.list,item,'authorityTypeId')){
            				parentNode = listItem;
            				break;
            			};
            		}
            	} 
            	if(!parentNode.selected){
            		parentNode.selected = 'selected';
            	}
            }
        },
        chooseParentItem : function(parentItem){
     	   if(parentItem.selected){
     		   if(parentItem.list&&parentItem.list.length>0){
     			   for(var j in parentItem.list){
     				   for(var i in $scope.itemArr2.choosedItemArr){
         				   if($scope.itemArr2.choosedItemArr[i].authorityTypeId==parentItem.list[j].authorityTypeId)
         				   {
     	               			$scope.itemArr2.choosedItemArr.splice(i, 1);
     	               			parentItem.list[j].selected = undefined;
     	               			$scope.buildRoleAuthority();
     	               			break;
         				   }
                        }
     				   /*$scope.itemArr2.chooseItem(parentItem.list[j]);*/
     			   }
     		   }
     		   parentItem.selected = undefined;
     	   }else{
     		   if(parentItem.list&&parentItem.list.length>0){
     			   for(var j in parentItem.list){
     				   var isContained = false;
     				   for(var i in $scope.itemArr2.choosedItemArr){
         				   if($scope.itemArr2.choosedItemArr[i].authorityTypeId==parentItem.list[j].authorityTypeId)
         				   {
         					   isContained = true;
         					   break;
         				   } 
                        }
     				   if(!isContained){
     					   $scope.itemArr2.choosedItemArr.push(parentItem.list[j]);
     					   parentItem.list[j].selected = 'selected';
     				   }
     			   }
     			   $scope.buildRoleAuthority();
 			   }
     		   parentItem.selected = 'selected';
     	   }
        },
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr2.choosedItemArr) 
            {
            	$scope.itemArr2.nodeLoopStatus($scope.authorityList,$scope.itemArr2.choosedItemArr[i].authorityTypeId,'P',[]);
            }
            $scope.$emit('isLoading', false);
        },
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.authorityList) 
        	{
	            for(var i in $scope.itemArr2.oldChooseItemArr)
	            {
	            	var authorityTypeId = $scope.itemArr2.oldChooseItemArr[i]; 
	            	$scope.itemArr2.nodeLoop($scope.authorityList,authorityTypeId); 
	            }
            }
        	$scope.$emit('isLoading', false);
        },
        nodeLoop:function(LoopNode,authorityTypeId){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].authorityTypeId==authorityTypeId)
        		{
        			if($scope.itemArr2.choosedItemArr.length<1)
    				{
    					$scope.itemArr2.choosedItemArr.push(LoopNode[i]);
    					continue;
    				}
    				var isContained = false;
    				for(var j in $scope.itemArr2.choosedItemArr)
    				{
    					if($scope.itemArr2.choosedItemArr[j].authorityTypeId==LoopNode[i].authorityTypeId)
    					{
    						isContained = true;
    						break;
        				} 
    				}
    				if(!isContained)
    				{
    					$scope.itemArr2.choosedItemArr.push(LoopNode[i]);
    				}
        		}
        		if(LoopNode[i].list){
            		$scope.itemArr2.nodeLoop(LoopNode[i].list,authorityTypeId);
            	}
        	}
        } ,
        nodeLoopStatus:function(LoopNode,authorityTypeId,loopType,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].authorityTypeId==authorityTypeId)
        		{
        			LoopNode[i].selected = "selected"; 
        			parentNodeArr.push(LoopNode[i].authorityTypeName);
        			break;
        		}
        		if(LoopNode[i].list){
            		$scope.itemArr2.nodeLoopStatus(LoopNode[i].list,authorityTypeId,'C',parentNodeArr);
            	}
        		if(loopType=='P'){
        			var containedItems = false;
        			for(var j in $scope.itemArr2.choosedItemArr){
        				if(-1!=$scope.itemArr2.indexOf(LoopNode[i].list,$scope.itemArr2.choosedItemArr[j],'authorityTypeId')){
        					containedItems = true; 
            				break;
            			}
        			}
        			if(containedItems&&!LoopNode[i].selected){
        				LoopNode[i].selected = 'selected';
        			}
        		}
        	}
        	if(loopType=='P'){ 
        		for(var j in parentNodeArr){ 
        			if(parentNodeArr[j]!=""&&parentNodeArr[j]!=null){ 
        				$scope.itemArr2.nodeLoopForCollapsed(LoopNode,parentNodeArr[j]);  
        			} 
        		} 
        	}
        } ,
        //初始化化树的折叠状态 //>
        nodeLoopForCollapsed:function(LoopNode,authorityTypeName){ 
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].typeFunction==authorityTypeName)
        		{
        			LoopNode[i].collapsed = true;
        			break;
        		}
        	}
        }
    };
	$scope.buildRoleScope = function(){
		var roleScopeId = [];
		for(var i in $scope.itemArr.choosedItemArr){
			roleScopeId.push($scope.itemArr.choosedItemArr[i].code);
		}
		$scope.role.roleScope = roleScopeId.join(",");
	}
	$scope.buildRoleAuthority = function(){
		var roleAuthorityId = [];
		for(var i in $scope.itemArr2.choosedItemArr){
			roleAuthorityId.push($scope.itemArr2.choosedItemArr[i].authorityTypeId);
		}
		$scope.role.roleAuthority  = roleAuthorityId.join(",");
	}
	$scope.doSave = function() {
		$scope.editRoleForm.$submitted = true;
		if($scope.editRoleForm.$invalid)
   	 	{
			// dialogService.setContent("表单填写不完整").setShowDialog(true);
			 return;
   	 	}
		$scope.$emit('isLoading', true);
		$http({
			method : 'PUT',
			url : '/enterpriseuniversity/services/role/update',
			data : $.param($scope.role),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
			$scope.$emit('isLoading', false);
			dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   			$scope.go('/system/roleList', 'roleList', null);
	   		},2000);	
		}).error(function() {
			$scope.$emit('isLoading', false);
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   		},2000);
		});
	}
	$scope.doReturn = function(){
    	$scope.go('/system/roleList','roleList',null);
    };
}]);
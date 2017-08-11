angular.module('ele.commodity', ['ele.admin'])
.controller('CommodityController', ['$scope', '$http','dialogService','$timeout',function ($scope, $http,dialogService,$timeout) {
	//用于显示面包屑状态
    $scope.$parent.cm = {pmc:'pmc-d', pmn: '积分商城 ', cmc:'cmc-da', cmn: '商品列表'};
    $scope.url = "/enterpriseuniversity/services/commodity/findList?pageNum=";
    $scope.paginationConf = {
	        currentPage:1,
	        totalItems: 10,
	        itemsPerPage:20,
	        pagesLength: 13,
	        perPageOptions:[20,50,100,'全部'],
	        rememberPerPage: 'perPageItems',
	        onChange: function(){
	        	$scope.$httpUrl = "";
	        	$scope.$httpPrams = "";
	        	$scope.$emit('isLoading', true);
                if ($scope.commodityName!=undefined && $scope.commodityName!= "") {
                	$scope.$httpPrams = $scope.$httpPrams +"&commodityName="
                		+$scope.commodityName.replace(/\%/g,"%25").replace(/\#/g,"%23")
                			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
                			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
                }
                $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
                $http.get($scope.$httpUrl).success(function (response) {
                	$scope.$emit('isLoading', false);
                    $scope.page = response;
                    $scope.paginationConf.totalItems = response.count;
                    $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                });
		    }
	};
	$scope.search = function() {
		$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
		$scope.paginationConf.onChange();
	};
	//回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
	$scope.updateStatus=function(commodity){
		alertMsg(commodity.commodityGrounding == "1"?"确定下架商品?":"确定上架商品?",1);
		sure = function(){
			$http.get("/enterpriseuniversity/services/commodity/updateStatus?commodityId="+commodity.commodityId).success(function (response) {
				if(response.result=="success"){
					if(commodity.commodityGrounding == "1"){
		        		alertMsg("上架成功",0);
		        	}else{
		        		alertMsg("下架成功",0);
		        	}
					sure = function(){$scope.search();}
				}else{
					if(commodity.commodityGrounding == "1"){
		        		alertMsg("访问异常,上架失败,请重新登录后再试",0);
		        	}else{
		        		alertMsg("访问异常,下架失败,请重新登录后再试",0);
		        	}
		        	sure = function(){}
				}
				
	        }).error(function(){
	        	if(commodity.commodityGrounding == "1"){
	        		alertMsg("访问异常,上架失败,请重新登录后再试",0);
	        	}else{
	        		alertMsg("访问异常,下架失败,请重新登录后再试",0);
	        	}
	        	sure = function(){}
	        });
		}
	}
	
	// 添加题商品按钮
	$scope.doAdd = function() {
		$scope.go('/EBStore/addCommodity', 'addCommodity',{});
	};
	
	// 编辑题商品按钮
	$scope.edit = function(commodity) {
		$scope.go('/EBStore/editCommodity', 'editCommodity',{id:commodity.commodityId});
	}
	
	 //上/下架按钮
    $scope.doUpdate = function (commodity) {
    	dialogService.setContent(commodity.commodityGrounding == "1"?"确定下架商品?":"确定上架商品?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
			 $http({
			        url: "/enterpriseuniversity/services/commodity/updateStatus?commodityId=" + commodity.commodityId + "&commodityGrounding=" + (commodity.commodityGrounding == "1" ? "2" : "1"),
			        method: "GET"
			    }).success(function (response) {
			    	//延迟弹框
			    	$timeout(function(){
			    		if(response.result=="error"){
//			    			dialogService.setContent("商品下架失败！").setShowDialog(true);
			    			dialogService.setContent(commodity.commodityGrounding == "1"?"商品下架失败":"商品上架失败").setShowSureButton(false).setShowDialog(true);
				    		$timeout(function(){
								dialogService.sureButten_click(); 
							},2000);
			    		}else{
//			    			dialogService.setContent(commodity.commodityGrounding == "1"?"下架成功":"上架成功").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
//			    				$scope.paginationConf.onChange();
//		    				}
			    			dialogService.setContent(commodity.commodityGrounding == "1"?"下架成功":"上架成功").setShowSureButton(false).setShowDialog(true);
				    		$timeout(function(){
								dialogService.sureButten_click(); 
								$scope.paginationConf.onChange();
							},2000);
			    		}
			    	},200);
			    }).error(function(){
			    	$timeout(function(){
			    		dialogService.setContent("网络异常,请重新登录后再试").setShowDialog(true);
			    	},200);
			    });
    	};
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(commodity){
        $scope.currHighLightRow.commodityId = commodity.commodityId; 
    } 
}])
   
.controller('AddCommodityController', ['$scope', '$http', '$timeout', 'dialogService', function ( $scope, $http, $timeout, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-d', pmn: '积分商城 ', cmc:'cmc-da', cmn: '商品列表', gcmn: '添加商品'};
	$scope.commodity = $scope.formData = {
		arayacak:'N',
		post:'N'
	};
	$scope.supportWays = [];
	$scope.toggleArayacak = function(){
		if($scope.commodity.arayacak=="N"){
			$scope.commodity.arayacak="Y";
			$scope.supportWays.push(1);
		}else{
			$scope.commodity.arayacak="N";
			$scope.supportWays.splice(0,1);
		}
	}
   $scope.togglePost = function(){
    	if($scope.commodity.post=="N"){
			$scope.commodity.post="Y";
			$scope.supportWays.push(1);
		}else{
			$scope.commodity.post="N";
			$scope.supportWays.splice(0,1);
		}
    }
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/EBStore/commodityList','commodityList',null);
    };
    
 // 提交表单
	$scope.addCommodity=false;
	$scope.doSave = function() {
		$scope.commodity.commodityGrounding='2';
		$scope.commodityForm.$submitted = true;
    	if($scope.commodityForm.$invalid)
	   	{
    		$timeout(function(){
				dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true)
			},200);
	   		return;
	   	} 
    	$scope.addCommodity=true;
		$http({
			method : 'POST',
			url : '/enterpriseuniversity/services/commodity/add',
			data : $.param($scope.commodity),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
			if(data.result=="error"){
//				$timeout(function(){
//		    		dialogService.setContent("添加商品失败！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/EBStore/commodityList','commodityList', null);}
//		    	},200);
				dialogService.setContent("添加商品失败！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addCommodity=false;
				},2000);
			}else if(data.result=="success"){
//				$timeout(function(){
//		    		dialogService.setContent("添加商品成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/EBStore/commodityList','commodityList', null);}
//		    	},200);
				dialogService.setContent("添加商品成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addCommodity=false;
					$scope.go('/EBStore/commodityList','commodityList', null);
				},2000);
			}
			$scope.addCommodity=false;
		}).error(function() {
//			dialogService.setContent("添加商品失败！").setShowDialog(true)
			dialogService.setContent("添加商品失败！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addCommodity=false;
			},2000);
			$scope.addCommodity=false;
		});
	};
	
	// 提交表单
	$scope.addCommodity=false;
	$scope.saveRelease = function() {
		$scope.commodity.commodityGrounding='1';
		$scope.commodityForm.$submitted = true;
    	if($scope.commodityForm.$invalid)
	   	{
    		$timeout(function(){
				dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true)
			},200);
	   		return;
	   	} 
    	$scope.addCommodity=true;
		$http({
			method : 'POST',
			url : '/enterpriseuniversity/services/commodity/add',
			data : $.param($scope.commodity),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
			if(data.result=="error"){
//				$timeout(function(){
//		    		dialogService.setContent("添加商品失败！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/EBStore/commodityList','commodityList', null);}
//		    	},200);
				dialogService.setContent("添加商品失败！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addCommodity=false;
				},2000);
			}else if(data.result=="success"){
//				$timeout(function(){
//		    		dialogService.setContent("添加商品成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/EBStore/commodityList','commodityList', null);}
//		    	},200);
				dialogService.setContent("添加商品成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addCommodity=false;
					$scope.go('/EBStore/commodityList','commodityList', null);
				},2000);
			}
			$scope.addCommodity=false;
		}).error(function() {
//			dialogService.setContent("添加商品失败！").setShowDialog(true)
			dialogService.setContent("添加商品失败！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addCommodity=false;
			},2000);
			$scope.addCommodity=false;
		});
	};
}])
//编辑商品
.controller('EditCommodityController',['$scope', '$http', '$state', '$stateParams','$q','dialogService','$timeout', function($scope, $http, $state, $stateParams, $q,dialogService,$timeout) {
	$scope.$parent.cm = {pmc:'pmc-d', pmn: '积分商城 ', cmc:'cmc-da', cmn: '商品列表', gcmn: '编辑商品'};
	$scope.supportWays = [];
	$scope.toggleArayacak = function(){
		if($scope.commodity.arayacak=="N"){
			$scope.commodity.arayacak="Y";
			$scope.supportWays.push(1);
		}else{
			$scope.commodity.arayacak="N";
			$scope.supportWays.splice(0,1);
		}
	}
	$scope.togglePost = function(){
    	if($scope.commodity.post=="N"){
			$scope.commodity.post="Y";
			$scope.supportWays.push(1);
		}else{
			$scope.commodity.post="N";
			$scope.supportWays.splice(0,1);
		}
    }
	$scope.deferred = $q.defer();
	if ($stateParams.id) {
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/commodity/findOne?commodityId="+$stateParams.id)
			.success(function(data) {
				$scope.deferred.resolve(data);
			}).error(function(data){
	    		$scope.deferred.reject(data); 
	    	});
	} else {
		// 未传参数
	}
	$scope.deferred.promise.then(function(data) { // 成功回调
		$scope.$emit('isLoading', false);
		$scope.commodity = $scope.formData = data;
		if($scope.commodity.arayacak=="Y"){
			$scope.supportWays.push(1);
		}
		if($scope.commodity.post=="Y"){
			$scope.supportWays.push(1);
		}
	}, function(data) { // 错误回调
		$scope.$emit('isLoading', false);
		dialogService.setContent("商品数据查询异常！").setShowDialog(true)
	});
	
	// 编辑商品返回
	$scope.doReturn = function() {
		$scope.go('/EBStore/commodityList', 'commodityList',{});
	}
	
	//发布
	$scope.toggleCommodityGrounding = function() {
		if($scope.commodity.commodityGrounding=="1"){
			$scope.commodity.commodityGrounding="2";
		}else{
			$scope.commodity.commodityGrounding="1";
		}
	}
	
	// 编辑题库保存
	$scope.editCommoditySubmit=false;
	$scope.doSave = function() {
		$scope.editCommodityForm.$submitted = true;
    	if($scope.editCommodityForm.$invalid)
	   	{
	    	dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true);
	   		return;
	   	}
    	$scope.editCommoditySubmit=true;
		$http({
			url : "/enterpriseuniversity/services/commodity/edit",
			method : "POST",
			data : $.param($scope.commodity),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		})
		.success(function(data) {
			if(data.result=="error"){
//				$timeout(function(){
//		    		dialogService.setContent("修改商品失败！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/EBStore/commodityList', 'commodityList',{});}
//		    	},200);
				dialogService.setContent("修改商品失败！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.editCommoditySubmit=false;
				},2000);
			}else if(data.result=="success"){
//				$timeout(function(){
//		    		dialogService.setContent("修改商品成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/EBStore/commodityList', 'commodityList',{});}
//		    	},200);
				dialogService.setContent("修改商品成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.editCommoditySubmit=false;
					$scope.go('/EBStore/commodityList', 'commodityList',{});
				},2000);
			}
			$scope.editCommoditySubmit=false;
		})
		.error(function() {
//			$timeout(function(){
//	    		dialogService.setContent("修改商品失败！").setShowDialog(true)
//	    	},200);
			dialogService.setContent("修改商品失败！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.editCommoditySubmit=false;
			},2000);
			$scope.editCommoditySubmit=false;
		});
	}
}])
.directive('requiredSupportways', [function () {
	return {
		require: "ngModel",
		link: function (scope, element, attr, ngModel) {
			var customValidator = function (value) {
				var validity = null;
				scope.$watch(function () {
					return value.length;
				}, function(){
					validity = value!=undefined ? value.length <1 : true;
					ngModel.$setValidity("requiredSupportways", !validity);
				});
				return !validity ? value : [];
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])
//上传
.controller('CommodityImgController', ['$scope', 'FileUploader', '$timeout', 'dialogService', function ($scope, FileUploader, $timeout, dialogService) {
    var imgUploader = $scope.imgUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/commodityImg',
            autoUpload:true
        }
    );
    imgUploader.filters.push({
        name: 'customFilter',
        fn: function(item , options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0)
            {
            	dialogService.setContent("要上传新的商品图片，请先删除旧的商品图片").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("只允许上传最多一张商品图片").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|'.indexOf(type) == -1){
                dialogService.setContent("请上传符合要求的图片文件").setShowDialog(true);
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
            if( item.size/1024 > 2048)
            {
            	dialogService.setContent("上传文件大小不符合要求(大于2M)").setShowDialog(true);
            	return item.size/1024 <= 2048;
            }
            return true;
        }
    });
    //编辑按钮带过来的章节文件地址集合
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currFileUrlQueue =[]; 
    		if($scope.formData.commodityPicture&&$scope.formData.commodityPicture!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.commodityPicture);
    		}
        }, function(data) {
        	 
        }); 
    } 
    //预览
    $scope.openPreview = false;
    $scope.preview = function(){
    	if($scope.$parent.formData.commodityPicture&&$scope.$parent.formData.commodityPicture!="")
    	{
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.formData.commodityPicture;
    	}
    	else
    	{
    		dialogService.setContent("请先上传再预览").setShowDialog(true);
    	}
    }
    $scope.closePreview = function(){
    	$scope.openPreview = false;
    }
    $scope.commodityPicture = function(imgUrl){
		$scope.$parent.formData.commodityPicture = imgUrl;
    }
    $scope.removeCurrItem = function (item) {
        $scope.currFileUrlQueue.splice($scope.currFileUrlQueue.indexOf(item), 1);
        $scope.commodityPicture("");
    }
    $scope.removeItem = function (item) {
        item.remove();
        document.getElementById("commodityImgFileInput").value=[];
        $scope.commodityPicture(""); 
    }
    $scope.uploadItem = function(item){
    	if(!(item.isReady || item.isUploading || item.isSuccess)){
    		 item.upload();
    	}
    }
    // CALLBACKS
    imgUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("文件解析异常,获取上传文件地址失败").setShowDialog(true);
        }
        else
        {
        	$scope.commodityPicture(response.url);//接收返回的文件地址
        }
    };
}])
//商品兑换列表控制器
.controller('CommodityExchangeController', ['$scope', '$http','dialogService','$timeout',function( $scope, $http,dialogService,$timeout) {
	$scope.$parent.cm = {pmc:'pmc-d', pmn: '积分商城 ', cmc:'cmc-db', cmn: '商品兑换'};
	$scope.showCommodityExchangeList = true;
	$scope.commodityExchange = function() {
		$scope.currHighLightRow = {};
		$scope.showCommodityExchangeList = true;
		$scope.showExchangeHistoryList = false;
		$scope.search();
	}
	$scope.exchangeHistory = function() {
		$scope.currHighLightRow = {};
		$scope.showExchangeHistoryList = true;
		$scope.showCommodityExchangeList = false; 
		$scope.search();
	}
	 $scope.state = [
	      {name: "兑换", value: "Y"},
	      {name: "未兑换", value: "N"}
	 ];
	 $scope.method = [
   	      {name: "邮寄", value: "1"},
   	      {name: "自提", value: "2"}
	 ];
	// 显示活动统计列表
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 13,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$emit('isLoading', true);
				if($scope.showCommodityExchangeList){
					$scope.url = "/enterpriseuniversity/services/commodityExchange/commodityExchangeList?exchangeState=N&pageNum=";
				}else{
					$scope.url = "/enterpriseuniversity/services/commodityExchange/commodityExchangeList?exchangeState=Y&pageNum=";
				}
				if ($scope.empCode != undefined && $scope.empCode != "") {
					$scope.$httpPrams = $scope.$httpPrams + "&empCode="
					+ $scope.empCode.replace(/\%/g,"%25").replace(/\#/g,"%23")
					.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
					.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
				}
				 if ($scope.postMethod != undefined) 
		            {
		            	$scope.$httpPrams = $scope.$httpPrams + "&postMethod=" + $scope.postMethod
		            }
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
				+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)
				+ $scope.$httpPrams;
				$http.get($scope.$httpUrl).success(
						function(response) {
							$scope.$emit('isLoading', false);
							$scope.page = response;
							$scope.paginationConf.totalItems = response.count;
							$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
						});
			}
	};
	//搜索按钮函数
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
	//查询下拉菜单改变后查询列表数据
    $scope.changeSeclectOption = function(){
    	$scope.search();
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(rowItem){
        $scope.currHighLightRow.cdkey = rowItem.cdkey; 
    }
}])

.controller('exchangeCommodityExchangeController', ['$scope', '$http','dialogService','$timeout', function( $scope, $http, dialogService,$timeout) {
	$scope.openModalDialog=false;
	
	$scope.search = function () {
		$http.get($scope.url+$scope.$httpPrams).success(
				function(response) {
					$scope.commodityInformation = response;
					$scope.commodityInformation.exchangeTimeS=$scope.exchangeTime;
				})
    };
    
    $scope.$parent.$parent.exchange = function(a){
    	$scope.$httpPrams = "";
    	$scope.commodityId = a.commodityId;
    	$scope.code = a.code;
    	$scope.exchangeState = a.exchangeState;
    	$scope.exchangeTime=a.exchangeTime;
    	$scope.url = "/enterpriseuniversity/services/commodityExchange/selectByExchange?"
    	 if ($scope.commodityId != undefined) 
         {
         	$scope.$httpPrams = $scope.$httpPrams + "commodityId=" + $scope.commodityId;
         }
    	 if ($scope.code != undefined) 
         {
         	$scope.$httpPrams = $scope.$httpPrams + "&code=" + $scope.code;
         }
    	 if ($scope.exchangeTime != undefined) 
         {
         	$scope.$httpPrams = $scope.$httpPrams + "&exchangeTime=" + $scope.exchangeTime;
         }
    	 
    	$scope.search();
    	if(a.postMethod=="1"){
    		$scope.openModalDialog=true;
    	}else{
    		dialogService.setContent('确定要兑换吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
    		.doNextProcess = function(){
    			$http({
    				url : "/enterpriseuniversity/services/commodityExchange/modifyCommodityExchangeState",
    				method : "POST",
    				data : $.param($scope.commodityInformation),
    				headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
    			})
    			.success(function(data) {
    				if(data.result=="error"){
//    					$timeout(function(){
//    			    		dialogService.setContent("兑换失败！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.$parent.$parent.search();}
//    			    	},200);
    					dialogService.setContent("兑换失败！").setShowSureButton(false).setShowDialog(true);
    		    		$timeout(function(){
    						dialogService.sureButten_click(); 
    					},2000);
    				}else if(data.result=="success"){
//    					$timeout(function(){
//    			    		dialogService.setContent("兑换成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.$parent.$parent.search();}
//    			    	},200);
    					dialogService.setContent("兑换成功！").setShowSureButton(false).setShowDialog(true);
    		    		$timeout(function(){
    						dialogService.sureButten_click(); 
    						$scope.$parent.$parent.search();
    					},2000);
    				}
    			})
    		}
    	}
    };
    //模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
	};
	
	$scope.returnCommodityExchange = function (){
		$scope.openModalDialog=false;
	};
	
	//寄出
	$scope.openModalDialog=false;
	$scope.mail = function() {
		$scope.commodityExchangeForm.$submitted = true;
    	if($scope.commodityExchangeForm.$invalid)
	   	{
    		$timeout(function(){
	    		dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true);
	    	},200);
	   		return;
	   	}
    	$scope.openModalDialog=true;
		$http({
			url : "/enterpriseuniversity/services/commodityExchange/modifyCommodityExchangeState",
			method : "POST",
			data : $.param($scope.commodityInformation),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		})
		.success(function(data) {
			if(data.result=="error"){
//				$timeout(function(){
//		    		dialogService.setContent("寄出失败！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.$parent.$parent.search();}
//		    	},200);
				dialogService.setContent("寄出失败！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.openModalDialog=false;
				},2000);
			}else if(data.result=="success"){
//				$timeout(function(){
//		    		dialogService.setContent("寄出成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.$parent.$parent.search();}
//		    	},200);
				dialogService.setContent("寄出成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.openModalDialog=false;
					$scope.$parent.$parent.search();
				},2000);
			}
			$scope.openModalDialog=false;
		})
		.error(function() {
//			$timeout(function(){
//	    		dialogService.setContent("寄出失败！").setShowDialog(true)
//	    	},200);
			dialogService.setContent("寄出失败！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.openModalDialog=false;
			},2000);
			$scope.openModalDialog=false;
		});
	}
}])

angular.module('ele.news', ['ele.admin'])
.controller('NewsController', ['$scope', '$http','dialogService','$timeout',function ($scope, $http,dialogService,$timeout) {
	//用于显示面包屑状态
	$scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-ff', cmn: '资讯管理'};
    $scope.url = "/enterpriseuniversity/services/news/selectByList?pageNum=";
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
        	 if ($scope.newsTitle != undefined && $scope.newsTitle != "") {
                	$scope.$httpPrams = $scope.$httpPrams + "&newsTitle=" 
                		+ $scope.newsTitle.replace(/\%/g,"%25").replace(/\#/g,"%23")
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
	// 添加题商品按钮
	$scope.doAdd = function() {
		$scope.go('/system/addNews', 'addNews',null);
	};
	// 编辑题商品按钮
	$scope.doUpdates = function(r) {
		if(r.isRelease=='N'){
			$scope.go('/system/editNews', 'editNews',{id:r.newsId});
		}else{
			dialogService.setContent("请先撤回资讯再修改！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
			},2000);
		}
	}
	//发布/撤回按钮
    $scope.releaseWithdrawal = function (r) {
    	dialogService.setContent(r.isRelease == "Y"?"确定撤回吗?":"确定发布吗?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
			 $http({
			        url: "/enterpriseuniversity/services/news/updateByReleaser?newsId=" + r.newsId + "&isRelease=" + (r.isRelease == "Y" ? "N" : "Y"),
			        method: "PUT"
			    }).success(function (response) {
			    	//延迟弹框
			    	$timeout(function(){
			    		if(response.result=="error"){
			    			dialogService.setContent(r.isRelease == "Y"?"撤回失败":"发布失败").setShowSureButton(false).setShowDialog(true);
				    		$timeout(function(){
								dialogService.sureButten_click(); 
							},2000);
			    		}else if(response.result=="protected"){
			    			dialogService.setContent("请先撤回关联的轮播图再撤回资讯！").setShowSureButton(false).setShowDialog(true);
				    		$timeout(function(){
								dialogService.sureButten_click(); 
							},2000);
			    		}else{
			    			dialogService.setContent(r.isRelease == "Y"?"撤回成功":"发布成功").setShowSureButton(false).setShowDialog(true);
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
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.newsId = item.newsId; 
    }
}]) 
.controller('AddNewsController', ['$scope', '$http', '$timeout', 'dialogService', function ( $scope, $http, $timeout, dialogService) {
	$scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-ff', cmn: '资讯管理', gcmn: '添加资讯'};
	$scope.news = $scope.formData = {
			isRelease:'N'
    };
	/*
	$scope.editorConfig = {
		     //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
		     toolbars: [
		       ['', 'Source', 'Undo', 'Redo', 'customstyle','paragraph','fontfamily','fontsize', 'Bold', 'test','italic','underline','fontborder','strikethrough','superscript','subscript','formatmatch','autotypeset',
		        'blockquote','pasteplain','forecolor','backcolor','insertorderedlist','insertunorderedlist','selectall','cleardoc','rowspacingtop','rowspacingbottom',
		        'lineheight','indent','justifyleft','justifycenter','justifyright','justifyjustify','horizontal','touppercase','tolowercase',
		        'link','unlink','anchor','imagenone','imageleft','imageright','imagecenter','','insertimage','emotion','date','time','spechars',
		        'inserttable','deletetable','insertparagraphbeforetable','insertrow','deleterow','insertcol','deletecol','mergecells','mergeright','mergedown','splittocells','splittorows',
		        'splittocols','searchreplace','map','gmap']
		     ],
		     //focus时自动清空初始化时的内容
		     autoClearinitialContent: true,
		     //关闭字数统计
		     wordCount: false,
		     //关闭elementPath
		     elementPathEnabled: false
	};
	$scope.news.newsDetails = '';
	*/
	
	$scope.searchScope = function(){
		$http.get("/enterpriseuniversity/services/news/selectAll").success(function (response) {
	        $scope.selectOptions = response;
	    }).error(function () {
			dialogService.setContent("选择范围下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	$scope.searchScope();
	
	 // 提交表单
	$scope.addNews=false;
	$scope.doSave = function() {
		$scope.news.newsDetails=CKEDITOR.instances['newsEditor'].getData();
		//alert($scope.news.newsDetails);
		$scope.newsForm.$submitted = true;
		if($scope.news.newsDetails==""||$scope.news.newsDetails==undefined)
	   	{
    		$timeout(function(){
				dialogService.setContent("未检测到资讯内容！").setShowDialog(true)
			},200);
	   		return;
	   	} 
    	if($scope.newsForm.$invalid)
	   	{
    		$timeout(function(){
				dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true)
			},200);
	   		return;
	   	} 
    	
    	$scope.addNews=true;
//    	$scope.news.newsDetails=CKEDITOR.instances['newsDetails'].getData();
    
		$http({
			method : 'POST',
			url : '/enterpriseuniversity/services/news/insertSelective',
			data : $.param($scope.news),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
			if(data.result=="error"){
				dialogService.setContent("添加资讯失败！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addNews=false;
				},2000);
			}else if(data.result=="success"){
				dialogService.setContent("添加资讯成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addNews=false;
					$scope.go('/system/newsList','newsList', null);
				},2000);
			}
			$scope.addNews=false;
		}).error(function() {
			dialogService.setContent("添加资讯失败！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addNews=false;
			},2000);
			$scope.addNews=false;
		});
	};
	
	//添加资讯返回按钮
	$scope.doReturn = function() {
		$scope.go('/system/newsList', 'newsList',{});
	}
	
   }])
   
   //上传
.controller('NewsImgController', ['$scope', 'FileUploader', '$timeout', 'dialogService', function ($scope, FileUploader, $timeout, dialogService) {
    var imgUploader = $scope.imgUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/newsImg'
        }
    );
    imgUploader.filters.push({
        name: 'customFilter',
        fn: function(item , options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0)
            {
            	dialogService.setContent("要上传新的资讯封面图片，请先删除旧的资讯封面图片").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("只允许上传最多一资讯封面图片").setShowDialog(true);
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
    })
    
    //编辑页面课程封面图片文件地址
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currFileUrlQueue =[]; 
    		if($scope.formData.newsPicture&&$scope.formData.newsPicture!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.newsPicture);
    		}
        }, function(data) {
        	dialogService.setContent("页面数据初始化错误").setShowDialog(true);
            return;
        }); 
    } 
    
    //预览
    $scope.openPreview = false;
    $scope.preview = function(){
    	if($scope.$parent.formData.newsPicture&&$scope.$parent.formData.newsPicture!="")
    	{
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.$parent.formData.newsPicture;
    	}
    	else
    	{
    		dialogService.setContent("请先上传再预览").setShowDialog(true);
    	}
    }
    
    $scope.newsPicture = function(imgUrl){
		$scope.$parent.formData.newsPicture = imgUrl;
    }
    $scope.closePreview = function(){
    	$scope.openPreview = false;
    }
    //删除之前上传的文件路径集合
    $scope.removeCurrItem = function (item) {
        $scope.currFileUrlQueue.splice($scope.currFileUrlQueue.indexOf(item), 1);
        $scope.newsPicture("");
    }
    //删除 
    $scope.removeItem = function (item) {
        item.remove();
        document.getElementById("newsImgFileInput").value=[];
        $scope.newsPicture(""); 
    }
    
 // CALLBACKS
    imgUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("获取上传文件地址失败").setShowDialog(true);
        }
        else
        {
        	$scope.newsPicture(response.url);//接收返回的文件地址
        }
    };
}])

.controller('EditNewsController',['$scope', '$http', '$state', '$stateParams','$q','dialogService','$timeout', function($scope, $http, $state, $stateParams, $q,dialogService,$timeout) {
	$scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-ff', cmn: '资讯管理', gcmn: '编辑资讯'};
	$scope.supportWays = [];
	$scope.deferred = $q.defer();
	if ($stateParams.id) {
		$http.get("/enterpriseuniversity/services/news/selectById?newsId="+$stateParams.id)
			.success(function(data) {
				$scope.deferred.resolve(data);
			});
	} else {
		// 未传参数
	}
	$scope.deferred.promise.then(function(data) { // 成功回调
		$scope.r = $scope.formData = data;
		CKEDITOR.instances['newsEditor'].setData($scope.r.newsDetails);
	}, function(data) { // 错误回调
		console.log('请求失败' + data);
		$timeout(function(){
			dialogService.setContent("查询出错！").setShowDialog(true)
		},200);
		return;
	});
	
	$scope.searchScope = function(){
		$http.get("/enterpriseuniversity/services/news/selectAll").success(function (response) {
	        $scope.selectOptions = response;
	    }).error(function () {
			dialogService.setContent("选择范围下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	$scope.searchScope();
	
	//添加资讯返回按钮
	$scope.doReturn = function() {
		$scope.go('/system/newsList', 'newsList',{});
	}
	
	// 提交表单
	$scope.editNews=false;
	$scope.doEdit = function() {
//		var content=CKEDITOR.instances['test'].getData();
//		alert(content);
		$scope.r.newsDetails=CKEDITOR.instances['newsEditor'].getData();
		$scope.editNewsForm.$submitted = true;
		if($scope.r.newsDetails==""||$scope.r.newsDetails==undefined)
	   	{
    		$timeout(function(){
				dialogService.setContent("未检测到资讯内容！").setShowDialog(true)
			},200);
	   		return;
	   	} 
    	if($scope.editNewsForm.$invalid)
	   	{
    		$timeout(function(){
        		dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true);
        	},200);
    		return;
	   	}
    	$scope.editNews=true;
    	
		$http({
			url : '/enterpriseuniversity/services/news/updateByPrimaryKeySelective',
			method : 'POST',
			data : $.param($scope.r),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
			if (data.result == "success") {
				dialogService.setContent("编辑资讯成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.editNews=false;
					$scope.go('/system/newsList', 'newsList',{});
				},2000);
			}else if(data.result == "error"){
				dialogService.setContent("编辑资讯失败！").setShowSureButton(false).setShowDialog(true); 
				$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.editNews=false;
				},2000);
			}
			$scope.editNews=false;
		}).error(function() {
			dialogService.setContent("编辑资讯失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.editNews=false;
			},2000);
			$scope.editNews=false;
		});
	};
}]);

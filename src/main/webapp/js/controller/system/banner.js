/*
 * 轮播图管理模块
 * */
angular.module('ele.banner', ['ele.admin'])
//轮播图列表控制器
.controller('BannerController',['$scope', '$http', '$state', '$timeout', 'dialogService', function ($scope, $http, $state, $timeout, dialogService) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fe', cmn: '轮播图'};
    $scope.url = "/enterpriseuniversity/services/rollingPicture/findpage?pageNum=";
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
            if ($scope.resourceName != undefined && $scope.resourceName != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&resourceName=" + $scope.resourceName
            		.replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
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
    //添加轮播图
    $scope.doAdd = function(){
        $scope.go('/system/addBanner','addBanner',null);
    }
    //编辑轮播图
    $scope.doEdit = function(banner){
        $scope.go('/system/editBanner','editBanner',{id:banner.pictureId});
    }
    //发布/撤回按钮
    $scope.toggleRelease = function (banner) {
    	dialogService.setContent(banner.isRelease == "Y"?"确定撤回吗?":"确定发布吗?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
			 $http({
				 url: "/enterpriseuniversity/services/rollingPicture/isRelease?pictureId=" + banner.pictureId + "&isRelease=" + (banner.isRelease == "Y" ? "N" : "Y"),
				 method: "PUT"
			 })
			 .success(function (response) {
				 //延迟弹框
				 $timeout(function(){
		    		if(response.result=="N"){
		    			dialogService.setContent(banner.isRelease == "Y"?"撤回失败":"发布失败,此轮播图使用的资讯已经撤回").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
		    		}else if(response.result=="C"){
		    			dialogService.setContent(banner.isRelease == "Y"?"撤回失败":"发布失败，此轮播图使用的课程已经下架").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);		    			
		    		}else if(response.result=="error"){
		    			dialogService.setContent(banner.isRelease == "Y"?"撤回失败":"发布失败").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);		    			
		    		}else{
		    			dialogService.setContent(banner.isRelease == "Y"?"撤回成功":"发布成功").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.paginationConf.onChange();
						},2000);
		    		}
				 },500);
			 })
			 .error(function(){
				 $timeout(function(){
		    		dialogService.setContent("网络异常,请重新登录后再试").setShowDialog(true);
				 },500);
			 });
    	};
    }
    //置顶/取消置顶
    $scope.toggleTop = function (banner) {
    	dialogService.setContent(banner.isTop == "Y"?"确定取消置顶吗?":"确定置顶吗?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
			 $http({
				 url: "/enterpriseuniversity/services/rollingPicture/updateStatus?pictureId=" + banner.pictureId + "&isTop=" + (banner.isTop == "Y" ? "N" : "Y"),
				 method: "PUT"
			 })
			 .success(function (response) {
				 //延迟弹框
				 $timeout(function(){
					 if(response.result=="error"){
						 dialogService.setContent(banner.isTop == "Y"?"取消置顶失败":"置顶失败").setShowSureButton(false).setShowDialog(true);
						 $timeout(function(){
							 dialogService.sureButten_click(); 
						 },2000);
					 }else{
						 dialogService.setContent(banner.isTop == "Y"?"取消置顶成功":"置顶成功").setShowSureButton(false).setShowDialog(true);
						 $timeout(function(){
							 dialogService.sureButten_click(); 
							 $scope.paginationConf.onChange();
						 },2000);
					 }
				 },500);
			 })
			 .error(function(response){
				 $timeout(function(){
					 dialogService.setContent("网络异常,请重新登录后再试").setShowDialog(true);
				 },500);
			 });
    	};
    }
    //删除
    $scope.doDelete = function (banner) {
    	dialogService.setContent("确定删除吗?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
			 $http({
				 url: "/enterpriseuniversity/services/rollingPicture/delete?pictureId=" + banner.pictureId + "&deleted=" + (banner.deleted == "Y" ? "N" : "Y"),
				 method: "PUT"
			 })
			 .success(function (response) {
				 //延迟弹框
				 $timeout(function(){
					 if(response.result=="error"){
						 dialogService.setContent("删除失败").setShowSureButton(false).setShowDialog(true);
						 $timeout(function(){
							 dialogService.sureButten_click(); 
						 },2000);
					 }else{
						 dialogService.setContent("删除成功").setShowSureButton(false).setShowDialog(true);
						 $timeout(function(){
							 dialogService.sureButten_click(); 
							 $scope.paginationConf.onChange();
						 },2000);
					 }
				 },500);
			 })
			 .error(function(){
				 $timeout(function(){
					 dialogService.setContent("网络异常,请重新登录后再试").setShowDialog(true);
				 },500);
			 });
    	};
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(banner){
    	$scope.currHighLightRow.pictureId = banner.pictureId; 
    }
}])
//表单验证
.directive('requiredBannerimg', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = (value!=undefined&&value!="") ? false : true;
	                  ngModel.$setValidity("requiredBannerimg", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
//添加轮播图
.controller('AddBannerController', ['$scope', '$http', '$timeout', 'dialogService', 'saveDialog', function  ($scope, $http, $timeout, dialogService, saveDialog) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fe', cmn: '轮播图', gcmn: '添加轮播图'};
    $scope.banner = $scope.formData={pictureCategory:"N"};
    $scope.pictureCategories = [
        {name:"课程",value:"C"},
        {name:"资讯",value:"N"},
        {name:"链接",value:"U"}
	];
    $scope.flag=true;
    $scope.test = true;
    
    $scope.toggleCategory = function(){
    	$scope.banner.resourceId = "";
    	$scope.banner.resourceName = "";
    	
    	if($scope.banner.pictureCategory=='U'){
    		$scope.flag=false;
    		$scope.test = false;
    	}else{
    		$scope.flag=true;
    		$scope.test = true;
    	}
    	
    }
    //保存按钮
    $scope.doSave = function(){
    	$scope.bannerForm.$submitted = true;
    	if($scope.bannerForm.$invalid)
    	{
    		dialogService.setContent("表单存在填写不合法的字段,请按提示修改后重试").setShowDialog(true);
    		return;
    	}
    	saveDialog.saving();
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/rollingPicture/insert',
    		data : $.param($scope.banner),
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
    	})
    	.success(function(data) {
    		saveDialog.saved();
    		dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    			$scope.go('/system/bannerList','bannerList',null)
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
    	$scope.go('/system/bannerList','bannerList',null)
    };
}])
//编辑轮播图
.controller('EditBannerController', ['$scope', '$http', '$stateParams', '$q', '$timeout', 'dialogService', 'saveDialog', function  ($scope, $http, $stateParams, $q, $timeout, dialogService, saveDialog) {
    $scope.$parent.cm = { pmc:'pmc-f', pmn: '系统管理 ', cmc:'cmc-fe', cmn: '轮播图', gcmn: '编辑轮播图'};
    $scope.deferred = $q.defer();
    //查询数据
    if ($stateParams.id) 
    {
        $http.get("/enterpriseuniversity/services/rollingPicture/findById?pictureId=" + $stateParams.id).success(function (data) {
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
    	 if(data.pictureCategory=='U'){
	     		$scope.flag1=false;
	     		$scope.test1 = false;
	     	}else{
	     		$scope.flag1=true;
	     		$scope.test1 = true;
	     	}
    	 $scope.banner = $scope.formData = data; 
    }, function(data) {  // 错误回调
    	dialogService.setContent("查询轮播图详细数据异常").setShowDialog(true);
    });
    //函数
    $scope.pictureCategories = [
        {name:"课程",value:"C"},
        {name:"资讯",value:"N"},
        {name:"链接",value:"U"}
	];
    $scope.flag1=true;
    $scope.test1 = true;
    
    $scope.toggleCategory = function(){
    	$scope.banner.resourceId = "";
    	$scope.banner.resourceName = "";
    	
    	if($scope.banner.pictureCategory=='U'){
    		$scope.flag1=false;
    		$scope.test1 = false;
    	}else{
    		$scope.flag1=true;
    		$scope.test1 = true;
    	}
    	
    }
    //保存按钮
    $scope.doSave = function(){
    	$scope.bannerForm.$submitted = true;
    	if($scope.bannerForm.$invalid)
    	{
    		dialogService.setContent("表单存在填写不合法的字段,请按提示修改后重试").setShowDialog(true);
    		return;
    	}
    	saveDialog.saving();
    	$http({
    		method : 'PUT',
    		url  : '/enterpriseuniversity/services/rollingPicture/update',
    		data : $.param($scope.banner),
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
    	})
    	.success(function(data) {
    		saveDialog.saved();
    		dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    			$scope.go('/system/bannerList','bannerList',null)
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
    	$scope.go('/system/bannerList','bannerList',null)
    };
}])
//模态框函数
.controller('BannerResourseModalController',['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
    $scope.url = "/enterpriseuniversity/services/rollingPicture/findByCategory?pageNum=";
    $scope.openModalDialog = false;
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 7,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	if(!$scope.openModalDialog){
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
        	$scope.$httpPrams = $scope.$httpPrams + "&pictureCategory=" + $scope.formData.pictureCategory;
            if ($scope.resourseName != undefined && $scope.resourseName != "") 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&name=" 
            		+ $scope.resourseName.replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
            	+ $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                //初始化模态框选中状态
                if ($scope.itemArr.initCurrChoosedItemArrStatus) 
                {
                    $scope.itemArr.initCurrChoosedItemArrStatus();
                } 
            });
        }
    };
    
    $scope.search = function () {
		$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
    	$scope.paginationConf.onChange();
    };
    //筛选数据
    $scope.resourseFilter = function(item){
    	if(item.courseId){
    		return item.courseOnline=="Y"&&item.status=="Y";
    	}else{
    		return item.isRelease=="Y";
    	}
    } 
    /*
     * 模态框容器
     * */
    $scope.itemArr = {
    	oldChooseItemArr: [],
        choosedItemArr: [],
        currChoosedItemArr: [],
        //选中choosedItemArr中的项           
        initCurrChoosedItemArrStatus: function () {
            for (var i in $scope.itemArr.currChoosedItemArr) {
            	for(var j in $scope.page.data){
            		 if($scope.page.data[j].courseId&&$scope.itemArr.currChoosedItemArr[i].courseId&&$scope.page.data[j].courseId==$scope.itemArr.currChoosedItemArr[i].courseId
            				 ||$scope.page.data[j].newsId&&$scope.itemArr.currChoosedItemArr[i].newsId&&$scope.page.data[j].newsId==$scope.itemArr.currChoosedItemArr[i].newsId){
            			 $scope.page.data[j].checked = true;
            		 }
            	}
            }
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.checked) 
            {
                item.checked = undefined;
                $scope.itemArr.currChoosedItemArr = []; 
            } 
            else 
            {
            	for(var j in $scope.page.data){
            		$scope.page.data[j].checked = false;
            	}
                item.checked = true;
                $scope.itemArr.currChoosedItemArr = [];
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        } 
    };
    //初始化管理员项
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	$scope.itemArr.oldChooseItemArr = [];
	    	if($scope.formData.pictureCategory=="N"){
	    		$scope.itemArr.oldChooseItemArr.push({newsId:$scope.formData.resourceId});
	    	}else{
	    		$scope.itemArr.oldChooseItemArr.push({courseId:$scope.formData.resourceId});
	    	}
	    	$scope.itemArr.choosedItemArr = $scope.itemArr.oldChooseItemArr.concat();
        }, function(data) {}); 
    }
    //    字符串
    $scope.buildResource = function () {
        if($scope.itemArr.choosedItemArr.length>0){
        	$scope.$parent.$parent.formData.resourceId = $scope.itemArr.choosedItemArr[0].courseId!=undefined ? $scope.itemArr.choosedItemArr[0].courseId : $scope.itemArr.choosedItemArr[0].newsId;
        	$scope.$parent.$parent.formData.resourceName = $scope.itemArr.choosedItemArr[0].courseName!=undefined ? $scope.itemArr.choosedItemArr[0].courseName : $scope.itemArr.choosedItemArr[0].newsTitle;
        }else{
        	$scope.$parent.$parent.formData.resourceId = "";
        	$scope.$parent.$parent.formData.resourceName = "";
        }
    }
    //显示模态框
    $scope.$parent.$parent.doBannerResourseModal = function () {
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	if($scope.formData.resourceId==""){
    		$scope.itemArr.currChoosedItemArr = [];
    		$scope.itemArr.choosedItemArr = [];
    	}else{
    		$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
    	}
        //清空查询条件
        $scope.resourseName = "";
        //重新查询模态框中的数据
        $scope.search();
    }
    //确定按钮
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        //每次点击确定按钮，生成新的   
        $scope.buildResource();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭按钮
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
.controller('BannerImgController', ['$scope', 'FileUploader', '$timeout', 'dialogService','$http', function ($scope, FileUploader, $timeout, dialogService,$http) {
    var imgUploader = $scope.imgUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/rollingPicture',
            autoUpload:true
        }
    );
    //上传课程封面图片过滤器
    imgUploader.filters.push({
        name: 'customFilter',
        fn: function(item , options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            var size = item.size;
            if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0)
            {
            	dialogService.setContent("要上传新的轮播图片，请先删除旧的轮播图片").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("超出上传图片数量限制").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|'.indexOf(type) == -1)
            {
                dialogService.setContent("上传图片格式不符合要求").setShowDialog(true);
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
            if( item.size/1024 > 2048)
            {
            	dialogService.setContent("上传文件大小不符合要求(大于2048kb)").setShowDialog(true);
            	return item.size/1024 <= 2048;
            }
            return true;
        }
    });
    //编辑页面课程封面图片文件地址
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currFileUrlQueue =[]; 
    		if($scope.formData.pictureUrl&&$scope.formData.pictureUrl!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.pictureUrl);
    		}
        }, function(data) {}); 
    } 
    //修改课程封面图片
    $scope.buildBannerImg = function(imgUrl){
		$scope.$parent.formData.pictureUrl = imgUrl;
    }
    //预览
    $scope.openPreview = false;
    $scope.preview = function(){
    	if($scope.$parent.formData.pictureUrl&&$scope.$parent.formData.pictureUrl!="")
    	{
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.$parent.formData.pictureUrl;
    	}
    	else
    	{
    		dialogService.setContent("请先上传再预览").setShowDialog(true);
    	}
    }
    $scope.closePreview = function(){
    	$scope.openPreview = false;
    }
    //删除之前上传的文件路径集合
    $scope.removeCurrItem = function (item) {
        $scope.currFileUrlQueue.splice($scope.currFileUrlQueue.indexOf(item), 1);
        $scope.buildBannerImg("");
    }
    //删除 
    $scope.removeItem = function (item) {
    	if(item.fileUrl!=null){
    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
    	}
        item.remove();
        document.getElementById("BannerImgFileInput").value=[];
        $scope.buildBannerImg(""); 
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
        	dialogService.setContent("获取上传图片地址失败").setShowDialog(true);
        }
        else
        {
        	fileItem.fileUrl=response.url;
        	$scope.buildBannerImg(response.url);//接收返回的文件地址
        }
    };
}]);
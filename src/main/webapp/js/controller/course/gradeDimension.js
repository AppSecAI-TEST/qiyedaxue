/**
 * Created by Mr.xiang on 2016/3/26.
 */
angular.module('ele.gradeDimension', [])
.controller('GradeDimensionController', ['$scope', '$http', '$timeout','dialogService', function ($scope, $http , $timeout , dialogService) {
	$scope.url = "/enterpriseuniversity/services/commentScore/findList?pageNum=";
	$scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-c', cmn: '评分管理'};
	$scope.showCourseGradeDimensionList = true;
	$scope.showLectureGradeDimensionList = false;
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
        	if($scope.showCourseGradeDimensionList)
        	{
        		$scope.$httpPrams = $scope.$httpPrams + "&gradeDimensionType='C'";
        	}else
        	{
        		$scope.$httpPrams = $scope.$httpPrams + "&gradeDimensionType='L'";
        	}
            if ($scope.gradeDimensionName != undefined&&$scope.gradeDimensionName != "") 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&gradeDimensionName=" 
            		+ $scope.gradeDimensionName.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response){
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            });   	 
        }
    };
    
    //搜索按钮
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
    $scope.openCourseGradeDimensionList = function(){
    	$scope.currHighLightRow = {};
    	$scope.gradeDimensionName = "";
    	$scope.showCourseGradeDimensionList = true;
    	$scope.showLectureGradeDimensionList = false;
    	$scope.search();
    }
    $scope.openLectureGradeDimensionList = function(){
    	$scope.currHighLightRow = {};
    	$scope.gradeDimensionName = "";
    	$scope.showCourseGradeDimensionList = false;
    	$scope.showLectureGradeDimensionList = true;
    	$scope.search();
    }
    //添加评分标准按钮
    $scope.doAdd = function(){
    	if($scope.showCourseGradeDimensionList){
    		$scope.go('/commentScore/addCourseGradeDimension','addCourseGradeDimension',null);
    	}else{
    		$scope.go('/commentScore/addLectureGradeDimension','addLectureGradeDimension',{});
    	}
    }
    //编辑评分标准按钮
    $scope.doEdit = function(gradeDimension){
    	if($scope.showCourseGradeDimensionList){
    		$scope.go('/commentScore/EditCourseGradeDimension','editCourseGradeDimension',{id:gradeDimension.gradeDimensionId});
    	}else{
    		$scope.go('/commentScore/EditLectureGradeDimension','editLectureGradeDimension',{id:gradeDimension.gradeDimensionId});
    	}
    }
    // 删除评分维度
    $scope.doDelete = function (gradeDimension) {
    	dialogService.setContent("确定删除评分维度?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
    		$http({
                url: "/enterpriseuniversity/services/commentScore/delete?gradeDimensionId=" + gradeDimension.gradeDimensionId,
                method: "PUT"
            }).success(function (response) {
            	//延迟弹框
		    	$timeout(function(){
		    		/*dialogService.setContent("删除评分维度成功").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
		    			$scope.gradeDimensionName = "";
		    			$scope.search();
		    		}*/
		    		dialogService.setContent("删除评分维度成功！").setShowSureButton(false).setShowDialog(true);
    	    		$timeout(function(){
    					dialogService.sureButten_click(); 
    					$scope.gradeDimensionName = "";
		    			$scope.search(); 
    				},2000);
		    	},200);
            }).error(function(){
				$timeout(function(){
		    		//dialogService.setContent("删除评分维度失败").setShowDialog(true);
		    		dialogService.setContent("删除评分维度失败！").setShowSureButton(false).setShowDialog(true);
    	    		$timeout(function(){
    					dialogService.sureButten_click(); 
    				},2000);
		    	},200);
			});
    	}
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(gradeDimension){
        $scope.currHighLightRow.gradeDimensionId = gradeDimension.gradeDimensionId; 
    } 
}])
.controller('AddCourseGradeDimensionController',['$scope', '$http', '$timeout', 'dialogService', function ($scope, $http, $timeout, dialogService) {
    $scope.$parent.cm = {pmc: 'pmc-a', pmn: '课程管理 ', cmc:'cmc-c', cmn: '评分管理', gcmn:'添加课程评分维度'};
    $scope.gradeDimension = {};
    $scope.gradeDimension.gradeDimensionType = "C";
    $scope.addCourseGradeSubmit=false;
    $scope.doSave = function(){
    	$scope.loding=true;
    	$scope.gradeDimensionForm.$submitted = true;
    	if($scope.gradeDimensionForm.$invalid)
   	 	{
    		 dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
	   		 return;
   	 	}
    	$scope.addCourseGradeSubmit=true;
    	$http({
		  	method : 'POST',
		  	url  : '/enterpriseuniversity/services/commentScore/add',
		  	data : $.param($scope.gradeDimension), // pass in data as strings
		  	headers : { 'Content-Type': 'application/x-www-form-urlencoded' } // set the headers so angular passing info as form data (not request payload)
    	})
    	.success(function(data) {
			dialogService.setContent("新增课程评分维度成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addCourseGradeSubmit=false;
				$scope.go('/course/gradeDimensionList','gradeDimensionList',null);
			},2000);
    	})
    	.error(function(){
			dialogService.setContent("新增课程评分维度失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addCourseGradeSubmit=false;
			},2000);
    	});
    }
    $scope.doReturn = function(){
    	$scope.go('/course/gradeDimensionList','gradeDimensionList',null);
    }  
}])
.controller('AddLectureGradeDimensionController',['$scope', '$http', '$timeout', 'dialogService', function ($scope, $http, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-c', cmn: '评分管理', gcmn:'添加讲师评分维度'}; 
    $scope.gradeDimension = {};
    $scope.gradeDimension.gradeDimensionType = "L";
    $scope.addLectureGradeSubmit=false;
    $scope.doSave = function(){
    	//验证
    	$scope.gradeDimensionForm.$submitted = true;
    	if($scope.gradeDimensionForm.$invalid)
   	 	{
    		dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
   		 	return;
   	 	}
    	$scope.addLectureGradeSubmit=true;
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/commentScore/add',
    		data : $.param($scope.gradeDimension),
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
    	})
    	.success(function(data) {
    		dialogService.setContent("新增讲师评分维度成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addLectureGradeSubmit=false;
				$scope.go('/course/gradeDimensionList','gradeDimensionList',null);
    		},2000);
    	})
    	.error(function(){
    		dialogService.setContent("新增讲师评分维度失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addLectureGradeSubmit=false;
			},2000);
    	});
    }
    $scope.doReturn = function(){
    	$scope.go('/course/gradeDimensionList','gradeDimensionList',null);
    }
}])
.controller('EditCourseGradeDimensionController', ['$scope', '$http', '$stateParams', '$timeout', 'dialogService', function ($scope, $http, $stateParams, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-c', cmn:'评分管理', gcmn: '编辑课程评分维度'};
    if ($stateParams.id) 
    {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/commentScore/findDetails?gradeDimensionId=" + $stateParams.id)
        .success(function (response) {
        	$scope.$emit('isLoading', false);
            $scope.gradeDimension = response;
        }).error(function(){
        	$scope.$emit('isLoading', false);
        });
    } 
    else 
    {
        dialogService.setContent("未获取到查询参数").setShowDialog(true);
    }
    $scope.editCourseGradeSubmit=false;
    $scope.doSave = function(){
    	$scope.gradeDimensionForm.$submitted = true;
    	if($scope.gradeDimensionForm.$invalid)
   	 	{
    		dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
   		 	return;
   	 	}
    	$scope.editCourseGradeSubmit=true;
    	$http({
    		method : 'POST',
		  	url  : '/enterpriseuniversity/services/commentScore/update',
		  	data : $.param($scope.gradeDimension), 
		  	headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
    	})
    	.success(function(data) {
    		if(data.result=="success"){
    			dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.editCourseGradeSubmit=false;
					$scope.go('/course/gradeDimensionList','gradeDimensionList',null);
	    		},2000);
    		}else{
    			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true); 
    			$timeout(function(){
    				dialogService.sureButten_click(); 
    				$scope.editCourseGradeSubmit=false;
    			},2000);
    		}
    	})
    	.error(function(){
    		dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.editCourseGradeSubmit=false;
			},2000);
    	});
    }
    $scope.doReturn = function(){
    	$scope.go('/course/gradeDimensionList','gradeDimensionList',null);
    }
}])
.controller('EditLectureGradeDimensionController', ['$scope', '$http', '$stateParams', '$timeout', 'dialogService', function ($scope, $http, $stateParams, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-c', cmn:'评分管理', gcmn:'编辑讲师评分维度'};
    if ($stateParams.id) 
    {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/commentScore/findDetails?gradeDimensionId=" + $stateParams.id)
        .success(function (response) {
        	$scope.$emit('isLoading', false);
        	$scope.gradeDimension = response;
        }).error(function(){
        	$scope.$emit('isLoading', false);
        });
    } else 
    {
        dialogService.setContent("未获取到查询参数").setShowDialog(true);
    }
    $scope.editLectureGradeSubmit=false;
    $scope.doSave = function(){
    	$scope.gradeDimensionForm.$submitted = true;
    	if($scope.gradeDimensionForm.$invalid)
   	 	{
    		dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true); 
    		return;
   	 	}
    	$scope.editLectureGradeSubmit=true;
    	$http({
    		method : 'POST',
		  	url  : '/enterpriseuniversity/services/commentScore/update',
		  	data : $.param($scope.gradeDimension), // pass in data as strings
		  	headers : { 'Content-Type': 'application/x-www-form-urlencoded' } // set the headers so angular passing info as form data (not request payload)
    	})
    	.success(function(data) {
			dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.editLectureGradeSubmit=false;
				$scope.go('/course/gradeDimensionList','gradeDimensionList',null);
    		},2000);
    	})
    	.error(function(){
    		dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.editLectureGradeSubmit=false;
			},2000);
    	});
    }
    $scope.doReturn = function(){
    	$scope.go('/course/gradeDimensionList','gradeDimensionList',null);
    }
}])
/*
 * 模态框函数
 * */
.controller('CourseGradeDimensionController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
	$scope.isInitUpdatePageData = false;
	$scope.openModalDialog = false;
	$scope.gradeDimensionType = 'C';
	$scope.url = "/enterpriseuniversity/services/commentScore/findList?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 15,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	if(!($scope.isInitUpdatePageData||$scope.openModalDialog)){
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "&gradeDimensionType=" + $scope.gradeDimensionType;
        	$scope.$emit('isLoading', true);
            if ($scope.gradeDimensionName != undefined && $scope.gradeDimensionName != "") 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&gradeDimensionName=" 
            		+ $scope.gradeDimensionName.replace(/\%/g,"%25").replace(/\#/g,"%23")
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
            	$scope.paginationConf.totalItems = 0;
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
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	for(var j in $scope.page.data)
            	{
            		 if($scope.page.data[j].gradeDimensionId==$scope.itemArr.currChoosedItemArr[i].gradeDimensionId)
            		 {
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
                $scope.showTipMessage = false;
                for(var i in $scope.itemArr.currChoosedItemArr)
                {
                	if(item.gradeDimensionId==$scope.itemArr.currChoosedItemArr[i].gradeDimensionId)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                	}
                }
            } 
            else 
            {
            	if($scope.itemArr.currChoosedItemArr.length>=5){
            		//dialogService.setContent("最多可选择5种评分维度").setShowDialog(true);
            		$scope.showTipMessage = true;
            		document.getElementById(item.gradeDimensionId).checked = false;
            	}
            	else
            	{
            		item.checked = true;
                    $scope.itemArr.currChoosedItemArr.push(item);
            	}
            }
        },
        //删除操作
        deleteItem : function (item) {
            item.checked = undefined;
            for(var i in $scope.itemArr.choosedItemArr)
            {
            	if(item.gradeDimensionId==$scope.itemArr.choosedItemArr[i].gradeDimensionId)
            	{
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            	}
            }
            $scope.buildCourseGradeDimensions();
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
	            	var gradeDimensionId = $scope.itemArr.oldChooseItemArr[i];
	            	for (var j in $scope.page.data) 
	            	{
	            		if($scope.page.data[j])
	            		{
	            			if($scope.page.data[j].gradeDimensionId==gradeDimensionId)
	            			{
	            				if($scope.itemArr.choosedItemArr.length<1)
	            				{
	            					$scope.itemArr.choosedItemArr.push($scope.page.data[j]);
	            					continue;
	            				}
	            				var isContained = false;
	            				for(var k in $scope.itemArr.choosedItemArr)
	            				{
	            					if($scope.itemArr.choosedItemArr[k].gradeDimensionId==$scope.page.data[j].gradeDimensionId)
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
        	$scope.$emit('isLoading', false);
        }
    };
    //接收上级scope传递的参数
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
        	$scope.formData = data; 
        	var courseGradeDimensions = [];
        	if($scope.formData.courseGradeDimensions){
        		courseGradeDimensions = $scope.formData.courseGradeDimensions.split(",");
        	}
        	for(var i = 0;i<courseGradeDimensions.length;i++)
        	{
        		if($scope.itemArr.oldChooseItemArr.indexOf(courseGradeDimensions[i])==-1)
        		{
        			$scope.itemArr.oldChooseItemArr.push(courseGradeDimensions[i]);
        		}
        	}
        	$scope.isInitUpdatePageData = true;
        	$scope.search();
        }, function(data) {
            
        }); 
    }
     
    $scope.buildCourseGradeDimensions = function () {
        var courseGradeDimensionsArry = [];
        for (var i in $scope.itemArr.choosedItemArr) 
        {
        	courseGradeDimensionsArry.push($scope.itemArr.choosedItemArr[i].gradeDimensionId);
        }
        $scope.$parent.$parent.formData.courseGradeDimensions = courseGradeDimensionsArry.join(",");
    }
    
    $scope.$parent.$parent.doOpenCourseGradeDimensionModal = function () {
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.showTipMessage = false;
    	$scope.isBuildOldChoosedItems = true;
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
        //清空查询条件
        $scope.gradeDimensionName = "";
        $scope.paginationConf.currentPage = 1;
        //初始化每页显示条数
        $scope.paginationConf.itemsPerPage = 20;
        //重新查询模态框中的数据
        $scope.search();
    }
   
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildCourseGradeDimensions();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
.controller('LectureGradeDimensionController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout , dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/commentScore/findList?pageNum=";
	$scope.isInitUpdatePageData = false;
	$scope.openModalDialog = false;
	$scope.gradeDimensionType = 'L';
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 15,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	if(!($scope.isInitUpdatePageData||$scope.openModalDialog)){
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "&gradeDimensionType=" + $scope.gradeDimensionType;
        	$scope.$emit('isLoading', true);
            if ($scope.gradeDimensionName != undefined && $scope.gradeDimensionName != "") 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&gradeDimensionName=" 
            		+ $scope.gradeDimensionName.replace(/\%/g,"%25").replace(/\#/g,"%23")
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
                //初始化模态框选中状态
                if ($scope.itemArr.initCurrChoosedItemArrStatus&&$scope.isBuildOldChoosedItems) {
                    $scope.itemArr.initCurrChoosedItemArrStatus();
                } 
                //只执行一次
                if($scope.itemArr.buildOldChoosedItemArr&&!$scope.isBuildOldChoosedItems){
                	$scope.itemArr.buildOldChoosedItemArr();
                }
            }).error(function(){
            	$scope.paginationConf.totalItems = 0;
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
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	for(var j in $scope.page.data)
            	{
            		 if($scope.page.data[j].gradeDimensionId==$scope.itemArr.currChoosedItemArr[i].gradeDimensionId)
            		 {
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
                $scope.showTipMessage = false;
                for(var i in $scope.itemArr.currChoosedItemArr)
                {
                	if(item.gradeDimensionId==$scope.itemArr.currChoosedItemArr[i].gradeDimensionId)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                	}
                }
            } 
            else 
            {
            	if($scope.itemArr.currChoosedItemArr.length>=5){
            		//dialogService.setContent("最多可选择5种评分维度").setShowDialog(true);
            		$scope.showTipMessage = true;
            		document.getElementById(item.gradeDimensionId).checked = false;
            	}
            	else
            	{
            		item.checked = true;
                    $scope.itemArr.currChoosedItemArr.push(item);
            	}
            }
        },
        //删除操作
        deleteItem : function (item) {
            item.checked = undefined;
            for(var i in $scope.itemArr.choosedItemArr)
            {
            	if(item.gradeDimensionId==$scope.itemArr.choosedItemArr[i].gradeDimensionId)
            	{
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            	}
            }
            $scope.buildLectureGradeDimensions();
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
	            	var gradeDimensionId = $scope.itemArr.oldChooseItemArr[i];
	            	for (var j in $scope.page.data) 
	            	{
	            		if($scope.page.data[j]){
	            			if($scope.page.data[j].gradeDimensionId==gradeDimensionId)
	            			{
	            				if($scope.itemArr.choosedItemArr.length<1)
	            				{
	            					$scope.itemArr.choosedItemArr.push($scope.page.data[j]);
	            					continue;
	            				}
	            				var isContained = false;
	            				for(var k in $scope.itemArr.choosedItemArr)
	            				{
	            					if($scope.itemArr.choosedItemArr[k].gradeDimensionId==$scope.page.data[j].gradeDimensionId)
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
        	$scope.$emit('isLoading', false);
        }
    };
    
	//接收上级scope传递的参数
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	var lecturerGradeDimensions = [];
	    	if($scope.formData.lecturerGradeDimensions){
	    		lecturerGradeDimensions = $scope.formData.lecturerGradeDimensions.split(",");
	    	}
	    	for(var i = 0;i<lecturerGradeDimensions.length;i++)
	    	{
	    		if($scope.itemArr.oldChooseItemArr.indexOf(lecturerGradeDimensions[i])==-1)
	    		{
	    			$scope.itemArr.oldChooseItemArr.push(lecturerGradeDimensions[i]);
	    		}
	    	}
	    	$scope.isInitUpdatePageData = true;
	    	$scope.search();
        }, function(data) {
            
        }); 
    }
    $scope.buildLectureGradeDimensions = function () {
        var lectureGradeDimensionsArry = [];
        for (var i in $scope.itemArr.choosedItemArr) {
        	lectureGradeDimensionsArry.push($scope.itemArr.choosedItemArr[i].gradeDimensionId);
        }
        $scope.$parent.$parent.formData.lecturerGradeDimensions = lectureGradeDimensionsArry.join(",");
    }
    
    $scope.$parent.$parent.doOpenLectureGradeDimensionModal = function () {
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.showTipMessage = false;
    	$scope.isBuildOldChoosedItems = true;
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
        //清空查询条件
        $scope.gradeDimensionName = "";
        $scope.paginationConf.currentPage = 1;
        //初始化每页显示条数
        $scope.paginationConf.itemsPerPage = 20;
        //重新查询模态框中的数据
        $scope.search();
    }
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildLectureGradeDimensions();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}]);
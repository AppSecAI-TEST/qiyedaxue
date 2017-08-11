/**
 * Created by Wanglg on 2016/3/30.
 */
angular.module('ele.taskPackage',['ele.admin'])
.controller('TaskPackageController',['$scope', '$http','dialogService','$timeout', function ($scope, $http,dialogService,$timeout) {
    //用于显示面包屑状态
    $scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-ca', cmn: '任务包'};
    //http请求url
    $scope.url = "/enterpriseuniversity/services/taskPackage/findTaskList?pageNum=";
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
            if ($scope.packageName != undefined && $scope.packageName != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&packageName=" + $scope.packageName
	                .replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
            "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
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
    //添加任务包按钮
    $scope.doAdd = function(){
        $scope.go('/taskPackage/addTaskPackage','addTaskPackage',{});
    }
    //编辑任务包按钮
    $scope.doEdit = function(t){
        $scope.go('/taskPackage/editTaskPackage','editTaskPackage',{id:t.packageId});
    }
    //查看任务包明细
    $scope.doView = function(t){
	 	$scope.go('/taskPackage/detailTaskPackage','detailTaskPackage',{id:t.packageId});
    }
    //删除任务包
    $scope.doDelete = function(t){
    	dialogService.setContent('确定要删除吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
 		.doNextProcess = function(){
    		 $http({
         		url: "/enterpriseuniversity/services/taskPackage/delete?packageId=" + t.packageId,
                method: "DELETE"
             }).success(function (response) {
            	 if(response.result=="protected"){
            		 dialogService.setContent("不能删除已使用的任务包！").setShowDialog(true);
            	 }else if(response.result=="error"){
            		 dialogService.setContent("删除失败！").setShowDialog(true);
            	 }else if(response.result=="success"){
            		 dialogService.setContent("删除成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
            			 $scope.paginationConf.onChange();
        			 };
            	 }
             }); 
    	 }
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(taskPackage){
        $scope.currHighLightRow.packageId = taskPackage.packageId; 
    }
}])
//添加任务包
.controller('AddTaskPackageController', ['$scope', '$http', '$timeout', 'dialogService', function ($scope, $http, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-ca', cmn: '任务包', gcmn: '添加任务包'};
    $scope.taskPackage = $scope.formData={};
    $scope.itemArr = {
        choosedItemArr: [],
        deleteItem : function (item) {
            item.checked = undefined;
            for(var i in $scope.itemArr.choosedItemArr){
            	var currItem = null;
            	if(item.examPaperId&&item.examPaperId==$scope.itemArr.choosedItemArr[i].examPaperId){
            		currItem = $scope.itemArr.choosedItemArr[i];
            	}
            	if(item.courseId&&item.courseId==$scope.itemArr.choosedItemArr[i].courseId){
            		currItem = $scope.itemArr.choosedItemArr[i]; 
            	}
            	if(currItem){
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            	}
            }
            for(var j in $scope.taskPackage.coursesId){
            	if(item.courseId&&item.courseId==$scope.taskPackage.coursesId[j]){
            		$scope.taskPackage.coursesId.splice(j, 1);
            	}
            }
            for(var k in $scope.taskPackage.examPaperId){
            	if(item.examPaperId&&item.examPaperId==$scope.taskPackage.examPaperId[k]){
            		$scope.taskPackage.examPaperId.splice(k, 1);
            	}
            }
        }
    }
    $scope.doReturn=function(){
    	$scope.go('/activity/taskPackageList','taskPackageList',null);
    }
    $scope.addTaskPackageSubmit=false;
    $scope.doSave = function(){
    	$scope.taskPackageForm.$submitted = true;
    	if($scope.taskPackageForm.$invalid)
   	 	{
   		 	return;
   	 	}
    	$scope.addTaskPackageSubmit=true;
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/taskPackage/add',
    		data : $.param($scope.taskPackage), 
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
    	})
    	.success(function(data) {
    		$scope.addTaskPackageSubmit=false;
    		if(data.result=="error"){
    			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
    			$timeout(function(){
    				dialogService.sureButten_click(); 
    			},2000);
	   	 	}else if(data.result=="success"){
		   		dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
		   		$timeout(function(){
		   			dialogService.sureButten_click(); 
		   			$scope.go('/activity/taskPackageList','taskPackageList',null);
		   		},2000);
	   	 	}
    	})
    	.error(function(){
    		$scope.addTaskPackageSubmit=false;
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
				dialogService.sureButten_click(); 
			},2000);
    	});
    }
}])
//编辑任务包控制器
.controller('EditTaskPackageController',['$scope','$http','$state','$stateParams','$q','dialogService','$timeout',function ($scope, $http, $state, $stateParams, $q, dialogService, $timeout) {
    $scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-ca', cmn: '任务包', gcmn: '编辑任务包'};
    $scope.deferred = $q.defer();
    $scope.deferredAll = $q.defer();
    $scope.taskForm=$scope.formData={};
    if ($stateParams.id) {
        $http.get("/enterpriseuniversity/services/taskPackage/findDetails?packageId=" + $stateParams.id).success(function (data) {
        	$scope.taskPackage = $scope.formData = data;
        	$scope.bindCourseIdAndExamPaperId(data);
        	$scope.bindAdmins(data);
        	$scope.deferred.resolve(data);
        }).error(function(){
        	$scope.deferred.reject(data); 
        });
    } else {
    	$timeout(function(){
			dialogService.setContent("未传递taskPackageId参数！").setShowDialog(true)
		},200);
    }
    $scope.doReturn=function(){
    	$scope.go('/activity/taskPackageList','taskPackageList',null);
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.taskPackage = $scope.formData = data;
    	$scope.itemArr.choosedItemArr = $scope.taskPackage.objectList;
    }, function(data) {  // 错误回调
        console.log('请求失败');
        $timeout(function(){
			dialogService.setContent("查询出错！").setShowDialog(true)
		},200);
        return;
    });
    $scope.bindCourseIdAndExamPaperId=function(data){
    	var courseIdArry = [];
        for (var i in data.objectList) {
        	if(data.objectList[i].courseId){
        		courseIdArry.push(data.objectList[i].courseId);
        	}
        }
        $scope.formData.coursesId = courseIdArry;
        
        var examsIdArry = [];
        for (var i in data.objectList) {
        	if(data.objectList[i].examPaperId){
        		examsIdArry.push(data.objectList[i].examPaperId);
        	}
        }
        $scope.formData.examPaperId = examsIdArry;
    }
    $scope.bindAdmins=function(data){
        $scope.formData.admins = data.admins;
    }
    $http.get("/enterpriseuniversity/services/course/findPage?pageNum=1&pageSize=-1").success(function(data) {
    	$scope.deferredAll.resolve(data);
	}).error(function(){
		$scope.deferredAll.reject(data); 
	});
    
    $scope.deferredAll.promise.then(function(response) {  // 成功回调
    	$scope.courseList  = response.data;
    }, function(data) {  // 错误回调
        console.log('查询角色列表失败');
        $timeout(function(){
			dialogService.setContent("查询出错！").setShowDialog(true)
		},200);
        return;
    }); 
    
    $http.get("/enterpriseuniversity/services/course/findPage?pageNum=1&pageSize=-1").success(function(data) {
    	$scope.deferredAll.resolve(data);
	}).error(function(){
		$scope.deferredAll.reject(data); 
	});
    
    $scope.deferredAll.promise.then(function(response) {  // 成功回调
    	$scope.courseList  = response.data;
    }, function(data) {  // 错误回调
        console.log('查询角色列表失败');
        $timeout(function(){
			dialogService.setContent("查询出错！").setShowDialog(true)
		},200);
        return;
    }); 
    $scope.itemArr = {
		 choosedItemArr: [],
		 deleteItem : function (item) {
            item.checked = undefined;
            for(var i in $scope.itemArr.choosedItemArr){
            	var currItem = null;
            	if(item.examPaperId&&item.examPaperId==$scope.itemArr.choosedItemArr[i].examPaperId){
            		currItem = $scope.itemArr.choosedItemArr[i];
            	}
            	if(item.courseId&&item.courseId==$scope.itemArr.choosedItemArr[i].courseId){
            		currItem = $scope.itemArr.choosedItemArr[i]; 
            	}
            	if(currItem){
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            	}
            }
        }
    };
    $scope.editTaskPackageSubmit=false;
    $scope.doSave = function(){
    	$scope.taskPackageForm.$submitted = true;
    	if($scope.taskPackageForm.$invalid)
   	 	{
   		 	return;
   	 	}
    	$scope.taskPackage.packageId=$stateParams.id;
    	$scope.taskPackage.objectList=$scope.itemArr.choosedItemArr;
    	$scope.bindCourseIdAndExamPaperId($scope.taskPackage);
    	$scope.editTaskPackageSubmit=true;
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/taskPackage/update',
    		data : $.param($scope.taskPackage), 
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' }
    	}).success(function(data) {
    		$scope.editTaskPackageSubmit=false;
    		if(data.result=="protected"){
    			dialogService.setContent("不能编辑已使用的任务包！").setShowDialog(true);
    		}else if(data.result=="error"){
    			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
				$timeout(function(){
					dialogService.sureButten_click(); 
				},2000);
    		}else if(data.result=="success"){
    			dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   			$scope.go('/activity/taskPackageList','taskPackageList',null);
		   		},2000);
    		}
    	}).error(function(){
    		$scope.editTaskPackageSubmit=false;
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
				dialogService.sureButten_click(); 
			},2000);
    	});
    }
}])
.directive('requiredItem', [function () {
  return {
      require: "ngModel",
      link: function (scope, element, attr, ngModel) {
          var customValidator = function (value) {
        	  var validity = null;
        	  scope.$watch(function () {
                  return value;
              }, function(){
            	  validity = value < 1 ? true : false;
                  ngModel.$setValidity("requiredItem", !validity);
              });
              return !validity ? value : 0;
          };
          ngModel.$formatters.push(customValidator);
          ngModel.$parsers.push(customValidator);
      }
  };
}])
//查看任务包控制器
.controller('DetailTaskPackageController', ['$scope','$http','$state','$stateParams','$q','dialogService','$timeout',function ($scope, $http, $state, $stateParams,$q,dialogService,$timeout) {
    $scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-ca', cmn: '任务包', gcmn: '查看任务包'};
    $scope.deferred1 = $q.defer();
    $scope.deferred2 = $q.defer();
    if($stateParams.id) {
        $http.get("/enterpriseuniversity/services/taskPackage/findDetails?packageId=" + $stateParams.id).success(function (data) {
        	$scope.deferred1.resolve(data);
	    }).error(function(data){
			$scope.deferred1.reject(data); 
		});
	} else {
		dialogService.setContent("未获取传递的查询参数！").setShowDialog(true)
	}
	$http.get("/enterpriseuniversity/services/admin/findpage?pageNum=1&pageSize=-1").success(function (data) {
		$scope.deferred2.resolve(data);
	}).error(function(data){
	    $scope.deferred2.reject(data); 
	});
    $scope.deferred1.promise.then(function(data) {  // 成功回调
    	$scope.taskPackage = $scope.formData = data; 
    	$scope.adminIdArr = $scope.formData.admins.split(",");
   		$scope.deferred2.promise.then(function(data) { 
   			$scope.page = data;
   			$scope.choosedItemArr = [];
   			// 成功回调
   			$scope.getAdminNames();
   			
        }, function(data) {  // 错误回调
            console.log('请求失败'+ data);
            $timeout(function(){
				dialogService.setContent("查询出错！").setShowDialog(true)
			},200);
            return;
        });  
   		
   }, function(data) {  // 错误回调
       console.log('请求失败'+ data);
       $timeout(function(){
			dialogService.setContent("查询出错！").setShowDialog(true)
		},200);
       return;
   }); 
   $scope.getAdminNames=function(){
	   for(var i in $scope.adminIdArr){
			var adminId = $scope.adminIdArr[i];
			for (var j in $scope.page.data) 
			{
				if($scope.page.data[j])
				{
					if($scope.page.data[j].adminId==adminId)
					{
						if($scope.choosedItemArr.length<1)
						{
							$scope.choosedItemArr.push($scope.page.data[j]);
							continue;
						}
						var isContained = false;
						for(var k in $scope.choosedItemArr)
						{
							if($scope.choosedItemArr[k].adminId==$scope.page.data[j].adminId)
							{
								isContained = true;
								break;
			    				} 
						}
						if(!isContained)
						{
							$scope.choosedItemArr.push($scope.page.data[j]);
						}
			    	}
				}
			 }
	   	}
   } 
   //返回按钮
   $scope.doReturn=function(){
   	$scope.go('/activity/taskPackageList','taskPackageList',null);
   }
}])
//任务包选择课程摸态框
.controller('CourseModelController',  ['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/course/findPage?pageNum=";
	$scope.openModalDialog = false;
    $scope.isInitUpdatePageData = false;
    $scope.online = [
        {name: "线上课程", value: "Y"},
        {name: "线下课程", value: "N"}
    ];
    $http.get("/enterpriseuniversity/services/courseClassify/findHighest")
	    .success(function (response) {
	        $scope.type = response;
	    })
	    .error(function () {
	    	dialogService.setContent("课程分类下拉菜单初始化错误！").setShowDialog(true)
	    });
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
        	$scope.$httpPrams = "";
            if ($scope.courseName != undefined&&$scope.courseName != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&courseName=" + $scope.courseName;
            }
            if ($scope.selectedOnline != undefined&&$scope.selectedOnline != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&courseOnline=" + $scope.selectedOnline;
            }
            if ($scope.selectedType != undefined&&$scope.selectedType != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&courseType=" + $scope.selectedType;
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
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
            	dialogService.setContent("课程数据查询异常！").setShowDialog(true);
            });
        }
    };
    //搜索按钮函数
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
    //改变查询条件
    $scope.changeSelectedOption = function(){
    	$scope.search();
    }
    /*
     * 模态框容器
     * */
    $scope.itemArr = {
    	isChooseAllItem: false,
    	oldChooseItemArr: [],
        currChoosedItemArr: [], 
        //选中choosedItemArr中的项           
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	for(var j in $scope.page.data)
            	{
            		if($scope.itemArr.currChoosedItemArr[i].courseId)
            		{
            			if($scope.page.data[j].courseId==$scope.itemArr.currChoosedItemArr[i].courseId)
            			{
               			 	$scope.page.data[j].checked = true;
               		 	}
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
                	if($scope.itemArr.currChoosedItemArr[i].courseId)
            		{
                    	if(item.courseId==$scope.itemArr.currChoosedItemArr[i].courseId)
                    	{
                    		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                    	}
            		}
                }
            } 
            else 
            {
                item.checked = true;
                $scope.itemArr.currChoosedItemArr.push(item);
                //$scope.$parent.$parent.taskPackage.objectList= $scope.itemArr.currChoosedItemArr;
            }
        },
        //全选
        chooseAllItem : function(){
        	//...
        	 
        },
        //编辑页面数据回显
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.page.data) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	if($scope.itemArr.oldChooseItemArr[i].courseId){
    	            	var courseId = $scope.itemArr.oldChooseItemArr[i].courseId;
    	            	for (var j in $scope.page.data) 
    	            	{
    	            		if($scope.page.data[j])
    	            		{
    	            			if($scope.page.data[j].courseId==courseId)
    	            			{
    	            				if($scope.$parent.$parent.itemArr.choosedItemArr.length<1)
    	            				{
    	            					$scope.$parent.$parent.itemArr.choosedItemArr.push($scope.page.data[j]);
    	            					continue;
    	            				}
    	            				var isContained = false;
    	            				for(var k in $scope.$parent.$parent.itemArr.choosedItemArr)
    	            				{
    	            					if($scope.$parent.$parent.itemArr.choosedItemArr[k].courseId==$scope.page.data[j].courseId)
    	            					{
    	            						isContained = true;
    	            						break;
        	            				} 
    	            				}
    	            				if(!isContained)
    	            				{
    	            					$scope.$parent.$parent.itemArr.choosedItemArr.push($scope.page.data[j]);
    	            				}
        		            	}
    	            		}
    		            }
	            	}
	            }
            }
        	$scope.$emit('isLoading', false);
        }
    }
    $scope.buildCourses = function () {
        var courseIdArry = [];
        for (var i in $scope.$parent.$parent.itemArr.choosedItemArr) {
        	if($scope.$parent.$parent.itemArr.choosedItemArr[i].courseId){
        		courseIdArry.push($scope.$parent.$parent.itemArr.choosedItemArr[i].courseId);
        	}
        }
        $scope.$parent.$parent.formData.coursesId = courseIdArry;
    }
    $scope.$parent.$parent.doOpenCourseModal= function() {
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.isBuildOldChoosedItems = true;
    	$scope.itemArr.currChoosedItemArr = $scope.$parent.$parent.itemArr.choosedItemArr.concat();
        //清空查询条件
    	$scope.courseName = "";
    	$scope.selectedType = "";
    	$scope.selectedOnline = "";
        $scope.paginationConf.currentPage = 1;
        //初始化每页显示条数
        $scope.paginationConf.itemsPerPage = 20;
        //重新查询模态框中的数据
        $scope.search();
	}
    $scope.doSure = function () {
    	$scope.$parent.$parent.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildCourses();
        //每次点击确定按钮，生成新的  course
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭按钮
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //编辑页面初始化
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	$scope.itemArr.oldChooseItemArr = [];
	    	if($scope.formData){
	    		$scope.itemArr.oldChooseItemArr = $scope.formData.objectList;
	    	}
    		$scope.isInitUpdatePageData = true;
	    	$scope.search(); 
        }, function(data) {}); 
    }
}])
//任务包选择试卷
.controller('ExamModelController', ['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/examPaper/findPage?pageNum=";
	$scope.openModalDialog = false;
    $scope.isInitUpdatePageData = false;
	$scope.examType = [
	     {name: "固定考试", value: "1"},
	     {name: "随机考试", value: "2"},
	     {name: "固定调研", value: "3"},
	     {name: "随机调研", value: "4"},
	     {name: "固定总结", value: "5"},
	     {name: "随机总结", value: "6"}
    ];
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
        	$scope.$httpPrams = "";
            if ($scope.examPaperName != undefined && $scope.examPaperName != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&examPaperName=" + $scope.examPaperName;
            }
            if ($scope.selectedType != undefined && $scope.selectedType != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&examPaperType=" + $scope.selectedType;
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
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
            	dialogService.setContent("试卷数据查询异常！").setShowDialog(true);
            });
        }
	};
	//搜索按钮函数
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
    //改变查询条件
    $scope.changeSelectedOption = function(){
    	$scope.search();
    }
    $scope.$parent.$parent.doOpenExamPaperModal= function() {
		$scope.openModalDialog = true;
		dialogStatus.setHasShowedDialog(true);
    	$scope.isBuildOldChoosedItems = true;
    	$scope.itemArr.currChoosedItemArr = $scope.$parent.$parent.itemArr.choosedItemArr.concat();
        //清空查询条件
    	$scope.examPaperName = "";
    	$scope.selectedType = "";
        $scope.paginationConf.currentPage = 1;
        //初始化每页显示条数
        $scope.paginationConf.itemsPerPage = 20;
        //重新查询模态框中的数据
        $scope.search();
	}
    //编辑页面初始化 
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	$scope.itemArr.oldChooseItemArr = [];
	    	if($scope.formData){
	    		$scope.itemArr.oldChooseItemArr = $scope.formData.objectList;
	    	}
    		$scope.isInitUpdatePageData = true;
	    	$scope.search(); 
        }, function(data) {}); 
    }
    /*
     * 模态框容器
     * */
    $scope.itemArr = {
    	isChooseAllItem: false,
    	oldChooseItemArr: [],
        currChoosedItemArr: [],
        //选中choosedItemArr中的项           
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true); 
            for (var i in $scope.itemArr.currChoosedItemArr) {
            	for(var j in $scope.page.data){
            		if($scope.page.data[j].examPaperId)
            		{
            			if($scope.page.data[j].examPaperId==$scope.itemArr.currChoosedItemArr[i].examPaperId)
            			{
               			 	$scope.page.data[j].checked = true;
               		 	}
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
                	if(item.examPaperId==$scope.itemArr.currChoosedItemArr[i].examPaperId)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                	}
                }
            } 
            else 
            {
                item.checked = true;
                $scope.itemArr.currChoosedItemArr.push(item);
                //$scope.$parent.$parent.taskPackage.objectList= $scope.itemArr.currChoosedItemArr;
            }
        },
        //全选
        chooseAllItem : function(){
        	//...
        },
        //编辑页面数据回显
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true); 
        	if ($scope.page.data) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	if($scope.itemArr.oldChooseItemArr[i].examPaperId){ 
		            	var examPaperId = $scope.itemArr.oldChooseItemArr[i].examPaperId;
		            	for (var j in $scope.page.data) 
		            	{
		            		if($scope.page.data[j])
		            		{
		            			if($scope.page.data[j].examPaperId==examPaperId)
		            			{
		            				if($scope.$parent.$parent.itemArr.choosedItemArr.length<1)
		            				{
		            					$scope.$parent.$parent.itemArr.choosedItemArr.push($scope.page.data[j]);
		            					continue;
		            				}
		            				var isContained = false;
		            				for(var k in $scope.$parent.$parent.itemArr.choosedItemArr)
		            				{
		            					if($scope.$parent.$parent.itemArr.choosedItemArr[k].examPaperId==$scope.page.data[j].examPaperId)
		            					{
		            						isContained = true;
		            						break;
	    	            				} 
		            				}
		            				if(!isContained)
		            				{
		            					$scope.$parent.$parent.itemArr.choosedItemArr.push($scope.page.data[j]);
		            				}
	    		            	}
		            		}
			            }
	            	}
	            }
            }
        	$scope.$emit('isLoading', false); 
        }
    }
    $scope.buildExamIds = function () {
        var examsIdArry = [];
        for (var i in $scope.$parent.$parent.itemArr.choosedItemArr) {
        	if($scope.$parent.$parent.itemArr.choosedItemArr[i].examPaperId){
        		examsIdArry.push($scope.$parent.$parent.itemArr.choosedItemArr[i].examPaperId);
        	}
        }
        $scope.$parent.$parent.formData.examPaperId = examsIdArry;
    }
    $scope.doSure = function () {
        $scope.$parent.$parent.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        //每次点击确定按钮，生成新的  exam
        $scope.buildExamIds();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭按钮
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }       
}]);
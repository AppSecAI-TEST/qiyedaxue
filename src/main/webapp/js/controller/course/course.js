/**
 * Created by Mr.xiang on 2016/3/26.
 */

angular.module('ele.course', ['ele.orgGroup','ele.lecture','ele.admin','ele.courseClassify','ele.gradeDimension', 'summernote'])
//课程列表
.controller('CourseController', ['$scope', '$http', '$timeout','dialogService', function ($scope, $http , $timeout, dialogService) {
    //用于显示面包屑状态
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-d', cmn: '课程列表'};
    //http请求url
    $scope.url = "/enterpriseuniversity/services/course/findPage?pageNum=";
    $scope.online = [
        {name: "线上课程", value: "Y"},
        {name: "线下课程", value: "N"}
    ];
    $scope.status = [
        {name: "已上架", value: "Y"},
        {name: "已下架", value: "N"}
    ];
    $http.get("/enterpriseuniversity/services/courseClassify/findHighest").success(function (response) {
        $scope.type = response;
    }).error(function () {
		dialogService.setContent("课程分类下拉菜单初始化错误").setShowDialog(true);
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
        	$scope.$emit('isLoading', true);
            if ($scope.courseName != undefined && $scope.courseName != "") 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&courseName=" 
            		+ $scope.courseName.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            if ($scope.selectedOnline != undefined) 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&courseOnline=" + $scope.selectedOnline;
            }
            if ($scope.selectedStatus != undefined) 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&status=" + $scope.selectedStatus;
            }
            if ($scope.selectedType != undefined) 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&courseType=" + $scope.selectedType;
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                for(var i in $scope.page.data){
                	var item = $scope.page.data[i];
                	if(item&&item.courseOnline!='Y'){
                		$scope.page.data[i].signInTwoDimensionCode = item.signInTwoDimensionCode.replace(/\%/g,"%25").replace(/\#/g,"%23")
	            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
	            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
                		$scope.page.data[i].gradeTwoDimensionCode = item.gradeTwoDimensionCode.replace(/\%/g,"%25").replace(/\#/g,"%23")
	            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
	            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
                	}
                }
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
    $scope.highLightCurrRow = function(course){
    	$scope.currHighLightRow.courseId = course.courseId; 
    }
    //上/下架按钮
    $scope.doUpdate = function (course) {
    	/*
    	 * setContent 提示内容   必须设置  
    	 * setShowDialog(true) 设置弹出对话框      必须设置为true 
    	 * setHasNextProcess(true)   可选            默认为false  默认没有需要执行的后续代码
    	 * doNextProcess 指定点击确认按钮后需要执行的后续函数                     当setHasNextProcess(true)设置为true时必须设置
    	 * setShowCancelButton(true) 设置是否显示取消按钮    可选      默认为不显示 
    	 * */
    	dialogService.setContent(course.status == "Y"?"确定下架课程?":"确定上架课程?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
			 $http({
			        url: "/enterpriseuniversity/services/course/updateStatus?courseId=" + course.courseId + "&status=" + (course.status == "Y" ? "N" : "Y"),
			        method: "PUT"
			    }).success(function (response) {
			    	//延迟弹框
			    	$timeout(function(){
			    		if(response.result=="inActivity"){
			    			//dialogService.setContent("不能下架活动中的课程").setShowDialog(true);
			    			dialogService.setContent("不能下架活动中关联的课程").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click(); 
	        				},2000);
			    		}else if(response.result=="protected"){
			    			//dialogService.setContent("不能下架轮播图中的课程").setShowDialog(true);
			    			dialogService.setContent("不能下架轮播图中关联的课程").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click(); 
	        				},2000);
			    		}else{
			    			/*dialogService.setContent(course.status == "Y"?"下架成功":"上架成功").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
			    				$scope.paginationConf.onChange();
		    				}*/
			    			dialogService.setContent(course.status == "Y"?"下架成功!":"上架成功!").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click(); 
	        					$scope.paginationConf.onChange();
	        				},2000);
			    		}
			    	},200);
			    }).error(function(){
			    	$timeout(function(){
			    		//dialogService.setContent("网络异常,请重新登录后再试").setShowDialog(true);
			    		dialogService.setContent("网络异常,请重新登录后再试").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        				},2000);
			    	},200);
			    });
    	};
    }
    /*//下载签到二维码
    $scope.downLoadSignInTwoDimensionCode = function(course){
    	
    }
    //下载评分二维码
    $scope.downLoadGradeTwoDimensionCode = function(course){
    	
    }*/
    //添加课程按钮
    $scope.doAdd = function(){
        $scope.go('/course/addCourse','addCourse',null);
    }
    //编辑课程按钮
    $scope.doEdit = function(course){
        $scope.go('/course/editCourse','editCourse',{id:course.courseId});
    }
    
   
    
    
}])
//课程评论列表模态框控制器
.controller('CourseCommentListController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout ,dialogService, dialogStatus) {
	$scope.openModalDialog = false;
    $scope.url = "/enterpriseuniversity/services/courseComment/findPage?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 15,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	if(!$scope.courseId){
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "&courseId="+$scope.courseId;
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
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
    //弹出评论模态框
    $scope.$parent.$parent.viewCourseComment = function(courseId){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.courseId = courseId;
    	$scope.search(); 
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code;
    	$scope.currHighLightRow.createTime = item.createTime; 
    }
    //禁用评论
    $scope.doDisable = function (comment) {
    	dialogService.setContent(comment.status == "Y"?"确定要禁用当前课程评论吗？":"确定要启用当前课程评论吗？").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true).doNextProcess = function(){ 
	        $http({
	            url: "/enterpriseuniversity/services/courseComment/updateStatus?courseCommentId=" + comment.courseCommentId + "&status=" + (comment.status == "Y" ? "N" : "Y"),
	            method: "PUT"
	        }).success(function (response) {
	        	$timeout(function(){
	        		dialogService.setContent(comment.status == "Y"?"评论已禁用":"评论已启用").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){ $scope.search();};
	        	},200);
	        }).error(function(response,state){
	        	$timeout(function(){
	        		if(state==401||state==403){
		        		dialogService.setContent(comment.status == "Y"?"用户权限不足,无法禁用当前评论":"用户权限不足,无法启用当前评论").setShowDialog(true);
		        	}else{
		        		dialogService.setContent(comment.status == "Y"?"评论禁用失败,请稍后重试":"评论启用失败,请稍后重试").setShowDialog(true);
		        	}
	        	},200);
	        });
    	}
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    } 
}])
/*
 * 编辑课程控制器
 * */
.controller('EditCourseController', ['$scope', '$http', '$state', '$stateParams', '$q', '$timeout', 'dialogService', function ($scope, $http, $state, $stateParams, $q, $timeout, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-d', cmn: '课程列表', gcmn: '编辑课程'}; 
	 $scope.$on("$RebuiltEmps", function(){
 		$scope.formData.tempEmps = 
 			$scope.formData.empIds.concat($scope.formData.empIds.deptIds, $scope.formData.empIds.labelClassIds, $scope.formData.empIds.labelIds, $scope.formData.empIds.labelEmps);
	 });
	$scope.deferred = $q.defer();
    if ($stateParams.id){
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/course/find?courseId=" + $stateParams.id).success(function (data) {
        	$scope.deferred.resolve(data);
    	}).error(function(data){
    		$scope.deferred.reject(data); 
    	});
    }else{
        //未传参数
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.$emit('isLoading', false);
    	$scope.course = $scope.formData = data; 
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("页面数据初始化异常").setShowDialog(true);
    });  
    //切换线上/线下课程   单选按钮
    $scope.beCourseOnline = function(course){
    	if (course.courseOnline == "N"){
    		//线下课程清空学习人员
    		$scope.cleanChoosedEmpData();
        }else{
    		//线上课程清空讲师数据   需要重新单选讲师
    		$scope.cleanChoosedLectureData();
    	}
    }
    
    //删除章节
    var spliceByKeyHandler = function(section, sectionsArr){
    	var temp;
    	for(var i in sectionsArr){
    		temp = sectionsArr[i];
    		if(temp.key == section.key){
    			sectionsArr.splice(i, 1);
    			break;
    		}
    	}
    }
    $scope.urlCourseSectionArr = [];
    $scope.summernoteCourseSectionArr = [];
    //新增章节
    $scope.$on("$AddCourseSection",function(e, section){
    	e.stopPropagation();
    	$scope.formData.sectionList.push(section);
    })
    $scope.$on("$DeleteCourseSection",function(e, section){
    	e.stopPropagation();
    	spliceByKeyHandler(section, $scope.formData.sectionList);
    })
    //20161209 add s  上传url类型课程章节
    $scope.$on("$AddUrlSection",function(e, section){
    	e.stopPropagation();
    	$scope.urlCourseSectionArr.push(section);
    	$scope.$emit("$AddCourseSection", section);
    });
    $scope.$on("$RemoveUrlSection",function(e, section){
    	e.stopPropagation();
    	spliceByKeyHandler(section, $scope.urlCourseSectionArr);
    	spliceByKeyHandler(section, $scope.formData.sectionList);
    });
    
    $scope.$on("$AddSummernoteSection",function(e, section){
    	e.stopPropagation();
    	$scope.summernoteCourseSectionArr.push(section);
    	$scope.$emit("$AddCourseSection", section);
    });
    $scope.$on("$RemoveSummernoteSection",function(e, section){
    	e.stopPropagation();
    	spliceByKeyHandler(section, $scope.summernoteCourseSectionArr);
    	spliceByKeyHandler(section, $scope.formData.sectionList);
    	$('#' + section.key).summernote('destroy');
    });
    
    $scope.$on("initSummernoteCourseSectionArr",function(e, summernoteCourseSectionArr){
    	e.stopPropagation();
    	$scope.summernoteCourseSectionArr = summernoteCourseSectionArr.concat();
    });
    $scope.$on("initUrlCourseSectionArr",function(e, urlCourseSectionArr){
    	e.stopPropagation();
    	$scope.urlCourseSectionArr = urlCourseSectionArr.concat();
    });
    
    $scope.$on("$UpdateSectionType",function(e, section){
    	e.stopPropagation();
    	if(section.formData){
    		var temp ;
        	for(var i in $scope.formData.sectionList){
        		temp = $scope.formData.sectionList[i];
        		if(temp.key == section.formData["key"]){
        			temp.sectionType = section.formData.sectionType;
        			break;
        		}
        	}
    	}
    });
    
    $scope.courseBak = {};
    //20161209 add e
    
    $scope.editCourseSubmit=false;
    //提交表单   
    $scope.doSave = function() {
		 $scope.courseForm.$submitted = true;
		 if($scope.courseForm.$invalid)
	   	 {
    		dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
			return;
	   	 }
		 //线下课程不指定学习人员
		 if($scope.course.courseOnline=="N"||$scope.course.openCourse=="Y")
    	 {
    		 $scope.course.empIds = [];
    		 $scope.course.empNames = [];
    	 }
		 $scope.editCourseSubmit=true;
		//20161209 add s
		 //备份
		 angular.copy($scope.formData, $scope.courseBak);
		 //添加章节数据
		 //pushSectionInfoData();
		 //20161209 add e
    	 //调接口保存表单数据
		 $http({
			method : 'POST',
			url  : '/enterpriseuniversity/services/course/edit',
			data : $scope.course, 
			headers : { 'Content-Type': 'application/json' }
		 })
		 .success(function(data) {
			 $scope.editCourseSubmit=false;
			 if(data.result=="inActivity"){
				 dialogService.setContent("不能修改活动中课程！").setShowDialog(true);
			 }else{
				 dialogService.setContent("保存课程成功！").setShowSureButton(false).setShowDialog(true);
				 $timeout(function(){
					 dialogService.sureButten_click(); 
					 $scope.go('/course/courseList','courseList',null);
				 },2000);
			 }
		 })
		 .error(function(){
			 $scope.editCourseSubmit=false;
			 //20161209 add s
			 //回滚数据
			 angular.copy($scope.courseBak, $scope.formData);
			 //20161209 add e
			 dialogService.setContent("保存课程失败！").setShowSureButton(false).setShowDialog(true); 
			 $timeout(function(){
				 dialogService.sureButten_click(); 
			 },2000);
		 }); 
	};
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/courseList','courseList',null);
    };
    
}])
/*
 * 添加课程
 * */
.controller('AddCourseController', ['$scope', '$http', '$state', '$timeout', 'dialogService', function ($scope, $http, $state , $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-d', cmn: '课程列表' , gcmn: '添加课程'};
    $scope.course = $scope.formData = {
    		//初始化容器及单选按钮选中状态
    		sectionList : [],
    		classifyIds : [],
    		empIds:[],
    		deptIds:[],
    		labelClassIds:[],
    		labelIds:[],
    		labelEmps:[],
    		courseOnline:'Y',
			courseDownloadable:'Y',
			isExcellentCourse:'Y',
			status:'Y',
			openComment:'Y',
			openCourse:'N',
			tempEmps : []
    };
    $scope.$on("$RebuiltEmps", function(){
    		$scope.formData.tempEmps = 
    			$scope.formData.empIds.concat($scope.formData.empIds.deptIds, $scope.formData.empIds.labelClassIds, $scope.formData.empIds.labelIds, $scope.formData.empIds.labelEmps);
    });
    //切换线上/线下课程  执行函数
    $scope.beCourseOnline = function(course){
    	if (course.courseOnline == "N"){
    		$scope.cleanChoosedEmpData();
        }else{
    		$scope.cleanChoosedLectureData();
    	}
    }
    ///////////////////////////////////////////////////////////////////////////20170524
    //删除章节
    var spliceByKeyHandler = function(section, sectionsArr){
    	var temp;
    	for(var i in sectionsArr){
    		temp = sectionsArr[i];
    		if(temp.key == section.key){
    			sectionsArr.splice(i, 1);
    			break;
    		}
    	}
    }
    $scope.urlCourseSectionArr = [];
    $scope.summernoteCourseSectionArr = [];
    //新增章节
    $scope.$on("$AddCourseSection",function(e, section){
    	e.stopPropagation();
    	$scope.formData.sectionList.push(section);
    })
    $scope.$on("$DeleteCourseSection",function(e, section){
    	e.stopPropagation();
    	spliceByKeyHandler(section, $scope.formData.sectionList);
    })
    //20161209 add s  上传url类型课程章节
    $scope.$on("$AddUrlSection",function(e, section){
    	e.stopPropagation();
    	$scope.urlCourseSectionArr.push(section);
    	$scope.$emit("$AddCourseSection", section);
    });
    $scope.$on("$RemoveUrlSection",function(e, section){
    	e.stopPropagation();
    	spliceByKeyHandler(section, $scope.urlCourseSectionArr);
    	spliceByKeyHandler(section, $scope.formData.sectionList);
    });
    
    $scope.$on("$AddSummernoteSection",function(e, section){
    	e.stopPropagation();
    	$scope.summernoteCourseSectionArr.push(section);
    	$scope.$emit("$AddCourseSection", section);
    });
    $scope.$on("$RemoveSummernoteSection",function(e, section){
    	e.stopPropagation();
    	spliceByKeyHandler(section, $scope.summernoteCourseSectionArr);
    	spliceByKeyHandler(section, $scope.formData.sectionList);
    	$('#' + section.key).summernote('destroy');
    });
    
    $scope.$on("$UpdateSectionType",function(e, section){
    	e.stopPropagation();
    	if(section.formData){
    		var temp ;
        	for(var i in $scope.formData.sectionList){
        		temp = $scope.formData.sectionList[i];
        		if(temp.key == section.formData["key"]){
        			temp.sectionType = section.formData.sectionType;
        			break;
        		}
        	}
    	}
    });
    
    $scope.courseBak = {};
    //20161209 add e
    
    $scope.addCourseSubmit=false;
    //提交表单   
    $scope.doSave = function() {
    	 $scope.courseForm.$submitted = true;
		 if($scope.courseForm.$invalid){
    		dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
   		 	return;
	   	 }
    	 //线下课程不指定学习人员
		 if($scope.course.courseOnline=="N"||$scope.course.openCourse=="Y"){
    		 $scope.course.empIds = [];
    		 $scope.course.empNames = [];
    		 $scope.course.deptIds = [];
    		 $scope.course.labelClassIds = [];
    		 $scope.course.labelIds = [];
    		 $scope.course.labelEmps = [];
    	 }
		 $scope.addCourseSubmit=true;
		 //20161209 add s
		 //备份
		 angular.copy($scope.formData, $scope.courseBak);
		 //添加章节数据
		 //pushSectionInfoData();
		 //20161209 add e
		 //调接口保存表单数据
		 $http({
			 method : 'POST',
			 url  : '/enterpriseuniversity/services/course/add',
			 data : $scope.course,  
			 headers : { 'Content-Type': 'application/json' }  
		 })
		 .success(function(data) {
			 $scope.addCourseSubmit=false;
			 dialogService.setContent("新增课程成功！").setShowSureButton(false).setShowDialog(true);
			 $timeout(function(){
				 dialogService.sureButten_click(); 
				 $scope.go('/course/courseList','courseList',null);
			 },2000);
		 })
		 .error(function(){
			 $scope.addCourseSubmit=false;
			 //20161209 add s
			 //回滚数据
			 angular.copy($scope.courseBak, $scope.formData);
			 //20161209 add e
			 dialogService.setContent("新增课程失败！").setShowSureButton(false).setShowDialog(true); 
			 $timeout(function(){
				 dialogService.sureButten_click(); 
			 },2000);
		 }); 
	};
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/courseList','courseList',null);
    };
}])
//表单验证
.directive('requiredCourseimg', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = (value!=undefined&&value!="") ? false : true;
	                  ngModel.$setValidity("requiredCourseimg", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
.directive('requireString', [function () {//?
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = (value!=undefined&&value!="") ? (value.split(",").length < 1 ? true : false) : true;
	                  ngModel.$setValidity("requireString", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
.directive('requiredLecturers', [function () {
	return {
       require: "ngModel",
       link: function (scope, element, attr, ngModel) {
           var customValidator = function (value) {
        	   var validity = null;
        	   scope.$watch(function () {
                   return value;
               }, function(){
            	   validity = (value!=undefined&&value!="") ? (value.substr(1,value.length-2).split(",").length < 1 ? true : false):true;
                   ngModel.$setValidity("requiredLecturers", !validity);
               });
               return !validity ? value : undefined;
           };
           ngModel.$formatters.push(customValidator);
           ngModel.$parsers.push(customValidator);
        }
    };
}])
//表单验证
.directive('requireNumber', [function () {
	return {
		require: "ngModel",
		link: function (scope, element, attr, ngModel) {
			var customValidator = function (value) {
				var validity = null;
				scope.$watch(function () {
					return value;
				}, function(){
					validity = parseInt(value) < 1 ? true : false;
					ngModel.$setValidity("requireNumber", !validity);
				});
				return !validity ? value : undefined;
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])
//表单验证
.directive('requiredSections', [function () {
	return {
		require: "ngModel",
		link: function (scope, element, attr, ngModel) {
			var customValidator = function (value) {
				var validity = isNaN(value) || parseInt(value) < 1 ? true : false;
				ngModel.$setValidity("requiredSections", !validity);
				return value ;
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])
//验证未上传完毕的课程章节
.directive('sectionLength', [function () {
	return {
		require: "ngModel",
		link: function (scope, element, attr, ngModel) {
			var customValidator = function (value) {
				scope.$watch(function () {
					return (scope.formData ? scope.formData.sectionList ? scope.formData.sectionList.length : 0 : 0) - value;
				}, function(newValue, oldValue){
					//validity = scope.formData ? scope.formData.sectionList ? (scope.formData.sectionList.length - newValue) < 0 ? true : false : false : false; 
					validity = newValue < 0 ? true : false; 
					ngModel.$setValidity("sectionLength", !validity);
				});
				return value ; 
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])
.directive('requireEmps', [function () {//?
  return {
      require: "ngModel",
      link: function (scope, element, attr, ngModel) {
          var customValidator = function (value) {
        	  var validity = null;
        	  scope.$watch(function () {
        		  if(scope.course){
        			  return value + scope.course.courseOnline + scope.course.openCourse;
        		  }else{
        			  return value;
        		  }
              }, function(){
            	  validity = parseInt(value) < 1 ? true : false;
            	  if(scope.course&&scope.course.courseOnline=="N"||scope.course&&scope.course.openCourse=="Y"){
            		  validity = false;
            	  }
                  ngModel.$setValidity("requireEmps", !validity);
              });
              return !validity ? value : undefined;
          };
          ngModel.$formatters.push(customValidator);
          ngModel.$parsers.push(customValidator);
      }
  };
}])
.directive('requiredCourse', [function () {
	return {
	  require: "ngModel",
	  link: function (scope, element, attr, ngModel) {
		  var customValidator = function (value) {
			  var validity = null;
			  scope.$watch(function () {
				  return value ? value.length : 0;
		      }, function(newV, oldV){
	    		  validity = newV ? newV < 1  ? true : false : true;
	    		  ngModel.$setValidity("requiredCourse", !validity);
	          });
			  return  value;
	      };
	      ngModel.$formatters.push(customValidator);
	      ngModel.$parsers.push(customValidator);
	  }
	};
}])
// 课程章节文件上传控制器
.controller('CourseSectionFileUploadController', ['$scope','$http', 'FileUploader', '$timeout', 'dialogService', function ($scope,$http, FileUploader , $timeout , dialogService) {
	var uploader = $scope.uploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/course',
            autoUpload:true
        }
    );
    uploader.filters.push({
        name: 'customFilter',
        fn: function (item, options) {
        	var type = '';
        	if(item.type==''){
        		type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
        	}else{
        		type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
        	}
        	var currFileCount = 0;
        	//支持mp4、ppt、pdf、word格式的文件和zip压缩文件
        	if('|mp3|mp4|vnd.ms-powerpoint|vnd.openxmlformats-officedocument.presentationml.presentation|pdf|zip|vnd.openxmlformats-officedocument.wordprocessingml.document|msword|x-zip-compressed|x-mpeg|mpeg|'.indexOf(type) == -1)
        	{
        		dialogService.setContent("上传文件格式不符合要求").setShowDialog(true);
        		return false;
        	}
        	if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0){
        		currFileCount = $scope.currFileUrlQueue.length;
        	}
        	/*if((currFileCount+this.queue.length) >= 10){
        		dialogService.setContent("同一课程最多可上传10个课程章节文件").setShowDialog(true);
        		return false;
        	}*/
        	//过滤相同文件
        	for(var i in this.queue){
        		if(this.queue[i]._file.name == item.name
        				&&new Date(this.queue[i]._file.lastModifiedDate).getTime()==new Date(item.lastModifiedDate).getTime()
        				&&this.queue[i]._file.size == item.size
        				&&this.queue[i]._file.type == item.type){
        			dialogService.setContent("上传课程章节文件重复").setShowDialog(true);
        			return false;
        		}
        	}
            return true;
        }
    });
    //20161209 add s 
    $scope.mouseenter = function(){
    	$scope.sectionFileInputIsHover = true;
    }
    $scope.mouseleave = function(){
    	$scope.sectionFileInputIsHover = false;
    }
    //url链接
    $scope.addUrlCourseSection = function(){
    	$scope.$emit("$AddUrlSection", {
    		"key": new Date().getTime(),
    		"sectionSort" : "",
     		"sectionId" : -1,// -1为新增  约定	
     		"sectionName" :  "",	
     		"sectionAddress" : "" ,	
     		"sectionType" : "7",
     		"sectionSize" : "",
     		"sectionDuration" : "",
     		"sectionDetails" : ""
         });
    }
    $scope.removeUrlCourseSection = function(section){
    	$scope.$emit("$RemoveUrlSection", section);
    }
    //富文本
    $scope.addSummernoteCourseSection = function(){
    	$scope.$emit("$AddSummernoteSection", {
    		"key" : new Date().getTime(),
    		"sectionSort" : "",
    		"sectionId" : -1,
    		"sectionType" : "8",
    		"sectionName" : "",
    		"sectionAddress" : "",
    		"sectionSize" : "",
    		"sectionDuration" : "",
    		"sectionDetails" : "",
    		"init" : function(){
    			$('#summernote' + this.key).summernote({
    				  height: 300,                 // set editor height
    				  minHeight: null,             // set minimum height of editor
    				  maxHeight: null,             // set maximum height of editor
    				  focus: true ,                 // set focus to editable area after initializing summernote,
    				  lang: 'en-US' // default: 'en-US'
				});
    		}
    	});
    }
    $scope.removeSummernoteCourseSection = function(section){
    	$scope.$emit("$RemoveSummernoteSection", section);
    }
    //20161209 add e
    //编辑页面数据初始化
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currFileUrlQueue = [];
    		var summernoteCourseSectionArr = [];
    		var urlCourseSectionArr = [], temp;
        	if(true)
        	{
        		for(var i = 0; i< $scope.formData.sectionList.length; i++)
        		{
        			temp = $scope.formData.sectionList[i];
        			//富文本章节
        			temp["key"] = new Date().getTime() + i;
        			if(temp["sectionType"] == "8"){
        				temp["init"] = function(){
    		    			$('#summernote' + this.key).summernote({
    		    				height: 300,                 // set editor height
    		    				minHeight: null,             // set minimum height of editor
    		    				maxHeight: null,             // set maximum height of editor
    		    				focus: true ,                 // set focus to editable area after initializing summernote,
    		    				lang: 'en-US' // default: 'en-US'
	  						});
	  		    		}
        				summernoteCourseSectionArr.push(temp);
        			} else if(temp["sectionType"] == "7"){
        				urlCourseSectionArr.push(temp);
        			} else{
        				$scope.currFileUrlQueue.push(temp); 
        			}
        		}
        		if(summernoteCourseSectionArr.length > 0){
        			$scope.$emit("initSummernoteCourseSectionArr", summernoteCourseSectionArr);
        		}
        		if(urlCourseSectionArr.length > 0){
        			$scope.$emit("initUrlCourseSectionArr", urlCourseSectionArr);
        		}
        	}
        }, function(data) {
            
        }); 
    } 
    //删除之前上传的文件路径集合   currFileUrlQueue
    $scope.removeCurrItem = function (item) {
        $scope.currFileUrlQueue.splice($scope.currFileUrlQueue.indexOf(item), 1);
        $scope.$emit("$DeleteCourseSection", item);
    }
    //zip文件区分h5和PC
    $scope.toggleType = function(type, item){
    	if(type=='PC'){
    		//$scope.$parent.formData.sectionType[$index] = 5;
    		item.formData.sectionType = 5;
    	}else{
    		//$scope.$parent.formData.sectionType[$index] = 4;
    		item.formData.sectionType = 4;
    	}
    	$scope.$emit("$UpdateSectionType", item);
    }
    //删除新上传的文件路径集合 
    $scope.removeItem = function (item) {
    	if(item.formData && item.formData.sectionAddress){
    		$http.get("/enterpriseuniversity/services/file/deleteFile?path=" + item.sectionInfo.sectionAddress);
    	}
    	//从queue中删除
        item.remove();
        //删除 上传成功的文件地址及其他数据
        if(item.isUploaded){
        	$scope.$emit("$DeleteCourseSection", item.formData);
        }
    }
    $scope.uploadItem = function(item){
    	if(!(item.isReady || item.isUploading || item.isSuccess)){
    		 item.upload();
    	}
    }
    uploader.onAfterAddingFile = function (fileItem) {
    	//特殊文件类型判断
    	var fileType = fileItem.file.type.slice(fileItem.file.type.lastIndexOf('/') + 1),
    	sectionType;
    	if(fileType == ""){
    		//zip
    		fileType = fileItem.file.name.slice(fileItem.file.name.lastIndexOf('.') + 1);
    	}
    	if(fileType == "XXX"){
    		sectionType = "XXX";
    	}
    	//初始化参数 
    	fileItem.formData = {
    		key : new Date().getTime(),
			sectionSort : null,
    		sectionId :-1,
			sectionSize : null,
			sectionName : null,
			sectionNameEn : null,
			sectionType : sectionType,
			lang : null,
			sectionAddress : null,
			fileUrl : null,
			sectionDuration : null,
			sectionDetails : ""
    	};
    };
    //上传文件回调函数  回调成功设置表单数据 
    uploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        //回调失败处理
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("文件解析异常,获取上传文件地址失败").setShowDialog(true);
        }
        else
        {//回调成功
        	var sectionType = fileItem.file.type.slice(fileItem.file.type.lastIndexOf('/') + 1);
        	if(sectionType==''){
        		//zip
        		sectionType = fileItem.file.name.slice(fileItem.file.name.lastIndexOf('.') + 1);
        	}
        	if(sectionType=="mp4"){
        		sectionType = 1;
        	}
        	if(sectionType=="vnd.ms-powerpoint"||sectionType=="vnd.openxmlformats-officedocument.presentationml.presentation"||sectionType=="vnd.ms-word"||sectionType=="vnd.openxmlformats-officedocument.wordprocessingml.document"||sectionType=="msword"){
        		sectionType = 2;
        	}
        	if(sectionType=="pdf"){
        		sectionType = 3;
        	}
        	if(sectionType=="zip"||sectionType=="x-zip-compressed"){
        		sectionType = 4;
        	}
        	if(sectionType=="x-mpeg" || sectionType=="mpeg"){
        		sectionType = 6;
        	}
        	if(sectionType=="mp3"){
        		sectionType = 9;
        	}
        	var courseSection = {
        		"key": new Date().getTime(),
         		"sectionId" : -1,// -1为新增  约定	
         		"sectionName" : fileItem.file.name.slice(0, fileItem.file.name.lastIndexOf('.')),	
         		"sectionAddress" : response.url,	
         		"sectionType" : fileItem.sectionType != undefined ? fileItem.sectionType : sectionType,
         		"sectionSize" : response.num,
         		"sectionDuration" : sectionType == 1 ? response.duration : null,
         		"sectionDetails" : ""
         	};
        	//fileItem.sectionInfo = courseSection;
        	fileItem.formData.sectionName = courseSection.sectionName;
        	fileItem.formData.sectionSize = courseSection.sectionSize;
        	fileItem.formData.sectionAddress = courseSection.sectionAddress;
        	fileItem.formData.sectionType = courseSection.sectionType;
    		fileItem.formData.sectionDuration = courseSection.sectionDuration;
    		fileItem.formData.sectionDetails = courseSection.sectionDetails;
        	
         	$scope.$emit("$AddCourseSection", fileItem.formData);
        }
    }; 
}])
//上传课程封面图片控制器
.controller('CourseImgController', ['$scope', 'FileUploader', '$timeout', 'dialogService','$http', function ($scope, FileUploader, $timeout, dialogService,$http) {
    var imgUploader = $scope.imgUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/courseImg',
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
            	dialogService.setContent("要上传新的课程封面图片，请先删除旧的课程封面图片").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("超出上传文件数量限制").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|'.indexOf(type) == -1)
            {
                dialogService.setContent("上传文件格式不符合要求").setShowDialog(true);
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
    //编辑页面课程封面图片文件地址
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currFileUrlQueue =[]; 
    		if($scope.formData.courseImg&&$scope.formData.courseImg!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.courseImg);
    		}
        }, function(data) {}); 
    } 
    //修改课程封面图片
    $scope.buildCourseImg = function(imgUrl){
		$scope.$parent.formData.courseImg = imgUrl;
    }
    //预览
    $scope.openPreview = false;
    $scope.preview = function(){
    	if($scope.$parent.formData.courseImg&&$scope.$parent.formData.courseImg!="")
    	{
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.$parent.formData.courseImg;
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
        $scope.buildCourseImg("");
    }
    //删除 
    $scope.removeItem = function (item) {
    	if(item.fileUrl!=null){
    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
    	}
        item.remove();
        document.getElementById("courseImgFileInput").value=[];
        $scope.buildCourseImg(""); 
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
        	dialogService.setContent("获取上传文件地址失败").setShowDialog(true);
        }
        else
        {
        	fileItem.fileUrl=response.url;
        	$scope.buildCourseImg(response.url);//接收返回的文件地址
        }
    };
}]);
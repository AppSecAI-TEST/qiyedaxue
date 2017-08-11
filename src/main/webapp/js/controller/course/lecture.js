/**
 * Created by Mr.xiang on 2016/3/26.
 */
angular.module('ele.lecture', [])
//讲师列表
.controller('LectureController', ['$scope', '$http', '$timeout', 'dialogService', function ($scope, $http, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-b', cmn: '讲师管理'};
    $scope.url = "/enterpriseuniversity/services/lecturer/findPage?pageNum=";
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
            if ($scope.lecturerName != undefined && $scope.lecturerName != "") {
            	$scope.$httpPrams = $scope.$httpPrams+"&lecturerName=" 
            		+ $scope.lecturerName.replace(/\%/g,"%25").replace(/\#/g,"%23")
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
    	$scope.paginationConf.itemsPerPage = 20 ;
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
        $scope.go('/course/addLecture','addLecture',null);
    }
    //查看课程
    $scope.doView = function(lecture){
    	$scope.go('/course/lectureCourseList', 'lectureCourseList', {id: lecture.lecturerId});
    }
    // 删除讲师
    $scope.doDelete = function (lecture) {
    	dialogService.setContent("确定删除讲师?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
	        $http({
	            url: "/enterpriseuniversity/services/lecturer/delete?lecturerId=" + lecture.lecturerId+"&status="+(lecture.status == "Y" ? "N" : "Y"),
	            method: "DELETE"
	        }).success(function (response) {
	        	$timeout(function(){
		        	if(response.result=="success"){
		        		//延迟弹框
				    	/*$timeout(function(){
				    		dialogService.setContent("成功删除讲师").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.search();}
				    	},200);*/
				    	dialogService.setContent("删除讲师成功！").setShowSureButton(false).setShowDialog(true);
	    	    		$timeout(function(){
	    					dialogService.sureButten_click(); 
	    					$scope.lecturerName = "";
			    			$scope.search(); 
	    				},2000);
		        	}else if(response.result=="protected"){
			    		//dialogService.setContent("讲师名下有课程,不能删除讲师").setShowDialog(true);
			    		dialogService.setContent("讲师名下有课程,不能删除讲师！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
		        	}
	        	},200);
	        }).error(function(){
				$timeout(function(){
		    		//dialogService.setContent("网络异常,删除讲师失败").setShowDialog(true);
		    		dialogService.setContent("网络异常,删除讲师失败！").setShowSureButton(false).setShowDialog(true);
		    		$timeout(function(){
						dialogService.sureButten_click(); 
					},2000);
		    	},200);
			});
    	}
    }
    //编辑
    $scope.doEdit = function (lecture) {
        $scope.go('/course/editLecture', 'editLecture', {id: lecture.lecturerId});
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(lecture){
        $scope.currHighLightRow.lecturerId = lecture.lecturerId; 
    } 
}])
//添加讲师表单控制器
.controller('AddLectureController', ['$scope', '$http', '$timeout', 'dialogService', function ( $scope, $http, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-b', cmn: '讲师管理', gcmn: '添加讲师'};
    $scope.lecturer = $scope.formData = {
    		isInternalLecturer:'N'
	};
    $scope.lecturerGrades = 
    	[
            {name : "金牌讲师",value:"G"},
            {name : "银牌讲师",value:"S"},
            {name : "铜牌讲师",value:"C"}
        ]; 
    $scope.lecturerTypes = 
    	[
            {name : "全职",value:"Y"},
            {name : "兼职",value:"N"} 
        ]; 
    $scope.addLecturerSubmit=false;
    //提交表单   
    $scope.doSave = function() {
    	 $scope.lectureForm.$submitted = true;
		 if($scope.lectureForm.$invalid)
	   	 {
			 dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
	   		 return;
	   	 }
		 $scope.addLecturerSubmit=true;
		 $http({
			 method : 'POST',
			 url  : '/enterpriseuniversity/services/lecturer/add',
			 data : $.param($scope.lecturer),  
			 headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
		 })
		 .success(function(data) {
			 dialogService.setContent("新增讲师成功！").setShowSureButton(false).setShowDialog(true);
			 $timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addLecturerSubmit=false;
				$scope.go('/course/lectureList','lectureList',null);
			 },2000);
		 })
		 .error(function(){
			 dialogService.setContent("新增讲师失败！").setShowSureButton(false).setShowDialog(true); 
			 $timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addLecturerSubmit=false;
			 },2000);
		 }); 
	};
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/lectureList','lectureList',null);
    };
}])
//编辑讲师表单控制器
.controller('EditLectureController', ['$scope', '$http', '$stateParams', '$q', '$timeout', 'dialogService', function ($scope, $http, $stateParams, $q, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-b', cmn: '讲师管理', gcmn: '编辑讲师'};
    $scope.lecturerGrades = 
    	[
            {name : "金牌讲师",value:"G"},
            {name : "银牌讲师",value:"S"},
            {name : "铜牌讲师",value:"C"}
        ]; 
    $scope.lecturerTypes = 
    	[
            {name : "全职",value:"Y"},
            {name : "兼职",value:"N"} 
        ]; 
    $scope.deferred = $q.defer();
    $scope.lecturer = $scope.formData = {};
    if ($stateParams.id) {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/lecturer/find?lecturerId=" + $stateParams.id)
            .success(function (data) {
            	$scope.deferred.resolve(data);
            }).error(function(data){
        		$scope.deferred.reject(data); 
        	});
    } else {
    	//未传参数
    	dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
    	return;
    }
    $scope.deferred.promise.then(function(data) {  // 查询成功
    	$scope.$emit('isLoading', false);
    	$scope.lecturer = $scope.formData = data; 
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("数据初始化异常").setShowDialog(true);
    }); 
    //提交表单   
    $scope.editLecturerSubmit=false;
    $scope.doSave = function() {
    	 $scope.lectureForm.$submitted = true;
		 if($scope.lectureForm.$invalid)
	   	 {
			 dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
	   		 return;
	   	 }
		 $scope.editLecturerSubmit=true;
		 $http({
			 method : 'PUT',
			 url  : '/enterpriseuniversity/services/lecturer/edit',
			 data : $.param($scope.lecturer),  
			 headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
		 })
		 .success(function(data) {
			  dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
			  $timeout(function(){
				  dialogService.sureButten_click(); 
				  $scope.editLecturerSubmit=false;
				  $scope.go('/course/lectureList','lectureList',null);
			  },2000);
		 })
		 .error(function(){
			 dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true); 
			 $timeout(function(){
				dialogService.sureButten_click(); 
				$scope.editLecturerSubmit=false;
			 },2000);
		 }); 
	};
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/lectureList','lectureList',null);
    };
}])
//校验选择员工
.directive('requiredEmp', [function () {
  return {
      require: "ngModel",
      link: function (scope, element, attr, ngModel) {
	      var customValidator = function (value) {
	    	  var validity = null;
	    	  var needCheck = true;
	    	  scope.$watch(function () {
	              return scope.lecturer.isInternalLecturer;
	          }, function(){
	        	  if(scope.lecturer.isInternalLecturer=="N")
	        	  {
	        		  needCheck = false; 
	        	  }
	        	  else
	        	  {
	        		  needCheck = true;
	        	  }
	          });
	    	  scope.$watch(function () {
	    		  return value+" "+needCheck;
	          }, function(){
	        	  validity = needCheck?(value!=undefined?(value.length < 1 ? true : false):true):false;
	              ngModel.$setValidity("requiredEmp", !validity);
	          });
	          return !validity ? value : undefined;
	      };
	      ngModel.$formatters.push(customValidator);
	      ngModel.$parsers.push(customValidator);
      }
  };
 }])
 //校验选择讲师级别
.directive('requiredLectureGrade', [function () {
  return {
      require: "ngModel",
      link: function (scope, element, attr, ngModel) {
          var customValidator = function (value) {
        	  var validity = null;
        	  var needCheck = true;
        	  scope.$watch(function () {
                  return scope.lecturer.isInternalLecturer;
              }, function(){
            	  if(scope.lecturer.isInternalLecturer=="N")
            	  {
            		  needCheck = false; 
            	  }
            	  else
            	  {
            		  needCheck = true;
            	  }
              });
        	   
        	  scope.$watch(function () {
        		  return value+" "+needCheck;
              }, function(){
            	  validity = needCheck?(value!=undefined?(value.length < 1 ? true : false):true):false;
                  ngModel.$setValidity("requiredLectureGrade", !validity);
              });
              return !validity ? value : undefined;
          };
          ngModel.$formatters.push(customValidator);
          ngModel.$parsers.push(customValidator);
      }
   };
}])
//校验选择讲师类型
.directive('requiredLectureType', [function () {
  return {
      require: "ngModel",
      link: function (scope, element, attr, ngModel) {
          var customValidator = function (value) {
	    	  var validity = null;
	    	  scope.$watch(function () {
	    		  return value ;
	          }, function(){
	        	  validity = value!=undefined&&value!=null?(value.length < 1 ? true : false):true;
	              ngModel.$setValidity("requiredLectureType", !validity);
	          });
	    	  return !validity ? value : undefined;
          };
          ngModel.$formatters.push(customValidator);
          ngModel.$parsers.push(customValidator);
      }
  };
}])
//校验上传讲师头像
.directive('requireLecturerimg', [function () {
  return {
      require: "ngModel",
      link: function (scope, element, attr, ngModel) {
          var customValidator = function (value) {
        	  var validity = null;
        	  scope.$watch(function () {
                  return value;
              }, function(){
            	  validity = value?(value.length < 1 ? true : false):true;
                  ngModel.$setValidity("requireLecturerimg", !validity);
              });
              return !validity ? value : undefined;
          };
          ngModel.$formatters.push(customValidator);
          ngModel.$parsers.push(customValidator);
      }
  };
}])
//上传讲师头像控制器
.controller('LectureImgController', ['$scope', 'FileUploader', '$timeout', 'dialogService','$http', function ($scope, FileUploader, $timeout, dialogService,$http) {
    var imgUploader = $scope.imgUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/lectureImg',
            autoUpload: true
        }
    );
    imgUploader.filters.push({
        name: 'imageFilter',
        fn: function(item , options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0)
            {
            	dialogService.setContent("要上传新的讲师头像图片，请先删除旧的讲师头像图片").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("只允许上传最多一张讲师头像图片").setShowDialog(true);
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
    		if($scope.formData.lecturerImg&&$scope.formData.lecturerImg!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.lecturerImg);
    		}
        }, function(data) {
        	dialogService.setContent("页面数据初始化错误").setShowDialog(true);
            return;
        }); 
    } 
    //预览
    $scope.openPreview = false;
    $scope.preview = function(){
    	if($scope.$parent.formData.lecturerImg&&$scope.$parent.formData.lecturerImg!="")
    	{
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.formData.lecturerImg;
    	}
    	else
    	{
    		dialogService.setContent("请先上传再预览").setShowDialog(true);
    	}
    }
    $scope.closePreview = function(){
    	$scope.openPreview = false;
    }
    $scope.buildLectureImg = function(imgUrl){
		$scope.$parent.formData.lecturerImg = imgUrl;
    }
    //删除fileUrlQueue中存储的文件地址
    $scope.removeCurrItem = function (item) {
        $scope.currFileUrlQueue.splice($scope.currFileUrlQueue.indexOf(item), 1);
        $scope.buildLectureImg("");
    }
    //删除新上传的文件地址
    $scope.removeItem = function (item) {
    	if(item.fileUrl!=null){
    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
    	}
        item.remove();
        document.getElementById("lectureImgInput").value=[];
        $scope.buildLectureImg(""); 
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
        	fileItem.fileUrl=response.url;
        	$scope.buildLectureImg(response.url);//接收返回的文件地址
        }
    };
}])
//查看讲师课程列表
.controller('LecturerCourseListController', ['$scope', '$http', '$stateParams', '$q', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http , $stateParams, $q, $timeout, dialogService, dialogStatus) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-b', cmn: '讲师管理', gcmn :'讲师课程'};
    /*$scope.deferred = $q.defer();
    if ($stateParams.id) 
    {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/lecturer/find?lecturerId=" + $stateParams.id)
            .success(function (data) {
            	$scope.deferred.resolve(data);
            }).error(function(data){
        		$scope.deferred.reject(data); 
        	});
    } 
    else 
    {
    	//未传参数
    	dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
    	return;
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.$emit('isLoading', false);
    	$scope.lecturer = $scope.formData = data; 
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("页面数据初始化异常").setShowDialog(true);
    }); */
    //查询讲师的课程列表
    $scope.url = "/enterpriseuniversity/services/course/findPage?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 15,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.httpParams = "";
        	if($stateParams.id)
        	{
        		$scope.httpParams = "&lecturers="+$stateParams.id;
        	}
        	else
        	{
        		$scope.page = {};
        		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
        		return;
        	}
        	$scope.$emit('isLoading', true);
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.httpParams;
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
    	$scope.paginationConf.itemsPerPage = 20 ;
    	$scope.paginationConf.onChange();
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.courseId = item.courseId;
    }
}])
//讲师评论列表模态框
.controller('LecturerCommentListController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
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
        	if(!$scope.courseId)
        	{
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "&courseId="+$scope.courseId;
        	$scope.$emit('isLoading', true);
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
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    $scope.$parent.$parent.doView = function(courseId){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.courseId = courseId;
    	$scope.search(); 
    }
    /*$scope.doDisable = function (comment) {
        $http({
            url: "/enterpriseuniversity/services/courseComment/updateStatus?courseCommentId=" + comment.courseCommentId + "&status=" + (comment.status == "Y" ? "N" : "Y"),
            method: "PUT"
        }).success(function (response) {
            $scope.search();
        }).error(function(){
        	alert("评论禁用失败,请稍后重试");
        });
    }*/
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    } 
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.courseCommentId = item.courseCommentId;
    }
}])
//选择讲师模态框
.controller('LectureModalController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
    $scope.url = "/enterpriseuniversity/services/lecturer/findPage?pageNum=";
    $scope.isInitUpdatePageData = false;
	$scope.openModalDialog = false;
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 15,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	//只在模态框即将打开/第一次初始化编辑页面时查询数据
        	if(!($scope.isInitUpdatePageData||$scope.openModalDialog)){
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
        	$scope.$emit('isLoading', true);
            if ($scope.lectureName != undefined && $scope.lectureName != "")
            {
            	$scope.$httpPrams = $scope.$httpPrams+"&lecturerName=" 
            		+ $scope.lectureName.replace(/\%/g,"%25").replace(/\#/g,"%23")
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
                //模态框中数据选中状态回显
                if ($scope.itemArr.initCurrChoosedItemArrStatus&&$scope.isBuildOldChoosedItems) {
                    $scope.itemArr.initCurrChoosedItemArrStatus();
                } 
                //生成旧的选中项  只执行一次
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
    	}
    	else
    	{
    		$scope.paginationConf.currentPage = 1;
    		//查询全部数据用于buildOldChoosedItemArr
        	$scope.paginationConf.itemsPerPage = "全部" ;
    	}
    	$scope.paginationConf.onChange();
    };
    //是否初始化旧的选项标记
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
            		 if($scope.page.data[j].lecturerId==$scope.itemArr.currChoosedItemArr[i].lecturerId)
            		 {
            			 $scope.page.data[j].checked = true;
            		 }
            	}
            }
            $scope.$emit('isLoading', false);
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item,isRadio) {
        	//单选
        	if(isRadio=='Y'){
        		if (item.checked) 
                {
                    item.checked = undefined;
                    $scope.itemArr.currChoosedItemArr = [];
                } 
                else 
                {
                	for(var j in $scope.page.data)
                	{
           			 	$scope.page.data[j].checked = undefined;
                	} 
                	item.checked = true;
                	$scope.itemArr.currChoosedItemArr = [];
                    $scope.itemArr.currChoosedItemArr.push(item);
                }
        	}
        	else
        	{//多选
        		if (item.checked) 
                {
                    item.checked = undefined;
                    for(var i in $scope.itemArr.currChoosedItemArr)
                    {
                    	if(item.lecturerId==$scope.itemArr.currChoosedItemArr[i].lecturerId)
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
        	}
        },
        //线上课程只允许选择1位讲师
        chooseOneItem : function (item) {
            if (item.checked)
            {
                item.checked = undefined;
                $scope.itemArr.currChoosedItemArr = [];
            } 
            else
            {
                item.checked = true;
                $scope.itemArr.currChoosedItemArr = [];
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        //删除操作
        deleteItem : function (item) {
            item.checked = undefined;
            for(var i in $scope.itemArr.choosedItemArr)
            {
            	if(item.lecturerId==$scope.itemArr.choosedItemArr[i].lecturerId)
            	{
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            	}
            }
            $scope.buildLectures();
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
	            	var lecturerId = $scope.itemArr.oldChooseItemArr[i];
	            	for (var j in $scope.page.data) 
	            	{
	            		if($scope.page.data[j]){
	            			if($scope.page.data[j].lecturerId==lecturerId)
	            			{
	            				if($scope.itemArr.choosedItemArr.length<1)
	            				{
	            					$scope.itemArr.choosedItemArr.push($scope.page.data[j]);
	            					continue;
	            				}
	            				var isContained = false;
	            				for(var k in $scope.itemArr.choosedItemArr)
	            				{
	            					if($scope.itemArr.choosedItemArr[k].lecturerId==$scope.page.data[j].lecturerId)
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
	            //释放
            	$scope.page = {};
            }
        	$scope.$emit('isLoading', false);
        }
    };
    //接收上级编辑控制器$scope中的数据
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
        	var lecturers = [];
        	if($scope.formData.lecturers){
        		lecturers = $scope.formData.lecturers.split(",");
        	}
        	for(var i = 0;i<lecturers.length;i++)
        	{
        		if($scope.itemArr.oldChooseItemArr.indexOf(lecturers[i])==-1)
        		{
        			$scope.itemArr.oldChooseItemArr.push(lecturers[i]);
        		}
        	}
        	$scope.isInitUpdatePageData = true;
	    	$scope.search();
        }, function(data) {
             
        }); 
    }
    //生成表单数据  lecturers  字符串
    $scope.buildLectures = function () {
        var lecturersArry = [];
        for (var i in $scope.itemArr.choosedItemArr) 
        {
            lecturersArry.push($scope.itemArr.choosedItemArr[i].lecturerId);
        }
        if(lecturersArry.length>0){
        	$scope.$parent.$parent.formData.lecturers = ","+lecturersArry.join(",")+",";
        }else{
        	$scope.$parent.$parent.formData.lecturers = "";
        }
    	lecturersArry = null;
    }
    //切换线上/线下课程时清空选择的讲师数据
    $scope.$parent.$parent.cleanChoosedLectureData = function(){
    	$scope.itemArr.choosedItemArr = [];
		$scope.buildLectures();
    }
    //打开模态框
    $scope.$parent.$parent.doOpenLectureModal = function () {
        $scope.openModalDialog = true;
        dialogStatus.setHasShowedDialog(true);
    	$scope.isBuildOldChoosedItems = true;
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
        //清空查询条件
    	$scope.lectureName = "";
        //重新查询模态框中的数据
        $scope.search(); 
    }
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildLectures();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}]);

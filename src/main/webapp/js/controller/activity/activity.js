/**
 * Created by Wanglg on 2016/3/30.
 */
angular.module('ele.activity',['ele.admin'])
.controller('ActivityController', ['$scope', '$http', 'dialogService','$timeout',function ($scope, $http ,dialogService,$timeout) {
    //用于显示面包屑状态
    $scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动'};
    //http请求url
    $scope.url = "/enterpriseuniversity/services/activity/findList?pageNum=";
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
            if ($scope.activityName != undefined && $scope.activityName != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
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
    //添加
    $scope.doAdd = function(){
        $scope.go('/activity/addActivity','addActivity',null);
    }
    //编辑
    $scope.doEdit = function(activity){
        $scope.go('/activity/editActivity','editActivity',{id:activity.activityId});
    }
    //查看
    $scope.doView = function(activity){
    	 $scope.go('/activity/detailActivity','detailActivity',{id:activity.activityId});
    }
    //复制
    $scope.doCopy = function(activity){
    	$scope.go('/activity/copyActivity','copyActivity',{id:activity.activityId});
    };
    //删除
    $scope.doDelete = function(t){
    	dialogService.setContent("确定删除培训活动?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
        	$http({
        		url: "/enterpriseuniversity/services/activity/delete?activityId=" + t.activityId,
                method: "DELETE"
            }).success(function (response) {
            	//延迟弹框
		    	$timeout(function(){
		    		if(response.result=="protected"){
		    			dialogService.setContent("不能删除已经开始的培训").setShowDialog(true);
		    		}else{
		    			dialogService.setContent("删除培训活动成功").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
		    				$scope.paginationConf.onChange();
	    				}
		    		}
		    	},200);                
            }).error(function(){
		    	$timeout(function(){
		    		dialogService.setContent("网络异常,未能成功删除培训活动,请重新登录后再试").setShowDialog(true);
		    	},200);
		    }); 
    	}
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(activity){
        $scope.currHighLightRow.activity = activity.activityId; 
    }
}])
//费用统计
.controller('ActivityFeesController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
    $scope.openFeeModalDialog = false;
    $scope.$parent.$parent.doCost = function(activity){
    	$scope.tomFree={activityId:activity.activityId};
		$http.get("/enterpriseuniversity/services/activity/findCost?activityId="+activity.activityId).success(function (response) {
			$scope.tomFree.fees = response;
			$scope.currHighLightRow = {};
			$scope.openFeeModalDialog = true;
			dialogStatus.setHasShowedDialog(true);
		}); 
    }
    //关闭费用统计
    $scope.closeFeeModalDialog = function (){
 		$scope.openFeeModalDialog = false;
 		dialogStatus.setHasShowedDialog(false);
 	};
    //添加费用
 	var feeIndex = 0;//唯一性标记
    $scope.addFee = function() {
    	feeIndex = feeIndex+1;
    	var feeItem = {
			activityId:$scope.tomFree.activityId,
			feeId:"",
			feeName:"",
			fee:"",
			remark:"",
			creator:"",
			operator:"",
			updateTime:"",
			status:"",
			tip:"1",
			feeIndex:feeIndex
    	};
    	$scope.tomFree.fees.push(feeItem);
    };
    //删除费用统计
    $scope.deleteFee = function(fee){
    	dialogService.setContent('确定要删除当前培训活动费用吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
    		if(fee.feeId!==""){
    			$http({
        			url: "/enterpriseuniversity/services/activity/deleteCost?activityId="+fee.activityId+"&feeId=" + fee.feeId,
        			method: "DELETE"
        		}).success(function(data) {
        			$timeout(function(){
        				dialogService.setContent("删除培训活动费用成功！").setShowSureButton(false).setShowDialog(true);
        				$timeout(function(){
        					dialogService.sureButten_click();
	        				$http.get("/enterpriseuniversity/services/activity/findCost?activityId="+$scope.tomFree.activityId).success(function (response) {
	    						$scope.tomFree.fees = response;
	    					});
        				},2000);
        			},500);
        		}).error(function(){
        			$timeout(function(){
        				dialogService.setContent("删除培训活动费用失败！").setShowSureButton(false).setShowDialog(true);
        				$timeout(function(){
        					dialogService.sureButten_click();
        				},2000);
        			},500);
        		});
    		}else{
    			$scope.tomFree.fees.splice($scope.tomFree.fees.indexOf(fee), 1);
    		}
    	}
    }
    //费用统计保存
    $scope.saveFees = function (){
    	$scope.addFeesForm.$submitted = true;
    	if($scope.addFeesForm.$invalid){
    		dialogService.setContent("新增培训活动费用存在不合法填写字段!").setShowDialog(true) 
    		return;
    	}
    	var activityFees = {listFrees:[]};
    	for(var i in $scope.tomFree.fees){
    		if($scope.tomFree.fees[i].tip=="1"){
    			activityFees.listFrees.push($scope.tomFree.fees[i]);
    		}
    	}
    	if(activityFees.listFrees.length>0){
    		$http({
    			method : 'POST',
    			url  : '/enterpriseuniversity/services/activity/addCost',
    			data : activityFees, 
    			headers : { 'Content-Type': 'application/json' }
    		}).success(function(data) {
    			$scope.addFeesForm.$submitted = false;
				dialogService.setContent("培训活动费用保存成功！").setShowSureButton(false).setShowDialog(true);
				$timeout(function(){
					dialogService.sureButten_click();
    				$http.get("/enterpriseuniversity/services/activity/findCost?activityId="+$scope.tomFree.activityId).success(function (response) {
						$scope.tomFree.fees = response;
					});
				},2000);
    		}).error(function(){
    			$scope.addFeesForm.$submitted = false;
				dialogService.setContent("培训活动费用保存失败！").setShowSureButton(false).setShowDialog(true);
				$timeout(function(){
					dialogService.sureButten_click();
				},2000);
    		});
    	}else{
    		$scope.addFeesForm.$submitted = false;
    		dialogService.setContent("无新增培训活动费用记录!").setShowDialog(true);
    	}
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function($index){
    	$scope.currHighLightRow.index = $index;
    }
}])
//校验
.directive('requiredActivityemps', [function () {
	return {
	  require: "ngModel",
	  link: function (scope, element, attr, ngModel) {
		  var customValidator = function (value) {
			  var validity = null;
			  scope.$watch(function () {
				  var deptManagerIds = scope.formData==undefined?[]:scope.formData.deptManagerIds==null||scope.formData.deptManagerIds==undefined?[]:scope.formData.deptManagerIds;
				  var empsIds = value==undefined||value==null?[]:value;
		          return deptManagerIds.length+empsIds.length;
		      }, function(){
		    	  var deptManagerIds = scope.formData==undefined?[]:scope.formData.deptManagerIds==null||scope.formData.deptManagerIds==undefined?[]:scope.formData.deptManagerIds;
				  var empsIds = value==undefined||value==null?[]:value;
	    		  validity = deptManagerIds.length + empsIds.length < 1 ? true : false;
	    		  ngModel.$setValidity("requiredActivityemps", !validity);
	          });
			  return /*!validity ? value : []*/value;
	      };
	      ngModel.$formatters.push(customValidator);
	      ngModel.$parsers.push(customValidator);
	  }
	};
}])
.directive('requiredArr', [function () {
	return {
	  require: "ngModel",
	  link: function (scope, element, attr, ngModel) {
		  var customValidator = function (value) {
			  var validity = null;
			  scope.$watch(function () {
				  return value ? value.length : 0;
		      }, function(newV, oldV){
	    		  validity = newV ? newV < 1  ? true : false : true;
	    		  ngModel.$setValidity("requiredArr", !validity);
	          });
			  return  value;
	      };
	      ngModel.$formatters.push(customValidator);
	      ngModel.$parsers.push(customValidator);
	  }
	};
}])
.directive('requiredTaskpackage', [function () {
	return {
		  require: "ngModel",
		  link: function (scope, element, attr, ngModel) {
			  var customValidator = function (arry) {
				  var validity = null;
				  scope.$watch(function () {
			          return arry;
			      }, function(){
			    	  validity = arry==undefined ? true : arry.length <=0 ? true : false;
			          ngModel.$setValidity("requiredTaskpackage", !validity);
		          });
		          return !validity ? arry : [];
		      };
		      ngModel.$formatters.push(customValidator);
		      ngModel.$parsers.push(customValidator);
		  }
	};
}])
.directive('requiredLecturer', [function () {
	return {
	  require: "ngModel",
	  link: function (scope, element, attr, ngModel) {
		  var customValidator = function (value) {
			  var validity = null;
			  scope.$watch(function () {
		          return value;
		      }, function(){
		    	  validity = value==undefined || value =="" ? true : false;
		          ngModel.$setValidity("requiredLecturer", !validity);
	          });
	          return !validity ? value : undefined;
	      };
	      ngModel.$formatters.push(customValidator);
	      ngModel.$parsers.push(customValidator);
	  }
	};
}])
//添加活动
.controller('AddActivityController', ['$scope', '$http', 'FileUploader', 'dialogService','$timeout', function ($scope, $http, FileUploader, dialogService,$timeout) {
    $scope.$emit("cm", {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动', gcmn:'添加培训活动'});
    var nowDate=new Date() ;
    $scope.defaultStartTime=nowDate.getFullYear()+"-"+(nowDate.getMonth()+1)+"-"+nowDate.getDate()+" 9:00:00";
    $scope.defaultEndTime=nowDate.getFullYear()+"-"+(nowDate.getMonth()+1)+"-"+nowDate.getDate()+" 19:00:00";
    $scope.$on("$RebuiltEmps", function(){
		$scope.formData.tempEmps = 
			$scope.formData.empIds.concat($scope.formData.empIds.deptIds, $scope.formData.empIds.labelClassIds, $scope.formData.empIds.labelIds, $scope.formData.empIds.labelEmps);
    });
    $scope.activity = $scope.formData={
    	needApply : 'Y',
		protocol : 'N',
		preTaskInfo :[],
		deptCodes:"",
		employeeGradeStr:"",
		city:"",
		certificateState:"Y",
		receiveState:"Y"
    };
    $scope.$on("$empIds", function(e, empIds){
    	e.stopPropagation();
    	$scope.activity.empIds = empIds; 
    });
    $scope.$on("$certificateTemplate", function(e, d){
    	e.stopPropagation();
    	d && d.id ? $scope.formData.certificateId = Number(d.id) : "";
    });
    
    $scope.choosedEmpsArr = [];//指定学习人员
    $scope.choosedItemArr = [];//推送部门负责人
    $scope.taskPackage = [];
    
    //创建补考日期 html
    $scope.createRetaking = function(item){
    	item.retakingCountArr = [];
    	for(var i =1;i<=item.retakingExamCount;i++){
    		item.retakingCountArr.push(i);
    	}
    }
    //切换签订培训协议
    $scope.toggleProtocol = function(){
    	$scope.activity.trainFee = undefined;
    	if($scope.activity.protocol=='Y'){
    		$scope.activity.protocol = 'N';
    	}else{
    		$scope.activity.protocol = 'Y';
    	}
    }
    //计算课程费用
    $scope.calculateTotalPrice = function(item){
    	if(item.unitPrice!=undefined&&item.courseTime!=undefined){
    		item.totalPrice = item.unitPrice*item.courseTime/60;
    	}else{
    		item.totalPrice = 0;
    	}
    }
    //切换线下、线上考试
    $scope.toggleOfflineExam = function(item){
    	item.examAddress = '';
    	if(item.offlineExam=='2'){
    		item.offlineExam = '1';
    	}else{
    		item.offlineExam = '2';
    	}
    }
    //向上移动
    $scope.sortUp = function(item){
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort+1;//后移元素的sort+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort;//后移元素的preTaskName编号+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	//currItem.sort = index-2;
    	item.sort = item.sort-1;//前移的元素sort-1
    	item.preTaskName = "任务"+item.sort;
    	//清空前置任务数据
    	item.preTaskIds = "";
    	item.preTaskNames = "";
    	item.choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index-1,0,item);
    }
    //向下移动	
    $scope.sortDown = function(item){
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort-1;//前移的元素sort-1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort;
    	//清空前置任务数据
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskIds = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskNames = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	item.sort = item.sort+1;//后移元素的sort+1
    	item.preTaskName = "任务"+item.sort;
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index+1,0,item);
    }
    
    $scope.addActivitySubmit=false;
    //保存按钮
    $scope.doSave=function(){
    	$scope.activityForm.$submitted = true;
    	if($scope.activityForm.$invalid)
		{
    		dialogService.setContent("表单存在不合法字段,请按提示要求调整表单后重试").setShowDialog(true);
		 	return;
		}
    	//协议日期
    	if($scope.activity.protocol=="Y"){
    		var protocolStartTime = document.getElementById("protocolStartTime").value;
    		var protocolEndTime = document.getElementById("protocolEndTime").value;
    		$scope.activity.protocolStartTime = protocolStartTime;
   			$scope.activity.protocolEndTime = protocolEndTime;
    		if(protocolStartTime!=undefined&&protocolStartTime!=''&&protocolEndTime!=undefined&&protocolEndTime!=''){
    			protocolStartTime = protocolStartTime.replace(/-/g,"/");
        		protocolEndTime = protocolEndTime.replace(/-/g,"/");
    	   		if(new Date(protocolEndTime).getTime()/1000-new Date(protocolStartTime).getTime()/1000 < 0){
    	   			dialogService.setContent("协议终止日期不能大于协议生效日期").setShowDialog(true);
    	   			return;
    	   		} 
        	}else if(protocolStartTime!=undefined&&protocolStartTime!=''){
        		dialogService.setContent("请填写协议终止日期").setShowDialog(true);
        		return;
        	}else{
        		dialogService.setContent("请填写协议生效日期").setShowDialog(true);
        		return;
        	}
    	} 
    	//活动时间
    	var activityStartTime = document.getElementById("activityStartTime").value;
		var activityEndTime = document.getElementById("activityEndTime").value;
		$scope.activity.activityStartTime = activityStartTime;
		$scope.activity.activityEndTime = activityEndTime; 
		if(activityStartTime!=undefined&&activityStartTime!=''&&activityEndTime!=undefined&&activityEndTime!=''){
			activityStartTime = activityStartTime.replace(/-/g,"/");
			activityEndTime = activityEndTime.replace(/-/g,"/");
			if($scope.activity.protocol=="Y"){//判断培训活动时间是否在培训活动协议时间范围内
				var formattedProtocolEndTime = $scope.activity.protocolEndTime;
				formattedProtocolEndTime = formattedProtocolEndTime.replace(/-/g,"/");
				var formattedProtocolStartTime = $scope.activity.protocolStartTime;
				formattedProtocolStartTime = formattedProtocolStartTime.replace(/-/g,"/");
				if(new Date(activityStartTime).getTime()/1000-new Date(formattedProtocolStartTime).getTime()/1000 < 0
						||new Date(formattedProtocolEndTime).getTime()/1000-new Date(activityEndTime).getTime()/1000 < 0){
		   			dialogService.setContent("培训活动开始、结束时间不在培训协议开始、结束时间范围内").setShowDialog(true);
		   			return;
		   		} 
			}
	   		if(new Date(activityEndTime).getTime()/1000-new Date(activityStartTime).getTime()/1000 < 0){
	   			dialogService.setContent("活动结束时间不能大于活动开始时间").setShowDialog(true);
	   			return;
	   		} 
    	}else if(activityStartTime!=undefined&&activityStartTime!=''){
    		dialogService.setContent("请填写活动结束时间").setShowDialog(true);
    		return;
    	}else{
    		dialogService.setContent("请填写活动开始时间").setShowDialog(true);
    		return;
    	}
		
		//报名时间
		if($scope.activity.needApply== "Y"){
			var applicationStartTime = document.getElementById("applicationStartTime").value;
    		var applicationDeadline = document.getElementById("applicationDeadline").value;
    		$scope.activity.applicationStartTime = applicationStartTime;
   			$scope.activity.applicationDeadline = applicationDeadline; 
    		if(applicationStartTime!=undefined&&applicationStartTime!=''&&applicationDeadline!=undefined&&applicationDeadline!=''){
    			applicationStartTime = applicationStartTime.replace(/-/g,"/");
    			applicationDeadline = applicationDeadline.replace(/-/g,"/");
    			var formattedActivityStartTime = $scope.activity.activityStartTime;
    			formattedActivityStartTime = formattedActivityStartTime.replace(/-/g,"/");
				if(new Date(formattedActivityStartTime).getTime()/1000-new Date(applicationDeadline).getTime()/1000 < 0){
    	   			dialogService.setContent("培训活动开始时间须大于活动报名结束时间").setShowDialog(true);
    	   			return;
    	   		} 
    			if(new Date(applicationDeadline).getTime()/1000-new Date(applicationStartTime).getTime()/1000 < 0){
    	   			dialogService.setContent("报名开始时间不能大于报名结束时间").setShowDialog(true);
    	   			return;
    	   		} 
        	}else if(applicationStartTime!=undefined&&applicationStartTime!=''){
        		dialogService.setContent("请填写报名结束时间").setShowDialog(true);
        		return;
        	}else{
        		dialogService.setContent("请填写报名开始时间").setShowDialog(true);
        		return;
        	}
		}
		//任务包时间校验
		for(var i=0; i<$scope.taskPackage.length; i++){
			//任务包id
			$scope.activity.packageId= $scope.taskPackage[i].packageId ;
			for(var j=0; j<$scope.taskPackage[i].taskCoursesOrExamPapers.length; j++){
				var currItem = $scope.taskPackage[i].taskCoursesOrExamPapers[j];
				var taskInfo = {};
				//$scope.activity.preTaskInfo[j].pretaskId = currItem.preTaskIds==undefined?"": currItem.preTaskIds;
				taskInfo.pretaskId = currItem.preTaskIds== undefined? "" : currItem.preTaskIds;
				//课程
   				if(currItem.courseId){
   					var courseStartTime = document.getElementById('courseStartTime'+currItem.courseNumber).value;
   					var courseEndTime = document.getElementById('courseEndTime'+currItem.courseNumber).value;
   					taskInfo.startTime = courseStartTime ;
   					taskInfo.endTime = courseEndTime ;
   	   				if(courseStartTime!=undefined&&courseStartTime!=''&&courseEndTime!=undefined&&courseEndTime!=''){
   	   					currItem.formattedStartTime = courseStartTime = courseStartTime.replace(/-/g,"/");
   	   					currItem.formattedEndTime = courseEndTime = courseEndTime.replace(/-/g,"/");
   	   	    			if(currItem.preTaskIds!=undefined&&currItem.preTaskIds!=""&&currItem.preTaskIds!=null){
   	   	    				var lastPreTaskSort = currItem.preTaskIds.split(",")[currItem.preTaskIds.length-1];
   	   	    				for(var index in $scope.taskPackage[i].taskCoursesOrExamPapers){
   	   	    					var indexItem = $scope.taskPackage[i].taskCoursesOrExamPapers[index];
   	   	    					if(indexItem.sort == lastPreTaskSort){
	   	   	    					if(new Date(courseStartTime).getTime()/1000-new Date(indexItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不得早于(前置)任务"+indexItem.sort+"的结束时间").setShowDialog(true);
	   	    	   	    	   			return;
	   	    	   	    	   		} 
   	   	    					}
   	   	    				}
   	   	    			}
   	   	    	   		if(new Date(courseEndTime).getTime()/1000-new Date(courseStartTime).getTime()/1000 < 0){
   	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不能大于结束时间").setShowDialog(true);
   	   	    	   			return;
   	   	    	   		} else if(new Date(courseEndTime).getTime()/1000-new Date(courseStartTime).getTime()/1000 <currItem.courseTime*60){
   	   			   			dialogService.setContent("开始时间、结束时间间隔不得小于课程时长").setShowDialog(true);
   	   			   			return ;
   	   			   		}
   	   	        	}else if(courseStartTime!=undefined&&courseStartTime!=''){
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的结束时间").setShowDialog(true);
   	   	        		return;
   	   	        	}else{
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的开始时间").setShowDialog(true);
   	   	        		return;
   	   	        	}
   	   				taskInfo.taskId = currItem.courseId ;
   	   				taskInfo.sort = currItem.sort ;
   	   				taskInfo.lecturerId = currItem.lecturerId  ;
   	   				taskInfo.courseAddress = currItem.courseAddress ;  
   	   				taskInfo.courseTime = currItem.courseTime ;
   	   				taskInfo.unitPrice = currItem.unitPrice ;
   	   				taskInfo.totalPrice = currItem.totalPrice ;
   					//判断最后一个任务的结束时间是否在培训活动结束时间内
   	   				if(j==$scope.taskPackage[i].taskCoursesOrExamPapers.length-1){
	   	   				var formattedActivityEndTime = $scope.activity.activityEndTime;
	   	   				formattedActivityEndTime = formattedActivityEndTime.replace(/-/g,"/");
   	   					if(new Date(formattedActivityEndTime).getTime()/1000-new Date(currItem.formattedEndTime).getTime()/1000 < 0){//?
	   	    	   			dialogService.setContent("任务"+(j+1)+"的结束时间不在培训活动结束时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		} 
   	   				}
   	   				if(j==0){
	   	   				var formattedActivityStartTime = $scope.activity.activityStartTime;
	   	   				formattedActivityStartTime = formattedActivityStartTime.replace(/-/g,"/");
	   					if(new Date(currItem.formattedStartTime).getTime()/1000-new Date(formattedActivityStartTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不在培训活动开始时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		}
   	   				}
   				}
   				//试卷
   				if(currItem.examPaperId){
   	   				var examStartTime = document.getElementById('examStartTime'+currItem.examPaperNumber).value;
   					var examEndTime = document.getElementById('examEndTime'+currItem.examPaperNumber).value;
   					taskInfo.startTime = examStartTime ;
   					taskInfo.endTime = examEndTime ;
   	   				if(examStartTime!=undefined&&examStartTime!=''&&examEndTime!=undefined&&examEndTime!=''){
   	   					currItem.formattedStartTime = examStartTime = examStartTime.replace(/-/g,"/");
   	   					currItem.formattedEndTime = examEndTime = examEndTime.replace(/-/g,"/");
   	   					if(currItem.preTaskIds!=undefined&&currItem.preTaskIds!=""&&currItem.preTaskIds!=null){
	   	    				var lastPreTaskSort = currItem.preTaskIds.split(",")[currItem.preTaskIds.length-1];
	   	    				for(var index in $scope.taskPackage[i].taskCoursesOrExamPapers){
	   	    					var indexItem = $scope.taskPackage[i].taskCoursesOrExamPapers[index];
	   	    					if(indexItem.sort == lastPreTaskSort){
	   	   	    					if(new Date(examStartTime).getTime()/1000-new Date(indexItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不得早于(前置)任务"+indexItem.sort+"的结束时间").setShowDialog(true);
	   	    	   	    	   			return;
	   	    	   	    	   		} 
	   	    					}
	   	    				}
	   	    			}
   	   					if(new Date(examEndTime).getTime()/1000-new Date(examStartTime).getTime()/1000 < 0){
   	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不能大于结束时间").setShowDialog(true);
   	   	    	   			return;
   	   	    	   		}
   	   					//XXXXXXXXXXXXXXXXXXXXXXX
   	   					if(new Date(examEndTime).getTime()/1000-new Date(examStartTime).getTime()/1000 < parseInt(currItem.examTime)*60){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始、结束时间间隔不能小于考试时长").setShowDialog(true);
	   	    	   			return;
	   	    	   		}
   	   					//XXXXXXXXXXXXXXXXXXXXXXX
   	   	        	}else if(examStartTime!=undefined&&examStartTime!=''){
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的结束时间").setShowDialog(true);
   	   	        		return;
   	   	        	}else{
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的开始时间").setShowDialog(true);
   	   	        		return;
   	   	        	}
   	   				taskInfo.taskId = currItem.examPaperId ;
   	   				taskInfo.sort = currItem.sort ;
   	   				taskInfo.offlineExam = currItem.offlineExam  ; 
   	   				if(currItem.offlineExam=="2"){
   	   					//线下考试地址
   	   					taskInfo.examAddress = currItem.examAddress==undefined?"":currItem.examAddress;
   	   				}
   	   				taskInfo.retakingExamTimes = currItem.retakingExamCount ; 
   	   				taskInfo.offlineExam = currItem.offlineExam==undefined?"1":currItem.offlineExam;
   	   				taskInfo.retakingExamBeginTimeList = [];
   	   				taskInfo.retakingExamEndTimeList = [];
   	   				//判断最后一个任务的结束时间是否在培训活动结束时间内
   	   				if(j==$scope.taskPackage[i].taskCoursesOrExamPapers.length-1&&currItem.retakingExamCount==0){//没有补考次数
	   	   				var formattedActivityEndTime = $scope.activity.activityEndTime;
	   	   				formattedActivityEndTime = formattedActivityEndTime.replace(/-/g,"/");
   	   					if(new Date(formattedActivityEndTime).getTime()/1000-new Date(currItem.formattedEndTime).getTime()/1000 < 0){//
	   	    	   			dialogService.setContent("任务"+(j+1)+"的结束时间不在培训活动结束时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		} 
   	   				}
   	   				if(j==0){
	   	   				var formattedActivityStartTime = $scope.activity.activityStartTime;
	   	   				formattedActivityStartTime = formattedActivityStartTime.replace(/-/g,"/");
	   					if(new Date(currItem.formattedStartTime).getTime()/1000-new Date(formattedActivityStartTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不在培训活动开始时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		}
	   				}
   	   				for(var k =0; k < currItem.retakingExamCount; k++){
   	   					var retakingStartTime = document.getElementById('retakingStartTime'+currItem.examPaperNumber+(k+1)).value;
	   					var retakingEndTime = document.getElementById('retakingEndTime'+currItem.examPaperNumber+(k+1)).value;
	   					taskInfo.retakingExamBeginTimeList[k]= retakingStartTime;
	   					taskInfo.retakingExamEndTimeList[k]= retakingEndTime;
	   					if(retakingStartTime!=undefined&&retakingStartTime!=''&&retakingEndTime!=undefined&&retakingEndTime!=''){
	   						retakingStartTime = retakingStartTime.replace(/-/g,"/");
	   						retakingEndTime = retakingEndTime.replace(/-/g,"/");
	   						if(k==0){
	   							//校验首次补考日期不得早于活动结束日期
	   							if(new Date(retakingStartTime).getTime()/1000-new Date(currItem.formattedEndTime).getTime()/1000 <0){
	   								dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间不得早于首次考试的结束时间").setShowDialog(true);
		   	   	    	   			return;
	   							}
	   						}
	   						if(k>0){
	   							if(new Date(retakingStartTime).getTime()/1000-new Date(taskInfo.retakingExamEndTimeList[k-1]).getTime()/1000 <0){
	   								dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间不能小于补考日期"+k+"的结束时间").setShowDialog(true);
		   	   	    	   			return;
	   							}
		   					}
	   	   	    	   		if(new Date(retakingEndTime).getTime()/1000-new Date(retakingStartTime).getTime()/1000 < 0){
	   	   	    	   			dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间不能大于结束时间").setShowDialog(true);
	   	   	    	   			return;
	   	   	    	   		}
	   	   	    	   		//XXXXXXXXXXXXXXXXXXXXXXX
	   	   					if(new Date(retakingEndTime).getTime()/1000-new Date(retakingStartTime).getTime()/1000 < parseInt(currItem.examTime)*60){
		   	    	   			dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始、结束时间间隔不能小于考试时长").setShowDialog(true);
		   	    	   			return;
		   	    	   		}
	   	   					//XXXXXXXXXXXXXXXXXXXXXXX
	   	   	    	   		if(k==currItem.retakingExamCount-1){
			   	   	    	   	var formattedActivityEndTime = $scope.activity.activityEndTime;
			   	   				formattedActivityEndTime = formattedActivityEndTime.replace(/-/g,"/");
		   	   					if(new Date(formattedActivityEndTime).getTime()/1000-new Date(retakingEndTime).getTime()/1000 < 0){
			   	    	   			dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的结束时间不在培训活动结束时间范围内").setShowDialog(true);
			   	    	   			return;
			   	    	   		} 
	   	   	    	   		}
	   	   	        	}else if(retakingStartTime!=undefined&&retakingStartTime!=''){
	   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的补考日期"+(k+1)+"的结束时间").setShowDialog(true);
	   	   	        		return;
	   	   	        	}else{
	   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间").setShowDialog(true);
	   	   	        		return;
	   	   	        	}
   	   				}
   				}
   				$scope.activity.preTaskInfo[j] = taskInfo ;
   			}
		}
		$scope.addActivitySubmit=true;
		$http({
			method : 'POST',
			url  : '/enterpriseuniversity/services/activity/add',
			data : $scope.activity, 
			headers : { 'Content-Type': 'application/json' }
		}).success(function(data) {
			$scope.addActivitySubmit=false;
			if(data.result=="success"){
				dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   			$scope.go('/activity/activityList','activityList',null);
		   		},2000);
			}else if(data.result=="protected"){
				dialogService.setContent("活动报名日期已结束或活动已开始！").setShowDialog(true);
			}else if(data.result=="examTimeOut"){
				dialogService.setContent("考试时间超出起止时间间隔！").setShowDialog(true);
			}else{
				dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   		},2000);
			}
		})
		.error(function(){
			$scope.addActivitySubmit=false;
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   		},2000);
		});
    }
    //返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/activityList','activityList',null);
    }; 
}])
//编辑活动控制器
.controller('EditActivityController', ['$scope', '$http', '$state', '$stateParams', '$q', '$timeout','dialogService', function ($scope, $http, $state, $stateParams, $q, $timeout, dialogService) {
	//编辑
	$scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动' , gcmn: '编辑培训活动'};
	$scope.deferred = $q.defer();
	if($stateParams.id){
		$scope.$emit('isLoading', true);
	    $http.get("/enterpriseuniversity/services/activity/findDetails?activityId=" + $stateParams.id).success(function (data) {
	        $scope.deferred.resolve(data);
	    }).error(function(data){
	    	$scope.deferred.reject(data);
	    })
	}else {
		//未传参数
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
	}
	$scope.deferred.promise.then(function(data) {  // 成功回调
		$scope.$emit('isLoading', false);
		$scope.activity = $scope.formData = data; 
		$scope.activity.activityId = $stateParams.id;
		if($scope.activity.city=='\"null\"'){
			$scope.activity.city = undefined;
		}
	}, function(data) {  // 错误回调
		$scope.$emit('isLoading', false);
	    dialogService.setContent("页面数据初始化异常").setShowDialog(true);
	});
	
	$scope.$on("$RebuiltEmps", function(){
		$scope.formData.tempEmps = 
			$scope.formData.empIds.concat($scope.formData.empIds.deptIds, $scope.formData.empIds.labelClassIds, $scope.formData.empIds.labelIds, $scope.formData.empIds.labelEmps);
});
	
	$scope.$on("$empIds", function(e, empIds){
    	e.stopPropagation();
    	$scope.formData.empIds = empIds;
    });
	
	$scope.$on("$certificateTemplate", function(e, d){
    	e.stopPropagation();
    	d && d.id ? $scope.formData.certificateId = Number(d.id) : "";
    });
	
	$scope.taskPackage = [];
	//创建补考日期 html
    $scope.createRetaking = function(item){
    	if(item.retakingExamCount==undefined&&item.retakingInfo!=undefined){
    		item.retakingInfo = []; 
    	}
    	if(item.retakingExamCount!=undefined&&item.retakingInfo!=undefined){
    		if(item.retakingExamCount >= item.retakingInfo.length){
    			var len = item.retakingInfo.length;
        		for(var i =0;i<item.retakingExamCount-len;i++){
            		item.retakingInfo.push({startTime:'',endTime:''});
            	}
        	}else{
        		var len = item.retakingInfo.length - item.retakingExamCount ;
        		item.retakingInfo.splice(item.retakingExamCount,len);
        	}
    	}
    	if(item.retakingExamCount!=undefined&&item.retakingInfo==undefined){
    		item.retakingInfo = []; 
        	for(var i =1;i<=item.retakingExamCount;i++){
        		item.retakingInfo.push({startTime:'',endTime:''});
        	}
    	}
    }
    //切换签订培训协议
    $scope.toggleProtocol = function(){
    	$scope.activity.trainFee = undefined;
    	if($scope.activity.protocol=='Y'){
    		$scope.activity.protocol = 'N';
    	}else{
    		$scope.activity.protocol = 'Y';
    	}
    }
    //计算课程费用
    $scope.calculateTotalPrice = function(item){
    	if(item.unitPrice!=undefined&&item.courseTime!=undefined){
    		item.totalPrice = item.unitPrice*item.courseTime/60;
    	}else{
    		item.totalPrice = 0;
    	}
    }
    //切换线下、线上考试
    $scope.toggleOfflineExam = function(item){
    	item.examAddress = '';
    	if(item.offlineExam=='2'){
    		item.offlineExam = '1';
    	}else{
    		item.offlineExam = '2';
    	}
    }
    //向上移动
    $scope.sortUp = function(item){
    	//$scope.taskPackage = $scope.taskPackageBackup.concat();
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort+1;//后移元素的sort+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort;//后移元素的preTaskName编号+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	//currItem.sort = index-2;
    	item.sort = item.sort-1;//前移的元素sort-1
    	item.preTaskName = "任务"+item.sort;
    	//清空前置任务数据
    	item.preTaskIds = "";
    	item.preTaskNames = "";
    	item.choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index-1,0,item);
    }
    //向下移动	
    $scope.sortDown = function(item){
    	//$scope.taskPackage = $scope.taskPackageBackup.concat();
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort-1;//前移的元素sort-1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort;
    	//清空前置任务数据
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskIds = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskNames = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	item.sort = item.sort+1;//后移元素的sort+1
    	item.preTaskName = "任务"+item.sort;
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index+1,0,item);
    }
    
    $scope.editActivitySubmit=false;
    //保存按钮    
	$scope.doSave=function(){
    	$scope.activityForm.$submitted = true;
    	if($scope.activityForm.$invalid)
		{
    		dialogService.setContent("表单存在不合法字段,请按提示要求调整表单后重试").setShowDialog(true);
		 	return;
		}
    	//协议日期
    	if($scope.activity.protocol=="Y"){
    		var protocolStartTime = document.getElementById("protocolStartTime").value;
    		var protocolEndTime = document.getElementById("protocolEndTime").value;
    		$scope.activity.protocolStartTime = protocolStartTime;
   			$scope.activity.protocolEndTime = protocolEndTime;
    		if(protocolStartTime!=undefined&&protocolStartTime!=''&&protocolEndTime!=undefined&&protocolEndTime!=''){
    			protocolStartTime = protocolStartTime.replace(/-/g,"/");
        		protocolEndTime = protocolEndTime.replace(/-/g,"/");
    	   		if(new Date(protocolEndTime).getTime()/1000-new Date(protocolStartTime).getTime()/1000 < 0){
    	   			dialogService.setContent("协议终止日期不能大于协议生效日期").setShowDialog(true);
    	   			return;
    	   		} 
        	}else if(protocolStartTime!=undefined&&protocolStartTime!=''){
        		dialogService.setContent("请填写协议终止日期").setShowDialog(true);
        		return;
        	}else{
        		dialogService.setContent("请填写协议生效日期").setShowDialog(true);
        		return;
        	}
    	} 
    	//活动时间
    	var activityStartTime = document.getElementById("activityStartTime").value;
		var activityEndTime = document.getElementById("activityEndTime").value;
		$scope.activity.activityStartTime = activityStartTime;
		$scope.activity.activityEndTime = activityEndTime; 
		if(activityStartTime!=undefined&&activityStartTime!=''&&activityEndTime!=undefined&&activityEndTime!=''){
			activityStartTime = activityStartTime.replace(/-/g,"/");
			activityEndTime = activityEndTime.replace(/-/g,"/");
			if($scope.activity.protocol=="Y"){//判断培训活动时间是否在培训活动协议时间范围内
				var formattedProtocolEndTime = $scope.activity.protocolEndTime;
				formattedProtocolEndTime = formattedProtocolEndTime.replace(/-/g,"/");
				var formattedProtocolStartTime = $scope.activity.protocolStartTime;
				formattedProtocolStartTime = formattedProtocolStartTime.replace(/-/g,"/");
				if(new Date(activityStartTime).getTime()/1000-new Date(formattedProtocolStartTime).getTime()/1000 < 0
						||new Date(formattedProtocolEndTime).getTime()/1000-new Date(activityEndTime).getTime()/1000 < 0){
		   			dialogService.setContent("培训活动开始、结束时间不在培训协议开始、结束时间范围内").setShowDialog(true);
		   			return;
		   		} 
			}
			if(new Date(activityEndTime).getTime()/1000-new Date(activityStartTime).getTime()/1000 < 0){
	   			dialogService.setContent("活动结束时间不能大于活动开始时间").setShowDialog(true);
	   			return;
	   		} 
    	}else if(activityStartTime!=undefined&&activityStartTime!=''){
    		dialogService.setContent("请填写活动结束时间").setShowDialog(true);
    		return;
    	}else{
    		dialogService.setContent("请填写活动开始时间").setShowDialog(true);
    		return;
    	}
		//报名时间
		if($scope.activity.needApply== "Y"){
			var applicationStartTime = document.getElementById("applicationStartTime").value;
    		var applicationDeadline = document.getElementById("applicationDeadline").value;
    		$scope.activity.applicationStartTime = applicationStartTime;
   			$scope.activity.applicationDeadline = applicationDeadline; 
    		if(applicationStartTime!=undefined&&applicationStartTime!=''&&applicationDeadline!=undefined&&applicationDeadline!=''){
    			applicationStartTime = applicationStartTime.replace(/-/g,"/");
    			applicationDeadline = applicationDeadline.replace(/-/g,"/");
    			var formattedActivityStartTime = $scope.activity.activityStartTime;
    			formattedActivityStartTime = formattedActivityStartTime.replace(/-/g,"/");
				if(new Date(formattedActivityStartTime).getTime()/1000-new Date(applicationDeadline).getTime()/1000 < 0){
    	   			dialogService.setContent("培训活动开始时间须大于活动报名结束时间").setShowDialog(true);
    	   			return;
    	   		}
    			if(new Date(applicationDeadline).getTime()/1000-new Date(applicationStartTime).getTime()/1000 < 0){
    	   			dialogService.setContent("报名开始时间不能大于报名结束时间").setShowDialog(true);
    	   			return;
    	   		} 
        	}else if(applicationStartTime!=undefined&&applicationStartTime!=''){
        		dialogService.setContent("请填写报名结束时间").setShowDialog(true);
        		return;
        	}else{
        		dialogService.setContent("请填写报名开始时间").setShowDialog(true);
        		return;
        	}
		}
		//任务包时间校验
		for(var i=0; i<$scope.taskPackage.length; i++){
			//任务包id
			$scope.activity.packageId= $scope.taskPackage[i].packageId ;
			for(var j=0; j<$scope.taskPackage[i].taskCoursesOrExamPapers.length; j++){
				var currItem = $scope.taskPackage[i].taskCoursesOrExamPapers[j];
				var taskInfo = {};
				//$scope.activity.preTaskInfo[j].pretaskId = currItem.preTaskIds==undefined?"": currItem.preTaskIds;
				taskInfo.pretaskId = currItem.preTaskIds== undefined? "" : currItem.preTaskIds;
				//课程
   				if(currItem.courseId){
   					var courseStartTime = document.getElementById('courseStartTime'+currItem.courseNumber).value;
   					var courseEndTime = document.getElementById('courseEndTime'+currItem.courseNumber).value;
   					taskInfo.startTime = courseStartTime ;
   					taskInfo.endTime = courseEndTime ;
   	   				if(courseStartTime!=undefined&&courseStartTime!=''&&courseEndTime!=undefined&&courseEndTime!=''){
   	   					currItem.formattedStartTime = courseStartTime = courseStartTime.replace(/-/g,"/");
   	   					currItem.formattedEndTime = courseEndTime = courseEndTime.replace(/-/g,"/");
   	   	    			if(currItem.preTaskIds!=undefined&&currItem.preTaskIds!=""&&currItem.preTaskIds!=null){
	   	    				var lastPreTaskSort = currItem.preTaskIds.split(",")[currItem.preTaskIds.length-1];
	   	    				for(var index in $scope.taskPackage[i].taskCoursesOrExamPapers){
	   	    					var indexItem = $scope.taskPackage[i].taskCoursesOrExamPapers[index];
	   	    					if(indexItem.sort == lastPreTaskSort){
	   	   	    					if(new Date(courseStartTime).getTime()/1000-new Date(indexItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不得早于(前置)任务"+indexItem.sort+"的结束时间").setShowDialog(true);
	   	    	   	    	   			return;
	   	    	   	    	   		} 
	   	    					}
	   	    				}
	   	    			}
   	   	    			if(new Date(courseEndTime).getTime()/1000-new Date(courseStartTime).getTime()/1000 < 0){
   	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不能大于结束时间").setShowDialog(true);
   	   	    	   			return;
   	   	    	   		}else if(new Date(courseEndTime).getTime()/1000-new Date(courseStartTime).getTime()/1000 <currItem.courseTime*60){
   	   			   			dialogService.setContent("开始时间、结束时间间隔不得小于课程时长").setShowDialog(true);
   	   			   			return ;
   	   			   		} 
   	   	        	}else if(courseStartTime!=undefined&&courseStartTime!=''){
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的结束时间").setShowDialog(true);
   	   	        		return;
   	   	        	}else{
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的开始时间").setShowDialog(true);
   	   	        		return;
   	   	        	}
   	   				taskInfo.taskId = currItem.courseId ;
   	   				taskInfo.sort = currItem.sort ;
   	   				taskInfo.lecturerId = currItem.lecturerId  ;
   	   				taskInfo.courseAddress = currItem.courseAddress ;  
   	   				taskInfo.courseTime = currItem.courseTime ;
   	   				taskInfo.unitPrice = currItem.unitPrice ;
   	   				taskInfo.totalPrice = currItem.totalPrice ;
   	   				//判断最后一个任务的结束时间是否在培训活动结束时间内
   	   				if(j==$scope.taskPackage[i].taskCoursesOrExamPapers.length-1){
	   	   				var formattedActivityEndTime = $scope.activity.activityEndTime;
	   	   				formattedActivityEndTime = formattedActivityEndTime.replace(/-/g,"/");
   	   					if(new Date(formattedActivityEndTime).getTime()/1000-new Date(currItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的结束时间不在培训活动结束时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		} 
   	   				}
   	   				if(j==0){
	   	   				var formattedActivityStartTime = $scope.activity.activityStartTime;
	   	   				formattedActivityStartTime = formattedActivityStartTime.replace(/-/g,"/");
	   					if(new Date(currItem.formattedStartTime).getTime()/1000-new Date(formattedActivityStartTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不在培训活动开始时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		}
	   				}
   				}
   				//试卷
   				if(currItem.examPaperId){
   	   				var examStartTime = document.getElementById('examStartTime'+currItem.examPaperNumber).value;
   					var examEndTime = document.getElementById('examEndTime'+currItem.examPaperNumber).value;
   					taskInfo.startTime = examStartTime ;
   	   				taskInfo.endTime = examEndTime ;
   	   				if(examStartTime!=undefined&&examStartTime!=''&&examEndTime!=undefined&&examEndTime!=''){
   	   					currItem.formattedStartTime = examStartTime = examStartTime.replace(/-/g,"/");
   	   					currItem.formattedEndTime = examEndTime = examEndTime.replace(/-/g,"/");
   	   					if(currItem.preTaskIds!=undefined&&currItem.preTaskIds!=""&&currItem.preTaskIds!=null){
	   	    				var lastPreTaskSort = currItem.preTaskIds.split(",")[currItem.preTaskIds.length-1];
	   	    				for(var index in $scope.taskPackage[i].taskCoursesOrExamPapers){
	   	    					var indexItem = $scope.taskPackage[i].taskCoursesOrExamPapers[index];
	   	    					if(indexItem.sort == lastPreTaskSort){
	   	   	    					if(new Date(examStartTime).getTime()/1000-new Date(indexItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不得早于(前置)任务"+indexItem.sort+"的结束时间").setShowDialog(true);
	   	    	   	    	   			return;
	   	    	   	    	   		} 
	   	    					}
	   	    				}
	   	    			}
   	   					if(new Date(examEndTime).getTime()/1000-new Date(examStartTime).getTime()/1000 < 0){
   	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不能大于结束时间").setShowDialog(true);
   	   	    	   			return;
   	   	    	   		} 
   	   					//XXXXXXXXXXXXXXXXXXXXXXX
   	   					if(new Date(examEndTime).getTime()/1000-new Date(examStartTime).getTime()/1000 < parseInt(currItem.examTime)*60){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始、结束时间间隔不能小于考试时长").setShowDialog(true);
	   	    	   			return;
	   	    	   		}
   	   					//XXXXXXXXXXXXXXXXXXXXXXX
   	   	        	}else if(examStartTime!=undefined&&examStartTime!=''){
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的结束时间").setShowDialog(true);
   	   	        		return;
   	   	        	}else{
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的开始时间").setShowDialog(true);
   	   	        		return;
   	   	        	}
   	   				taskInfo.taskId = currItem.examPaperId ;
   	   				taskInfo.sort = currItem.sort ;
   	   				taskInfo.offlineExam = currItem.offlineExam  ; 
   	   				if(currItem.offlineExam=="2"){
   	   					//线下考试地址
   	   					taskInfo.examAddress = currItem.examAddress==undefined?"":currItem.examAddress;
   	   				}
   	   				taskInfo.retakingExamTimes = currItem.retakingExamCount ; 
   	   				taskInfo.offlineExam = currItem.offlineExam==undefined?"1":currItem.offlineExam;
   	   				taskInfo.retakingExamBeginTimeList = [];
   	   				taskInfo.retakingExamEndTimeList = [];
   	   				//判断最后一个任务的结束时间是否在培训活动结束时间内
   	   				if(j==$scope.taskPackage[i].taskCoursesOrExamPapers.length-1&&currItem.retakingExamCount==0){//没有补考次数
	   	   				var formattedActivityEndTime = $scope.activity.activityEndTime;
	   	   				formattedActivityEndTime = formattedActivityEndTime.replace(/-/g,"/");
   	   					if(new Date(formattedActivityEndTime).getTime()/1000-new Date(currItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的结束时间不在培训活动结束时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		} 
   	   				}
   	   				if(j==0){
	   	   				var formattedActivityStartTime = $scope.activity.activityStartTime;
	   	   				formattedActivityStartTime = formattedActivityStartTime.replace(/-/g,"/");
	   					if(new Date(currItem.formattedStartTime).getTime()/1000-new Date(formattedActivityStartTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不在培训活动开始时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		}
	   				}
   	   				for(var k =0; k<currItem.retakingExamCount; k++){
   	   					var retakingStartTime = document.getElementById('retakingStartTime'+currItem.examPaperNumber+(k+1)).value;
	   					var retakingEndTime = document.getElementById('retakingEndTime'+currItem.examPaperNumber+(k+1)).value;
	   					taskInfo.retakingExamBeginTimeList[k]= retakingStartTime;
	   					taskInfo.retakingExamEndTimeList[k]= retakingEndTime;
	   					if(retakingStartTime!=undefined&&retakingStartTime!=''&&retakingEndTime!=undefined&&retakingEndTime!=''){
	   						retakingStartTime = retakingStartTime.replace(/-/g,"/");
	   						retakingEndTime = retakingEndTime.replace(/-/g,"/");
	   						if(k==0){
	   							//校验首次补考日期不得早于活动结束日期
	   							if(new Date(retakingStartTime).getTime()/1000-new Date(currItem.formattedEndTime).getTime()/1000 <0){
	   								dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间不得早于首次考试的结束时间").setShowDialog(true);
		   	   	    	   			return;
	   							}
	   						}
	   						if(k>0){
	   							if(new Date(retakingStartTime).getTime()/1000-new Date(taskInfo.retakingExamEndTimeList[k-1]).getTime()/1000 <0){
	   								dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间不能小于补考日期"+k+"的结束时间").setShowDialog(true);
		   	   	    	   			return;
	   							}
		   					}
	   	   	    	   		if(new Date(retakingEndTime).getTime()/1000-new Date(retakingStartTime).getTime()/1000 < 0){
	   	   	    	   			dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间不能大于结束时间").setShowDialog(true);
	   	   	    	   			return;
	   	   	    	   		} 
	   	   	    	   		//XXXXXXXXXXXXXXXXXXXXXXX
	   	   					if(new Date(retakingEndTime).getTime()/1000-new Date(retakingStartTime).getTime()/1000 < parseInt(currItem.examTime)*60){
		   	    	   			dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始、结束时间间隔不能小于考试时长").setShowDialog(true);
		   	    	   			return;
		   	    	   		}
	   	   					//XXXXXXXXXXXXXXXXXXXXXXX
	   	   	    	   		if(k==currItem.retakingExamCount-1){
			   	   	    	   	var formattedActivityEndTime = $scope.activity.activityEndTime;
			   	   				formattedActivityEndTime = formattedActivityEndTime.replace(/-/g,"/");
		   	   					if(new Date(formattedActivityEndTime).getTime()/1000-new Date(retakingEndTime).getTime()/1000 < 0){
			   	    	   			dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的结束时间不在培训活动结束时间范围内").setShowDialog(true);
			   	    	   			return;
			   	    	   		} 
	   	   	    	   		}
	   	   	        	}else if(retakingStartTime!=undefined&&retakingStartTime!=''){
	   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的补考日期"+(k+1)+"的结束时间").setShowDialog(true);
	   	   	        		return;
	   	   	        	}else{
	   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间").setShowDialog(true);
	   	   	        		return;
	   	   	        	}
   	   				}
   				}
   				$scope.activity.preTaskInfo[j] = taskInfo ;
   			}
		}
		$scope.editActivitySubmit=true;
		$http({
			method : 'POST',
			url  : '/enterpriseuniversity/services/activity/update',
			data : $scope.activity, 
			headers : { 'Content-Type': 'application/json' }
		}).success(function(data) {
			$scope.editActivitySubmit=false;
			if(data.result=="success"){
				dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   			$scope.go('/activity/activityList','activityList',null);
		   		},2000);
			}else if(data.result=="examTimeOut"){
				dialogService.setContent("考试时间超出起止时间间隔！").setShowDialog(true);
			}else if(data.result=="protected"){
				dialogService.setContent("不能编辑已开始的活动！").setShowDialog(true);
			}else{
				dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   		},2000);
			}
		})
		.error(function(){
			$scope.editActivitySubmit=false;
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   		},2000);
		});
	}
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/activityList','activityList',null);
    };   
}])
.controller('ViewActivityController', ['$scope', '$http', '$stateParams', '$q', '$timeout','dialogService', function ($scope, $http, $stateParams, $q, $timeout, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动' , gcmn: '查看培训活动'};
	$scope.deferred = $q.defer();
	$scope.taskPackage = [];
	if($stateParams.id){
	    $http.get("/enterpriseuniversity/services/activity/findDetails?activityId=" + $stateParams.id).success(function (data) {
	        $scope.deferred.resolve(data);
	    }).error(function(data){
	    	$scope.deferred.reject(data);
	    })
	}else {
		//未传参数
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
	}
	$scope.deferred.promise.then(function(data) {  // 成功回调
		$scope.activity = $scope.formData = data; 
		$scope.activity.activityId = $stateParams.id;
		if($scope.activity.city=='\"null\"'){
			$scope.activity.city = undefined;
		}
	}, function(data) {  // 错误回调
	    dialogService.setContent("页面数据初始化异常").setShowDialog(true);
	    return;
	});
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/activityList','activityList',null);
    };
}])
//复制活动控制器
.controller('CopyActivityController',['$scope', '$http', '$state', '$stateParams', '$q', '$timeout', 'dialogService', function ($scope, $http, $state, $stateParams, $q, $timeout, dialogService) {
    //编辑
    $scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动', gcmn:'复制培训活动'};
    $scope.deferred = $q.defer();
	if($stateParams.id){
	    $http.get("/enterpriseuniversity/services/activity/copyDetails?activityId=" + $stateParams.id).success(function (data) {
	        $scope.deferred.resolve(data);
	    }).error(function(data){
	    	$scope.deferred.reject(data);
	    })
	}else {
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
	}
	$scope.deferred.promise.then(function(data) {  // 成功回调
		$scope.activity = $scope.formData = data; 
		$scope.activity.activityId = $stateParams.id;
		if($scope.activity.city=='\"null\"'){
			$scope.activity.city = undefined;
		}
	}, function(data) {  // 错误回调
	    dialogService.setContent("页面数据初始化异常").setShowDialog(true);
	    return;
	});
	$scope.taskPackage = [];
	//创建补考日期 html
    $scope.createRetaking = function(item){
    	if(item.retakingExamCount==undefined&&item.retakingInfo!=undefined){
    		item.retakingInfo = []; 
    	}
    	if(item.retakingExamCount!=undefined&&item.retakingInfo!=undefined){
    		if(item.retakingExamCount >= item.retakingInfo.length){
    			var len = item.retakingInfo.length;
        		for(var i =0;i<item.retakingExamCount-len;i++){
            		item.retakingInfo.push({startTime:'',endTime:''});
            	}
        	}else{
        		var len = item.retakingInfo.length - item.retakingExamCount ;
        		item.retakingInfo.splice(item.retakingExamCount,len);
        	}
    	}
    	if(item.retakingExamCount!=undefined&&item.retakingInfo==undefined){
    		item.retakingInfo = []; 
        	for(var i =1;i<=item.retakingExamCount;i++){
        		item.retakingInfo.push({startTime:'',endTime:''});
        	}
    	}
    }
    //切换签订培训协议
    $scope.toggleProtocol = function(){
    	$scope.activity.trainFee = undefined;
    	if($scope.activity.protocol=='Y'){
    		$scope.activity.protocol = 'N';
    	}else{
    		$scope.activity.protocol = 'Y';
    	}
    }
    //计算课程费用
    $scope.calculateTotalPrice = function(item){
    	if(item.unitPrice!=undefined&&item.courseTime!=undefined){
    		item.totalPrice = item.unitPrice*item.courseTime/60;
    	}else{
    		item.totalPrice = 0;
    	}
    }
    //切换线下、线上考试
    $scope.toggleOfflineExam = function(item){
    	item.examAddress = '';
    	if(item.offlineExam=='2'){
    		item.offlineExam = '1';
    	}else{
    		item.offlineExam = '2';
    	}
    }
    //向上移动
    $scope.sortUp = function(item){
    	//$scope.taskPackage = $scope.taskPackageBackup.concat();
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort+1;//后移元素的sort+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort;//后移元素的preTaskName编号+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	//currItem.sort = index-2;
    	item.sort = item.sort-1;//前移的元素sort-1
    	item.preTaskName = "任务"+item.sort;
    	//清空前置任务数据
    	item.preTaskIds = "";
    	item.preTaskNames = "";
    	item.choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index-1,0,item);
    }
    //向下移动	
    $scope.sortDown = function(item){
    	//$scope.taskPackage = $scope.taskPackageBackup.concat();
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort-1;//前移的元素sort-1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort;
    	//清空前置任务数据
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskIds = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskNames = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	item.sort = item.sort+1;//后移元素的sort+1
    	item.preTaskName = "任务"+item.sort;
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index+1,0,item);
    }
    //保存按钮    
    $scope.showSubmitModel=false;
	$scope.doSave=function(){
    	$scope.activityForm.$submitted = true;
    	if($scope.activityForm.$invalid)
		{
    		dialogService.setContent("表单存在不合法字段,请按提示要求调整表单后重试").setShowDialog(true);
		 	return;
		}
    	
    	//协议日期
    	if($scope.activity.protocol=="Y"){
    		var protocolStartTime = document.getElementById("protocolStartTime").value;
    		var protocolEndTime = document.getElementById("protocolEndTime").value;
    		$scope.activity.protocolStartTime = protocolStartTime;
   			$scope.activity.protocolEndTime = protocolEndTime;
    		if(protocolStartTime!=undefined&&protocolStartTime!=''&&protocolEndTime!=undefined&&protocolEndTime!=''){
    			protocolStartTime = protocolStartTime.replace(/-/g,"/");
        		protocolEndTime = protocolEndTime.replace(/-/g,"/");
    	   		if(new Date(protocolEndTime).getTime()/1000-new Date(protocolStartTime).getTime()/1000 < 0){
    	   			dialogService.setContent("协议终止日期不能大于协议生效日期").setShowDialog(true);
    	   			return;
    	   		} 
        	}else if(protocolStartTime!=undefined&&protocolStartTime!=''){
        		dialogService.setContent("请填写协议终止日期").setShowDialog(true);
        		return;
        	}else{
        		dialogService.setContent("请填写协议生效日期").setShowDialog(true);
        		return;
        	}
    	} 
    	//活动时间
    	var activityStartTime = document.getElementById("activityStartTime").value;
		var activityEndTime = document.getElementById("activityEndTime").value;
		$scope.activity.activityStartTime = activityStartTime;
		$scope.activity.activityEndTime = activityEndTime; 
		if(activityStartTime!=undefined&&activityStartTime!=''&&activityEndTime!=undefined&&activityEndTime!=''){
			activityStartTime = activityStartTime.replace(/-/g,"/");
			activityEndTime = activityEndTime.replace(/-/g,"/");
			if($scope.activity.protocol=="Y"){//判断培训活动时间是否在培训活动协议时间范围内
				var formattedProtocolEndTime = $scope.activity.protocolEndTime;
				formattedProtocolEndTime = formattedProtocolEndTime.replace(/-/g,"/");
				var formattedProtocolStartTime = $scope.activity.protocolStartTime;
				formattedProtocolStartTime = formattedProtocolStartTime.replace(/-/g,"/");
				if(new Date(activityStartTime).getTime()/1000-new Date(formattedProtocolStartTime).getTime()/1000 < 0
						||new Date(formattedProtocolEndTime).getTime()/1000-new Date(activityEndTime).getTime()/1000 < 0){
		   			dialogService.setContent("培训活动开始、结束时间不在培训协议开始、结束时间范围内").setShowDialog(true);
		   			return;
		   		} 
			}
			if(new Date(activityEndTime).getTime()/1000-new Date(activityStartTime).getTime()/1000 < 0){
	   			dialogService.setContent("活动结束时间不能大于活动开始时间").setShowDialog(true);
	   			return;
	   		} 
    	}else if(activityStartTime!=undefined&&activityStartTime!=''){
    		dialogService.setContent("请填写活动结束时间").setShowDialog(true);
    		return;
    	}else{
    		dialogService.setContent("请填写活动开始时间").setShowDialog(true);
    		return;
    	}
		//报名时间
		if($scope.activity.needApply== "Y"){
			var applicationStartTime = document.getElementById("applicationStartTime").value;
    		var applicationDeadline = document.getElementById("applicationDeadline").value;
    		$scope.activity.applicationStartTime = applicationStartTime;
   			$scope.activity.applicationDeadline = applicationDeadline; 
    		if(applicationStartTime!=undefined&&applicationStartTime!=''&&applicationDeadline!=undefined&&applicationDeadline!=''){
    			applicationStartTime = applicationStartTime.replace(/-/g,"/");
    			applicationDeadline = applicationDeadline.replace(/-/g,"/");
    			var formattedActivityStartTime = $scope.activity.activityStartTime;
    			formattedActivityStartTime = formattedActivityStartTime.replace(/-/g,"/");
				if(new Date(formattedActivityStartTime).getTime()/1000-new Date(applicationDeadline).getTime()/1000 < 0){
    	   			dialogService.setContent("培训活动开始时间须大于活动报名结束时间").setShowDialog(true);
    	   			return;
    	   		}
    			if(new Date(applicationDeadline).getTime()/1000-new Date(applicationStartTime).getTime()/1000 < 0){
    	   			dialogService.setContent("报名开始时间不能大于报名结束时间").setShowDialog(true);
    	   			return;
    	   		} 
        	}else if(applicationStartTime!=undefined&&applicationStartTime!=''){
        		dialogService.setContent("请填写报名结束时间").setShowDialog(true);
        		return;
        	}else{
        		dialogService.setContent("请填写报名开始时间").setShowDialog(true);
        		return;
        	}
		}
		//任务包时间校验
		for(var i=0; i<$scope.taskPackage.length; i++){
			//任务包id
			$scope.activity.packageId= $scope.taskPackage[i].packageId ;
			for(var j=0; j<$scope.taskPackage[i].taskCoursesOrExamPapers.length; j++){
				var currItem = $scope.taskPackage[i].taskCoursesOrExamPapers[j];
				var taskInfo = {};
				//$scope.activity.preTaskInfo[j].pretaskId = currItem.preTaskIds==undefined?"": currItem.preTaskIds;
				taskInfo.pretaskId = currItem.preTaskIds== undefined? "" : currItem.preTaskIds;
				//课程
   				if(currItem.courseId){
   					var courseStartTime = document.getElementById('courseStartTime'+currItem.courseNumber).value;
   					var courseEndTime = document.getElementById('courseEndTime'+currItem.courseNumber).value;
   					taskInfo.startTime = courseStartTime ;
   	   				taskInfo.endTime = courseEndTime ;
   	   				if(courseStartTime!=undefined&&courseStartTime!=''&&courseEndTime!=undefined&&courseEndTime!=''){
   	   					currItem.formattedStartTime = courseStartTime = courseStartTime.replace(/-/g,"/");
   	   	    			currItem.formattedEndTime = courseEndTime = courseEndTime.replace(/-/g,"/");
	   	   	    		if(currItem.preTaskIds!=undefined&&currItem.preTaskIds!=""&&currItem.preTaskIds!=null){
	   	    				var lastPreTaskSort = currItem.preTaskIds.split(",")[currItem.preTaskIds.length-1];
	   	    				for(var index in $scope.taskPackage[i].taskCoursesOrExamPapers){
	   	    					var indexItem = $scope.taskPackage[i].taskCoursesOrExamPapers[index];
	   	    					if(indexItem.sort == lastPreTaskSort){
	   	   	    					if(new Date(courseStartTime).getTime()/1000-new Date(indexItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不得早于(前置)任务"+indexItem.sort+"的结束时间").setShowDialog(true);
	   	    	   	    	   			return;
	   	    	   	    	   		} 
	   	    					}
	   	    				}
	   	    			}
   	   	    			if(new Date(courseEndTime).getTime()/1000-new Date(courseStartTime).getTime()/1000 < 0){
   	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不能大于结束时间").setShowDialog(true);
   	   	    	   			return;
   	   	    	   		}else if(new Date(courseEndTime).getTime()/1000-new Date(courseStartTime).getTime()/1000 <currItem.courseTime*60){
   	   			   			dialogService.setContent("开始时间、结束时间间隔不得小于课程时长").setShowDialog(true);
   	   			   			return ;
   	   			   		}  
   	   	        	}else if(courseStartTime!=undefined&&courseStartTime!=''){
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的结束时间").setShowDialog(true);
   	   	        		return;
   	   	        	}else if(new Date(courseEndTime).getTime()/1000-new Date(courseStartTime).getTime()/1000 <currItem.courseTime*60){
   			   			dialogService.setContent("开始时间、结束时间间隔不得小于考试时长").setShowDialog(true);
   			   			return ;
   			   		}else{
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的开始时间").setShowDialog(true);
   	   	        		return;
   	   	        	}
   	   				
   	   				taskInfo.taskId = currItem.courseId ;
   	   				taskInfo.sort = currItem.sort ;
   	   				taskInfo.lecturerId = currItem.lecturerId  ;
   	   				taskInfo.courseAddress = currItem.courseAddress ;  
   	   				taskInfo.courseTime = currItem.courseTime ;
   	   				taskInfo.unitPrice = currItem.unitPrice ;
   	   				taskInfo.totalPrice = currItem.totalPrice ;
   	   				//判断最后一个任务的结束时间是否在培训活动结束时间内
   	   				if(j==$scope.taskPackage[i].taskCoursesOrExamPapers.length-1){
	   	   				var formattedActivityEndTime = $scope.activity.activityEndTime;
	   	   				formattedActivityEndTime = formattedActivityEndTime.replace(/-/g,"/");
   	   					if(new Date(formattedActivityEndTime).getTime()/1000-new Date(currItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的结束时间不在培训活动结束时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		} 
   	   				}
   	   				if(j==0){
	   	   				var formattedActivityStartTime = $scope.activity.activityStartTime;
	   	   				formattedActivityStartTime = formattedActivityStartTime.replace(/-/g,"/");
	   					if(new Date(currItem.formattedStartTime).getTime()/1000-new Date(formattedActivityStartTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不在培训活动开始时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		}
	   				}
   				}
   			
   				//试卷
   				if(currItem.examPaperId){
   	   				var examStartTime = document.getElementById('examStartTime'+currItem.examPaperNumber).value;
   					var examEndTime = document.getElementById('examEndTime'+currItem.examPaperNumber).value;
   					taskInfo.startTime = examStartTime ;
   	   				taskInfo.endTime = examEndTime ;
   	   				if(examStartTime!=undefined&&examStartTime!=''&&examEndTime!=undefined&&examEndTime!=''){
   	   					currItem.formattedStartTime = examStartTime = examStartTime.replace(/-/g,"/");
   	   	    			currItem.formattedEndTime = examEndTime = examEndTime.replace(/-/g,"/");
	   	   	    		if(currItem.preTaskIds!=undefined&&currItem.preTaskIds!=""&&currItem.preTaskIds!=null){
	   	    				var lastPreTaskSort = currItem.preTaskIds.split(",")[currItem.preTaskIds.length-1];
	   	    				for(var index in $scope.taskPackage[i].taskCoursesOrExamPapers){
	   	    					var indexItem = $scope.taskPackage[i].taskCoursesOrExamPapers[index];
	   	    					if(indexItem.sort == lastPreTaskSort){
	   	   	    					if(new Date(examStartTime).getTime()/1000-new Date(indexItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不得早于(前置)任务"+indexItem.sort+"的结束时间").setShowDialog(true);
	   	    	   	    	   			return;
	   	    	   	    	   		} 
	   	    					}
	   	    				}
	   	    			}
   	   	    			if(new Date(examEndTime).getTime()/1000-new Date(examStartTime).getTime()/1000 < 0){
   	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不能大于结束时间").setShowDialog(true);
   	   	    	   			return;
   	   	    	   		} 
   	   	    			//XXXXXXXXXXXXXXXXXXXXXXX
   	   					if(new Date(examEndTime).getTime()/1000-new Date(examStartTime).getTime()/1000 < parseInt(currItem.examTime)*60){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始、结束时间间隔不能小于考试时长").setShowDialog(true);
	   	    	   			return;
	   	    	   		}
   	   					//XXXXXXXXXXXXXXXXXXXXXXX
   	   	        	}else if(examStartTime!=undefined&&examStartTime!=''){
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的结束时间").setShowDialog(true);
   	   	        		return;
   	   	        	}else{
   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的开始时间").setShowDialog(true);
   	   	        		return;
   	   	        	}
   	   				taskInfo.taskId = currItem.examPaperId ;
   	   				taskInfo.sort = currItem.sort ;
   	   				taskInfo.offlineExam = currItem.offlineExam  ; 
   	   				if(currItem.offlineExam=="2"){
   	   					//线下考试地址
   	   					taskInfo.examAddress = currItem.examAddress==undefined?"":currItem.examAddress;
   	   				}
   	   				taskInfo.retakingExamTimes = currItem.retakingExamCount ; 
   	   				taskInfo.offlineExam = currItem.offlineExam==undefined?"1":currItem.offlineExam;
   	   				taskInfo.retakingExamBeginTimeList = [];
   	   				taskInfo.retakingExamEndTimeList = [];
   	   				//判断最后一个任务的结束时间是否在培训活动结束时间内
   	   				if(j==$scope.taskPackage[i].taskCoursesOrExamPapers.length-1&&currItem.retakingExamCount==0){//没有补考次数
	   	   				var formattedActivityEndTime = $scope.activity.activityEndTime;
	   	   				formattedActivityEndTime = formattedActivityEndTime.replace(/-/g,"/");
   	   					if(new Date(formattedActivityEndTime).getTime()/1000-new Date(currItem.formattedEndTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的结束时间不在培训活动结束时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		} 
   	   				}
   	   				if(j==0){
	   	   				var formattedActivityStartTime = $scope.activity.activityStartTime;
	   	   				formattedActivityStartTime = formattedActivityStartTime.replace(/-/g,"/");
	   					if(new Date(currItem.formattedStartTime).getTime()/1000-new Date(formattedActivityStartTime).getTime()/1000 < 0){
	   	    	   			dialogService.setContent("任务"+(j+1)+"的开始时间不在培训活动开始时间范围内").setShowDialog(true);
	   	    	   			return;
	   	    	   		}
	   				}
   	   				for(var k =0; k<currItem.retakingExamCount; k++){//有补考次数
   	   					var retakingStartTime = document.getElementById('retakingStartTime'+currItem.examPaperNumber+(k+1)).value;
	   					var retakingEndTime = document.getElementById('retakingEndTime'+currItem.examPaperNumber+(k+1)).value;
	   					taskInfo.retakingExamBeginTimeList[k]= retakingStartTime;
	   					taskInfo.retakingExamEndTimeList[k]= retakingEndTime;
	   					if(retakingStartTime!=undefined&&retakingStartTime!=''&&retakingEndTime!=undefined&&retakingEndTime!=''){
	   						retakingStartTime = retakingStartTime.replace(/-/g,"/");
	   						retakingEndTime = retakingEndTime.replace(/-/g,"/");
	   						if(k==0){
	   							//校验首次补考日期不得早于活动结束日期
	   							if(new Date(retakingStartTime).getTime()/1000-new Date(currItem.formattedEndTime).getTime()/1000 <0){
	   								dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间不得早于首次考试的结束时间").setShowDialog(true);
		   	   	    	   			return;
	   							}
	   						}
	   						if(k>0){
	   							if(new Date(retakingStartTime).getTime()/1000-new Date(taskInfo.retakingExamEndTimeList[k-1]).getTime()/1000 <0){
	   								dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间不能小于补考日期"+k+"的结束时间").setShowDialog(true);
		   	   	    	   			return;
	   							}
		   					}
	   	   	    	   		if(new Date(retakingEndTime).getTime()/1000-new Date(retakingStartTime).getTime()/1000 < 0){
	   	   	    	   			dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间不能大于结束时间").setShowDialog(true);
	   	   	    	   			return;
	   	   	    	   		} 
	   	   	    	   		//XXXXXXXXXXXXXXXXXXXXXXX
	   	   					if(new Date(retakingEndTime).getTime()/1000-new Date(retakingStartTime).getTime()/1000 < parseInt(currItem.examTime)*60){
		   	    	   			dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的开始、结束时间间隔不能小于考试时长").setShowDialog(true);
		   	    	   			return;
		   	    	   		}
	   	   					//XXXXXXXXXXXXXXXXXXXXXXX
	   	   	    	   		if(k==currItem.retakingExamCount-1){
			   	   	    	   	var formattedActivityEndTime = $scope.activity.activityEndTime;
			   	   				formattedActivityEndTime = formattedActivityEndTime.replace(/-/g,"/");
		   	   					if(new Date(formattedActivityEndTime).getTime()/1000-new Date(retakingEndTime).getTime()/1000 < 0){
			   	    	   			dialogService.setContent("任务"+(j+1)+"的补考日期"+(k+1)+"的结束时间不在培训活动结束时间范围内").setShowDialog(true);
			   	    	   			return;
			   	    	   		} 
	   	   	    	   		}
	   	   	        	}else if(retakingStartTime!=undefined&&retakingStartTime!=''){
	   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的补考日期"+(k+1)+"的结束时间").setShowDialog(true);
	   	   	        		return;
	   	   	        	}else{
	   	   	        		dialogService.setContent("请填写任务"+(j+1)+"的补考日期"+(k+1)+"的开始时间").setShowDialog(true);
	   	   	        		return;
	   	   	        	}
   	   				}
   				}
   				$scope.activity.preTaskInfo[j] = taskInfo ;
   			}
		}
		$scope.showSubmitModel=true;
		$http({
			method : 'POST',
			url  : '/enterpriseuniversity/services/activity/add',
			data : $scope.activity, 
			headers : { 'Content-Type': 'application/json' }
		}).success(function(data) {
			$scope.showSubmitModel=false;
			if(data.result=="success"){
				dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   			$scope.go('/activity/activityList','activityList',null);
		   		},2000);
			}else if(data.result=="examTimeOut"){
				dialogService.setContent("考试时间超出起止时间间隔！").setShowDialog(true);
			}else if(data.result=="protected"){
				dialogService.setContent("活动报名日期已结束或活动已开始！").setShowDialog(true);
			}else{
				dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   		},2000);
			}
		})
		.error(function(){
			$scope.showSubmitModel=false;
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   		},2000);
		});
    }    
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/activityList','activityList',null);
    };
}])
//选择课活动图片控制器
.controller('ActivtiyImgController', ['$scope', 'FileUploader', 'dialogService','$http', function ($scope, FileUploader, dialogService,$http) {
    var imgUploader = $scope.imgUploader = new FileUploader(
        {
            url : '/enterpriseuniversity/services/file/upload/activityImg',
            autoUpload : true
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
            	dialogService.setContent("要上传新的培训活动封面图片，请先删除旧的培训活动封面图片").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("仅允许上传一张培训活动封面图片").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|'.indexOf(type) == -1)
            {
                dialogService.setContent("上传文件不符合培训活动封面图片格式要求").setShowDialog(true);
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
    //编辑页面培训活动封面图片文件地址
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currFileUrlQueue =[]; 
    		if($scope.formData.activityImg&&$scope.formData.activityImg!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.activityImg);
    		}
        }, function(data) {
            
        }); 
    } 
    //修改活动图片
    $scope.buildActivityImg = function(imgUrl){
		$scope.$parent.formData.activityImg = imgUrl;
    }
    //预览框显示/隐藏控制
    $scope.openPreview = false;
    //预览
    $scope.preview = function(){
    	if($scope.$parent.formData.activityImg&&$scope.$parent.formData.activityImg!=""){
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.$parent.formData.activityImg;
    	}
    	else
    	{
    		dialogService.setContent("封面图片还未上传完毕,请稍后重试预览").setShowDialog(true);
    	}
    }
    //关闭预览框
    $scope.closePreview = function(){
    	$scope.openPreview = false;
    }
    //删除之前上传的文件路径集合
    $scope.removeCurrItem = function (item) {
        $scope.currFileUrlQueue.splice($scope.currFileUrlQueue.indexOf(item), 1);
        $scope.buildActivityImg("");
    }
    //删除 
    $scope.removeItem = function (item) {
   		if(item.fileUrl!=null){
    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
    	}
        item.remove();
        document.getElementById("activityImg").value=[];
        $scope.buildActivityImg(""); 
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
        	dialogService.setContent("解析上传文件失败").setShowDialog(true);
        }
        else
        {
        	fileItem.fileUrl=response.url;
        	$scope.buildActivityImg(response.url);//接收返回的文件地址
        }
    };
}])
//选择部门控制器
.controller('DeptModelController', ['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
	$scope.isBuildOldChoosedItems = false;
    $scope.openModalDialog = false;
	$scope.queryDept = function(){
		//查询部门树数据
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/orggroups/selectByScope")
			.success(function(response) {
				$scope.$emit('isLoading', false);
				$scope.deptList = response;
				if($scope.isBuildOldChoosedItems&&$scope.itemArr.initCurrChoosedItemArrStatus){
	            	$scope.itemArr.initCurrChoosedItemArrStatus();
	            }
	            if($scope.itemArr.buildOldChoosedItemArr&&!$scope.isBuildOldChoosedItems){
	            	$scope.itemArr.buildOldChoosedItemArr();
	            }
			})
			.error(function(){
				$scope.$emit('isLoading', false);
				dialogService.setContent("查询部门数据错误").setShowDialog(true);
			});
	}
	//容器及函数
    $scope.itemArr = {
		oldChooseItemArr:[],
        choosedItemArr: [],
        currChoosedItemArr: [],
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.selected) 
            {
            	for(var i in $scope.itemArr.currChoosedItemArr)
            	{
                	if($scope.itemArr.currChoosedItemArr[i].code==item.code)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                		break;
                	}
                }
            } 
            else
            {
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        deleteItem : function (item) {
            for(var i in $scope.itemArr.choosedItemArr){
            	if($scope.itemArr.choosedItemArr[i].code==item.code){
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            		break;
            	}
            }
            $scope.buildCode();
        },
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	$scope.itemArr.nodeLoopStatus($scope.deptList,$scope.itemArr.currChoosedItemArr[i].code,'P',[]);
            }
            $scope.$emit('isLoading', false);
        },
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.deptList) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var code = $scope.itemArr.oldChooseItemArr[i];
	            	$scope.itemArr.nodeLoop($scope.deptList,code); 
	            }
            }
        	$scope.$emit('isLoading', false);
        },
        nodeLoop:function(LoopNode,code){
        	for(var i in LoopNode)
        	{
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
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoop(LoopNode[i].children,code);
            	}
        	}
        } ,
        nodeLoopStatus:function(LoopNode,code,loopType,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==code)
        		{
        			LoopNode[i].selected = true; 
        			parentNodeArr.push(LoopNode[i].fathercode); 
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.deptList,LoopNode[i].fathercode,parentNodeArr);
        			break;
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
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.deptList,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopForParentNodeFatherCode(LoopNode[i].children,fathercode,parentNodeArr);//>C 子节点 LoopNode[i].children
            	}
        	}
        }
    };
    //编辑页面初始化
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	var deptCodeArr = [];
	    	if($scope.formData.deptCodes)
	    	{
	    		var deptCodeArr = $scope.formData.deptCodes.split(",");
	    	}
    		for(var i = 0;i<deptCodeArr.length;i++)
    		{
        		if(deptCodeArr[i]!=""&&$scope.itemArr.oldChooseItemArr.indexOf(deptCodeArr[i])==-1)
        		{
        			$scope.itemArr.oldChooseItemArr.push(deptCodeArr[i]);
        		}
        	}
	    	$scope.queryDept(); 
        }, function(data) {}); 
    }
    //打开选择部门模态框
    $scope.$parent.$parent.doOpenDeptModal = function(){
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
    	$scope.isBuildOldChoosedItems = true;
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
        //重新查询模态框中的数据
        $scope.queryDept();
    }
    //生成所属部门字段值
    $scope.buildCode = function () {
        var deptCodeStr = "";
        for (var i in $scope.itemArr.choosedItemArr) {
        	deptCodeStr = deptCodeStr + "," + $scope.itemArr.choosedItemArr[i].code;
        }
        $scope.$parent.$parent.formData.deptCodes = deptCodeStr;
    }
    $scope.doSure= function () {
    	$scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildCode();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    } 
}])
//员工等级控制器
.controller('EmployeeGradeModelController', ['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
	$scope.openModalDialog = false;
    $scope.employeeGrade=[
      {id:1,name:"P0"},{id:2,name:"P1"},{id:3,name:"P2"},{id:4,name:"P3"},{id:5,name:"P4"},
      {id:6,name:"P5"},{id:7,name:"P6"},{id:8,name:"P7"},{id:9,name:"P8"},{id:10,name:"P9"},
      {id:11,name:"P10"},{id:12,name:"P11"},{id:13,name:"P12"},{id:14,name:"新人"},{id:15,name:"M0"},
      {id:16,name:"M1"},{id:17,name:"M2"},{id:18,name:"M3"},{id:19,name:"M4"},{id:20,name:"M5"},
      {id:21,name:"M6"},{id:22,name:"M7"},{id:23,name:"M8"},{id:24,name:"M9"},{id:25,name:"M10"},
      {id:26,name:"M11"},{id:27,name:"M12"}
    ];
    $scope.$parent.$parent.doOpenEmployeeGradeModal = function(){
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
    	$scope.itemArr.initCurrChoosedItemArrStatus();
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    }
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
            	for(var j in $scope.employeeGrade){
            		 if($scope.employeeGrade[j].name==$scope.itemArr.currChoosedItemArr[i].name){
            			 $scope.employeeGrade[j].checked = true;
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
                	if(item.name==$scope.itemArr.currChoosedItemArr[i].name)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                		break;
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
            for(var i in $scope.itemArr.choosedItemArr){
            	if(item.name==$scope.itemArr.choosedItemArr[i].name){
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            		break;
            	}
            }
            $scope.buildEmployeeGradeStr();
        },
        //编辑页面数据回显
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
            for(var i in $scope.itemArr.oldChooseItemArr)
            {
            	var empGradeName = $scope.itemArr.oldChooseItemArr[i];
            	for (var j in $scope.employeeGrade) 
            	{
        			if($scope.employeeGrade[j].name==empGradeName)
        			{
        				if($scope.itemArr.choosedItemArr.length<1)
        				{
        					$scope.itemArr.choosedItemArr.push($scope.employeeGrade[j]);
        					continue;
        				}
        				var isContained = false;
        				for(var k in $scope.itemArr.choosedItemArr)
        				{
        					if($scope.itemArr.choosedItemArr[k].name==$scope.employeeGrade[j].name)
        					{
        						isContained = true;
        						break;
            				} 
        				}
        				if(!isContained)
        				{
        					$scope.itemArr.choosedItemArr.push($scope.employeeGrade[j]);
        				}
	            	}
	            }
            }
            $scope.buildEmployeeGradeStr();
            $scope.$emit('isLoading', false);
        },
        //全选
        chooseAllItem : function(){
        	//...
        	if(this.isChooseAllItem){
        		 
        	}else{
        		 
        	}
        }           
    };
    //初始化管理员项
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	var employeeGradeArr = [];
	    	if($scope.formData.employeeGradeStr)
	    	{
	    		var employeeGradeArr = $scope.formData.employeeGradeStr.split(",");
	    	}
    		for(var i = 0; i< employeeGradeArr.length; i++)
    		{
        		if(employeeGradeArr[i]!=""&&$scope.itemArr.oldChooseItemArr.indexOf(employeeGradeArr[i])==-1)
        		{
        			$scope.itemArr.oldChooseItemArr.push(employeeGradeArr[i]);
        		}
        	}
    		$scope.itemArr.buildOldChoosedItemArr();
        }, function(data) {}); 
    }
    $scope.buildEmployeeGradeStr = function () {
        var employeeGradeStr = "";
        for (var i in $scope.itemArr.choosedItemArr) {
        	employeeGradeStr = employeeGradeStr + "," + $scope.itemArr.choosedItemArr[i].name;
        }
        $scope.$parent.$parent.formData.employeeGradeStr = employeeGradeStr;
    }
    $scope.doSure= function () {
    	$scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildEmployeeGradeStr();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
//选择任务包控制器
.controller('TaskPackageModalController', ['$scope', '$http', '$state', '$stateParams', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $state, $stateParams , $timeout, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/taskPackage/findActivityTaskList?pageNum=";
	$scope.openModalDialog = false;
	$scope.isInitUpdatePageData = false;
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
			if ($scope.packageName != undefined && $scope.packageName != "") {
				$scope.$httpPrams = $scope.$httpPrams + "&packageName=" + $scope.packageName
					.replace(/\%/g,"%25").replace(/\#/g,"%23")
					.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
					.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
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
            	dialogService.setContent("任务包数据查询异常！").setShowDialog(true);
            });
		}
	};
     //搜索按钮函数
	$scope.search = function () {
		if($scope.isBuildOldChoosedItems){
	 		$scope.paginationConf.currentPage = 1;
	     	$scope.paginationConf.itemsPerPage = 20 ;
		}else{
			$scope.paginationConf.currentPage = 1;
			//查询全部数据用于buildOldChoosedItemArr
		 	$scope.paginationConf.itemsPerPage = "全部" ;
		}
		$scope.paginationConf.onChange();
	};
	$scope.isBuildOldChoosedItems = false;
	
	$scope.itemArr = {
    	oldChooseItemArr: [],
        currChoosedItemArr: [],
        choosedItemArr :[],
        //选中choosedItemArr中的项           
	    initCurrChoosedItemArrStatus: function () {
	    	$scope.$emit('isLoading', true);
	        for (var i in $scope.itemArr.currChoosedItemArr) {
	        	for(var j in $scope.page.data){
	        		 if($scope.page.data[j].packageId==$scope.itemArr.currChoosedItemArr[i].packageId){
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
	    },
        //编辑页面数据回显
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.page.data) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var packageId = $scope.itemArr.oldChooseItemArr[i];
	            	for (var j in $scope.page.data) 
	            	{
	            		if($scope.page.data[j])
	            		{
	            			if($scope.page.data[j].packageId==packageId)
	            			{
	            				if($scope.itemArr.choosedItemArr.length<1)
	            				{
	            					$scope.itemArr.choosedItemArr.push($scope.page.data[j]);
	            					continue;
	            				}
	            				var isContained = false;
	            				for(var k in $scope.itemArr.choosedItemArr)
	            				{
	            					if($scope.itemArr.choosedItemArr[k].packageId==$scope.page.data[j].packageId)
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
	            $scope.buildItemProp();
	            $scope.$parent.$parent.taskPackage = $scope.itemArr.choosedItemArr.concat();
	            $scope.echoTaskPackage();
            }
        	$scope.$emit('isLoading', false);
        }          
	};
	$scope.echoTaskPackage = function(){
		for(var k in $scope.itemArr.choosedItemArr){
			var courseAndExamPaperArr = $scope.itemArr.choosedItemArr[k].taskCoursesOrExamPapers;
			for(var i in courseAndExamPaperArr){
				var choosedItem = courseAndExamPaperArr[i];
				for(var j in $scope.formData.preTaskInfo){
					var preTaskItem = $scope.formData.preTaskInfo[j];
					if(preTaskItem.courseId!=null){//课程
						if(choosedItem.courseId&&choosedItem.courseId==preTaskItem.courseId){
							 choosedItem.preTaskNames = preTaskItem.preName ;
							 choosedItem.preTaskIds = preTaskItem.pretaskId ;
							 choosedItem.startTime = preTaskItem.startTime ;
							 choosedItem.endTime = preTaskItem.endTime ;
							 var lecturerLists = [];
							 for(var n in choosedItem.lecturerLists){
								 var currItem = choosedItem.lecturerLists[n];
								 lecturerLists.push({lecturerId:currItem.lecturerId,lecturerName:currItem.lecturerName});
							 }
							 choosedItem.lecturerLists = lecturerLists;
							 choosedItem.lecturerId = preTaskItem.lecturerId==null?null:Number(preTaskItem.lecturerId);
							 choosedItem.courseAddress = preTaskItem.courseAddress;
							 choosedItem.courseTime = preTaskItem.courseTime;
							 choosedItem.unitPrice = preTaskItem.unitPrice;
							 choosedItem.totalPrice = preTaskItem.totalPrice ;
							 //前置任务
							 choosedItem.choosedItemArr = (preTaskItem.pretaskId==null||preTaskItem.pretaskId==undefined)?[]:preTaskItem.pretaskId.split(",");
							 //排序
							 choosedItem.sort = preTaskItem.sort;
						}
					}else{//考试
						if(choosedItem.examPaperId&&choosedItem.examPaperId==preTaskItem.examPaperId){
							 choosedItem.preTaskNames = preTaskItem.preName ;
							 choosedItem.preTaskIds = preTaskItem.pretaskId ;
							 choosedItem.startTime = preTaskItem.startTime ;
							 choosedItem.endTime = preTaskItem.endTime ;
							 choosedItem.offlineExam = preTaskItem.offlineExam;
							 if(preTaskItem.offlineExam=='2'){
								 choosedItem.examAddress = preTaskItem.examAddress;
							 }
							 choosedItem.retakingExamCount = preTaskItem.retakingExamTimes;
							 //前置任务
							 choosedItem.choosedItemArr = (preTaskItem.pretaskId==null||preTaskItem.pretaskId==undefined)?[]:preTaskItem.pretaskId.split(",");
							 //排序
							 choosedItem.sort = preTaskItem.sort;
							 choosedItem.retakingInfo = [];
							 for(var m=1;m<=preTaskItem.retakingExamTimes;m++){
								 choosedItem.retakingInfo.push({startTime:preTaskItem.retakingExamBeginTimeList[m],endTime:preTaskItem.retakingExamEndTimeList[m]});
							 }
						}
					}
				}
			}
			//编辑页面初始化顺序
			courseAndExamPaperArr.sort(function(a,b){
				if(a.sort==""||a.sort==null||b.sort==""||b.sort==null){
					return 0;
				}
				return Number(a.sort) - Number(b.sort);
			});
		}
	}
	//初始化管理员项
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	if($scope.formData.packageId!=""&&$scope.formData.packageId!=undefined){
	    		$scope.itemArr.oldChooseItemArr.push($scope.formData.packageId);
	    	}
    		$scope.isInitUpdatePageData = true;
	    	$scope.search(); 
        }, function(data) {}); 
    }
    $scope.$parent.$parent.doOpenTaskPackageModal = function () {
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.isBuildOldChoosedItems = true;
    	$scope.itemArr.choosedItemArr = $scope.$parent.$parent.taskPackage.concat();
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
        $scope.paginationConf.currentPage = 1;
        //初始化每页显示条数
        $scope.paginationConf.itemsPerPage = 20;
        //重新查询模态框中的数据
        $scope.search();
    }
    $scope.buildItemProp = function(){
    	for(var i = 0 ; i < $scope.itemArr.choosedItemArr.length ; i++ ){
	    	var taskPackage = $scope.itemArr.choosedItemArr[i];
	    	for(var j = 0 ; j < taskPackage.taskCoursesOrExamPapers.length ; j++){
	    		taskPackage.taskCoursesOrExamPapers[j].sort = j+1;
	    		taskPackage.taskCoursesOrExamPapers[j].preTaskName = "任务"+(j+1);
	    	}
	    }
    }
	$scope.doSure = function () {
	    $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
	    $scope.buildItemProp();
	    $scope.$parent.$parent.taskPackage = $scope.itemArr.choosedItemArr.concat();
		$scope.openModalDialog = false;
		dialogStatus.setHasShowedDialog(false);
	} 
	$scope.doClose = function () {
	   $scope.openModalDialog = false;
	   dialogStatus.setHasShowedDialog(false);
	} 
}])
//选择任务包筛选学习人员控制器
.controller('ChooseTaskPackageForEmpModalController', ['$scope','$http','$state','$stateParams','dialogService', function ($scope, $http, $state, $stateParams, dialogService) {
	$scope.url = "/enterpriseuniversity/services/taskPackage/findTaskList?pageNum=";
	$scope.openTaskPackage2ModalDialog = false;
	$scope.paginationConf = {
		currentPage: 1,
		totalItems: 10,
		itemsPerPage: 20,
		pagesLength: 15,
		perPageOptions: [20, 50, 100, '全部'],
		rememberPerPage: 'perPageItems',
		onChange: function () {
			if(!$scope.openTaskPackage2ModalDialog){
				$scope.page = {};
				return;
			}
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			if($scope.packageName != undefined && $scope.packageName != "") {
				$scope.$httpPrams = $scope.$httpPrams + "&packageName=" 
					+ $scope.packageName.replace(/\%/g,"%25").replace(/\#/g,"%23")
					.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
					.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
			}
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
		    	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
			$scope.$emit('isLoading', true);
			$http.get($scope.$httpUrl).success(function (response) {
				$scope.$emit('isLoading', false);
		        $scope.page = response;
		        $scope.paginationConf.totalItems = response.count;
		        $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				if($scope.itemArr.initCurrChoosedItemArrStatus){
					$scope.itemArr.initCurrChoosedItemArrStatus();
				} 
		    }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("任务包数据查询异常！").setShowDialog(true);
            });
		}
	};
	//搜索按钮函数
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	//打开摸态框
	$scope.$parent.$parent.doOpenTaskPackageForEmpModal = function(){
		$scope.itemArr.currChoosedItemArr = $scope.$parent.$parent.tomPackage.packageId==undefined?[]:$scope.itemArr.choosedItemArr.concat(); 
		$scope.packageName = "";
		$scope.openTaskPackage2ModalDialog = true;
		$scope.search();
	}
	$scope.itemArr = {
		choosedItemArr: [],
		currChoosedItemArr: [],
		initCurrChoosedItemArrStatus: function () {
			$scope.$emit('isLoading', true);
			for (var i in $scope.itemArr.currChoosedItemArr) 
			{
				for(var j in $scope.page.data)
			 	{
			 		 if($scope.page.data[j].packageId==$scope.itemArr.currChoosedItemArr[i].packageId)
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
	     },
	     //删除操作
	     deleteItem : function (item) {
	         item.checked = undefined;
	         for(var i in $scope.itemArr.choosedItemArr)
	         {
	         	if(item.packageId==$scope.itemArr.choosedItemArr[i].packageId)
	         	{
	         		$scope.itemArr.choosedItemArr.splice(i, 1);
	         	}
	         }
	     }
	};
	//确定
	$scope.doSure = function () {
	     $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
	     if($scope.itemArr.choosedItemArr.length==0){
	    	 $scope.$parent.$parent.tomPackage={};
	     }else{
	    	 $scope.$parent.$parent.tomPackage.packageName=$scope.itemArr.choosedItemArr[0].packageName;
	    	 $scope.$parent.$parent.tomPackage.packageId=$scope.itemArr.choosedItemArr[0].packageId;
	     }
	     $scope.openTaskPackage2ModalDialog = false;
	}
	//取消按钮
    $scope.doClose = function () {
        $scope.openTaskPackage2ModalDialog = false;
    } 
}])
//学习人员控制器
.controller('LearnerModalController',['$scope', '$http', 'FileUploader', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, FileUploader, $timeout, dialogService, dialogStatus) {
	$scope.showDeptList = true;
	$scope.deptExchange = function () {
		$scope.showLabelList = false;
    	$scope.showDeptList = true;
    	$scope.getTreeData();
    }
    $scope.labelExchange = function () {
    	$scope.currNode = {};
    	$scope.showDeptList = false;
    	$scope.showLabelList = true;
    	$scope.getlabelClassData();
    	//$scope.getLabelEmp();
    }
    
    var initQuery = function(){
    	$scope.paginationConf.currentPage == 1 ? $scope.paginationConf.itemsPerPage == 20 ? $scope.paginationConf.onChange(): $scope.paginationConf.itemsPerPage = 20 : $scope.paginationConf.currentPage = 1;
    }
    
    $scope.openModalDialog = false;
    $scope.tomPackage={};
    //组织架构树数据
    $scope.formData={};
    $scope.getTreeData = function () {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/orggroups/selectEmp").success(function (response) {
            $scope.empTreeData = response;
            $scope.getDefaultAutoSearchFatherNode($scope.empTreeData);
            $scope.$emit('isLoading', false);
        }).error(function(){
        	$scope.$emit('isLoading', false); 
        	dialogService.setContent("组织架构数据初始化异常").setShowDialog(true);
        });
    }
	$scope.getDefaultAutoSearchFatherNode = function(treeData){
//    	for(var i in treeData){
//    		//选择人员摸态框   组织架构父节点展开 和默认查询父节点下的全部人员数据
//    		if(treeData[i].statuss=="2"&&treeData[i].fathercode==null){
//    			$scope.currNode = treeData[i];
//    			treeData[i].collapsed = true;
//    			treeData[i].selected = 'selected';
//    			break;
//    		}
//    	}
    	//$scope.paginationConf.onChange();
    }
	$scope.getDefaultAutoSearchFatherNodess = function(treeData){
//		for(var i in treeData){
//    		//选择人员摸态框   组织架构父节点展开 和默认查询父节点下的全部人员数据
//    		if(treeData[i].fathercode=="0"){  
//    			$scope.currNode.statuss = undefined;
//    			$scope.currNode.code = undefined;
//    			//break;
//    		}
//    	}
		//$scope.paginationConf.onChange();
    }
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 7,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function(){
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
            if ($scope.showDeptList){
            	$scope.url = "/enterpriseuniversity/services/activity/findEmpPage?pageNum=";
            	if($scope.currNode){
            		$scope.$httpPrams = $scope.$httpPrams+"&statuss="+$scope.currNode.statuss+"&code="+$scope.currNode.code;
            	}else{
                	$scope.paginationConf.totalItems = 0 ;
                	$scope.page = {};
                	return;
                }
            	 if($scope.selectedOption != ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
         			$scope.$httpPrams = $scope.$httpPrams + "&" + $scope.selectedOption + "="
         				+ $scope.optionValue.replace(/\%/g,"%25").replace(/\#/g,"%23")
                 			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
                 			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
             	}
                 //条件查询
                 if($scope.selectedOption != ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
             		$scope.$httpPrams = $scope.$httpPrams + "&" + $scope.selectedOption + "=" 
             			+ $scope.optionValue.replace(/\%/g,"%25").replace(/\#/g,"%23")
                 			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
                 			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
             	} 
                 if ($scope.tomPackage.packageId != "" && $scope.tomPackage.packageId != undefined&& $scope.tomPackage.taskState != undefined) 
                 {
                 	$scope.$httpPrams = $scope.$httpPrams + "&packageId=" + $scope.tomPackage.packageId + "&taskState=" + $scope.tomPackage.taskState;
                 }
             	if($scope.searchOptionCountry !="" && $scope.searchOptionCountry != undefined){
                 	$scope.$httpPrams = $scope.$httpPrams + "&country=" +$scope.selectOptionCountry;
                 }
            }else{
            	$scope.url = "/enterpriseuniversity/services/labelEmp/selectLabelEmpList?pageNum=";
    			$scope.$httpPrams = $scope.$httpPrams+"&statuss="+$scope.currNode.statuss+"&id="+$scope.currNode.code;
            	if($scope.poststat != undefined && $scope.poststat != ""&&$scope.currNode.statuss!=""){
            		$scope.$httpPrams = $scope.$httpPrams+"&poststat="+$scope.poststat;
            	}
            	if($scope.selectedOption != ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
         			$scope.$httpPrams = $scope.$httpPrams + "&" + $scope.selectedOption + "="
         				+ $scope.optionValue.replace(/\%/g,"%25").replace(/\#/g,"%23")
                 			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
                 			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
             	}
                 //条件查询
                 if($scope.selectedOption != ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
             		$scope.$httpPrams = $scope.$httpPrams + "&" + $scope.selectedOption + "=" 
             			+ $scope.optionValue.replace(/\%/g,"%25").replace(/\#/g,"%23")
                 			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
                 			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
             	} 
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
            	+ $scope.$httpPrams;
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
            	$scope.chooseAllItem = false;
    			$scope.page = response;
    			$scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                //勾选后未确定  点击分页项  初始勾选状态
                if ($scope.itemArr.initCurrChoosedItemArrStatus) 
                {
                    $scope.itemArr.initCurrChoosedItemArrStatus();
                }
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("数据查询响应异常，请重新登录后重试").setShowDialog(true);
            });
        }
    };
    
    //标签分类数据
	$scope.getlabelClassData = function () {
		$scope.$emit('isLoading', true);
	    $http.get("/enterpriseuniversity/services/labelClassRelation/tree").success(function (response) {
	    	$scope.$emit('isLoading', false);
	        $scope.labelClassData = response.children;
	        $scope.getDefaultAutoSearchFatherNodess($scope.labelClassData);
	    }).error(function(){
	    	$scope.$emit('isLoading', false);
	    	dialogService.setContent("标签分类数据初始化异常").setShowDialog(true);
	    });
	}
    //查询按钮
    $scope.search = function () {
    	if(!$scope.currNode){
    		dialogService.setContent("请先选择公司或者部门后再查询").setShowDialog(true);
    	}else if($scope.selectedOption == ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
    		dialogService.setContent("请选择查询条件").setShowDialog(true);
    		return;
    	}
    	initQuery();
    };
    //查询条件下拉菜单
    $scope.searchOption = [
       {name: "---查询条件---", value: ""},
       {name: "员工编号", value: "empcode"},
       {name: "员工姓名", value: "name"},
       {name: "员工性别", value: "sex"},
       {name: "所在城市", value: "cityname"},
       {name: "入职时间", value: "begindate"}
    ];
    //选择人员
    $scope.searchOptionCountry = [
                           {name: "---查询语言环境---", value: ""},
                           {name: "中文", value: "ch"},
                           {name: "英文", value: "en"}
                        ];
    //点击树节点      按部门id查询员工列表
	$scope.searchByCondition = function (currNode) {
	    $scope.currNode = currNode;
//	    if($scope.currNode.statuss=="1"){
//	    	//公司
//	    	return ;
//	    }
	    $scope.selectedOption = "";
	    $scope.optionValue = "";
	    $scope.selectOptionCountry = "";
	    $scope.chooseAllItem = false;
	    $scope.search();
	}
	/*///用于查询未通过选择的任务包的员工
    $scope.failed =function(){
    	$scope.tomPackage.taskState="N";
    	$scope.search();
    }
    //用于查询已通过选择的任务包的员工
    $scope.pass =function(){
    	$scope.tomPackage.taskState="Y";
    	$scope.search();
    }
    //重置查询条件
    $scope.reset = function(){
    	$scope.tomPackage = {};
    	$scope.search();
    }*/
	$scope.changeSelectedOption = function(){
		if($scope.currNode){
			if($scope.selectedOption==""){
				$scope.optionValue = "";
			}
    		$scope.search();
    	}
    } 
	$scope.changeSelectedTaskState = function(){
    	$scope.search();
    }
	
	$scope.deleteAll = function(){
		var temp = $scope.itemArr.currChoosedItemArr.concat();
		for (var i in temp){
			$scope.itemArr.tempDeleteItem(temp[i]);
		}
	}
	
    //容器
    $scope.itemArr = {
    	isChooseAllItem: false,
        choosedItemArr: [],
        currChoosedItemArr: [],
        //初始化选中状态
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr){
            	for(var j in $scope.page.data){
            		 if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code){
            			 $scope.page.data[j].checked = true;
            		 }
            	}
            }
            $scope.$emit('isLoading', false);
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.checked){
            	//取消勾选
                item.checked = undefined;
                $scope.chooseAllItem = false;
                for(var i in $scope.itemArr.currChoosedItemArr){
                	if(item.code==$scope.itemArr.currChoosedItemArr[i].code){
                		//从容器中删除取消勾选的项
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                	}
                }
            }else{
            	//勾选
                item.checked = true;
                $scope.itemArr.currChoosedItemArr.push(item);
                var temp = $scope.itemArr.currChoosedItemArr.concat();
                for(var i in temp){
                	if(item.orgcode==temp[i].code||item.deptcode==temp[i].code||item.depttopcode==temp[i].code
                			||item.onedeptcode==temp[i].code||item.tagId==temp[i].code||item.code==temp[i].tagId||item.classId==temp[i].code||item.code==temp[i].classId){
                		$scope.itemArr.tempDeleteItem(temp[i]);
                		if(item.orgcode==temp[i].code||item.deptcode==temp[i].code||item.depttopcode==temp[i].code
                				||item.onedeptcode==temp[i].code||item.tagId==temp[i].code||item.code==temp[i].tagId||item.classId==temp[i].code||item.code==temp[i].classId){
                    		$scope.itemArr.tempDeleteItem(temp[i]);
                        }
                    }
                }
            }
        },
        //模态框中预删除  点击doSure删除生效
        tempDeleteItem : function (item) {
            item.checked = undefined;
            for(var i in $scope.itemArr.currChoosedItemArr){
            	if(item.code==$scope.itemArr.currChoosedItemArr[i].code){
            		$scope.itemArr.currChoosedItemArr.splice(i, 1);
            		if(item.toggleSelected){
            			item.toggleSelected();
            		}
            	}
            }
            //立即从当前页中取消勾选预删除的项
            //其他页取消勾选预删除的项将在分页初始化函数中进行  initCurrChoosedItemArrStatus
            for(var i in $scope.page.data)
            {
            	if($scope.page.data[i].code == item.code)
            	{
            		$scope.page.data[i].checked = undefined;
            		$scope.chooseAllItem = false;
            	}
            }
        },
        //全选
        chooseAllItem : function(){
        	if($scope.chooseAllItem){
        		$scope.chooseAllItem = false;
        	}else{
        		$scope.chooseAllItem = true;
        	}
        	if($scope.chooseAllItem){
        		for(var i in $scope.page.data){
        			for(var j in $scope.itemArr.currChoosedItemArr){
        				var hasContained = false ;
                    	if($scope.page.data[i].code==$scope.itemArr.currChoosedItemArr[j].code){
                    		hasContained = true ;
                    		break;
                    	}
                    }
        			if(!hasContained){
        				$scope.page.data[i].checked = true ;
        				$scope.itemArr.currChoosedItemArr.push($scope.page.data[i]);
        				var temp = $scope.itemArr.currChoosedItemArr.concat();
        				for(var i in temp){
        					if($scope.page.data[i].orgcode==temp[i].code||$scope.page.data[i].deptcode==temp[i].code||$scope.page.data[i].depttopcode==temp[i].code
                        			||$scope.page.data[i].onedeptcode==temp[i].code||$scope.page.data[i].tagId==temp[i].code||$scope.page.data[i].code==temp[i].tagId||$scope.page.data[i].classId==temp[i].code||$scope.page.data[i].code==temp[i].classId){
                        		$scope.itemArr.tempDeleteItem(temp[i]);
                        		if($scope.page.data[i].orgcode==temp[i].code||$scope.page.data[i].deptcode==temp[i].code||$scope.page.data[i].depttopcode==temp[i].code
                        				||$scope.page.data[i].onedeptcode==temp[i].code||$scope.page.data[i].tagId==temp[i].code||$scope.page.data[i].code==temp[i].tagId||$scope.page.data[i].classId==temp[i].code||$scope.page.data[i].code==temp[i].classId){
                            		$scope.itemArr.tempDeleteItem(temp[i]);
                                }
                            }
        				}
        			}
        		} 
        	}else{
        		for(var i in $scope.page.data){
        			var currChoosedItemArrBak = $scope.itemArr.currChoosedItemArr.concat();
        			for(var j in currChoosedItemArrBak){
                    	if($scope.page.data[i].code==currChoosedItemArrBak[j].code){
                    		$scope.page.data[i].checked = false ;
                    		$scope.itemArr.currChoosedItemArr.splice(j, 1); 
                    	}
                    }
        		} 
        	}
        }
    };
    
    //接收编辑页面传值
    if($scope.$parent.$parent.deferred){
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	$scope.itemArr.choosedItemArr = [];
	    	if($scope.formData.deptManagers&&$scope.formData.deptManagers.length<1){
	    		$scope.$parent.$parent.isDept = "N";
	    		for(var i in $scope.formData.emps){
	        		$scope.itemArr.choosedItemArr.push({"type": $scope.formData.emps[i].type,"statuss": $scope.formData.emps[i].statuss, "code":$scope.formData.emps[i].code,"name":$scope.formData.emps[i].name,"cityname":$scope.formData.emps[i].cityname,"deptname":$scope.formData.emps[i].deptname});
	        	}
	    	}
        	$scope.$parent.$parent.choosedEmpsArr = $scope.itemArr.choosedItemArr.concat();
        	$scope.buildEmpIds("Y");
        	//$scope.$emit("$RebuiltEmps");
        }, function(data) {}); 
    }
    
    //接收编辑页面传值
//    if($scope.$parent.$parent.deferred){
//    	$scope.$parent.$parent.deferred.promise.then(function(data) {
//	    	$scope.formData = data; 
//	    	$scope.labelArr.choosedItemArr = [];
//    		for(var i in $scope.formData.labelEmps){
//        		$scope.labelArr.choosedItemArr.push({"code":$scope.formData.labelEmps[i].code});
//        	}
//        	$scope.$parent.$parent.choosedLabelEmpsArr = $scope.labelArr.choosedItemArr.concat();
//        	$scope.buildLabelEmpIds();
//        }, function(data) {}); 
//    }
    
    //选择学习人员按钮,显示模态框
	$scope.$parent.$parent.doOpenLearnerModal = function () {
		if($scope.$parent.$parent.isDept=='N'){
			$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
		}else{
			$scope.itemArr.currChoosedItemArr = [];
			$scope.itemArr.choosedItemArr = [];
		}
        $scope.chooseAllItem = false;
        $scope.selectedOption = ""; 
        $scope.optionValue = "";
        $scope.selectOptionCountry = "";
        $scope.currNode = undefined;
        $scope.paginationConf.totalItems = 0 ;
    	$scope.page = {};
    	$scope.tomPackage={};
    	$scope.showDeptList = true;
        $scope.openModalDialog = true;
        dialogStatus.setHasShowedDialog(true);
        $scope.getTreeData();
    }
	$scope.$parent.$parent.deleteEmpItem = function(item){
		var choosedItemArrBak = $scope.itemArr.choosedItemArr.concat();
		for(var i in choosedItemArrBak){
			var currItem = choosedItemArrBak[i];
			if(item.code == currItem.code){
				$scope.itemArr.choosedItemArr.splice($scope.itemArr.choosedItemArr.indexOf(currItem),1);
			}
		}
		for(var i in $scope.$parent.$parent.formData.emps){
			var currItem = $scope.$parent.$parent.formData.emps[i];
			if(item.code == currItem.code){
				$scope.$parent.$parent.formData.emps.splice($scope.$parent.$parent.formData.emps.indexOf(currItem),1);
			}
		}
		$scope.$parent.$parent.choosedEmpsArr = $scope.itemArr.choosedItemArr.concat();
		$scope.buildEmpIds();
	}
	
	//导入员工功能 s
	$scope.isMerge = "N";//默认
	$scope.toggleMergeType = function(mergeType){
		if(mergeType=="Y"){
			$scope.isMerge = 'Y';
		}else{
			$scope.isMerge = 'N';
		}
	}
	var empUploader = $scope.empUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/uploadEmp',
            autoUpload:true
        }
    );
	empUploader.filters.push({
		name : 'customFilter',
		fn : function(item, options) {
			var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
			if ('|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) == -1) {
				dialogService.setContent("导入文件格式不正确！").setShowDialog(true)
			}
			return '|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) !== -1;
		}
	});
	//上传文件回调函数  回调成功设置表单数据 
	empUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        //回调失败处理
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("文件上传失败！").setShowDialog(true);
        }
        else
        {//回调成功
        	console.info('uploadEmp-response.url', response.url); 
        	$scope.getUploadEmpInfo(response.url); 
        }
    }; 
    $scope.getUploadEmpInfo = function(fileUrl){
    	$scope.$emit('isLoading', true);
    	$http.get("/enterpriseuniversity/services/activity/importEmps?filePath="+fileUrl).success(function (response) {
    		$scope.uploadEmps = response.emps!=undefined?response.emps:[];
    		$scope.uploadFailedEmps = response.errorCodes!=undefined?response.errorCodes:[];
    		//考虑之前勾选的项？
    		if($scope.itemArr.currChoosedItemArr.length>0&&$scope.isMerge=="Y"){
    			var currItem = "";
        		var uploadItem = "";
        		var isContainedMark = false;
        		for(var i in $scope.uploadEmps){
        			uploadItem = $scope.uploadEmps[i];
        			isContainedMark = false;
        			for(var j in $scope.itemArr.currChoosedItemArr){
        				currItem = $scope.itemArr.currChoosedItemArr[j];
        				if(currItem.code==uploadItem.code){
        					isContainedMark = true;
            			}
        			}
        			if(!isContainedMark){
        				$scope.itemArr.currChoosedItemArr.push(uploadItem);
        			}
        		}
    		}else{
    			$scope.itemArr.currChoosedItemArr = $scope.uploadEmps.concat();
    		}
    		
    		if($scope.uploadFailedEmps.length>0){
    			var failedEmpCodes = $scope.uploadFailedEmps.join("、");
    			dialogService.setContent("员工数据导入完毕，仍有以下员工编号未成功导入:" + failedEmpCodes).setShowDialog(true);
    		}else{
    			dialogService.setContent("员工数据导入成功！").setShowSureButton(false).setShowDialog(true);
        		$timeout(function(){
    				dialogService.sureButten_click(); 
    			},2000);
    		}
    		
    		//初始化导入的数据的勾选状态
    		if($scope.page.data){
    			for (var j in $scope.page.data) 
                {
    				$scope.page.data[j].checked = false;
                	for(var i in $scope.itemArr.currChoosedItemArr)
                	{
                		if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code)
                		{
                			$scope.page.data[j].checked = true;
                		}
                	}
                }
    		}
    		$scope.$emit('isLoading', false);
        })
        .error(function(response){
        	$scope.$emit('isLoading', false);
        	dialogService.setContent("查询导入员工数据失败！").setShowDialog(true);
        });
    }
	$scope.buildEmpIds = function(mark){
		$scope.$parent.$parent.formData.empIds = [];
		$scope.$parent.$parent.formData.deptIds = [];
		$scope.$parent.$parent.formData.labelEmps = [];
		$scope.$parent.$parent.formData.labelIds = [];
		$scope.$parent.$parent.formData.labelClassIds = [];
		var empIds = [];
		var deptIds = [];
		var labelEmps = [];
		var labelIds = [];
		var labelClassIds = [];
		if(mark != "Y"){
			for(var i in $scope.itemArr.choosedItemArr){
				if($scope.itemArr.choosedItemArr[i].type!='L'){
					if($scope.itemArr.choosedItemArr[i].statuss!=null){
						$scope.$parent.$parent.formData.deptIds.push($scope.itemArr.choosedItemArr[i].code);
					}else{
						empIds.push($scope.itemArr.choosedItemArr[i].code);
					}
				}else{
					if($scope.itemArr.choosedItemArr[i].statuss!=null){
						if($scope.itemArr.choosedItemArr[i].statuss=='1'||$scope.itemArr.choosedItemArr[i].statuss=='2'){
							$scope.$parent.$parent.formData.labelClassIds.push($scope.itemArr.choosedItemArr[i].code);
						}else{
							$scope.$parent.$parent.formData.labelIds.push($scope.itemArr.choosedItemArr[i].code);
						}
					}else{
						$scope.$parent.$parent.formData.labelEmps.push($scope.itemArr.choosedItemArr[i].code);
					}
				}
			}
		}else {
			var temp ;
			for(var i in $scope.itemArr.choosedItemArr){
				temp = $scope.itemArr.choosedItemArr[i];
				if(temp.type=='D'){
					$scope.$parent.$parent.formData.deptIds.push(temp.code);
				}else if(temp.type=='L'){
					$scope.$parent.$parent.formData.labelIds.push(temp.code);
				}else if(temp.type=='LE'){
					$scope.$parent.$parent.formData.labelEmps.push(temp.code);
				}else if(temp.type=='C'){
					$scope.$parent.$parent.formData.labelClassIds.push(temp.code);
				}else{
					$scope.$parent.$parent.formData.empIds.push(temp.code);
				}
			}
		}
		$scope.$emit("$empIds", empIds);
		$scope.$emit("$RebuiltEmps");
	}
	
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        //$scope.labelArr.choosedItemArr = $scope.labelArr.currChoosedItemArr.concat();
        $scope.$parent.$parent.isDept='N';
        $scope.$parent.$parent.choosedEmpsArr = $scope.itemArr.choosedItemArr;//推送员工
        //$scope.$parent.$parent.choosedLabelEmpsArr = $scope.labelArr.choosedItemArr;
        //清空推送部门负责人容器
        $scope.$parent.$parent.choosedItemArr = [];
        $scope.$parent.$parent.formData.codes = $scope.$parent.$parent.formData.codes==undefined?[]:[];
        $scope.$parent.$parent.formData.deptManagerIds = $scope.$parent.$parent.formData.deptManagerIds==undefined?[]:[];
        $scope.$parent.$parent.formData.deptManagers = $scope.$parent.$parent.formData.deptManagers==undefined?[]:[];
        $scope.$parent.$parent.formData.emps = $scope.$parent.$parent.formData.emps==undefined?[]:[];
    	$scope.buildEmpIds();
    	//$scope.$emit("$RebuiltEmps");
    	//$scope.buildLabelEmpIds();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
.controller('LearnerModalControllers',['$scope', '$http', 'FileUploader', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, FileUploader, $timeout, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activity/findEmpPage?pageNum=";
    $scope.openModalDialog = false;
    $scope.getTreeData = function () {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/orggroups/selectEmp").success(function (response) {
            $scope.empTreeData = response;
            $scope.getDefaultAutoSearchFatherNode($scope.empTreeData);
            $scope.$emit('isLoading', false);
            $scope.paginationConf.onChange();
        }).error(function(){
        	$scope.$emit('isLoading', false); 
        	dialogService.setContent("组织架构数据初始化异常").setShowDialog(true);
        });
    }
    $scope.getDefaultAutoSearchFatherNode = function(treeData){
    	for(var i in treeData){
    		//选择人员摸态框   组织架构父节点展开 和默认查询父节点下的全部人员数据
    		if(treeData[i].statuss=="2"&&treeData[i].fathercode==null){
    			$scope.currNode = treeData[i];
    			treeData[i].collapsed = true;
    			treeData[i].selected = 'selected';
    			break;
    		}
    	}
    }
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 7,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
            if ($scope.currNode){
            	$scope.$httpPrams = $scope.$httpPrams+"&statuss="+$scope.currNode.statuss+"&code="+$scope.currNode.code;
            }else{
            	$scope.paginationConf.totalItems = 0 ;
            	$scope.page = {};
            	return;
            }
            //条件查询
            if($scope.selectedOption != ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
        		$scope.$httpPrams = $scope.$httpPrams + "&" + $scope.selectedOption + "=" 
        			+ $scope.optionValue.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
        	} 
             
            if($scope.searchOptionCountry !="" && $scope.searchOptionCountry != undefined){
            	$scope.$httpPrams = $scope.$httpPrams + "&country=" +$scope.selectOptionCountry;
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
            	+ $scope.$httpPrams;
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
            	$scope.chooseAllItem = false;
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                //勾选后未确定  点击分页项  初始勾选状态
                if ($scope.itemArr.initCurrChoosedItemArrStatus){
                    $scope.itemArr.initCurrChoosedItemArrStatus();
                }
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("数据查询响应异常，请重新登录后重试").setShowDialog(true);
            });
        }
    };
    
    //查询按钮
    $scope.search = function () {
    	if(!$scope.currNode){
    		dialogService.setContent("请先选择公司或者部门后再查询").setShowDialog(true);
    	}else if($scope.selectedOption == ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
    		dialogService.setContent("请选择查询条件").setShowDialog(true);
    		return;
    	}
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
    	$scope.paginationConf.onChange();
    };
    //查询条件下拉菜单
    $scope.searchOption = [
       {name: "---查询条件---", value: ""},
       {name: "员工编号", value: "empcode"},
       {name: "员工姓名", value: "name"},
       {name: "员工性别", value: "sex"},
       {name: "所在城市", value: "cityname"},
       {name: "入职时间", value: "begindate"}
    ];
    //选择人员
    $scope.searchOptionCountry = [
                           {name: "---查询语言环境---", value: ""},
                           {name: "中文", value: "ch"},
                           {name: "英文", value: "en"}
                        ];
    //点击树节点      按部门id查询员工列表
	$scope.searchByCondition = function (currNode) {
	    $scope.currNode = currNode;
	    if($scope.currNode.statuss=="1"){
	    	//公司
	    	return ;
	    }
	    $scope.selectedOption = "";
	    $scope.optionValue = "";
	    $scope.selectOptionCountry = "";
	    $scope.chooseAllItem = false;
	    $scope.search();
	}
    
    
    //选择学习人员按钮,显示模态框
	$scope.$parent.$parent.doOpenLearnerModal = function () {
 
        $scope.openModalDialog = true;
        dialogStatus.setHasShowedDialog(true);
        $scope.getTreeData();
    }
	
	$scope.changeSelectedOption = function(){
		if($scope.currNode){
			if($scope.selectedOption==""){
				$scope.optionValue = "";
			}
    		$scope.search();
    	}
    } 
	$scope.changeSelectedTaskState = function(){
    	$scope.search();
    }
    //容器
    $scope.itemArr = {
    	isChooseAllItem: false,
        choosedItemArr: [],
        currChoosedItemArr: [],
        //初始化选中状态
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr){
            	for(var j in $scope.page.data){
            		 if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code){
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
            	//取消勾选
                item.checked = undefined;
                $scope.chooseAllItem = false;
                for(var i in $scope.itemArr.currChoosedItemArr)
                {
                	if(item.code==$scope.itemArr.currChoosedItemArr[i].code)
                	{
                		//从容器中删除取消勾选的项
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                	}
                }
            } 
            else
            {
            	//勾选
                item.checked = true;
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        //模态框中预删除  点击doSure删除生效
        tempDeleteItem : function (item) {
            item.checked = undefined;
            for(var i in $scope.itemArr.currChoosedItemArr)
            {
            	if(item.code==$scope.itemArr.currChoosedItemArr[i].code)
            	{
            		$scope.itemArr.currChoosedItemArr.splice(i, 1);
            	}
            }
            //立即从当前页中取消勾选预删除的项
            //其他页取消勾选预删除的项将在分页初始化函数中进行  initCurrChoosedItemArrStatus
            for(var i in $scope.page.data)
            {
            	if($scope.page.data[i].code == item.code)
            	{
            		$scope.page.data[i].checked = undefined;
            		$scope.chooseAllItem = false;
            	}
            }
        },
        //全选
        chooseAllItem : function(){
        	//...
        	if($scope.chooseAllItem){
        		$scope.chooseAllItem = false;
        	}else{
        		$scope.chooseAllItem = true;
        	}
        	if($scope.chooseAllItem){
        		for(var i in $scope.page.data){
        			for(var j in $scope.itemArr.currChoosedItemArr)
                    {
        				var hasContained = false ;
                    	if($scope.page.data[i].code==$scope.itemArr.currChoosedItemArr[j].code)
                    	{
                    		hasContained = true ;
                    		break;
                    	}
                    }
        			if(!hasContained){
        				$scope.page.data[i].checked = true ;
        				$scope.itemArr.currChoosedItemArr.push($scope.page.data[i]);
        			}
        		} 
        	}else{
        		for(var i in $scope.page.data){
        			var currChoosedItemArrBak = $scope.itemArr.currChoosedItemArr.concat();
        			for(var j in currChoosedItemArrBak)
                    {
                    	if($scope.page.data[i].code==currChoosedItemArrBak[j].code)
                    	{
                    		$scope.page.data[i].checked = false ;
                    		$scope.itemArr.currChoosedItemArr.splice(j, 1); 
                    	}
                    }
        		} 
        	}
        }
    };
    //接收编辑页面传值
    if($scope.$parent.$parent.deferred){
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	$scope.itemArr.choosedItemArr = [];
	    	if(($scope.formData.emps&&$scope.formData.emps.length>0) || $scope.module){
	    		$scope.$parent.$parent.isDept = "N";
	    		for(var i in $scope.formData.emps){
	        		$scope.itemArr.choosedItemArr.push({"code":$scope.formData.emps[i].code,"name":$scope.formData.emps[i].name,"cityname":$scope.formData.emps[i].cityname,"deptname":$scope.formData.emps[i].deptname});
	        	}
	    	}
        	$scope.$parent.$parent.choosedEmpsArr = $scope.itemArr.choosedItemArr.concat();
        	$scope.buildEmpIds();
        }, function(data) {}); 
    }
    //选择学习人员按钮,显示模态框
	$scope.$parent.$parent.doOpenLearnerModal = function () {
		if($scope.$parent.$parent.isDept=='N'){
			$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
		}else{
			$scope.itemArr.currChoosedItemArr = [];
			$scope.itemArr.choosedItemArr = [];
		}
        $scope.chooseAllItem = false;
        $scope.selectedOption = ""; 
        $scope.optionValue = "";
        $scope.selectOptionCountry = "";
        $scope.currNode = undefined;
        $scope.paginationConf.totalItems = 0 ;
    	$scope.page = {};
        $scope.openModalDialog = true;
        dialogStatus.setHasShowedDialog(true);
        $scope.getTreeData();
    }
	$scope.$parent.$parent.deleteEmpItem = function(item){
		var choosedItemArrBak = $scope.itemArr.choosedItemArr.concat();
		for(var i in choosedItemArrBak){
			var currItem = choosedItemArrBak[i];
			if(item.code == currItem.code){
				$scope.itemArr.choosedItemArr.splice($scope.itemArr.choosedItemArr.indexOf(currItem),1);
			}
		}
		$scope.$parent.$parent.choosedEmpsArr = $scope.itemArr.choosedItemArr.concat();
		$scope.buildEmpIds();
	}
	//导入员工功能 s
	$scope.isMerge = "N";//默认
	$scope.toggleMergeType = function(mergeType){
		if(mergeType=="Y"){
			$scope.isMerge = 'Y';
		}else{
			$scope.isMerge = 'N';
		}
	}
	var empUploader = $scope.empUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/uploadEmp',
            autoUpload:true
        }
    );
	empUploader.filters.push({
		name : 'customFilter',
		fn : function(item, options) {
			var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
			if ('|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) == -1) {
				dialogService.setContent("导入文件格式不正确！").setShowDialog(true)
			}
			return '|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) !== -1;
		}
	});
	//上传文件回调函数  回调成功设置表单数据 
	empUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        //回调失败处理
        if(response.result=="error"){
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("文件上传失败！").setShowDialog(true);
        }
        else
        {//回调成功
        	console.info('uploadEmp-response.url', response.url); 
        	$scope.getUploadEmpInfo(response.url); 
        }
    }; 
    $scope.getUploadEmpInfo = function(fileUrl){
    	$scope.$emit('isLoading', true);
    	$http.get("/enterpriseuniversity/services/activity/importEmps?filePath="+fileUrl).success(function (response) {
    		$scope.uploadEmps = response.emps!=undefined?response.emps:[];
    		$scope.uploadFailedEmps = response.errorCodes!=undefined?response.errorCodes:[];
    		//考虑之前勾选的项？
    		if($scope.itemArr.currChoosedItemArr.length>0&&$scope.isMerge=="Y"){
    			var currItem = "";
        		var uploadItem = "";
        		var isContainedMark = false;
        		for(var i in $scope.uploadEmps){
        			uploadItem = $scope.uploadEmps[i];
        			isContainedMark = false;
        			for(var j in $scope.itemArr.currChoosedItemArr){
        				currItem = $scope.itemArr.currChoosedItemArr[j];
        				if(currItem.code==uploadItem.code){
        					isContainedMark = true;
            			}
        			}
        			if(!isContainedMark){
        				$scope.itemArr.currChoosedItemArr.push(uploadItem);
        			}
        		}
    		}else{
    			$scope.itemArr.currChoosedItemArr = $scope.uploadEmps.concat();
    		}
    		
    		if($scope.uploadFailedEmps.length>0){
    			var failedEmpCodes = $scope.uploadFailedEmps.join("、");
    			dialogService.setContent("员工数据导入完毕，仍有以下员工编号未成功导入:" + failedEmpCodes).setShowDialog(true);
    		}else{
    			dialogService.setContent("员工数据导入成功！").setShowSureButton(false).setShowDialog(true);
        		$timeout(function(){
    				dialogService.sureButten_click(); 
    			},2000);
    		}
    		
    		//初始化导入的数据的勾选状态
    		if($scope.page.data){
    			for (var j in $scope.page.data) 
                {
    				$scope.page.data[j].checked = false;
                	for(var i in $scope.itemArr.currChoosedItemArr)
                	{
                		if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code)
                		{
                			$scope.page.data[j].checked = true;
                		}
                	}
                }
    		}
    		$scope.$emit('isLoading', false);
        })
        .error(function(response){
        	$scope.$emit('isLoading', false);
        	dialogService.setContent("查询导入员工数据失败！").setShowDialog(true);
        });
    }
	$scope.buildEmpIds = function(){
		$scope.$parent.$parent.formData.empIds = [];
		if($scope.itemArr.choosedItemArr.length<1){
			$scope.$parent.$parent.formData.empIds = null;
			return;
		}
		for(var i in $scope.itemArr.choosedItemArr){
			$scope.$parent.$parent.formData.empIds.push($scope.itemArr.choosedItemArr[i].code);
		}
	}
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.$parent.$parent.choosedEmpsArr = $scope.itemArr.choosedItemArr;//推送员工
        $scope.buildEmpIds();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])

//部门负责人控制器
.controller('ManagerModalController',['$scope', '$http', 'FileUploader', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, FileUploader, $timeout, dialogService, dialogStatus) {
    $scope.url = "/enterpriseuniversity/services/activity/findEmpPage?pageNum=";
    $scope.openModalDialog = false;
    $scope.tomPackage={};
    //组织架构树数据
    $scope.formData={};
    $scope.getTreeData = function () {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/orggroups/selectEmp").success(function (response) {
            $scope.empTreeData = response;
            $scope.getDefaultAutoSearchFatherNode($scope.empTreeData);
            $scope.$emit('isLoading', false);
            $scope.paginationConf.onChange();
        }).error(function(){
        	$scope.$emit('isLoading', false);
        });
    }
    $scope.getDefaultAutoSearchFatherNode = function(treeData){
    	for(var i in treeData){
    		//选择人员摸态框   组织架构父节点展开 和默认查询父节点下的全部人员数据
    		if(treeData[i].statuss=="2"&&treeData[i].fathercode==null){
    			$scope.currNode = treeData[i];
    			treeData[i].collapsed = true;
    			treeData[i].selected = 'selected';
    			break;
    		}
    	}
    }
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 5,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
        	$scope.chooseAllItem = false;
            if ($scope.currNode)
            {
            	$scope.$httpPrams = $scope.$httpPrams+"&statuss="+$scope.currNode.statuss+"&code="+$scope.currNode.code;
            }
            else
            {
            	$scope.paginationConf.totalItems = 0 ;
            	$scope.page = {};
            	return;
            }
            //条件查询
            if($scope.selectedOption != ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
        		$scope.$httpPrams = $scope.$httpPrams + "&" + $scope.selectedOption + "=" 
        			+ $scope.optionValue.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
        	} 
            if ($scope.tomPackage.packageId != "" && $scope.tomPackage.packageId != undefined && $scope.tomPackage.taskState != undefined && $scope.tomPackage.taskState != "") 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&packageId=" + $scope.tomPackage.packageId + "&taskState=" + $scope.tomPackage.taskState;
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
            	+ $scope.$httpPrams;
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
            	$scope.chooseAllItem = false;
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                //勾选后未确定  点击分页项  初始勾选状态
                if ($scope.itemArr.initCurrChoosedItemArrStatus) 
                {
                    $scope.itemArr.initCurrChoosedItemArrStatus();
                }
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("数据查询响应异常，请重新登录后重试").setShowDialog(true);
            });
        }
    };
    $scope.search = function () {
    	if(!$scope.currNode){
    		dialogService.setContent("请先选择公司或者部门后再查询").setShowDialog(true);
    	}else if($scope.selectedOption == ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
    		dialogService.setContent("请选择查询条件").setShowDialog(true);
    		return;
    	}
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
    	$scope.paginationConf.onChange();
    };
    $scope.searchOption = [
           {name: "---查询条件---", value: ""},
           {name: "员工编号", value: "empcode"},
           {name: "员工姓名", value: "name"},
           {name: "员工性别", value: "sex"},
           {name: "所在城市", value: "cityname"}
    ];
    //按部门id查询员工列表
    $scope.searchByCondition = function (currNode) {
        $scope.currNode = currNode;
        $scope.chooseAllItem = false;
        $scope.selectedOption = "";
        $scope.optionValue = "";
        $scope.search();
    }
    //改变查询条件
    $scope.changeSelectedOption = function(){
    	if($scope.currNode){
    		if($scope.selectedOption==""){
				$scope.optionValue = "";
			}
    		$scope.search();
    	}
    }
    $scope.changeSelectedTaskState = function(){
    	$scope.search();
    }
    $scope.itemArr = {
        choosedItemArr: [],
        currChoosedItemArr: [],
        choosedDeptHeaderItemArr: [],
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr){
            	for(var j in $scope.page.data){
            		 if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code){
            			 $scope.page.data[j].checked = true;
            		 }
            	}
            }
            $scope.$emit('isLoading', false);
        },
        //添加部门负责人
        putDeptHeaderItem : function(item){//N
        	var hasContained = false;
        	for(var i in $scope.itemArr.currChoosedItemArr){
        		var currItem = $scope.itemArr.currChoosedItemArr[i];
         		if(item.deptpsncode == currItem.code){
         			hasContained = true;
         			break;
        		}
        	}
        	var hasDeptContained = false;
        	for(var i in $scope.itemArr.choosedDeptHeaderItemArr){
        		var currItem = $scope.itemArr.choosedDeptHeaderItemArr[i];
         		if(item.deptpsncode == currItem.code){
         			hasDeptContained = true;
         			break;
        		}
        	}
        	if(item.deptpsncode==item.code){
            	//部门负责人 to choosedDeptHeaderItemArr
        		if(!hasDeptContained){
        			$scope.itemArr.choosedDeptHeaderItemArr.push(item);
        		}
            	if(!hasContained){
            		$scope.itemArr.currChoosedItemArr.push(item);
            	}
            }else{
            	//非部门负责人 to choosedDeptHeaderItemArr
            	if(!hasDeptContained){
            		$scope.itemArr.choosedDeptHeaderItemArr.push({code:item.deptpsncode,name:item.deptpsnname,deptcode:item.deptcode,deptname:item.deptname,cityname:item.cityname,deptpsncode:item.deptpsncode});
    			}
            	if(!hasContained){
            		$scope.itemArr.currChoosedItemArr.push({code:item.deptpsncode,name:item.deptpsnname,deptcode:item.deptcode,deptname:item.deptname,cityname:item.cityname,deptpsncode:item.deptpsncode});
            	}
            }
        	//放入部门负责人currChoosedItemArr
        	if(!hasContained){
        		//立即选中当前页的部门负责人数据
        		for(var i in $scope.page.data)
                {
                	if($scope.page.data[i].code == item.deptpsncode)
                	{
                		$scope.page.data[i].checked = true;
                	}
                }
			}
        },
        buildDeptHeaderArr:function(){//Y
        	$scope.itemArr.choosedDeptHeaderItemArr = [];
        	for(var i in $scope.itemArr.currChoosedItemArr)
            {
        		var currChoosedItem = $scope.itemArr.currChoosedItemArr[i];
        		var hasDeptContained = false;
            	for(var j in $scope.itemArr.choosedDeptHeaderItemArr){
            		var choosedDeptHeaderItem = $scope.itemArr.choosedDeptHeaderItemArr[j];
             		if(currChoosedItem.deptpsncode == choosedDeptHeaderItem.code){
             			hasDeptContained = true;
             			break;
            		}
            	}
            	if(!hasDeptContained){
            		if(currChoosedItem.deptpsncode == currChoosedItem.code){//部门负责人
            			$scope.itemArr.choosedDeptHeaderItemArr.push(currChoosedItem);
            		}else{//普通员工  手动生成一个部门负责人
            			$scope.itemArr.choosedDeptHeaderItemArr.push(
        					{
        						code:currChoosedItem.deptpsncode,
        						name:currChoosedItem.deptpsnname,
	    						deptcode:currChoosedItem.deptcode,
	    						deptname:currChoosedItem.deptname,
	    						cityname:currChoosedItem.cityname,
	    						deptpsncode:currChoosedItem.deptpsncode});
            		}
            	}
            }
        },
        //删除部门负责人
        deleteDeptHeaderItem:function(item){//N
        	var currChoosedItemArrBak = $scope.itemArr.currChoosedItemArr.concat();
        	for(var j in currChoosedItemArrBak){
        		var currItem = currChoosedItemArrBak[j];
        		if(item.code==currItem.deptpsncode)
            	{
        			$scope.itemArr.currChoosedItemArr.splice($scope.itemArr.currChoosedItemArr.indexOf(currItem), 1);
            	}
        	}
        	for(var k in $scope.itemArr.choosedDeptHeaderItemArr){
        		var currItem = $scope.itemArr.choosedDeptHeaderItemArr[k];
         		if(item.code == currItem.code){
         			$scope.itemArr.choosedDeptHeaderItemArr.splice(k, 1); 
         			break;
        		}
        	}
        	for(var i in $scope.page.data)
            {
            	if($scope.page.data[i].deptpsncode == item.code)
            	{
            		$scope.chooseAllItem = false;
            		$scope.page.data[i].checked = undefined;
            	}
            }
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item) {//Y
            if (item.checked)
            {//取消选中
            	/*
                if(item.code!=item.deptpsncode){
                	for(var i in $scope.itemArr.currChoosedItemArr)
                    {
                    	if(item.code==$scope.itemArr.currChoosedItemArr[i].code)
                    	{
                    		//取消选中
                    		$scope.chooseAllItem = false;
                            item.checked = undefined;
                    		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                    		break;
                    	}
                    }
                }else{
                	//删除部门负责人
                    $scope.itemArr.deleteDeptHeaderItem(item);
                }
                */
            	for(var i in $scope.itemArr.currChoosedItemArr)
                {
                	if(item.code==$scope.itemArr.currChoosedItemArr[i].code)
                	{
                		//取消选中全选
                		$scope.chooseAllItem = false;
                        item.checked = undefined;
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                		break;
                	}
                }
            	//重新生成部门负责人数组
            	$scope.itemArr.buildDeptHeaderArr();
            } 
            else
            {	//选中
            	item.checked = true;
            	/*
                if(item.code!=item.deptpsncode){
                	$scope.itemArr.currChoosedItemArr.push(item);
                }
                */
            	$scope.itemArr.currChoosedItemArr.push(item);
            	//添加部门负责人
                //$scope.itemArr.putDeptHeaderItem(item);
            	$scope.itemArr.buildDeptHeaderArr();
            }
        },
        //模态框中预删除  点击doSure删除生效
        tempDeleteItem : function (item) {//Y
        	item.checked = undefined;
        	$scope.chooseAllItem = false;
        	for(var i in $scope.itemArr.currChoosedItemArr){
            	if(item.code==$scope.itemArr.currChoosedItemArr[i].code){
            		$scope.itemArr.currChoosedItemArr.splice(i, 1);
            		break;
            	}
            }
            for(var i in $scope.page.data){
            	if($scope.page.data[i].code == item.code){
            		$scope.page.data[i].checked = undefined;
            		break;
            	}
            }
            $scope.itemArr.buildDeptHeaderArr();
        },
        //全选
        chooseAllItem : function(){//Y
        	if($scope.chooseAllItem){
        		$scope.chooseAllItem = false;
        	}else{
        		$scope.chooseAllItem = true;
        	}
        	if($scope.chooseAllItem){
        		for(var i in $scope.page.data){
        			var item = $scope.page.data[i];
        			item.checked = true ;
    				for(var j in $scope.itemArr.currChoosedItemArr)
                    {
        				var hasContained = false ;
                    	if(item.code==$scope.itemArr.currChoosedItemArr[j].code)
                    	{
                    		hasContained = true ;
                    		break;
                    	}
                    }
        			if(!hasContained){
        				$scope.itemArr.currChoosedItemArr.push(item);
        			}
    				//添加部门负责人
    				//$scope.itemArr.putDeptHeaderItem(item);
        		}
        		$scope.itemArr.buildDeptHeaderArr();
        	}else{
        		for(var i in $scope.page.data){
        			var item = $scope.page.data[i];
        			item.checked = false ;
        			var currChoosedItemArrBak = $scope.itemArr.currChoosedItemArr.concat();
        			for(var j in currChoosedItemArrBak)
                    {
                    	if(item.code==currChoosedItemArrBak[j].code)
                    	{
                    		//if(item.code!=item.deptpsncode){
                    		$scope.itemArr.currChoosedItemArr.splice($scope.itemArr.currChoosedItemArr.indexOf(currChoosedItemArrBak[j]), 1); 
                    		//}
                			//删除部门负责人
                    		//$scope.itemArr.deleteDeptHeaderItem(currChoosedItemArrBak[j]);
                    	}
                    }
        		} 
        		$scope.itemArr.buildDeptHeaderArr();
        	}
        }
    };
    //接收编辑页面传值
    if($scope.$parent.$parent.deferred){
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	$scope.itemArr.choosedItemArr = [];
	    	if($scope.formData.deptManagers&&$scope.formData.deptManagers.length>0){
	    		$scope.$parent.$parent.isDept = "Y";
	    		$scope.itemArr.choosedDeptHeaderItemArr = $scope.formData.deptManagers;
	    		$scope.itemArr.choosedItemArr = $scope.formData.emps;
	    		$scope.$parent.$parent.choosedItemArr = $scope.itemArr.choosedDeptHeaderItemArr.concat();
	    	} 
	    	$scope.buildParentFormData();
        }, function(data) {}); 
    }
    //推送部门负责人按钮,显示模态框
	$scope.$parent.$parent.doOpenManagerModal = function () {
		if($scope.$parent.$parent.isDept=='Y'){
			$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
		}else{
			$scope.itemArr.choosedDeptHeaderItemArr = [];
			$scope.itemArr.currChoosedItemArr = [];
			$scope.itemArr.choosedItemArr = [];
		}
        $scope.chooseAllItem = false;
        $scope.selectedOption = ""; 
        $scope.optionValue = "";
        $scope.tomPackage = {};
        $scope.currNode = undefined;
        $scope.paginationConf.totalItems = 0 ;
    	$scope.page = {};
        $scope.openModalDialog = true;
        dialogStatus.setHasShowedDialog(true);
        $scope.getTreeData();
    }
	//导入员工功能 s
	$scope.isMerge = "N";//默认
	$scope.toggleMergeType = function(mergeType){
		if(mergeType=="Y"){
			$scope.isMerge = 'Y';
		}else{
			$scope.isMerge = 'N';
		}
	}
	var empUploader = $scope.empUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/uploadEmp',
            autoUpload:true
        }
    );
	empUploader.filters.push({
		name : 'customFilter',
		fn : function(item, options) {
			var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
			if ('|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) == -1) {
				dialogService.setContent("导入文件格式不正确！").setShowDialog(true)
			}
			return '|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) !== -1;
		}
	});
	//上传文件回调函数  回调成功设置表单数据 
	empUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        //回调失败处理
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("文件上传失败！").setShowDialog(true);
        }
        else
        {//回调成功
        	console.info('uploadEmp-response.url', response.url); 
        	$scope.getUploadEmpInfo(response.url); 
        }
    }; 
    $scope.getUploadEmpInfo = function(fileUrl){
    	$scope.$emit('isLoading', true);
    	$http.get("/enterpriseuniversity/services/activity/importEmps?filePath="+fileUrl).success(function (response) {
    		$scope.uploadEmps = response.emps!=undefined?response.emps:[];
    		$scope.uploadFailedEmps = response.errorCodes!=undefined?response.errorCodes:[];
    		//考虑之前勾选的项？
    		if($scope.itemArr.currChoosedItemArr.length>0&&$scope.isMerge=="Y"){
    			var currItem = "";
        		var uploadItem = "";
        		var isContainedMark = false;
        		for(var i in $scope.uploadEmps){
        			uploadItem = $scope.uploadEmps[i];
        			isContainedMark = false;
        			for(var j in $scope.itemArr.currChoosedItemArr){
        				currItem = $scope.itemArr.currChoosedItemArr[j];
        				if(currItem.code==uploadItem.code){
        					isContainedMark = true;
            			}
        			}
        			if(!isContainedMark){
        				$scope.itemArr.currChoosedItemArr.push(uploadItem);
        			}
        		}
    		}else{
    			$scope.itemArr.currChoosedItemArr = $scope.uploadEmps.concat();
    		}
    		
    		$scope.itemArr.buildDeptHeaderArr();//生成部门负责人数组
    		
    		if($scope.uploadFailedEmps.length>0){
    			var failedEmpCodes = $scope.uploadFailedEmps.join("、");
    			dialogService.setContent("员工数据导入完毕，仍有以下员工编号未成功导入:" + failedEmpCodes).setShowDialog(true);
    		}else{
    			dialogService.setContent("员工数据导入成功！").setShowSureButton(false).setShowDialog(true);
        		$timeout(function(){
    				dialogService.sureButten_click(); 
    			},2000);
    		}
    		
    		//初始化导入的数据的勾选状态
    		if($scope.page.data){
    			for (var j in $scope.page.data) 
                {
    				$scope.page.data[j].checked = false;
                	for(var i in $scope.itemArr.currChoosedItemArr)
                	{
                		if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code)
                		{
                			$scope.page.data[j].checked = true;
                		}
                	}
                }
    		}
    		$scope.$emit('isLoading', false);
        })
        .error(function(response){
        	$scope.$emit('isLoading', false);
        	dialogService.setContent("查询导入员工数据失败！").setShowDialog(true);
        });
    }
    //导入员工功能 e
	$scope.$parent.$parent.deleteDeptHeaderItem = function(item){
		var choosedItemArrBak = $scope.itemArr.choosedItemArr.concat();
		for(var j in choosedItemArrBak)
        {
			var currItem = choosedItemArrBak[j];
        	if(item.code==currItem.deptpsncode)
        	{
        		$scope.itemArr.choosedItemArr.splice($scope.itemArr.choosedItemArr.indexOf(currItem), 1);
        	}
        }
		var choosedDeptHeaderItemArrBak = $scope.itemArr.choosedDeptHeaderItemArr.concat();
		for(var i in choosedDeptHeaderItemArrBak){
			var currItem = choosedDeptHeaderItemArrBak[i];
			if(item.code == currItem.code){
				$scope.itemArr.choosedDeptHeaderItemArr.splice($scope.itemArr.choosedDeptHeaderItemArr.indexOf(currItem), 1);
			}
		}
		$scope.$parent.$parent.choosedItemArr = $scope.itemArr.choosedDeptHeaderItemArr.concat();
		$scope.buildParentFormData();
	}
	$scope.buildParentFormData = function(){
		$scope.$parent.$parent.formData.codes = [];
		$scope.$parent.$parent.formData.deptManagerIds = [];
		for(var i in $scope.itemArr.choosedDeptHeaderItemArr){
			var deptHeaderItem = $scope.itemArr.choosedDeptHeaderItemArr[i]; 
			var empStr = "";
			for(var j in $scope.itemArr.choosedItemArr){
				var empItem = $scope.itemArr.choosedItemArr[j];
				if(empItem.deptpsncode==deptHeaderItem.code){
					empStr = empStr + ","+ empItem.code;
				}
			}
			$scope.$parent.$parent.formData.deptManagerIds.push(deptHeaderItem.code);
			$scope.$parent.$parent.formData.codes.push(empStr);
		}
	}
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.$parent.$parent.choosedItemArr = $scope.itemArr.choosedDeptHeaderItemArr;//推送部门负责人
        $scope.buildParentFormData();
        $scope.$parent.$parent.isDept='Y'; 
        $scope.$parent.$parent.choosedEmpsArr = [];//清空员工容器
        $scope.$parent.$parent.formData.empIds = $scope.$parent.$parent.formData.empIds !=undefined ? []:[]; 
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
//选择前置任务模态框
.controller('PreTaskModelController', ['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
	$scope.openModalDialog = false;
	//$scope.$parent.$parent.formData.preTaskInfo=[];
	$scope.$parent.$parent.choosePretask = function (item,index) {
		$scope.currItem = item;
		$scope.openModalDialog = true;
		dialogStatus.setHasShowedDialog(true);
		$scope.itemArr.choosedItemArr = item.choosedItemArr!=undefined?item.choosedItemArr.concat():[];
		$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
		$scope.page={data:[]};
		for(var i = 0; i < $scope.$parent.$parent.taskPackage.length; i++){
			var taskCoursesOrExamPapers = $scope.$parent.$parent.taskPackage[i].taskCoursesOrExamPapers.concat();
			for(var j=0;j< taskCoursesOrExamPapers.length; j++){
				if(j<index){
					$scope.page.data.push(taskCoursesOrExamPapers[j]); 
				}
			}
		}
		$scope.itemArr.initCurrChoosedItemArrStatus();
  	}
	$scope.itemArr = {
        	isChooseAllItem: false,
        	oldChooseItemArr: [],
            choosedItemArr: [],
            currChoosedItemArr: [],
            //选中choosedItemArr中的项           
            initCurrChoosedItemArrStatus: function () {
            	for(var j in $scope.page.data){
            		$scope.page.data[j].checked = false;
            		var itemArr = [];
            		var needRebuild = false;
            		for (var i in $scope.itemArr.currChoosedItemArr) {
            			//alert($scope.itemArr.currChoosedItemArr[i].examPaperId);
		        		 /*if(($scope.page.data[j].courseId&&$scope.page.data[j].courseId==$scope.itemArr.currChoosedItemArr[i].courseId)
		        				 ||($scope.page.data[j].examPaperId&&$scope.page.data[j].examPaperId==$scope.itemArr.currChoosedItemArr[i].examPaperId)){
		        			 $scope.page.data[j].checked = true;
		        		 }*/
            			//alert($scope.page.data[j].sort+"=====-"+$scope.itemArr.currChoosedItemArr[i]);
            			if($scope.page.data[j].sort==$scope.itemArr.currChoosedItemArr[i].sort){
            				//编辑页面数据
            				if($scope.itemArr.currChoosedItemArr[i].sort==undefined||$scope.itemArr.currChoosedItemArr[i].preTaskName==undefined){
            					itemArr.push($scope.page.data[j]);
            					needRebuild = true;
            				}
            				$scope.page.data[j].checked = true;
            			}
                	}
            		//编辑页面数据
            		if(needRebuild){
            			$scope.itemArr.currChoosedItemArr = itemArr;
    				}
                }
            },
            //勾选 or 取消勾选操作
            chooseItem : function (item,index) {
                if (item.checked) 
                {
                	item.checked = undefined;
                    for(var i in $scope.itemArr.currChoosedItemArr)
                    {
                    	if(item.courseId&&item.courseId==$scope.itemArr.currChoosedItemArr[i].courseId||item.examPaperId&&item.examPaperId==$scope.itemArr.currChoosedItemArr[i].examPaperId){
                    		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                    	}
                    	/*else if(item.examPaperId&&item.examPaperId==$scope.itemArr.currChoosedItemArr[i].examPaperId){
                    		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                    	}*/
                    }
                } 
                else 
                {
                    item.checked = true;
                    //item.preTaskName="任务"+(index+1);
                    //item.sort = 
                    $scope.itemArr.currChoosedItemArr.push(item);
                }
            } ,
            //全选
            chooseAllItem : function(){
            	//...
            	if(this.isChooseAllItem){
            		this.isChooseAllItem = false;
            		
            		
            	}else{
            		this.isChooseAllItem = false;
            	}
            }           
    };
	$scope.buildPreTaskInfo=function(){
		var currItemPreTaskNameArry = [];
		var currItemPreTaskIdArry = [];
		for(var i=0;i<$scope.itemArr.choosedItemArr.length;i++){
			currItemPreTaskNameArry.push($scope.itemArr.choosedItemArr[i].preTaskName);
			/*if($scope.itemArr.choosedItemArr[i].courseId){
				currItemPreTaskIdArry.push($scope.itemArr.choosedItemArr[i].courseId);
			}else if($scope.itemArr.choosedItemArr[i].examPaperId){
				currItemPreTaskIdArry.push($scope.itemArr.choosedItemArr[i].examPaperId);
			}*/
			currItemPreTaskIdArry.push($scope.itemArr.choosedItemArr[i].sort);
		}
		currItemPreTaskNameArry.sort(function(a,b){
			return Number(a.replace("任务",""))-Number(b.replace("任务",""));
		});
		$scope.currItem.preTaskNames = currItemPreTaskNameArry.join(","); 
		$scope.currItem.preTaskIds = currItemPreTaskIdArry.join(","); 
	}
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.currItem.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildPreTaskInfo();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
.controller('CertificateController', ['$scope', '$http', 'dialogService', '$timeout', function ($scope, $http ,dialogService,$timeout) {
    //用于显示面包屑状态
    $scope.$emit("cm", {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cc', cmn: '证书管理'});
    //http请求url
    $scope.url = "/enterpriseuniversity/services/certificateManage/certificateManageList?pageNum=";
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
            if ($scope.activityName != undefined && $scope.activityName != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
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
            }).error(function(rs){
            	$scope.$emit('isLoading', false);
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
    //添加
    $scope.doAdd = function(){
    	$scope.go('/activity/addCertificate', 'addCertificate', null);
    }
    //删除
    $scope.doDelete = function(t){
    	dialogService.setContent("确定删除培训活动?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
        	$http({
        		url: "/enterpriseuniversity/services/activity/delete?activityId=" + t.activityId,
                method: "DELETE"
            }).success(function (response) {
            	//延迟弹框
		    	$timeout(function(){
		    		if(response.result=="protected"){
		    			dialogService.setContent("不能删除已经开始的培训").setShowDialog(true);
		    		}else{
		    			dialogService.setContent("删除培训活动成功").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
		    				$scope.paginationConf.onChange();
	    				}
		    		}
		    	},200);                
            }).error(function(){
		    	$timeout(function(){
		    		dialogService.setContent("网络异常,未能成功删除培训活动,请重新登录后再试").setShowDialog(true);
		    	},200);
		    }); 
    	}
    }
    //导出 活动中获得证书人员
    $scope.downExport = function(activityId){
		location.href="/enterpriseuniversity/services/certificateManage/downExport?activityId=" + activityId;
//		var url="/enterpriseuniversity/services/certificateManage/downExport?activityId=" + activityId;
//		$http.get(url).success(function (data) {
//			 var bin = new $window.Blob([data[0].certificateAddress]);
//			 
//			 deferred.resolve(data[0].certificateAddress);
//			 
//			 // Using file-saver library to handle saving work.
//			 saveAs(bin, toFilename);
//			})
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(activity){
        $scope.currHighLightRow.activity = activity.activityId; 
    }
}])
.controller('AddCertificateController', ['$scope', '$http', 'dialogService', '$timeout', function ($scope, $http, dialogService, $timeout) {
    $scope.$emit("cm",{pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cc', cmn: '证书管理', gcmn:'添加活动证书'});
    var postAddUrl = "/enterpriseuniversity/services/certificate/addCertificate";
    
    $scope.cert = $scope.formData = {
		attribute : [] ,
		imageWidth: 702,
		imageHeight: 496
    }; 
     
    $scope.$on("$certificate", function(e, d){
    	e.stopPropagation();
    	d && d.id ? $scope.formData.id = Number(d.id) : "";
    	$scope.certTemplate  = d;
    	if(d){
    		$scope.$broadcast("$InitResizerContainerPos");
    	}
    });
    
    $scope.doSave = function(){
    	
    	var cert = {};
    	
    	angular.copy($scope.cert, cert);
    	 
    	cert.attribute = $scope.font.formatSubmitStyle(cert.attribute.concat());
    	
    	cert.attribute = $scope.font.formatSubmitData(cert.attribute);
    	 
    	$http({
    		method : 'POST',
    		url  : postAddUrl,
    		data : cert, 
    		headers : { 'Content-Type': 'application/json' }
    	}).success(function(data) {
    		if(data.result == "success"){
    			dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
    			$timeout(function(){
    	   			dialogService.sureButten_click(); 
    	   			$scope.doReturn();
    	   		},2000);
    		}else{
    			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
    			$timeout(function(){
    	   			dialogService.sureButten_click(); 
    	   		},2000);
    		}
    	})
    	.error(function(){
    		dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
       			dialogService.sureButten_click(); 
       		},2000);
    	});
    } 
    //返回按钮
    $scope.doReturn = function(){
    	//$scope.go('/activity/certificateList', 'certificateList', null);
    	$scope.go('/activity/addActivity', 'addActivity', null);
    }; 
}])
.controller('ChooseCertificateModelController', ['$scope', '$http', 'dialogService', '$timeout', 'dialogStatus', function ($scope, $http, dialogService, $timeout, dialogStatus) {
	var certificateModalUrl = "/enterpriseuniversity/services/certificate/certificateList?pageNum=";
    
	//查询证书模板
	$scope.openModalDialog = false;
	$scope.isInitUpdatePageData = false;
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
            $scope.$httpUrl = certificateModalUrl + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $scope.$emit('isLoading', true); 
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false); 
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                if ($scope.itemArr.initCurrChoosedItemStatus) 
                {
                    $scope.itemArr.initCurrChoosedItemStatus();
                } 
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("数据查询异常！").setShowDialog(true);
            });
        }
	};
    
    var initQuery = function(){
    	$scope.paginationConf.currentPage == 1 ? $scope.paginationConf.itemsPerPage == 20 ? $scope.paginationConf.onChange() : $scope.paginationConf.itemsPerPage = 20 : $scope.paginationConf.currentPage = 1;
    }
    
    $scope.certImgStyle = function(rootUrl, address){
    	//if(isAbsolute) return { 'background': 'url(' + address + ')', 'background-size': '100% 100%' };
    	return address ? { 'background': 'url(' + rootUrl + address + ')', 'background-size': '100% 100%' } : {};
    }
    
    $scope.itemArr = {
    	key : "id",
    	oldChoosedItem : null,
        currChoosedItem : null, 
        //选中choosedItemArr中的项           
        initCurrChoosedItemStatus: function () {
        	$scope.$emit('isLoading', true);
        	if(this.currChoosedItem)
    		{
	        	for(var j in $scope.page.data)
	        	{
        			if($scope.page.data[j][$scope.itemArr.key] == this.currChoosedItem[$scope.itemArr.key])
        			{
           			 	$scope.page.data[j].checked = true;
           			 	break;
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
                this.currChoosedItem = null;
            } 
            else 
            {
                item.checked = true;
                this.currChoosedItem = item;
            }
        } 
    }
    //接收编辑页面传值
    if($scope.$parent.$parent.deferred){
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	if($scope.formData.certificateId && $scope.formData.receiveAddress){
	    		$scope.itemArr.oldChoosedItem = {
	    			id : $scope.formData.certificateId,
	    			address : $scope.formData.receiveAddress
	    		};
	    	} 
        }, function(data) {}); 
    }
    
    $scope.openCertificateModal = function(){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.itemArr.currChoosedItem = $scope.itemArr.oldChoosedItem;
    	initQuery();
    }
    $scope.doSure = function () {
    	$scope.itemArr.oldChoosedItem = $scope.itemArr.currChoosedItem;
    	//$scope.itemArr.oldChoosedItem = {a:123};
        $scope.$emit("$certificateTemplate", $scope.itemArr.currChoosedItem);
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    $scope.toAddCertificatePage = function(){
    	$scope.go('/activity/addCertificate', 'addCertificate', null);
    }
    //关闭按钮
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
.controller('CertificateModalController', ['$scope', '$http', 'dialogService', '$timeout', 'dialogStatus', function ($scope, $http, dialogService, $timeout, dialogStatus) {
	var certificateModalUrl = "/enterpriseuniversity/services/certificate/certificateList?pageNum=";
    
	//查询证书模板
	$scope.openModalDialog = false;
	$scope.isInitUpdatePageData = false;
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
            $scope.$httpUrl = certificateModalUrl + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $scope.$emit('isLoading', true); 
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false); 
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                if ($scope.itemArr.initCurrChoosedItemStatus) 
                {
                    $scope.itemArr.initCurrChoosedItemStatus();
                } 
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("数据查询异常！").setShowDialog(true);
            });
        }
	};
    
    var initQuery = function(){
    	$scope.paginationConf.currentPage == 1 ? $scope.paginationConf.itemsPerPage == 20 ? $scope.paginationConf.onChange() : $scope.paginationConf.itemsPerPage = 20 : $scope.paginationConf.currentPage = 1;
    }
    
    $scope.itemArr = {
    	key : "id",
    	oldChoosedItem : null,
        currChoosedItem : null, 
        //选中choosedItemArr中的项           
        initCurrChoosedItemStatus: function () {
        	$scope.$emit('isLoading', true);
        	if(this.currChoosedItem)
    		{
	        	for(var j in $scope.page.data)
	        	{
        			if($scope.page.data[j][$scope.itemArr.key] == this.currChoosedItem[$scope.itemArr.key])
        			{
           			 	$scope.page.data[j].checked = true;
           			 	break;
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
                this.currChoosedItem = {};
            } 
            else 
            {
                item.checked = true;
                if(this.currChoosedItem) this.currChoosedItem.checked = undefined;
                this.currChoosedItem = item;
            }
        } 
    }
    $scope.openCertificateModal = function(){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.itemArr.currChoosedItem = $scope.itemArr.oldChoosedItem;
    	initQuery();
    }
    $scope.doSure = function () {
    	$scope.itemArr.oldChoosedItem = $scope.itemArr.currChoosedItem;
    	//$scope.itemArr.oldChoosedItem = {a:123};
        $scope.$emit("$certificate", $scope.itemArr.currChoosedItem);
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭按钮
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
.directive("resizerContainer", ['$document', function($document){
	return {
		restrict: "A",
		link: function (scope, element, attr) {
			
			var inputNames = ["user", "year", "month", "day", "activity", "company", "dateYear", "dateMonth", "dateDay", "info"];
			var inputTypes = ["input", "input", "input", "input", "input", "input", "input", "input", "input", "textarea"];
		    var fontFamilys = ["宋体", "隶书", "微软雅黑", "黑体"];
		    var placeholders = ["姓名", "参加日期：年", "参加日期：月", "参加日期：日", "活动名称", "公司名称", "颁发日期：年", "颁发日期：月", "颁发日期：日", "自定义证书内容"];
		    var inputWidth = 110, inputHeight = 28; firstInputMargin = 10, inputMargin = 8, inputTop = 590;
			
		    scope.certImgStyle = function(rootUrl, address){
		    	//if(isAbsolute) return { 'background': 'url(' + address + ')', 'background-size': '100% 100%' };
		    	return address ? { 'background': 'url(' + rootUrl + address + ')', 'background-size': '100% 100%' } : {};
		    }
		    
		    scope.focusedInput = null; 
		    
		    scope.font = {
		    	isInit : false,
		    	fontFamilys : fontFamilys,
		    	fontFamily : "",
		    	defaultFontFamily : "微软雅黑",
		    	initFontFamily : function(){
		    		this.fontFamilys && this.fontFamilys.length > 0 ?  this.fontFamily = this.fontFamilys[0]: this.defaultFontFamily;
		    	},
		    	defaultSize : 12,
		    	minSize : 12,
		    	maxSize : 22,
		    	fontUnit : "px",
		    	fontSizeNums : [],
		    	initFontSizeNums : function(){
		    		var fontSizeNums = [];
		    		for(var i = this.minSize; i<= this.maxSize; i++){
		    			if(i % 2 == 0){
		    				fontSizeNums.push(i);
		    			}
		    		}
		    		this.fontSizeNums = fontSizeNums;
		    	},
		    	fontSizeNum : null,
		    	initFontSizeNum : function(){
					this.fontSizeNums && this.fontSizeNums.length > 0 ?  this.fontSizeNum = this.fontSizeNums[0]: this.defaultSize;
				},
				fontSize : "",
				formatFontSize : function(unit){
		    		this.fontSize = (this.fontSizeNum ? this.fontSizeNum : this.defaultSize) + unit;
		    	},
				/*fontWeights : ["normal", "100", "200", "300", "400", "500", "600", "700", "800", "900", "bold", "bolder"],*/
		    	fontWeights : ["normal", "bold"],
				fontWeight : null,
				initFontWeight : function(){
					this.fontWeights && this.fontWeights.length > 0 ? this.fontWeight = this.fontWeights[0] : "normal";
				},
				fontStyles : ["normal", "italic"],
				fStyle : null,
				initFStyle : function(){
					this.fontStyles && this.fontStyles.length > 0 ? this.fStyle = this.fontStyles[0] : "normal";
				},
				fontStyle : {},
				initFontAndWeight : function(id, attribute){
					var fontStyle = this.fontStyle, temp;
					for(var i in attribute){
						temp = attribute[i];
						if(id == temp["id"]){
							fontStyle = temp["fontStyle"];
							break;
						}
					}
					this.fontSizeNum = this.trimPx(fontStyle["font-size"]);
					this.fontWeight = fontStyle["font-weight"];
					this.fontFamily = fontStyle["font-family"];
					this.fStyle = fontStyle["font-style"];
				},
				initFontStyle : function(){
					if(!this.isInit){
						this.initFontFamily();
						this.initFontSizeNums();
						this.initFontSizeNum();
						this.initFontWeight();
						this.initFStyle();
						this.isInit = true;
					}
					this.formatFontSize(this.fontUnit);
					this.fontStyle = {"font-size": this.fontSize,  "line-height": this.fontSize, "font-weight": this.fontWeight, "font-family": this.fontFamily, "font-style":  this.fStyle};
					scope.$broadcast("$UpdateFontStyle", {id: scope.focusedInput, fontStyle : this.fontStyle});
				},
				trimPx: function(str){
					return str ? str.indexOf(".") != -1 ? parseFloat(str): parseInt(str): 0;
				},
				formatSubmitStyle : function(arr){
					var temp, style;
					for(var i in arr){
						temp = arr[i];
						style = {
							width : this.trimPx(temp.style.width),
							height : this.trimPx(temp.style.height),
							top : this.trimPx(temp.style.top),
							right : this.trimPx(temp.style.right),
							bottom : this.trimPx(temp.style.bottom),
							left : this.trimPx(temp.style.left)
						};
						temp.style = style;
					}
					return arr;
				},
				formatFontStyle : function(fontStyle){
					var fontWeight = fontStyle["font-weight"];
					var fs = fontStyle["font-style"];
					if(fontWeight == "bold" && fs == "italic"){
						return "bold|italic";
					} 
					return fontWeight == "bold" ? fontWeight : fs;
				},
				formatSubmitData : function(attribute){
					var tempAttribute = {}, temp;
					for(var i in attribute){
						temp = attribute[i];
						tempAttribute[temp["name"]] = {
							"name": temp["value"],
							"x":temp["style"]["left"],
							"y":temp["style"]["top"],
							"width":temp["style"]["width"],
							"height":temp["style"]["height"],
							"fontSize": this.trimPx(temp["fontStyle"]["font-size"]),
							//"fontWeight": temp["fontStyle"]["font-weight"],
							"fontFamily": temp["fontStyle"]["font-family"],
							//"fontStyle": temp["fontStyle"]["font-style"]
							"fontStyle": this.formatFontStyle(temp["fontStyle"])
						};
					}
					return tempAttribute;
				}
		    };
		    
		    scope.font.initFontStyle();
		    
		    for(var i = 0 ; i < 10; i++){
		    	scope.formData.attribute.push(
					{
						id: i,
						type : inputTypes[i],
						style : {
							width: inputWidth + "px",
							height: inputHeight + "px",
							top : (i < 5 ? inputTop : inputTop + inputMargin + inputHeight) + "px",
							right : 0,
							bottom : 0,
							left : (i < 5 ? inputWidth * i + (i < 1 ? firstInputMargin : inputMargin) * (i+1) : inputWidth * (i-5) + (i-5+1 == 1 ? firstInputMargin : inputMargin) * (i-5 + 1)) + 'px'
						},
						fontStyle : scope.font.fontStyle,
						placeholder : placeholders[i],
						name : inputNames[i],
						value : ""
					}
		    	);
		    }
			 
			scope.$on("$GetResizerContainer", function(e, data){
				e.stopPropagation();
				scope.$broadcast("$ResizerContainer", element[0]);
			})
			//监听字体调整目标
			scope.$on("$CurrEditInput", function(e, target){
				e.stopPropagation();
				scope.focusedInput = target;
				scope.font.initFontAndWeight(target, scope.formData.attribute);
			})
		}
	}
}])
.directive("resizer", function($document){
	return {
		restrict: "A",
		template: 
				//'<div id="{{dragData.id + \'up\'}}" class="minDiv top" resize-handler resize-direction="up"></div>'+
	    		//'<div id="{{dragData.id + \'rightUp\'}}" class="minDiv right-up" resize-handler resize-direction="rightUp"></div>'+
	    		'<div class="minDiv right" resize-handler ="right"></div>'+
	    		'<div class="minDiv right-down" resize-handler="rightDown"></div>'+
	    		'<div class="minDiv bottom" resize-handler="down"></div>'+
	    		//'<div class="minDiv left-down" resize-handler resize-direction="leftDown"></div>'+
	    		//'<div class="minDiv left" resize-handler resize-direction="left"></div>'+
	    		//'<div class="minDiv left-up" resize-handler resize-direction="leftUp"></div>'+
				'<input ng-if="dragData.type==\'input\'" class="cert-input" ng-style="dragData.fontStyle" type="text" ng-model="dragData.value" resize-handler="input" resize-id="{{dragData.id}}" placeholder="{{dragData.placeholder}}"/>' +
				'<textarea ng-if="dragData.type==\'textarea\'" class="cert-input" ng-style="dragData.fontStyle" ng-model="dragData.value"  resize-handler="input" resize-id="{{dragData.id}}" placeholder="{{dragData.placeholder}}"></textarea>',
		scope : {
			dragStyle : "=",
			dragData : "=",
			minWidth : "@",
			minHeight : "@"
		},
		link: function (scope, element, attr) {
			document.onselectstart = new Function('event.returnValue = false;');
			
			scope.resizerHandler = {
				resizerContainerPosition : {},
				resizeTargetElement : element[0],
				initResizeTargetElementPos : function(){
					this.resizeTargetElement.style.width = scope.dragStyle.width;
					this.resizeTargetElement.style.height = scope.dragStyle.height;
					this.resizeTargetElement.style.left = scope.dragStyle.left;
					this.resizeTargetElement.style.top = scope.dragStyle.top;
				},
				getOffsetPosition : function (node){
					var left = node.offsetLeft;
					var top = node.offsetTop;
					var offsetParent = node.offsetParent;
					while(offsetParent != null){
						left += offsetParent.offsetLeft;
						top += offsetParent.offsetTop;
						offsetParent = offsetParent.offsetParent;
					}
					return { "left" : left, "top" : top }
				},
				getRelativePosition : function (node){
					var po = this.getOffsetPosition(node);
					return { "width": node.offsetWidth + "px", "height": node.offsetHeight + "px", "left" : (po.left - this.resizerContainerPosition.left) + "px", "top" : (po.top - this.resizerContainerPosition.top) + "px"};
				},
				getStyle : function(node){
					//var nodePo = this.getRelativePosition(node);
					//return { width: nodePo.width, height : nodePo.height, top : nodePo.top, left: nodePo.left };
					var nds = node.style;
					return { width: nds.width, height : nds.height, top : nds.top, left: nds.left };
				},
				rightMove : function(e){
					var x = e.clientX;//鼠标横坐标
					if(x > this.resizerContainerPosition.left + this.resizerContainerPosition.offsetWidth){
						x = this.resizerContainerPosition.left + this.resizerContainerPosition.offsetWidth;
					}
					console.log(" this.eClientX",  this.eClientX, x , this.resizerContainerPosition.left, this.resizerContainerPosition.offsetWidth);
					var addWidth = this.eClientX != undefined ? x - this.eClientX : 0;
					this.eClientX = x; 
					var width = this.resizeTargetElement.offsetWidth;//选择层宽度
					console.log(" width + addWidth",  width + addWidth);
					scope.$emit("$Width", width + addWidth); 
				},
				downMove : function(e){
					var y = e.clientY; 
					if(y > this.resizerContainerPosition.top + this.resizerContainerPosition.offsetHeight){
						y = this.resizerContainerPosition.top + this.resizerContainerPosition.offsetHeight;
					}
					var addHeight = this.eClientY != undefined ? y - this.eClientY : 0;
					this.eClientY = y; 
					var height = this.resizeTargetElement.offsetHeight;//选择层的高度
					scope.$emit("$Height", height + addHeight);
				},
				mousemove : function(e){
					switch (scope.actionDirection) {
						case "right":
						 	scope.resizerHandler.rightMove(e);
							break;
						case "rightDown":
						 	scope.resizerHandler.rightMove(e);
						 	scope.resizerHandler.downMove(e);
							break;
						case "down":
						 	scope.resizerHandler.downMove(e);
							break;
						default : break;
						 
					}
				},
				mouseup : function(e){
					scope.actionDirection = "";
					setTimeout(function(){
						scope.$emit("$ResizeDirection", "");
					}, 100);
				}
			}
			 
			$document.on("mousemove" , scope.resizerHandler.mousemove);
			$document.on("mouseup" , scope.resizerHandler.mouseup);
			
			scope.resizerHandler.initResizeTargetElementPos();
			
			setTimeout(function(){
				scope.$emit("$GetResizerContainer");
			}, 10);
			
			scope.$on("$UpdateFontStyle", function(e, d){
				if(d.id == null || scope.dragData.id == d.id){
					scope.dragData.fontStyle = d.fontStyle;
				} 
			});
			
			scope.$on("$Width", function(e, d){
				e.stopPropagation();
				scope.resizerHandler.resizeTargetElement.style.width = d < scope.minWidth ? scope.minWidth + "px" : d + "px";
				scope.dragStyle.width = scope.resizerHandler.resizeTargetElement.style.width;

			});
			scope.$on("$Height", function(e, d){
				e.stopPropagation();
				scope.resizerHandler.resizeTargetElement.style.height = d < scope.minHeight ? scope.minHeight + "px" : d + "px";
				scope.dragStyle.height = scope.resizerHandler.resizeTargetElement.style.height;

			}); 
			scope.$on("$ResizerContainer", function(e, pl){
			 	scope.resizerHandler["resizerRelativeContainerElement"] = pl;
			 	scope.resizerHandler.resizerContainerPosition = scope.resizerHandler.getOffsetPosition(pl);
			 	scope.resizerHandler.resizerContainerPosition["offsetWidth"] = pl.offsetWidth;
			 	scope.resizerHandler.resizerContainerPosition["offsetHeight"] = pl.offsetHeight;
			});
			scope.$on("$InitResizerContainerPos", function(e){
				setTimeout(function(){
					var rlce = scope.resizerHandler["resizerRelativeContainerElement"];
					scope.resizerHandler.resizerContainerPosition = scope.resizerHandler.getOffsetPosition(rlce);
				 	scope.resizerHandler.resizerContainerPosition["offsetWidth"] = rlce.offsetWidth;
				 	scope.resizerHandler.resizerContainerPosition["offsetHeight"] = rlce.offsetHeight;
				 	//console.log("scope.resizerHandler.resizerContainerPosition", scope.resizerHandler.resizerContainerPosition);
				}, 100);
			})
			scope.$on("$ResizeDirection", function(e, direction){
				e.stopPropagation();
				scope.resizerHandler.eClientX = undefined;
				scope.resizerHandler.eClientY = undefined;
				scope.actionDirection = direction;
				scope.$apply(function(){
					scope.dragStyle = scope.resizerHandler.getStyle(scope.resizerHandler.resizeTargetElement);
				});
			});
			scope.$on("$InitElements", function(e, data){
				e.stopPropagation();
				scope.resizerHandler[data.name + "HandlerElement"] = data.element; 
			});
		}
	}
})
.directive("resizeHandler", [function(){
	return {
		restrict: "A",
		scope:{
			resizeType: "@resizeHandler",
			resizeId : "@"
		},
		link: function (scope, element, attr) {
			
			setTimeout(function(){
				scope.$emit("$InitElements", {"name": scope.resizeType, "element": element[0]});
			}, 10)
			
			var stopPropagation = false, inputDisabled = false;
			if(scope.resizeType === "input"){
				angular.element(element).on("mousedown", function(e){
					if(stopPropagation){
						e.stopPropagation();
					}
					setTimeout(function(){
						scope.$emit("$ResizeDirection", scope.resizeType);
					}, 50)
				})
				angular.element(element).on("dblclick", function(e){
					this.focus();
					stopPropagation = true;
					inputDisabled = false;
					setTimeout(function(){
						scope.$emit("$ResizeDirection", scope.resizeType);
						//修改字体样式
						scope.$emit("$CurrEditInput", scope.resizeId);
					}, 50)
				});
				angular.element(element).on("focusout", function(e){
					stopPropagation = false; 
					//setTimeout(function(){
						//修改字体样式
						//scope.$emit("$CurrEditInput", null);
					//}, 50)
				}); 
			}else{
				angular.element(element).on("mousedown", function(e){
					e.stopPropagation();
					inputDisabled = true;
					setTimeout(function(){
						scope.$emit("$ResizeDirection", scope.resizeType);
					}, 50)
				});
			}
			  
		}
	}
}]) 
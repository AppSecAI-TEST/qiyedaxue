/*试卷js*/
angular.module('ele.examPaper', [ 'ele.admin','ele.questionBank'])
//试卷列表
.controller('ExamPaperController',['$scope', '$http','$timeout','dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
	 //用于显示面包屑状态
	$scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理 ', cmc:'cmc-bc', cmn: '试卷列表'};
	//列表数据查询
	$scope.url = "/enterpriseuniversity/services/examPaper/findPage?pageNum=";
	
	$scope.type= [
          {name :"固定考试",value : "1"},
          {name :"随机考试",value : "2"},
          {name :"固定调研",value : "3"},
          {name :"随机调研",value : "4"},
          {name :"固定总结",value : "5"},
          {name :"随机总结",value : "6"}
      ];
	$scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 13,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
            var $httpUrl = "";
            var $httpPrams = "";
            $scope.$emit('isLoading', true);
            if ($scope.examPaperName != undefined && $scope.examPaperName != "") {
                $httpPrams = $httpPrams + "&examPaperName=" 
                	+ $scope.examPaperName.replace(/\%/g,"%25").replace(/\#/g,"%23")
                    	.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
                    	.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            if ($scope.selectedType != undefined) {
                $httpPrams = $httpPrams + "&examPaperType=" + $scope.selectedType;
            }
            $httpUrl = $scope.url + $scope.paginationConf.currentPage +
            "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $httpPrams;
            $http.get($httpUrl).success(function (response) {
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
    $scope.changeSelectOption =function(){
    	$scope.search();
    }
    //添加
    $scope.add = function(){
        $scope.go('/exam/addExamPaper','addExamPaper',{});
    }
    //编辑
    $scope.edit = function(examPaper){
        $scope.go('/exam/editExamPaper','editExamPaper',{id:examPaper.examPaperId})
    }
    // 删除 
    $scope.doDelete = function (examPaper) {
    	dialogService.setContent('确定删除试卷？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
    	//if(confirm("确定删除试卷?")){
    		$http({
                url: "/enterpriseuniversity/services/examPaper/delete?examPaperId=" + examPaper.examPaperId,
                method: "DELETE"
            }).success(function (response) {
            	$timeout(function(){
                	if(response.result=="protected"){
                		dialogService.setContent("不能删除正在使用的试卷！").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        				},2000);
                	}else if(response.result=="inActivity"){
                		dialogService.setContent("不能删除任务包中的试卷！").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        				},2000);
   				 	}
                	if(response.result=="error"){
                		dialogService.setContent("删除失败！").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        				},2000);
                	}
                	if(response.result=="success"){
                		dialogService.setContent("成功删除试卷！").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        					$scope.search();
        				},2000);
                	}
            	},200);
            }).error(function(){
            	dialogService.setContent("删除试卷失败！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
				},2000);
    		});
    	};
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(examPaper){
        $scope.currHighLightRow.examPaperId = examPaper.examPaperId; 
    }  
    
    $scope.examineMode = {
		'openExamineModal':false
    }
    $scope.examine = function (examPaper){
    	if(examPaper.examPaperType=="固定考试" || examPaper.examPaperType=="固定调研" || examPaper.examPaperType=="固定总结"){
    		var url="/enterpriseuniversity/services/examPaper/examine?examPaperId="+examPaper.examPaperId;
    		$http.get(url).success(function(response) {
    			$scope.examineDetail = response;
    		});
    		$scope.examineMode.openExamineModal = true;
    		dialogStatus.setHasShowedDialog(true);
    	}else{
			dialogService.setContent("只能查看固定试卷！").setShowDialog(true)
    	}
    }
    $scope.closeExamineModal = function (){
    	$scope.examineMode.openExamineModal = false;
    	dialogStatus.setHasShowedDialog(false);
    }
}])
//添加试卷
.controller('AddExamPaperController', ['$scope', '$http','dialogService','$timeout', function ($scope, $http,dialogService,$timeout) {
    $scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理', cmc:'cmc-bc', cmn: '试卷列表' ,gcmn: '添加试卷'};
    $scope.examPaper = $scope.formData = {
    		questionBankId:[],
    		examPaperType : 1,
    		testQuestionCount : 0,
    		fullMark : 0,
    		//passMark : null,
    		showScore : 'Y',
    		showQualifiedStandard :'Y'
    };  
    $scope.sortArry1 = [1,2,3,4,5];
    $scope.sort1 = $scope.sortArry1[0];
    $scope.sortArry2 = [2,3,4,5];
    $scope.sort2 = $scope.sortArry2[0];
    $scope.sortArry3 = [3,4,5];
    $scope.sort3 = $scope.sortArry3[0];
    $scope.sortArry4 = [4,5];
    $scope.sort4 = $scope.sortArry4[0];
    $scope.sortArry5 = [5];
    $scope.sort5 = $scope.sortArry5[0];
    
    $scope.initSelectOption = function(sort){
    	if(sort=="sort1"){
    		$scope.sortArry2 = $scope.sortArry1.concat();
    		$scope.sortArry2.splice($scope.sortArry2.indexOf($scope.sort1),1);
    		$scope.sort2 = $scope.sortArry2[0]; 
    		$scope.sortArry3 = $scope.sortArry2.concat();
    		$scope.sortArry3.splice($scope.sortArry3.indexOf($scope.sort2),1);
    		$scope.sort3 = $scope.sortArry3[0]; 
    		$scope.sortArry4 = $scope.sortArry3.concat();
    		$scope.sortArry4.splice($scope.sortArry4.indexOf($scope.sort3),1);
    		$scope.sort4 = $scope.sortArry4[0]; 
    		$scope.sortArry5 = $scope.sortArry4.concat();
    		$scope.sortArry5.splice($scope.sortArry5.indexOf($scope.sort4),1);
    		$scope.sort5 = $scope.sortArry5[0];
    	}
    	if(sort=="sort2"){
    		$scope.sortArry3 = $scope.sortArry2.concat();
    		$scope.sortArry3.splice($scope.sortArry3.indexOf($scope.sort2),1);
    		$scope.sort3 = $scope.sortArry3[0]; 
    		$scope.sortArry4 = $scope.sortArry3.concat();
    		$scope.sortArry4.splice($scope.sortArry4.indexOf($scope.sort3),1);
    		$scope.sort4 = $scope.sortArry4[0]; 
    		$scope.sortArry5 = $scope.sortArry4.concat();
    		$scope.sortArry5.splice($scope.sortArry5.indexOf($scope.sort4),1);
    		$scope.sort5 = $scope.sortArry5[0];
    	}
    	if(sort=="sort3"){
    		$scope.sortArry4 = $scope.sortArry3.concat();
    		$scope.sortArry4.splice($scope.sortArry4.indexOf($scope.sort3),1);
    		$scope.sort4 = $scope.sortArry4[0]; 
    		$scope.sortArry5 = $scope.sortArry4.concat();
    		$scope.sortArry5.splice($scope.sortArry5.indexOf($scope.sort4),1);
    		$scope.sort5 = $scope.sortArry5[0]; 
    	}
    	if(sort=="sort4"){
    		$scope.sortArry5 = $scope.sortArry4.concat();
    		$scope.sortArry5.splice($scope.sortArry5.indexOf($scope.sort4),1);
    		$scope.sort5 = $scope.sortArry5[0];
    	}
    }
    
    $scope.addExamPaperSubmit=false;
    $scope.doSave = function(){
		 $scope.examPaperForm.$submitted = true;
		 if($scope.examPaperForm.$invalid)
		 {
			 $timeout(function(){
					dialogService.setContent("表单填写验证未通过，请按提示修改表单后重试！").setShowDialog(true)
				},200);
			 //alert("表单填写验证未通过，请按提示修改表单后重试");
			 return;
		 }
		 $scope.examPaper.sort = [$scope.sort1,$scope.sort2,$scope.sort3,$scope.sort4,$scope.sort5];
		 $scope.addExamPaperSubmit=true;
		 $http({
			  method : 'POST',
			  url  : '/enterpriseuniversity/services/examPaper/addExamPaper',
			  data : $.param($scope.examPaper),
			  headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
		 })
		 .success(function(data) {
			 if(data.result=="success"){
//				 $timeout(function(){
//						dialogService.setContent("保存成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/exam/examPaperList','examPaperList',null);}
//					},200);
				 dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
		    		$timeout(function(){
						dialogService.sureButten_click(); 
						$scope.addExamPaperSubmit=false;
						$scope.go('/exam/examPaperList','examPaperList',null);
					},2000);
			 }else if(data.result=="wrong"){
//				 dialogService.setContent("同一题库同一类型的题目不能重复选择！").setShowDialog(true)
				 dialogService.setContent("同一题库同一类型的题目不能重复选择！").setShowSureButton(false).setShowDialog(true); 
					$timeout(function(){
						dialogService.sureButten_click(); 
						$scope.addExamPaperSubmit=false;
					},2000);
			 }else {
//				 $timeout(function(){
//						dialogService.setContent("保存失败！").setShowDialog(true)
//					},200);
				 dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true); 
					$timeout(function(){
						dialogService.sureButten_click(); 
						$scope.addExamPaperSubmit=false;
					},2000);
			 }
			 $scope.addExamPaperSubmit=false;
		 })
		 .error(function(){
//			 $timeout(function(){
//					dialogService.setContent("保存失败！").setShowDialog(true)
//				},200);
			 dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true); 
				$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addExamPaperSubmit=false;
				},2000);
			 $scope.addExamPaperSubmit=false;
		 });
    }
    $scope.doReturn = function(){
    	$scope.go('/exam/examPaperList','examPaperList',null);
    }
}])
//编辑试卷
.controller('EditExamPaperController', ['$scope', '$http', '$state', '$stateParams', '$q','dialogService','$timeout',function ($scope, $http, $state, $stateParams ,$q,dialogService,$timeout) {
    //编辑
	$scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理', cmc:'cmc-bc', cmn: '试卷列表' ,gcmn: '编辑试卷'};
    $scope.deferred = $q.defer();
    if ($stateParams.id) {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/examPaper/findById?examPaperId=" + $stateParams.id).success(function (data) {
        	$scope.deferred.resolve(data);
    	}).error(function(data){
    		$scope.deferred.reject(data); 
    	});
    } else {
        //未传参数
    	dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.$emit('isLoading', false);
    	$scope.examPaper = $scope.formData = data; 
    	//初始化排序数据
    	$scope.initSort(); 
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
		dialogService.setContent("页面数据初始化异常!").setShowDialog(true)
    }); 
    $scope.initSort = function(){
    	$scope.$emit('isLoading', true);
    	$scope.sortArry1 =  ["1","2","3","4","5"];
    	for(var i in $scope.examPaper.sort){
    		if(i==0){
    			$scope.sort1=$scope.examPaper.sort[i];
    			$scope.sortArry2 = $scope.sortArry1.concat();
        		$scope.sortArry2.splice($scope.sortArry2.indexOf($scope.sort1),1);
    		}
    		if(i==1){
    			$scope.sort2 = $scope.examPaper.sort[i];
        		$scope.sortArry3 = $scope.sortArry2.concat();
        		$scope.sortArry3.splice($scope.sortArry3.indexOf($scope.sort2),1);
    		}
    		if(i==2){
    			$scope.sort3 = $scope.examPaper.sort[i];
        		$scope.sortArry4 = $scope.sortArry3.concat();
        		$scope.sortArry4.splice($scope.sortArry4.indexOf($scope.sort3),1);
    		}
    		if(i==3){
    			$scope.sort4 = $scope.examPaper.sort[i]; 
        		$scope.sortArry5 = $scope.sortArry4.concat();
        		$scope.sortArry5.splice($scope.sortArry5.indexOf($scope.sort4),1);
    		}
    		if(i==4){
    			$scope.sort5 = $scope.examPaper.sort[i];
    		}
    	}
    	$scope.$emit('isLoading', false);
    }
    $scope.initSelectOption = function(sort){
    	if(sort=="sort1"){
    		$scope.sortArry2 = $scope.sortArry1.concat();
    		$scope.sortArry2.splice($scope.sortArry2.indexOf($scope.sort1),1);
    		$scope.sort2 = $scope.sortArry2[0]; 
    		$scope.sortArry3 = $scope.sortArry2.concat();
    		$scope.sortArry3.splice($scope.sortArry3.indexOf($scope.sort2),1);
    		$scope.sort3 = $scope.sortArry3[0]; 
    		$scope.sortArry4 = $scope.sortArry3.concat();
    		$scope.sortArry4.splice($scope.sortArry4.indexOf($scope.sort3),1);
    		$scope.sort4 = $scope.sortArry4[0]; 
    		$scope.sortArry5 = $scope.sortArry4.concat();
    		$scope.sortArry5.splice($scope.sortArry5.indexOf($scope.sort4),1);
    		$scope.sort5 = $scope.sortArry5[0];
    	}
    	if(sort=="sort2"){
    		$scope.sortArry3 = $scope.sortArry2.concat();
    		$scope.sortArry3.splice($scope.sortArry3.indexOf($scope.sort2),1);
    		$scope.sort3 = $scope.sortArry3[0]; 
    		$scope.sortArry4 = $scope.sortArry3.concat();
    		$scope.sortArry4.splice($scope.sortArry4.indexOf($scope.sort3),1);
    		$scope.sort4 = $scope.sortArry4[0]; 
    		$scope.sortArry5 = $scope.sortArry4.concat();
    		$scope.sortArry5.splice($scope.sortArry5.indexOf($scope.sort4),1);
    		$scope.sort5 = $scope.sortArry5[0];
    	}
    	if(sort=="sort3"){
    		$scope.sortArry4 = $scope.sortArry3.concat();
    		$scope.sortArry4.splice($scope.sortArry4.indexOf($scope.sort3),1);
    		$scope.sort4 = $scope.sortArry4[0]; 
    		$scope.sortArry5 = $scope.sortArry4.concat();
    		$scope.sortArry5.splice($scope.sortArry5.indexOf($scope.sort4),1);
    		$scope.sort5 = $scope.sortArry5[0]; 
    	}
    	if(sort=="sort4"){
    		$scope.sortArry5 = $scope.sortArry4.concat();
    		$scope.sortArry5.splice($scope.sortArry5.indexOf($scope.sort4),1);
    		$scope.sort5 = $scope.sortArry5[0];
    	}
    }
    
    $scope.editExamPaperSubmit=false;
    $scope.doSave = function(){
		 $scope.examPaperForm.$submitted = true;
		 if($scope.examPaperForm.$invalid)
		 {
			 dialogService.setContent("表单填写验证未通过，请按提示修改表单后重试！").setShowDialog(true)
			 return;
		 }
		 $scope.examPaper.sort = [$scope.sort1,$scope.sort2,$scope.sort3,$scope.sort4,$scope.sort5];
		 $scope.editExamPaperSubmit=true;
		 $http({
		  method : 'POST',
		  url  : '/enterpriseuniversity/services/examPaper/updateExamPaper',
		  data : $.param($scope.examPaper), 
		  headers : { 'Content-Type': 'application/x-www-form-urlencoded' } 
		 })
		  .success(function(data) {
			  $timeout(function(){
			  if(data.result=="success"){
//				  dialogService.setContent("保存成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/exam/examPaperList','examPaperList',null);}
				  dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
		    		$timeout(function(){
						dialogService.sureButten_click(); 
						$scope.editExamPaperSubmit=false;
						$scope.go('/exam/examPaperList','examPaperList',null);
					},2000);
				 }else if(data.result=="wrong"){
//					 dialogService.setContent("同一题库同一类型的题目不能重复选择！").setShowDialog(true)
					 dialogService.setContent("同一题库同一类型的题目不能重复选择！").setShowSureButton(false).setShowDialog(true); 
						$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.editExamPaperSubmit=false;
						},2000);
				 }else if(data.result=="protected"){
//					 dialogService.setContent("不能编辑已应用的试卷！").setShowDialog(true)
					 dialogService.setContent("不能编辑已应用的试卷！").setShowSureButton(false).setShowDialog(true); 
						$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.editExamPaperSubmit=false;
						},2000);
				 }else if(data.result=="inActivity"){
//					 dialogService.setContent("不能编辑任务包中的试卷！").setShowDialog(true)
					 dialogService.setContent("不能编辑任务包中的试卷！").setShowSureButton(false).setShowDialog(true); 
						$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.editExamPaperSubmit=false;
						},2000);
				 }else {
//					dialogService.setContent("保存失败！").setShowDialog(true);
					 dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true); 
						$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.editExamPaperSubmit=false;
						},2000);
				 }
			  },200);
			  $scope.editExamPaperSubmit=false;
		  })
		  .error(function(){
//			  $timeout(function(){
//					dialogService.setContent("保存失败！").setShowDialog(true)
//				},200);
			  dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true); 
				$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.editExamPaperSubmit=false;
				},2000);
			  $scope.editExamPaperSubmit=false;
		  });
    }
    $scope.doReturn = function(){
    	$scope.go('/exam/examPaperList','examPaperList',null);
    }
}])
.directive('checkQuestionbankid', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = value== undefined ? true : value.length > 0 ? false : true ;
	                  ngModel.$setValidity("checkQuestionbankid", !validity);
	              });
	        	  return !validity ? value : [];
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}]) 
.directive('checkPassmark', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  var mark= false ;
	        	  scope.$watch(function () {
	                  //return value+" "+mark;
	        		  return value;
	              }, function(){
	            	  if(!/^(0|[1-9][0-9]*)$/.test(value)){
	            		  ngModel.$setValidity("checkPassmark", true);
	            		  return;
	            	  } 
	            	  if(!isNaN(value)){
	            		  validity = Number(value) > Number(scope.examPaper.fullMark) ? true : false ;
	            		  ngModel.$setValidity("checkPassmark", !validity);
	            	  } 
	              });
	        	  return !validity ? value : "";
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}]) 
.directive('requiredImg', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = value?(value.length < 1 ? true : false):true;
	                  ngModel.$setValidity("requiredImg", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
//选择封面图片控制器
.controller('ExamPaperImgController', ['$scope', 'FileUploader','dialogService','$timeout', function ($scope, FileUploader,dialogService,$timeout) {
    var imgUploader = $scope.imgUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/examPaperImg',
            autoUpload:true
        }
    );
    imgUploader.filters.push({
        name: 'customFilter',
        fn: function(item , options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            var size = item.size;
            if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0)
            {
				dialogService.setContent("要上传新的课程封面图片，请先删除旧的课程封面图片！").setShowDialog(true)
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
				dialogService.setContent("超出上传文件数量限制！").setShowDialog(true)
				return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|'.indexOf(type) == -1)
            {
				dialogService.setContent("上传文件格式不符合要求！").setShowDialog(true)
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
            if( item.size/1024 > 2048)
            {
				dialogService.setContent("上传文件大小不符合要求(大于2M)！").setShowDialog(true)
            	return item.size/1024 <= 2048;
            }
            return true;
        }
    });
    //历史文件列表
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currFileUrlQueue =[]; 
    		if($scope.formData.examPaperPicture&&$scope.formData.examPaperPicture!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.examPaperPicture);
    		}
        }, function(data) {
        	 
        }); 
    } 
    //预览
    $scope.openPreview = false;
    $scope.preview = function(){
    	if($scope.$parent.formData.examPaperPicture&&$scope.$parent.formData.examPaperPicture!="")
    	{
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.$parent.formData.examPaperPicture;
    	}
    	else
    	{
			dialogService.setContent("请先上传再预览！").setShowDialog(true)
    	}
    }
    $scope.closePreview = function(){
    	$scope.openPreview = false;
    }
    //试卷封面图片地址
    $scope.buildExamPaperImg = function(imgUrl){
		$scope.$parent.formData.examPaperPicture = imgUrl;
    }
    //删除之前上传的文件路径集合
    $scope.removeCurrItem = function (item) {
        $scope.currFileUrlQueue.splice($scope.currFileUrlQueue.indexOf(item), 1);
        $scope.buildExamPaperImg("");
    }
    //删除 
    $scope.removeItem = function (item) {
        item.remove();
        document.getElementById("examPaperImg").value=[];
        $scope.buildExamPaperImg(""); 
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
			dialogService.setContent("获取上传文件地址失败！").setShowDialog(true)
        }
        else
        {
        	$scope.buildExamPaperImg(response.url);//接收返回的文件地址
        }
    };
}])
//选择试卷模态框
.controller('ExamPaperModalController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/examPaper/findPage?pageNum=";
    $scope.isInitUpdatePageData = false;
	$scope.openModalDialog = false;
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 9,
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
        	if ($scope.examPaperName!=undefined &&$scope.examPaperName!= "") {
        		$scope.$httpPrams = $scope.$httpPrams + "&examPaperName=" 
        			+ $scope.examPaperName.replace(/\%/g,"%25").replace(/\#/g,"%23")
        				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D") ;
            }
            if ($scope.selectPaperType != undefined && $scope.selectPaperType != "")
            {
            	$scope.$httpPrams = $scope.$httpPrams+"&examPaperType=" + $scope.selectPaperType;
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $scope.$emit('isLoading', true); 
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
            	$scope.$emit('isLoading', false);
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
    //按试卷类型下拉菜单项查询试卷
    $scope.selectByPaperType=function(){
    	$scope.search();
    }
    //是否初始化旧的选项标记
    $scope.isBuildOldChoosedItems = false;
    /*
     * 模态框容器
     * */
    $scope.itemArr = {
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
            		 if($scope.page.data[j].examPaperId==$scope.itemArr.currChoosedItemArr[i].examPaperId)
            		 {
            			 $scope.page.data[j].checked = true;
            		 }
            	}
            }
            $scope.$emit('isLoading', false); 
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
        	//单选
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
        } ,
        //编辑页面数据回显
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true); 
        	if ($scope.page.data) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var examPaperId = $scope.itemArr.oldChooseItemArr[i];
	            	for (var j in $scope.page.data) 
	            	{
            			if($scope.page.data[j].examPaperId==examPaperId)
            			{
            				if($scope.itemArr.choosedItemArr.length<1)
            				{
            					$scope.itemArr.choosedItemArr.push($scope.page.data[j]);
            					continue;
            				}
            				var isContained = false;
            				for(var k in $scope.itemArr.choosedItemArr)
            				{
            					if($scope.itemArr.choosedItemArr[k].examPaperId==$scope.page.data[j].examPaperId)
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
	            //释放
            	$scope.page = {};
            }
        	$scope.buildExamAndExamPaperRelation();
        	$scope.$emit('isLoading', false); 
        }
    };
    //接收上级编辑控制器$scope中的数据
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.itemArr.oldChooseItemArr = [];
        	if($scope.formData.examPaperId&&$scope.formData.examPaperId!=""){
        		$scope.itemArr.oldChooseItemArr.push($scope.formData.examPaperId);
        	}
        	$scope.isInitUpdatePageData = true;
	    	$scope.search();
        }, function(data) {
        	 
        }); 
    }
    //生成表单数据  lecturers  字符串
    $scope.buildExamAndExamPaperRelation = function () {
    	$scope.$parent.$parent.formData.examPaper = {};
        for (var i in $scope.itemArr.choosedItemArr) 
        {
        	$scope.$parent.$parent.formData.examPaper = $scope.itemArr.choosedItemArr[i];
        }
    }
    //打开模态框
    $scope.$parent.$parent.openExamPaperModal = function () {
        $scope.openModalDialog = true;
        dialogStatus.setHasShowedDialog(true);
    	$scope.isBuildOldChoosedItems = true;
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
        //清空查询条件
    	$scope.selectPaperType = "";
        //重新查询模态框中的数据
        $scope.search(); 
    }
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildExamAndExamPaperRelation();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}]);
    

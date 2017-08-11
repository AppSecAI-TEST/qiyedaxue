angular.module('ele.exam', ['ele.orgGroup','ele.admin','ele.examPaper'])
.controller('ExamController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
    //用于显示面包屑状态
    $scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理 ', cmc:'cmc-bd', cmn: '考试推送'};
    $scope.url = "/enterpriseuniversity/services/exam/list?pageNum=";
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
                if ($scope.examname!=undefined && $scope.examname!= "") {
                	$scope.$httpPrams = $scope.$httpPrams +"&examname="
                		+$scope.examname.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
                }
                $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
                $http.get($scope.$httpUrl).success(function (response) {
                	$scope.$emit('isLoading', false);
                    $scope.page = response;
                    $scope.paginationConf.totalItems = response.count;
                    $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                    $scope.initChoosedItemStatus();
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
	$scope.updateStatus = function(e,$event) {
		//阻止事件冒泡  ie 及 其他
		window.event? window.event.cancelBubble = true : $event.stopPropagation();
		dialogService.setContent("确定删除考试?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
			$http({
				url:"/enterpriseuniversity/services/exam/deleteExam?examId="+e.examId,
				method:"delete"
			})
			.success(function (response) {
				$timeout(function(){
					if(response.result=="protected"){  
						//dialogService.setContent("不能删除已开始的考试！").setShowDialog(true);
						dialogService.setContent("不能删除已开始的考试！").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        				},2000);
					}else if(response.result=="success"){
						dialogService.setContent("删除成功！").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        					$scope.examname = "";
        					$scope.search();
        				},2000);
					}else if(response.result=="error"){
						//dialogService.setContent("删除考试异常！").setShowDialog(true);
						dialogService.setContent("删除考试异常！").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        				},2000);
					}else if(response.result=="inActivity"){
						//dialogService.setContent("不能删除活动中关联的考试！").setShowDialog(true);
						dialogService.setContent("不能删除活动中关联的考试！").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        				},2000);
					}
				},200);
			})
			.error(function(){
				$timeout(function(){
					//dialogService.setContent("服务器响应异常，请稍后重试！").setShowDialog(true);
					dialogService.setContent("服务器响应异常，请稍后重试！").setShowSureButton(false).setShowDialog(true);
    	    		$timeout(function(){
    					dialogService.sureButten_click(); 
    				},2000);
		    	},200);
			});
		}
	}
	/*
	 * 勾选功能
	 */
	//容器
	$scope.choosedItemArr = [];
	//单选函数
	$scope.chooseItem = function(item){
		if(item.checked){
			for(var i in $scope.choosedItemArr){
				if($scope.choosedItemArr[i].examId==item.examId){
					$scope.choosedItemArr.splice(i,1); 
					break;
				}
			}
			item.checked = false;
		}else{
			item.checked = true;
			$scope.choosedItemArr.push(item);
		}
	}
	//全选当前函数
	$scope.chooseCurrPageAllItem = function(){
		if($scope.selectAll){
			$scope.selectAll = false;
			var choosedItemArrBak = $scope.choosedItemArr.concat();
			for(var i in $scope.page.data){
				var pItem = $scope.page.data[i];
				for(var j in choosedItemArrBak){
					var cItem = choosedItemArrBak[j];
					if(pItem.examId == cItem.examId){
						$scope.choosedItemArr.splice($scope.choosedItemArr.indexOf(cItem),1); 
					}
				}
				pItem.checked = false;
			}
		}else{
			$scope.selectAll = true;
			for(var i in $scope.page.data){
				var pItem = $scope.page.data[i];
				pItem.checked = true;
				var hasContained = false;
				for(var j in $scope.choosedItemArr){
					var cItem = $scope.choosedItemArr[j];
					if(pItem.examId == cItem.examId){
						hasContained = true;
						break;
					}
				}
				if(!hasContained){
					$scope.choosedItemArr.push(pItem);
				}
			} 
		}
	}
	$scope.initChoosedItemStatus = function(){
		$scope.selectAll = false;
		for(var i in $scope.page.data){
			var pItem = $scope.page.data[i];
			for(var j in $scope.choosedItemArr){
				var cItem = $scope.choosedItemArr[j];
				if(pItem.examId == cItem.examId){
					pItem.checked = true;
				}
			}
		}
	}
	// 批量删除
	$scope.deleteExamList = function() {
		if($scope.choosedItemArr.length < 1 ){
			dialogService.setContent("未勾选任何考试！").setShowDialog(true);
    		return;
		}
		var ids = "0";
		for (var i = 0; i < $scope.choosedItemArr.length; i++) {
			ids += "," + $scope.choosedItemArr[i].examId;
		}
		if (ids != "0") {
			dialogService.setContent("确定批量删除考试?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true).doNextProcess = function(){
				$http.get("/enterpriseuniversity/services/exam/deleteExamList?examIds="+ ids)
				.success(function(response) {
					$timeout(function(){
						if(response.result=="success"){
							dialogService.setContent("批量删除成功！").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click(); 
	        					$scope.choosedItemArr = [];//清空容器
	        					$scope.examname = "";
	        					$scope.search();
	        					//$scope.paginationConf.onChange();
	        				},2000);
						}else if(response.result=="protected"){
							//dialogService.setContent("包含已经开始的考试，无法删除！").setShowDialog(true);
							dialogService.setContent("包含已经开始的考试，无法删除！").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click();
	        	    		},2000);
						}else if(response.result=="error"){
							//dialogService.setContent("批量删除失败！").setShowDialog(true);
							dialogService.setContent("批量删除失败！").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click();
	        	    		},2000);
						}
					},500);
				})
				.error(function() {
					$timeout(function(){
						//dialogService.setContent("批量删除失败！").setShowDialog(true);
						dialogService.setContent("批量删除失败！").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click();
        	    		},2000);
					},500);
				});
			}
		}
	}
	 
	//添加考试按钮
    $scope.add = function(){
        $scope.go('/exam/addExam','addExam',{});
    }
    //编辑考试按钮
    $scope.doEditExam = function(e){
        $scope.go('/exam/editExam','editExam',{id:e.examId});
    }
    $scope.modalDialog ={
		'openScoreModal':false,
		'openGradeModel':false
	};
    //成绩信息--------------------------------------------------
    $scope.paginationConf2 = {
        currentPage:1,
        totalItems: 10,
        itemsPerPage:20,
        pagesLength: 15,
        perPageOptions:[20,50,100,'全部'],
        rememberPerPage: 'perPageItems',
        onChange: function(){
        	$scope.$emit('isLoading', true);
        	if(!$scope.scoreExam){
        		$scope.score = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "&examId=" + $scope.scoreExam.examId;
        	$scope.examScoreUrl="/enterpriseuniversity/services/examScore/findPage?pageNum=";
            $scope.$httpUrl = $scope.examScoreUrl + $scope.paginationConf2.currentPage + "&pageSize=" + ($scope.paginationConf2.itemsPerPage == "全部" ? -1 : $scope.paginationConf2.itemsPerPage)+ $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.score = response;
                $scope.paginationConf2.totalItems = response.count;
                $scope.paginationConf2.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            });
	    }
	};
    //查看成绩信息
 	$scope.showScore=function(exam, $event){
 		//阻止事件冒泡  ie 及 其他
 		window.event? window.event.cancelBubble = true : $event.stopPropagation();
 		$scope.scoreExam = exam;
 		$scope.paginationConf2.onChange();
 		$scope.currHighLightRow = {};
 		$scope.modalDialog.openScoreModal=true;
 		dialogStatus.setHasShowedDialog(true);
	}
 	//关闭成绩信息模态框
 	$scope.closeExamPaperModal = function (){
 		$scope.modalDialog.openScoreModal=false;
 		dialogStatus.setHasShowedDialog(false);
 	}
 	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
 	//------------------------------------------------------------成绩信息
 	//试卷评分--------------------------------------------------------
 	//评分按钮
 	$scope.toGrade=function(s){
 		if(s.gradeState==null){
 			$scope.showInput=true;
 		}else{
 			$scope.showInput=false;
 		}
 		$scope.gradeFormInvalidMarkArr = [];//表单合法性校验容器
 		$scope.searchAnswerDetails(s);
 	}
 	//查询考试详细数据
 	$scope.searchAnswerDetails = function(s){
 		$scope.$emit('isLoading', true);
   		$http.get("/enterpriseuniversity/services/answerDetails/find?examId="+s.examId+"&code="+s.code+"&gradeState="+s.gradeState).success(function (response) {
   			$scope.data=response;
   			$scope.$emit('isLoading', false);
   		});
   		$scope.modalDialog.openGradeModel=true;
 	}
 	//关闭评分摸态框
 	$scope.closeGradeModal = function (){
 		$scope.modalDialog.openGradeModel=false;
 	}
 	//表单合法性校验
 	$scope.checkNumber = function(item){
 		var reg = new RegExp("^(0|[1-9][0-9]*)$"); 
 		if(!reg.test(item.score)){
 			item.scoreInvalid = true;
 			if($scope.gradeFormInvalidMarkArr.indexOf(item.topicId)==-1){
 				$scope.gradeFormInvalidMarkArr.push(item.topicId);
 			}
 		}else{
 			if(Number(item.score) <= Number(item.rightScore)){
 				item.scoreInvalid = false;
 				if($scope.gradeFormInvalidMarkArr.indexOf(item.topicId)!=-1){
 					$scope.gradeFormInvalidMarkArr.splice($scope.gradeFormInvalidMarkArr.indexOf(item.topicId),1);
 				}
 			}else{
 				item.scoreInvalid = true;
 				if($scope.gradeFormInvalidMarkArr.indexOf(item.topicId)==-1){
 	 				$scope.gradeFormInvalidMarkArr.push(item.topicId);
 	 			}
 			}
 		}
 	}
 	//评分
 	$scope.grade = function() {
 		if($scope.gradeFormInvalidMarkArr.length>0){
 			dialogService.setContent("存在不合法的评分分值").setShowDialog(true);
			return;
 		}
    	$scope.formData = {
			examId:"",
			code:"",
			topicId	:[],
			rightScore	:[],
			score	:[]
    	};
    	for(var i in $scope.data){
    		if(i==0){
    			$scope.formData.examId = $scope.data[i].examId;
    			$scope.formData.code = $scope.data[i].code;
    		}
    		$scope.formData.topicId.push($scope.data[i].topicId);
    		$scope.formData.rightScore.push($scope.data[i].rightScore);
    		$scope.formData.score.push($scope.data[i].score);
    	}
    	$http({
    		url:"/enterpriseuniversity/services/examScore/grade",
    		method:"POST",
		 	data:$.param($scope.formData), 
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'}
    	}).success(function(data) {
    		$timeout(function(){
	    		if(data.result=="success"){
	    			dialogService.setContent("评分成功").setShowDialog(true);
    				$scope.closeGradeModal();
    				$scope.paginationConf2.onChange();
	    		}else{
	    			dialogService.setContent("评分失败").setShowDialog(true);
	    			$scope.closeGradeModal();
	    		}
    		},200); 
    	})
    	.error(function(){
			  dialogService.setContent("评分失败").setShowDialog(true);
			  $scope.closeGradeModal();
    	}); 
    }
}])
.directive('requiredExamAddress', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value+"-"+scope.myVar;
	              }, function(){
	            	  validity =  scope.myVar ? false : value== undefined ? true :  false ;
	                  ngModel.$setValidity("requiredExamAddress", !validity);
	              });
	        	  return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
.directive('requiredExampaper', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = value== undefined ? true : false ;
	                  ngModel.$setValidity("requiredExampaper", !validity);
	              });
	        	  return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
.directive('requiredExam', [function () {
	return {
	  require: "ngModel",
	  link: function (scope, element, attr, ngModel) {
		  var customValidator = function (value) {
			  var validity = null;
			  scope.$watch(function () {
				  return value ? value.length : 0;
		      }, function(newV, oldV){
	    		  validity = newV ? newV < 1  ? true : false : true;
	    		  ngModel.$setValidity("requiredExam", !validity);
	          });
			  return  value;
	      };
	      ngModel.$formatters.push(customValidator);
	      ngModel.$parsers.push(customValidator);
	  }
	};
}])
//添加考试
.controller('AddExamController', ['$scope', '$http', '$state', 'dialogService','$timeout', function ($scope, $http, $state,dialogService,$timeout) {
    $scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理', cmc:'cmc-bd', cmn: '考试推送', gcmn: '添加考试'};
    $scope.myVar = true;
    $scope.toggle = function() {
    	if($scope.myVar){
    		$scope.exam.offlineExam = "2";
    	}else{
    		$scope.exam.offlineExam = "1";
    		$scope.exam.examAddress = undefined;
    	}
        $scope.myVar = !$scope.myVar;
    };
    $scope.exam = $scope.formData = {
		//初始化容器
		empIds:[],
		deptIds:[],
		labelClassIds:[],
		labelIds:[],
		labelEmps:[],
		tempEmps : []
    }; 
    $scope.$on("$RebuiltEmps", function(){
		$scope.formData.tempEmps = 
			$scope.formData.empIds.concat($scope.formData.empIds.deptIds, $scope.formData.empIds.labelClassIds, $scope.formData.empIds.labelIds, $scope.formData.empIds.labelEmps);
    });
    
//    var nowDate=new Date() ;
//    $scope.defaultStartTime=nowDate.getFullYear()+"-"+(nowDate.getMonth()+1)+"-"+nowDate.getDate()+" 9:00:00";
//    $scope.defaultEndTime=nowDate.getFullYear()+"-"+(nowDate.getMonth()+1)+"-"+nowDate.getDate()+" 19:00:00";
    $scope.onlyNumber=function($event){
        var keyCode = $event.keyCode;
        if(!((keyCode>=48&&keyCode<=57)||keyCode==8||(keyCode>=96&&keyCode<=105))){
        	$event.preventDefault();
        } 
    }
    
//        $scope.characterRule=function($event){
//        	alert($event.keyCode);
//            var keyCode = $event.keyCode;
//            if(keyCode==51){
//            	
//            }else{
//            	$event.preventDefault();
//            }
//        }
    $scope.addExamSubmit =false;
	$scope.validate = function() {
		$scope.examForm.$submitted = true;
		if($scope.examForm.$invalid){
			dialogService.setContent("表单填写不完整,请按提示完善表单后重试").setShowDialog(true);
			return;
	   	}else{
			var beginTimeS = document.getElementById("beginTimeS").value;
	    	var endTimeS0 = document.getElementById("endTimeS0").value;
	    	$scope.exam.beginTimeS = beginTimeS ;
	    	$scope.exam.endTimeS = endTimeS0 ;
	    	$scope.exam.examPaperId=$scope.exam.examPaper.examPaperId;
	    	$scope.exam.examTime=$scope.exam.examPaper.examTime;
	    	if(beginTimeS==""){
		   		dialogService.setContent("请填写开始时间").setShowDialog(true);
		   		return;
		   	}else if(endTimeS0==""){
		   		dialogService.setContent("请填写结束时间").setShowDialog(true);
		   		return;
		   	}else{
		   		beginTimeS = beginTimeS.replace(/-/g,"/");
		   		endTimeS0 = endTimeS0.replace(/-/g,"/");
		   		if(new Date(endTimeS0).getTime()/1000-new Date(beginTimeS).getTime()/1000 < $scope.exam.examPaper.examTime*60){
		   			dialogService.setContent("开始时间、结束时间间隔不得小于考试时长").setShowDialog(true);
		   			return ;
		   		}
		   	}
	   		
	   		if($scope.exam.retakingExamCount > 0){
	   			$scope.exam.rbeginTime= [];
	        	$scope.exam.rendTime= [];
		   		for(var i=0;i<document.getElementsByName("rbeginTime").length;i++){
		   			var beginTime = document.getElementsByName("rbeginTime")[i].value;
		   			var endTime = document.getElementsByName("rendTime")[i].value;
		   			if(beginTime==""||endTime==""){
		   				dialogService.setContent("有未填写的补考时间").setShowDialog(true);
		   				return;
		   			}else{
		   				beginTime = beginTime.replace(/-/g,"/");
		   				endTime = endTime.replace(/-/g,"/");
		   				if((new Date(beginTime).getTime()/1000-new Date(endTimeS0).getTime()/1000)<0){
		   					dialogService.setContent("补考开始时间不得早于考试结束时间").setShowDialog(true);
				   			return ;
		   				}
				   		if(new Date(endTime).getTime()/1000-new Date(beginTime).getTime()/1000 < $scope.exam.examPaper.examTime*60){
				   			dialogService.setContent("补考开始时间至补考结束时间间隔不得小于考试时长").setShowDialog(true);
				   			return ;
				   		}
				   		$scope.exam.rbeginTime.push(beginTime);
				   		$scope.exam.rendTime.push(endTime);
		   			}
		   		}
		   	}
	    	//处理线上/线下考试
	    	if($scope.myVar){
	    		$scope.exam.offlineExam="1";
	        }else{
	        	$scope.exam.offlineExam="2";
	        }
	    	$scope.exam.beginTimeS=document.getElementById("beginTimeS").value;
	    	$scope.exam.endTimeS=document.getElementById("endTimeS0").value;
	    	$scope.exam.examPaperId=$scope.exam.examPaper.examPaperId;
	    	$scope.exam.examTime=$scope.exam.examPaper.examTime;
	    	var rbeginTimes=new Array();
	    	var rendTimes=new Array();
	    	for(var i=0;i<document.getElementsByName("rbeginTime").length;i++){
	    		rbeginTimes.push(document.getElementsByName("rbeginTime")[i].value);
	    		rendTimes.push(document.getElementsByName("rendTime")[i].value);
	    	}
	    	$scope.exam.rbeginTime=rbeginTimes;
	    	$scope.exam.rendTime=rendTimes;
	    	$scope.addExamSubmit =true;
	    	$http({
	    		url:"/enterpriseuniversity/services/exam/add",
	    		method:"POST",
			 	data:$.param($scope.exam), 
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'}
	    	}).success(function(data) {
	    		$timeout(function(){
		    		if(data.result=="success"){
		    			dialogService.setContent("新增考试成功！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.addExamSubmit=false;
							$scope.go('/exam/examList','examList',{});
						},2000);
		    		}else{
		    			dialogService.setContent("新增考试失败！").setShowSureButton(false).setShowDialog(true); 
						$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.addExamSubmit=false;
						},2000);
		    		}
		    		$scope.addExamSubmit =false;
	    		},200);  
			  })
			  .error(function(){
				  dialogService.setContent("新增考试失败！").setShowSureButton(false).setShowDialog(true); 
					$timeout(function(){
						dialogService.sureButten_click(); 
						$scope.addExamSubmit=false;
					},2000);
				  $scope.addExamSubmit =false;
			  }); 
		}
	}
        
	$scope.addInput=function(){
		if($scope.exam.retakingExamCount>=0&&$scope.exam.retakingExamCount<100){
			var s='';
			for(var i=1;i<=$scope.exam.retakingExamCount;i++){
	    		s+='<div class="form-group">'+
	    	    '<label  class="col-xs-2 control-label">补考时间'+i+'：</label>'+
	    			  '<div class="col-xs-2">'+
	    	      '<label for="beginTime">开始时间</label>'+
	    	      '<input class="Wdate form-control" id="beginTimeS'+i+'" name="rbeginTime" type="text" onFocus="WdatePicker({startDate:\'%y-%M-%d 09:00:00\',lang:\'zh-cn\',dateFmt:\'yyyy-MM-dd HH:mm:ss\',minDate:\'#F{$dp.$D(\\\'endTimeS'+(i-1)+'\\\')}\'})" readonly>'+
	    	    '</div>'+
	    	    '<div class="col-xs-2">'+
	    	      '<label for="endTime">结束时间</label>'+
	    	      '<input class="Wdate form-control" id="endTimeS'+i+'" name="rendTime" type="text" onFocus="WdatePicker({startDate:\'%y-%M-%d 19:00:00\',lang:\'zh-cn\',dateFmt:\'yyyy-MM-dd HH:mm:ss\',minDate:\'#F{$dp.$D(\\\'beginTimeS'+i+'\\\')}\'})" readonly>'+
	    	    '</div>'+
	    		'</div>';
			}
			document.getElementById("retakingExamTime").innerHTML=s;
		}
		if($scope.exam.retakingExamCount==undefined){
			document.getElementById("retakingExamTime").innerHTML = "";
		}
		if($scope.exam.retakingExamCount>=100||$scope.exam.retakingExamCount<0){
			document.getElementById("retakingExamTime").innerHTML = "";
			dialogService.setContent("补考次数超出限制,请重新输入0~100的数字").setShowDialog(true);
			sure = function(){}
		}
	}
    $scope.returnToList=function(){
    	$scope.go('/exam/examList','examList',{});
	}
}])

//编辑考试控制器
.controller('EditExamController', ['$scope', '$http', '$state','$stateParams', '$q', '$timeout', 'dialogService', function ($scope, $http, $state, $stateParams, $q, $timeout, dialogService) {
    //编辑
    $scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理 ', cmc:'cmc-bd', cmn: '考试推送', gcmn: '编辑考试'};
    $scope.deferred = $q.defer();
    if ($stateParams.id) {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/exam/findOne?examId=" + $stateParams.id).success(function (response) {
        	$scope.deferred.resolve(response);
        }).error(function(response){
    		$scope.deferred.reject(response); 
    	})
    } else {
        //未传参数
    	dialogService.setContent("未获取到传递的参数").setShowDialog(true);
    	return;
    }
    
    $scope.$on("$RebuiltEmps", function(){
		$scope.formData.tempEmps = 
			$scope.formData.empIds.concat($scope.formData.empIds.deptIds, $scope.formData.empIds.labelClassIds, $scope.formData.empIds.labelIds, $scope.formData.empIds.labelEmps);
    });
    
//    var nowDate=new Date() ;
//    $scope.defaultStartTime=nowDate.getFullYear()+"-"+(nowDate.getMonth()+1)+"-"+nowDate.getDate()+" 9:00:00";
//    $scope.defaultEndTime=nowDate.getFullYear()+"-"+(nowDate.getMonth()+1)+"-"+nowDate.getDate()+" 19:00:00";
    
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.$emit('isLoading', false);
    	$scope.exam = $scope.formData = data; 
    	$scope.exam.beginTimeS = $scope.exam.beginTime;
		$scope.exam.endTimeS = $scope.exam.endTime;
		if($scope.exam.offlineExam=="1"){
			$scope.myVar = true;
		}else{
			$scope.myVar = false;
		}
		$scope.addInput();
		$http.get("/enterpriseuniversity/services/examPaper/findOne?examPaperId="+$scope.exam.examPaperId).success(function (data) {
			$scope.exam.examPaper=data;
		}).error(function(data){
    		$scope.deferred.reject(data); 
    	});
		$scope.showDate();
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("查询出错").setShowDialog(true);
    });
    
    $scope.myVar = true;
    $scope.toggle = function() {
    	if($scope.myVar){
    		$scope.exam.offlineExam = "2";
    	}else{
    		$scope.exam.offlineExam = "1";
    		$scope.exam.examAddress = undefined;;
    	}
        $scope.myVar = !$scope.myVar;
    };
    
    /*$scope.onlyNumber=function($event){
//        	alert($event.keyCode);
            var keyCode = $event.keyCode;
            if((keyCode>=48&&keyCode<=57)||keyCode==8||(keyCode>=96&&keyCode<=105)){
            	
            }else{
            	$event.preventDefault();
            }
        }*/
    $scope.onlyNumber=function($event){
//        	alert($event.keyCode);
        var keyCode = $event.keyCode;
        if(!((keyCode>=48&&keyCode<=57)||keyCode==8||(keyCode>=96&&keyCode<=105))){
        	$event.preventDefault();
        } 
    }
    $scope.showDate=function(){
    	var rbeginTimeObjs=document.getElementsByName("rbeginTime");
		var rendTimeObjs=document.getElementsByName("rendTime");
		/*for(var i=1;i<rendTimeObjs.length+1;i++){
			rbeginTimeObjs[i-1].value=$scope.exam.rbeginTime[i];
			rendTimeObjs[i-1].value = $scope.exam.rendTime[i];
		}*/
		if($scope.exam.retakingExamCount > 0){
			for(var i=1 ; i< $scope.exam.retakingExamCount+1 ; i++){
				if( $scope.exam.rbeginTime[i]!=undefined){
					rbeginTimeObjs[i-1].value = $scope.exam.rbeginTime[i];
					rendTimeObjs[i-1].value = $scope.exam.rendTime[i];
				}
			}
		}
    }

    $scope.addInput=function(){
		if($scope.exam.retakingExamCount>=0&&$scope.exam.retakingExamCount<100)
		{
			var s='';
			for(var i=1;i<=$scope.exam.retakingExamCount;i++){
	    		s+='<div class="form-group">'+
	    	    '<label  class="col-xs-2 control-label">补考时间'+i+'：</label>'+
	    			  '<div class="col-xs-2">'+
	    	      '<label for="beginTime">开始时间</label>'+
	    	      '<input class="Wdate form-control" id="beginTimeS'+i+'" name="rbeginTime" type="text" onFocus="WdatePicker({startDate:\'%y-%M-%d 09:00:00\',lang:\'zh-cn\',dateFmt:\'yyyy-MM-dd HH:mm:ss\',minDate:\'#F{$dp.$D(\\\'endTimeS'+(i-1)+'\\\')}\'})" readonly>'+
	    	    '</div>'+
	    	    '<div class="col-xs-2">'+
	    	      '<label for="endTime">结束时间</label>'+
	    	      '<input class="Wdate form-control" id="endTimeS'+i+'" name="rendTime" type="text" onFocus="WdatePicker({startDate:\'%y-%M-%d 19:00:00\',lang:\'zh-cn\',dateFmt:\'yyyy-MM-dd HH:mm:ss\',minDate:\'#F{$dp.$D(\\\'beginTimeS'+i+'\\\')}\'})" readonly>'+
	    	    '</div>'+
	    		'</div>';
			}
			document.getElementById("retakingExamTime").innerHTML=s;
		}
		if($scope.exam.retakingExamCount==undefined){
			document.getElementById("retakingExamTime").innerHTML = "";
		}
		if($scope.exam.retakingExamCount>=100||$scope.exam.retakingExamCount<0)
		{
			document.getElementById("retakingExamTime").innerHTML = "";
			dialogService.setContent("补考次数超出限制,请重新输入0~100的数字！").setShowDialog(true);
		}
		$scope.showDate();
	}
    
    $scope.editExamSubmit=false;
    $scope.update=function(){
    	$scope.examForm.$submitted = true;
    	
    	if($scope.examForm.$invalid)
	   	{
			dialogService.setContent("表单填写不完整,请按提示完善表单后重试").setShowDialog(true);
	   		return;
	   	}
		else
	   	{
			var beginTimeS = document.getElementById("beginTimeS").value;
	    	var endTimeS0 = document.getElementById("endTimeS0").value;
	    	$scope.exam.beginTimeS = beginTimeS ;
	    	$scope.exam.endTimeS = endTimeS0 ;
	    	$scope.exam.examPaperId=$scope.exam.examPaper.examPaperId;
	    	$scope.exam.examTime=$scope.exam.examPaper.examTime;
	    	if(beginTimeS==""){
		   		dialogService.setContent("请填写开始时间").setShowDialog(true);
		   		return;
		   	}else if(endTimeS0==""){
		   		dialogService.setContent("请填写结束时间").setShowDialog(true);
		   		return;
		   	}else{
		   		beginTimeS = beginTimeS.replace(/-/g,"/");
		   		endTimeS0 = endTimeS0.replace(/-/g,"/");
		   		if(new Date(endTimeS0).getTime()/1000-new Date(beginTimeS).getTime()/1000 < $scope.exam.examPaper.examTime*60){
		   			dialogService.setContent("开始时间、结束时间间隔不得小于考试时长").setShowDialog(true);
		   			return ;
		   		}
		   	}
	   		
	   		if($scope.exam.retakingExamCount > 0){
	   			$scope.exam.rbeginTime= [];
	        	$scope.exam.rendTime= [];
		   		for(var i=0;i<$scope.exam.retakingExamCount;i++){
		   			var beginTime = document.getElementsByName("rbeginTime")[i].value;
		   			var endTime = document.getElementsByName("rendTime")[i].value;
		   			if(beginTime==""||endTime==""){
		   				dialogService.setContent("有未填写的补考时间").setShowDialog(true);
		   				return;
		   			}else{
		   				$scope.exam.rbeginTime.push(beginTime);
				   		$scope.exam.rendTime.push(endTime);
				   		
		   				beginTime = beginTime.replace(/-/g,"/");
		   				endTime = endTime.replace(/-/g,"/");
		   				if((new Date(beginTime).getTime()/1000-new Date(endTimeS0).getTime()/1000)<0){
		   					dialogService.setContent("补考开始时间不得早于考试结束时间").setShowDialog(true);
				   			return ;
		   				}
				   		if(new Date(endTime).getTime()/1000-new Date(beginTime).getTime()/1000 < $scope.exam.examPaper.examTime*60){
				   			dialogService.setContent("补考开始时间、结束时间间隔不得小于考试时长").setShowDialog(true);
				   			return ;
				   		}
		   			}
		   		}
		   	}
	    	//处理线上/线下考试
	    	if($scope.myVar){
	    		$scope.exam.offlineExam="1";
	        }else{
	        	$scope.exam.offlineExam="2";
	        }
	    	$scope.editExamSubmit=true;
        	$http({
	    		url:"/enterpriseuniversity/services/exam/update",
	    		method:"PUT",
   			 	data:$.param($scope.exam), 
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'}
	    	})
    		.success(function(data) {
    			$timeout(function(){
					if(data.result=="success"){
//						dialogService.setContent("修改考试成功").setShowDialog(true);
//						$scope.go('/exam/examList','examList',{});
						dialogService.setContent("修改考试成功！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.editExamSubmit=false;
							$scope.go('/exam/examList','examList',{});
						},2000);
					}else if(data.result=="protected"){
//						dialogService.setContent("不能编辑已开始考试").setShowDialog(true);
						dialogService.setContent("不能编辑已开始考试！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.editExamSubmit=false;
						},2000);
					}else if(response.result=="inActivity"){
//						dialogService.setContent("不能删除活动中的考试！").setShowDialog(true);
						dialogService.setContent("不能删除活动中的考试！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.editExamSubmit=false;
						},2000);
					}else{
//						dialogService.setContent("修改考试失败").setShowDialog(true);
						dialogService.setContent("修改考试失败！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.editExamSubmit=false;
						},2000);
					}
					$scope.editExamSubmit=false;
    			},200);
    		})
			.error(function(){
//				dialogService.setContent("修改考试失败").setShowDialog(true);
				dialogService.setContent("修改考试失败！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.editExamSubmit=false;
				},2000);
				$scope.editExamSubmit=false;
			}); 
	   	}
    }
    $scope.returnToList=function(){
    	$scope.go('/exam/examList','examList',{});
    }
}]) 
.controller('ScoreUploadController', ['$scope','$http', 'FileUploader','dialogService','$timeout', function ($scope,$http,FileUploader,dialogService,$timeout) {
    var excelUploader = $scope.excelUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/examScore',
            autoUpload:true                           
        }
    );
    //导入成绩过滤器
    excelUploader.filters.push({
        name: 'customFilter',
        fn: function(item , options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            var size = item.size;
            if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0)
            {
            	dialogService.setContent("要导入新的成绩，请先删除旧的成绩").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("超出上传文件数量限制").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|vnd.ms-excel|vnd.openxmlformats-officedocument.spreadsheetml.sheet|'.indexOf(type) == -1)
            {
            	dialogService.setContent("上传文件格式不符合要求").setShowDialog(true);
                return '|vnd.ms-excel|vnd.openxmlformats-officedocument.spreadsheetml.sheet|'.indexOf(type) !== -1;
            }
            return true;
        }
    });
    //删除 
    $scope.removeItem = function (item) {
        item.remove();
        document.getElementById("fileInput").value=[];
    }
    $scope.uploadItem = function(item){
    	if(!(item.isReady || item.isUploading || item.isSuccess)){
    		 item.upload();
    	}
    }
    // CALLBACKS
    excelUploader.onSuccessItem = function (fileItem, response, status, headers) {
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
        	$http.get("/enterpriseuniversity/services/exam/importResults?examId="+document.getElementById("examId").value+"&filePath="+response.url).success(function (response) {
	    		if(response.result=="error"){
	    			dialogService.setContent("考试成绩导入失败").setShowDialog(true);
	    		}else{
	    			dialogService.setContent(response.result).setShowSureButton(false).setShowDialog(true);
	   			 	$timeout(function(){
	   			 		dialogService.sureButten_click(); 
	   			 	},2000);
	    		}
    		}).error(function(response){
    			dialogService.setContent("服务器响应异常，考试成绩导入失败").setShowDialog(true);
        	});
        }
    };
}]);

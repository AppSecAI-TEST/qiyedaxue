angular.module('ele.questionBank', [ 'ele.admin' ])
.controller('QuestionBankController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus',function( $scope, $http, $timeout, dialogService, dialogStatus) {
	$scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理', cmc:'cmc-bb', cmn: '题库列表'};
	// 显示题库列表
	$scope.url = "/enterpriseuniversity/services/questionBank/chooseQuestionBank?pageNum=";
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
			if ($scope.questionBankName != undefined && $scope.questionBankName != "") {
				$scope.$httpPrams = $scope.$httpPrams + "&questionBankName=" 
					+ $scope.questionBankName.replace(/\%/g,"%25").replace(/\#/g,"%23")
						.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
						.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
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
					$scope.initChoosedItemStatus();
			});
		}
	};
	// 搜索按钮
	$scope.search = function() {
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
	// 添加题库按钮
	$scope.doAdd = function() {
		$scope.go('/exam/addQuestionBank', 'addQuestionBank',{});
	}
	// 追加题库按钮
	$scope.superadditionQuestionBank = function(question) {
		$scope.go('/exam/superadditionQuestionBank','superadditionQuestionBank', {id : question.questionBankId});
	}
	// 编辑题库按钮
	$scope.edit = function(question) {
		$scope.go('/exam/editQuestionBank', 'editQuestionBank',{id : question.questionBankId});
	}
	// 查看题库
	$scope.modalDialog = {
		'openQuestionModal' : false,
	};

	$scope.openQuestionModal = function(c) {
		$scope.questionBank = c;
		var url = "/enterpriseuniversity/services/questionBank/findQuestionBank?questionBankId=" + c.questionBankId;
		$http.get(url).success(function(response) {
			$scope.questionBankDetail = response;
		});
		$scope.modalDialog.openQuestionModal = true;
		dialogStatus.setHasShowedDialog(true);
	}

	$scope.closeQuestionModal = function() {
		$scope.modalDialog.openQuestionModal = false;
		dialogStatus.setHasShowedDialog(false);
	}
	// 删除
	$scope.deleteQuestion = function(c,$event) {
		//阻止事件冒泡  ie 及 其他
		window.event? window.event.cancelBubble = true : $event.stopPropagation();
		dialogService.setContent('确定要删除吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
		//if (window.confirm('确定要删除吗？')) {
			$http({
				url : "/enterpriseuniversity/services/questionBank/deleteQuestionBank?questionBankId=" + c.questionBankId,
				method : "delete"
			}).success(function(response) {
				$timeout(function(){
					if(response.result=="error"){
						dialogService.setContent("删除失败！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}else if(response.result=="success"){
						dialogService.setContent("删除成功！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.questionBankName = "";
							$scope.search();
						},2000);
					}else if(response.result=="protected"){
						dialogService.setContent("题库题目被占用，无法删除！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}
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
				if($scope.choosedItemArr[i].questionBankId==item.questionBankId){
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
					if(pItem.questionBankId == cItem.questionBankId){
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
					if(pItem.questionBankId == cItem.questionBankId){
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
				if(pItem.questionBankId == cItem.questionBankId){
					pItem.checked = true;
				}
			}
		}
	}
	
	// 批量删除
	$scope.deleteQuestionBank = function() {
		if($scope.choosedItemArr.length < 1 ){
			dialogService.setContent("未勾选任何题库！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
			},2000);
    		return;
		}
		var ids = "0";
		for (var i = 0; i < $scope.choosedItemArr.length; i++) {
			ids += "," + $scope.choosedItemArr[i].questionBankId;
		}
		if (ids != "0") {
			dialogService.setContent('确定要批量删除题库吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
			.doNextProcess = function(){
				$http.get("/enterpriseuniversity/services/questionBank/deleteQuestionBank?quetionBankId=" + ids)
					.success(function(response) {
						$timeout(function(){
							if(response.result=="success"){
	//							$timeout(function(){
	//								dialogService.setContent("删除成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.search();}
	//							},200);
								dialogService.setContent("批量删除成功！").setShowSureButton(false).setShowDialog(true);
					    		$timeout(function(){
									dialogService.sureButten_click(); 
									$scope.choosedItemArr = [];//清空容器
									$scope.search();
								},2000);
							}else if(response.result=="protected"){
	//							$timeout(function(){
	//								dialogService.setContent("题库题目被占用，无法删除！").setShowDialog(true)
	//							},200);
								dialogService.setContent("题库题目被占用，无法删除！").setShowSureButton(false).setShowDialog(true);
					    		$timeout(function(){
									dialogService.sureButten_click(); 
								},2000);
							}else if(response.result=="error"){
	//							$timeout(function(){
	//								dialogService.setContent("删除失败！").setShowDialog(true)
	//							},200);
								dialogService.setContent("批量删除失败！").setShowSureButton(false).setShowDialog(true);
					    		$timeout(function(){
									dialogService.sureButten_click(); 
								},2000);
							}
						},200);
					})
					.error(function() {
//						$timeout(function(){
//							dialogService.setContent("删除失败！").setShowDialog(true)
//						},200);
						dialogService.setContent("删除失败！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
				});
			}  
		}
	}
		
}])
// 题库文件上传控制器
.controller('QuestionBankFileUploadController',['$scope', 'FileUploader', 'dialogService','$timeout','$http',function($scope, FileUploader,dialogService,$timeout,$http) {
	var uploader = $scope.uploader = new FileUploader({
		url : '/enterpriseuniversity/services/file/upload/questionBank',
		autoUpload:true
	});
	uploader.filters.push({
		name : 'customFilter',
		fn : function(item, options) {
			var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
			if ('|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) == -1) {
				dialogService.setContent("文件格式不正确！").setShowDialog(true)
			}
			return '|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) !== -1 && this.queue.length < 1;
		}
	});
	// 删除
	$scope.removeItem = function(item) {
		if(item.fileUrl!=null){
    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
    	}
		// 从queue中删除
		item.remove();
		document.getElementById("questionBankFileInput").value=[];
		$scope.$parent.questionBank.filePath = "";
		$scope.$parent.questionBank.fileName = "";
	}
	$scope.uploadItem = function(item){
    	if(!(item.isReady || item.isUploading || item.isSuccess)){
    		 item.upload();
    	}
    }
	// CALLBACKS
	uploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	$timeout(function(){
				dialogService.setContent("获取上传文件地址失败！").setShowDialog(true)
			},200);
        	//alert("获取上传文件地址失败");
        }
        else
        {
        	fileItem.fileUrl=response.url;
        	$scope.$parent.questionBank.fileName = fileItem.file.name.slice(0,fileItem.file.name.lastIndexOf('.')) ;
        	$scope.$parent.questionBank.filePath = response.url;//接收返回的文件地址
        }
    };
}])
//添加题库
.controller('AddQuestionBankController', ['$scope', '$http', '$state','dialogService','$timeout', function($scope, $http, $state,dialogService,$timeout) {
	$scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理', cmc:'cmc-bb', cmn: '题库列表', gcmn: '添加题库'};
	$scope.questionBank = $scope.formData = {
		questionClassificationId:"",
		filePath:""
	};
	//文件导入与逐个添加试题切换
	$scope.addquestion=true;
	$scope.automataddQuestionBank=function(){
		$scope.addquestion=false;
	}
	$scope.addQuestionBank=function(){
		$scope.addquestion=true;
	}
	
//初始化题型
	$scope.single=true;
	$scope.mcq=false;
	$scope.gap=false;
	$scope.essay=false;
	$scope.tOrF=false;
	
	//单选
	$scope.opensingle=function(){
		$scope.questionType="1";
		$scope.tompicName="";
		$scope.single=true;
		$scope.mcq=false;
		$scope.gap=false;
		$scope.essay=false;
		$scope.tOrF=false;
	}
	//多选
	$scope.openmcq=function(){
		$scope.questionType="2";
		$scope.tompicName="";
		$scope.single=false;
		$scope.mcq=true;
		$scope.gap=false;
		$scope.essay=false;
		$scope.tOrF=false;
	}
	//填空
	$scope.opengap=function(){
		$scope.questionType="3";
		$scope.tompicName="";
		$scope.single=false;
		$scope.mcq=false;
		$scope.gap=true;
		$scope.essay=false;
		$scope.tOrF=false;
	}
	//简答
	$scope.openJessay=function(){
		$scope.questionType="4";
		$scope.tompicName="";
		$scope.single=false;
		$scope.mcq=false;
		$scope.gap=false;
		$scope.essay=true;
		$scope.tOrF=false;
	}
	//判断
	$scope.opentOrF=function(){
		$scope.questionType="5";
		$scope.tompicName="";
		$scope.single=false;
		$scope.mcq=false;
		$scope.gap=false;
		$scope.essay=false;
		$scope.tOrF=true;
	}
	
//初始化判断题选项
 //	$scope.yes="正确";
 //	$scope.no="错误";
	//单选多选选项控制
	$scope.optionNameArr=[];
	 $scope.$watch(function(){
	    	return $scope.optionNameArr.length ;
	    },function(){
	    	$scope.sectionLength = $scope.optionNameArr.length ;
	    });
	$scope.addOptionName = function(){
		$scope.$emit("addoptionname", new Date().getTime());
	}
	
	var keyStr = "ABCDEFGHIJ";
    var keyArr = keyStr.split("");
    var nextKey = $scope.optionNameArr.length;
    
	$scope.$on("addoptionname",function(e, param){
		e.stopPropagation();
		$scope.optionNameArr.push({
			key : param,
			optionName : "",
			rightCell : "",
			questionType : "",
			optionKey: null,
		});
		for(var i in $scope.optionNameArr){
    		$scope.optionNameArr[i].optionKey = keyArr[i]; 
    	}
		//选项不超过10个
		var currentOptionCount=0;
		if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0)
		{
			currentOptionCount=$scope.currFileUrlQueue.length;
		}
		if((currentOptionCount+$scope.optionNameArr.length)>=10)
		{
			dialogService.setContent("选项最多有10个").setShowDialog(true);
			return (currentOptionCount+$scope.optionNameArr.length) <10;
		}
	});
	/*if(i>=10){
		dialogService.setContent("选项最多有10个").setShowDialog(true);
		return i<10;
	}*/
	
	 $scope.removeOptionName = function(item){
	    	$scope.$emit("removeoptionname", item);
	    }
	$scope.$on("removeoptionname",function(e, item){
		e.stopPropagation();
		$scope.optionNameArr.splice($scope.optionNameArr.indexOf(item), 1);
		for(var i in $scope.optionNameArr){
    		$scope.optionNameArr[i].optionKey = keyArr[i];
    	}
	});
	
	$http.get("/enterpriseuniversity/services/questionBank/classificationList?pageSize=-1")
		.success(function(response) {
			// alert(response.data);
			$scope.type = response.data;
		}).error(function() {
			$timeout(function(){
				dialogService.setContent("题库分类下拉菜单初始化错误！").setShowDialog(true)
			},200);
			//alert("题库分类下拉菜单初始化错误");
		});
	// 添加题库返回
	$scope.returnAddQuestionBank = function() {
		$scope.go('/exam/questionBankList', 'questionBankList',{});
	}
	
	$scope.itemArr = {
        choosedItemArr: [],
        deleteItem : function (item) {
        	$scope.itemArr.choosedItemArr = [];
        	$scope.questionBank.questionClassificationId = "";
			$scope.questionBank.questionClassificationName = "";
        }
	}
	// Excel模板下载
//					$scope.downloadExcel = function() {
//						$http.get(
//										"/enterpriseuniversity/services/file/download?filePath=file/ItemBankTemplate/题库模板.xlsx&fileName=题库模板.xlsx")
//								.success(function(response) {
//									alert("下载成功");
//								}).error(function() {
//									alert("下载失败");
//								});
//					}
	
	// 提交表单
	$scope.addQuestionBankSubmit=false;
	$scope.doSave = function() {
		$scope.questionBankForm.$submitted = true;
    	if($scope.questionBankForm.$invalid)
	   	{
    		$timeout(function(){
				dialogService.setContent("表单验证未通过,请按提示修改表单后重试1！").setShowDialog(true)
			},200);
    		//alert("表单验证未通过,请按提示修改表单后重试！");
	   		return;
	   	} 
    	$scope.addQuestionBankSubmit=true;
		$http({
			method : 'POST',
			url : '/enterpriseuniversity/services/questionBank/addQuestionBank',
			data : $.param($scope.questionBank),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
//			$timeout(function(){
//				dialogService.setContent(data.result).setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
//					if(data.result=="添加题库成功"){
//						$scope.go('/exam/questionBankList','questionBankList', null);
//					}else if(data.result=="题库添加失败"){
//						$scope.go('/exam/questionBankList','questionBankList', null);
//					}
//					$scope.addQuestionBankSubmit=false;
//				}
//			},200);
			if(data.result=="success"){
				dialogService.setContent("添加题库成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addQuestionBankSubmit=false;
					$scope.go('/exam/questionBankList','questionBankList', null);
				},2000);
			}else{
				dialogService.setContent(data.result).setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addQuestionBankSubmit=false;
				},2000);
			}
		}).error(function() {
//			dialogService.setContent("新增题库失败！").setShowDialog(true)
			dialogService.setContent(data.result).setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addQuestionBankSubmit=false;
			},2000);
			$scope.addQuestionBankSubmit=false;
		});
	};
	
	// 提交表单
	$scope.addQuestionBankSubmit1=false;
	$scope.doAutomaSave = function() {
		$scope.questionBankForm1.$submitted = true;
		alert($scope.questionBankForm1);
		
    	if($scope.questionBankForm1.$invalid)
	   	{
    		$timeout(function(){
				dialogService.setContent("表单验证未通过,请按提示修改表单后重试2！").setShowDialog(true)
			},200);
    		//alert("表单验证未通过,请按提示修改表单后重试！");
	   		return;
	   	} 
    	$scope.addQuestionBankSubmit1=true;
		$http({
			method : 'POST',
			url : '/enterpriseuniversity/services/questionBank/automataddQuestionBank',
			data : $.param($scope.questionBank),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
//			$timeout(function(){
//				dialogService.setContent(data.result).setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
//					if(data.result=="添加题库成功"){
//						$scope.go('/exam/questionBankList','questionBankList', null);
//					}else if(data.result=="题库添加失败"){
//						$scope.go('/exam/questionBankList','questionBankList', null);
//					}
//					$scope.addQuestionBankSubmit=false;
//				}
//			},200);
			if(data.result=="success"){
				dialogService.setContent("添加题库成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addQuestionBankSubmit1=false;
					$scope.go('/exam/questionBankList','questionBankList', null);
				},2000);
			}else{
				dialogService.setContent(data.result).setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addQuestionBankSubmit1=false;
				},2000);
			}
		}).error(function() {
//			dialogService.setContent("新增题库失败！").setShowDialog(true)
			dialogService.setContent(data.result).setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addQuestionBankSubmit1=false;
			},2000);
			$scope.addQuestionBankSubmit1=false;
		});
	};
}])
// 编辑题库控制器
.controller('EditQuestionBankController',['$scope', '$http', '$state', '$stateParams','$q','dialogService','$timeout', function($scope, $http, $state, $stateParams, $q,dialogService,$timeout) {
	$scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理 ', cmc:'cmc-bb', cmn: '题库列表', gcmn : '编辑题库'};
	$scope.deferred = $q.defer();
	if ($stateParams.id) {
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/questionBank/findQuestionBank?questionBankId=" + $stateParams.id)
		.success(function(data) {
			$scope.deferred.resolve(data);
		}).error(function(data){
    		$scope.deferred.reject(data); 
    	});
	} else {
		// 未传参数
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
	}
	$scope.deferred.promise.then(function(data) { // 成功回调
		$scope.$emit('isLoading', false);
		$scope.questionBank = $scope.formData = data;
		$scope.questionBank.deleteTopicIds=[];
		$scope.itemArr.choosedItemArr.push({
			questionClassificationId:$scope.questionBank.questionClassificationId,
			questionClassificationName:$scope.questionBank.questionClassificationName});
	}, function(data) { // 错误回调
		$scope.$emit('isLoading', false);
		dialogService.setContent("页面数据初始化异常").setShowDialog(true)
	});
	//删除按钮
	$scope.deleteProblems = function(t) {
		dialogService.setContent('确定要删除吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
			$scope.questionBank.deleteTopicIds.push(t.topicId);
			$scope.questionBank.topics.splice($scope.questionBank.topics.indexOf(t),1);
		}
	};
	//全选按钮
	$scope.chooseAllItem = function(){
		if($scope.selectAll){
			$scope.selectAll = false;
			for(var i in $scope.questionBank.topics){
				$scope.questionBank.topics[i].checked = false;
			}
		}else{
			$scope.selectAll = true;
			for(var i in $scope.questionBank.topics){
				$scope.questionBank.topics[i].checked = true;
			}
		}
	}
	//单选
	$scope.chooseItem = function(item){
		if(item.checked){
			item.checked = false;
			$scope.questionBank.deleteTopicIds.splice($scope.questionBank.deleteTopicIds.indexOf(item.topicId),1);
		}else{
			item.checked = true;
			$scope.questionBank.deleteTopicIds.push(item.topicId);
		}
	}
	// 批量删除
	$scope.batchDeleteProblems = function() {
		if($scope.selectAll){
			dialogService.setContent('确定要批量删除题目吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
			.doNextProcess = function(){
				for(var i in $scope.questionBank.topics){
					$scope.questionBank.deleteTopicIds.push($scope.questionBank.topics[i].topicId);
				}
				$scope.questionBank.topics = [];
			}
		}else{
			if($scope.questionBank.deleteTopicIds.length<1){
				dialogService.setContent("请勾选题目后,再批量删除！").setShowDialog(true);
				return;
			}else{
				dialogService.setContent('确定要批量删除题目吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
				.doNextProcess = function(){
					var topicsArry = $scope.questionBank.topics.concat();
					for(var i in $scope.questionBank.deleteTopicIds){
						for(var j in topicsArry){
							if($scope.questionBank.deleteTopicIds[i] == topicsArry[j].topicId){
								$scope.questionBank.topics.splice($scope.questionBank.topics.indexOf(topicsArry[j]),1);
							}
						}
					}
				}
			}
		}
	}
	$scope.itemArr = {
        choosedItemArr: [],
        deleteItem : function (item) {
        	$scope.itemArr.choosedItemArr = [];
        	$scope.questionBank.questionClassificationId = "";
			$scope.questionBank.questionClassificationName = "";
        }
	}
	// 编辑题库返回
	$scope.doReturn = function() {
		$scope.go('/exam/questionBankList', 'questionBankList',{});
	}
	// 编辑题库保存
	$scope.editQuestionBankSubmit=false;
	$scope.doSave = function() {
		$scope.editQuestionBankForm.$submitted = true;
    	if($scope.editQuestionBankForm.$invalid)
	   	{
    		dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true);
	   		return;
	   	}
    	$scope.editQuestionBankSubmit=true;
		$http({
			url : "/enterpriseuniversity/services/questionBank/updateQuestionBank",
			method : "PUT",
			data : $.param($scope.questionBank),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		})
		.success(function(data) {
			$scope.editQuestionBankSubmit=false;
			if(data.result=="error"){
				dialogService.setContent("修改题库失败！").setShowSureButton(false).setShowDialog(true); 
				$timeout(function(){
					dialogService.sureButten_click(); 
				},2000);
			}else if(data.result=="success"){
				dialogService.setContent("修改题库成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.go('/exam/questionBankList','questionBankList', {});
				},2000);
			}else if(data.result=="protected"){
				dialogService.setContent("题库题目被占用，无法修改！").setShowSureButton(false).setShowDialog(true); 
				$timeout(function(){
					dialogService.sureButten_click(); 
				},2000);
			}
		})
		.error(function() {
			$scope.editQuestionBankSubmit=false;
			dialogService.setContent("修改题库失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
			},2000);
		});
	}
}])
// 追加题库控制器
.controller('SuperadditionQuestionBankController',['$scope','$http','$state','$stateParams', '$q','dialogService','$timeout', function($scope, $http, $state, $stateParams ,$q,dialogService,$timeout) {
	// 追加
	$scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理 ', cmc:'cmc-bb', cmn: '题库列表', gcmn : '追加题库'};
	$scope.deferred = $q.defer();
	$scope.questionBank = $scope.formData = {};
	if ($stateParams.id) {
		$http.get("/enterpriseuniversity/services/questionBank/findQuestionBank?questionBankId=" + $stateParams.id)
			.success(function(data) {
				$scope.deferred.resolve(data);
			});
	} 
	else
	{
		// 未传参数
	}
	$scope.deferred.promise.then(function(data) { // 成功回调
		$scope.questionBank = $scope.formData = data;
		//清空旧的文件url
		$scope.questionBank.filePath = "";
	}, function(data) { // 错误回调
		console.log('请求失败' + data);
		$timeout(function(){
    		dialogService.setContent("查询出错！").setShowDialog(true)
    	},200);
		//alert("查询出错");
		return;
	});
					
	// 提交表单
	$scope.superadditionQuestionBankSubmit=false;
	$scope.doSave = function() {
		$scope.superadditionQuestionBankForm.$submitted = true;
		if($scope.superadditionQuestionBankForm.$invalid)
	   	{
			$timeout(function(){
	    		dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true)
	    	},200);
			//alert("表单验证未通过,请按提示修改表单后重试！");
	   		return;
	   	}
		$scope.superadditionQuestionBankSubmit=true;
		$http({
			method : 'POST',
			url : '/enterpriseuniversity/services/questionBank/superadditionBank',
			data : $.param($scope.questionBank),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
//			$timeout(function(){
//	    		dialogService.setContent(data.result).setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
//	    			if(data.result=="追加成功"){
//	    				$scope.go('/exam/questionBankList','questionBankList', null);
//	    			}else{
//	    				$scope.go('/exam/questionBankList','questionBankList', null);
//	    			}
//	    		}
//	    	},200);
			if(data.result=="success"){
				dialogService.setContent("追加成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.superadditionQuestionBankSubmit=false;
					$scope.go('/exam/questionBankList','questionBankList', null);
				},2000);
			}else{
				dialogService.setContent(data.result).setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.superadditionQuestionBankSubmit=false;
				},2000);
			}
			$scope.superadditionQuestionBankSubmit=false;
		}).error(function() {
//			$timeout(function(){
//	    		dialogService.setContent("追加题库失败！").setShowDialog(true);
//	    	},200);
			dialogService.setContent(data.result).setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.superadditionQuestionBankSubmit=false;
			},2000);
			$scope.superadditionQuestionBankSubmit=false;
			//alert("追加题库失败！");
		});
	};
	// 追加题库返回
	$scope.doReturn = function() {
		$scope.go('/exam/questionBankList', 'questionBankList',{});
	}
}])
.controller('QuestionBankClassifyController',['$scope', '$http', '$timeout', 'dialogService', function($scope, $http, $timeout, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理 ', cmc:'cmc-ba', cmn: '题库分类'};
	$scope.url = "/enterpriseuniversity/services/questionBank/classificationList?pageNum=";
	$scope.paginationConf = {
		currentPage : 1,
		totalItems : 10,
		itemsPerPage : 20,
		pagesLength : 9,
		perPageOptions : [ 20, 50, 100, '全部' ],
		rememberPerPage : 'perPageItems',
		onChange : function() {
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			if ($scope.currNode) {
				$scope.$httpPrams = $scope.$httpPrams + "&questionClassificationId=" + $scope.currNode.code;
			}
			if ($scope.questionClassificationName != undefined && $scope.questionClassificationName != "") {
				$scope.$httpPrams = $scope.$httpPrams + "&questionClassificationName=" 
					+ $scope.questionClassificationName.replace(/\%/g,"%25").replace(/\#/g,"%23")
						.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
						.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
			}
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
				+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
			$http.get($scope.$httpUrl).success(
				function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			});
		}
	};
	// 搜索按钮
	$scope.doSearch = function() {
		$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	//回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.doSearch();
		}
    } 
	$scope.searchTreeData = function() {
		$http.get("/enterpriseuniversity/services/questionBank/tree")
		.success(
			function(data) {
				$scope.questionClassifyData = data.children;
			})
		.error(function() {
			$timeout(function(){
	    		dialogService.setContent("题库分类树数据查询错误！").setShowDialog(true);
	    	},200);
		});
	}
	$scope.searchTreeData();
	$scope.parentClassifyId = 0;
	// 点击节点查询
	$scope.searchByCondition = function(currNode) {
		// alert(currNode.name);
		$scope.currNode = currNode;
		$scope.parentClassifyId = currNode.code;
		$scope.questionClassificationName = "";
		$scope.doSearch();
	}
//					// 查看题库分类
//					$scope.modalDialog = {
//						'openQuestionModas' : false,
//					};
//
//					$scope.doExamine = function(item) {
//						$scope.questionBank = item;
//
//						var url = "/enterpriseuniversity/services/questionBank/findClassification?questionClassificationId="
//								+ item.questionClassificationId;
//						$http.get(url).success(function(response) {
//							$scope.questionBankDetai = response;
//							// alert($scope.questionBankDetai.questionClassificationName);
//						});
//
//						$scope.modalDialog.openQuestionModas = true;
//					}
//
//					$scope.openQuestionModas = function() {
//						$scope.modalDialog.openQuestionModas = false;
//					}
				// ////////////////////////////////////////////////

	// 编辑题库分类按钮
	$scope.doEdit = function(question) {
		// alert(question.questionClassificationId);
		$scope.go('/exam/editQuestionBankClassify','editQuestionBankClassify', {id : question.questionClassificationId});
	}
	// 添加题库分类按钮
	$scope.addQuestionBankClassify = function() {
		$scope.go('/exam/addQuestionBankClassify','addQuestionBankClassify', {/*id : $scope.parentClassifyId*/});
	}
	// 题库分类删除
	$scope.doDelete = function(item) {
		dialogService.setContent('确定要删除吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
		//if (window.confirm('确定要删除吗？')) {
			$http({
				url : "/enterpriseuniversity/services/questionBank/deleteClassification?questionClassificationId=" + item.questionClassificationId,
				method : "delete"
			}).success(function(response) {
				$timeout(function(){
					if(response.result=="success"){
	//					$timeout(function(){
	//			    		dialogService.setContent("删除成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
	//			    			$scope.currNode  = undefined;
	//							$scope.questionClassificationName = "";
	//							$scope.doSearch();
	//							$scope.searchTreeData();
	//			    		}
	//			    	},200);
						dialogService.setContent("删除题库分类成功！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
							$scope.currNode  = undefined;
							$scope.questionClassificationName = "";
							$scope.doSearch();
							$scope.searchTreeData();
						},2000);
					}else if(response.result=="error"){
	//					$timeout(function(){
	//			    		dialogService.setContent("删除失败！").setShowDialog(true);
	//			    	},200);
						dialogService.setContent("删除题库分类失败！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}else if(response.result=="hasChildren"){
	//					$timeout(function(){
	//			    		dialogService.setContent("含有子分类，无法删除！").setShowDialog(true);
	//			    	},200);
						dialogService.setContent("当前题库分类含有子分类，无法删除！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}else if(response.result=="protected"){
	//					$timeout(function(){
	//			    		dialogService.setContent("含有题库，无法删除！").setShowDialog(true);
	//			    	},200);
						dialogService.setContent("当前题库分类含有题库，无法删除！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}
				},200);
			});
		} 
	}
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.questionClassificationId = item.questionClassificationId; 
    }
}])
//新增题库分类
.controller('AddQuestionBankClassifyController',['$scope', '$http', '$state', '$stateParams','dialogService','$timeout', function($scope, $http, $state, $stateParams,dialogService,$timeout) {
	$scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理 ', cmc:'cmc-ba', cmn: '题库分类', gcmn : '添加题库分类'};
	if ($stateParams.id) {
		$http.get("/enterpriseuniversity/services/questionBank/findClassification?questionClassificationId="+ $stateParams.id)
		.success(function(data) {
			$scope.questionBankClassify = data;
		});
	} else {
		// 未传参数
	}
	$scope.questionBankClassify = $scope.formData = {admins:"",parentClassificationId: 0 , parentClassificationName:""};
	// 提交表单
	$scope.addQuestionBankClassifySubmit=false;
	$scope.doSave = function() {
		$scope.questionBankClassifyForm.$submitted = true;
    	if($scope.questionBankClassifyForm.$invalid)
	   	{
    		 $timeout(function(){
    	    		dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true);
    	    	},200);
    		//alert("表单验证未通过,请按提示修改表单后重试！");
	   		return;
	   	}
    	$scope.addQuestionBankClassifySubmit=true;
		$http({
			method : 'POST',
			url : '/enterpriseuniversity/services/questionBank/addClassification',
			data : $.param($scope.questionBankClassify),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		})
		.success(function(data) {
			if (data.result == "success") {
//				$timeout(function(){
//		    		dialogService.setContent("新增题库分类成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/exam/questionBankClassifyList','questionBankClassifyList',null);}
//		    	},200);
				dialogService.setContent("新增题库分类成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addQuestionBankClassifySubmit=false;
					$scope.go('/exam/questionBankClassifyList','questionBankClassifyList',null);
				},2000);
			} else if(data.result == "error"){
//				$timeout(function(){
//		    		dialogService.setContent("新增题库分类失败！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/exam/questionBankClassifyList','questionBankClassifyList',null);}
//		    	},200);
				dialogService.setContent("新增题库分类失败！").setShowSureButton(false).setShowDialog(true); 
				$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.addQuestionBankClassifySubmit=false;
				},2000);
			}
			$scope.addQuestionBankClassifySubmit=false;
		}).error(function() {
			dialogService.setContent("新增题库分类失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.addQuestionBankClassifySubmit=false;
			},2000);
			$scope.addQuestionBankClassifySubmit=false;
		});
	};
	// 添加题库分类返回
	$scope.doReturn = function() {
		$scope.go('/exam/questionBankClassifyList', 'questionBankClassifyList',{});
	}
}])
//编辑
.controller('EditQuestionBankClassifyController',['$scope', '$http', '$state', '$stateParams', '$q', '$timeout', 'dialogService',function($scope, $http, $state, $stateParams, $q, $timeout, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-b', pmn: '试卷管理 ', cmc:'cmc-ba', cmn: '题库分类', gcmn : '编辑题库分类'};
	$scope.deferred = $q.defer();
	if ($stateParams.id) {
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/questionBank/findClassification?questionClassificationId=" + $stateParams.id)
			.success(function(data) {
				$scope.deferred.resolve(data);
			}).error(function(data){
	    		$scope.deferred.reject(data); 
	    	});
	} else {
		// 未传参数
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
	}
	$scope.deferred.promise.then(function(data) { // 成功回调
		$scope.$emit('isLoading', false);
		$scope.questionClassification = $scope.formData = data;
	}, function(data) { // 错误回调
		$scope.$emit('isLoading', false);
        dialogService.setContent("页面数据初始化异常").setShowDialog(true);
	});
	// 提交表单
	$scope.editQuestionBankClassifySubmit=false;
	$scope.doSave = function() {
		$scope.questionBankClassifyForm.$submitted = true;
    	if($scope.questionBankClassifyForm.$invalid)
	   	{
    		$timeout(function(){
        		dialogService.setContent("表单验证未通过,请按提示修改表单后重试！").setShowDialog(true);
        	},200);
    		return;
	   	}
    	$scope.editQuestionBankClassifySubmit=true;
		$http({
			method : 'PUT',
			url : '/enterpriseuniversity/services/questionBank/updateClassification',
			data : $.param($scope.questionClassification),
			headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
		}).success(function(data) {
			if (data.result == "success") {
//				$timeout(function(){
//		    		dialogService.setContent("编辑题库分类成功！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/exam/questionBankClassifyList', 'questionBankClassifyList',{});}
//		    	},200);
				dialogService.setContent("编辑题库分类成功！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.editQuestionBankClassifySubmit=false;
					$scope.go('/exam/questionBankClassifyList', 'questionBankClassifyList',{});
				},2000);
			}else if(data.result == "error"){
//				$timeout(function(){
//		    		dialogService.setContent("编辑题库分类失败！").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){$scope.go('/exam/questionBankClassifyList', 'questionBankClassifyList',{});}
//		    	},200);
				dialogService.setContent("编辑题库分类失败！").setShowSureButton(false).setShowDialog(true); 
				$timeout(function(){
					dialogService.sureButten_click(); 
					$scope.editQuestionBankClassifySubmit=false;
				},2000);
			}
			$scope.editQuestionBankClassifySubmit=false;
		}).error(function() {
//			$timeout(function(){
//	    		dialogService.setContent("编辑题库分类失败！").setShowDialog(true);
//	    	},200);
			dialogService.setContent("编辑题库分类失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.editQuestionBankClassifySubmit=false;
			},2000);
			$scope.editQuestionBankClassifySubmit=false;
		});
	};
	//返回
	$scope.doReturn = function() {
		$scope.go('/exam/questionBankClassifyList', 'questionBankClassifyList',{});
	}
}])
//添加题库选择题库分类模态框
.controller('SelectQuestionClassify', ['$scope', '$http', '$state', '$timeout', 'dialogService', 'dialogStatus', function($scope, $http, $state, $timeout, dialogService, dialogStatus) {
	// 显示题库分类树
	$scope.searchTreeData = function() {
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/questionBank/tree")
			.success(function(data) {
				$scope.$emit('isLoading', false);
				$scope.questionClassifyData = data.children;
			})
			.error(function() {
				$scope.$emit('isLoading', false);
	    		dialogService.setContent("题库分类树数据查询错误！").setShowDialog(true);
			});
	}
	$scope.itemArr = {
        choosedItemArr: []
	}
	$scope.modalDialog = {'openSelectQuestionClassify' : false};
	// 点选
	$scope.searchByCondition = function(currNode) {
		$scope.$parent.$parent.itemArr.choosedItemArr = [];
		$scope.$parent.$parent.itemArr.choosedItemArr.push({
			questionClassificationId:currNode.code,
			questionClassificationName:currNode.name
		})
		$scope.$parent.$parent.questionBank.questionClassificationId = currNode.code;
		$scope.$parent.$parent.questionBank.questionClassificationName = currNode.name;
		$scope.modalDialog.openSelectQuestionClassify = false;
		dialogStatus.setHasShowedDialog(false);
	}
	$scope.$parent.$parent.openQuestionClassifyPaperModal = function() {
		$scope.searchTreeData();
		$scope.modalDialog.openSelectQuestionClassify = true;
		dialogStatus.setHasShowedDialog(true);
	}
	//关闭
	$scope.closeQuestionClassifyModal = function() {
		$scope.modalDialog.openSelectQuestionClassify = false;
		dialogStatus.setHasShowedDialog(false);
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
                	  validity = (value=="" )? true : false;
                      ngModel.$setValidity("requiredItem", !validity);
                  });
                  return !validity ? value : "";
              };
              ngModel.$formatters.push(customValidator);
              ngModel.$parsers.push(customValidator);
          }
      };
  }])
.controller('RadioQuestionClassifyModalController', ['$scope', '$http','$timeout','dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
    //课程分类树数据
    $scope.searchTreeData = function(){
    	$scope.$emit('isLoading', true); 
        $http.get("/enterpriseuniversity/services/questionBank/tree").success(function (data) {
        	$scope.$emit('isLoading', false);
        	$scope.questionClassifyData = data.children;
            if($scope.isBuildOldChoosedItem&&$scope.itemArr.initCurrChoosedItemArrStatus){
            	$scope.itemArr.initCurrChoosedItemArrStatus();
            }
            if($scope.itemArr.buildOldChoosedItemArr&&!$scope.isBuildOldChoosedItem){
            	$scope.itemArr.buildOldChoosedItemArr();
            }
        }).error(function () {
        	$scope.$emit('isLoading', false);
    		dialogService.setContent("题库分类树数据查询错误！").setShowDialog(true);
        });
    }
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.itemArr.oldChooseItemArr = [];
    		if($scope.formData.parentClassificationId){
    			$scope.itemArr.oldChooseItemArr.push($scope.formData.parentClassificationId);
    		}
        	$scope.searchTreeData();
        }, function(data) {
        	 
        }); 
    }
    $scope.isBuildOldChoosedItem = false;
    $scope.openModalDialog = false;
    $scope.itemArr = {
		oldChooseItemArr:[],
        choosedItemArr: [],
        currChoosedItemArr: [],
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.selected) 
            {
            	$scope.itemArr.currChoosedItemArr = [];
            } 
            else
            {
            	$scope.itemArr.currChoosedItemArr = [];
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	$scope.itemArr.nodeLoopStatus($scope.questionClassifyData,$scope.itemArr.currChoosedItemArr[i].code,'P',[]);
            }
            $scope.$emit('isLoading', false);
        },
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.questionClassifyData) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var questionClassificationId = $scope.itemArr.oldChooseItemArr[i];
	            	$scope.itemArr.nodeLoop($scope.questionClassifyData,questionClassificationId); 
	            }
            }
        	$scope.$emit('isLoading', false);
        },
        nodeLoop:function(LoopNode,questionClassificationId){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==questionClassificationId)
        		{
        			if($scope.itemArr.choosedItemArr.length<1)
    				{
    					$scope.itemArr.choosedItemArr.push(LoopNode[i]);
    					continue;
    				}
    				var isContained = false;
    				for(var j in $scope.itemArr.choosedItemArr)
    				{
    					if($scope.itemArr.choosedItemArr[j].questionClassificationId==LoopNode[i].code)
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
            		$scope.itemArr.nodeLoop(LoopNode[i].children,questionClassificationId);
            	}
        	}
        } ,
        nodeLoopStatus:function(LoopNode,questionClassificationId,loopType,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==questionClassificationId)
        		{
        			LoopNode[i].selected = true;
        			parentNodeArr.push(LoopNode[i].fathercode); 
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.questionClassifyData,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopStatus(LoopNode[i].children,questionClassificationId,'C',parentNodeArr);
            	}
        	}
        	if(loopType=='P'){ 
        		console.log('parentNodeArr', parentNodeArr);
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
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.questionClassifyData,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopForParentNodeFatherCode(LoopNode[i].children,fathercode,parentNodeArr);//>C 子节点 LoopNode[i].children
            	}
        	}
        } 
    };
    //生成classifyIds数组
    $scope.buildParentClassifyNameAndId = function(){
    	if($scope.itemArr.choosedItemArr.length>0){
    		$scope.$parent.$parent.formData.parentClassificationId= $scope.itemArr.choosedItemArr[0].code;
    		$scope.$parent.$parent.formData.parentClassificationName= $scope.itemArr.choosedItemArr[0].name;
    	}
    	else
    	{
    		$scope.$parent.$parent.formData.parentClassificationId= 0;
    		$scope.$parent.$parent.formData.parentClassificationName= "";
    	}
    }
    $scope.$parent.$parent.doOpenRadioCourseClassifyModal = function () {
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.isBuildOldChoosedItem = true;
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
        //重新查询模态框中的数据
        $scope.searchTreeData(); 
    }
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildParentClassifyNameAndId();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
//选择题库模态框
.controller('ChooseQuestionBankController',['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus){
	$scope.url = "/enterpriseuniversity/services/questionBank/chooseQuestionBank?pageNum=";
	$scope.isInitUpdatePageData = false;
	$scope.openModalDialog = false;
	$scope.paginationConf = {
		currentPage : 1,
		totalItems : 10,
		itemsPerPage : 20,
		pagesLength : 9,
		perPageOptions : [ 20, 50, '全部' ],
		rememberPerPage : 'perPageItems',
		onChange : function() {
			if(!($scope.isInitUpdatePageData||$scope.openModalDialog)){
        		$scope.page = {};
        		return;
        	}
			$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
			if ($scope.questionBankName != undefined && $scope.questionBankName != "") {
				$scope.$httpPrams = $scope.$httpPrams + "&questionBankName=" 
					+ $scope.questionBankName.replace(/\%/g,"%25").replace(/\#/g,"%23")
						.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
						.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
			}
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
				+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
				+ $scope.$httpPrams;
			$scope.$emit('isLoading', true);
			$http.get($scope.$httpUrl).success(
				function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
					//生成旧的选中项  只执行一次
	                if($scope.itemArr.buildOldChoosedItemArr&&!$scope.isBuildOldChoosedItems){
	                	$scope.itemArr.buildOldChoosedItemArr();
	                }
				}
			).error(function(){
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
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.questionBankId = item.questionBankId; 
    }
    //是否初始化旧的选项标记
    $scope.isBuildOldChoosedItems = false;
    //容器
    $scope.itemArr = {
		choosedItemArr : [],
		//存放编辑页面传递过来的questionBankId 用于buildOldChoosedItemArr函数
		oldChooseItemArr: [],
		//删除
		removeItem : function(item) {
			$scope.itemArr.choosedItemArr.splice($scope.itemArr.choosedItemArr.indexOf(item), 1);
			$scope.calculateCountAndTotalScore();
		},
		//编辑页面数据格式化
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.page.data) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var oldItem = $scope.itemArr.oldChooseItemArr[i];
	            	if(!oldItem.isLooped)
	            	{//是否循环过了
	            		for (var j in $scope.page.data) 
		            	{
	            			if($scope.page.data[j].questionBankId==oldItem.questionBankId)
	            			{
	            				$scope.createQuestionBankItem($scope.page.data[j],oldItem);
			            	}
			            }
	            		//标记已循环过
	            		oldItem.isLooped = true;
	            	}
	            }
	            //释放
            	$scope.page = {};
            }
        	$scope.$emit('isLoading', false);
        }
	};
    
	$scope.openQuestionBankModal = function() {
		$scope.openModalDialog = true;
		dialogStatus.setHasShowedDialog(true);
    	$scope.isBuildOldChoosedItems = true;
    	$scope.questionBankName = "";
    	$scope.currHighLightRow = {};
    	$scope.search();
	}
	//编辑页面执行     数据传递 
	if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
        	$scope.formData = data; 
        	$scope.itemArr.oldChooseItemArr = [];
        	for(var i = 0; i < $scope.formData.questionBankId.length; i++){
        		$scope.itemArr.oldChooseItemArr.push(
	        		{
	        			questionBankId:$scope.formData.questionBankId[i],
	        			questionType : $scope.formData.questionType[i],
	        			count :  $scope.formData.count[i],
	        			score : $scope.formData.score[i]
	        		}		
        		); 
        	}
        	$scope.isInitUpdatePageData = true;
	    	$scope.search(); 
        }, function(data) {
            
        }); 
    }
	
	//变更题型时初始化  试题数量和试题分数
	$scope.initData = function(item){
		item.questionCount = 0;
		item.score = 0;
		$scope.calculateCountAndTotalScore();
	} 
	//试算
	$scope.tryCalculateCountAndTotalScore = function(item){
		//检查题型数量是否在范围内
		$scope.formatQuestionCount(item);
		//格式化分数
		$scope.formatQuestionScore(item);
		//计算
		$scope.calculateCountAndTotalScore();
	}
	//格式化数据
	$scope.formatQuestionCount = function(item){
		item.questionCount = new String(item.questionCount).replace(/\b(0+)/gi,"");
		item.questionCount = Number(item.questionCount);
		if(isNaN(item.questionCount)||item.questionCount <=0){
			item.questionCount = 0 ;
			item.score = 0 ;
			return ;
		}
		if(item.questionCount > parseInt(item.questionType.count)){
			item.questionCount = parseInt(item.questionType.count) ;
		} 
	}
	//格式化数据
	$scope.formatQuestionScore = function(item){
		item.score = new String(item.score).replace(/\b(0+)/gi,"");
		item.score = Number(item.score);
		if(isNaN(item.score)||item.score <=0 || !$scope.isInteger(item.score)){
			item.score = 0 ;
			return ;
		}
		if(item.score > 1000){
			item.score = 1000 ;
		} 
	}
	$scope.isInteger = function (obj) {
		 return Math.floor(obj) === obj;
	}

	//计算
	$scope.calculateCountAndTotalScore = function(){
		var totalQuestionCount = 0;
		var totalQuestionScore = 0;
		for(var i in $scope.itemArr.choosedItemArr){
			if($scope.itemArr.choosedItemArr[i].questionCount==0){
				continue;
			}
			totalQuestionCount = totalQuestionCount + $scope.itemArr.choosedItemArr[i].questionCount ;
			if($scope.itemArr.choosedItemArr[i].score==0){
				continue;
			}
			totalQuestionScore = totalQuestionScore + $scope.itemArr.choosedItemArr[i].score * $scope.itemArr.choosedItemArr[i].questionCount ;
		}
		$scope.$parent.$parent.examPaper.testQuestionCount = totalQuestionCount;
		$scope.$parent.$parent.examPaper.fullMark = totalQuestionScore;
		$scope.$parent.$parent.examPaper.passMark = "";
		$scope.bulidFormData();
	}
	//生成表单数据
	$scope.bulidFormData = function(){
		$scope.$parent.$parent.examPaper.questionBankId = [];
		$scope.$parent.$parent.examPaper.questionBankName = [];
		$scope.$parent.$parent.examPaper.questionType = [];
		$scope.$parent.$parent.examPaper.count = [];
		$scope.$parent.$parent.examPaper.score = [];
		for(var i in $scope.itemArr.choosedItemArr){
			$scope.$parent.$parent.examPaper.questionBankId.push($scope.itemArr.choosedItemArr[i].questionBankId);
			$scope.$parent.$parent.examPaper.questionBankName.push($scope.itemArr.choosedItemArr[i].questionBankName);
			$scope.$parent.$parent.examPaper.questionType.push($scope.itemArr.choosedItemArr[i].questionType.value);
			$scope.$parent.$parent.examPaper.count.push($scope.itemArr.choosedItemArr[i].questionCount);
			$scope.$parent.$parent.examPaper.score.push($scope.itemArr.choosedItemArr[i].score);
		}
	}
	//唯一性条件
	$scope.questionBankItemKey = 0;
	//创建questionBankItem
	$scope.createQuestionBankItem = function(item,oldItem){
		var questionBankItem = {
				//约束唯一性
				questionBankItemKey : $scope.questionBankItemKey,
				questionBankId : item.questionBankId,
				questionBankName : item.questionBankName,
				questionTypes : [
					                {
										name : "单选题",
										value : "1",
										count : item.singleCount 
									},
									{
										name : "多选题",
										value : "2",
										count : item.mcqCount 
									},
									{
										name : "填空题",
										value : "3",
										count : item.gapCount 
									},
									{
										name : "简答题",
										value : "4",
										count : item.essayCount 
									},
									{
										name : "判断题",
										value : "5",
										count : item.tOrFCount 
									}	
				                ] 
			};
			questionBankItem.questionType = oldItem==null?questionBankItem.questionTypes[0]:questionBankItem.questionTypes[oldItem.questionType-1];
			questionBankItem.questionCount = oldItem==null?0:oldItem.count;
			questionBankItem.score = oldItem==null?0:oldItem.score;
			$scope.itemArr.choosedItemArr.push(questionBankItem);
			$scope.questionBankItemKey = $scope.questionBankItemKey + 1;
	}
	$scope.doClose = function() {
		$scope.openModalDialog = false;
		dialogStatus.setHasShowedDialog(false);
	}
	$scope.doSure = function(item) {
		$scope.createQuestionBankItem(item,null);
		$scope.bulidFormData();
		$scope.openModalDialog = false;
		dialogStatus.setHasShowedDialog(false);
	}
}])
//上传题目图片控制器
.controller('ExamImgController', ['$scope', 'FileUploader', '$timeout', 'dialogService','$http', function ($scope, FileUploader, $timeout, dialogService,$http) {
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
            	dialogService.setContent("要上传新的题目图片，请先删除旧的题目图片").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("超出上传文件数量限制").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|mp4|x-mpeg|mpeg|'.indexOf(type) == -1)
            {
                dialogService.setContent("上传文件格式不符合要求").setShowDialog(true);
                return '|jpg|png|jpeg|bmp|gif|mp4|x-mpeg|mpeg|'.indexOf(type) !== -1;
            }
            if( item.size/1024 > 2048*15)
            	
            {
            	dialogService.setContent("上传文件大小不符合要求(大于2M)").setShowDialog(true);
            	return item.size/1024 <= 2048*15;
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
    		if($scope.formData.examImg&&$scope.formDataexamImg!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.examImg);
    		}
        }, function(data) {}); 
    } 
    //修改课程封面图片
    $scope.buildExamImg = function(imgUrl){
		$scope.$parent.formData.examImg = imgUrl;
    }
    //预览
    $scope.openPreview = false;
    $scope.preview = function(){
    	if($scope.$parent.formData.examImg&&$scope.$parent.formData.examImg!="")
    	{
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.$parent.formData.examImg;
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
        $scope.buildExamImg("");
    }
    //删除 
    $scope.removeItem = function (item) {
    	if(item.fileUrl!=null){
    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
    	}
        item.remove();
        document.getElementById("examImgFileInput").value=[];
        $scope.buildExamImg(""); 
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
        	$scope.buildExamImg(response.url);//接收返回的文件地址
        	$scope.$parent.questionBank.tompicNameList = fileItem.file.name.slice(0,fileItem.file.name.lastIndexOf('.')) ;
        }
    };
}]);

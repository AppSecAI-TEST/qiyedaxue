/**
 * Created by Mr.xiang on 2016/3/26.
 */
/**
 * 课程分类模块
 */
angular.module('ele.courseClassify', ['ele.admin'])
.controller('CourseClassifyController', ['$scope', '$http', '$timeout', 'dialogService', function ($scope, $http, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-a', cmn: '课程分类'};
    //课程分类树数据
    $scope.searchTreeData = function(){
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/courseClassify/tree").success(function (data) {
        	$scope.$emit('isLoading', false);
            $scope.courseClassifyData = data.children;
        }).error(function () {
        	$scope.$emit('isLoading', false);
            dialogService.setContent("课程分类树数据查询异常").setShowDialog(true);
        });
    }
    $scope.searchTreeData();
    //http请求url
    $scope.url = "/enterpriseuniversity/services/courseClassify/findPage?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 9,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
        	$scope.$emit('isLoading', true);
            if ($scope.currNode) {
            	$scope.$httpPrams = $scope.$httpPrams + "&classifyId=" + $scope.currNode.code;
            } 
            if ($scope.classifyName != undefined && $scope.classifyName != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&classifyName=" 
            		+ $scope.classifyName.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            });
        }
    };
    //点击节点查询
    $scope.searchByCondition = function (currNode) {
        $scope.currNode = currNode;
        $scope.paginationConf.currentPage = 1;
        $scope.paginationConf.itemsPerPage = 20;
        $scope.classifyName = "";
        $scope.paginationConf.onChange();
    }
    //查询按钮
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
    //新增
    $scope.doAdd = function(){
    	$scope.go('/course/addCourseClassify','addCourseClassify',null);
    }
    //编辑
    $scope.doEdit = function(classify){
    	$scope.go('/course/editCourseClassify','editCourseClassify',{id:classify.classifyId});
    }
    //删除课程分类
    $scope.doDelete = function(classify) {
		dialogService.setContent("确定要删除课程分类?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
    		$http({
				url : "/enterpriseuniversity/services/courseClassify/delete?classifyId="+ classify.classifyId,
				method : "delete"
			})
			.success(function(response) {
				$timeout(function(){
					if(response.result=="success"){
						dialogService.setContent("删除课程分类成功！").setShowSureButton(false).setShowDialog(true);
	    	    		$timeout(function(){
	    					dialogService.sureButten_click(); 
	    					$scope.currNode  = undefined;
							$scope.classifyName = "";
							$scope.search();
							$scope.searchTreeData();
	    				},2000);
					}else if(response.result=="error"){
						dialogService.setContent("删除课程分类失败！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}else if(response.result=="hasChildren"){
						dialogService.setContent("课程分类下存在下级课程分类，无法删除！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}else if(response.result=="protected"){
						dialogService.setContent("课程分类下存在课程，无法删除！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}
				},200);
			})
			.error(function(){
				dialogService.setContent("网络异常,删除课程分类失败！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
				},2000);
			});
    	};
	}
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(classify){
    	$scope.currHighLightRow.classifyId = classify.classifyId; 
    }
}])
//updated v1
.controller('AddCourseClassifyController',['$http', '$scope', '$timeout', 'dialogService', function( $http, $scope, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-a', cmn: '课程分类', gcmn : '添加课程分类'};
    $scope.courseClassify = $scope.formData = {parentClassifyId: 0 , parentClassifyName:""};
    $scope.addCourseClassifySubmit=false;
    $scope.doSave = function() {
    	$scope.courseClassifyForm.$submitted = true;
    	if($scope.courseClassifyForm.$invalid)
   	 	{
    		//dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
   		 	return;
   	 	}
    	$scope.addCourseClassifySubmit=true;
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/courseClassify/add',
    		data : $.param($scope.courseClassify),  
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
    	})
    	.success(function(data) {
    		$scope.addCourseClassifySubmit=false;
    		dialogService.setContent("新增课程分类成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.go('/course/courseClassifyList', 'courseClassifyList', null);
			},2000);
    	})
    	.error(function(){
    		$scope.addCourseClassifySubmit=false;
    		dialogService.setContent("新增课程分类失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
			},2000);
    	}); 
	};
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/courseClassifyList', 'courseClassifyList', null);
    }
}])
//编辑课程分类控制器
.controller('EditCourseClassifyController',['$scope', '$http', '$q', '$stateParams', '$timeout', 'dialogService', function ($scope, $http, $q, $stateParams, $timeout, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-a', cmn: '课程分类', gcmn : '编辑课程分类'};
    $scope.deferred = $q.defer();
    if ($stateParams.id) {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/courseClassify/find?classifyId=" + $stateParams.id)
            .success(function (data) {
            	$scope.deferred.resolve(data);
            })
            .error(function(data){
        		$scope.deferred.reject(data); 
        	});
    } else {
    	dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.$emit('isLoading', false);
    	$scope.courseClassify = $scope.formData = data; 
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
    	dialogService.setContent("页面数据初始化异常").setShowDialog(true);
    });  
    $scope.editCourseClassifySubmit=false;
    $scope.doSave = function() {
    	$scope.courseClassifyForm.$submitted = true;
		 if($scope.courseClassifyForm.$invalid)
	   	 {
			 //dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
	   		 return;
	   	 }
		 $scope.editCourseClassifySubmit=true;
		 $http({
			 method : 'PUT',
			 url  : '/enterpriseuniversity/services/courseClassify/edit',
			 data : $.param($scope.courseClassify),  
			 headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
		 })
		 .success(function(data) {
			 $scope.editCourseClassifySubmit=false;
			 dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
			 $timeout(function(){
				 dialogService.sureButten_click(); 
				 $scope.go('/course/courseClassifyList', 'courseClassifyList', null);
			 },2000);
		 })
		 .error(function(){
			 $scope.editCourseClassifySubmit=false;
			 dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			 $timeout(function(){
				 dialogService.sureButten_click(); 
			 },2000);
		 }); 
	};
    //返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/courseClassifyList', 'courseClassifyList', null);
    }
}])
.directive('requireParentclassify',[ function() {
	return {
		require : "ngModel",
		link : function(scope, element, attr, ngModel) {
			var customValidator = function(value) {
				var validity = null;
				scope.$watch(function() {
								return value;
							},
							function() {
								validity = value ? (parseInt(value.length) < 1 ? true : false) : true;
								ngModel.$setValidity("requireParentclassify",!validity);
							});
				return !validity ? value : undefined;
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])
.controller('CourseClassifyModalController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
    //课程分类树数据
    $scope.searchTreeData = function(){
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/courseClassify/tree").success(function (data) {
        	$scope.$emit('isLoading', false);
            $scope.courseClassifyData = data.children;
            if($scope.isBuildOldChoosedItem&&$scope.itemArr.initCurrChoosedItemArrStatus){
            	$scope.itemArr.initCurrChoosedItemArrStatus();
            }
            if($scope.itemArr.buildOldChoosedItemArr&&!$scope.isBuildOldChoosedItem){
            	$scope.itemArr.buildOldChoosedItemArr();
            }
        }).error(function () {
        	$scope.$emit('isLoading', false);
        	dialogService.setContent("课程分类树数据查询异常").setShowDialog(true);
        });
    }
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
        	$scope.itemArr.oldChooseItemArr = $scope.formData.classifyIds;
        	$scope.searchTreeData();
        }, function(data) {}); 
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
            	for(var i in $scope.itemArr.currChoosedItemArr)
            	{
                	if($scope.itemArr.currChoosedItemArr[i].code==item.code)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                	}
                }
            } 
            else
            {
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        deleteItem : function (item) {
            item.selected = undefined;
            for(var i in $scope.itemArr.choosedItemArr){
            	if($scope.itemArr.choosedItemArr[i].code==item.code){
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            	}
            }
            $scope.buildClassifyIds();
        },
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	$scope.itemArr.nodeLoopStatus($scope.courseClassifyData,$scope.itemArr.currChoosedItemArr[i].code,'P',[]);//> P 数据全量   []存selected节点的fathercode
            }
            $scope.$emit('isLoading', false);
        },
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.courseClassifyData) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var classifyId = $scope.itemArr.oldChooseItemArr[i];
	            	$scope.itemArr.nodeLoop($scope.courseClassifyData,classifyId); 
	            }
            }
        	$scope.$emit('isLoading', false);
        },
        nodeLoop:function(LoopNode,classifyId){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==classifyId)
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
            		$scope.itemArr.nodeLoop(LoopNode[i].children,classifyId);
            	}
        	}
        } ,
        nodeLoopStatus:function(LoopNode,classifyId,loopType,parentNodeArr){//>
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==classifyId)
        		{
        			LoopNode[i].selected = true; 
        			parentNodeArr.push(LoopNode[i].fathercode); 
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.courseClassifyData,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopStatus(LoopNode[i].children,classifyId,'C',parentNodeArr);//>C 子节点 LoopNode[i].children
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
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.courseClassifyData,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopForParentNodeFatherCode(LoopNode[i].children,fathercode,parentNodeArr);//>C 子节点 LoopNode[i].children
            	}
        	}
        }
    };
    //生成classifyIds数组
    $scope.buildClassifyIds = function(){
    	var classifyIds = [];
        for (var i in $scope.itemArr.choosedItemArr) 
        {
        	classifyIds.push($scope.itemArr.choosedItemArr[i].code);
        }
    	$scope.$parent.$parent.formData.classifyIds= classifyIds;
    }
    $scope.$parent.$parent.doOpenCourseClassifyModal = function () {
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
        $scope.buildClassifyIds();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
//添加课程分类选择上级课程分类模态框函数
.controller('RadioCourseClassifyModalController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
    //课程分类树数据
    $scope.searchTreeData = function(){
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/courseClassify/tree").success(function (data) {
        	$scope.$emit('isLoading', false);
            $scope.courseClassifyData = data.children;
            if($scope.isBuildOldChoosedItem&&$scope.itemArr.initCurrChoosedItemArrStatus){
            	$scope.itemArr.initCurrChoosedItemArrStatus();
            }
            if($scope.itemArr.buildOldChoosedItemArr&&!$scope.isBuildOldChoosedItem){
            	$scope.itemArr.buildOldChoosedItemArr();
            }
        }).error(function () {
        	$scope.$emit('isLoading', false);
        	dialogService.setContent("课程分类树数据查询异常").setShowDialog(true);
        });
    }
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.itemArr.oldChooseItemArr = [];
    		if($scope.formData.parentClassifyId){
    			$scope.itemArr.oldChooseItemArr.push($scope.formData.parentClassifyId);
    		}
        	$scope.searchTreeData();
        }, function(data) {
        	dialogService.setContent("页面数据初始化异常").setShowDialog(true);
            return;
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
            	$scope.itemArr.nodeLoopStatus($scope.courseClassifyData,$scope.itemArr.currChoosedItemArr[i].code,'P',[]);
            }
            $scope.$emit('isLoading', false);
        },
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.courseClassifyData) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var classifyId = $scope.itemArr.oldChooseItemArr[i];
	            	$scope.itemArr.nodeLoop($scope.courseClassifyData,classifyId); 
	            }
            }
        	$scope.$emit('isLoading', false);
        },
        nodeLoop:function(LoopNode,classifyId){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==classifyId)
        		{
        			if($scope.itemArr.choosedItemArr.length<1)
    				{
    					$scope.itemArr.choosedItemArr.push(LoopNode[i]);
    					continue;
    				}
    				var isContained = false;
    				for(var j in $scope.itemArr.choosedItemArr)
    				{
    					if($scope.itemArr.choosedItemArr[j].classifyId==LoopNode[i].code)
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
            		$scope.itemArr.nodeLoop(LoopNode[i].children,classifyId);
            	}
        	}
        } ,
        nodeLoopStatus:function(LoopNode,classifyId,loopType,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==classifyId)
        		{
        			LoopNode[i].selected = true; 
        			parentNodeArr.push(LoopNode[i].fathercode);
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.courseClassifyData,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopStatus(LoopNode[i].children,classifyId,'C',parentNodeArr);
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
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.courseClassifyData,LoopNode[i].fathercode,parentNodeArr);
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
    		$scope.$parent.$parent.formData.parentClassifyId= $scope.itemArr.choosedItemArr[0].code;
    		$scope.$parent.$parent.formData.parentClassifyName= $scope.itemArr.choosedItemArr[0].name;
    	}
    	else
    	{
    		$scope.$parent.$parent.formData.parentClassifyId= 0;
    		$scope.$parent.$parent.formData.parentClassifyName= "";
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
}]);


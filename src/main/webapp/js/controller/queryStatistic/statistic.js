/*
 * 统计模块
 * */
angular.module('ele.statistic', [ 'ele.admin' ])
.controller('StatisticChartController', ['$scope', '$http', '$q', 'dialogService', function( $scope, $http, $q, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ee', cmn: '整体统计'};
	/*Date.prototype.Format = function (fmt) {
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}*/
	function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	}
	/*$scope.beginTime = new Date().Format("yyyy-MM-dd") + " 00:00:01";
	$scope.endTime = new Date().Format("yyyy-MM-dd") + " 23:59:59";*/
	$scope.beginTime = getDate(-1);
	$scope.endTime = getDate(0);
	$scope.searchData = function(){
		var params = '';
		if($scope.beginTime!=''&&$scope.endTime != ''){
			params = '?beginTime='+ $scope.beginTime + '&endTime=' + $scope.endTime ;
		}else if($scope.beginTime!=''&&$scope.endTime == ''){
			params = '?beginTime='+ $scope.beginTime ;
		}else if($scope.beginTime ==''&&$scope.endTime != ''){
			params = '?endTime='+ $scope.endTime ;
		}
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/wholeStatistics/getWholeStatistics"+params).success(function (response) {
			if(response.result=="error"){
				$scope.$emit('isLoading', false);
				dialogService.setContent("统计数据查询异常！").setShowDialog(true);
			}else{
				$scope.chartData = response;
				$scope.buildFusionCharts();
				$scope.$emit('isLoading', false);
			}
	    }).error(function (response) {
	    	$scope.$emit('isLoading', false); 
	    	dialogService.setContent("统计数据查询异常！").setShowDialog(true);
	    });
	}
	$scope.reSetSearchOption = function(){
		$scope.beginTime = '';
		$scope.endTime = '';
		document.getElementById('startTime').value="";
		document.getElementById('endTime').value="";
	} 
	pickedStartTimeFunc = function(){
		$scope.$emit('startTime', $dp.cal.getDateStr());
 	}
	clearStartTimeFunc = function(){
		$scope.$emit('startTime', '');
 	}
	$scope.$on('startTime', function(e,value) {
		$scope.beginTime = value;
    });
	pickedEndTimeFunc = function(){
		$scope.$emit('endTime', $dp.cal.getDateStr());
	}
	clearEndTimeFunc = function(){
		$scope.$emit('endTime', '');
	}
	$scope.$on('endTime', function(e,value) {
		$scope.endTime = value;
    });
	 
	FusionCharts.ready(function(){
		/*$scope.$emit('isLoading', true); 
		$http.get("ele_tom/services/wholeStatistics/getWholeStatistics").success(function (response) {
			$scope.chartData = response;
			$scope.buildFusionCharts();
			$scope.$emit('isLoading', false); 
		}).error(function (response) {
			$scope.$emit('isLoading', false); 
	    	dialogService.setContent("统计数据查询异常！").setShowDialog(true);
	    });*/
		$scope.searchData();
	})
	$scope.buildFusionCharts = function(){
		$scope.newChart = new FusionCharts({
	        type: 'column2d',
	        renderAt: 'chart-container',
	        width: '100%',
	        height: '425',
	        dataFormat: 'json',
	        dataSource: {
	            "chart": {
	                "caption": "整体统计图表",
	                "subcaption": "活跃率" + ($scope.chartData.activeRate==undefined ? "0%":$scope.chartData.activeRate) + "（活跃用户数/累计用户数）",
	                "xaxisname": "统计项",
	                "yaxisname": "数量 ",
	                "formatNumberScale": "0",
	                "placeValuesInside" :"0",
	                "rotateValues": "0",
			        "showborder": "1",
	                "valueFontColor": "#0C0C0C",
	                "adjustDiv": "1",
	                "numdivlines": "4",
	                "yAxisMinValue": "0",     
	                "yAxisValueDecimals":"0",
	                "forceYAxisValueDecimals": "1", 
	                "baseFontSize":"14", 
	                "theme" : "fint" 
	            },
	            "data": [
	                {
	                    "label": "活跃用户数",
	                    "value": $scope.chartData.activeUsers,
	                    "color": "#0075c2"
	                }, 
	                {
	                    "label": "累计用户数",
	                    "value": $scope.chartData.totalUsers,
	                    //"value": 198,
	                    "color": "#22e0cc"
	                }, 
	                {
	                    "label": "累计课程数",
	                    "value": $scope.chartData.totalCourses,
	                    "color": "#71952d"
	                }, 
	                {
	                    "label": "累计播放次数",
	                    "value": $scope.chartData.totalViews,
	                    "color": "#d5841b"
	                     
	                }, 
	                {
	                    "label": "累计考试数",
	                    "value": $scope.chartData.totalExams,
	                    "color": "#af72e7"
	                }, 
	                {
	                    "label": "签到数",
	                    "value": $scope.chartData.totalSignIn,
	                    "color": "#ef7497"
	                }
	            ]
	        }
	    }).render();
	}
}])
.controller('CourseStatisticController', ['$scope', '$http', 'dialogService', function( $scope, $http, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ea', cmn: '课程统计'};
	$scope.method = [
      	      {name: "线上", value: "Y"},
      	      {name: "线下", value: "N"}
	       	 ];
	// 显示课程统计列表 
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 13,
			perPageOptions : [ 20, 50, 100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.url = "/enterpriseuniversity/services/attendanceStatistics/attendanceStatisticsList?pageNum=";
				if ($scope.courseName != undefined && $scope.courseName != "") {
					$scope.$httpPrams = $scope.$httpPrams + "&courseName=" + $scope.courseName
						.replace(/\%/g,"%25").replace(/\#/g,"%23")
						.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
						.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
				}
				if ($scope.courseOnline != undefined) 
	            {
	            	$scope.$httpPrams = $scope.$httpPrams + "&courseOnline=" + $scope.courseOnline
	            }
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("课程统计数据查询异常！").setShowDialog(true);
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
	//查询下拉菜单改变后查询列表数据
    $scope.changeSeclectOption = function(){
    	$scope.search();
    }
    
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
        $scope.currHighLightRow.courseId = item.courseId; 
    	$scope.currHighLightRow.activityId = item.activityId; 
    } 
}])
//查看课程学习明细
.controller('viewSignUpStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/seeAttendanceStatistics?pageNum=";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if($scope.activityId==undefined||$scope.courseId==undefined||$scope.courseName==undefined){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams = "&activityId="+$scope.activityId+"&courseId="+$scope.courseId;
				}
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)
					+ $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("课程统计详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewSignUpStatistic = function(item){
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	$scope.activityId = item.activityId;
	 	$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;
	 	$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
}])
.controller('ExamStatisticController', ['$scope', '$http', 'dialogService', function ($scope, $http, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-eb', cmn: '考试统计'};
    $scope.url = "/enterpriseuniversity/services/examStatistics/queryExamStatistics?pageNum=";
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
            if ($scope.examName != undefined && $scope.examName != "") {
            	$scope.$httpPrams = $scope.$httpPrams+"&examName=" + $scope.examName
            		.replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("考试统计数据查询异常！").setShowDialog(true);
            });
        }
    };
    //搜索
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
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.examId = item.examId; 
    }
}])
//考试详情
.controller('viewExamStatisticDetailController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/examStatistics/queryExamEmployeeStatistics?pageNum=";
	$scope.openModalDialog=false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				if(!$scope.openModalDialog||$scope.examId==undefined){
	        		$scope.page = {};
	        		return;
	        	}
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$httpPrams = $scope.$httpPrams + "&examId="+$scope.examId;
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("考试统计详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
	$scope.$parent.$parent.viewExamStatisticDetail = function(item){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.examId = item.examId;
    	$scope.examName = item.examName;
    	$scope.search(); 
    }; 
    //模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])
.controller('ActivityStatisticController', ['$scope', '$http', 'dialogService', function( $scope, $http, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ec', cmn: '活动统计'};
	// 显示活动统计列表
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 13,
			perPageOptions : [ 20, 50, 100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.url = "/enterpriseuniversity/services/activityStatistics/activityStatisticsList?pageNum=";
				if ($scope.activityName != undefined && $scope.activityName != "") {
					$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
						.replace(/\%/g,"%25").replace(/\#/g,"%23")
						.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
						.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
				}
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("活动统计数据查询异常！").setShowDialog(true);
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
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(activity){
        $scope.currHighLightRow.activityId = activity.activityId; 
    } 
}])
//查看培训活动统计明细
.controller('viewActivityStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activityStatistics/seeActivityStatistics?pageNum=";
	$scope.openModalDialog=false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				if(!$scope.openModalDialog||$scope.activityId==undefined){
	        		$scope.page = {};
	        		return;
	        	}
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$httpPrams = $scope.$httpPrams + "&activityId="+$scope.activityId;
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)
					+ $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("活动统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
	$scope.$parent.$parent.viewActivityStatistic = function(item){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.activityId = item.activityId;
    	$scope.activityName = item.activityName;
    	$scope.search(); 
    };
    //模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])
.controller('FeeStatisticController', ['$scope', '$http', 'dialogService', function( $scope, $http, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ed', cmn: '费用统计'};
	// 显示活动统计列表
	$scope.url = "/enterpriseuniversity/services/feeStatistics/findActityCostList?pageNum=";
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
				if ($scope.activityName != undefined && $scope.activityName != "") {
					$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
						.replace(/\%/g,"%25").replace(/\#/g,"%23")
						.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
						.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
				}
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)
					+ $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("费用统计数据查询异常！").setShowDialog(true);
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
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.actitvtyId = item.actitvtyId; 
    }
}])
//查看费用统计详情
.controller('viewFeeDetailModalController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/feeStatistics/findActityCostDetailList?pageNum=";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if(!$scope.openModalDialog||$scope.activity==undefined||$scope.activity.actitvtyId==undefined){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams = "&activityId="+$scope.activity.actitvtyId;
				}
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)
					+ $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("费用统计详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item,$index){
    	$scope.currHighLightRow.item = item; 
    	$scope.currHighLightRow.index = $index; 
    }
	$scope.$parent.$parent.viewFeeDetail = function(item){
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	$scope.activity = item;
	 	$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])
//查看公开课统计列表
.controller('OpenCourseStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ef', cmn: '公开课统计'};
	// 显示课程统计列表 
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50, 100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.url = "/enterpriseuniversity/services/attendanceStatistics/openCourseStatisticsList?pageNum=";
				if ($scope.courseName != undefined && $scope.courseName != "") {
					$scope.$httpPrams = $scope.$httpPrams + "&courseName=" + $scope.courseName
						.replace(/\%/g,"%25").replace(/\#/g,"%23")
						.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
						.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
				}
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("公开课统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	// 搜索按钮
	$scope.search = function() {
		$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
	};
	
	//查询下拉菜单改变后查询列表数据
    $scope.changeSeclectOption = function(){
    	$scope.search();
    }
    
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
        $scope.currHighLightRow.courseId = item.courseId; 
    	$scope.currHighLightRow.activityId = item.activityId; 
    } 
}])
//查看公开课学习明细
.controller('OpenCourseLearnStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/openCourseLearnStatistics?pageNum=";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if($scope.courseId==undefined||$scope.courseName==undefined){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams ="&courseId="+$scope.courseId;
				}
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)
					+ $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("公开课学习详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewCourseLearnStatistic = function(item){
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;
	 	$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
}])
;
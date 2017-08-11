angular.module('eleApp',
    ['angularFileUpload',
        'ui.router',
        'ng.ueditor',
        'angular-drag',
        'menu',
        'ele.course', 
        'ele.courseClassify',
        'ele.lecture',
        'ele.gradeDimension',
        'ele.examPaper',
        'ele.questionBank',
        'ele.exam',
        'ele.examPaper',
        'ele.taskPackage',
        'ele.activity',
        //'ele.certificate',//证书
        'ele.commodity',
        'ele.statistic',//查询统计
        'ele.orgGroup',
        'ele.role',
        'ele.admin',
        'ele.message',
        'ele.news',
        'ele.banner',
        'pagination',
        'angularTreeview',
        'angularRadiotreeview',
        'angularCheckboxtreeview'])
					    .config(
					    ['$stateProvider', '$urlRouterProvider', '$locationProvider',
					        function ($stateProvider, $urlRouterProvider, $locationProvider) {
					            /*系统页面视图跳转路由配置*/
					            /*
					             * 课程管理  页面路由配置
					             * */
					            //课程库  页面跳转路由
					            $stateProvider.state('courseList', {
					                url: '/courseList',
					                templateUrl: '/enterpriseuniversity/views/course/courseList.html',
					            }).state('addCourse', {
					                url: '/addCourse',
					                templateUrl: '/enterpriseuniversity/views/course/addCourse.html',
					            }).state('editCourse', {
					                url: '/editCourse/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editCourse.html',
					            }) 
					            // 课程分类页面跳转路由
					            .state('courseClassifyList', {
					                url: '/courseClassifyList',
					                templateUrl: '/enterpriseuniversity/views/course/courseClassifyList.html',
					            }).state('addCourseClassify', {
					                url: '/addCourseClassify',
					                templateUrl: '/enterpriseuniversity/views/course/addCourseClassify.html',
					            }).state('editCourseClassify', {
					                url: '/editCourseClassify/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editCourseClassify.html',
					            })
					            //讲师管理
					            .state('lectureList', {
					                url: '/lectureList',
					                templateUrl: '/enterpriseuniversity/views/course/lectureList.html',
					            }).state('addLecture', {
					                url: '/addLecture',
					                templateUrl: '/enterpriseuniversity/views/course/addLecture.html',
					            }).state('editLecture', {
					                url: '/editLecture/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editLecture.html',
					            }).state('lectureCourseList', {
					                url: '/lectureCourseList/:id',
					                templateUrl: '/enterpriseuniversity/views/course/lecturerCourseList.html',
					            })
					            //评价管理
					            .state('gradeDimensionList', {
					                url: '/gradeDimensionList',
					                templateUrl: '/enterpriseuniversity/views/course/gradeDimensionList.html',
					            }).state('addCourseGradeDimension', {
					                url: '/addCourseGradeDimension',
					                templateUrl: '/enterpriseuniversity/views/course/addCourseGradeDimension.html',
					            }).state('editCourseGradeDimension', {
					                url: '/editCourseGradeDimension/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editCourseGradeDimension.html',
					            }).state('addLectureGradeDimension', {
					                url: '/addLectureGradeDimension',
					                templateUrl: '/enterpriseuniversity/views/course/addLectureGradeDimension.html',
					            }).state('editLectureGradeDimension', {
					                url: '/editLectureGradeDimension/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editLectureGradeDimension.html',
					            })
					
					            /*
					             * 试卷管理  页面跳转路由配置
					             * */
					            //试卷库
					            .state('examPaperList', {
					                url: '/examPaperList',
					                templateUrl: '/enterpriseuniversity/views/exam/examPaperList.html',
					            }).state('addExamPaper', {
					                url: '/addExamPaper',
					                templateUrl: '/enterpriseuniversity/views/exam/addExamPaper.html',
					            }).state('editExamPaper', {
					                url: '/editExamPaper/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/editExamPaper.html',
					            })
					            //题库
					            .state('questionBankList', {
					                url: '/questionBankList',
					                templateUrl: '/enterpriseuniversity/views/exam/questionBankList.html',
					            }).state('addQuestionBank', {
					                url: '/addQuestionBank',
					                templateUrl: '/enterpriseuniversity/views/exam/addQuestionBank.html',
					            }).state('editQuestionBank', {
					                url: '/editQuestionBank/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/editQuestionBank.html',
					            }).state('superadditionQuestionBank', {
					                url: '/superadditionQuestionBank/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/superadditionQuestionBank.html',
					            })
					            //题库分类
					            .state('questionBankClassifyList', {
					                url: '/questionBankClassifyList',
					                templateUrl: '/enterpriseuniversity/views/exam/questionBankClassifyList.html',
					            }).state('addQuestionBankClassify', {
					                url: '/addQuestionBankClassify',
					                templateUrl: '/enterpriseuniversity/views/exam/addQuestionBankClassify.html',
					            }).state('editQuestionBankClassify', {
					                url: '/editQuestionBankClassify/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/editQuestionBankClassify.html',
					            })
					            
					            //考试管理
					            .state('examList', {
					                url: '/examList',
					                templateUrl: '/enterpriseuniversity/views/exam/examList.html',
					            }).state('addExam', {
					                url: '/addExam',
					                templateUrl: '/enterpriseuniversity/views/exam/addExam.html',
					            }).state('editExam', {
					                url: '/editExam/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/editExam.html',
					            })
					            /*
					             * 活动管理    页面跳转路由配置
					             * */
					            //任务包
					            .state('taskPackageList', {
					                url: '/taskPackageList',
					                templateUrl: '/enterpriseuniversity/views/activity/taskPackageList.html',
					            }).state('addTaskPackage', {
					                url: '/addTaskPackage',
					                templateUrl: '/enterpriseuniversity/views/activity/addTaskPackage.html',
					            }).state('editTaskPackage', {
					                url: '/editTaskPackage/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/editTaskPackage.html',
					            }).state('detailTaskPackage', {
					                url: '/detailTaskPackage/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/detailTaskPackage.html',
					            }) 
					            
					            //培训活动
					            .state('activityList', {
					                url: '/activityList',
					                templateUrl: '/enterpriseuniversity/views/activity/activityList.html',
					            }).state('addActivity', {
					                url: '/addActivity',
					                templateUrl: '/enterpriseuniversity/views/activity/addActivity.html',
					            }).state('editActivity', {
					                url: '/editActivity/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/editActivity.html',
					            }).state('detailActivity', {
					                url: '/detailActivity/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/detailActivity.html',
					            }).state('copyActivity', {
					                url: '/copyActivity/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/copyActivity.html',
					            })
					            //证书列表
					            .state('certificateList', {
					                url: '/certificateList',
					                templateUrl: '/enterpriseuniversity/views/activity/certificateList.html',
					            })
					            //制作证书
					            .state('addCertificate', {
					                url: '/addCertificate',
					                templateUrl: '/enterpriseuniversity/views/activity/addCertificate.html',
					            })
					            //e币商城
					            .state('commodityList', {
					                url: '/commodityList',
					                templateUrl: '/enterpriseuniversity/views/EBStore/commodityList.html',
					            }).state('addCommodity', {
					                url: '/addCommodity',
					                templateUrl: '/enterpriseuniversity/views/EBStore/addCommodity.html',
					            }).state('editCommodity', {
					                url: '/editCommodity/:id',
					                templateUrl: '/enterpriseuniversity/views/EBStore/editCommodity.html',
					            })
					            .state('commodityExchangeList', {
					                url: '/commodityExchangeList',
					                templateUrl: '/enterpriseuniversity/views/EBStore/commodityExchangeList.html',
					            }).state('addCommodityExchange', {
					                url: '/addCommodityExchange',
					                templateUrl: '/enterpriseuniversity/views/EBStore/addCommodityExchange.html',
					            }).state('editCommodityExchange', {
					                url: '/editCommodityExchange/:id',
					                templateUrl: '/enterpriseuniversity/views/EBStore/editCommodityExchange.html',
					            })
					            //查询统计
					            .state('courseStatisticList', {
					                url: '/courseStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/courseStatisticList.html',
					            })
					            .state('openCourseStatisticList', {
					                url: '/openCourseStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/openCourseStatisticList.html',
					            })
					            //查询统计
					            .state('examStatisticList', {
					                url: '/examStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/examStatisticList.html',
					            })
					            //查询统计
					            .state('activityStatisticList', {
					                url: '/activityStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/activityStatisticList.html',
					            })
					            //查询统计
					            .state('feeStatisticList', {
					                url: '/feeStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/feeStatisticList.html',
					            })
					            .state('statisticChart', {
					                url: '/statisticChart',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/statisticChart.html',
					            })
					            /*
					             * 系统管理  页面跳转路由配置
					             * */
					            //组织架构
					            .state('empList', {
					                url: '/empList',
					                templateUrl: '/enterpriseuniversity/views/system/empList.html',
					            }).state('empDetails', {
					                url: '/empDetails/:id',
					                templateUrl: '/enterpriseuniversity/views/system/empDetails.html',
					            }).state('addLabelClass', {
					                url: '/addLabelClass',
					                templateUrl: '/enterpriseuniversity/views/system/addLabelClass.html',
					            }).state('editLabelClass', {
					            	url: '/editLabelClass/',
					            	templateUrl: '/enterpriseuniversity/views/system/editLabelClass.html',
					            }).state('addLabel', {
					                url: '/addLabel',
					                templateUrl: '/enterpriseuniversity/views/system/addLabel.html',
					            }).state('editLabel', {
					            	url: '/editLabel/',
					            	templateUrl: '/enterpriseuniversity/views/system/editLabel.html',
					            }).state('studentsRecord', {
					                url: '/studentsRecord/:id',
					                templateUrl: '/enterpriseuniversity/views/system/studentsRecord.html',
					            }).state('addOrg', {
					                url: '/addOrg',
					                templateUrl: '/enterpriseuniversity/views/system/addOrg.html',
					            }).state('addDept', {
					                url: '/addDept',
					                templateUrl: '/enterpriseuniversity/views/system/addDept.html',
					            }).state('addEmp', {
					                url: '/addEmp',
					                templateUrl: '/enterpriseuniversity/views/system/addEmp.html',
					            }).state('editEmp', {
					            	url: '/editEmp/:code',
					            	templateUrl: '/enterpriseuniversity/views/system/editEmp.html',
					            }).state('editOrg', {
					                url: '/editOrg/:code',
					                templateUrl: '/enterpriseuniversity/views/system/editOrg.html',
					            }).state('editDept', {
					                url: '/editDept/:code',
					                templateUrl: '/enterpriseuniversity/views/system/editDept.html',
					            })
					            //角色管理
					            .state('roleList', {
					                url: '/roleList',
					                templateUrl: '/enterpriseuniversity/views/system/roleList.html',
					            }).state('addRole', {
					                url: '/addRole',
					                templateUrl: '/enterpriseuniversity/views/system/addRole.html',
					            }).state('editRole', {
					                url: '/editRole/:id',
					                templateUrl: '/enterpriseuniversity/views/system/editRole.html',
					            })
					            //权限管理
					            .state('adminList', {
					                url: '/adminList',
					                templateUrl: '/enterpriseuniversity/views/system/adminList.html',
					            }).state('addAdmin', {
					                url: '/addAdmin',
					                templateUrl: '/enterpriseuniversity/views/system/addAdmin.html',
					            }).state('editAdmin', {
					                url: '/editAdmin/:id',
					                templateUrl: '/enterpriseuniversity/views/system/editAdmin.html',
					            })
					            //消息管理
					            .state('messageList', {
					                url: '/messageList',
					                templateUrl: '/enterpriseuniversity/views/system/messageList.html',
					            }).state('addMessage', {
					                url: '/addMessage',
					                templateUrl: '/enterpriseuniversity/views/system/addMessage.html',
					            }).state('viewMessage', {
					                url: '/viewMessage/:id',
					                templateUrl: '/enterpriseuniversity/views/system/viewMessage.html',
					            }).state('messageSet', {
					                url: '/messageSet',
					                templateUrl: '/enterpriseuniversity/views/system/messageSet.html',
					            }).state('messageDetails', {
					                url: '/messageDetails',
					                templateUrl: '/enterpriseuniversity/views/system/messageDetails.html',
					            })
					            //轮播图
					            .state('bannerList', {
					                url: '/bannerList',
					                templateUrl: '/enterpriseuniversity/views/system/bannerList.html',
					            }).state('addBanner', {
					                url: '/addBanner',
					                templateUrl: '/enterpriseuniversity/views/system/addBanner.html',
					            }).state('editBanner', {
					                url: '/editBanner/:id',
					                templateUrl: '/enterpriseuniversity/views/system/editBanner.html',
					            })
					            //资讯管理
					            .state('newsList', {
					                url: '/newsList',
					                templateUrl: '/enterpriseuniversity/views/system/newsList.html',
					            }).state('addNews', {
					                url: '/addNews',
					                templateUrl: '/enterpriseuniversity/views/system/addNews.html',
					            }).state('editNews', {
					                url: '/editNews/:id',
					                templateUrl: '/enterpriseuniversity/views/system/editNews.html',
					            }) 
					            //欢迎页
					            .state('welcomePage',{
					            	url:'/welcomePage',
					            	templateUrl: '/enterpriseuniversity/views/welcomePage.html',
					            })
					            //无权限提示页面
					            .state('401',{
					            	url:'/noAuthorityPage',
					            	templateUrl: '/enterpriseuniversity/views/401.html',
					            });
					        }])
					        .factory('dialogService',function(){
						    	var dialog = {
					    			content:"",//提示内容
					    			showCancelButton : false,
					    			showSureButton: true,
					    			showDialog : false ,
					    			hasNextProcess : false,
					    			doNextProcess : function (){},
					    			setHasNextProcess : function(status){
					    				dialog.hasNextProcess = status;
					    				return dialog;
					    			},
					    			setShowDialog : function(status){
					    				dialog.showDialog = status;
					    				return dialog;
					    			},
					    			setShowCancelButton : function(status){
					    				dialog.showCancelButton = status;
					    				return dialog;
					    			},
					    			setShowSureButton : function(status){
					    				dialog.showSureButton = status;
					    				return dialog;
					    			},
					    			sureButten_click : function(){
					    				dialog.setShowDialog(false);
					    				dialog.setShowCancelButton(false);
					    				dialog.setShowSureButton(true);
					    				dialog.removeContent();
					    				if(dialog.hasNextProcess){
					    					dialog.doNextProcess();
					    					dialog.setHasNextProcess(false);
					    					dialog.doNextProcess = function (){};
					    				} 
					    			},
					    			cancelButten_click:function(){
					    				dialog.setShowDialog(false);
					    				dialog.setShowCancelButton(false);
					    				dialog.removeContent();
					    				dialog.setHasNextProcess(false);
					    				dialog.doNextProcess = function (){};
					    			},	
					    			setContent : function(msg){
					    				dialog.content = msg;
					    				return dialog;
					    			},
					    			removeContent : function(){
					    				dialog.content = "";
					    			}
						    	}; 
						    	return dialog;
					        })
						    .factory('loadDialog',['$timeout',function($timeout){
						    	var loadDialog = {
					    			showDialog : false ,
					    			loadingItemCount:0,
					    			loadingItems :[],//多个加载项
					    			timeout: undefined,
					    			init:function(){
					    				this.timeout = undefined;
					    				this.loadingItemCount = 0;
					    			},
					    			loading:function(){
					    				loadDialog.showDialog = true;
					    				return loadDialog;
					    			},
					    			loaded:function(){
					    				if(!loadDialog.showDialog){
					    					return loadDialog;
					    				}
					    				loadDialog.showDialog = false;
					    				return loadDialog;
					    			},
					    			addLoadingItem : function(){
					    				loadDialog.loadingItemCount ++;
					    				loadDialog.checkLoadingItems();
					    			},
					    			deleteLoadingItem : function(){
					    				loadDialog.loadingItemCount --;
					    				loadDialog.checkLoadingItems();
					    			},
					    			checkLoadingItems:function(){
					    				if(loadDialog.loadingItemCount <=0){
					    					 
					    					this.timeout = $timeout(function(){
					    						loadDialog.loaded();
					    						loadDialog.init();
					    					},500);
					    					
					    				}else{
					    					this.timeout && $timeout.cancel(this.timeout) && (this.timeout = undefined); 
			    							loadDialog.loading();
			    						}
					    				 
					    			}
						    	};
						    	return loadDialog;
						    }])
						    .factory('saveDialog',function(){
						    	var saveDialog = {
					    			showDialog : false ,
					    			saving:function(){
					    				saveDialog.showDialog = true;
					    			},
					    			saved:function(){
					    				saveDialog.showDialog = false;
					    			}
						    	};
						    	return saveDialog;
						    })
						    //控制body元素是否出滚动条
						    .factory('dialogStatus',function(){
						    	var dialogStatus = {
					    			hasShowedDialog : false ,
					    			setHasShowedDialog:function(status){
					    				dialogStatus.hasShowedDialog = status;
					    			} 
					    			//初始化模态框滚动条位置
					    			//另一种解决方案： 在模态框容器上使用ng-if指令（需要在查询条件属性上加$parent.）  采用后者！
					    			/*initDialogScrollPosition : function(elementId){
					    				document.getElementById(elementId).scrollTop = 0;
					    				document.getElementById(elementId).scrollLeft = 0;
					    			}*/
						    	};
						    	return dialogStatus;
						    })
						    .factory('sessionService',['$http', 'dialogService', function($http, dialogService){
						    	var sessionService = {
					    			 user:null,
					    			 logoutUrl:"",
					    			 logined:false,
					    			 login:function(){//获取登录人信息
					    				 $http.get("/enterpriseuniversity/services/backend/sys/getUser").success(function(data){
					    					 sessionService.setUser(data);
					    					 sessionService.logined = true;
					    				 }).error(function(data){
					    					 //...登录超时
					    				 })
					    			 },
					    			 logout:function(){
					    				 $http.get("/enterpriseuniversity/services/backend/sys/logout").success(function(data){
					    					 //退出sso 
					    					 window.location.reload();
//					    					 window.location.href = "/enterpriseuniversity/services/backend/sys/loginpage";
					    					 /*if(data!="error"){
					    						 sessionService.logoutUrl = data;
					    						 console.log("logoutUrl",sessionService.logoutUrl); 
					    					 }*/
					    				 }).error(function(data){
					    					 //...
					    				 })
					    			 },
					    			 pcLearningSysUrl : "http://dev.chinamobo.com/ele_pcweb/views/index/index.html?token=",
					    			 toPcLearnSys : function(){
					    				 if(this.user && this.user.user && this.user.user.token){
					    					 window.location.href = this.pcLearningSysUrl + this.user.user.token;
					    				 }else {
					    					 console.log("error : user don't have a token"); 
					    				 }
					    			 },
					    			 setUser:function(userData){
					    				 sessionService.user = userData;
					    				 console.log("sessionService.user" , sessionService.user);
					    			 },
					    			 clearUser:function(){
					    				 sessionService.user = null;
					    				 sessionService.logined = false;
					    			 }
						    	}; 
						    	return sessionService;
						    }])
					        //监听窗口高度、宽度 用于视图自适应
					        .directive('windowResize', ['$window', 'dialogService', 'loadDialog', 'dialogStatus', function ($window, dialogService, loadDialog, dialogStatus) {
							    return function (scope, element) {
							        var w = angular.element($window);
							        scope.getWindowHeightAndWidth = function () {
							            return {
							                'h': w.innerHeight(),
							                'w': w.innerWidth()
							            };
							        };
							        scope.$watch(scope.getWindowHeightAndWidth, function (newValue, oldValue) {
							            scope.windowHeight = newValue.h;
							            scope.windowWidth = newValue.w;
							            //设置对话框margin-top
							            scope.setModalDialogStyle = function () {
							                return {
							                    'margin-top': (newValue.h<=200?0:(newValue.h/2 - 200)<0?0:(newValue.h/2 - 200)) + 'px',
							                    'margin-bottom':'0px'
							                };
							            };
							            scope.setNomalModalDialogStyle = function () {
							                return {
							                    'margin-top': (newValue.h<=200?60:(newValue.h/2 - 100)<0?0:(newValue.h/2 - 100)) + 'px',
							                    'margin-bottom':'0px'
							                };
							            };
							            //当模态框显示时   设置body元素overflow = hidden
							            scope.setBodyStyle = function () {
							            	//确认框、加载、摸态框显示时 body元素overflow = hidden
							            	if(dialogService.showDialog||loadDialog.showDialog||dialogStatus.hasShowedDialog){
							            		return {
								                    'overflow': 'hidden'
								                };
							            	}else{
							            		return {};
							            	}
							            };
							            //设置评分模态框样式？
							            scope.setFullScreenModalDialogStyle = function () {
							            	if(newValue.w<1280){
							            		return {'margin': '0px auto'};
							            	}else{
							            		if(newValue.h<635){
							            			return {
									                    'margin': '0 auto',
									                    'width': (newValue.w-30)+'px'
									                };
							            		}else{
							            			return {
									                    'margin': '10px auto',
									                    'width': (newValue.w-30)+'px'
									                };
							            		}
							            	}
							            };
							            // ？
							            scope.setStyle_01 = function () {
							            	if(newValue.h<635){
							            		return {
							            			'overflow-x': 'hidden',
							            			'max-height': '510px'
						            			}
							            	}else{
							            		return {
							            			'overflow-x': 'hidden',
							            			'max-height': (newValue.h-150)+'px',
						            			}
							            	}
							            }
							            
							            scope.setStyle_02 = function () {
							            	return {
							            		'margin': '30px auto'
					            			}
							            }
							            
							            //emp dialog 左、中、右三块高度设置
							            scope.setStyle_03 = function () {
							            	if(newValue.h>=610){
							            		return {
							            			'height': newValue.h-185+'px'
						            			}
							            	}else{
							            		return {}
							            	}
							            }
							            
							            //设置统计图位置
							            scope.setStyle_04 = function () {
							            	if(newValue.h>=635){
							            		return {'margin': ((newValue.h-610)<0?0:(newValue.h-610))/2+'px auto'}
							            	}else{
							            		return {}
							            	}
							            }
							            
							        }, true);
							        
							        w.bind('resize', function () {
							            scope.$apply();
							        });
							    }
				        	}]);
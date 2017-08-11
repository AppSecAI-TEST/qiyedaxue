angular.module('loginApp',[])
.controller('loginFormController', ['$scope', '$http', '$timeout','$window', function($scope, $http, $timeout, $window) {
	$scope.formData = {};
	$scope.isLearnSite = true;
   	$scope.loginSuccessed = false;
   	$scope.isProcessLogin = false;
	$scope.showLoginForm = true;//默认显示登录表单
	$scope.errorMessage = {};//错误提示信息
	$scope.successMessage = {};//成功提示信息
	
	/* 切换新用户、密码找回表单 */
	$scope.openNewUserLoginForm = function(type){
		if(type==='newUser'){
			$scope.formTitle = '新用户登录';
		}else{
			$scope.formTitle = '修改密码';
		}
		$scope.showLoginForm = false;
		$scope.formData = {};
	}
	/*返回登录*/
	$scope.backLogin = function(){
		$scope.showLoginForm = true;
		$scope.formData = {};
	}
	/* 重置input值 */
	$scope.resetUserName = function(){
	   	$scope.formData.userName = undefined;
	   	//$scope.initStatus();
	   	$scope.errorMessage.invalidUserName = undefined;
	   	$scope.errorMessage.invalidPassword = undefined;
	   	//$scope.successMessage = {};
   	}
   	$scope.resetPassword = function(){
	   	$scope.formData.password = undefined;
	   	//$scope.initStatus();
	   	$scope.errorMessage.invalidPassword = undefined;
	   	//$scope.successMessage = {};
   	}
   	$scope.resetPhoneNumber = function(){
	   	$scope.formData.phone = undefined;
	   	$scope.errorMessage.invalidPhone = undefined;
	   //	$scope.successMessage = {};
	   	//$scope.initStatus();
   	}
   	$scope.checkPhone = function () {
   		if(!new RegExp(/^1[3|4|5|7|8]\d{9}$/).test($scope.formData.phone)){
   			$scope.errorMessage.invalidPhone = "输入手机号不合法";
   		}else{
   			$scope.errorMessage.invalidPhone = undefined;
   		}
   	}
   	$scope.checkPassword = function(){
   		if(!new RegExp(/^\w{6,15}$/).test($scope.formData.password)){
			$scope.errorMessage.invalidPassword = "请输入6-15位字符密码";
   		} else{
   			$scope.errorMessage.invalidPassword = undefined;
   		}
   	}
   	/* 初始化表单状态 */
   	$scope.initStatus = function(){
   		$scope.loginForm.$submitted = false;
   		//$scope.errorMessage = {};
   		$scope.successMessage = {};
   	}
   	/* 切换登录窗口 */
	$scope.toggleSite = function(mark){
		$scope.formData = {};
		$scope.errorMessage = {};
		if(mark==='Y'){
	 		$scope.isLearnSite = true; 
		}else{
	 		$scope.isLearnSite = false; 
		}
	}
   	/* 获取手机验证码 */
	$scope.sendIdentifyCode = function(){
		$scope.errorMessage = {};
		$scope.successMessage = {};
		$scope.formData.captcha = undefined;
		if(!$scope.formData.phone){
			$scope.errorMessage.invalidPhone = "请输入手机号";
			return;
		} 
		if(!(new RegExp(/^1[3|4|5|7|8]\d{9}$/).test($scope.formData.phone))){
			$scope.errorMessage.invalidPhone = "输入手机号不合法";
			return;
   		}
		$http({
 			method : 'POST',
		 	url  : '/enterpriseuniversity/services/userFirstLog/sendCaptchaByPhone',
		 	data : JSON.stringify({"phone":$scope.formData.phone}), 
		 	headers : { 'Content-Type': 'application/json' }  
	 	}).success(function(response){
	 		if(response.captcha!=undefined){
	 			$scope.formData.callbackCaptcha = response.captcha;
	 			$scope.successMessage.hasSendedCaptcha = "手机验证码已发送";
	 		}else{
	 			$scope.errorMessage.invalidPhone = response.result;
	 		}
	 	}).error(function(){
	 		$scope.errorMessage.invalidCaptcha = "手机验证码发送异常";
	 	});
	}
   	/* 完成密码修改 */
   	$scope.submitNewPassword = function(){
   		$scope.resetPasswordForm.$submitted = true;
   		$scope.errorMessage = {};
		$scope.successMessage = {};
   		if(!$scope.formData.phone){
			$scope.errorMessage.invalidPhone = "请输入手机号";
			return;
		} 
   		if(!(new RegExp(/^1[3|4|5|7|8]\d{9}$/).test($scope.formData.phone))){
			$scope.errorMessage.invalidPhone = "输入手机号不合法";
			return;
   		}
   		if(!$scope.formData.callbackCaptcha){
			$scope.errorMessage.invalidCaptcha = "请发送验证码";
			return;
		}
   		if(!$scope.formData.captcha){
			$scope.errorMessage.invalidCaptcha = "请输入验证码";
			return;
		} 
		if(!$scope.formData.password){
			$scope.errorMessage.invalidPassword = "请输入新密码";
			return;
		} 
   		/* (?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15} 必须包含字母的正则*/
		if(!(new RegExp(/^\w{6,15}$/).test($scope.formData.password))){
			$scope.errorMessage.invalidPassword = "请输入6-15位字符密码";
			return;
   		}  
   		//验证码校验
   		if($scope.formData.callbackCaptcha&&$scope.formData.callbackCaptcha==$scope.formData.captcha){
   			/* 加密 */
	   		var password = hex_md5($scope.formData.password);
	   		
   			$http({
   	 			method : 'POST',
   			 	url  : '/enterpriseuniversity/services/userFirstLog/updatePassword',
   			 	data : JSON.stringify({"password" : password , "phone" : $scope.formData.phone }), 
   			 	headers : { 'Content-Type': 'application/json' }  
   		 	}).success(function(response){
   		 		if(response.result=="success"){
   		 			alert("修改密码成功"); 
   		 			window.location.href="/enterpriseuniversity/services/backend/sys/loginpage";
   		 		}else{
   		 			alert("修改密码失败");  
   		 		}
   		 	}).error(function(){
   		 		alert("修改密码异常");
   		 	});
   		}else{
   			$scope.errorMessage.invalidCaptcha = "验证码错误";
   		}
   	}
  
    /* 登录 */
    $scope.doLogin = function(){
		$scope.loginForm.$submitted = true;
		$scope.errorMessage = {};
		$scope.successMessage = {};
	 	if($scope.formData.userName==undefined){
	 		$scope.errorMessage.invalidUserName = "请输入用户名";
	 		return;
	 	}
	 	if($scope.formData.password==undefined){
	 		$scope.errorMessage.invalidPassword = "请输入密码";
	 		return;
	 	}
	 	$scope.processMessage = "登录中...";
		$scope.isProcessLogin = true;
		
		//加密密码
		var password = hex_md5($scope.formData.password);
		console.log("password",password);
        var formData = $scope.isLearnSite?{userName:$scope.formData.userName,passWord:password}:{userName:$scope.formData.userName,password:password};
        
   	 	$http({
	 		method : 'POST',
		 	url  : $scope.isLearnSite ? '/enterpriseuniversity/services/backend/sys/apilogin' : '/enterpriseuniversity/services/backend/sys/login',
		 	data : $scope.isLearnSite ? JSON.stringify(formData) : $.param(formData),  
		 	headers : { 'Content-Type': $scope.isLearnSite ? 'application/json' : 'application/x-www-form-urlencoded' }  
	 	}).success(function(response){
			if($scope.isLearnSite){
				if(response.status=='Y'){
					$scope.processMessage = "登录成功...";
					$timeout(function(){
						$scope.isProcessLogin = false;
//						window.location.href = "http://pc.moxueyuan.net/views/index/index.html?token="+response.token;
						window.location.href = "http://dev.chinamobo.com/ele_pcweb/views/index/index.html?token="+response.token + "&userId=" + response.userId;
						//window.location.href = "http://local.pc.university.com/views/index/index.html?token="+response.token;
					},500);
				}else {
					$scope.isProcessLogin = false;
					if(response.errorType=="incorrectusername"){
						$scope.errorMessage.invalidUserName = "用户名不存在";
					}else {
						$scope.errorMessage.invalidPassword = "登录密码错误";
					}
				}
			}else{
				$scope.isProcessLogin = false;
				if(response=='success'){
					$scope.processMessage = "登录中成功";
					$scope.isProcessLogin = false;
			 		window.location.href = "/enterpriseuniversity/services/menu";
		 		} else if(response=='incorrectusername'){
			 		$scope.errorMessage.invalidUserName = "用户名不存在";
			 	}else if(response=='passworderror'){
			 		$scope.errorMessage.invalidPassword = "登录密码错误"; 
			 	}else if(response=='permissiondenied'){
			 		$scope.errorMessage.systemError = "用户无登录权限"; 
				}
			}
	 	}).error(function(){
			$scope.isProcessLogin = false;
		 	$scope.errorMessage.systemError = "系统忙，请稍后重试";
	 	})
	}
}]) 
.directive('windowResize', ['$window', function ($window) {
	return function (scope, element) {
  		var w = angular.element($window);
      	scope.getWindowHeightAndWidth = function () {
          	return {
              	'h': w.outerHeight(),
              	'w': w.outerWidth()
          	};
      	};
      	scope.$watch(scope.getWindowHeightAndWidth, function (newValue, oldValue) {
          	scope.windowHeight = newValue.h;
          	scope.windowWidth = newValue.w;
          	//设置对话框margin-top
          	scope.setBodyStyle = function () {
          		return {
                   'width':newValue.w + 'px',
                   'height':newValue.h + 'px'
              	};
          	};
          	scope.setFormStyle = function () {
          		return {
                   	'padding-top':(((newValue.h-400)/2)>0?(newValue.h-400)/2:0) + 'px'
              	};
          	};
      	}, true);
      
   	 	w.bind('resize', function () {
          scope.$apply();
      	});
  }
}])
.factory('dialogService',function(){
	
	
	
});
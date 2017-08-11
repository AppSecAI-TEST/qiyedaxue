<%@ page language="java" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form action="services/file/upload/aaa" method="post" enctype="multipart/form-data">
		<input type="file" id="a" />
		<input type ="text" onclick="test();">
		<button type="submit">提交</button>
	</form>
	<script type="text/javascript">
		function test(){
			alert(document.getElementById("a"));
		}
		
	</script>
</body>
</html>
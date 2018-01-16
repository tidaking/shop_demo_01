<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<script type="text/javascript">
	$.getJSON("${pageContext.request.contextPath}/category?method=getCategory", function(result){
		$(result).each(function(i,obj){
			$("#ul_category_id").append("<li><a href='${pageContext.request.contextPath}/product?method=findByCidInPages&curPage=1&cid="+obj.cid+"'>"+obj.cname+"</a></li>");
		})
	})
</script>

<div class="container-fluid">
				<div class="col-md-4">
					<img src="${pageContext.request.contextPath}/img/logo2.png" />
				</div>
				<div class="col-md-5">
					<img src="${pageContext.request.contextPath}/img/header.png" />
				</div>
				<div class="col-md-3" style="padding-top:20px">
					<!-- empty: 1, 判断对象是否为null 2. 判断一个集合的长度是否为0 3 .判断一个字符串 是否"" -->
					<c:if test="${not empty user}">
						<ol class="list-inline">
							<li>尊敬的会员,${user.name }</li>
							<li><a href="${pageContext.request.contextPath }/userServlet?method=logout">注销</a></li>
							<li><a href="${pageContext.request.contextPath }/order?method=findOrderInPage&curPage=1">我的订单</a></li>
							<li><a href="#">购物车</a></li>
						</ol>
					</c:if>	
					
					<c:if test="${empty user}">
						<ol class="list-inline">
							<li><a href="${pageContext.request.contextPath }/userServlet?method=loginUI">登录</a></li>
							<li><a href="${pageContext.request.contextPath }/userServlet?method=registUI">注册</a></li>
							<li><a href="${pageContext.request.contextPath }/jsp/cart.jsp">购物车</a></li>
						</ol>
					</c:if>	
				</div>
			</div>
			<!--
            	时间：2015-12-30
            	描述：导航条
            -->
			<div class="container-fluid">
				<nav class="navbar navbar-inverse">
					<div class="container-fluid">
						<!-- Brand and toggle get grouped for better mobile display -->
						<div class="navbar-header">
							<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
								<span class="sr-only">Toggle navigation</span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
							</button>
							<a class="navbar-brand" href="${pageContext.request.contextPath }/index?method=gotoIndex">首页</a>
						</div>

						<!-- Collect the nav links, forms, and other content for toggling -->
						<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
							<ul class="nav navbar-nav" id="ul_category_id">
							</ul>
							<form class="navbar-form navbar-right" role="search">
								<div class="form-group">
									<input type="text" class="form-control" placeholder="Search">
								</div>
								<button type="submit" class="btn btn-default">Submit</button>
							</form>

						</div>
						<!-- /.navbar-collapse -->
					</div>
					<!-- /.container-fluid -->
				</nav>
			</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<title><fmt:message key="login.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Le styles -->
<link href="${ctx}/resources/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${ctx}/resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
<link href="${ctx}/resources/bootstrap/css/default.css" rel="stylesheet">

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
	<script src="${ctx}/resources/bootstrap/js/html5shiv.js"></script>
<![endif]-->

</head>

<body data-spy="scroll" data-target=".menu">
<script type="text/javascript" src="${ctx}/resources/jquery/jquery-1.9.1.min.js"></script>
	<div class="container-fluid">
  <div class="row-fluid">

		<!-- menu -->
			<div class="span3 menu">
				<ul class="nav nav-list menu-sidenav">
					<li><a href="${ctx}/typography"> <i class="icon-chevron-right"></i> Typography
					</a></li>
					<li><a href="${ctx}/code"> <i class="icon-chevron-right"></i> Code
					</a></li>
					<li><a href="${ctx}/tables"> <i class="icon-chevron-right"></i> Tables
					</a></li>
					<li><a href="${ctx}/forms"> <i class="icon-chevron-right"></i> Forms
					</a></li>
					<li><a href="${ctx}/buttons"> <i class="icon-chevron-right"></i> Buttons
					</a></li>
					<li><a href="${ctx}/images"> <i class="icon-chevron-right"></i> Images
					</a></li>
					<li><a href="${ctx}/icons"> <i class="icon-chevron-right"></i> Icons by Glyphicons
					</a></li>
				</ul>
			</div>

			<!-- content -->
			<div class="span9" id="pjax-container">
			<jsp:include page="${path}"></jsp:include>
			</div>

		</div>
	</div>


	<script type="text/javascript" src="${ctx}/resources/bootstrap/js/bootstrap.min.js"></script>
<%-- 	<script type="text/javascript" src="${ctx}/resources/pjax/jquery.pjax.js"></script> --%>
	<script type="text/javascript" src="${ctx}/resources/pjax/jquery.pjax.welefen.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			 var $window = $(window)

			    // Disable certain links in docs
			    $('section [href^=#]').click(function (e) {
			      e.preventDefault();
			    });
			// side bar
			setTimeout(function() {
				$('.menu-sidenav').affix({
					offset : {
						top : 20,
						bottom : 20
					}
				});
			}, 100);

			//$(document).pjax('a', '#pjax-container');

			$.pjax({
        selector: 'a',
        container: '#pjax-container', //内容替换的容器
       // show: 'fade',  //展现的动画，支持默认和fade, 可以自定义动画方式，这里为自定义的function即可。
        cache: true,  //是否使用缓存
        storage: false,  //是否使用本地存储
        titleSuffix: '', //标题后缀
        filter: function(){},
        callback: function(){}
    });
		});
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<div id="headerDiv">
	<table style="width:100%;height:100%;table-layout:fixed;">
		<tr>
			<td width="180px"><a href="${ctx}/"><img src="${ctx}/resources/images/header.png"></img></a></td>
			<td align="center">
				<span style="padding:3px 10px 3px 18px;" class="user" style="height:20px;">${user.realName }，<span id="sayHelloSpan"></span></span>
				<span style="padding:3px 10px 3px 18px;" class="user_edit" style="height:20px;"><a href="javascript:void(0);" id="editUser">账户信息</a></span>
				<span style="padding:3px 10px 3px 18px;" class="key" style="height:20px;"><a href="javascript:void(0);" id="editPassword">修改密码</a></span>
				<span style="padding:3px 10px 3px 18px;" class="user_go" style="height:20px;"><a href="javascript:void(0);" id="logout">退出</a></span>
			</td>
			<td width="250px" align="right" style="padding-right:5px;" class="loginInfo">
				<div>上次登录时间: <fmt:formatDate value="${user.lastLoginTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			</td>
		</tr>
	</table>
</div>
   
<script type="text/javascript">
yepnope("${ctx}/resources/js/header.js");
</script>
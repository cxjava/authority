<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<title><fmt:message key="login.title" /></title>
<link rel="stylesheet" type="text/css" href="${ctx}/resources/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/resources/bootstrap/css/bootstrap-responsive.min.css" />
<style type="text/css">
.well {
	background-color: #F5F5F5;
	border: 1px solid rgba(0, 0, 0, 0.05);
	border-radius: 4px 4px 4px 4px;
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05) inset;
	margin-bottom: 20px;
	min-height: 20px;
	padding: 19px;
}
</style>
</head>
<body>
	<div class="container" style="padding-top: 20px;">
		<div class="row-fluid">
			<div class="span3">&nbsp;</div>
			<div class="span6">
				<c:choose>
					<c:when test="${error!=null}">
						<div class="alert alert-error">
							<a data-dismiss="alert" class="close">×</a> <strong>${error}</strong>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${msg!=null}">
								<div class="alert alert-success">
									<a data-dismiss="alert" class="close">×</a> <strong>${msg}</strong>
								</div>
							</c:when>
							<c:otherwise>
								<form class="well form-horizontal" action="${ctx}/resetpwd" method="post">
									<input type="hidden" size="40" name="u" value="${u}" /> <input type="hidden" size="40" name="t" value="${t}" />
									<fieldset>
										<div class="control-group">
											<label for="input01" class="control-label">新密码</label>
											<div class="controls">
												<input type="text" id="input01" class="input" name="newpwd" placeholder="请输入新密码">
											</div>
										</div>
										<div class="control-group">
											<label for="input01" class="control-label">确认密码</label>
											<div class="controls">
												<input type="text" id="input01" class="input" name="renewpwd" placeholder="请输入确认密码">
											</div>
										</div>
										<div class="control-group">
											<label for="submit" class="control-label"></label>
											<div class="controls">
												<button class="btn btn-primary" type="submit" id="submit">提交</button>
												<button class="btn" type="reset">重置</button>
											</div>
										</div>
									</fieldset>
								</form>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="span3">&nbsp;</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctx}/resources/jquery/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>

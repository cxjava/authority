<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
//Set to expire far in the past.
response.setDateHeader("Expires", 0);
// Set standard HTTP/1.1 no-cache headers.
response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
response.addHeader("Cache-Control", "post-check=0, pre-check=0");
// Set standard HTTP/1.0 no-cache header.
response.setHeader("Pragma", "no-cache");
%>
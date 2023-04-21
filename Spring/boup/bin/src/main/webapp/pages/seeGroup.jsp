<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Grupo - ${group.groupName}</title>
<link type="text/css" rel="stylesheet" href="../resources/css/materialize.css" media="screen,proyection"/>
<script type="text/javascript" src="../resources/js/materialize.js"></script>
</head>
<body>
	<%@include file="header.jsp" %>
	<div class="container">
		<div class="row">
			<h2>Grupo ${group.groupName} </h2>
		</div>
		<div class="row">
			<c:url var="url" value="groupForm">
				<c:param name="id" value="${group.id}"></c:param>
			</c:url>
			<a href="${url}" class="waves-effect waves-light btn">Modificar</a>
		</div>
	</div>
	
</body>
</html>
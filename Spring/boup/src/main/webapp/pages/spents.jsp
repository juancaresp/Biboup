<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Gastos</title>
<link type="text/css" rel="stylesheet" href="../resources/css/materialize.css" media="screen,proyection"/>
<script type="text/javascript" src="../resources/js/materialize.js"></script>
</head>
<body>
	<%@include file="header.jsp" %>
	
	<div class="container">
		<div class="row">
			<h2>Gastos</h2>
		</div>
		<div class="row">
			<div class="col s2  offset-s4">
				<form method="get" action="/web/spentFormu">
					<input type="submit" class="waves-effect waves-light btn" value="Nuevo">
				</form>	
			</div>
		</div>
		
		<div class="row">
			<table class="striped">
				
					<thead>
						<tr>
							<th>Nombre</th>
							<th>Pagador</th>
							<th>Fecha</th>
							<th>Cantidad</th>
							<th>Grupo</th>
						</tr>	
					</thead>
					<tbody>
						<c:forEach items="${spents}" var="s">
							<tr>
								<td>
									<c:url var="url" value="spent">
										<c:param name="id" value="${s.id}"></c:param>
									</c:url>
									<a href="${url}">${s.spentName}</a>
								</td>
								<td>${s.payer.username}</td>
								<td>${s.date}</td>
								<td>${s.quantity}</td>
								<td>${s.group.groupName}</td>
							</tr>
						</c:forEach>
					</tbody>	
				</table>
		</div>
	</div>
</body>
</html>
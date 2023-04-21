<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Formulario de Usuario</title>
<link type="text/css" rel="stylesheet" href="../resources/css/materialize.css" media="screen,proyection"/>
<script type="text/javascript" src="../resources/js/materialize.js"></script>
</head>
<body>
	<%@include file="header.jsp" %>
	
	<div class="container">
		<div class="row">
			<h2>Usuarios</h2>
		</div>
		<div class="row">
			<div class="col s2  offset-s4">
				<form method="get" action="/web/userFormu">
					<input type="submit" class="waves-effect waves-light btn" value="Nuevo">
				</form>	
			</div>
		</div>
		
		<div class="row">
			<table class="striped">
				
					<thead>
						<tr>
							<th>Username</th>
							<th>Correo</th>
							<th>Nombre</th>
							<th>Telefono</th>
							<th>Monedero</th>
						</tr>	
					</thead>
					<tbody>
						<c:forEach items="${users}" var="u">
							<tr>
								<td>
									<c:url var="url" value="user">
										<c:param name="id" value="${u.id}"></c:param>
									</c:url>
									<a href="${url}">${u.username}</a>
								</td>
								<td>${u.email}</td>
								<td>${u.nameU}</td>
								<td>${u.telephone}</td>
								<td>${u.wallet}</td>
							</tr>
						</c:forEach>
					</tbody>	
				</table>
		</div>
	</div>
</body>
</html>
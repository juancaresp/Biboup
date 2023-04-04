<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Formulario de Deuda</title>
<link type="text/css" rel="stylesheet" href="../resources/css/materialize.css" media="screen,proyection"/>
<script type="text/javascript" src="../resources/js/materialize.js"></script>
</head>
<script>
	function enviarPeticion(accion){
		let formuario=document.getElementById("formulario")
		formulario.action=accion;
	}
</script>
<body>
	<%@include file="header.jsp" %>
	
	<div class="container">
		<div class="row">
			<h2>Deudas</h2>
		</div>
		<div class="row">
			<div class="col s2  offset-s4">
				<form method="get" action="/web/debtFormu">
					<input type="submit" class="waves-effect waves-light btn" value="Nuevo">
				</form>	
			</div>
		</div>
		
		<div class="row">
			<table class="striped">
				
					<thead>
						<tr>
							<th>Modificar</th>
							<th>Recibidor</th>
							<th>Endeudado</th>
							<th>Cantidad</th>
						</tr>	
					</thead>
					<tbody>
						<c:forEach items="${debts}" var="d">
							<tr>
								<td>
									<c:url var="url" value="debtForm">
										<c:param name="id" value="${d.id}"></c:param>
									</c:url>
									<a href="${url}" class="waves-effect waves-light btn">Editar</a>
								</td>
								<td>${d.receiver.username}</td>
								<td>${d.debtor.username}</td>
								<td>${d.amount}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</div>
	</div>
</body>
</html>
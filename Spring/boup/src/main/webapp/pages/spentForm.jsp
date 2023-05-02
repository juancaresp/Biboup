<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Formulario de Gasto</title>
<link type="text/css" rel="stylesheet"
	href="../resources/css/materialize.css" media="screen,proyection" />
<script type="text/javascript" src="../resources/js/materialize.js"></script>
</head>
<script>
	function enviarPeticion(accion) {
		let formuario = document.getElementById("formulario")
		formulario.action = accion;
	}
</script>
<body>
	<%@include file="header.jsp"%>
	<div class="container">
		<div class="row">
			<h2>Gasto</h2>
		</div>
		<div class="row">
			<form:form id="formulario" modelAttribute="spent" method="POST">

				<input type="submit" value="Insertar"
					class="waves-effect waves-light btn-large"
					onclick="enviarPeticion('/web/spent/insert')">
				<input type="submit" value="Modificar"
					class="waves-effect waves-light btn-large"
					onclick="enviarPeticion('/web/spent/update')">
				<input type="submit" value="Borrar"
					class="waves-effect waves-light btn-large"
					onclick="enviarPeticion('/web/spent/delete')">

				<form:hidden path="id" id="id" />
				<div>
					<div class="input-field col s12">
						<label for="spentName">Nombre</label>
						<form:input path="spentName" id="spentName" name="spentName" />
					</div>
					<div class="input-field col s12">
						<label for="paye">Pagador</label> 
						<input type="text" id="paye" name="paye" value="${spent.payer.username}">
					</div>
					<div class="input-field col s12">
						<label for="quantity">Quantity</label>
						<form:input path="quantity" id="quantity" name="quantity" />
					</div>
					<div class="input-field col s12">
						<label for="dat">Fecha (YYYY-MM-DD)</label>
						<input type="text" id="dat" name="dat" value="${spent.date}" />
					</div>
					<div class="input-field col s12">
						<label for="groupId">ID del Grupo</label>
						<input type="text" id="groupId" name="groupId" value="${spent.group.groupName}" />
					</div>
				</div>
			</form:form>
		</div>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Formulario de Usuario</title>
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
			<h2>Grupo</h2>
		</div>
		<div class="row">
			<form:form id="formulario" modelAttribute="group" method="POST">
				<input type="submit" value="Insertar"
					class="waves-effect waves-light btn-large"
					onclick="enviarPeticion('/web/group/insert')">
				<input type="submit" value="Modificar"
					class="waves-effect waves-light btn-large"
					onclick="enviarPeticion('/web/group/update')">
				<input type="submit" value="Borrar"
					class="waves-effect waves-light btn-large"
					onclick="enviarPeticion('/web/group/delete')">

				<form:hidden path="id" id="id" />
				<div>
					<div class="input-field col s12">
						<label for="groupName">Nombre del grupo</label>
						<form:input path="groupName" id="groupName" name="groupName" />
					</div>
				</div>

			</form:form>
		</div>
	</div>
</body>
</html>
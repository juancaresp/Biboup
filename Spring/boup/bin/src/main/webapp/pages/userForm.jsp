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
			<h2>Usuarios</h2>
		</div>
		<div class="row">
			<form:form id="formulario" modelAttribute="user" method="POST">
				<input type="submit" value="Insertar"
					class="waves-effect waves-light btn-large"
					onclick="enviarPeticion('/web/user/insert')">
				<input type="submit" value="Modificar"
					class="waves-effect waves-light btn-large"
					onclick="enviarPeticion('/web/user/update')">
				<input type="submit" value="Borrar"
					class="waves-effect waves-light btn-large"
					onclick="enviarPeticion('/web/user/delete')">

				<form:hidden path="id" id="id" />
				<div>
					<div class="input-field col s12">
						<label for="username">Username</label>
						<form:input path="username" id="username" name="username" />
					</div>
					<div class="input-field col s12">
						<label for="email">Email</label>
						<form:input path="email" id="email" type="email" name="email"
							class="validate" />
					</div>
					<div class="input-field col s12">
						<label for="nameU">Nombre de Usuario</label>
						<form:input path="nameU" id="nameU" name="nameU" />
					</div>
					<div class="input-field col s6">
						<label for="telephone">Teléfono</label>
						<form:input path="telephone" id="telephone" name="telephone" />
					</div>
					<div class="input-field col s3">
						<label for="wallet">Monedero</label>
						<form:input path="wallet" id="wallet" name="wallet" />
					</div>
				</div>

			</form:form>
		</div>
	</div>
</body>
</html>
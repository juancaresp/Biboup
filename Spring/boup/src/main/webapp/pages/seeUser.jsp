<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Usuario - ${user.username}</title>
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
			<h2>Usuario ${user.username}</h2>
		</div>
		<div class="row">
			<c:url var="url" value="userForm">
				<c:param name="id" value="${user.id}"></c:param>
			</c:url>
			<a href="${url}" class="waves-effect waves-light btn">Modificar</a>
		</div>
	</div>
	<div>
		<h2>Grupos</h2>
	</div>
	<div>
		<form method="POST" id="addGformulario" action='/web/user/addGroup'>
			<div>
				<div class="input-field col s2">
					<label for="groupid">ID del grupo</label>
					<input type="text" id="groupid" name="groupid" />
				</div>
			</div>
			<input type="hidden" name="userId" value="${user.id}" /> 
			<input type="submit" value="Agregar" />
		</form>
	</div>

	<div class="row">
		<table class="striped col s6 offset-s3">

			<thead>
				<tr>
					<th>Groupname</th>
					<th>Accion</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${groups}" var="g">
					<tr>
						<td><c:url var="url" value="group">
								<c:param name="id" value="${g.id}"></c:param>
							</c:url> <a href="${url}">${g.groupName}</a></td>
						<td>
							<form method="POST" id="deleteGformulario"
								action='/web/user/deleteGroup'>
								<input type="hidden" name="groupId" value="${g.id}" /> <input
									type="hidden" name="userId" value="${user.id}" /> <input
									type="submit" value="Borrar" />
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

</body>
</html>
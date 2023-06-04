
<html>
<head>
<meta charset="ISO-8859-1">
<title>Gasto - ${spent.spentName}</title>
<link type="text/css" rel="stylesheet" href="../resources/css/materialize.css" media="screen,proyection"/>
<script type="text/javascript" src="../resources/js/materialize.js"></script>
</head>
<body>
	<%@include file="header.jsp" %>
	<div class="container">
		<div class="row">
			<h2>Gasto ${spent.spentName} </h2>
		</div>
		<div class="row">
			<c:url var="url" value="spentForm">
				<c:param name="id" value="${spent.id}"></c:param>
			</c:url>
			<a href="${url}" class="waves-effect waves-light btn">Modificar</a>
		</div>
	</div>
	<div>
		<h2>Usuarios</h2>
	</div>
	<div>
		<form method="POST" id="addUformulario" action='/web/spent/addUser'>
			<div>
				<div class="input-field col s2">
					<label for="userId">ID del usuario</label>
					<input type="text" id="userId" name="userId" />
				</div>
			</div>
			<input type="hidden" name="spentId" value="${spent.id}" /> 
			<input type="submit" value="Agregar" />
		</form>
	</div>

	<div class="row">
		<table class="striped col s6 offset-s3">

			<thead>
				<tr>
					<th>Usuario</th>
					<th>Accion</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${users}" var="u">
					<tr>
						<td><c:url var="url" value="user">
								<c:param name="id" value="${u.id}"></c:param>
							</c:url> <a href="${url}">${u.username}</a></td>
						<td>
							<form method="POST" id="deleteGformulario"
								action='/web/spent/deleteUser'>
								<input type="hidden" name="spentId" value="${spent.id}" /> <input
									type="hidden" name="userId" value="${u.id}" /> <input
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
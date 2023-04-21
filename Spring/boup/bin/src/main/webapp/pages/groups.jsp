<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Formulario de Grupos</title>
<link type="text/css" rel="stylesheet" href="../resources/css/materialize.css" media="screen,proyection"/>
<script type="text/javascript" src="../resources/js/materialize.js"></script>
</head>
<body>
	<%@include file="header.jsp" %>
	
	<div class="container">
		<div class="row">
			<h2>Grupos</h2>
		</div>
		<div class="row">
			<div class="col s2  offset-s4">
				<form method="get" action="/web/groupFormu">
					<input type="submit" class="waves-effect waves-light btn" value="Nuevo">
				</form>	
			</div>
		</div>
		
		<div class="row">
			<table class="striped">
				
					<thead>
						<tr>
							<th>Groupname</th>
						</tr>	
					</thead>
					<tbody>
						<c:forEach items="${groups}" var="g">
							<tr>
								<td>
									<c:url var="url" value="group">
										<c:param name="id" value="${g.id}"></c:param>
									</c:url>
									<a href="${url}">${g.groupName}</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>	
				</table>
		</div>
	</div>
</body>
</html>
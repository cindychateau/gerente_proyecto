<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>
	<div class="container">
		<header class="d-flex justify-content-between align-items-center">
			<h1>¡Bienvenid@ ${userSession.firstName}! </h1>
			<a href="/projects/new" class="btn btn-success">Nuevo Proyecto</a>
			<a href="/logout" class="btn btn-danger">Cerrar Sesión</a>
		</header>
		<div class="row">
			<h2>Todos los Proyectos</h2>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Proyecto</th>
						<th>Líder de Proyecto</th>
						<th>Fecha Límite</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${otherProjects}" var="p" >
						<tr>
							<td>${p.title}</td>
							<td>${p.lead.firstName}</td>
							<td>${p.dueDate}</td>
							<td>
								<a href="/projects/join/${p.id}" class="btn btn-info">Unirme</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<hr>
		<div>
			<h2>Mis Proyectos</h2>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Proyecto</th>
						<th>Líder de Proyecto</th>
						<th>Fecha Límite</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${myProjects}" var="p" >
						<tr>
							<td>${p.title}</td>
							<td>${p.lead.firstName}</td>
							<td>${p.dueDate}</td>
							<td>
							
								<c:if test="${p.lead.id == userSession.id}" >
									<a href="/projects/edit/${p.id}" class="btn btn-warning">Editar</a>
								</c:if>
								
								<c:if test="${p.lead.id != userSession.id}">
									<a href="/projects/leave/${p.id}" class="btn btn-danger">Salir</a>
								</c:if>
								
								<!-- 
								<c:choose>
									<c:when test="CONDICIONAL">
										ETIQUETAS
									</c:when>
									<c:otherwise>
										ETIQUETAS
									</c:otherwise>
								</c:choose>
								 -->
								
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	
</body>
</html>
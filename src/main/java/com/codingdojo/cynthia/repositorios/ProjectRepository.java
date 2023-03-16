package com.codingdojo.cynthia.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.cynthia.modelos.Project;
import com.codingdojo.cynthia.modelos.User;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	
	//Seleccionamos aquellos proyectos a los que el usuario NO pertenece
	List<Project> findByUsersJoinedNotContains(User user);
	
	//Seleccionamos aquellos proyectos a los que el usuario SI pertenezca
	List<Project> findAllByUsersJoined(User user);
	
}

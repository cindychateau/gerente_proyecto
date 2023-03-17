package com.codingdojo.cynthia.servicios;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.codingdojo.cynthia.modelos.LoginUser;
import com.codingdojo.cynthia.modelos.Project;
import com.codingdojo.cynthia.modelos.User;
import com.codingdojo.cynthia.repositorios.ProjectRepository;
import com.codingdojo.cynthia.repositorios.UserRepository;

@Service
public class AppService {
	
	@Autowired
	private UserRepository repoUser;
	
	@Autowired
	private ProjectRepository repoProject;
	
	
	//Función que registra un usuario
	public User register(User nuevoUsuario, BindingResult result) {
		
		//Comparando las contraseñas
		if(!nuevoUsuario.getPassword().equals(nuevoUsuario.getConfirm())) {
			result.rejectValue("password", "Matches", "Las contraseñas no coinciden.");
		}
		
		//Revisar si el correo electrónico ya existe
		String nuevoEmail = nuevoUsuario.getEmail();
		if(repoUser.findByEmail(nuevoEmail).isPresent()) {
			result.rejectValue("email", "Unique", "El correo electrónico fue ingresado previamente.");
		}
		
		if(result.hasErrors()) {
			return null;
		} else {
			//Encriptamos la contraseña
			String contraEncriptada = BCrypt.hashpw(nuevoUsuario.getPassword(), BCrypt.gensalt());
			nuevoUsuario.setPassword(contraEncriptada);
			return repoUser.save(nuevoUsuario);
		}
		
	}
	
	//Función de inicio de sesión
	public User login(LoginUser nuevoLogin, BindingResult result) {
		
		//Buscamos por correo electrónico
		Optional<User> posibleUsuario = repoUser.findByEmail(nuevoLogin.getEmail());
		if(!posibleUsuario.isPresent()) {
			result.rejectValue("email", "Unique", "Correo no registrado");
			return null;
		}
		
		User userLogin = posibleUsuario.get(); //Usuario que me regresa mi DB
		if(!BCrypt.checkpw(nuevoLogin.getPassword(), userLogin.getPassword())) {
			result.rejectValue("password", "Matches", "Contraseña inválida");
		}
		
		if(result.hasErrors()) {
			return null;
		} else {
			return userLogin;
		}	
		
	}
	
	/*Guarda objeto de proyecto en BD*/
	public Project saveProject(Project nuevoProyecto) {
		return repoProject.save(nuevoProyecto);
	}
	
	/*Regrese objeto de usuario en base a su ID*/
	public User findUser(Long id) {
		return repoUser.findById(id).orElse(null);
	}
	
	/*Guarda en BD los cambios de usuario*/
	public User saveUser(User user) {
		return repoUser.save(user);
	}
	
	/*Regrese la lista de proyectos a los cuales NO pertenezco*/
	public List<Project> findOtherProjects(User userInSession) {
		return repoProject.findByUsersJoinedNotContains(userInSession);
	}
	
	/*Regrese la lista de proyectos a los cuales SI pertenezco*/
	public List<Project> findMyProjects(User userInSession) {
		return repoProject.findAllByUsersJoined(userInSession);
	}
	
	/*Regrese objeto de proyecto en base a su ID*/
	public Project findProject(Long id) {
		return repoProject.findById(id).orElse(null);
	}
	
	/*Método que nos una a un proyecto */
	public void saveProjectUser(Long projectId, Long userId) {
		User myUser = findUser(userId); //Obtiene el objeto de usuario
		Project myProject = findProject(projectId);
		
		myUser.getProjectsJoined().add(myProject);
		repoUser.save(myUser);
	}
	
	/*Método que nos elimine de un proyecto*/
	public void removeProjectUser(Long projectId, Long userId) {
		User myUser = findUser(userId);
		Project myProject = findProject(projectId);
		
		/*myProject.getUsersJoined().remove(myUser);
		repoProject.save(myProject);*/
		
		myUser.getProjectsJoined().remove(myProject);
		repoUser.save(myUser);
	}
	
}
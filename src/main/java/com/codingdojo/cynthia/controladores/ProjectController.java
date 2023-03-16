package com.codingdojo.cynthia.controladores;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingdojo.cynthia.modelos.Project;
import com.codingdojo.cynthia.modelos.User;
import com.codingdojo.cynthia.servicios.AppService;

@Controller
@RequestMapping("/projects")
public class ProjectController {
	
	@Autowired
	private AppService servicio;
	
	@GetMapping("/new")
	public String newProject(@ModelAttribute("project") Project project,
							 HttpSession session) {
		/*REVISAMOS LA SESIÓN*/
		User currentUser = (User)session.getAttribute("userSession");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS LA SESIÓN*/
		
		return "new.jsp";
	}
	
	@PostMapping("/create")
	public String createProject(@Valid @ModelAttribute("project") Project project,
								BindingResult result,
								HttpSession session) {
		
		/*REVISAMOS LA SESIÓN*/
		User currentUser = (User)session.getAttribute("userSession");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS LA SESIÓN*/
		
		//Revisamos si hay errores en el registro
		if(result.hasErrors()) {
			return "new.jsp";
		} else {
			//Guardamos el proyecto
			Project nuevoProyecto = servicio.saveProject(project);
			//Agregamos a la lista de proyectos a los que me uní
			User myUser = servicio.findUser(currentUser.getId()); //Obtiene el objeto de usuario
			myUser.getProjectsJoined().add(nuevoProyecto); //Agregamos a la lista de proyectos unidos el nuevo proyecto
			servicio.saveUser(myUser);
			
			return "redirect:/dashboard";
		}	
		
	}
	
	
}

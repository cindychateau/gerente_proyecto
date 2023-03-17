package com.codingdojo.cynthia.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.codingdojo.cynthia.modelos.LoginUser;
import com.codingdojo.cynthia.modelos.Project;
import com.codingdojo.cynthia.modelos.User;
import com.codingdojo.cynthia.servicios.AppService;

@Controller
public class UserController {
	
	@Autowired
	private AppService service;
	
	@GetMapping("/")
	public String index(@ModelAttribute("nuevoUsuario") User nuevoUsuario,
						@ModelAttribute("nuevoLogin") LoginUser nuevoLogin) {
		//ModelAttribute -> Enviar un objeto vacío
		
		/*
		 *model.addAttribute("nuevoUsuario", new User()); -> Enviando un objeto vacío
		 */
		
		return "index.jsp";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("nuevoUsuario") User nuevoUsuario,
						   BindingResult result,
						   Model model,
						   HttpSession session) {
		
		service.register(nuevoUsuario, result);
		
		if(result.hasErrors()) {
			model.addAttribute("nuevoLogin", new LoginUser()); //Enviamos LoginUser vacío
			return "index.jsp";
		} else {
			session.setAttribute("userSession", nuevoUsuario);
			return "redirect:/dashboard";
		}
		
	}
	
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session,
							Model model) {
		User currentUser = (User)session.getAttribute("userSession");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		
		//Proyectos a los que pertenezco
		List<Project> myProjects = service.findMyProjects(currentUser); 
		
		//Proyectos a los que NO pertenezco
		List<Project> otherProjects = service.findOtherProjects(currentUser);
		
		model.addAttribute("otherProjects", otherProjects);
		model.addAttribute("myProjects", myProjects);
		
		
		return "dashboard.jsp";
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("nuevoLogin") LoginUser nuevoLogin,
						BindingResult result,
						Model model,
						HttpSession session) {
		
		User user = service.login(nuevoLogin, result);
		if(result.hasErrors()) {
			model.addAttribute("nuevoUsuario", new User());
			return "index.jsp";
		}
		
		session.setAttribute("userSession", user);
		return "redirect:/dashboard";
		
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("userSession");
		return "redirect:/";
	}
	
}
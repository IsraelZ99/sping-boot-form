package com.bolsadeideas.springboot.form.app.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.form.app.editors.NombreMayusculaEditor;
import com.bolsadeideas.springboot.form.app.models.domain.Pais;
import com.bolsadeideas.springboot.form.app.models.domain.Usuario;
import com.bolsadeideas.springboot.form.app.validation.UsuarioValidador;

@Controller
@SessionAttributes("usuario")
public class FormController {

	@Autowired
	private UsuarioValidador validador;

	@InitBinder // Validar de forma transaparente las validaciones
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(validador);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false); // Ser estricto
		binder.registerCustomEditor(Date.class, "fechaNacimiento", new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(String.class, "nombre", new NombreMayusculaEditor());
		binder.registerCustomEditor(String.class, "apellido", new NombreMayusculaEditor());

	}
	
	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises() {
		return Arrays.asList(
				new Pais(1, "ES", "España"),
				new Pais(2, "MX", "Mexico"),
				new Pais(3, "CL", "Chile"),
				new Pais(4, "AR", "Argentina"),
				new Pais(5, "PE", "Peru"),
				new Pais(6, "CO", "Colombia"),
				new Pais(7, "VE", "Venezuela")
				);
	}

	@ModelAttribute("paises")
	public List<String> paises() {
		return Arrays.asList("España", "Mexico", "Chile", "Peru", "Colombia", "Venezuela");
	}

	@ModelAttribute("paisesMap")
	public Map<String, String> paisesMap() {
		Map<String, String> paises = new HashMap<String, String>();
		paises.put("ES", "España");
		paises.put("MX", "Mexico");
		paises.put("CL", "Chile");
		paises.put("AR", "Argentina");
		paises.put("PE", "Peru");
		paises.put("CO", "Colombia");
		paises.put("VE", "Venezuela");
		return paises;
		
	}

	@GetMapping("/form")
	public String form(Model model) {
		Usuario usuario = new Usuario();
		usuario.setIdentificador("12.456.789-K");
		usuario.setNombre("John");
		usuario.setApellido("Doe");
		usuario.setUsername("israel");
		usuario.setPassword("12345");
		usuario.setCuenta(10);
		model.addAttribute("titulo", "Formulario usuarios");
		model.addAttribute("usuario", usuario);
		return "form";
	}

	@PostMapping("/form")
	public String procesar(@Valid Usuario usuario, BindingResult result, Model model, SessionStatus status) {

		// validador.validate(usuario, result);

		model.addAttribute("titulo", "Resultado form");

		if (result.hasErrors()) {

			return "form";
		}

		model.addAttribute("usuario", usuario);
		status.setComplete();
		return "resultado";
	}

//	 @PostMapping("/form")
//	//@Valid sirve para validaciones
//	//@Binding sirve para manejar los errores a partir de las validaciones y debe de estar despues del valid
//	public String procesar( @Valid  /*@ModelAttribute("user") */ Usuario usuario , BindingResult result, Model model, SessionStatus status) {
//		model.addAttribute("titulo", "Resultado del formulario");
//		/* LISTANDO LOS ERRORES QUE ARROJA VALID */
//		if(result.hasErrors()) {
////			Map<String, String> errores = new HashMap<>();
////			result.getFieldErrors().forEach(error -> {
////				errores.put(error.getField(), "El campo ".concat(error.getField().concat(" ").concat(error.getDefaultMessage())));
////			});
////			model.addAttribute("error", errores);
//			return "form";
//		}
//		model.addAttribute("usuario", usuario);
//		status.setComplete(); // Completar el proceso Se elimina el objeto usuario de la sesión
//		return "resultado";
//	}

}

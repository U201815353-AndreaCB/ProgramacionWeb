package pe.edu.upc.spring.controller;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sun.el.parser.ParseException;
import pe.edu.upc.spring.model.Tutor;
import pe.edu.upc.spring.service.ITutorService;

@Controller
@RequestMapping("/tutor")
public class TutorController {

	@Autowired
	private ITutorService tService;

	@RequestMapping("/bienvenido")
	public String irPaginaBienvenida() {
		return "bienvenido";
	}

	@RequestMapping("/")
	public String irPaginaListadoTutores(Map<String, Object> model) {
		model.put("listaTutores", tService.listar());
		return "listTutor";
	}

	@RequestMapping("/irRegistrar")
	public String irPaginaRegistrar(Model model) {
		model.addAttribute("tutor", new Tutor());
		return "tutor";
	}

	@RequestMapping("/registrar")
	public String registrar(@ModelAttribute Tutor objTutor, BindingResult binRes, Model model) throws ParseException {
		if (binRes.hasErrors())
			return "tutor";
		else {
			boolean flag = tService.insertar(objTutor);
			if (flag)
				return "redirect:/tutor/listar";
			else {
				model.addAttribute("mensaje", "Ocurrió un error");
				return "redirect:/tutor/irRegistrar";
			}
		}
	}

	@RequestMapping("/modificar/{id}")
	public String modificar(@PathVariable int id, Model model, RedirectAttributes objRedir) throws ParseException {
		Optional<Tutor> objTutor = tService.listarId(id);
		if (objTutor == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrió un error");
			return "redirect:/tutor/listar";
		} else {
			model.addAttribute("tutor", objTutor);
			return "tutor";
		}
	}
	
	@RequestMapping("/eliminar")
	public String eliminar(Map<String, Object> model, @RequestParam(value="id") Integer id) {
		try {
			if (id!=null && id>0) {
				tService.eliminar(id);
				model.put("listaTutores", tService.listar());
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			model.put("mensaje", "Ocurrio un error");
			model.put("listaTutor", tService.listar());
		}
		return "listTutor";
		}
	
	@RequestMapping("/listar")
	public String listar(Map<String, Object> model) {
		model.put("listaTutores", tService.listar());
		return "listTutor";
	}
}
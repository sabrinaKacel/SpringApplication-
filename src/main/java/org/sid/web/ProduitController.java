package org.sid.web;

import javax.validation.Valid;

import org.sid.dao.ProduitRepository;
import org.sid.entities.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProduitController {
	
	@Autowired
	private ProduitRepository produitRepository;
	
	//@RequestMapping(value = "/index", method = RequestMethod.GET)
	@GetMapping("/index") //quand on tape l'url localhost/index on execute cette methode ci-dessous, retourne le fichier vue produits dans template
	//le methode utilisée par défaut GET
	public String index(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "motCle", defaultValue = "") String mc) {
		//Pagination 
		Page<Produit> produits = produitRepository.findByDesignationContains(mc, PageRequest.of(page, 5));
		model.addAttribute("listProduits", produits.getContent());
		model.addAttribute("pages", new int [produits.getTotalPages()]);
		model.addAttribute("currentPage", page);
		return "produits";
	}
	
	@GetMapping("/delete")
	public String delete(Long id, int page, String mc) {
		produitRepository.deleteById(id);
		return "redirect:/index?page="+page+"&motCle="+mc;
	}
	
	@GetMapping("/edit")
	public String edit(Model model, Long id) {
		Produit produit = produitRepository.findById(id).get();
		model.addAttribute("produit", produit);
		return "EditProduit";
	}
	
	@GetMapping("/formProduit")
	public String form(Model model) {
		model.addAttribute("produit", new Produit());
		return "FormProduit";
	}
	
	//We use PostMapping when we add a product to the database, method post should be used in form
	//l'annotation @Valid valide les annotations de validation du produit
	@PostMapping("/save")
	public String save(Model model, @Valid Produit produit, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) return "FormProduit";
		produitRepository.save(produit);
		return "redirect:/index";
	}

}

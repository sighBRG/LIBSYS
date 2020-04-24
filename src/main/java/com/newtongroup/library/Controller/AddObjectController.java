package com.newtongroup.library.Controller;

import com.newtongroup.library.Entity.Seminary;
import com.newtongroup.library.Repository.BookRepository;
import com.newtongroup.library.Repository.EBookRepository;
import com.newtongroup.library.Repository.SeminaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/new-object")
public class AddObjectController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    EBookRepository eBookRepository;

    @Autowired
    SeminaryRepository seminaryRepository;

    @GetMapping("/new-seminar")
    public String getSeminarForm(Model model){
        Seminary seminary=new Seminary();
        model.addAttribute("seminary", seminary);
        return "object/add-seminar";
    }
    @PostMapping ("/save-seminar")
    public String saveSeminar(Seminary seminary, Model model){
        seminaryRepository.save(seminary);
        return "redirect:/new-object/new-seminar";
    }

}

package com.newtongroup.library.Controller;

import com.newtongroup.library.Entity.*;
import com.newtongroup.library.Repository.*;
import com.newtongroup.library.Utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookrepository;

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Autowired
    private EbookLoanRepository ebookLoanRepository;

    @Autowired
    private EBookRepository eBookRepository;



    private String header = "librarian/bootstrapheader.html";

    @RequestMapping("/")
    public String mainView(Model theModel, Principal principal){
        theModel.addAttribute("header", HeaderUtils.getHeaderString(userRepository.findByUsername(principal.getName())));
        return "librarian/start";
    }

    @RequestMapping("/delete-visitor-menu")
    public String deleteUserMenu(Model theModel, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        theModel.addAttribute("header", HeaderUtils.getHeaderString(user));
        theModel.addAttribute("email", new String());
        return "librarian/delete-user-menu";
    }

    @RequestMapping("/delete-visitor")
    public String deleteUser(@RequestParam(name="email") String email, Model theModel, Principal principal) {
        theModel.addAttribute("header", HeaderUtils.getHeaderString(userRepository.findByUsername(principal.getName())));
        User user = userRepository.findByUsername(email);
        if(user == null) {
            return "error/email-cannot-be-found";
        } else if (user.getAuthority().getAuthorityName().equals("ROLE_VISITOR")) {
            visitorRepository.deleteById(email);
            userRepository.deleteById(user.getId());
        } else {
            return "error/email-cannot-be-found";
        }

        return "admin/delete-confirmation";
    }

    @RequestMapping("/input-book-to-return")
    private String prepareToReturnBook(Model theModel, Principal principal) {
        System.out.println(principal.getName());
        theModel.addAttribute("header", HeaderUtils.getHeaderString(userRepository.findByUsername(principal.getName())));
        return "loan/return-book";
    }

    @RequestMapping("/return-book")
    private String returnBook(@RequestParam(name="bookId", required = false) Long bookId,
                              @RequestParam(name="eBookId", required = false) Long eBookId,
                              Model theModel, Principal principal) {

        theModel.addAttribute("header", HeaderUtils.getHeaderString(userRepository.findByUsername(principal.getName())));


        if(bookId != null) {
            Book bookToReturn = bookrepository.findById(bookId).orElse(null);
            BookLoan loan = bookLoanRepository.findByBookAndIsBookReturned(bookToReturn, false);

            if(loan == null) {
                return "error/book-or-no-active-librarycard";
            }

            bookToReturn.setAvailable(true);
            loan.setDateReturned(new Date(Calendar.getInstance().getTime().getTime()));
            loan.setBookReturned(true);
            bookLoanRepository.save(loan);

        } else if ( eBookId != null) {
            EBook bookToReturn = eBookRepository.findById(eBookId).orElse(null);
            EbookLoan loan = ebookLoanRepository.findByEbookAndIsEbookReturned(bookToReturn, false);

            if(loan == null) {
                return "error/book-or-no-active-librarycard";
            }

            bookToReturn.setAvailable(true);
            loan.setDateReturned(new Date(Calendar.getInstance().getTime().getTime()));
            loan.setEbookReturned(true);
            ebookLoanRepository.save(loan);

        } else {
            return "error/book-or-no-active-librarycard";
        }

        return "loan/return-success";
    }




}

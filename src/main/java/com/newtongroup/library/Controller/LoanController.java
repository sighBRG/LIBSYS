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

@Controller
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private BookRepository bookrepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Autowired
    private  EbookLoanRepository ebookLoanRepository;

    @Autowired
    private EBookRepository eBookRepository;

    @RequestMapping("/")
    public String loanhome(Model theModel, Principal principal){
        theModel.addAttribute("header", HeaderUtils.getHeaderString(userRepository.findByUsername(principal.getName())));

        return "loan/register-book";
    }

    @RequestMapping("/register-loan")
    public String registerLoan(@RequestParam(value = "bookId", required = false) Long bookId,
                               @RequestParam(name="eBookId", required = false) Long eBookId,
                               Model theModel, Principal principal) {

        theModel.addAttribute("header", HeaderUtils.getHeaderString(userRepository.findByUsername(principal.getName())));
        Visitor visitor = visitorRepository.findById(principal.getName()).orElse(null);

        if(!doesVisitorHaveActiveLibraryCard(visitor)) {
            return "error/error-book-or-no-active-librarycard";
        }

        if(bookId != null) {
            Book book = bookrepository.findById((bookId)).orElse(null);
            if(!book.isAvailable()) {
                return "error/book-is-not-available";
            }
            BookLoan bookLoan = getLoan(book, visitor);
            bookLoanRepository.save(bookLoan);

        } else if (eBookId != null) {
            EBook ebook = eBookRepository.findById((eBookId)).orElse(null);
            if(!ebook.isAvailable()) {
                return "error/book-is-not-available";
            }
            EbookLoan ebookLoan = getLoan(ebook, visitor);
            ebookLoanRepository.save(ebookLoan);
        } else {
            return "error/book-or-no-active-librarycard";
        }

        return "loan/loan-success";

    }



    private boolean doesVisitorHaveActiveLibraryCard(Visitor visitor) {
        return visitor.getActiveLibraryCard() != null;
    }

    private BookLoan getLoan(Book book, Visitor visitor) {
        BookLoan currentLoanToRegister = new BookLoan();
        Calendar calendar = Calendar.getInstance();
        LibraryCard libraryCard = visitor.getActiveLibraryCard();
        book.setAvailable(false);
        currentLoanToRegister.setBook(book);
        currentLoanToRegister.setDateLoanStart(new Date(calendar.getTime().getTime()));
        currentLoanToRegister.setLibraryCard(libraryCard);
        calendar.add(Calendar.MONTH,1 );
        currentLoanToRegister.setDateLoanEnd(new Date(calendar.getTime().getTime()));
        currentLoanToRegister.setBookReturned(false);
        return currentLoanToRegister;
    }

    private EbookLoan getLoan(EBook book, Visitor visitor) {
        EbookLoan currentLoanToRegister = new EbookLoan();
        Calendar calendar = Calendar.getInstance();
        LibraryCard libraryCard = visitor.getActiveLibraryCard();
        book.setAvailable(false);
        currentLoanToRegister.setEbook(book);
        currentLoanToRegister.setDateLoanStart(new Date(calendar.getTime().getTime()));
        currentLoanToRegister.setLibraryCard(libraryCard);
        calendar.add(Calendar.MONTH,1 );
        currentLoanToRegister.setDateLoanEnd(new Date(calendar.getTime().getTime()));
        currentLoanToRegister.setEbookReturned(false);
        return currentLoanToRegister;
    }
}
package ua.baklanov.springcourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.baklanov.springcourse.models.Book;
import ua.baklanov.springcourse.models.Person;
import ua.baklanov.springcourse.service.BookService;
import ua.baklanov.springcourse.service.PeopleService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "false") boolean sort) {
        model.addAttribute("books", bookService.findBookWithPaginationAndSorting(page, size, sort));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model,
                       @ModelAttribute("person") Person person) {
        Optional<Person> bookOwner = bookService.getBookOwner(id);

        if  (bookService.getBookOwner(id).isPresent()) {
            Person owner = bookOwner.get();
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("people", peopleService.findAll());
        }

        model.addAttribute("book", bookService.findById(id));

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book Book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book Book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        bookService.save(Book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        bookService.update(book, id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(Model model) {
        List<Book> books = bookService.findAll(); // Получить список всех книг
        model.addAttribute("books", books);
        return "books/search";
    }

    @PostMapping("/search")
    public String performSearch(Model model, @RequestParam("title") String title) {
        List<Book> books = bookService.searchBookByTitleStartingWith(title); // Поиск книги по названию
        model.addAttribute("booksSearch", books);
        return "books/search";
    }
}

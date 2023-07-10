package ua.baklanov.springcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.baklanov.springcourse.models.Book;
import ua.baklanov.springcourse.models.Person;
import ua.baklanov.springcourse.repository.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update (Book updateBook, int id) {
        updateBook.setId(id);
        bookRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id) {
        return bookRepository.findById(id)
                .map(Book::getOwner);
    }

    public void release(int id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setOwner(null);
            book.setTakeAt(null);
            bookRepository.save(book);
        }
    }


    public void assign(int id, Person selectedPerson) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setOwner(selectedPerson);
            book.setTakeAt(new Date());
            bookRepository.save(book);
        }
    }

    public Page<Book> findBookWithPaginationAndSorting(int offset, int pageSize, boolean sortByYear) {
        Sort sort = sortByYear ? Sort.by("year") : Sort.unsorted();
        Page<Book> books = bookRepository.findAll(PageRequest.of(offset, pageSize, sort));

        return books;
    }

    public List<Book> searchBookByTitleStartingWith(String titleSearch) {
        return bookRepository.searchBookByTitleStartingWith(titleSearch);
    }
}

package ua.baklanov.springcourse.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.baklanov.springcourse.models.Book;
import ua.baklanov.springcourse.models.Person;
import ua.baklanov.springcourse.repository.BookRepository;
import ua.baklanov.springcourse.repository.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BookRepository bookRepository) {
        this.peopleRepository = peopleRepository;
        this.bookRepository = bookRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return peopleRepository.findById(id);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update (Person updatePerson, int id) {
        updatePerson.setId(id);
        peopleRepository.save(updatePerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }


    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public List<Book> getBooksByPersonId (int id) {
        Optional<Person> personOptional = peopleRepository.findById(id);
        boolean status;

        if (personOptional.isPresent()) {
            Hibernate.initialize(personOptional.get().getBooks());

            List<Book> books = bookRepository.getBookByOwnerId(personOptional.get().getId());
            for (Book book : books) {
                long ld = Math.abs(book.getTakeAt().getTime() - new Date().getTime());
                if (ld > 864000000) {
                    book.setStatus(true);
                }
            }

            return bookRepository.getBookByOwnerId(personOptional.get().getId());
        } else {
            return Collections.emptyList();
        }
    }
}
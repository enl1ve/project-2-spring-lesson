package ua.baklanov.springcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.baklanov.springcourse.models.Book;
import ua.baklanov.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> getBookByOwnerId(int id);

    List<Book> searchBookByTitleStartingWith(String startTitle);
}

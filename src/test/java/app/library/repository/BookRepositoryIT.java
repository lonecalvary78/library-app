package app.library.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import app.library.model.entity.Book;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookRepositoryIT {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Test find by ISBN")
    void findByIsbn_ReturnsBookWithMatchingIsbn() {
        Book book1 = new Book();
        book1.setTitle("Test Book 1");
        book1.setAuthor("Test Author 1");
        book1.setIsbn("0306406152");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Test Book 2");
        book2.setAuthor("Test Author 2");
        book2.setIsbn("0131495050");
        bookRepository.save(book2);

        List<Book> result = bookRepository.findByIsbn("0306406152");

        assertEquals(1, result.size());
        assertEquals("Test Book 1", result.get(0).getTitle());
        assertEquals("Test Author 1", result.get(0).getAuthor());
    }

    @Test
    @DisplayName("Test find by title containing ignore case")
    void findByTitleContainingIgnoreCase_ReturnsMatchingBooks() {
        Book book1 = new Book();
        book1.setTitle("Java Programming");
        book1.setAuthor("Author 1");
        book1.setIsbn("0306406152");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Python Basics");
        book2.setAuthor("Author 2");
        book2.setIsbn("0131495050");
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("Advanced Java");
        book3.setAuthor("Author 3");
        book3.setIsbn("0393040029");
        bookRepository.save(book3);

        List<Book> result = bookRepository.findByTitleContainingIgnoreCase("java");

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(b -> b.getTitle().equals("Java Programming")));
        assertTrue(result.stream().anyMatch(b -> b.getTitle().equals("Advanced Java")));
    }

    @Test
    @DisplayName("Test exists by ISBN and title and author")
    void existsByIsbnAndTitleAndAuthor_ReturnsTrueWhenExists() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("0306406152");
        bookRepository.save(book);

        boolean exists = bookRepository.existsByIsbnAndTitleAndAuthor("0306406152", "Test Book", "Test Author");

        assertTrue(exists);
    }

    @Test

    void existsByIsbnAndTitleAndAuthor_ReturnsFalseWhenDoesNotExist() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("0306406152");
        bookRepository.save(book);

        boolean exists = bookRepository.existsByIsbnAndTitleAndAuthor("0306406152", "Different Title", "Test Author");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Test find by id")
    void findById_ReturnsBookWithMatchingId() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("0306406152");
        Book savedBook = bookRepository.save(book);

        Optional<Book> result = bookRepository.findById(savedBook.getId());

        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        assertEquals("Test Author", result.get().getAuthor());
        assertEquals("0306406152", result.get().getIsbn());
    }
} 
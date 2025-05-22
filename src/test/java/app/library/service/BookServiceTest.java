package app.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.library.exception.ResourceAlreadyExistsException;
import app.library.exception.ResourceNotFoundException;
import app.library.model.dto.BookRequestDTO;
import app.library.model.dto.BookResponseDTO;
import app.library.model.entity.Book;
import app.library.repository.BookRepository;
import app.library.repository.BorrowerRepository;
import app.library.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookRequestDTO bookRequestDTO;
    private Book book;

    @BeforeEach
    void setUp() {
        bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("Test Book");
        bookRequestDTO.setAuthor("Test Author");
        bookRequestDTO.setIsbn("1234567890");

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
    }

    @Test
    @DisplayName("Test create book success")
    void createBook_Success() {
        when(bookRepository.existsByIsbnAndTitleAndAuthor(
                bookRequestDTO.getIsbn(),
                bookRequestDTO.getTitle(),
                bookRequestDTO.getAuthor())).thenReturn(false);
        when(bookRepository.findByIsbn(bookRequestDTO.getIsbn())).thenReturn(List.of());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO result = bookService.createBook(bookRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Book", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        assertEquals("1234567890", result.getIsbn());
        assertFalse(result.isBorrowed());

        verify(bookRepository, times(1)).existsByIsbnAndTitleAndAuthor(
                bookRequestDTO.getIsbn(),
                bookRequestDTO.getTitle(),
                bookRequestDTO.getAuthor());
        verify(bookRepository, times(1)).findByIsbn(bookRequestDTO.getIsbn());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Test create book existing ISBN with different title or author")
    void createBook_ExistingISBNWithDifferentTitleOrAuthor() {
        Book existingBook = new Book();
        existingBook.setTitle("Different Title");
        existingBook.setAuthor("Test Author");
        existingBook.setIsbn("1234567890");

        when(bookRepository.existsByIsbnAndTitleAndAuthor(
                bookRequestDTO.getIsbn(),
                bookRequestDTO.getTitle(),
                bookRequestDTO.getAuthor())).thenReturn(false);
        when(bookRepository.findByIsbn(bookRequestDTO.getIsbn())).thenReturn(List.of(existingBook));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            bookService.createBook(bookRequestDTO);
        });

        verify(bookRepository, times(1)).existsByIsbnAndTitleAndAuthor(
                bookRequestDTO.getIsbn(),
                bookRequestDTO.getTitle(),
                bookRequestDTO.getAuthor());
        verify(bookRepository, times(1)).findByIsbn(bookRequestDTO.getIsbn());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Test get all books")
    void getAllBooks_ReturnsAllBooks() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("1111111111");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("2222222222");

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<BookResponseDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test get book by id existing id")
    void getBookById_ExistingId_ReturnsBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponseDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Book", result.getTitle());

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test get book by id non existing id")
    void getBookById_NonExistingId_ThrowsException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(99L);
        });

        verify(bookRepository, times(1)).findById(99L);
    }
} 
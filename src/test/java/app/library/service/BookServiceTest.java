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
        // Setup test data
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
    void createBook_Success() {
        // Given
        when(bookRepository.existsByIsbnAndTitleAndAuthor(
                bookRequestDTO.getIsbn(),
                bookRequestDTO.getTitle(),
                bookRequestDTO.getAuthor())).thenReturn(false);
        when(bookRepository.findByIsbn(bookRequestDTO.getIsbn())).thenReturn(List.of());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        BookResponseDTO result = bookService.createBook(bookRequestDTO);

        // Then
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
    void createBook_ExistingISBNWithDifferentTitleOrAuthor() {
        // Given
        Book existingBook = new Book();
        existingBook.setTitle("Different Title");
        existingBook.setAuthor("Test Author");
        existingBook.setIsbn("1234567890");

        when(bookRepository.existsByIsbnAndTitleAndAuthor(
                bookRequestDTO.getIsbn(),
                bookRequestDTO.getTitle(),
                bookRequestDTO.getAuthor())).thenReturn(false);
        when(bookRepository.findByIsbn(bookRequestDTO.getIsbn())).thenReturn(List.of(existingBook));

        // When & Then
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
    void getAllBooks_ReturnsAllBooks() {
        // Given
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

        // When
        List<BookResponseDTO> result = bookService.getAllBooks();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_ExistingId_ReturnsBook() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // When
        BookResponseDTO result = bookService.getBookById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Book", result.getTitle());

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_NonExistingId_ThrowsException() {
        // Given
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(99L);
        });

        verify(bookRepository, times(1)).findById(99L);
    }
} 
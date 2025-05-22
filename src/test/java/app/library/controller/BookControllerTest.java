package app.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.library.model.dto.BookRequestDTO;
import app.library.model.dto.BookResponseDTO;
import app.library.service.BookService;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    public void testCreateBook() throws Exception {
        BookRequestDTO requestDTO = new BookRequestDTO();
        requestDTO.setTitle("Test Book");
        requestDTO.setAuthor("Test Author");
        requestDTO.setIsbn("0306406152");

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Test Book");
        responseDTO.setAuthor("Test Author");
        responseDTO.setIsbn("0306406152");
        responseDTO.setBorrowed(false);
        responseDTO.setCreatedAt(Instant.now());

        when(bookService.createBook(any(BookRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.isbn").value("0306406152"))
                .andExpect(jsonPath("$.borrowed").value(false));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        BookResponseDTO book1 = new BookResponseDTO();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("0131495050");
        book1.setBorrowed(false);
        book1.setCreatedAt(Instant.now());

        BookResponseDTO book2 = new BookResponseDTO();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("0306406152");
        book2.setBorrowed(false);
        book2.setCreatedAt(Instant.now());

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Book 2"));
    }
} 
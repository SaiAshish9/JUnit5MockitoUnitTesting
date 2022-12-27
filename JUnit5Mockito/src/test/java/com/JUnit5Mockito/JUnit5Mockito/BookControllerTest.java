package com.JUnit5Mockito.JUnit5Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    private static MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookController bookController;

    Book RECORD_1 = new Book(1L, "Sai", "Ashish", 180);
    Book RECORD_2 = new Book(2L, "Sai9", "Ashish9", 160);
    Book RECORD_3 = new Book(3L, "Sai8", "Ashish8", 120);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllBookRecords_success() throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
        Mockito.when(bookRepository.findAll()).thenReturn(records);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Sai")))
                .andExpect(jsonPath("$[1].name", is("Sai9")))
                .andExpect(jsonPath("$[2].name", is("Sai8")));
    }

    @Test
    public void getBookById_success() throws Exception {
        Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.of(RECORD_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Sai")));
    }

    @Test
    public void createBookRecord_success() throws Exception {
        Book record = Book.builder()
                .bookId(4L)
                .name("Sai7")
                .summary("Ashish7")
                .rating(9)
                .build();
        Mockito.when(bookRepository.save(record)).thenReturn(record);
        String content = objectWriter.writeValueAsString(record);
        MockHttpServletRequestBuilder mockReq = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockReq)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Sai7")));
    }

    @Test
    public void updateBookRecord_success() throws Exception {
        Book updatedRecord = Book.builder()
                .bookId(1L)
                .name("Sai1")
                .summary("Ashish1")
                .rating(1)
                .build();
        Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.of(RECORD_1));
        Mockito.when(bookRepository.save(updatedRecord)).thenReturn(updatedRecord);
        String content = objectWriter.writeValueAsString(updatedRecord);
        MockHttpServletRequestBuilder mockReq = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockReq)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Sai1")));
    }

    @Test
    public void deleteBookById_success() throws Exception {
        Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.of(RECORD_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBookById_failure(){
        Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.empty());
        Assertions
                .assertThatThrownBy(
                        () -> mockMvc.perform(MockMvcRequestBuilders.delete("/book/1").contentType(MediaType.APPLICATION_JSON)))
                .hasCauseInstanceOf(Exception.class).hasMessageContaining("bookId : 1 not present");
        }
}
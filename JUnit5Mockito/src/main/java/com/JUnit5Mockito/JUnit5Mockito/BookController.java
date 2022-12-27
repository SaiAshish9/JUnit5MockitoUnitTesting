package com.JUnit5Mockito.JUnit5Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController {
//    option + return

    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBookRecords() {
        return bookRepository.findAll();
    }

    @GetMapping(value = "{bookId}")
    public Book getBookById(@PathVariable(value = "bookId") Long bookId) {
        return bookRepository.findById(bookId).get();
    }

    @PostMapping
    public Book createBookRecord(@RequestBody @Validated Book bookRecord) {
        return bookRepository.save(bookRecord);
    }

    @PutMapping
    public Book updateBookRecord(@RequestBody @Validated Book bookRecord) throws ClassNotFoundException {
        if (bookRecord == null || bookRecord.getBookId() == null) {
            throw new ClassNotFoundException("Must Not Be Null");
        }
        Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());
        if (!optionalBook.isPresent())
            throw new ClassNotFoundException("Book with ID: " + bookRecord.getBookId() + " not found ");
        Book existingBookRecord = optionalBook.get();
        existingBookRecord.setName(bookRecord.getName());
        existingBookRecord.setSummary(bookRecord.getSummary());
        existingBookRecord.setRating(bookRecord.getRating());
        return bookRepository.save(existingBookRecord);
    }

    @DeleteMapping(value = "{bookId}")
    public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws Exception {
        if(!bookRepository.findById(bookId).isPresent())
            throw new Exception("bookId : " + bookId + " not present");
        bookRepository.deleteById(bookId);
    }

}

package com.vvit.bookapp.controller;

import com.vvit.bookapp.model.Book;
import com.vvit.bookapp.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    // Constructor injection of the BookService
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Integer id, @RequestBody Book bookDetails) {
        return bookService.updateBook(id, bookDetails);
    }
    @DeleteMapping("/{id}")
    public Book deleteBook(@PathVariable Integer id) {
        return bookService.deleteBook(id);
    }
}

package com.vvit.bookapp.service;

import com.vvit.bookapp.model.Book;
import com.vvit.bookapp.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService implements IBookService {
    private final BookRepository bookRepository;
    // Constructor injection of the BookRepository
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //create a book
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }
    //get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    //get a book by id
    public Book getBookById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    //update a book
    public Book updateBook(Integer id, Book bookDetails) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPrice(bookDetails.getPrice());
            return bookRepository.save(book);
        }
        return null;
    }
    //delete a book
    public Book deleteBook(Integer id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            bookRepository.delete(book);
            return book;
        }
        return null;
    }
}

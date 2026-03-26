package com.vvit.bookapp.service;

import com.vvit.bookapp.model.Book;

import java.util.List;

public interface IBookService {
        public List<Book> getAllBooks();
        public Book getBookById(Integer id);
        public Book createBook(Book book);
        public Book updateBook(Integer id, Book book);
        public Book deleteBook(Integer id);
}

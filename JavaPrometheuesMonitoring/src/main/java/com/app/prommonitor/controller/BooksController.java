package com.app.prommonitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.prommonitor.beans.Book;
import com.app.prommonitor.exception.BookNotFoundException;
import com.app.prommonitor.service.BooksService;
import com.app.prommonitor.util.MetricUtil;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;


@RestController
//@RequiredArgsConstructor
//@RequestMapping(value = "/api/books")
public class BooksController extends MetricUtil{

	@Autowired
    private BooksService booksService;
	
	@Autowired
    private MeterRegistry meterRegistry;

    @GetMapping(value = "/books")
    public ResponseEntity<List<Book>> getBooks(@RequestParam(required = false) String title) throws InterruptedException {
        List<Book> books = booksService.findByTitle(title);

        Counter counter = Counter.builder(MetricUtil.METRIC_API_BOOKS_GET_COUNT)
            .tag(MetricUtil.TAG_TITLE, MetricUtil.getTagTitle(title))
            .tag(MetricUtil.TAG_MATCHING_BOOKS, String.valueOf(books.size()))
            .description("a number of requests to GET /books endpoint")
            .register(meterRegistry);
        counter.increment();

        return ResponseEntity.ok(books); 
    }

    
    @GetMapping(value = "/booksByAuthor")
    public ResponseEntity<List<Book>> getBooksbyAuthor(@RequestParam(required = true) String author) throws InterruptedException {
        List<Book> books = booksService.findByAuthorName(author);

      /*  Counter counter = Counter.builder(MetricUtil.METRIC_API_BOOKS_GET_COUNT)
            .tag(MetricUtil.TAG_AUTHOR, MetricUtil.getTagAuthor(author))
            .tag(MetricUtil.TAG_MATCHING_BOOKS, String.valueOf(books.size()))
            .description("a number of requests to GET /getBooksbyAuthorName endpoint")
            .register(meterRegistry);
        counter.increment(); */

        return ResponseEntity.ok(books); 
    }
    
    @PostMapping(value="/books")
	public ResponseEntity<Book> createBook(@RequestBody Book bookDto) {
		Book newBook = booksService.createBook(bookDto);
		return new ResponseEntity<>(newBook, HttpStatus.CREATED);
	}
    
    
    @PutMapping(value="/updateBook")
    public ResponseEntity<Book> updateBook(@RequestBody Book bookDto) throws BookNotFoundException{
		Book newBook = booksService.updateBook(bookDto);
		return new ResponseEntity<>(newBook, HttpStatus.CREATED);
	}    
    
}	
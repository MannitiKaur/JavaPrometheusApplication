package com.app.prommonitor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.app.prommonitor.beans.Book;
import com.app.prommonitor.entity.BooksEntity;
import com.app.prommonitor.exception.BookNotFoundException;
import com.app.prommonitor.mapper.BookMapper;
import com.app.prommonitor.repository.BooksRepository;
import com.app.prommonitor.util.MetricUtil;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;


@Service
public class BooksService extends MetricUtil{

    private final BooksRepository booksRepository;
    private final MeterRegistry meterRegistry;    

    public BooksService(BooksRepository booksRepository, MeterRegistry meterRegistry) {
        this.booksRepository = booksRepository;
        this.meterRegistry = meterRegistry;
    }

   
    public List<Book> findByTitle(String title) throws InterruptedException {
        Tag titleTag = Tag.of(MetricUtil.TAG_TITLE, MetricUtil.getTagTitle(title));
        List<Book> books;
        Timer.Sample timer = Timer.start(meterRegistry);

        if (StringUtils.isEmpty(title)) {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 200));
            books  = BookMapper.maptoBookDTOList(booksRepository.findAll());
        } else {
            if ("FUNDAMENTAL ALGORITHMS".equalsIgnoreCase(title)) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1_000, 3_000));
            } else {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1_000));
            }
            Book book  = BookMapper.mapToBookDTO(booksRepository.findByTitle(title));
            books = new ArrayList<>();
            books.add(book);
        }

        timer.stop(Timer.builder(MetricUtil.METRIC_BOOKS_BY_TITLE_SEARCH)
            .description("Timer showing how long does it take to search for books")
            .tags(List.of(titleTag))
            .register(meterRegistry));

        return books;
    }

    
    public List<Book> findByAuthorName(String author) throws InterruptedException {
    		
    	Tag authorTag = Tag.of(MetricUtil.TAG_AUTHOR, MetricUtil.getTagAuthor(author));
        Timer.Sample timer = Timer.start(meterRegistry);
    	
        if ("ALEX XU".equalsIgnoreCase(author)) {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1_000, 3_000));
        } else {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1_000));
        }
        
        List<Book> books = BookMapper.maptoBookDTOList(booksRepository.findByAuthor(author));
        
        timer.stop(Timer.builder(MetricUtil.METRIC_BOOKS_BY_AUTHOR_SEARCH)
                .description("Timer showing how long does it take to search for books by AuthorName")
                .tags(List.of(authorTag))
                .register(meterRegistry));
        
        return books;
    	
    }
    
    
    public Book createBook(Book bookDto) {
		BooksEntity bookEntity = BookMapper.mapToBookEntity(bookDto);
		BooksEntity createdBook = booksRepository.save(bookEntity);
		return BookMapper.mapToBookDTO(createdBook);
	}
    
    
    public Book updateBook(Book bookDto) throws BookNotFoundException {
		Optional<BooksEntity> retrievedBook = booksRepository.findById(bookDto.getId());
		if (retrievedBook.isEmpty()) {
			throw new BookNotFoundException("Book with id - " + bookDto.getId() + " not found.");
		}
		BooksEntity bookEntity = retrievedBook.get();
		bookEntity.setQuantity(bookDto.getQuantity());
		BooksEntity updatedBook = booksRepository.save(bookEntity);
		
		
		
		return BookMapper.mapToBookDTO(updatedBook);
	}
}
package com.app.prommonitor.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.app.prommonitor.beans.Book;
import com.app.prommonitor.entity.BooksEntity;

public class BookMapper {
	
	public static Book mapToBookDTO(BooksEntity bookEntity) {
		return new Book(bookEntity.getId(), bookEntity.getTitle(),bookEntity.getAuthor(),bookEntity.getPrice(),bookEntity.getQuantity());
	}

	public static BooksEntity mapToBookEntity(Book bookDto) {
		return new BooksEntity(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor(),bookDto.getPrice(),bookDto.getQuantity());
	}

	public static List<Book> maptoBookDTOList(List<BooksEntity> booksEntityList){
		return booksEntityList.stream().map(bookEnity -> mapToBookDTO(bookEnity)).collect(Collectors.toList());
	}
	
}

package com.app.prommonitor.util;

import org.springframework.util.StringUtils;

import lombok.experimental.UtilityClass;

public class MetricUtil {
    protected static String BOOKS_SERVICE_PREFIX = "books_service";
    protected static String BOOKS = "_books";
    protected static String API_BOOKS = BOOKS_SERVICE_PREFIX + "_api" + BOOKS;
    protected static String METRIC_API_BOOKS_GET_COUNT = API_BOOKS + "_get_count";
    protected static String METRIC_API_BOOKS_GET_COUNT_AUTHOR = API_BOOKS + "_get_count_author";
    protected static String METRIC_BOOKS_IN_STORE_COUNT = BOOKS_SERVICE_PREFIX + BOOKS + "_in_store_count";
    protected static String METRIC_BOOKS_STORE_COUNT_BY_AUTHOR = BOOKS_SERVICE_PREFIX + BOOKS + "_store_count_by_author";

    protected static String METRIC_BOOKS_BY_TITLE_SEARCH = BOOKS_SERVICE_PREFIX + BOOKS + "_search_by_title";
    protected static String METRIC_BOOKS_BY_AUTHOR_SEARCH = BOOKS_SERVICE_PREFIX + BOOKS + "_search_by_author";
    protected static String TAG_TITLE = "title";
    protected static String TAG_AUTHOR = "author";
    protected static String TAG_MATCHING_BOOKS = "matching_books";

    protected static String getTagTitle(String title) {
        return StringUtils.isEmpty(title) ? "all" : title;
    }
    
    protected static String getTagAuthor(String author) {
        return author;
    }
    
}
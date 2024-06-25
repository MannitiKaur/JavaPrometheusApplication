package com.app.prommonitor.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.prommonitor.entity.BooksEntity;
import com.app.prommonitor.repository.BooksRepository;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

@Component
public class GaugeMetrics implements MeterBinder {

	@Autowired
	private BooksRepository booksRepository;

	@Autowired
	private MeterRegistry meterRegistry;

	private Supplier<Number> totalBooks;
	
	private Map<String, Integer> totalBooksByAuthor;

	@Override
	public void bindTo(MeterRegistry registry) {

		// Total Books available will be executed every 15s - Prometheus scrape_interval
		// setting
		Gauge.builder(MetricUtil.METRIC_BOOKS_IN_STORE_COUNT, getTotalBooksCount())
				.description("A current number of books in store").register(meterRegistry);

		// Total count of books available by author
		totalBooksByAuthor = getTotalBooksCountByAuthor();

		for (Map.Entry<String, Integer> entry : totalBooksByAuthor.entrySet()) {
			String key = entry.getKey();
			Integer booksCount = entry.getValue();
			Tags tag = Tags.of("author", key);
					
			Gauge.builder(MetricUtil.METRIC_BOOKS_STORE_COUNT_BY_AUTHOR, booksCount, value -> value.doubleValue())
						 .tags(tag)
						 .description("Total number of books in store available by author")
						 .register(meterRegistry);
		}
		
		Metrics.addRegistry(meterRegistry);
	}
	

	@Scheduled(fixedDelay = 15000)
	public void measure() {
		
		System.out.println(
			      "Fixed delay task - " + System.currentTimeMillis() / 1000);
		
		Metrics.gauge(MetricUtil.METRIC_BOOKS_IN_STORE_COUNT, getTotalBooksCount().get());
		
		// Total count of books available by author
		totalBooksByAuthor = getTotalBooksCountByAuthor();

		for (Map.Entry<String, Integer> entry : totalBooksByAuthor.entrySet()) {
			Integer booksCount = entry.getValue();			
			Metrics.gauge(MetricUtil.METRIC_BOOKS_STORE_COUNT_BY_AUTHOR, booksCount, value -> value.doubleValue());
		}
		
	}

	
	private Supplier<Number> getTotalBooksCount() {
		totalBooks = () -> booksRepository.findAll().stream()
				.mapToInt(bookEntity -> Integer.parseInt(bookEntity.getQuantity())).reduce(0, (a, b) -> a + b);
		return totalBooks;
	}
	

	private Map<String, Integer> getTotalBooksCountByAuthor() {

		return booksRepository.findAll().stream().collect(Collectors.toMap(BooksEntity::getAuthor,
				booksEntity -> Integer.parseInt(booksEntity.getQuantity()), Integer::sum));
	}

}

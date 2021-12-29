package io.tetrate.web.service;

import io.tetrate.web.domain.MarketSummary;
import io.tetrate.web.domain.Quote;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MarketSummaryService {
	private static final Logger logger = LoggerFactory.getLogger(MarketSummaryService.class);
	@Value("${pivotal.summary.quotes:3}")
	private Integer numberOfQuotes;
	
	//10 minutes in milliseconds
	@Value("${tetrate.summary.refresh:600000}")
	private final static String refreshPeriod = "600000";
	
	@Value("${tetrate.summary.symbols.it:EMC,IBM,VMW}")
	private String symbolsIT;
    @Value("${tetrate.summary.symbols.fs:JPM,C,MS}")
	private String symbolsFS;

    private final io.tetrate.web.domain.MarketSummary summary = new MarketSummary();
    
    @Autowired
	private QuoteService marketService;
	
	public MarketSummary getMarketSummary() {
		logger.debug("Retrieving Market Summary: {}", summary);
		if (!summary.isInitialised()) {
			retrieveMarketSummary();
		}
		return summary;
	}
	
	@Scheduled(fixedRateString = refreshPeriod, initialDelay = 5000)
	protected void retrieveMarketSummary() {
		logger.debug("Scheduled retrieval of Market Summary");
		summary.setTopGainers(getTopThree(symbolsIT));
		summary.setTopLosers(getTopThree(symbolsFS));

		//Testing...
		summary.getTopGainers();
		summary.getTopLosers();

	}

	/**
	 * Retrieve the list of top winners/losers.
	 * Currently retrieving list of 3 random.
	 */
	private List<Quote> getTopThree(String symbols) {
		StringBuilder builder = new StringBuilder();
		for(Iterator<String> i = pickRandomThree(Arrays.asList(symbols.split(","))).iterator(); i.hasNext();) {
			builder.append(i.next());
			if (i.hasNext()) {
				builder.append(",");
			}
		}
		return marketService.getMultipleQuotes(builder.toString());
	}
	
	private List<String> pickRandomThree(List<String> symbols) {
		Collections.shuffle(symbols);
	    return symbols.subList(0, numberOfQuotes);
	}
}
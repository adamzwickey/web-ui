package io.tetrate.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.tetrate.web.domain.CompanyInfo;
import io.tetrate.web.domain.Quote;

import java.util.*;

@Service
@RefreshScope
public class QuoteService {
	private static final Logger LOG = LoggerFactory.getLogger(QuoteService.class);

    @Value("${tetrate.quoteServiceUrl}")
	private String quotesServiceUrl;

    @Bean
    RestTemplate restTemplate(){
      return new RestTemplate();
    }
	
	//@HystrixCommand(fallbackMethod = "getQuoteFallback")
	public Quote getQuote(String symbol) {
		LOG.debug("Fetching quote: {}", symbol);
		List<Quote> quotes = getMultipleQuotes(symbol);
		if (quotes.size() == 1 ) {
			LOG.debug("Fetched quote: " + quotes.get(0));
			return quotes.get(0);
		}
		LOG.debug("exception: should only be 1 quote and got multiple or zero: " + quotes.size());
		return new Quote();
	}
	
	private Quote getQuoteFallback(String symbol) {
		LOG.info("Fetching fallback quote for: {}", symbol);
		Quote quote = new Quote();
		quote.setSymbol(symbol);
		//quote.setStatus("FAILED");
		return quote;
	}

	//@HystrixCommand(fallbackMethod = "getCompaniesFallback")
	public List<CompanyInfo> getCompanies(String name) {
		LOG.info("Fetching companies with name or symbol matching: {}", name);
		CompanyInfo[] infos = restTemplate().getForObject(quotesServiceUrl + "/v1/company/{name}", CompanyInfo[].class, name);
        List<CompanyInfo> companyInfoList = Arrays.asList(infos);
		return companyInfoList;
	}
	private List<CompanyInfo> getCompaniesFallback(String name) {
		List<CompanyInfo> infos = new ArrayList<>();
		return infos;
	}
	/**
	 * Retrieve multiple quotes.
	 * 
	 * @param symbols comma separated list of symbols.
	 * @return
	 */
	public List<Quote> getMultipleQuotes(String symbols) {
		LOG.info("retrieving multiple quotes: {}", symbols);
		Quote[] quotesArr = restTemplate().getForObject(quotesServiceUrl + "/v1/quotes?q={symbols}", Quote[].class, symbols);
		List<Quote> quotes = Arrays.asList(quotesArr);
		LOG.info("Received quotes: {}",quotes);
		return quotes;
		
	}
	/**
	 * Retrieve multiple quotes.
	 * 
	 * @param symbols
	 * @return
	 */
	public List<Quote> getMultipleQuotes(String[] symbols) {
		LOG.info("Fetching multiple quotes array: {} ",Arrays.asList(symbols));		
		return getMultipleQuotes(Arrays.asList(symbols));
	}
	/**
	 * Retrieve multiple quotes.
	 * 
	 * @param symbols
	 * @return
	 */
	public List<Quote> getMultipleQuotes(Collection<String> symbols) {
		LOG.info("Fetching multiple quotes array: {} ",symbols);
		StringBuilder builder = new StringBuilder();
		for (Iterator<String> i = symbols.iterator(); i.hasNext();) {
			builder.append(i.next());
			if (i.hasNext()) {
				builder.append(",");
			}
		}
		return getMultipleQuotes(builder.toString());
	}

}
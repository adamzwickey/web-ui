package io.tetrate.web.controller;

import io.tetrate.web.domain.*;
import io.tetrate.web.service.AccountService;
import io.tetrate.web.service.FlashService;
import io.tetrate.web.service.MarketSummaryService;
import io.tetrate.web.service.PortfolioService;
import io.tetrate.web.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ROLE_TRADE')")
public class TradeController {

	private static final Logger LOG = LoggerFactory.getLogger(TradeController.class);
	
	@Autowired
	private QuoteService quoteService;

	@Autowired
	private MarketSummaryService marketService;
	
	@Autowired
	private PortfolioService portfolioService;
	
	@Autowired
	private AccountService accountService;

	@ModelAttribute("accounts")
	public List<Account> accounts(@AuthenticationPrincipal OAuth2User principal) {
		return accountService.getAccounts(principal.getName(), principal.getAttribute("accessToken"));
	}

	@ModelAttribute("portfolio")
	public Portfolio portfolio(@AuthenticationPrincipal OAuth2User principal) {
		return portfolioService.getPortfolio(principal);
	}

	@ModelAttribute("search")
	public Search search() {
		return new Search();
	}

	@ModelAttribute("order")
	public Order order() {
		return new Order();
	}

	
	@GetMapping(value = "/trade")
	public String showTrade(Model model) {
		return "trade";
	}

	@PostMapping(value = "/trade")
	public String showTrade(Model model, @ModelAttribute("search") Search search) {
		LOG.debug("/trade.POST - symbol: " + search.getName());
		
		model.addAttribute("marketSummary", marketService.getMarketSummary());
		model.addAttribute("search", search);
		
		if (search.getName() == null || search.getName().equals("") ) {
			model.addAttribute("quotes", new ArrayList<Quote>());
		} else {
			List<Quote> newQuotes = getQuotes(search.getName());
			model.addAttribute("quotes", newQuotes);
		}

		return "trade";
	}
	
	@PostMapping(value = "/order")
	public String buy(Model model, @ModelAttribute("order") Order order, @AuthenticationPrincipal OAuth2User principal) {
		order.setCompletionDate(new Date());
		Order result = portfolioService.sendOrder(order, principal);
		return FlashService.redirectWithMessage("/trade", "Order successful!");
	}
	
	
	private List<Quote> getQuotes(String companyName) {
		LOG.debug("Fetching quotes for companies that have: " + companyName + " in name or symbol");
		List<CompanyInfo> companies = quoteService.getCompanies(companyName);
		
		/*
		 * Sleuth currently doesn't work with parallelStreams
		 */
		//get distinct companyinfos and get their respective quotes in parallel.

		List<String> symbols = companies.stream().map(company -> company.getSymbol()).collect(Collectors.toList());
		LOG.debug("symbols: fetching "+ symbols.size() + " quotes for following symbols: " + symbols);
		List<String> distinctsymbols = symbols.stream().distinct().collect(Collectors.toList());
		LOG.debug("distinct: fetching "+ distinctsymbols.size() + " quotes for following symbols: " + distinctsymbols);
		List<Quote> quotes = quoteService.getMultipleQuotes(distinctsymbols)
				.stream()
				.distinct()
				.filter(quote -> quote.getName() != null && !"".equals(quote.getName()))
				.collect(Collectors.toList());
		LOG.debug("Looked up quotes: {}", quotes);
		return quotes;
	}
	
}
package io.tetrate.web.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import io.tetrate.web.domain.Account;
import io.tetrate.web.domain.Order;
import io.tetrate.web.domain.Search;
import io.tetrate.web.service.AccountService;
import io.tetrate.web.service.FlashService;
import io.tetrate.web.service.MarketSummaryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("hasAuthority('ROLE_ACCOUNT')")
public class AccountsController {
	private static final Logger LOG = LoggerFactory.getLogger(AccountsController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MarketSummaryService summaryService;
	
	@GetMapping(value = "/accounts")
	public String accounts(Model model, @AuthenticationPrincipal OAuth2User principal) {
		LOG.debug("/accounts");
		model.addAttribute("marketSummary", summaryService.getMarketSummary());
		model.addAttribute("accounts",accountService.getAccounts(principal.getName(), principal.getAttribute("accessToken")));
		return "accounts";
	}
	
	@GetMapping(value = "/openaccount")
	public String openAccount(Model model) {
		Account account = new Account();
		account.setOpenbalance(new BigDecimal(100000));
		model.addAttribute("newaccount",account);
		return "openaccount";
	}
	
	@PostMapping(value = "/openaccount")
	public String saveAccount(Model model,@ModelAttribute(value="newaccount") Account account, @AuthenticationPrincipal OAuth2User principal) {
		LOG.debug("saveAccounts: creating account: {}", account);
		account.setBalance(account.getOpenbalance());
		account.setCreationdate(new Date());
        account.setUserid(principal.getName());
		
		LOG.info("saveAccounts: saving account: {}", account);		
		accountService.createAccount(account, principal.getAttribute("accessToken"));
		return FlashService.redirectWithMessage("/accounts", String.format("Account '%s' created successfully", account.getName()));
	}

}
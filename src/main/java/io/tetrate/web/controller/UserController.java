package io.tetrate.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.tetrate.web.service.MarketSummaryService;

import java.security.Principal;

@Controller
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
	private MarketSummaryService summaryService;
	
	@GetMapping(value = "/")
	public String showHome(Model model,Principal princ,  @AuthenticationPrincipal OAuth2User principal){
        LOG.info("Retrieving home page");
		//check if user is logged in!
		if (principal != null) {
            LOG.info("User is logged in: {}", principal);
			return "redirect:/home";
		}
        LOG.info("User is NOT logged in");
		model.addAttribute("marketSummary", summaryService.getMarketSummary());
		return "index";
	}

	@GetMapping("/home")
	public String authorizedHome(Model model, @AuthenticationPrincipal OAuth2User principal, @RegisteredOAuth2AuthorizedClient("tetratebank") OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        LOG.info("Retrieving authorized home page");
		model.addAttribute("marketSummary", summaryService.getMarketSummary());
		String currentUserName = principal.getName();
		LOG.debug("User logged in: {}", currentUserName);
		// model.addAttribute("accounts",accountService.getAccounts(oAuth2AuthorizedClient));
		// model.addAttribute("portfolio",portfolioService.getPortfolio(oAuth2AuthorizedClient));
		// model.addAttribute("user", userService.getUser(currentUserName, oAuth2AuthorizedClient, principal));
		return "index";
	}


	@GetMapping(value = "/registration")
	public String registration(Model model) {
        LOG.info("Preparing to register user");
		//model.addAttribute("registration", new RegistrationRequest());
		return "registration";
	}

	// @RequestMapping(value = "/registration", method = RequestMethod.POST)
	// public String register(Model model, @Valid @ModelAttribute(value="registration") RegistrationRequest registrationRequest,
	// 					   BindingResult bindingResult) {
	// 	log.info("register: user:" + registrationRequest.getEmail());
	// 	if (bindingResult.hasErrors()) {
	// 		model.addAttribute("errors", bindingResult);
	// 		return "registration";
	// 	}
	// 	this.userService.registerUser(registrationRequest);
	// 	return FlashService.redirectWithMessage( "/", String.format("User %s successfully registered", registrationRequest.getEmail()));
	// }
}
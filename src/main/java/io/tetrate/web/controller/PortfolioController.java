package io.tetrate.web.controller;

import io.tetrate.web.domain.Order;
import io.tetrate.web.service.AccountService;
import io.tetrate.web.service.MarketSummaryService;
import io.tetrate.web.service.PortfolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;

@Controller
@PreAuthorize("hasAuthority('ROLE_PORTFOLIO')")
public class PortfolioController {
    private static final Logger LOG = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private MarketSummaryService summaryService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/portfolio", method = RequestMethod.GET)
    public String portfolio(Model model, @AuthenticationPrincipal OAuth2User principal) {
        LOG.debug("/portfolio");
        model.addAttribute("marketSummary", summaryService.getMarketSummary());
        model.addAttribute("portfolio", portfolioService.getPortfolio(principal));
        model.addAttribute("accounts", accountService.getAccounts(principal.getName(), principal.getAttribute("accessToken")));
        model.addAttribute("order", new Order());
        return "portfolio";
    }

}
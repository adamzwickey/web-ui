package io.tetrate.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.tetrate.web.domain.Order;
import io.tetrate.web.domain.Portfolio;
import io.tetrate.web.exception.OrderNotSavedException;



@Service
public class PortfolioService {
	private static final Logger LOG = LoggerFactory.getLogger(PortfolioService.class);

    @Value("${tetrate.portfolioServiceUrl}")
	private String _url;

    @Bean
    RestTemplate restTemplate(){
      return new RestTemplate();
    }

	public Order sendOrder(Order order, OAuth2User principal ) {
		LOG.debug("send order: {}", order);

		HttpHeaders headers = new HttpHeaders();
        HttpEntity<Order> entity = new HttpEntity(order, headers);
        headers.add("Authorization", "Bearer " + principal.getAttribute("accessToken"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Order> result = restTemplate().exchange(_url + "/portfolio", 
                            HttpMethod.POST, 
                            entity, 
                            Order.class);
		// ResponseEntity<Order>  result = restTemplate().postForEntity(_url + "/portfolio", order, Order.class);
		if (result.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new OrderNotSavedException("Could not save the order");
		}
		LOG.debug("Order saved:: {}", result.getBody());
		return result.getBody();
	}

	public Portfolio getPortfolio(OAuth2User user ) {
        LOG.debug("Retreiving Portfolio for user {}", user);

		HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity("", headers);
        headers.add("Authorization", "Bearer " + user.getAttribute("accessToken"));
        //headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Portfolio> folio = restTemplate().exchange(_url + "/portfolio", 
                            HttpMethod.GET, 
                            entity, 
                            Portfolio.class);

		//Portfolio folio = restTemplate().getForObject(_url + "/portfolio", Portfolio.class);
		LOG.debug("Portfolio received: {}", folio);
		return folio.getBody();
	}
}
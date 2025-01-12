package io.tetrate.web.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.tetrate.web.domain.Account;
import io.tetrate.web.domain.RegistrationRequest;
import io.tetrate.web.domain.User;

@Service
public class AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Value("${tetrate.accountServiceUrl}")
    private String accountServiceUrl;

    @Bean
    RestTemplate restTemplate(){
      return new RestTemplate();
    }

    public void registerUser(RegistrationRequest registrationRequest) {
        LOG.debug("Creating user with userId: {}", registrationRequest.getEmail());

        User user = restTemplate().postForObject(accountServiceUrl + "/register", 
                        registrationRequest, User.class);
        LOG.info("Status from registering account for {}" + registrationRequest.getEmail() + " is " + user.getId());
    }

    public void createAccount(Account account, String token) {
        LOG.debug("Creating account {}", account);
        LOG.debug("\t with Token {}", token);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> accounts = restTemplate().exchange(accountServiceUrl + "/accounts", 
                            HttpMethod.POST, 
                            entity, 
                            String.class);
        LOG.info("Status from registering account is {}", accounts);
    }

    public List<Account> getAccounts(String user, String userToken) {
      LOG.debug("Getting Accounts for userId: {}", user);

      HttpHeaders headers = new HttpHeaders();
      HttpEntity<String> entity = new HttpEntity<>("", headers);
      headers.add("Authorization", "Bearer " + userToken);
      ResponseEntity<Account[]> accounts = restTemplate().exchange(accountServiceUrl + "/accounts", 
                            HttpMethod.GET, 
                            entity, 
                            Account[].class);
      LOG.debug("accounts response: {}", accounts);                            
		  return Arrays.asList(accounts.getBody());
  }
}

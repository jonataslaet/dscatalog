package com.devsuperior.dscatalog.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;

@Component
public class JwtTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Optional<User> userOptional = userRepository.findByEmail(authentication.getName());
		User user = userOptional.get();
		
		Map<String, Object> map = new HashMap<>();
		map.put("userFirstName", user.getFirstName());
		map.put("userId", user.getId());
		
		DefaultOAuth2AccessToken defaultAccessToken = (DefaultOAuth2AccessToken) accessToken;
		defaultAccessToken.setAdditionalInformation(map);
		
		return defaultAccessToken;
	}

}

package com.oauth.example.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.oauth.example.config.DBServiceConfig;
import com.oauth.example.model.UserModel;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
	@Autowired
    TestDataService testService;
    
   /* @Autowired
    UserDetailsServiceImpl userserviceImpl;*/
    
    
    public CustomAuthenticationProvider() {
       
    }

    @Override
    public Authentication authenticate(Authentication authentication)  throws AuthenticationException {
        String username = authentication.getName();
        Object credentials = authentication.getCredentials();
        System.out.println("credentials class: " + credentials.getClass());
        if (!(credentials instanceof String)) {
            return null;
        }
        String password = credentials.toString();
        System.out.println("-->u "+username+"-->p "+password);
        UserModel userModel = null;
		try {
			userModel = testService.findByUsername(username,password);
		} catch (Exception e) {
			e.printStackTrace();
			userModel = null;
		}

        if (userModel == null) {
            throw new BadCredentialsException("Authentication failed for " + username);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userModel.getRoleName()));
        Authentication auth = new
                UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

	
	
}

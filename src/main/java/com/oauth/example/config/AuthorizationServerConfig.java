package com.oauth.example.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.oauth.example.service.CustomAuthenticationProvider;

@Configuration
@EnableAuthorizationServer
@Order(1)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private String GRANT_TYPE_PASSWORD = "password";
    private String AUTHORIZATION_CODE = "authorization_code";
    private String REFRESH_TOKEN = "refresh_token";
    private String SCOPE_READ = "read";
    private String SCOPE_WRITE = "write";
    private int VALID_FOREVER = -1;

    @Value("${basic.auth.client.id}")
    private String clientId;
    @Value("${basic.auth.client.pass}")
    private String clientPass;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    /* @Autowired
   	private ClientDetailsService clientDetailsService;*/

    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .secret(passwordEncoder().encode(clientPass))
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN)
                .scopes(SCOPE_READ, SCOPE_WRITE)
                .accessTokenValiditySeconds(VALID_FOREVER).//Access token is only valid for 10 sec for testing.
                refreshTokenValiditySeconds(90);
        //.redirectUris("http://localhost:8081/login")

    }
    
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }


   @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
        		.userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
        // endpoints.tokenServices(tokenServices(endpoints));
    }
    
    
   
    
/*    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    	 endpoints.tokenServices(tokenServices(endpoints))
    	 .authenticationManager(authenticationManager).approvalStoreDisabled();
       
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security.checkTokenAccess("permitAll()").checkTokenAccess("isAuthenticated()")
        .passwordEncoder(new BCryptPasswordEncoder());

    }
    
    public AuthorizationServerTokenServices tokenServices(final AuthorizationServerEndpointsConfigurer endpoints) {
        final DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(new InMemoryTokenStore());
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
       // tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setSupportRefreshToken(true);
		tokenServices.setReuseRefreshToken(true);
       // tokenServices.setAuthenticationManager(authenticationManager);
        return tokenServices;
    }*/
   
   //customtokenservice
   /*@Override
   public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
   	 endpoints.tokenServices(this.customTokenServices())
   	 .authenticationManager(authenticationManager);
      
   }
   
	private DefaultTokenServices customTokenServices() {
		DefaultTokenServices tokenServices = new CustomTokenServices();
		tokenServices.setTokenStore(new InMemoryTokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setReuseRefreshToken(true);
		tokenServices.setClientDetailsService(this.clientDetailsService);
		return tokenServices;
	}
   
	private static class CustomTokenServices extends DefaultTokenServices {
		private TokenStore tokenStore;

		@Override
		public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
			OAuth2RefreshToken refreshToken = this.tokenStore.readRefreshToken(refreshTokenValue);
			OAuth2Authentication authentication = this.tokenStore.readAuthenticationForRefreshToken(refreshToken);
			System.out.println("--> AA: "+authentication.getName()+""+authentication.getCredentials());
			// Check attributes in the authentication and
			// decide whether to grant the refresh token
			boolean allowRefresh = true;

			if (!allowRefresh) {
				// throw UnauthorizedClientException or something similar

			}

			return super.refreshAccessToken(refreshTokenValue, tokenRequest);
		}

		@Override
		public void setTokenStore(TokenStore tokenStore) {
			super.setTokenStore(tokenStore);
			this.tokenStore = tokenStore;
		}
	}*/
    
    private PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
  
}
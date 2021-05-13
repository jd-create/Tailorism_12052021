package nl.novi.jdemeijervandriel.tailorism.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Hier gebruiken we de EnableGlobalMethodSecurity(prePostIsEnabled = true) om de @PreAuthorize annotaties te gebruiken
 * op andere plekken in de applicatie.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test/all").permitAll()
                .antMatchers("/api/test/user").hasRole("USER")
                .antMatchers("/api/test/admin").permitAll()
                .antMatchers("/api/test/customer").permitAll()
                .antMatchers("/api/test/operator").hasRole("OPERATOR")
                .antMatchers("/api/test/tailor").permitAll()
                .antMatchers("/api/customer/id/{id}").permitAll()
                .antMatchers("/api/customer/lastname/{lastname}").permitAll()
                .antMatchers("/api/customer/register").permitAll()
                .antMatchers("/api/customer/upload/customerid/*").permitAll()
                .antMatchers("/api/customer/download/customerid/*").permitAll()
                .antMatchers("/api/customer/address/customer_lastname/{lastname}").permitAll()
                .antMatchers("/api/operator/**").permitAll()
                .antMatchers("/api/order/list").permitAll()
                .antMatchers("/api/product/list").permitAll()
                //tijdelijk security uitgeschakeld
                //TODO inschakelen security
//                .antMatchers("/api/test/all").permitAll()
//                .antMatchers("/api/test/user").hasRole("USER")
//                .antMatchers("/api/test/admin").hasRole("ADMIN")
//                .antMatchers("/api/test/customer").hasRole("CUSTOMER")
//                .antMatchers("/api/test/operator").hasRole("OPERATOR")
//                .antMatchers("/api/test/tailor").hasRole("TAILOR")
//                .antMatchers("/api/customer/id/{id}").hasRole("CUSTOMER")
//                .antMatchers("/api/customer/lastname/{lastname}").hasRole("CUSTOMER")
//                .antMatchers("/api/customer/register").hasRole("CUSTOMER")
//                .antMatchers("/api/customer/upload/customerid/*").hasRole("CUSTOMER")
//                .antMatchers("/api/customer/download/customerid/*").hasRole("OPERATOR")
//                .antMatchers("/api/customer/address/customer_lastname/{lastname}").hasRole("OPERATOR")
//                .antMatchers("/api/operator/**").hasRole("OPERATOR")
//                .antMatchers("/api/order/list").hasRole("OPERATOR")
//                .antMatchers("/api/product/list").hasRole("TAILOR")
                .anyRequest().authenticated();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}

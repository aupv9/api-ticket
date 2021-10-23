package com.apps.filter;

import com.apps.mybatis.mysql.RoleRepository;
import com.apps.mybatis.mysql.UserAccountRepository;
import com.apps.service.UserService;
import com.apps.service.impl.RoleServiceImpl;
import io.lettuce.core.ScriptOutputType;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    private final static String TOKEN_HEADER = "Authorization";

    @Autowired
    private  JWTServiceImp jwtService;

    @Autowired
    private  UserService userServiceImp;

    @Autowired
    private  UserAccountRepository userAccountRepository;

    @Autowired
    private  RoleRepository roleRepository;

    @Autowired
    private  RoleServiceImpl roleService;



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(TOKEN_HEADER);

        if (authToken != null && jwtService != null ) {
            if(jwtService.verifyToken(authToken)){
                var email = jwtService.getEmailFromToken(authToken);
                try {
                    com.apps.domain.entity.User user = this.userAccountRepository.findUserByEmail(email);
                    if (user == null) {
                        throw new UsernameNotFoundException("No user found with username: " + email);
                    }
                    var roles = this.roleRepository.findUserRoleById(user.getId());
                    var authorities= roleService.getAuthorities(roles);
                    var userDetail = new User(email, user.getPassword(),
                            user.getUasId() >= 2, true,
                            true, true,authorities );
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail,
                            null, userDetail.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        chain.doFilter(request, response);
    }




}



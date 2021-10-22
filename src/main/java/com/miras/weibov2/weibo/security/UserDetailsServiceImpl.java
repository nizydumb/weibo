package com.miras.weibov2.weibo.security;

import com.miras.weibov2.weibo.entity.Role;
import com.miras.weibov2.weibo.entity.Status;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));
        List<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(Role role: roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        org.springframework.security.core.userdetails.User user1 = new org.springframework.security
                .core.userdetails.User(user.getId().toString(), user.getPassword(),user.getStatus() == Status.ACTIVE, true, true,
                        true, grantedAuthorities );
        return user1;
    }



}

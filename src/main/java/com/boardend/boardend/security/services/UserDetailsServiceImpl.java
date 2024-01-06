package com.boardend.boardend.security.services;

import com.boardend.boardend.models.MobileUser;
import com.boardend.boardend.repository.MobileUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boardend.boardend.models.User;
import com.boardend.boardend.models.Rider;
import com.boardend.boardend.repository.UserRepository;
import com.boardend.boardend.repository.RiderRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private MobileUserRepository mobileUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            return UserDetailsImpl.build(user);
        }

        Rider rider = riderRepository.findByUsername(username).orElse(null);

        if (rider != null) {
            return new RiderDetailsImpl(rider);
        }

        MobileUser mobileUser = mobileUserRepository.findByUsernameIgnoreCase(username).orElse(null);

        if (mobileUser != null) {
            return new MobileUserDetailsImpl(mobileUser);
        }

        throw new UsernameNotFoundException("User Not Found with username: " + username);
    }


}

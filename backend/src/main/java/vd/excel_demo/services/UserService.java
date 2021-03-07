package vd.excel_demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vd.excel_demo.models.User;
import vd.excel_demo.models.UserResponse;
import vd.excel_demo.repositories.UserRepository;
import vd.excel_demo.security.JwtTokenUtil;

@Service
public class UserService implements UserDetailsService {

    private static final String NOT_FOUND = "User with email %s not found!";

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public UserResponse login(String email){
        User user = this.findByEmail(email);
        String token = jwtTokenUtil.generateToken(user);
        return new UserResponse(user.getEmail(), user.getAuthority().getRole(), token);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.findByEmail(email);
    }

    private User findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(String.format(NOT_FOUND, email)));
    }
}

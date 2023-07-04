package com.malpha.acccrud.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.malpha.acccrud.Config.CustomUserDetails;
import com.malpha.acccrud.Model.Usuario;
import com.malpha.acccrud.Repo.UserRepository;

@Service
public class CustomUserDetailServices implements UserDetailsService {

    @Autowired
    private UserRepository UserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = UserRepo.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return new CustomUserDetails(usuario);
    }
    
}

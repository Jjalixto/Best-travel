package com.joel.best_travel.infraestructura.service;

import java.util.List;
import java.util.Map;
import java.util.Collections;

import org.springframework.stereotype.Service;

import com.joel.best_travel.domain.repositories.mongo.AppUserRepository;
import com.joel.best_travel.infraestructura.abstract_services.ModifyUserService;
import com.joel.best_travel.util.exceptions.UsernameNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class AppUserService implements ModifyUserService{

    private final AppUserRepository appUserRepository;

    @Override
    public Map<String, Boolean> enabled(String username) {
        var user = this.appUserRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(COLLECTION_NAME));
        user.setEnabled(!user.isEnabled());
        var userSaved = this.appUserRepository.save(user);
        return Collections.singletonMap(userSaved.getUsername(),userSaved.isEnabled());
    }

    @Override
    public Map<String, List<String>> addRole(String username, String role) {
        var user = this.appUserRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(COLLECTION_NAME));
        user.getRole().getGrantedAuthorities().add(role);
        var userSaved = this.appUserRepository.save(user);
        var authorities = userSaved.getRole().getGrantedAuthorities();
        log.info("User {} add role {}",userSaved.getUsername(),userSaved.getRole(),authorities);
        return Collections.singletonMap(userSaved.getUsername(), authorities);
    }

    @Override
    public Map<String, List<String>> removeRole(String username, String role) {
        var user = this.appUserRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(COLLECTION_NAME));
        user.getRole().getGrantedAuthorities().remove(role);
        var userSaved = this.appUserRepository.save(user);
        var authorities = userSaved.getRole().getGrantedAuthorities();
        log.info("User {} remove role {}",userSaved.getUsername(),userSaved.getRole(),authorities);
        return Collections.singletonMap(userSaved.getUsername(), authorities);
    }

    private static final String COLLECTION_NAME = "app_user";
    
}

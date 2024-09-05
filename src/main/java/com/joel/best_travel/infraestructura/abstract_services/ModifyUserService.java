package com.joel.best_travel.infraestructura.abstract_services;

import java.util.List;
import java.util.Map;

public interface ModifyUserService {
    
    Map<String,Boolean> enabled(String username);

    Map<String,List<String>> addRole(String username,String role);

    Map<String,List<String>> removeRole(String username,String role);
}

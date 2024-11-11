package com.iisi.patrol.webGuard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iisi.patrol.webGuard.security.domain.ERole;
import com.iisi.patrol.webGuard.security.domain.Role;
import com.iisi.patrol.webGuard.security.domain.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserService {

    List<User> users;

    public UserService(){
        this.loadUserFromResource();
    }

    public User findUserByName(String name){
        return this.users.stream().filter(user -> {
            return name.equals(user.getUsername());
        }).findFirst().orElse(null);
    }


    private void loadUserFromResource(){
        ObjectMapper objectMapper = new ObjectMapper();
        File file = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("data" + File.separator + "users.json");
            InputStream inputStream = classPathResource.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            List<Map<String, Object>> result = objectMapper.readValue(responseStrBuilder.toString(), ArrayList.class);
            List<User> users = result.stream().map(m -> {
                User user = new User();
                user.setUsername(m.get("username").toString());
                user.setPassword(m.get("password").toString());
                user.setId( Long.parseLong(m.get("id").toString()));
                HashSet<Role> set = new HashSet<>();
                List<String> roles = new ArrayList<>();
                String handledString = (m.get("roles").toString().replaceAll("[\\[\\]]", ""));
                if(handledString.length()>0){
                    roles = Arrays.asList(handledString.split("\\s*,\\s*"));
                }
                roles.stream().forEach(roleString->{

                    ERole roleEnum = ERole.valueOf(roleString);
                    Role role = new Role(roleEnum);
                    set.add(role);
                });
                user.setRoles(set);
                return user;
            }).collect(Collectors.toCollection(ArrayList::new));

            this.users = users;

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

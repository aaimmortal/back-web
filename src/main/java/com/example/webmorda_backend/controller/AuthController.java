package com.example.webmorda_backend.controller;

import com.example.webmorda_backend.entity.Role;
import com.example.webmorda_backend.entity.User;
import com.example.webmorda_backend.model.AgentRequest;
import com.example.webmorda_backend.payload.AuthRequest;
import com.example.webmorda_backend.payload.RegisterRequest;
import com.example.webmorda_backend.repository.RoleRepository;
import com.example.webmorda_backend.security.jwt.JwtTokenProvider;
import com.example.webmorda_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        String login = authRequest.getLogin();
        String password = authRequest.getPassword();
        System.out.println(login + " " + password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
            User user = userService.getUser(login);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password");
            }
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(login);
        final String token = new JwtTokenProvider().generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/user")
    private ResponseEntity<?> save(@RequestBody RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getRepeatPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords don't match");
        }
        User newUser = new User();
        newUser.setLogin(registerRequest.getLogin());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        List<Role> usrRoles = new ArrayList<>();
        Role role = roleRepository.findRoleByName("ROLE_USER");
        usrRoles.add(role);
        newUser.setRoles(usrRoles);
        if (!userService.saveUser(registerRequest, newUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this login exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @GetMapping("/agents")
    private ResponseEntity<?> getAgents() {
        List<String> agents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/etc/asterisk/sip.conf"))) {
            String line;
            String currentAgent = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("[")) {
                    currentAgent = line.substring(1, line.indexOf("]"));
                    agents.add(currentAgent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(agents);
    }

    @PostMapping("/agent")
    private ResponseEntity<?> addAgent(@RequestBody AgentRequest agentRequest) {
        String agentConfig = String.format(
                "[%s]\n" +
                        "type=friend\n" +
                        "secret=%s\n" +
                        "context=%s\n" +
                        "host=dynamic\n" +
                        agentRequest.getAgentName(), agentRequest.getAgentSecret(), agentRequest.getAgentContext()
        );
        String filePath = "/home/azamat/Documents/test.txt";
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            fileWriter.write(agentConfig);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

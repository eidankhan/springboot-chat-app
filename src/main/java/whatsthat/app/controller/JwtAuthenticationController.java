package whatsthat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.config.JwtTokenUtil;
import whatsthat.app.config.JwtUserDetailsService;
import whatsthat.app.entity.User;
import whatsthat.app.modal.GenericResponse;
import whatsthat.app.modal.JwtRequest;
import whatsthat.app.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public GenericResponse createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            Map<String, Object> response = new HashMap<String, Object>();
            User user = userService.findByEmail(authenticationRequest.getEmail());
            Boolean isAuthenticated = authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

            if (!isAuthenticated)
                new GenericResponse(400, "Invalid email/password supplied");

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
            final String token = jwtTokenUtil.generateToken(userDetails);
            response.put("user_id", userService.findByEmail(authenticationRequest.getEmail()).getId());
            response.put("session_token", token);
            return new GenericResponse(200, "OK", response);
        }
        catch (Exception e) {
            System.err.println("Error Message: " + e.getMessage());
            e.printStackTrace();
            return new GenericResponse(500, "Server Error");
        }
    }
    private Boolean authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException | BadCredentialsException e) {
            System.err.println("Error Message: " + e.getMessage());
            return false;
        }
        return true;
    }


    @PostMapping("/user")
    public GenericResponse registerUser(@Valid @RequestBody UserDTO user, BindingResult result) {
        try {
            // Check the length of the password
            if (user.getPassword().length() < 8)
                return new GenericResponse(400, "Bad Request","Password must be at least 8 characters");

            // Check whether password meets strong password requirements
            boolean isStrongPassword = user.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$");
            if (!isStrongPassword)
                return new GenericResponse(400, "Bad Request", "Password must contain at least one upper case letter, one number and one special character");
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return new GenericResponse(400, "Bad Request",errorMessage);
            }

            User existingUser = userService.findByEmail(user.getEmail());
            if (existingUser != null) {
                return new GenericResponse(400, "Bad Request","Email address already existys");
            }
            UserDTO createdUser = userService.save(new User(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()));
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("user_id", createdUser.getId());
            return new GenericResponse(200, "Created", data);
        }
        catch (Exception e) {
            return new GenericResponse(500, "Server Error");
        }
    }

}
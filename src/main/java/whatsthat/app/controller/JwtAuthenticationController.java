package whatsthat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import whatsthat.app.config.JwtTokenUtil;
import whatsthat.app.config.JwtUserDetailsService;
import whatsthat.app.entity.User;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        User user = userService.findByEmail(authenticationRequest.getEmail());
        Boolean isAuthenticated = authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        if (!isAuthenticated)
        {
            response.put("isLoggedIn", false);
            response.put("message", "Unable to authenticate user");
            System.out.println("Unable to authenticate user");
            return ResponseEntity.ok(response);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        response.put("token", token);
        return ResponseEntity.ok(response);
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


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user, BindingResult result) {
        // Check the length of the password
        if (user.getPassword().length() < 8)
            return ResponseEntity.badRequest().body("Password must be at least 8 characters.");

        // Check whether password meets strong password requirements
        boolean isStrongPassword = user.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$");
        if(!isStrongPassword)
            return ResponseEntity.badRequest().body("Password must contain at least one upper case letter, one number and one special character");

        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errorMessage);
        }

        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Email address already exists.");
        }

        userService.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }
}
package whatsthat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import whatsthat.app.config.JwtTokenUtil;
import whatsthat.app.config.JwtUserDetailsService;
import whatsthat.app.modal.GenericResponse;
import whatsthat.app.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LogoutController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;
    @PostMapping("/logout")
    public GenericResponse logout(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
            String email = jwtTokenUtil.getUsernameFromToken(token);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (token != null && jwtTokenUtil.validateToken(token, userDetails)) {
                jwtTokenUtil.expireToken(token);
                return new GenericResponse(200, "0K", "Successfully logged out");
            }
            return new GenericResponse(400, "Bad Request", "Invalid Token");
        }
        catch (Exception e) {
            return new GenericResponse(500, "Server Error", "Invalid Token");
        }
    }
}

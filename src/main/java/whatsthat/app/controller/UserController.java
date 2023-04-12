package whatsthat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.modal.GenericResponse;
import whatsthat.app.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{user_id}")
    public GenericResponse findById(@PathVariable Long user_id) {
        try {
            UserDTO user = userService.findById(user_id);
            return user != null ? new GenericResponse(200, "OK", user) : new GenericResponse(404,"Not Found");
        }catch (Exception e) {
            System.err.println("Error while retrieving user:"+e.getMessage());
            e.printStackTrace();
            return new GenericResponse(500,"Internal Server Error");
        }
    }


}

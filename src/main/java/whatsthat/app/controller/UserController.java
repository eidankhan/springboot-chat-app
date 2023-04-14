package whatsthat.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whatsthat.app.dto.UserDTO;
import whatsthat.app.entity.User;
import whatsthat.app.mapper.UserMapper;
import whatsthat.app.modal.GenericResponse;
import whatsthat.app.service.UserService;
import whatsthat.app.util.FileUtils;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{user_id}")
    public GenericResponse findById(@PathVariable Long user_id) {
        try {
            User user = userService.findById(user_id);
            return user != null ? new GenericResponse(200, "OK", UserMapper.INSTANCE.userToUserDTO(user)) : new GenericResponse(404,"Not Found");
        }catch (Exception e) {
            System.err.println("Error while retrieving user:"+e.getMessage());
            e.printStackTrace();
            return new GenericResponse(500,"Internal Server Error");
        }
    }

    @PutMapping("/{user_id}")
    public GenericResponse update(@PathVariable(required = true) Long user_id, @Valid @RequestBody UserDTO userDTO , BindingResult result) {
        try{
            User userById = userService.findById(user_id);
            if(userById == null)
                return new GenericResponse(404,"Not Found");

            if(userDTO.getFirstName() != null)
                userById.setFirstName(userDTO.getFirstName());
            if(userDTO.getLastName()!= null)
                userById.setLastName(userDTO.getLastName());
            if(userDTO.getEmail()!= null)
                userById.setEmail(userDTO.getEmail());
            if(userDTO.getPassword()!= null)
                userById.setPassword(userDTO.getPassword());

            // Check the length of the password
            if (userById.getPassword().length() < 8)
                return new GenericResponse(400, "Bad Request","Password must be at least 8 characters");

            // Check whether password meets strong password requirements
            boolean isStrongPassword = userById.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$");
            if (!isStrongPassword)
                return new GenericResponse(400, "Bad Request", "Password must contain at least one upper case letter, one number and one special character");
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return new GenericResponse(400, "Bad Request",errorMessage);
            }

            UserDTO createdUser = userService.save(userById);
            return new GenericResponse(200, "OK", createdUser);
        }
        catch (Exception e) {
            System.err.println("Error while updating user:"+e.getMessage());
            e.printStackTrace();
            return new GenericResponse(500,"Internal Server Error");
        }
    }

    @PostMapping("/{user_id}/photo")
    public GenericResponse uploadProfilePhoto(@PathVariable(required = true) Long user_id, @RequestParam("file") MultipartFile file) {
            User user = userService.findById(user_id);
            String uploadedFilePath = FileUtils.uploadProfilePicture(file, user);
            if(uploadedFilePath != null){
                user.setProfilePhotoPath(uploadedFilePath);
                userService.save(user);
                return new GenericResponse(200, "OK", "Profile photo uploaded successfully");
            }
            return new GenericResponse(500, "Server Error", "Failed to upload profile photo");
    }

    @GetMapping("/{user_id}/photo")
    public ResponseEntity<?> getUserPhoto(@PathVariable("user_id") Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new GenericResponse(404, "User not found"));
        }

        String photoPath = user.getProfilePhotoPath();
        if (photoPath == null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new GenericResponse(404, "Not found"));
        }

        try {
            Path path = Paths.get(photoPath);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new GenericResponse(404, "Not found"));
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new GenericResponse(500, "Not found"));
        }
    }


}

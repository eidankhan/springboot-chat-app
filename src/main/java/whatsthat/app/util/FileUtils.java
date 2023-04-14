package whatsthat.app.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import whatsthat.app.entity.User;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUtils {

    public static String uploadProfilePicture(MultipartFile file, User user){
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
                return null;
            }

            fileName = fileName.replace(" ", "_");

            String uploadDir = "profile-photos" ;
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // check if the user already has a profile photo
            String oldProfilePhotoPath = user.getProfilePhotoPath();
            if (oldProfilePhotoPath != null) {
                File oldProfilePhotoFile = new File(oldProfilePhotoPath);
                if (oldProfilePhotoFile.exists()) {
                    oldProfilePhotoFile.delete();
                }
            }

            String newProfilePhotoPath = uploadDir + "/" + fileName;
            Path storagePath = Paths.get(newProfilePhotoPath);
            Files.copy(file.getInputStream(), storagePath, StandardCopyOption.REPLACE_EXISTING);

            return newProfilePhotoPath;
        }
        catch (Exception e) {
            System.err.println("Error occurred while uploading profile photo:"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

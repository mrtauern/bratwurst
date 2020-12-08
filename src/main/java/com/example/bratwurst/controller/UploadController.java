package com.example.bratwurst.controller;

import com.example.bratwurst.model.File;
import com.example.bratwurst.model.User;
import com.example.bratwurst.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Controller
public class UploadController {

    public UploadController() {
    }

    Logger log = Logger.getLogger(UploadController.class.getName());

    @Autowired
    UploadService uploadService;

    private static final String UPLOADED_FOLDER = "src//main//resources//static//upload//";
    private static final String PROFILE_PICTURE_FOLDER = "src//main//resources//static//upload//";
    private final String UPLOAD = "upload";
    private final String UPLOAD_STATUS = "uploadStatus";
    private final String FILES = "files";
    private final String DELETEFILE = "deleteFile";
    private final String CHANGE_PROFILE_PICTURE = "changeProfilePicture";
    private final String CHANGE_PROFILE_PICTURE_STATUS = "changeProfilePictureStatus";
    private final String REDIRECT = "redirect:/";

    @CrossOrigin()
    @GetMapping("/upload")
    public String upload() {
        log.info("Upload getmapping called");

        return UPLOAD;
    }

    @CrossOrigin()
    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes, HttpSession session){

        log.info("Upload postmapping called");

        if(file.isEmpty()){
            log.info("No file selected!");

            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return REDIRECT + UPLOAD_STATUS;
        }

        if(uploadService.uploadFile(file)){
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } else {
            redirectAttributes.addFlashAttribute("message", "ERROR: '" + file.getOriginalFilename() + "' not uploaded!");
        }

        /*try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER, file.getOriginalFilename());
            Files.write(path, bytes);

            log.info("Uploading: " + file.getOriginalFilename());

            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException e){
            e.printStackTrace();;
        }*/

        return REDIRECT + UPLOAD_STATUS;

    }

    @CrossOrigin()
    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        log.info("UploadStatus getmapping called");

        return UPLOAD_STATUS;
    }

    @CrossOrigin()
    @GetMapping("/files")
    public String tilbud (Model model){
        log.info("Files is called");
        model.addAttribute("files", uploadService.getFiles(1));

        return FILES;
    }

    @CrossOrigin()
    @GetMapping("/deleteFile/{id}")
    public String deleteFile (@PathVariable("id") Integer id, Model model){
        log.info("Delete file getmapping med id " + id + " called");

        model.addAttribute("file", uploadService.getFile(id));
        /*String offerName = adminService.findOffer(id).getOfferName();
        model.addAttribute("pageTitle", "Slet tilbud (" + offerName + ")");*/

        return DELETEFILE;
    }

    @CrossOrigin()
    @PostMapping("/deleteFile")
    public String deleteFile (@ModelAttribute File file, Model model){
        log.info("Delete file putmapping med id " + file.getId() + " called");
        int id = file.getId();

        uploadService.deleteFile(id);

        return REDIRECT + FILES;
    }

    @CrossOrigin()
    @GetMapping("/changeProfilePicture")
    public String changeProfilePicture(RedirectAttributes redirectAttributes, HttpSession session) {
        log.info("changeProfilePicture getmapping called");

        //User user;

        if (session.getAttribute("login") != null) {
            //user = (User) session.getAttribute("login");

            return CHANGE_PROFILE_PICTURE;
        } else {
            log.info("Not logged in!");

            redirectAttributes.addFlashAttribute("notLoggedIn", true);
            return REDIRECT;
        }
    }

    @CrossOrigin()
    @PostMapping("/changeProfilePicture")
    public String changeProfilePicture(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes, HttpSession session){

        log.info("Upload postmapping called");

        User user;

        if (session.getAttribute("login") != null) {
            user = (User) session.getAttribute("login");
        } else {
            log.info("Not logged in!");

            redirectAttributes.addFlashAttribute("message", "You have to be logged in to change profile picture");
            return REDIRECT + CHANGE_PROFILE_PICTURE_STATUS;
        }

        if(file.isEmpty()){
            log.info("No file selected!");

            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return REDIRECT + CHANGE_PROFILE_PICTURE_STATUS;
        }

        if(!uploadService.verifyProfilePicture(file)){
            log.info("Not a valid file type!");

            redirectAttributes.addFlashAttribute("message", "Not a valid file type. You can only use a png, jpeg or gif file");
            return REDIRECT + CHANGE_PROFILE_PICTURE_STATUS;
        }

        if(uploadService.uploadProfilePicture(file, user.getId())){
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } else {
            redirectAttributes.addFlashAttribute("message", "ERROR: '" + file.getOriginalFilename() + "' not uploaded!");
        }

        /*try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER, file.getOriginalFilename());
            Files.write(path, bytes);

            log.info("Uploading: " + file.getOriginalFilename());

            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException e){
            e.printStackTrace();;
        }*/

        return REDIRECT + CHANGE_PROFILE_PICTURE_STATUS;

    }

    @CrossOrigin()
    @GetMapping("/changeProfilePictureStatus")
    public String changeProfilePictureStatus() {
        log.info("changeProfilePictureStatus getmapping called");

        return CHANGE_PROFILE_PICTURE_STATUS;
    }
}


//Test comment
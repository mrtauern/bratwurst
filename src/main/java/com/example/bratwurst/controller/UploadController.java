package com.example.bratwurst.controller;

import com.example.bratwurst.model.File;
import com.example.bratwurst.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final String UPLOAD = "upload";
    private final String UPLOAD_STATUS = "uploadStatus";
    private final String FILES = "files";
    private final String DELETEFILE = "deleteFile";
    private final String REDIRECT = "redirect:/";

    @GetMapping("/upload")
    public String upload() {
        log.info("Upload getmapping called");

        return UPLOAD;
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes){

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

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        log.info("UploadStatus getmapping called");

        return UPLOAD_STATUS;
    }

    @GetMapping("/files")
    public String tilbud (Model model){
        log.info("Files is called");
        model.addAttribute("files", uploadService.getFiles(1));

        return FILES;
    }

    @GetMapping("/deleteFile/{id}")
    public String deleteFile (@PathVariable("id") Integer id, Model model){
        log.info("Delete file getmapping med id " + id + " called");

        model.addAttribute("file", uploadService.getFile(id));
        /*String offerName = adminService.findOffer(id).getOfferName();
        model.addAttribute("pageTitle", "Slet tilbud (" + offerName + ")");*/

        return DELETEFILE;
    }

    @PostMapping("/deleteFile")
    public String deleteFile (@ModelAttribute File file, Model model){
        log.info("Delete file putmapping med id " + file.getId() + " called");
        int id = file.getId();

        uploadService.deleteFile(id);

        return REDIRECT + FILES;
    }
}

package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.imageDTOs.ImageDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.NotFoundException;
import com.example.technomarket.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
public class ImageController extends AbstractController{
    @Autowired
    private ImageService imageService;

    @PostMapping("products/{pid}/addimages")
    public List<ImageDTO> addImagesForProduct(@PathVariable long pid, @RequestParam(name = "file") MultipartFile[] multipartFile){
        return imageService.addImagesForProduct(pid, multipartFile);
    }

    @GetMapping("images/{filename}")
    public void downloadImage(@PathVariable String filename, HttpServletResponse response){
        File file = new File("images" + File.separator + filename);
        if(!file.exists()){
            throw new NotFoundException("File does not exists!");
        }
        response.setContentType("image/png");
        try {
            Files.copy(file.toPath(),response.getOutputStream());
        } catch (IOException e) {
            throw new BadRequestException("Issue with coping the file");
        }
    }
}

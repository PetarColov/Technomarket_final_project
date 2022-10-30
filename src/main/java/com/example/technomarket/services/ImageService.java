package com.example.technomarket.services;

import com.example.technomarket.model.dto.imageDTOs.ImageDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.pojo.Product;
import com.example.technomarket.model.pojo.ProductImage;
import com.example.technomarket.model.repository.ImageRepository;
import com.example.technomarket.model.repository.ProductRepository;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ModelMapper modelMapper;


    public List<ImageDTO> addImagesForProduct(long pid, MultipartFile[] multipartFile) {
        Product product = productRepository.findById(pid).orElseThrow(() -> new BadRequestException("No such product!"));
        List <ImageDTO> imageDTOs = new ArrayList<>();
        for (MultipartFile m: multipartFile) {
            String fileExtension = FilenameUtils.getExtension(m.getOriginalFilename());
            String fileName = "images" + File.separator + System.nanoTime() + "." + fileExtension;
            File file = new File(fileName);
            try {
                Files.copy(m.getInputStream(),file.toPath());
            } catch (IOException e) {
                throw new BadRequestException("Cannot copy image!" + e.getMessage());
            }

            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setImageName(fileName);
            Long imageId = imageRepository.save(productImage).getImageId();
            imageDTOs.add(new ImageDTO(imageId,fileName));
        }

        return imageDTOs;
    }

    public List<ImageDTO> getAllImages(List<ProductImage> productImages){
        return productImages.stream().map(productImage -> modelMapper.map(productImage, ImageDTO.class)).collect(Collectors.toList());
    }
}
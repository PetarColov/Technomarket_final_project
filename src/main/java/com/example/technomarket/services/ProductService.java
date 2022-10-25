package com.example.technomarket.services;


import com.example.technomarket.model.dto.characteristicDTOs.CharacteristicValueDTO;
import com.example.technomarket.model.dto.characteristicDTOs.CharacteristicWithValueDTO;
import com.example.technomarket.model.dto.characteristicDTOs.ResponseCharacteristicDTO;
import com.example.technomarket.model.dto.product.*;
import com.example.technomarket.model.dto.subcategoryDTOs.SubcategoryWithNameOnly;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.*;
import com.example.technomarket.model.repository.*;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private CharacteristicRepository characteristicRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductsWithCharacteristicsRepository productsWithCharacteristicsRepository;

    public ProductResponseDTO addProduct(AddProductDTO productDTO) {

        if (!validAmount(productDTO.getAmountLeft()) || !validPrice(productDTO.getPrice())) {
            throw new BadRequestException("Enter valid product data!");
        }
        if (!currentUser.checkAdmin()) {
            throw new UnauthorizedException("Method not allowed!");
        }

        if(!validBrandName(productDTO.getBrandName())){
            throw new BadRequestException("No such brand!");
        }

        if (!validSubCategoryName(productDTO.getSubcategoryName())){
            throw new BadRequestException("No such subcategory!");
        }

        Optional<Product> byName = productRepository.findByName(productDTO.getName());

        ProductResponseDTO p;

        if (byName.isPresent()) {
            Product existingProduct = byName.get();
            existingProduct.setAmountLeft(existingProduct.getAmountLeft() + productDTO.getAmountLeft());

            double newPrice = productDTO.getPrice().doubleValue();
            double oldPrice = existingProduct.getPrice().doubleValue();

            if (newPrice != oldPrice) {
                existingProduct.setPrice(BigDecimal.valueOf(newPrice));
            }

            SubCategory subCategory = getSubCategoryFromDB(productDTO);
            existingProduct.setSubcategory(subCategory);

            Brand brand = getBrandFromDB(productDTO);
            existingProduct.setBrandName(brand);

            productRepository.save(existingProduct);
            p = mapper.map(existingProduct, ProductResponseDTO.class);
        } else {
            Product newProduct = mapper.map(productDTO, Product.class);

            SubCategory subCategory = getSubCategoryFromDB(productDTO);
            newProduct.setSubcategory(subCategory);

            Brand brand = getBrandFromDB(productDTO);
            newProduct.setBrandName(brand);

            productRepository.save(newProduct);
            p = mapper.map(newProduct, ProductResponseDTO.class);
        }
        return p;
    }

    private Brand getBrandFromDB(AddProductDTO productDTO) {
        Brand brand = brandRepository.findByBrandName(productDTO.getBrandName()).get();
        return brand;
    }

    private SubCategory getSubCategoryFromDB(AddProductDTO productDTO) {
        return subcategoryRepository.findSubCategoryBySubcategoryName(productDTO.getSubcategoryName()).get();
    }

    private boolean validSubCategoryName(String subcategoryName) {
        Optional<SubCategory> byName = subcategoryRepository.findSubCategoryBySubcategoryName(subcategoryName);
        return byName.isPresent();
    }

    private boolean validBrandName(String brandName) {
        Optional<Brand> byName = brandRepository.findByBrandName(brandName);
        return byName.isPresent();
    }


    public ProductResponseDTO deleteProduct(long pid) {
        if (!currentUser.checkAdmin()) {
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }
        Product byId = productRepository.findById(pid).orElseThrow(() -> new BadRequestException("Product not found!"));
        productRepository.delete(byId);

        return mapper.map(byId, ProductResponseDTO.class);

    }


    private boolean validPrice(BigDecimal price) {
        return price.doubleValue() > 0;
    }

    private boolean validAmount(int amountLeft) {
        return amountLeft > 0;
    }

    public ProductInCartDTO addToCart(AddProductToCartDTO addProductToCartDTO, long pid) {
        if (currentUser.getId() == null){
            throw new UnauthorizedException("User not logged in!");
        }

        Product product = productRepository.findById(pid).orElseThrow(() ->
                new BadRequestException("Product with such id not found!"));

        User user = userRepository.findById(currentUser.getId()).get();

        CartKey cartKey = new CartKey(user.getId(), product.getId());

        Cart cart = new Cart(cartKey, user, product, addProductToCartDTO.getQuantity());

        user.getCartUser().add(cart);

        product.getCartProduct().add(cart);

        cartRepository.save(cart);

        currentUser.addToCart(cart);

        return new ProductInCartDTO(product.getName(), addProductToCartDTO.getQuantity());
    }

    public ProductForClientDTO searchForProductByName(String product) {
        Optional<Product> productOptional = productRepository.findByName(product);
        if(productOptional.isPresent()){
            return mapper.map(productOptional.get(),ProductForClientDTO.class);
        }
        else{
            throw new BadRequestException("No such product was found!");
        }
    }

    public List<ProductForClientDTO> sortProductsAscending(String subCategory) {
        Optional<SubCategory> subCategoryOptional = subcategoryRepository.findSubCategoryBySubcategoryName(subCategory);
        if(subCategoryOptional.isPresent()) {
            List<Product> products = productRepository.findAllBySubcategoryOrderByPriceDesc(subCategoryOptional.get());
            return products.stream().map(product -> mapper.map(product, ProductForClientDTO.class)).toList();
        }
        else{
            throw new BadRequestException("No such subcategories!");
        }
    }

    public List<ProductForClientDTO> sortProductsDescending(String subCategory) {
        Optional<SubCategory> subCategoryOptional = subcategoryRepository.findSubCategoryBySubcategoryName(subCategory);
        if(subCategoryOptional.isPresent()) {
            List<Product> products = productRepository.findAllBySubcategoryOrderByPriceAsc(subCategoryOptional.get());
            return products.stream().map(product -> mapper.map(product, ProductForClientDTO.class)).toList();
        }
        else{
            throw new BadRequestException("No such subcategories!");
        }
    }

    public ProductWithCharacteristicsDTO addCharacteristic(long pid, CharacteristicWithValueDTO characteristic) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }

        String characteristicName = characteristic.getCharacteristic().getCharacteristicName();
        Optional<Product> productOptional = productRepository.findById(pid);
        Optional<Characteristic> characteristicOptional = characteristicRepository.findCharacteristicByCharacteristicName(characteristicName);

        if(productOptional.isPresent() && characteristicOptional.isPresent()){
            Product product = productOptional.get();
            Characteristic characteristic1 = characteristicOptional.get();
            String charValue = characteristic.getCharacteristicValue();

            CharacteristicKey characteristicKey = new CharacteristicKey(product.getId(), characteristic1.getId());
            Chars chars = new Chars(characteristicKey, product, characteristic1, charValue);
            productsWithCharacteristicsRepository.save(chars);
            product.getCharacteristics().add(chars);

            List<Chars> charsList = productsWithCharacteristicsRepository.findAllByProduct(product);
            List<CharacteristicValueDTO> characteristicDTOS = charsList.stream().map(c -> mapper.map(c, CharacteristicValueDTO.class)).toList();
            ProductWithCharacteristicsDTO product1 = new ProductWithCharacteristicsDTO(product.getName(), new ArrayList<>());
            for(CharacteristicValueDTO chars1 : characteristicDTOS){
                product1.getCharacteristicValues().add(chars1.getCharacteristicValue());
            }
            return product1;
        }
        else{
            throw new BadRequestException("No such product or characteristic!");
        }
    }
}

package com.example.technomarket.services;

import com.example.technomarket.model.dto.characteristicDTOs.CharacteristicWithValueDTO;
import com.example.technomarket.model.dto.characteristicDTOs.ResponseCharacteristicDTO;
import com.example.technomarket.model.dto.product.*;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.NotFoundException;
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
    private ImageService imageService;
    @Autowired
    private  ImageRepository imageRepository;
    @Autowired
    private CharsRepository charsRepository;
    @Autowired
    private CharacteristicService characteristicService;

    public ProductResponseDTO addProduct(ProductDTO productDTO) {

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

    private Brand getBrandFromDB(ProductDTO productDTO) {
        Brand brand = brandRepository.findByBrandName(productDTO.getBrandName()).get();
        return brand;
    }

    private SubCategory getSubCategoryFromDB(ProductDTO productDTO) {
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

    public ProductInCartDTO addToCart(ProductForCartDTO productForCartDTO, long pid) {
        if (currentUser.getId() == null){
            throw new UnauthorizedException("User not logged in!");
        }

        Product product = productRepository.findById(pid).orElseThrow(() ->
                new BadRequestException("Product with such id not found!"));

        User user = userRepository.findById(currentUser.getId()).get();

        CartKey cartKey = new CartKey(user.getId(), product.getId());

        Cart cart = new Cart(cartKey, user, product, productForCartDTO.getQuantity());

        user.getCartUser().add(cart);

        product.getCartProduct().add(cart);

        cartRepository.save(cart);

        currentUser.addToCart(cart);

        return new ProductInCartDTO(product.getName(), productForCartDTO.getQuantity());
    }

    public ProductResponseDTO searchForProductByName(String productName) {
        Product product = productRepository
                .findByName(productName).orElseThrow(() -> new BadRequestException("No such product was found!"));

        return mapper.map(product,ProductResponseDTO.class);
    }

    public List<ProductResponseDTO> sortProducts(Long subCategory, String sort) {
        SubCategory subCategoryOptional = subcategoryRepository.
                findBySubcategoryId(subCategory).orElseThrow(() -> new BadRequestException("No such subcategories!"));

        List<Product> products = null;
        switch(sort){
            case "asc":
                products = productRepository.findAllBySubcategoryOrderByPriceAsc(subCategoryOptional);
                break;
            default:
                products = productRepository.findAllBySubcategoryOrderByPriceDesc(subCategoryOptional);
                break;
        }
        return products.stream().map(product -> mapper.map(product, ProductResponseDTO.class)).toList();
    }


    public ProductWithCharacteristicsDTO addCharacteristic(long pid, CharacteristicWithValueDTO characteristicDTO) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }

        String characteristicName = characteristicDTO.getCharacteristic().getCharacteristicName();
        Optional<Product> productOptional = productRepository.findById(pid);
        Optional<Characteristic> characteristicOptional = characteristicRepository.findCharacteristicByCharacteristicName(characteristicName);

        if(productOptional.isEmpty() || characteristicOptional.isEmpty()) {
            throw new BadRequestException("No such product or characteristicDTO!");
        }

        Product product = productOptional.get();
        Characteristic characteristic = characteristicOptional.get();
        String charValue = characteristicDTO.getCharacteristicValue();

        CharacteristicKey characteristicKey = new CharacteristicKey(product.getId(), characteristic.getId());
        Chars chars = new Chars(characteristicKey, product, characteristic, charValue);
        charsRepository.save(chars);
        product.getCharacteristics().add(chars);

        List<Chars> charsList = charsRepository.findAllByProduct(product);
        List<ResponseCharacteristicDTO> characteristicDTOS = charsList.stream()
                .map(c -> mapper.map(c, ResponseCharacteristicDTO.class)).toList();
        ProductWithCharacteristicsDTO productResponse = new ProductWithCharacteristicsDTO(product.getId(),product.getName(), new ArrayList<>());
        for(ResponseCharacteristicDTO responseChars : characteristicDTOS){
            productResponse.getCharacteristics().add(responseChars);
        }

        return productResponse;
    }

    public List<ProductResponseDTO> getProductBySubcategory(String subcategory) {
        Optional<SubCategory> subCategoryOptional = subcategoryRepository.findSubCategoryBySubcategoryName(subcategory);
        if(subCategoryOptional.isEmpty()) {
            throw new BadRequestException("No such subcategory!");
        }
            List<Product> products = productRepository.findAllBySubcategory(subCategoryOptional.get());
            return products.stream().map(p -> mapper.map(p,ProductResponseDTO.class)).toList();
    }

    public ProductForClientDTO getProduct(long pid) {
        Product product = productRepository.findById(pid).orElseThrow(() -> new BadRequestException("No such product!"));
        List<ProductImage> productImages = imageRepository.findAllByProduct(product);
        List<Chars> characteristics = charsRepository.findAllByProduct(product);
        ProductForClientDTO product1 = new ProductForClientDTO();
        product1.setPrice(product.getPrice());
        product1.setBrandName(product.getBrandName());
        product1.setAmountLeft(product.getAmountLeft());
        product1.setName(product.getName());
        product1.setProductId(product.getId());
        product1.setProductImages(imageService.getAllImages(productImages));
        product1.setCharacteristics(characteristicService.getAllCharsForProduct(characteristics));
        return product1;
    }

    public ProductWithNameDTO subscribeForProduct(long pid) {
        if (currentUser.getId() == null){
            throw new UnauthorizedException("User not logged in!");
        }

        Product product = productRepository.findById(pid).orElseThrow(() -> new NotFoundException("Product not found!"));

        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("User not found!"));

        product.getUsersSubscribed().add(user);

        Long productId = productRepository.save(product).getId();
        ProductWithNameDTO productDTO = mapper.map(product, ProductWithNameDTO.class);
        productDTO.setId(productId);

        return productDTO;
    }
}

package com.example.technomarket.model.dto.product;

import com.example.technomarket.model.dto.characteristicDTOs.ResponseCharacteristicDTO;
import com.example.technomarket.model.pojo.Brand;
import com.example.technomarket.model.pojo.Characteristic;
import com.example.technomarket.model.pojo.Chars;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductWithCharacteristicsDTO {
    private String productName;
    private List<String> characteristicValues;
}

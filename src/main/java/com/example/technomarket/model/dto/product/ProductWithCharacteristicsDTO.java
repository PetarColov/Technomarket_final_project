package com.example.technomarket.model.dto.product;

import com.example.technomarket.model.dto.characteristicDTOs.ResponseCharacteristicDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductWithCharacteristicsDTO {
    private Long id;
    private String productName;
    private List<ResponseCharacteristicDTO> characteristics;
}

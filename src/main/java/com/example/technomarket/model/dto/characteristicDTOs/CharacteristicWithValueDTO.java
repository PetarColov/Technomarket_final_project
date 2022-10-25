package com.example.technomarket.model.dto.characteristicDTOs;

import com.example.technomarket.model.pojo.Characteristic;
import lombok.Data;

@Data
public class CharacteristicWithValueDTO {
    private RequestCharacteristic characteristic;
    private String characteristicValue;
}

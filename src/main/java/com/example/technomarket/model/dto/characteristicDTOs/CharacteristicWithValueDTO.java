package com.example.technomarket.model.dto.characteristicDTOs;

import lombok.Data;

@Data
public class CharacteristicWithValueDTO {
    private RequestCharacteristic characteristic;
    private String characteristicValue;
}

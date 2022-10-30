package com.example.technomarket.contoller;

import com.example.technomarket.model.dto.characteristicDTOs.CharacteristicDTO;
import com.example.technomarket.model.dto.characteristicDTOs.RequestCharacteristic;
import com.example.technomarket.model.dto.characteristicDTOs.ResponseCharacteristicDTO;
import com.example.technomarket.services.CharacteristicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CharacteristicController extends AbstractController{
    @Autowired
    private CharacteristicService characteristicService;

    @PostMapping("/characteristic")
    public CharacteristicDTO addCharacteristic(@RequestBody RequestCharacteristic characteristic){
        return characteristicService.addCharacteristic(characteristic);
    }
}

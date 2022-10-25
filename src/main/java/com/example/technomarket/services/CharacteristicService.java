package com.example.technomarket.services;

import com.example.technomarket.model.dto.characteristicDTOs.RequestCharacteristic;
import com.example.technomarket.model.dto.characteristicDTOs.ResponseCharacteristicDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.Characteristic;
import com.example.technomarket.model.repository.CharacteristicRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CharacteristicService {

    @Autowired
    private CharacteristicRepository characteristicRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CurrentUser currentUser;

    public ResponseCharacteristicDTO addCharacteristic(RequestCharacteristic characteristic) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }
        Optional<Characteristic> characteristicOptional = characteristicRepository.findCharacteristicByCharacteristicName(characteristic.getCharacteristicName());
        if(characteristicOptional.isEmpty()){
            Characteristic characteristic1 = modelMapper.map(characteristic,Characteristic.class);
            characteristicRepository.save(characteristic1);
            Optional<Characteristic> characteristic2 = characteristicRepository.findCharacteristicByCharacteristicName(characteristic.getCharacteristicName());
            if(characteristic2.isPresent()) {
                return modelMapper.map(characteristic2,ResponseCharacteristicDTO.class);
            }
            else{
                throw new BadRequestException("Characteristic was not saved properly");
            }
        }
        else{
            throw new BadRequestException("Such characteristic already exists!");
        }
    }
}

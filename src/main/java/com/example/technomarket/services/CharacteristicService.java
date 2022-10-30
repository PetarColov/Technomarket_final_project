package com.example.technomarket.services;

import com.example.technomarket.model.dto.characteristicDTOs.CharacteristicDTO;
import com.example.technomarket.model.dto.characteristicDTOs.RequestCharacteristic;
import com.example.technomarket.model.dto.characteristicDTOs.ResponseCharacteristicDTO;
import com.example.technomarket.model.exceptions.BadRequestException;
import com.example.technomarket.model.exceptions.UnauthorizedException;
import com.example.technomarket.model.pojo.Characteristic;
import com.example.technomarket.model.pojo.Chars;
import com.example.technomarket.model.repository.CharacteristicRepository;
import com.example.technomarket.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CharacteristicService {

    @Autowired
    private CharacteristicRepository characteristicRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CurrentUser currentUser;

    public CharacteristicDTO addCharacteristic(RequestCharacteristic characteristicDTO) {
        if(!currentUser.isAdmin()){
            throw new UnauthorizedException("You don`t have permission for this operation!");
        }
        Optional<Characteristic> characteristicOptional = characteristicRepository.findCharacteristicByCharacteristicName(characteristicDTO.getCharacteristicName());
        if(characteristicOptional.isPresent()){
            throw new BadRequestException("Such characteristicDTO already exists!");
        }

        Characteristic characteristic = modelMapper.map(characteristicDTO,Characteristic.class);
        Long characteristicId = characteristicRepository.save(characteristic).getId();

        CharacteristicDTO responseCharacteristic = modelMapper.map(characteristic,CharacteristicDTO.class);
        responseCharacteristic.setId(characteristicId);

        return responseCharacteristic;
    }

    public List<ResponseCharacteristicDTO> getAllCharsForProduct(List<Chars> characteristics){
        return characteristics.stream().map(chars -> modelMapper.map(chars,ResponseCharacteristicDTO.class)).collect(Collectors.toList());
    }
}

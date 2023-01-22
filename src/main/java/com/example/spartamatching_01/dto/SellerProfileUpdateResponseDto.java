package com.example.spartamatching_01.dto;

import com.example.spartamatching_01.entity.Client;
import lombok.Getter;

@Getter
public class SellerProfileUpdateResponseDto {

    private String nickname;
    private String image;
    private String about;
    private String category;

    public SellerProfileUpdateResponseDto(Client client){
        this.nickname = client.getNickname();
        this.image = client.getImage();
        this.about = client.getAbout();
        this.category = client.getCategory();
    }

}

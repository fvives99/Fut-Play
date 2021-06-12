package com.example.futplay.Controllers.Items;

import com.example.futplay.R;

public class PlayersItem {

    private int image;

    private String name;

    public PlayersItem(){
        this.image = R.drawable.profile_image_icon;
        this.name = "Nombre";
    }

    public PlayersItem(int image, String name){
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}

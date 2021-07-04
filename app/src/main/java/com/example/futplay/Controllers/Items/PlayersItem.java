package com.example.futplay.Controllers.Items;

import com.example.futplay.R;

public class PlayersItem {

    private int image;

    private String name;

    private  String UID;

    public PlayersItem(){
        this.image = R.drawable.profile_image_icon;
        this.name = "Nombre";
    }

    public PlayersItem(int image, String name, String UID){
        this.image = image;
        this.name = name;
        this.UID = UID;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getUID() {
        return UID;
    }
}

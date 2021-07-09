package com.example.futplay.Controllers.Items;

import com.example.futplay.R;

public class PlayersItem {

    private int image;

    private String name;

    private  String UID;

    private  String CID;

    public PlayersItem(){
        this.image = R.drawable.profile_image_icon;
        this.name = "Nombre";
    }

    public PlayersItem(int image, String name, String UID, String CID){
        this.image = image;
        this.name = name;
        this.UID = UID;
        this.CID = CID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
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

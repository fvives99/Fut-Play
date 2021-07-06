package com.example.futplay.Controllers.Items;

public class Players {
    private String privileges; //P: Club Player, A: Club Admin, C: Club Creator
    private String userID;

    public Players(String userID) {
        this.privileges = "P";
        this.userID = userID;
    }

    public String getPrivileges() {
        return privileges;
    }

    public String getUserID() {
        return userID;
    }

    public void setPrivileges(String privileges) {
        if(privileges == "A" || privileges == "C"){
            this.privileges = privileges;
        }else{
            this.privileges = "P";
        }

    }
}

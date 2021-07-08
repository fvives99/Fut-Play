package com.example.futplay.Controllers.Items;

import java.util.ArrayList;

public class UserClubs {
    ArrayList<String> clubsJoined;
    int numClubsJoined;

    public UserClubs() {
        this.numClubsJoined = 0;
        this.clubsJoined = null;
    }

    public UserClubs(ArrayList<String> clubsJoined) {
        this.clubsJoined = clubsJoined;
        this.numClubsJoined = clubsJoined.size();
    }

    public void addClub(String clubID){
        if(clubsJoined == null){
            clubsJoined = new ArrayList<>();
        }
        this.clubsJoined.add(clubID);
        this.numClubsJoined++;
    }

    public void leaveClub(String clubID){
        for(int i = 0; i < this.numClubsJoined; i ++){
            if(this.clubsJoined.get(i).equals(clubID)){
                this.clubsJoined.remove(i);
                this.numClubsJoined--;
                break;
            }
        }
    }

    public ArrayList<String> getClubsJoined() {
        return clubsJoined;
    }

    public void setClubsJoined(ArrayList<String> clubsJoined) {
        this.clubsJoined = clubsJoined;
    }

    public int getNumClubsJoined() {
        return numClubsJoined;
    }

    public void setNumClubsJoined(int numClubsJoined) {
        this.numClubsJoined = numClubsJoined;
    }
}

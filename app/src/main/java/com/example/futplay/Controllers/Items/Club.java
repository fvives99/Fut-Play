package com.example.futplay.Controllers.Items;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;
public class Club {
    private String clubID;
    private String clubName;
    private String clubTag;
    private String clubRegion;
    private int matchesWon;
    private int matchesLost;
    private int matchesTied;
    private int clubSize;
    private ArrayList<Players> clubMembers;

    public Club() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Club(String clubName, String clubTag, String clubRegion, int matchesWon, int matchesLost, int matchesTied) {
        this.clubName = clubName;
        this.clubTag = clubTag;
        this.clubRegion = clubRegion;
        this.matchesWon = matchesWon;
        this.matchesLost = matchesLost;
        this.matchesTied = matchesTied;
        this.clubID = generateUniqueClubID();
    }

    public String setClubID(){
        this.clubID = generateUniqueClubID();
        return this.clubID;
    }

    public List<Players> getClubMembersList() {
        return clubMembers;
    }

    public Players getClubMember(String userID){
        Players memberSearched = null;
        int i = clubMembers.size() - 1;
        while(i >= 0){
            if(clubMembers.get(i).getUserID().equals(userID)){
                memberSearched = clubMembers.get(i);
            }
            i--;
        }
        return memberSearched;
    }

    public boolean removeClubMember(String userID){
        boolean isRemoved = false;
        int i = clubMembers.size() - 1;
        while(i >= 0){
            if(clubMembers.get(i).getUserID().equals(userID)){
                clubMembers.remove(i);
                isRemoved = true;
            }
            i--;
        }
        return isRemoved;
    }

    public void addMember(Players newMember) {
        if(this.clubMembers == null){
            clubMembers = new ArrayList<>();
        }
        this.clubMembers.add(newMember);
        this.clubSize++;
    }

    public String generateUniqueClubID(){
        String uniqueClubID = "";
        int random1 = (int)Math.floor(Math.random()*(9-0+1)+0);
        int random2 = (int)Math.floor(Math.random()*(9-0+1)+0);
        int random3 = (int)Math.floor(Math.random()*(9-0+1)+0);
        int random4 = (int)Math.floor(Math.random()*(9-0+1)+0);
        uniqueClubID = this.clubTag+"#"+ Integer.toString(random1) + Integer.toString(random2)  + Integer.toString(random3)  + Integer.toString(random4) ; //The unique id consists in a string with the club's name and tag, and finally 4 random digits between 0 - 9
        return uniqueClubID;
    }

    public Number getClubSize() {
        return clubSize;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubTag() {
        return clubTag;
    }

    public void setClubTag(String clubTag) {
        this.clubTag = clubTag;
    }

    public String getClubRegion() {
        return clubRegion;
    }

    public void setClubRegion(String clubRegion) {
        this.clubRegion = clubRegion;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(int matchesWon) {
        this.matchesWon = matchesWon;
    }

    public int getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(int matchesLost) {
        this.matchesLost = matchesLost;
    }

    public int getMatchesTied() {
        return matchesTied;
    }

    public void setMatchesTied(int matchesTied) {
        this.matchesTied = matchesTied;
    }

    public String getClubID() {
        return clubID;
    }
}

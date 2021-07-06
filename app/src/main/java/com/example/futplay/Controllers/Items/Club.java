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
        this.clubID = generateUniqueClubID(clubTag);
    }

    public String setClubID(){
        this.clubID = generateUniqueClubID(clubTag);
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

    public String generateUniqueClubID(String clubTag){ //this function implements UUID class from JAVA that creates an UNIQUE id in this case for the clubs
        String uniqueClubID = "";
        UUID uniqueKey = UUID.randomUUID();
        uniqueClubID = clubTag +"-"+ uniqueKey.toString(); //The unique id consists in a string with the club's tag at the beginning and the unique uuid next
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

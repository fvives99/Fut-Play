package com.example.futplay.Controllers.Items;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

public class Club {


    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z" };

    public static String getShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }
    private String clubID;
    private String clubName;
    private String clubTag;
    private String clubRegion;
    private int matchesWon;
    private int matchesLost;
    private int matchesTied;
    private int clubSize;
    private ArrayList<String> clubMembersList;
    private ArrayList<String> clubMembersPrivileges;

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
    }

    public String setClubID(){
        this.clubID = generateUniqueClubID();
        return this.clubID;
    }

    public ArrayList<String> getClubMembersList() {
        return clubMembersList;
    }

    /*public Players getClubMember(String userID){
        Players memberSearched = null;
        int i = clubMembers.size() - 1;
        while(i >= 0){
            if(clubMembers.get(i).getUserID().equals(userID)){
                memberSearched = clubMembers.get(i);
            }
            i--;
        }
        return memberSearched;
    }*/

    public boolean removeClubMember(String userID){
        boolean isRemoved = false;
        int i = clubMembersList.size() - 1;
        while(i >= 0){
            if(clubMembersList.get(i).equals(userID)){
                clubMembersList.remove(i);
                clubMembersPrivileges.remove(i);
                isRemoved = true;
            }
            i--;
        }
        return isRemoved;
    }

    public boolean changeClubMemberPrivileges(String userID, String newPrivilege){
        boolean isRemoved = false;
        if(newPrivilege != "A" || newPrivilege != "C"){
            newPrivilege = "P";
        }
        int i = clubMembersList.size() - 1;
        while(i >= 0){
            if(clubMembersList.get(i).equals(userID)){
                clubMembersPrivileges.remove(i);
                clubMembersPrivileges.add(i,newPrivilege);
                isRemoved = true;
            }
            i--;
        }
        return isRemoved;
    }

    public void addMember(String newMember, String privileges) {
        if(this.clubMembersList == null){
            clubMembersList = new ArrayList<>();
            clubMembersPrivileges = new ArrayList<>();
        }
        if(privileges != "A" && privileges != "C"){
            privileges = "P";
        }
        this.clubMembersPrivileges.add(privileges);
        this.clubMembersList.add(newMember);
        this.clubSize++;
    }


    public String generateUniqueClubID(){
        String uniqueClubID = "";
        int random1 = (int)Math.floor(Math.random()*(9-0+1)+0);
        int random2 = (int)Math.floor(Math.random()*(9-0+1)+0);
        int random3 = (int)Math.floor(Math.random()*(9-0+1)+0);
        int random4 = (int)Math.floor(Math.random()*(9-0+1)+0);
        uniqueClubID = getShortUuid() + "#" + Integer.toString(random1) + Integer.toString(random2)  + Integer.toString(random3)  + Integer.toString(random4); //The unique id consists in a string with a short UUID, and finally 4 random digits between 0 - 9
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

    public ArrayList<String> getClubMembersPrivileges() {
        return clubMembersPrivileges;
    }

    public void setClubMembersPrivileges(ArrayList<String> clubMembersPrivileges) {
        this.clubMembersPrivileges = clubMembersPrivileges;
    }
}

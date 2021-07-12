package com.example.futplay.Controllers.Items;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Requests {
    String userRequestingID;
    String clubRequestedID;
    String requestStatus;
    Timestamp requestBirthDate;

    public Requests(String requestID, String requestStatus){
        String[] parts = requestID.split("-");
        this.userRequestingID = parts[1];
        this.clubRequestedID = parts[0];
        this.requestStatus = requestStatus;
        this.requestBirthDate = new Timestamp(new Date());
    }

    public String getUserRequestingID() {
        return userRequestingID;
    }

    public void setUserRequestingID(String userRequestingID) {
        this.userRequestingID = userRequestingID;
    }

    public String getClubRequestedID() {
        return clubRequestedID;
    }

    public void setClubRequestedID(String clubRequestedID) {
        this.clubRequestedID = clubRequestedID;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        if(requestStatus == "Approved" || requestStatus == "Expired"){
            this.requestStatus = requestStatus;
        }else{
            this.requestStatus = "Pending";
        }

    }
}

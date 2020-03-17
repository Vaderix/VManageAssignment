package com.datdevteam.vmanageassignment;

import android.graphics.Bitmap;

public class VRecords {

    int _id;
    String numPlate;
    String vehicleType;
    String ownerName;
    String address;
    String mobNum;
    Bitmap plateIMG;

    public VRecords()
    {

    }

    public int getId()
    {
        return _id;
    }

    public VRecords(String numPlate, String vehicleType, String ownerName, String address, String mobNum, Bitmap plateIMG)
    {
        this.numPlate = numPlate;
        this.vehicleType = vehicleType;
        this.ownerName = ownerName;
        this.address = address;
        this.mobNum = mobNum;
        this.plateIMG = plateIMG;
    }

    public String getVehicleType(){
        return vehicleType;
    }

    public void setVehicleType(String vehicleType){
        this.vehicleType = vehicleType;
    }

    public String getNumPlate() {
        return numPlate;
    }

    public void setNumPlate(String numPlate) {
        this.numPlate = numPlate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public Bitmap getPlateIMG() {
        return plateIMG;
    }

    public void setPlateIMG(Bitmap plateIMG) {
        this.plateIMG = plateIMG;
    }
}

package com.datdevteam.vmanageassignment;

public class CurrentRecord {

    private static String plateNum;
    private static VRecords vRecords;
    public static boolean scannedRecord=false;

    public static String getPlateNum() {
        return plateNum;
    }

    public static void setPlateNum(String plateNum) {
        CurrentRecord.plateNum = plateNum;
    }

    public static VRecords getvRecords() {
        return vRecords;
    }

    public static void setvRecords(VRecords vRecords) {
        CurrentRecord.vRecords = vRecords;
    }
}

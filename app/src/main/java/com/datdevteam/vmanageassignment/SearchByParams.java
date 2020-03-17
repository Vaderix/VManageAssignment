package com.datdevteam.vmanageassignment;

public class SearchByParams {

    private static boolean searchByMethod = false;
    private static String searchBy;
    private static String searchByValue;
    private static String searchOp;

    public static String getSearchOp() {
        return searchOp;
    }

    public static void setSearchOp(String searchOp) {
        SearchByParams.searchOp = searchOp;
    }

    public static boolean isSearchByMethod() {
        return searchByMethod;
    }

    public static void setSearchByMethod(boolean searchByMethod) {
        SearchByParams.searchByMethod = searchByMethod;
    }

    public static String getSearchBy() {
        return searchBy;
    }

    public static void setSearchBy(String searchBy) {
        SearchByParams.searchBy = searchBy;
    }

    public static String getSearchByValue() {
        return searchByValue;
    }

    public static void setSearchByValue(String searchByValue) {
        SearchByParams.searchByValue = searchByValue;
    }
}

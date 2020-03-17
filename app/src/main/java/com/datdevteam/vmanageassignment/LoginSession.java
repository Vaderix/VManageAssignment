package com.datdevteam.vmanageassignment;

public class LoginSession {

    private static String loggedUser="";
    private static String loggedPass="";

    public static String getLoggedPass() {
        return loggedPass;
    }

    public static void setLoggedPass(String loggedPass) {
        LoginSession.loggedPass = loggedPass;
    }

    private static boolean activeSession=false;

    public static String getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(String loggedUser) {
        LoginSession.loggedUser = loggedUser;
    }

    public static boolean isActiveSession() {
        return activeSession;
    }

    public static void setActiveSession(boolean activeSession) {
        LoginSession.activeSession = activeSession;
    }
}

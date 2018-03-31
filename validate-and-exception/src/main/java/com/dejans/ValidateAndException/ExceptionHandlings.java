package com.dejans.ValidateAndException;

import java.util.ArrayList;
import java.util.List;

public class ExceptionHandlings {

    private List<String> exceptions = new ArrayList<String>();
    private boolean firstHandling = false;

    private static ExceptionHandlings instance = null;

    // this is Singleton class
    private static ExceptionHandlings getInstance() {
        if (instance == null) {
            instance = new ExceptionHandlings();
        }
        return instance;
    }

    private String setMessageCode(String code, String message, int forUser) {
        if (forUser < 0 || forUser > 3)
            forUser = 0;
        String fu = String.valueOf(forUser);
        return (code + ":" + message + "$$$" + fu);

    }

    private static String convertParam(Object param) {
        if (param instanceof String) {
            return (String) param;
        } else if (param instanceof Integer) {
            return Integer.toString((Integer) param);
        }
        return "XXX";
    }

    private String convertParam(int param) {
        return String.valueOf(param);
    }

    public static String setMessage(String message, Object... param) {
        int i = 0;
        for (Object par : param) {
            i++;
            message = message.replace("$" + String.valueOf(i), convertParam(par));
        }
        return message;

    }

    private String setMessageFinal(String temp) {
        if (temp.contains("$$$")) {
            // it's user defined (custom) exception by this system 
            // TODO: SR: Treba da se definise prikaz greske Client ili User u zavisnosti
            // da li je to Client ili User (citati konfiguraciju)
            if (!temp.substring(temp.length() - 1, temp.length()).equals("0")) {
                temp = temp.substring(0, temp.length() - 4);
            } else {
                // SR: Ukoliko nije definisano da se prikazuje originalna poruka 
                // prikazuje se Ilegal error poruka
                temp = "Ilegal error, try again";
            }
        }
        return temp;
    }

    protected ExceptionHandlings() {
    }

    // forUser -
    //   0 (or null) - don't show error to Client (end customer) or User
    //               - insted this message show Internal error, try again
    //   1 - show message to Client but not to User
    //   2 - show message to User byt not Client
    //   3 - show message to User and Client 
    public static void throwException(String code, String message, int forUser) throws Exception {
        getInstance().throwExceptionInternal(code, message, forUser);
    }

    private void throwExceptionInternal(String code, String message, int forUser) throws Exception {
        String temp = setMessageCode(code, message, forUser);
        throw new Exception(temp);
    }

    public static void throwValidateException(String code, String message, int forUser) {
        getInstance().throwValidateExceptionInternal(code, message, forUser);
    }

    private void throwValidateExceptionInternal(String code, String message, int forUser) {
        String temp = setMessageCode(code, message, forUser);
        temp = setMessageFinal(temp);
        exceptions.add(temp);

    }

    //TODO: SR: ako postoji Exceptions koji nije izazvao prekid sada ce biti izazvan prekid
    public static void checkExceptionsThrow() throws Exception {
        getInstance().checkExceptionsThrowInternal();
    }

    private void checkExceptionsThrowInternal() throws Exception {
        if (exceptions.size() > 0) {
            throw new Exception("Validate exceptions");
        }

    }

    //TODO: SR: true - ako postoje greske koje su se desile a nisu prekinule
    public static boolean ifExceptions() {
        return getInstance().ifExceptionsInternal();
    }

    private boolean ifExceptionsInternal() {
        return (exceptions.size() > 0);
    }

    //TODO: SR: true - ako postoje greske koje su se desile a nisu prekinule
    public static void clearExceptions() {
        getInstance().clearExceptionsInternal();
    }

    private void clearExceptionsInternal() {
        exceptions = new ArrayList<String>();
        firstHandling = false;
        return;
    }

    public static void catchHandlings(Exception e) throws Exception {
        getInstance().catchHandlingsInternal(e);
    }

    private void catchHandlingsInternal(Exception e) throws Exception {
        System.out.println("CATCH MYEXCEPTIONS HANDLINGS");
        if (!firstHandling) {
            exceptions.add(e.toString().replace("java.lang.", ""));
            firstHandling = true;
        }
        throw e;
    }

    public static void catchHandlingsEnd(Exception e) throws Error {
        getInstance().catchHandlingsEndInternal(e);
    }

    private void catchHandlingsEndInternal(Exception e) throws Error {
        System.out.println("CATCH ERROR HANDLINGS END");
        try {
            if (!firstHandling) {
                exceptions.add(e.toString().replace("java.lang.", ""));
                firstHandling = true;
            }
            System.out.println("EXCEPTION STACK:");
            for (String ex : exceptions) {
                String temp = setMessageFinal(ex);
                System.out.println(temp);
            }
            System.out.println();
            e.printStackTrace();
        } finally {
            exceptions = new ArrayList<String>();
            firstHandling = false;
        }
        System.out.println();

    }

    public static String catchHandlingsHTTP(Exception e) throws Error {
        return getInstance().catchHandlingsHTTPInternal(e);
    }

    private String catchHandlingsHTTPInternal(Exception e) throws Error {
        String tempString = new String();
        try {
            System.out.println("CATCH ERROR HANDLINGS END");
            if (!firstHandling) {
                exceptions.add(e.toString().replace("java.lang.", ""));
                firstHandling = true;
            }
            System.out.println("EXCEPTION STACK:");
            for (String ex : exceptions) {
                String temp = setMessageFinal(ex);
                tempString += temp + "<br/>";
            }
            System.out.println();
            e.printStackTrace();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("SUPERMEN");

            System.out.println();
            System.out.println();
        } finally {
            if (tempString == null || tempString == "") {
                tempString = "Ilegal error, try again<br/>";
            }
            exceptions = new ArrayList<String>();
            firstHandling = false;
            return tempString;
        }
    }

    public static void catchHandlings(Error e) throws Error {
        getInstance().catchHandlingsInternal(e);
    }

    private void catchHandlingsInternal(Error e) throws Error {
        System.out.println("CATCH ERROR HANDLINGS");
        if (!firstHandling) {
            exceptions.add(e.toString().replace("java.lang.", ""));
            firstHandling = true;
        }
        throw e;

    }

}
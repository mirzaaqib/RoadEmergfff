package com.mirzaaqibbeg.roademergencies;

import com.mirzaaqibbeg.roademergencies.DataStructure.ContactLocal;

import java.util.ArrayList;

/**
 * Created by Mirzaaqibbeg on 04-03-2016.
 * // TODO: 11/03/16  
 */
public  class Fields {
    public static final String BASE_URL="http://aqib.onstart.in/";
    public static final String URL_MECHANIC=BASE_URL+"app/mechanic.php";
    public static final String URL_RESTAURANT=BASE_URL+"app/restaurant.php";
    public static final String MECHANIC="mechanic";
    public static final String PUNCTURE = "puncture";
    public static final String RESTAURANT = "restaurant";
    public static final String BARS = "bars";
    public static final String URL_FUEL = BASE_URL+"app/gas.php";
    public static final String GAS = "gas";
    public static final String FUEL="fuel";
    public static final String PLACE = "place";
    public static final String SETTINGS = "settings";
    public static final String CONTACTS = "contacts";
    public static final String NUMBER = "number";
    public static final String URL_ADVERTISEMENT = BASE_URL+"app/advertisement/advertisement.php";

    public static
    double LATITUDE=0.0;
    public static double LONGITUDE=0.0;
    public static ArrayList<ContactLocal> contactLocalArrayList=new ArrayList<>();

}

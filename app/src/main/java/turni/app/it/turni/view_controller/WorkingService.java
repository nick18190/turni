package turni.app.it.turni.view_controller;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

import model.Util;
import turni.app.it.turni.R;

public class WorkingService  extends Service {
    // Debug variables
    private static final boolean DEBUG = true;
    private static final String TAG = "WORKING SERVICE";
    /**
     * Indicates that the day is a RECUPERO
     */
    private final static String RECUPERO = "N.REC";
    //Constants representing the work place
    private final static String VERONA = "VR1";
    private final static String BASSONA = "VR2";
    //Constants representing the shifts time
    private final static String MATTINA = "07.00-14.12";
    private final static String POMERIGGIO = "14.10-21.22";
    private final static String NOTTURNO_00 = "00.01-07.13";
    private final static String NOTTURNO_21 = "21.15-04.27";
    private final static String NOTTURNO_FESTIVO = "00.01-08.00";
    private final static String MATTINA_FESTIVO = "08.00-16.00";
    private final static String POMERIGGIO_FESTIVO = "16.00-24.00";

    private static final String SP_CALENDAR_USED = "calendar used";
    private static final String SP_ACCOUNT_USED = "account used";
    private static final String BASSONA_COLOR_DEFAULT = "bassona color default";
    private static final String VERONA_COLOR_DEFAULT = "result color selected";
    /**
     * Constant used in onStartCommand() to start the creation of the events
     */
    private static final String CREATE_EVENTS = "create events";
    private static final CharSequence ASSENZA = "Assenza";
    private static final CharSequence ASSENZE = "Assenze";
    private static boolean isCreated;
    private WorkingService thisService;
    private SharedPreferences mSharedPref;

    /**
     * Default constructor, not used
     */
    public WorkingService() {
    }

    /**
     * @return true if the service has been created
     */
    public static boolean isServiceCreated() {
        return isCreated;
    }

    /**
     * Change the isServiceCreated() value
     *
     * @param value Desired boolean
     */
    private static void setServiceCreated(boolean value) {
        isCreated = value;
    }

    private static boolean isVerona(String line) {
        if (line.contains(VERONA))
            return true;
        if (line.contains(BASSONA))
            return false;
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onCreate() {

        //The service has been created
        setServiceCreated(true);
        thisService = this;
        super.onCreate();
        mSharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null)
            return super.onStartCommand(intent, flags, startId);
        String action = intent.getAction();
        if (DEBUG)
            Log.d(TAG, "Action:  " + action);
        //Start the creation of the events
        if (CREATE_EVENTS.equals(action)) {
            //Get the desired data (text to process)
            String text = intent.getStringExtra(CREATE_EVENTS);
            Toast.makeText(this, "Start event command", Toast.LENGTH_LONG).show();
            //Create a background thread that does all the heavy work
            Thread thread = new Thread(new BackgroundThread(text));
            thread.start();
        }
        return 0;
    }

    /**
     * DO NOTHING
     *
     * @param intent
     * @return NULL
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class BackgroundThread implements Runnable {
        private String mCompleteText;
        private int mCalID;
        private ContentResolver mContentResolver;

        public BackgroundThread(String text) {
            mCompleteText = text;

        }

        /**
         * Method used by the background thread to process the text and create the events
         */
        @Override
        public void run() {
            if (DEBUG)
                Log.d(TAG, "thread start");
            //Put all the string to upper case to avoid errors reading the string
            mCompleteText = mCompleteText.toUpperCase();

            Scanner sc = new Scanner(mCompleteText);
            String line = "";
            //Get the calendar ID where the events should be put
            mCalID = Util.getCalendarID(thisService, mSharedPref.getString(SP_CALENDAR_USED, null), mSharedPref.getString(SP_ACCOUNT_USED, null));
            long startMillis = 0;
            long endMillis = 0;
            boolean isFullDay;
            boolean hasToCreateEvent;
            boolean isVerona, isBassona;
            String titleText, placeText = "";
            String recText = "RECUPERO";
            Calendar beginTime = null;
            Calendar endTime = null;
            //Process every line of the input text
            while (sc.hasNextLine()) {
                hasToCreateEvent = false;
                isFullDay = false;
                isVerona = isBassona = false;
                line = sc.nextLine();
                titleText = "TURNO LAVORATIVO";
                //Get the date for this event (year,month,day)
                beginTime = Util.getEventDate(line);
                endTime = null;
                //Find the work place

                if (line.contains(RECUPERO)|| line.contains(ASSENZA)||line.contains(ASSENZE)) {
                    hasToCreateEvent = false;
                }
                //Set the shift hours for each case
                if (line.contains(MATTINA)) {
                    beginTime.set(Calendar.HOUR_OF_DAY, 7);
                    beginTime.set(Calendar.MINUTE, 0);
                    endTime = (Calendar) beginTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, 14);
                    endTime.set(Calendar.MINUTE, 12);
                    hasToCreateEvent = true;
                }
                //                    if (DEBUG)
//                        Log.d(TAG, "get timezone  after timemillis"+beginTime.getTimeZone());
//                    if (DEBUG)
//                        Log.d(TAG, "Start hour is " + beginTime);
//                    if (DEBUG)
//                        Log.d(TAG, "finish hour is  " + endTime);
                if (line.contains(POMERIGGIO)) {
                    beginTime.set(Calendar.HOUR_OF_DAY, 14);
                    beginTime.set(Calendar.MINUTE, 10);
                    endTime = (Calendar) beginTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, 21);
                    endTime.set(Calendar.MINUTE, 22);
                    hasToCreateEvent = true;
                }
                if (line.contains(NOTTURNO_00)) {
                    beginTime.set(Calendar.HOUR_OF_DAY, 0);
                    beginTime.set(Calendar.MINUTE, 1);
                    endTime = (Calendar) beginTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, 7);
                    endTime.set(Calendar.MINUTE, 13);
                    hasToCreateEvent = true;
                }
                //TODO controllare add day
                if (line.contains(NOTTURNO_21)) {
                    beginTime.set(Calendar.HOUR_OF_DAY, 21);
                    beginTime.set(Calendar.MINUTE, 15);
                    endTime = (Calendar) beginTime.clone();
                    //This turn ends the next day
                    endTime.add(Calendar.DAY_OF_YEAR, 1);
                    endTime.set(Calendar.HOUR_OF_DAY, 4);
                    endTime.set(Calendar.MINUTE, 27);
                    hasToCreateEvent = true;
                }
                if (line.contains(NOTTURNO_FESTIVO)) {
                    beginTime.set(Calendar.HOUR_OF_DAY, 0);
                    beginTime.set(Calendar.MINUTE, 1);
                    endTime = (Calendar) beginTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, 8);
                    endTime.set(Calendar.MINUTE, 0);
                    hasToCreateEvent = true;
                }
                if (line.contains(MATTINA_FESTIVO)) {
                    beginTime.set(Calendar.HOUR_OF_DAY, 8);
                    beginTime.set(Calendar.MINUTE, 0);
                    endTime = (Calendar) beginTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, 16);
                    endTime.set(Calendar.MINUTE, 0);
                    hasToCreateEvent = true;
                }
                if (line.contains(POMERIGGIO_FESTIVO)) {
                    beginTime.set(Calendar.HOUR_OF_DAY, 16);
                    beginTime.set(Calendar.MINUTE, 0);
                    endTime = (Calendar) beginTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, 23);
                    endTime.set(Calendar.MINUTE, 59);
                    hasToCreateEvent = true;
                }

                if (line.contains(VERONA)) {
                    isVerona = true;
                    placeText = "a San Michele";
                }
                if (line.contains(BASSONA)) {
                    isBassona = true;
                    placeText = "in Bassona";
                }


                //If useful information has been found in the line create the event
                if (hasToCreateEvent) {
                    if (DEBUG)
                        Log.d(TAG, "create event " + beginTime);
                    startMillis = beginTime.getTimeInMillis();
                    endMillis = endTime.getTimeInMillis();
                    mContentResolver = thisService.getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Events.DTSTART, startMillis);
                    //Set the event for the all day if necessary
                    if (isFullDay)
                        values.put(CalendarContract.Events.ALL_DAY, true);
                    values.put(CalendarContract.Events.DTEND, endMillis);
                    //TODO opzione modifica titolo
                    if (isFullDay) {
                        values.put(CalendarContract.Events.TITLE, recText);
                        if (DEBUG)
                            Log.d(TAG, "Full day SET");
                    } else
                        values.put(CalendarContract.Events.TITLE, titleText);
                    //TODO opzione modifica descrizione
                    values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
                    if (isVerona) {
                        values.put(CalendarContract.Events.EVENT_COLOR_KEY, mSharedPref.getInt(VERONA_COLOR_DEFAULT,1));
                        values.put(CalendarContract.Events.EVENT_LOCATION, "Via Monte Bianco, 18\n" +
                                "37132 Verona VR");
                    }
                    if (isBassona) {
                        values.put(CalendarContract.Events.EVENT_COLOR_KEY, mSharedPref.getInt(BASSONA_COLOR_DEFAULT,1));
                        values.put(CalendarContract.Events.EVENT_LOCATION, "Via della Meccanica, 1\n" +
                                "37139 Verona VR");
                    }
                    values.put(CalendarContract.Events.CALENDAR_ID, mCalID);
                    TimeZone tz = TimeZone.getDefault();
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
                    Uri uri1 = mContentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
                    // get the event ID that is the last element in the
                    //  Uri  long eventID = Long.parseLong(uri1.getLastPathSegment());
                }
            }
            thisService.stopSelf();
        }
    }

}


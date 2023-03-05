package si.sintal.sintaltehnika;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SINTAL_TEHNIKA_TABLET.db";
    private SQLiteDatabase mDatabase;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTablesLogin()
    {
        mDatabase = this.getReadableDatabase();
        String CREATE_TEHNIKA_LOGIN = "CREATE TABLE IF NOT EXISTS sintal_teh_upo " +
                " (user_id INT NOT NULL, " +
                " upo_ime TEXT, " +
                " ime TEXT, " +
                " priimek TEXT, " +
                " prijava TEXT, " +
                " podpis BYTEA," +
                " email TEXT, " +
                " admin_dostop INT," +
                " servis INT," +
                " montaza INT," +
                " vzdrzevanje INT" +
                ")";
        //"CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS
        //+ " ( " + KEY_SIFRA_LARGO + " INTEGER NOT NULL, " + KEY_UPORABNIK + " TEXT NOT NULL, " + KEY_GESLO + " TEXT NOT NULL )";
        mDatabase.execSQL(CREATE_TEHNIKA_LOGIN);

        String CREATE_TEHNIKA_DELAVCI = "CREATE TABLE IF NOT EXISTS sintal_teh_delavci (" +
            "  tehnik_id int(11) NOT NULL," +
            "  naziv varchar(150) NOT NULL," +
            "  seznam_servis int(11) NOT NULL," +
            "  seznam_montaza int(11) NOT NULL," +
            "  seznam_vzdrzevanje int(11) NOT NULL," +
            "  user_id int(11) NOT NULL" +
            ");";
        mDatabase.execSQL(CREATE_TEHNIKA_DELAVCI);

        String CREATE_TEHNIKA_SN = "CREATE TABLE IF NOT EXISTS sintal_teh_sn ( " +
                "id int(11) NOT NULL, " +
        "DELOVNI_NALOG varchar(20) NOT NULL, " +
        "NAZIV TEXT NULL, " +
        "OE varchar(20) NULL, " +
        "DATUM_ZACETEK datetime NULL, " +
        "DATUM_KONEC datetime  NULL, " +
        "OPIS TEXT  NULL, " +
        "KLIENT TEXT  NULL, " +
        "TIP_DEL_NAL varchar(2)  NULL, " +
        "STATUS_AKT varchar(1)  NULL, " +
        "XUSER varchar(6)  NULL, " +
        "XDATETIME datetime  NULL, " +
        "VRSTA_DN varchar(10)  NULL, " +
        "STATUS_DN varchar(2)  NULL, " +
        "PLANIRANE_URE decimal(15,4)  NULL, " +
        "DELOVNI_NALOG_IZV varchar(20)  NULL, " +
        "OBJEKT_FAKT varchar(20)  NULL, " +
        "ODGOVORNA_OSEBA varchar(50)  NULL, " +
        "IZDAJATELJ_NALOGA varchar(30)  NULL, " +
        "TIP_NAROCILA varchar(2)  NULL, " +
        "TIP_VZDRZEVANJA varchar(2)  NULL, " +
        "KODA_OBJEKTA varchar(30)  NULL, " +
        "NAV_ERROR varchar(80)  NULL, " +
        "NAV_PRIPADNOST varchar(10)  NULL, " +
        "NAV_KODA_OBJ varchar(10)  NULL, " +
        "PRIPADNOST varchar(50)  NULL, " +
        "PRIPADNOST_VNC varchar(30)  NULL, " +
        "NAV_STATUS_PODIZV varchar(20)  NULL, " +
        "NAV_KLIENT varchar(20)  NULL, " +
        "NAV_OBJEKT_FAKT varchar(20)  NULL, " +
        "VODJA_NALOGA varchar(100)  NULL, " +
        "NAROCNIK_NAZIV TEXT  NULL, " +
        "NAROCNIK_NASLOV TEXT  NULL, " +
        "NAROCNIK_ULICA TEXT  NULL, " +
        "NAROCNIK_KRAJ TEXT  NULL, " +
        "NAROCNIK_HISNA_ST varchar(10)  NULL, " +
        "NAROCNIK_IME_SEKTORJA TEXT  NULL, " +
        "SEKTOR_NASLOV TEXT  NULL, " +
        "SEKTOR_POSTNA_ST TEXT  NULL, " +
        "OZNACI INT  DEFAULT 0 " +
        ") " ;
        mDatabase.execSQL(CREATE_TEHNIKA_SN);

        String CREATE_TEHNIKA_UPO_DELAVEC = "CREATE TABLE IF NOT EXISTS " +
        " sintal_teh_upo_delavci (" +
        "  user_id int NOT NULL," +
        "  tehnik_id int NOT NULL" +
        ")";
        mDatabase.execSQL(CREATE_TEHNIKA_UPO_DELAVEC);

        String CREATE_TEHNIKA_PRIPADNOST = "CREATE TABLE IF NOT EXISTS sintal_teh_pripadnost " +
                " (pripadnost varchar(2) NOT NULL, " +
                " pripadnost_vnc varchar(2) NOT NULL " +
                ")";
        mDatabase.execSQL(CREATE_TEHNIKA_PRIPADNOST);


        mDatabase.close();

    }

    public int usersLogin()
    {
        int stZapisov = 0;
        mDatabase = this.getReadableDatabase();
        String USERS_NADZOR_LOGIN = "select count(*) from sintal_teh_upo;";
        mCursor = mDatabase.rawQuery(USERS_NADZOR_LOGIN,null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();

        }
        if (mCursor.moveToFirst()) {
            //do {
            stZapisov = mCursor.getInt(0);
            //}
        }

        mDatabase.close();
        return stZapisov;
    }

    public void insertUpdatePripadnost(String pripadnost, String pripadnost_vnc ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_pripadnost where pripadnost ="+ pripadnost;
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            ContentValues values = new ContentValues();
            values.put("pripadnost",pripadnost);
            values.put("pripadnost_vnc",pripadnost_vnc);
            mDatabase.update("sintal_teh_pripadnost", values, "pripadnost=?", new String[]{pripadnost});

        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("pripadnost",pripadnost);
            values.put("pripadnost_vnc",pripadnost_vnc);

            mDatabase.insert("sintal_teh_pripadnost",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }

    public void insertUpdateTehnik(String tehnikID, String naziv, String servis, String montaza, String vzdrzevanje, String userID ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_delavci where tehnik_id ="+ tehnikID;
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            ContentValues values = new ContentValues();
            values.put("naziv",naziv);
            values.put("seznam_servis",servis);
            values.put("seznam_montaza",montaza);
            values.put("seznam_vzdrzevanje",vzdrzevanje);
            values.put("user_id",userID);
            mDatabase.update("sintal_teh_delavci", values, "tehnik_id=?", new String[]{tehnikID});

        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("tehnik_id",tehnikID);
            values.put("naziv",naziv);
            values.put("seznam_servis",servis);
            values.put("seznam_montaza",montaza);
            values.put("seznam_vzdrzevanje",vzdrzevanje);
            values.put("user_id",userID);

            mDatabase.insert("sintal_teh_delavci",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }

    public void deleteUpdateupoTehnik() {
        mDatabase = this.getWritableDatabase();
        mDatabase.execSQL("delete from sintal_teh_upo_delavci");
        mDatabase.close();

    }

    public void insertUpdateupoTehnik(String userID, String tehnikID ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_upo_delavci";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();

        }

        if (userID.equals("") && tehnikID.equals(""))
        {}
        else {
            ContentValues values = new ContentValues();
            values.put("user_ID", userID);
            values.put("tehnik_id", tehnikID);

            mDatabase.insert("sintal_teh_upo_delavci", null, values);
        }
            mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
            mDatabase.close();

        //return myList;
    }



    public void insertUpdateSN(String id,
                               String DELOVNI_NALOG,
                               String NAZIV,
                               String OE,
                               String DATUM_ZACETEK,
                               String DATUM_KONEC,
                               String OPIS,
                               String KLIENT,
                               String TIP_DEL_NAL,
                               String STATUS_AKT,
                               String XUSER,
                               String XDATETIME,
                               String VRSTA_DN,
                               String STATUS_DN,
                               String PLANIRANE_URE,
                               String DELOVNI_NALOG_IZV,
                               String OBJEKT_FAKT,
                               String ODGOVORNA_OSEBA,
                               String IZDAJATELJ_NALOGA,
                               String TIP_NAROCILA,
                               String TIP_VZDRZEVANJA,
                               String KODA_OBJEKTA,
                               String NAV_ERROR,
                               String NAV_PRIPADNOST,
                               String NAV_KODA_OBJ,
                               String PRIPADNOST,
                               String PRIPADNOST_VNC,
                               String NAV_STATUS_PODIZV,
                               String NAV_KLIENT,
                               String NAV_OBJEKT_FAKT,
                               String VODJA_NALOGA,
                               String NAROCNIK_NAZIV,
                               String NAROCNIK_NASLOV,
                               String NAROCNIK_ULICA,
                               String NAROCNIK_KRAJ,
                               String NAROCNIK_HISNA_ST,
                               String NAROCNIK_IME_SEKTORJA,
                               String SEKTOR_NASLOV,
                               String SEKTOR_POSTNA_ST


    ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_sn where id ="+ id;
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            /*
            ContentValues values = new ContentValues();
            values.put("DELOVNI_NALOG", DELOVNI_NALOG);
            values.put("NAZIV", NAZIV);
            values.put("OE", OE);
            values.put("DATUM_ZACETEK", DATUM_ZACETEK);
            values.put("DATUM_KONEC", DATUM_KONEC);
            values.put("OPIS", OPIS);
            values.put("KLIENT", KLIENT);
            values.put("TIP_DEL_NAL", TIP_DEL_NAL);
            values.put("STATUS_AKT", STATUS_AKT);
            values.put("XUSER", XUSER);
            values.put("XDATETIME", XDATETIME);
            values.put("VRSTA_DN", VRSTA_DN);
            values.put("STATUS_DN", STATUS_DN);
            values.put("PLANIRANE_URE", PLANIRANE_URE);
            values.put("DELOVNI_NALOG_IZV", DELOVNI_NALOG_IZV);
            values.put("OBJEKT_FAKT", OBJEKT_FAKT);
            values.put("ODGOVORNA_OSEBA", ODGOVORNA_OSEBA);
            values.put("IZDAJATELJ_NALOGA", IZDAJATELJ_NALOGA);
            values.put("TIP_NAROCILA", TIP_NAROCILA);
            values.put("TIP_VZDRZEVANJA", TIP_VZDRZEVANJA);
            values.put("KODA_OBJEKTA", KODA_OBJEKTA);
            values.put("NAV_ERROR", NAV_ERROR);
            values.put("NAV_PRIPADNOST", NAV_PRIPADNOST);
            values.put("NAV_KODA_OBJ", NAV_KODA_OBJ);
            values.put("PRIPADNOST", PRIPADNOST);
            values.put("PRIPADNOST_VNC", PRIPADNOST_VNC);
            values.put("NAV_STATUS_PODIZV", NAV_STATUS_PODIZV);
            values.put("NAV_KLIENT", NAV_KLIENT);
            values.put("NAV_OBJEKT_FAKT", NAV_OBJEKT_FAKT);
            values.put("VODJA_NALOGA", VODJA_NALOGA);
            values.put("NAROCNIK_NAZIV", NAROCNIK_NAZIV);
            values.put("NAROCNIK_NASLOV", NAROCNIK_NASLOV);
            values.put("NAROCNIK_ULICA", NAROCNIK_ULICA);
            values.put("NAROCNIK_KRAJ", NAROCNIK_KRAJ);
            values.put("NAROCNIK_HISNA_ST",NAROCNIK_HISNA_ST);
            values.put("NAROCNIK_IME_SEKTORJA", NAROCNIK_IME_SEKTORJA);
            values.put("SEKTOR_NASLOV", SEKTOR_NASLOV);
            values.put("SEKTOR_POSTNA_ST", SEKTOR_POSTNA_ST);
            mDatabase.update("sintal_teh_sn", values, "id=?", new String[]{id});
            */
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("DELOVNI_NALOG", DELOVNI_NALOG);
            values.put("NAZIV", NAZIV);
            values.put("OE", OE);
            values.put("DATUM_ZACETEK", DATUM_ZACETEK);
            values.put("DATUM_KONEC", DATUM_KONEC);
            values.put("OPIS", OPIS);
            values.put("KLIENT", KLIENT);
            values.put("TIP_DEL_NAL", TIP_DEL_NAL);
            values.put("STATUS_AKT", STATUS_AKT);
            values.put("XUSER", XUSER);
            values.put("XDATETIME", XDATETIME);
            values.put("VRSTA_DN", VRSTA_DN);
            values.put("STATUS_DN", STATUS_DN);
            values.put("PLANIRANE_URE", PLANIRANE_URE);
            values.put("DELOVNI_NALOG_IZV", DELOVNI_NALOG_IZV);
            values.put("OBJEKT_FAKT", OBJEKT_FAKT);
            values.put("ODGOVORNA_OSEBA", ODGOVORNA_OSEBA);
            values.put("IZDAJATELJ_NALOGA", IZDAJATELJ_NALOGA);
            values.put("TIP_NAROCILA", TIP_NAROCILA);
            values.put("TIP_VZDRZEVANJA", TIP_VZDRZEVANJA);
            values.put("KODA_OBJEKTA", KODA_OBJEKTA);
            values.put("NAV_ERROR", NAV_ERROR);
            values.put("NAV_PRIPADNOST", NAV_PRIPADNOST);
            values.put("NAV_KODA_OBJ", NAV_KODA_OBJ);
            values.put("PRIPADNOST", PRIPADNOST);
            values.put("PRIPADNOST_VNC", PRIPADNOST_VNC);
            values.put("NAV_STATUS_PODIZV", NAV_STATUS_PODIZV);
            values.put("NAV_KLIENT", NAV_KLIENT);
            values.put("NAV_OBJEKT_FAKT", NAV_OBJEKT_FAKT);
            values.put("VODJA_NALOGA", VODJA_NALOGA);
            values.put("NAROCNIK_NAZIV", NAROCNIK_NAZIV);
            values.put("NAROCNIK_NASLOV", NAROCNIK_NASLOV);
            values.put("NAROCNIK_ULICA", NAROCNIK_ULICA);
            values.put("NAROCNIK_KRAJ", NAROCNIK_KRAJ);
            values.put("NAROCNIK_HISNA_ST",NAROCNIK_HISNA_ST);
            values.put("NAROCNIK_IME_SEKTORJA", NAROCNIK_IME_SEKTORJA);
            values.put("SEKTOR_NASLOV", SEKTOR_NASLOV);
            values.put("SEKTOR_POSTNA_ST", SEKTOR_POSTNA_ST);

            //values.put("OZNACI",OZNACI);
            mDatabase.insert("sintal_teh_sn",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }


    public List<String> getTehnikiInString() {
        List<String> userList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM sintal_teh_delavci";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            do {
                userList.add(mCursor.getString(1));// +", " + mCursor.getString(0));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        mDatabase.close();
        // return contact list
        return userList;
    }

    public List<String> getPripadnostInString() {
        List<String> pripadnostList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM sintal_teh_pripadnost";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            do {
                pripadnostList.add(mCursor.getString(1));// +", " + mCursor.getString(0));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        mDatabase.close();
        // return contact list
        return pripadnostList;
    }

    public List<String> getTehnikiUporabnikInString(String user_id) {
        List<String> userList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "select * from sintal_teh_delavci std left join sintal_teh_upo_delavci stud on std.tehnik_id = stud.tehnik_id  where stud.user_id = "+ user_id;

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            do {
                userList.add(mCursor.getString(1));// +", " + mCursor.getString(0));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        mDatabase.close();
        if (userList.isEmpty()){
            ServisniNalog sn = new ServisniNalog();
            userList.add("");
        }
        // return contact list
        return userList;
    }

    public ArrayList<HashMap<String, String>> GetSeznamSN(String datum, String vodja_sn){

        ArrayList<HashMap<String, String>> seznamSN = new ArrayList<>();
        String query = "SELECT  * FROM sintal_teh_sn where datum_zacetek = '" + datum + "' and vodja_sn = 'Null'";
        if (vodja_sn.equals("-1"))
        {
            query = "SELECT  * FROM sintal_teh_sn where strftime('%d.%m.%Y', DATUM_ZACETEK) = '" + datum + "' and VODJA_NALOGA = 'null'";
        }
        else {
            query = "SELECT  * FROM sintal_teh_sn where strftime('%d.%m.%Y', DATUM_ZACETEK) = '" + datum + "' and VODJA_NALOGA <> 'null'";
        }
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            HashMap<String,String> sn = new HashMap<>();
            sn.put("stevilka_sn",mCursor.getString(mCursor.getColumnIndex("DELOVNI_NALOG")));
            sn.put("datum_zacetek",mCursor.getString(mCursor.getColumnIndex("DATUM_ZACETEK")));
            sn.put("narocnik",mCursor.getString(mCursor.getColumnIndex("NAROCNIK_NAZIV")));
            sn.put("opis",mCursor.getString(mCursor.getColumnIndex("OPIS")));
            sn.put("vodja_naloga",mCursor.getString(mCursor.getColumnIndex("VODJA_NALOGA")));
            seznamSN.add(sn);
        }

        mCursor.close();
        mDatabase.close();
        return  seznamSN;
    }

    public void updateSNServiser(String id, String serviser) {
        String GET_USER = "SELECT * FROM sintal_teh_sn where id =" + id;
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(GET_USER, null);
        //NadzorXML n;
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            ContentValues values = new ContentValues();
            //values.put("id", id);

            values.put("VODJA_NALOGA", serviser);
            mDatabase.update("sintal_teh_sn", values, "id=?", new String[]{id});

        }
        mCursor.close();
        mDatabase.close();
    }

    public void updateSNOznaci(String id, int izberi) {
        String GET_USER = "SELECT * FROM sintal_teh_sn where id =" + id;
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(GET_USER, null);
        //NadzorXML n;
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            ContentValues values = new ContentValues();
            //values.put("id", id);

            values.put("OZNACI", izberi);
            mDatabase.update("sintal_teh_sn", values, "id=?", new String[]{id});

        }
        mCursor.close();
        mDatabase.close();
    }

    public void updateSNOznaciDatum(String datum) {
        String GET_USER = "SELECT * FROM sintal_teh_sn where strftime('%d.%m.%Y', DATUM_ZACETEK) ='" + datum+"'";
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(GET_USER, null);
        //NadzorXML n;
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            ContentValues values = new ContentValues();
            //values.put("id", id);

            values.put("OZNACI", 0);
            mDatabase.update("sintal_teh_sn", values, "strftime('%d.%m.%Y', DATUM_ZACETEK)=?", new String[]{datum});

        }
        mCursor.close();
        mDatabase.close();
    }

    public void updateSNStatusAkt(String id, String statusAkt) {
        String GET_USER = "SELECT * FROM sintal_teh_sn where id ='" + id+"'";
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(GET_USER, null);
        //NadzorXML n;
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            ContentValues values = new ContentValues();
            values.put("STATUS_AKT", statusAkt);
            values.put("OZNACI", 0);
            mDatabase.update("sintal_teh_sn", values, "id=?", new String[]{id});

        }
        mCursor.close();
        mDatabase.close();
    }

    public ArrayList<ServisniNalog> GetSeznamSNDodelitev(String datum, String vodja_sn){

        ArrayList<ServisniNalog> list = new ArrayList<ServisniNalog>();
        String query = "SELECT  * FROM sintal_teh_sn where datum_zacetek = '" + datum + "' and vodja_sn = 'Null'";
        if (vodja_sn.equals("-1"))
        {
            query = "SELECT  * FROM sintal_teh_sn where strftime('%d.%m.%Y', DATUM_ZACETEK) = '" + datum + "' and VODJA_NALOGA = '' and STATUS_AKT = 'A' ";
        }
        else if (vodja_sn.equals("0")) {
            query = "SELECT  * FROM sintal_teh_sn where strftime('%d.%m.%Y', DATUM_ZACETEK) = '" + datum + "' and VODJA_NALOGA <> '' and STATUS_AKT = 'A' ";
        }
        else if (vodja_sn.equals("1"))
        {
            query = "SELECT  * FROM sintal_teh_sn where strftime('%d.%m.%Y', DATUM_ZACETEK) = '" + datum + "' and STATUS_AKT = 'S'";
        }
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            ServisniNalog sn = new ServisniNalog();
            sn.setid(mCursor.getInt(mCursor.getColumnIndex("id")));
            sn.setDelovniNalog(mCursor.getString(mCursor.getColumnIndex("DELOVNI_NALOG")));
            sn.setDatumZacetek(mCursor.getString(mCursor.getColumnIndex("DATUM_ZACETEK")));
            sn.setOpis(mCursor.getString(mCursor.getColumnIndex("OPIS")));
            sn.setVodjaNaloga(mCursor.getString(mCursor.getColumnIndex("VODJA_NALOGA")));
            sn.setOznacen(mCursor.getInt(mCursor.getColumnIndex("OZNACI")));
            sn.setOdgovornaOseba(mCursor.getString(mCursor.getColumnIndex("ODGOVORNA_OSEBA")));
            sn.setKodaObjekta(mCursor.getString(mCursor.getColumnIndex("KODA_OBJEKTA")));
            sn.setPripadnostNaziv(mCursor.getString(mCursor.getColumnIndex("PRIPADNOST_VNC")));
            sn.setNarocnikNaziv(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_NAZIV")));
            sn.setNarocnikNaslov(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_NASLOV")));
            sn.setNarocnikUlica(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_ULICA")));
            sn.setNarocnikKraj(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_KRAJ")));
            sn.setNarocnikHisnaSt(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_HISNA_ST")));
            sn.setNarocnikSektor(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_IME_SEKTORJA")));
            sn.setSektroNaslov(mCursor.getString(mCursor.getColumnIndex("SEKTOR_NASLOV")));
            sn.setSektorPostnaSt(mCursor.getString(mCursor.getColumnIndex("SEKTOR_POSTNA_ST")));
            sn.setIzdajatelj(mCursor.getString(mCursor.getColumnIndex("IZDAJATELJ_NALOGA")));
            sn.setStatus(mCursor.getString(mCursor.getColumnIndex("STATUS_AKT")));
            sn.setTipNarocila(mCursor.getInt(mCursor.getColumnIndex("TIP_NAROCILA")));
            sn.setTipVzdrzevanja(mCursor.getInt(mCursor.getColumnIndex("TIP_VZDRZEVANJA")));
            sn.setPripadnost(mCursor.getString(mCursor.getColumnIndex("PRIPADNOST")));
            //sn.setOznacen(0);
            list.add(sn);
        }

        mCursor.close();
        mDatabase.close();
        return  list;
    }

    public ArrayList<ServisniNalog> GetSeznamSNUporabnik(String datum, String vodja_sn){

        ArrayList<ServisniNalog> list = new ArrayList<ServisniNalog>();
        String query = "SELECT  * FROM sintal_teh_sn where datum_zacetek = '" + datum + "' and vodja_sn = 'Null'";
        query = "SELECT  * FROM sintal_teh_sn where strftime('%d.%m.%Y', DATUM_ZACETEK) = '" + datum + "' and VODJA_NALOGA = '"+vodja_sn+"'";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            ServisniNalog sn = new ServisniNalog();
            sn.setid(mCursor.getInt(mCursor.getColumnIndex("id")));
            sn.setDelovniNalog(mCursor.getString(mCursor.getColumnIndex("DELOVNI_NALOG")));
            sn.setDatumZacetek(mCursor.getString(mCursor.getColumnIndex("DATUM_ZACETEK")));
            sn.setOpis(mCursor.getString(mCursor.getColumnIndex("OPIS")));
            sn.setVodjaNaloga(mCursor.getString(mCursor.getColumnIndex("VODJA_NALOGA")));
            sn.setOznacen(mCursor.getInt(mCursor.getColumnIndex("OZNACI")));
            sn.setOdgovornaOseba(mCursor.getString(mCursor.getColumnIndex("ODGOVORNA_OSEBA")));
            sn.setKodaObjekta(mCursor.getString(mCursor.getColumnIndex("KODA_OBJEKTA")));
            sn.setPripadnostNaziv(mCursor.getString(mCursor.getColumnIndex("PRIPADNOST_VNC")));
            sn.setNarocnikNaziv(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_NAZIV")));
            sn.setNarocnikNaslov(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_NASLOV")));
            sn.setNarocnikUlica(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_ULICA")));
            sn.setNarocnikKraj(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_KRAJ")));
            sn.setNarocnikHisnaSt(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_HISNA_ST")));
            sn.setNarocnikSektor(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_IME_SEKTORJA")));
            sn.setSektroNaslov(mCursor.getString(mCursor.getColumnIndex("SEKTOR_NASLOV")));
            sn.setSektorPostnaSt(mCursor.getString(mCursor.getColumnIndex("SEKTOR_POSTNA_ST")));
            sn.setIzdajatelj(mCursor.getString(mCursor.getColumnIndex("IZDAJATELJ_NALOGA")));
            sn.setStatus(mCursor.getString(mCursor.getColumnIndex("STATUS_AKT")));
            sn.setTipNarocila(mCursor.getInt(mCursor.getColumnIndex("TIP_NAROCILA")));
            sn.setTipVzdrzevanja(mCursor.getInt(mCursor.getColumnIndex("TIP_VZDRZEVANJA")));
            sn.setPripadnost(mCursor.getString(mCursor.getColumnIndex("PRIPADNOST")));
            //sn.setOznacen(0);
            list.add(sn);
        }

        mCursor.close();
        mDatabase.close();
        return  list;
    }

    public ServisniNalog vrniSN(int idSN){

        ServisniNalog sn = new ServisniNalog();
        String query = "SELECT  * FROM sintal_teh_sn where id ="+idSN+"";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            //ServisniNalog sn = new ServisniNalog();
            sn.setid(mCursor.getInt(mCursor.getColumnIndex("id")));
            sn.setDelovniNalog(mCursor.getString(mCursor.getColumnIndex("DELOVNI_NALOG")));
            sn.setDatumZacetek(mCursor.getString(mCursor.getColumnIndex("DATUM_ZACETEK")));
            sn.setOpis(mCursor.getString(mCursor.getColumnIndex("OPIS")));
            sn.setVodjaNaloga(mCursor.getString(mCursor.getColumnIndex("VODJA_NALOGA")));
            sn.setOznacen(mCursor.getInt(mCursor.getColumnIndex("OZNACI")));
            sn.setOdgovornaOseba(mCursor.getString(mCursor.getColumnIndex("ODGOVORNA_OSEBA")));
            sn.setKodaObjekta(mCursor.getString(mCursor.getColumnIndex("KODA_OBJEKTA")));
            sn.setPripadnostNaziv(mCursor.getString(mCursor.getColumnIndex("PRIPADNOST_VNC")));
            sn.setNarocnikNaziv(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_NAZIV")));
            sn.setNarocnikNaslov(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_NASLOV")));
            sn.setNarocnikUlica(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_ULICA")));
            sn.setNarocnikKraj(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_KRAJ")));
            sn.setNarocnikHisnaSt(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_HISNA_ST")));
            sn.setNarocnikSektor(mCursor.getString(mCursor.getColumnIndex("NAROCNIK_IME_SEKTORJA")));
            sn.setSektroNaslov(mCursor.getString(mCursor.getColumnIndex("SEKTOR_NASLOV")));
            sn.setSektorPostnaSt(mCursor.getString(mCursor.getColumnIndex("SEKTOR_POSTNA_ST")));
            sn.setIzdajatelj(mCursor.getString(mCursor.getColumnIndex("IZDAJATELJ_NALOGA")));
            sn.setStatus(mCursor.getString(mCursor.getColumnIndex("STATUS_AKT")));
            sn.setTipNarocila(mCursor.getInt(mCursor.getColumnIndex("TIP_NAROCILA")));
            sn.setTipVzdrzevanja(mCursor.getInt(mCursor.getColumnIndex("TIP_VZDRZEVANJA")));
            sn.setPripadnost(mCursor.getString(mCursor.getColumnIndex("PRIPADNOST")));
            //sn.setOznacen(0);

        }

        mCursor.close();
        mDatabase.close();
        return  sn;
    }

    public void insertUpdateUser(String userID, String upIme, String ime, String priimek, String prijava, String email, String admin_dostop, String servis, String montaza, String vzdrzevanje ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_upo where user_id ="+ userID;
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            ContentValues values = new ContentValues();
            values.put("upo_ime",upIme);
            values.put("ime",ime);
            values.put("priimek",priimek);
            values.put("prijava",prijava);
            values.put("email",email);
            values.put("admin_dostop",admin_dostop);
            values.put("servis",servis);
            values.put("montaza",montaza);
            values.put("vzdrzevanje",vzdrzevanje);
            mDatabase.update("sintal_teh_upo", values, "user_id=?", new String[]{userID});

        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("user_id",userID);
            values.put("upo_ime",upIme);
            values.put("ime",ime);
            values.put("priimek",priimek);
            values.put("prijava",prijava);
            values.put("email",email);
            values.put("admin_dostop",admin_dostop);
            values.put("servis",servis);
            values.put("montaza",montaza);
            values.put("vzdrzevanje",vzdrzevanje);

            mDatabase.insert("sintal_teh_upo",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }



    public ArrayList<String> getUserLogin(String upo_ime, String prijava) {

        ArrayList<String> user=new ArrayList<String>();
        String user_id = "";
        String ime = "";
        String priimek = "";
        String geslo = "";
        String admin_dostop = "";
        String email = "";
        String servis = "";
        String montaza = "";
        String vzdrzevanje = "";

        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_upo where upo_ime = '" + upo_ime + "'";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            user_id = mCursor.getString(0);
            ime = mCursor.getString(2);
            priimek = mCursor.getString(3);
            geslo = mCursor.getString(4);
            email = mCursor.getString(6);
            admin_dostop = mCursor.getString(7);
            servis = mCursor.getString(8);
            montaza = mCursor.getString(9);
            vzdrzevanje = mCursor.getString(10);

        }

        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();
        if (geslo.equals(prijava))
        {
            user.add(user_id);
            user.add(ime);
            user.add(priimek);
            user.add(prijava);
            user.add(email);
            user.add(admin_dostop);
            user.add(servis);
            user.add(montaza);
            user.add(vzdrzevanje);
        }
        else
        {
            user.add(user_id);
            user.add(ime);
            user.add(priimek);
            user.add("");
            user.add(email);
            user.add("0");
            user.add("0");
            user.add("0");
            user.add("0");
        }
        return user;
    }



    private Cursor mCursor = new Cursor() {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public boolean move(int offset) {
            return false;
        }

        @Override
        public boolean moveToPosition(int position) {
            return false;
        }

        @Override
        public boolean moveToFirst() {
            return false;
        }

        @Override
        public boolean moveToLast() {
            return false;
        }

        @Override
        public boolean moveToNext() {
            return false;
        }

        @Override
        public boolean moveToPrevious() {
            return false;
        }

        @Override
        public boolean isFirst() {
            return false;
        }

        @Override
        public boolean isLast() {
            return false;
        }

        @Override
        public boolean isBeforeFirst() {
            return false;
        }

        @Override
        public boolean isAfterLast() {
            return false;
        }

        @Override
        public int getColumnIndex(String columnName) {
            return 0;
        }

        @Override
        public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
            return 0;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return null;
        }

        @Override
        public String[] getColumnNames() {
            return new String[0];
        }

        @Override
        public int getColumnCount() {
            return 0;
        }

        @Override
        public byte[] getBlob(int columnIndex) {
            return new byte[0];
        }

        @Override
        public String getString(int columnIndex) {
            return null;
        }

        @Override
        public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

        }

        @Override
        public short getShort(int columnIndex) {
            return 0;
        }

        @Override
        public int getInt(int columnIndex) {
            return 0;
        }

        @Override
        public long getLong(int columnIndex) {
            return 0;
        }

        @Override
        public float getFloat(int columnIndex) {
            return 0;
        }

        @Override
        public double getDouble(int columnIndex) {
            return 0;
        }

        @Override
        public int getType(int columnIndex) {
            return 0;
        }

        @Override
        public boolean isNull(int columnIndex) {
            return false;
        }

        @Override
        public void deactivate() {

        }

        @Override
        public boolean requery() {
            return false;
        }

        @Override
        public void close() {

        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public void registerContentObserver(ContentObserver observer) {

        }

        @Override
        public void unregisterContentObserver(ContentObserver observer) {

        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void setNotificationUri(ContentResolver cr, Uri uri) {

        }

        @Override
        public Uri getNotificationUri() {
            return null;
        }

        @Override
        public boolean getWantsAllOnMoveCalls() {
            return false;
        }

        @Override
        public void setExtras(Bundle extras) {

        }

        @Override
        public Bundle getExtras() {
            return null;
        }

        @Override
        public Bundle respond(Bundle extras) {
            return null;
        }
    };
}

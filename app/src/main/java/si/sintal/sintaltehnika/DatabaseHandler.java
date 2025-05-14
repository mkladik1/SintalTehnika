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

import com.itextpdf.styledxmlparser.jsoup.internal.StringUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import si.sintal.sintaltehnika.ui.main.DelovniNalogVZ;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZPeriodika;
import si.sintal.sintaltehnika.ui.main.RegCasa;
import si.sintal.sintaltehnika.ui.main.SNArtikel;
import si.sintal.sintaltehnika.ui.main.Serviser;
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
            "  user_id int(11) NOT NULL," +
            "  ime_regala varchar(20) NULL," +
            "  ime_vira varchar(20) NULL" +
            //"  user_id int(11) NOT NULL" +
            ");";
        mDatabase.execSQL(CREATE_TEHNIKA_DELAVCI);

        String CREATE_TEHNIKA_SN = "CREATE TABLE IF NOT EXISTS sintal_teh_sn ( " +
                "id int(11) NOT NULL, " +
                "DELOVNI_NALOG varchar(20) NOT NULL, " +
                "NAZIV TEXT NULL, " +
                "OE varchar(20) NULL, " +
                "DATUM_ZACETEK date NULL, " +
                "DATUM_KONEC date  NULL, " +
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
                "OZNACI INT  DEFAULT 0, " +
                "GARANCIJA INT NULL," +
                "URE_DELO decimal(15,2) NULL," +
                "URE_PREVOZ decimal(15,2) NULL," +
                "STEVILO_KM decimal(15,2) NULL," +
                "DATUM_DODELITVE date  NULL, " +
                "DATUM_IZVEDBE date  NULL, " +
                "OPIS_OKVARE TEXT  NULL, " +
                "OPIS_POSTOPKA TEXT  NULL, " +
                "PODPIS_NAROCNIK IMAGEDATA BLOB, " +
                "PODPIS_SERVISER IMAGEDATA BLOB, " +
                "DATUM_PODPISA date  NULL, " +
                "PRENOS INT  DEFAULT 0, " +
                "DATUM_PRENOSA datetime  NULL " +
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

        String CREATE_TEHNIKA_ARTIKLI = "CREATE TABLE IF NOT EXISTS sintal_teh_artikli (" +
                "  No_ varchar(20) NOT NULL," +
                "  Naziv text NOT NULL," +
                "  Naziv_iskanje text DEFAULT NULL," +
                "  Kratka_oznaka text DEFAULT NULL," +
                "  Merska_enota text DEFAULT NULL" +
                ")";
        mDatabase.execSQL(CREATE_TEHNIKA_ARTIKLI);

        String CREATE_TEHNIKA_SN_ARTKLI = "CREATE TABLE IF NOT EXISTS sintal_teh_sn_artikli (" +
                " sn_artikel_id INTEGER NOT NULL, " +
                " sn_id int(11) NOT NULL, " +
                " DELOVNI_NALOG varchar(20) NOT NULL, " +
                " No_ varchar(20) NOT NULL," +
                " kolicina decimal(15,2) not null, " +
                " vrsta_id int(2) null, " +
                " upo_id int(10) DEFAULT NULL," +
                " tehnik_id int(10) DEFAULT NULL," +
                " REGAL varchar(50) DEFAULT '', " +
                " PRIMARY KEY('sn_artikel_id' AUTOINCREMENT)" +
                ")";
        mDatabase.execSQL(CREATE_TEHNIKA_SN_ARTKLI);

        String CREATE_TEHNIKA_SN_SKLADISCE = "CREATE TABLE IF NOT EXISTS sintal_teh_artikli_skladisce (" +
                " St_artikla INTEGER NOT NULL, " +
                " Skladisce varchar(50) NOT NULL, " +
                " datum date NOT NULL," +
                " kolicina int NULL " +
                ")";
        mDatabase.execSQL(CREATE_TEHNIKA_SN_SKLADISCE);

        String CREATE_TEHNIKA_SKLADISCE_VRSTE_ARTIKLOV = "CREATE TABLE IF NOT EXISTS sintal_teh_skladisce_vrste_artiklov (" +
                " oznaka varchar(3) noT NULL, " +
                " naziv_oznake text NOT NULL" +
                ")";
        mDatabase.execSQL(CREATE_TEHNIKA_SKLADISCE_VRSTE_ARTIKLOV);

        String CREATE_TEHNIKA_SIFRE_ARTIKLOV = "CREATE TABLE IF NOT EXISTS sintal_teh_sifre_artiklov (" +
                " sifra_artikla varchar(6) noT NULL, " +
                " oznaka varchar(3) NOT NULL" +
                ")";
        mDatabase.execSQL(CREATE_TEHNIKA_SIFRE_ARTIKLOV);

        String CREATE_TEHNIKA_ZA_PRENOS_VIEW = "CREATE VIEW IF NOT EXISTS view_tehnika_prenos_sn as select 0 as 'Order Type' \n" +
                "                ,sn.id as 'Order ID' \n" +
                "                ,sn_art.sn_artikel_id as 'Order Item Line' \n" +
                "                ,sn.DELOVNI_NALOG ,sn_art.No_ ,'TEH-01' as 'Location Code' \n" +
                "                ,sn_art.REGAL as 'Bin Code' \n" +
                "                ,sn_art.kolicina as 'Quantity' \n" +
                "                ,art.Merska_enota as 'Unit of Measure' \n" +
                "                ,'' as Description \n" +
                "                ,'' as Description2 \n" +
                "                ,0 as 'NAV Status' \n" +
                "                ,datetime('now','localtime') as 'Proccessed' \n" +
                "                ,sn_art.upo_id as 'User ID' \n" +
                "                ,'' as 'NAV Error' ,sn.STATUS_AKT \n" +
                "                from sintal_teh_sn sn \n" +
                "                inner join sintal_teh_sn_artikli sn_art on sn.id = sn_art.sn_id \n" +
                "                left join sintal_teh_artikli art on sn_art.No_ = art.No_\n" +
                "                UNION\n" +
                "                select 0 as 'Order Type' \n" +
                "                ,sn.id as 'Order ID' \n" +
                "                ,9998 as 'Order Item Line' \n" +
                "                ,sn.DELOVNI_NALOG\n" +
                "                ,'S0135' as No_ \n" +
                "                ,'TEH-01' as 'Location Code' \n" +
                "                ,'' as 'Bin Code'  --regal\n" +
                "                ,sn.URE_DELO as 'Quantity' \n" +
                "                ,'URA' as 'Unit of Measure' \n" +
                "                ,'' as Description \n" +
                "                ,'' as Description2 \n" +
                "                ,0 as 'NAV Status' \n" +
                "                ,datetime('now','localtime') as 'Proccessed' \n" +
                "                ,-1 as 'User ID' \n" +
                "                ,'' as 'NAV Error' ,sn.STATUS_AKT \n" +
                "                from sintal_teh_sn sn\n" +
                "                where sn.URE_DELO is not 'null' and sn.URE_DELO > 0\n" +
                "                UNION\n" +
                "                select 0 as 'Order Type' \n" +
                "                ,sn.id as 'Order ID' \n" +
                "                ,9999 as 'Order Item Line' \n" +
                "                ,sn.DELOVNI_NALOG\n" +
                "                ,'S0042' as No_ \n" +
                "                ,'TEH-01' as 'Location Code' \n" +
                "                ,'' as 'Bin Code'  --regal\n" +
                "                ,sn.STEVILO_KM as 'Quantity' \n" +
                "                ,'KM' as 'Unit of Measure' \n" +
                "                ,'' as Description \n" +
                "                ,'' as Description2 \n" +
                "                ,0 as 'NAV Status' \n" +
                "                ,datetime('now','localtime') as 'Proccessed' \n" +
                "                ,-1 as 'User ID' \n" +
                "                ,'' as 'NAV Error' ,sn.STATUS_AKT \n" +
                "                from sintal_teh_sn sn\n" +
                "                where sn.STEVILO_KM is not 'null' and sn.STEVILO_KM > 0\n" +
                "                UNION\n" +
                "                select 0 as 'Order Type' \n" +
                "                ,sn.id as 'Order ID' \n" +
                "                ,9999 as 'Order Item Line' \n" +
                "                ,sn.DELOVNI_NALOG\n" +
                "                ,'S0053' as No_ \n" +
                "                ,'TEH-01' as 'Location Code' \n" +
                "                ,'' as 'Bin Code'  --regal\n" +
                "                ,sn.URE_PREVOZ as 'Quantity' \n" +
                "                ,'URA' as 'Unit of Measure' \n" +
                "                ,'' as Description \n" +
                "                ,'' as Description2 \n" +
                "                ,0 as 'NAV Status' \n" +
                "                ,datetime('now','localtime') as 'Proccessed' \n" +
                "                ,-1 as 'User ID' \n" +
                "                ,'' as 'NAV Error' ,sn.STATUS_AKT \n" +
                "                from sintal_teh_sn sn\n" +
                "                where sn.URE_PREVOZ is not 'null' and sn.URE_PREVOZ > 0" +
                "";
        mDatabase.execSQL(CREATE_TEHNIKA_ZA_PRENOS_VIEW);

        mDatabase = this.getReadableDatabase();
            String CREATE_TEHNIKA_EMAIL_LOG = "CREATE TABLE IF NOT EXISTS sintal_teh_email_log (" +
                    "log_id INTEGER NOT NULL, " +
                    "user_id INTEGER NOT NULL, " +
                    "sn_id INTEGER NOT NULL, " +
                    "sn_dn TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "datum DATETIME NOT NULL, " +
                    "PRIMARY KEY (log_id AUTOINCREMENT) " +
                    ");";
        mDatabase.execSQL(CREATE_TEHNIKA_EMAIL_LOG);


        String CREATE_TEHNIKA_RC = "CREATE TABLE IF NOT EXISTS sintal_teh_registracija_casa (" +
                "  user_id int(11) NOT NULL," +
                "  datum_cas_prihod TEXT NULL," +
                "  datum_cas_odhod TEXT NULL," +
                "  sifra_intervala int(11) NOT NULL," +
                "  prenos int(11) NOT NULL " +
                ");";
        mDatabase.execSQL(CREATE_TEHNIKA_RC);

        String CREATE_TEHNIKA_SN_DODATNI_TEHNIK = "CREATE TABLE IF NOT EXISTS sintal_teh_sn_dodatni_tehnik (\n" +
                " sn_id INTEGER NOT NULL,\n" +
                " DELOVNI_NALOG varchar(20) NOT NULL,\n" +
                " tehnik_id intEGER NOT NULL\n" +
                ");";
        mDatabase.execSQL(CREATE_TEHNIKA_SN_DODATNI_TEHNIK);


        String CREATE_TEHNIKA_UPO_PODJETJE = "CREATE TABLE IF NOT EXISTS sintal_teh_upo_podjetje (" +
                " user_id int(11) NOT NULL, " +
                " podjetje varchar(2) NOT NULL " +
                ")";
        mDatabase.execSQL(CREATE_TEHNIKA_UPO_PODJETJE);

        String CREATE_TEHNIKA_VZ_DN = "CREATE TABLE IF NOT EXISTS sintal_teh_vz_dn ( " +
                " id int(11) NOT NULL, " +
                " sintal_vzd_dn_id int(11) NOT NULL,"+
                " st_del_naloga varchar(50) DEFAULT NULL,"+
                " naziv_servisa varchar(250) DEFAULT NULL,"+
                " r_objekt_id int(11) DEFAULT NULL,"+
                " ime varchar(50) DEFAULT NULL,"+
                " mc_objekt_fakt varchar(15) DEFAULT NULL,"+
                " mc_klient varchar(15) DEFAULT NULL,"+
                " mc_ime_objekta varchar(100) DEFAULT NULL,"+
                " pogodba varchar(50) DEFAULT NULL,"+
                " periodika_dni int(11) DEFAULT NULL,"+
                " kontaktna_oseba varchar(250) DEFAULT NULL,"+
                " opomba varchar(250) DEFAULT NULL,"+
                " telefon varchar(50) DEFAULT NULL,"+
                " datum_zadnjega datetime DEFAULT NULL,"+
                " datum_naslednjega datetime DEFAULT NULL,"+
                " sis_pozar int(11) DEFAULT NULL,"+
                " sis_vlom int(11) DEFAULT NULL,"+
                " sis_video int(11) DEFAULT NULL,"+
                " sis_co int(11) DEFAULT NULL,"+
                " sis_pristopna int(11) DEFAULT NULL,"+
                " sis_dimni_banokvci int(11) DEFAULT NULL,"+
                " sis_ostalo int(11) DEFAULT NULL,"+
                " oprema varchar(255) DEFAULT NULL,"+
                " prenos_alarma varchar(255) DEFAULT NULL,"+
                //" Dok_BD int(11) DEFAULT NULL,"+
                //" datum_veljavnosti_Dok_BD datetime DEFAULT NULL, "+
                " servis_izvajalec int(11) DEFAULT NULL, "+
                " koda_objekta varchar(10) DEFAULT NULL, "+
                " PODPIS_NAROCNIK blob DEFAULT NULL, "+
                " PODPIS_SERVISER blob DEFAULT NULL, "+
                " STATUS VARCHAR(1), "+
                " DATUM_DODELITVE date, "+
                " DATUM_IZVEDBE date, "+
                " PRENOS int(11), "+
                " DATUM_PRENOSA datetime, "+
                " OPIS_OKVARE text, "+
                " OPIS_POSTOPKA text, "+
                " URE_PREVOZ decimal(15,2), "+
                " URE_DELO decimal(15,2), "+
                " STEVILO_KM decimal(15,2), "+
                " DATUM_PODPISA date, "+
                " naslov_objekta varchar(200), " +
                " podjetje varchar(3), " +
                " projekt varchar(15), " +
                " IP_urejanja varchar(255), "+
                " xuser varchar(3), " +
                " xdatetime datetime, " +
                " st_del_naloga_dc_all varchar(50), " +
                " naziv_servisa_dc_all varchar(255), " +
                " koda_objekta_dc_all varchar(15) , " +
                " narocnik varchar(200) ," +
                " narocnik_naslov varchar(500), " +
                " objekt varchar(200), " +
                " leto_mes_zadnja_per varchar(6)" +
                ") " ;
        mDatabase.execSQL(CREATE_TEHNIKA_VZ_DN);



        String CREATE_TEHNIKA_VZ_PERIODIKA = "CREATE TABLE IF NOT EXISTS sintal_teh_vz_dn_periodika (" +
                " id INTEGER PRIMARY KEY   AUTOINCREMENT, " +
                " st_del_naloga varchar(50) DEFAULT NULL,"+
                " STATUS varchar(1), "+
                " DATUM_DODELITVE date, "+
                " DATUM_IZVEDBE date, "+
                " PRENOS int(11), "+
                " DATUM_PRENOSA datetime, "+
                " OPIS_POSTOPKA text, "+
                " URE_PREVOZ decimal(15,2), "+
                " URE_DELO decimal(15,2), "+
                " STEVILO_KM decimal(15,2), "+
                " DATUM_PODPISA date, "+
                " PODPIS_NAROCNIK blob DEFAULT NULL, "+
                " PODPIS_SERVISER blob DEFAULT NULL, "+
                " TIP_NAROCILA int(11) NULL, "+
                " sis_pozar int(11) DEFAULT NULL,"+
                " sis_vlom int(11) DEFAULT NULL,"+
                " sis_video int(11) DEFAULT NULL,"+
                " sis_co int(11) DEFAULT NULL,"+
                " sis_pristopna int(11) DEFAULT NULL,"+
                " sis_dimni_banokvci int(11) DEFAULT NULL,"+
                " sis_ostalo int(11) DEFAULT NULL,"+
                " periodika_dni int(11) DEFAULT NULL,"+
                " vzdrzevanje_redno int(11) DEFAULT NULL,"+
                " vzdrzevanje_izredno int(11) DEFAULT NULL,"+
                " tip_elementov nvarchar(500) null," +
                " datum_zadnjega datetime DEFAULT NULL,"+
                " datum_naslednjega datetime DEFAULT NULL,"+
                " kontrolor_linije nvarchar(500) null," +
                " aku_baterije nvarchar(500) null," +
                " nacin_prenosa nvarchar(500) null," +
                " institucija_prenosa nvarchar(500) null," +
                " preizkus1 int(11) null," +
                " preizkus2 int(11) null," +
                " preizkus3 int(11) null," +
                " preizkus4 int(11) null," +
                " preizkus5 int(11) null," +
                " preizkus6 int(11) null," +
                " preizkus7 int(11) null," +
                " preizkus8 int(11) null," +
                " preizkus9 int(11) null," +
                " preizkus10 int(11) null," +
                " preizkus11 int(11) null," +
                " preizkus12 int(11) null," +
                " preizkus13 int(11) null," +
                " preizkus14 int(11) null," +
                " preizkus15 int(11) null," +
                " preizkus16 int(11) null," +
                " preizkus17 int(11) null," +
                " preizkus18 int(11) null," +
                " podjetje varchar(2) null," +
                " projekt varchar(20) null," +
                " opomba varchar(1000) null," +
                " ip_urejanja varchar(255) null," +
                " xuser varchar(3) null," +
                " xdatetime datetime null" +
                ");";
        mDatabase.execSQL(CREATE_TEHNIKA_VZ_PERIODIKA);


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

    public void insertEmailLog(String user_id, int sn_id, String sn_dn, String email, String datum ) {


        mDatabase = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put("log_id",null);
            values.put("user_id",user_id);
            values.put("sn_id",sn_id);
            values.put("sn_dn",sn_dn);
            values.put("email",email);
            values.put("datum",datum);

            mDatabase.insert("sintal_teh_email_log",null, values);

        mDatabase.close();

    }


    public void insertRc(String user_id, String datum, int klicatelj, int sifra_zapisa  ) {

        //klicatelj 1 - prihod
        //klicatelj 2 - odhod
        String query = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        mDatabase = this.getWritableDatabase();
        //mDatabase = this.getWritableDatabase();
        query = "SELECT * FROM sintal_teh_registracija_casa where user_id ="+ user_id + " and substr(datum_cas_prihod,0,11) = substr(date('now'),0,11) and sifra_intervala='" + sifra_zapisa + "';" ;
        mCursor = mDatabase.rawQuery(query,null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {

            if (klicatelj == 2)
            {
                String UPDATE_RC ="update sintal_teh_registracija_casa set datum_cas_odhod = '" + strDate +"' where user_id = " + user_id+ " and substr(datum_cas_prihod,0,11) = substr(date('now'),0,11) and sifra_intervala='0'; ";
                mDatabase.execSQL(UPDATE_RC);
            }

        }
        else
        {
            if (klicatelj == 1) {
                ContentValues values = new ContentValues();
                //values.put("log_id",null);
                values.put("user_id", user_id);
                values.put("datum_cas_prihod", strDate);
                values.put("datum_cas_odhod", "");
                values.put("sifra_intervala", sifra_zapisa);
                values.put("prenos", 0);
                mDatabase.insert("sintal_teh_registracija_casa", null, values);
            }
        }
        mCursor.close();

        mDatabase.close();

    }


    public void insertUpdateEmailLog(String user_id, int sn_id, String sn_dn, String email, String datum ) {

        String query = "";
        mDatabase = this.getWritableDatabase();
        query = "SELECT * FROM sintal_teh_email_log where sn_id ="+ sn_id + " and user_id = '"+   user_id + "' and email = '" + email + "'";
        mCursor = mDatabase.rawQuery(query,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            //ContentValues values = new ContentValues();
            //values.put("pripadnost",pripadnost);
            //values.put("pripadnost_vnc",pripadnost_vnc);
            //mDatabase.update("sintal_teh_pripadnost", values, "pripadnost=?", new String[]{pripadnost});

        }
        else
        {
            ContentValues values = new ContentValues();
            //values.put("log_id",null);
            values.put("user_id",user_id);
            values.put("sn_id",sn_id);
            values.put("sn_dn",sn_dn);
            values.put("email",email);
            values.put("datum",datum);

            mDatabase.insert("sintal_teh_email_log",null, values);
        }
        mCursor.close();


        mDatabase.close();

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

    public void insertUpdateArtikel(String No_, String Naziv, String Naziv_iskanje, String Merska_enota, String Kratka_oznaka ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_artikli where No_ = '"+ No_+"'";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            /*
            ContentValues values = new ContentValues();
            values.put("pripadnost",pripadnost);
            values.put("pripadnost_vnc",pripadnost_vnc);
            mDatabase.update("sintal_teh_pripadnost", values, "pripadnost=?", new String[]{pripadnost});
             */

        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("No_",No_);
            values.put("Naziv",Naziv);
            values.put("Naziv_iskanje",Naziv_iskanje);
            values.put("Merska_enota",Merska_enota);
            values.put("Kratka_oznaka",Kratka_oznaka);
            mDatabase.insert("sintal_teh_artikli",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }

    public List<String> getVrsteArtikov(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM sintal_teh_skladisce_vrste_artiklov";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    public void insertUpdateSkladisceVrsteArtiklov(String oznaka, String naziv_oznake) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_skladisce_vrste_artiklov where oznaka='"+oznaka+"'";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            /*
            ContentValues values = new ContentValues();
            values.put("pripadnost",pripadnost);
            values.put("pripadnost_vnc",pripadnost_vnc);
            mDatabase.update("sintal_teh_pripadnost", values, "pripadnost=?", new String[]{pripadnost});
             */

        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("oznaka",oznaka);
            values.put("naziv_oznake",naziv_oznake);
            //values.put("Datum",Datum);
            //values.put("Kolicina",Kolicina);
            //values.put("Kratka_oznaka",Kratka_oznaka);
            mDatabase.insert("sintal_teh_skladisce_vrste_artiklov",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }

    public void insertUpdateSifreArtiklov(String sifra_artikla, String oznaka) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_sifre_artiklov where sifra_artikla='"+sifra_artikla+"'";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            /*
            ContentValues values = new ContentValues();
            values.put("pripadnost",pripadnost);
            values.put("pripadnost_vnc",pripadnost_vnc);
            mDatabase.update("sintal_teh_pripadnost", values, "pripadnost=?", new String[]{pripadnost});
             */

        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("sifra_artikla",sifra_artikla);
            values.put("oznaka",oznaka);
            //values.put("Datum",Datum);
            //values.put("Kolicina",Kolicina);
            //values.put("Kratka_oznaka",Kratka_oznaka);
            mDatabase.insert("sintal_teh_sifre_artiklov",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }

    public void insertUpdateSkladisce(int St_artikla, String Skladisce, String Datum, int Kolicina ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_artikli_skladisce where St_artikla = " + String.valueOf(St_artikla) + " and Skladisce = '" + Skladisce + "' and kolicina = '" + String.valueOf(Kolicina)+"'";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            /*
            ContentValues values = new ContentValues();
            values.put("pripadnost",pripadnost);
            values.put("pripadnost_vnc",pripadnost_vnc);
            mDatabase.update("sintal_teh_pripadnost", values, "pripadnost=?", new String[]{pripadnost});
             */

        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("St_artikla",St_artikla);
            values.put("Skladisce",Skladisce);
            values.put("Datum",Datum);
            values.put("Kolicina",Kolicina);
            //values.put("Kratka_oznaka",Kratka_oznaka);
            mDatabase.insert("sintal_teh_artikli_skladisce",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }

    public void insertUpdateTehnik(String tehnikID, String naziv, String servis, String montaza, String vzdrzevanje, String ime_regala, String userID ,String ime_vira) {

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
            values.put("ime_regala",ime_regala);
            values.put("ime_vira",ime_vira);
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
            values.put("ime_regala",ime_regala);
            values.put("user_id",userID);
            values.put("ime_vira",ime_vira);

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

    public void deleteUpdateUpoPodjetje() {
        mDatabase = this.getWritableDatabase();
        mDatabase.execSQL("delete from sintal_teh_upo_podjetje");
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

    public void insertUpdateupoPodjejte(String userID, String podjetje ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_upo_podjetje";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();

        }

        if (userID.equals("") && podjetje.equals(""))
        {}
        else {
            ContentValues values = new ContentValues();
            values.put("user_ID", userID);
            values.put("podjetje", podjetje);

            mDatabase.insert("sintal_teh_upo_podjetje", null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }


    public void brisiSNServiserDodatni(String id) {

        mDatabase = this.getWritableDatabase();
        String delete_tehnik = "delete FROM sintal_teh_sn_dodatni_tehnik where sn_id ="+id+";";
        mDatabase.execSQL(delete_tehnik);
        mDatabase.close();

    }



    public void dodajSNServiserDodatni(String id, String DN, String tehnikID) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String delete_tehnik = "delete FROM sintal_teh_sn_dodatni_tehnik where sn_id ="+id+";";
        //mCursor = mDatabase.rawQuery(delete_tehnik,null);
        mDatabase.execSQL(delete_tehnik);
        ContentValues values = new ContentValues();
        values.put("sn_id", id);
        values.put("DELOVNI_NALOG", DN);
        values.put("tehnik_id", tehnikID);

        mDatabase.insert("sintal_teh_sn_dodatni_tehnik", null, values);
        mDatabase.close();

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
                               String SEKTOR_POSTNA_ST,
                               String DATUM_PODPISA,
                               String DATUM_DODELITVE,
                               String DATUM_IZVEDBE,
                               String PRENOSA,
                               String DATUM_PRENOSA,
                               String OPIS_OKVARE,
                               String OPIS_POSTOPKA,
                               String URE_PREVOZ,
                               String URE_DELO,
                               String STEVILO_KM



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
            values.put("DATUM_PODPISA", DATUM_PODPISA);
            values.put("DATUM_DODELITVE",DATUM_DODELITVE);
            values.put("DATUM_IZVEDBE", DATUM_IZVEDBE);
            values.put("PRENOS", PRENOSA);
            values.put("DATUM_PRENOSA", DATUM_PRENOSA);
            values.put("OPIS_OKVARE", OPIS_OKVARE);
            values.put("OPIS_POSTOPKA", OPIS_POSTOPKA);
            values.put("URE_PREVOZ", URE_PREVOZ);
            values.put("URE_DELO", URE_DELO);
            values.put("STEVILO_KM", STEVILO_KM);
            mDatabase.update("sintal_teh_sn", values, "id=?", new String[]{id});

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
            values.put("DATUM_PODPISA", DATUM_PODPISA);
            values.put("DATUM_DODELITVE",DATUM_DODELITVE);
            values.put("DATUM_IZVEDBE", DATUM_IZVEDBE);
            values.put("PRENOS", PRENOSA);
            values.put("DATUM_PRENOSA", DATUM_PRENOSA);
            values.put("OPIS_OKVARE", OPIS_OKVARE);
            values.put("OPIS_POSTOPKA", OPIS_POSTOPKA);
            values.put("URE_PREVOZ", URE_PREVOZ);
            values.put("URE_DELO", URE_DELO);
            values.put("STEVILO_KM", STEVILO_KM);
            //values.put("OZNACI",OZNACI);
            mDatabase.insert("sintal_teh_sn",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }



    public void insertUpdateVZDN(
            String id,
            String vz_id ,
            String st_del_naloga ,
            String naziv_servisa ,
            String r_objekt_id,
            String ime ,
            String mc_objekt_fakt ,
            String mc_klient ,
            String mc_ime_objekta ,
            String pogodba ,
            String periodika_dni ,

            String kontaktna_oseba ,
            String opomba ,
            String telefon ,
            String datum_zadnjega ,
            String datum_naslednjega ,
            String sis_pozar ,
            String sis_vlom ,
            String sis_video,
            String sis_co,
            String sis_pristopna ,

            String sis_dimni_banokvci ,
            String sis_ostalo ,
            String oprema ,
            String prenos_alarma ,
            //String Dok_BD ,
            //String datum_veljavnosti_Dok_BD ,
            String servis_izvajalec ,
            String koda_objekta ,
            String STATUS ,
            String DATUM_DODELITVE,

            String DATUM_IZVEDBE ,
            String PRENOS ,
            String DATUM_PRENOSA ,
            String OPIS_OKVARE ,
            String OPIS_POSTOPKA  ,
            String URE_PREVOZ ,
            String URE_DELO ,
            String STEVILO_KM ,
            String DATUM_PODPISA,
            String NASLOV_OBJEKTA,
            String PODJETJE,
            String NAROCNIK,
            String NAROCNIK_NASLOV,
            String OBJEKT

    ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_vz_dn where id ="+ id;
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            ContentValues values = new ContentValues();

            values.put("sintal_vzd_dn_id", vz_id);
            values.put("st_del_naloga", st_del_naloga);
            values.put("naziv_servisa", naziv_servisa);
            values.put("r_objekt_id", r_objekt_id);
            values.put("ime", ime);
            values.put("mc_objekt_fakt", mc_objekt_fakt);
            values.put("mc_klient", mc_klient);
            values.put("mc_ime_objekta", mc_ime_objekta);
            values.put("pogodba", pogodba);
            values.put("periodika_dni", periodika_dni);

            values.put("kontaktna_oseba", kontaktna_oseba);
            values.put("opomba", opomba);
            values.put("telefon", telefon);
            values.put("datum_zadnjega", datum_zadnjega);
            values.put("datum_naslednjega", datum_naslednjega);
            values.put("sis_pozar", sis_pozar);
            values.put("sis_vlom", sis_vlom);
            values.put("sis_video", sis_video);
            values.put("sis_co", sis_co);

            values.put("sis_pristopna", sis_pristopna);
            values.put("sis_dimni_banokvci", sis_dimni_banokvci);
            values.put("sis_ostalo", sis_ostalo);
            values.put("oprema", oprema);
            values.put("prenos_alarma", prenos_alarma);
            //values.put("Dok_BD", Dok_BD);
            //values.put("datum_veljavnosti_Dok_BD", datum_veljavnosti_Dok_BD);
            values.put("servis_izvajalec", servis_izvajalec);
            values.put("koda_objekta", koda_objekta);
            values.put("STATUS", STATUS);

            values.put("DATUM_DODELITVE",DATUM_DODELITVE);
            values.put("DATUM_IZVEDBE", DATUM_IZVEDBE);
            values.put("PRENOS", PRENOS);
            values.put("DATUM_PRENOSA", DATUM_PRENOSA);
            values.put("OPIS_OKVARE", OPIS_OKVARE);
            values.put("OPIS_POSTOPKA", OPIS_POSTOPKA);
            values.put("URE_PREVOZ", URE_PREVOZ);
            values.put("URE_DELO", URE_DELO);
            values.put("STEVILO_KM", STEVILO_KM);

            values.put("DATUM_PODPISA", DATUM_PODPISA);
            values.put("naslov_objekta", NASLOV_OBJEKTA);

            values.put("podjetje", PODJETJE);
            values.put("narocnik", NAROCNIK);
            values.put("narocnik_naslov", NAROCNIK_NASLOV);
            values.put("objekt", OBJEKT);


            mDatabase.update("sintal_teh_vz_dn", values, "id=?", new String[]{id});

        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("sintal_vzd_dn_id", vz_id);
            values.put("st_del_naloga", st_del_naloga);
            values.put("naziv_servisa", naziv_servisa);
            values.put("r_objekt_id", r_objekt_id);
            values.put("ime", ime);
            values.put("mc_objekt_fakt", mc_objekt_fakt);
            values.put("mc_klient", mc_klient);
            values.put("mc_ime_objekta", mc_ime_objekta);
            values.put("pogodba", pogodba);
            values.put("periodika_dni", periodika_dni);

            values.put("kontaktna_oseba", kontaktna_oseba);
            values.put("opomba", opomba);
            values.put("telefon", telefon);
            values.put("datum_zadnjega", datum_zadnjega);
            values.put("datum_naslednjega", datum_naslednjega);
            values.put("sis_pozar", sis_pozar);
            values.put("sis_vlom", sis_vlom);
            values.put("sis_video", sis_video);
            values.put("sis_co", sis_co);

            values.put("sis_pristopna", sis_pristopna);
            values.put("sis_dimni_banokvci", sis_dimni_banokvci);
            values.put("sis_ostalo", sis_ostalo);
            values.put("oprema", oprema);
            values.put("prenos_alarma", prenos_alarma);
            //values.put("Dok_BD", Dok_BD);
            //values.put("datum_veljavnosti_Dok_BD", datum_veljavnosti_Dok_BD);
            values.put("servis_izvajalec", servis_izvajalec);
            values.put("koda_objekta", koda_objekta);
            values.put("STATUS", STATUS);

            values.put("DATUM_DODELITVE",DATUM_DODELITVE);
            values.put("DATUM_IZVEDBE", DATUM_IZVEDBE);
            values.put("PRENOS", PRENOS);
            values.put("DATUM_PRENOSA", DATUM_PRENOSA);
            values.put("OPIS_OKVARE", OPIS_OKVARE);
            values.put("OPIS_POSTOPKA", OPIS_POSTOPKA);
            values.put("URE_PREVOZ", URE_PREVOZ);
            values.put("URE_DELO", URE_DELO);
            values.put("STEVILO_KM", STEVILO_KM);

            values.put("DATUM_PODPISA", DATUM_PODPISA);
            values.put("naslov_objekta", NASLOV_OBJEKTA);
            values.put("podjetje", PODJETJE);
            values.put("narocnik", NAROCNIK);
            values.put("narocnik_naslov", NAROCNIK_NASLOV);
            values.put("objekt", OBJEKT);
            mDatabase.insert("sintal_teh_vz_dn",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }


    public void insertUpdateVZDNPeriodika(
            String id,
            //String vz_id ,
            String st_del_naloga ,
            String status ,
            String DATUM_DODELITVE,
            String DATUM_IZVEDBE ,
            String PRENOS ,
            String DATUM_PRENOSA ,
            String OPIS_POSTOPKA ,
            String URE_PREVOZ ,

            String URE_DELO ,
            String STEVILO_KM ,
            String DATUM_PODPISA ,
            String PODPIS_NAROCNIK ,
            String PODPIS_SERVISER ,
            String TIP_NAROCILA ,
            String sis_pozar ,
            String sis_vlom ,
            String sis_video,
            String sis_co,
            String sis_pristopna ,

            String sis_dimni_banokvci ,
            String sis_ostalo ,
            String periodika_dni ,
            String vzdrzevanje_redno ,
            String vzdrzevanje_izredno ,
            String tip_elementov ,
            String datum_zadnjega ,
            String datum_naslednjega ,
            String kontrolor_linije ,
            String aku_baterije,

            String nacin_prenosa ,
            String institucija_prenosa ,
            String preizkus1 ,
            String preizkus2 ,
            String preizkus3 ,
            String preizkus4 ,
            String preizkus5 ,
            String preizkus6 ,
            String preizkus7 ,
            String preizkus8 ,
            String preizkus9 ,
            String preizkus10 ,
            String preizkus11 ,
            String preizkus12 ,
            String preizkus13 ,
            String preizkus14 ,
            String preizkus15 ,
            String preizkus16 ,
            String preizkus17 ,
            String preizkus18 ,
            String podjetje ,
            String projekt ,
            String opomba ,
            String ip_urejanja,
            String xuser,
            String xdatetime


    ) {

        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_vz_dn_periodika where st_del_naloga ='"+ st_del_naloga +"' and DATUM_IZVEDBE = '" +DATUM_IZVEDBE +"';";
        mCursor = mDatabase.rawQuery(GET_USER,null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            ContentValues values = new ContentValues();

            values.put("st_del_naloga", st_del_naloga);
            values.put("STATUS", status);
            values.put("DATUM_DODELITVE", DATUM_DODELITVE);
            values.put("DATUM_IZVEDBE", DATUM_IZVEDBE);
            values.put("PRENOS", PRENOS);
            values.put("DATUM_PRENOSA", DATUM_PRENOSA);
            values.put("OPIS_POSTOPKA", OPIS_POSTOPKA);
            values.put("URE_PREVOZ", URE_PREVOZ);
            values.put("URE_DELO", URE_DELO);
            values.put("STEVILO_KM", STEVILO_KM);
            values.put("DATUM_PODPISA", DATUM_PODPISA);
            values.put("PODPIS_NAROCNIK", PODPIS_NAROCNIK);
            values.put("PODPIS_SERVISER", PODPIS_SERVISER);
            values.put("TIP_NAROCILA", TIP_NAROCILA);
            values.put("sis_pozar", sis_pozar);
            values.put("sis_vlom", sis_vlom);
            values.put("sis_video", sis_video);
            values.put("sis_co", sis_co);
            values.put("sis_pristopna", sis_pristopna);
            values.put("sis_dimni_banokvci", sis_dimni_banokvci);
            values.put("sis_ostalo", sis_ostalo);
            values.put("periodika_dni", periodika_dni);
            values.put("vzdrzevanje_redno", vzdrzevanje_redno);
            values.put("vzdrzevanje_izredno", vzdrzevanje_izredno);
            values.put("tip_elementov", tip_elementov);
            values.put("datum_zadnjega", datum_zadnjega);
            values.put("datum_naslednjega", datum_naslednjega);
            values.put("kontrolor_linije", kontrolor_linije);
            values.put("aku_baterije",aku_baterije);
            values.put("nacin_prenosa", nacin_prenosa);
            values.put("institucija_prenosa", institucija_prenosa);
            values.put("preizkus1", preizkus1);
            values.put("preizkus2", preizkus2);
            values.put("preizkus3", preizkus3);
            values.put("preizkus4", preizkus4);
            values.put("preizkus5", preizkus5);
            values.put("preizkus6", preizkus6);
            values.put("preizkus7", preizkus7);
            values.put("preizkus8", preizkus8);
            values.put("preizkus9", preizkus9);
            values.put("preizkus10", preizkus10);
            values.put("preizkus11", preizkus11);
            values.put("preizkus12", preizkus12);
            values.put("preizkus13", preizkus13);
            values.put("preizkus14", preizkus14);
            values.put("preizkus15", preizkus15);
            values.put("preizkus16", preizkus16);
            values.put("preizkus17", preizkus17);
            values.put("preizkus18", preizkus18);
            values.put("podjetje" ,podjetje);
            values.put("projekt" ,projekt);
            values.put("opomba" ,opomba);
            values.put("ip_urejanja" ,ip_urejanja);
            values.put("xuser" ,xuser);
            values.put("xdatetime" ,xdatetime);
            values.put("opomba",opomba);
            String[] selectionArgs = { "st_del_naloga" };
            mDatabase.update("sintal_teh_vz_dn_periodika", values, " st_del_naloga = '"+st_del_naloga +"' and DATUM_IZVEDBE = '"+DATUM_IZVEDBE+"'", null);

        }
        else
        {
            ContentValues values = new ContentValues();
            //values.put("id", id);
            //values.put("sintal_vzd_dn_id", vz_id);
            values.put("st_del_naloga", st_del_naloga);
            values.put("STATUS", status);
            values.put("DATUM_DODELITVE", DATUM_DODELITVE);
            values.put("DATUM_IZVEDBE", DATUM_IZVEDBE);
            values.put("PRENOS", PRENOS);
            values.put("DATUM_PRENOSA", DATUM_PRENOSA);
            values.put("OPIS_POSTOPKA", OPIS_POSTOPKA);
            values.put("URE_PREVOZ", URE_PREVOZ);
            values.put("URE_DELO", URE_DELO);

            values.put("STEVILO_KM", STEVILO_KM);
            values.put("DATUM_PODPISA", DATUM_PODPISA);
            values.put("PODPIS_NAROCNIK", PODPIS_NAROCNIK);
            values.put("PODPIS_SERVISER", PODPIS_SERVISER);
            values.put("TIP_NAROCILA", TIP_NAROCILA);
            values.put("sis_pozar", sis_pozar);
            values.put("sis_vlom", sis_vlom);
            values.put("sis_video", sis_video);
            values.put("sis_co", sis_co);

            values.put("sis_pristopna", sis_pristopna);
            values.put("sis_dimni_banokvci", sis_dimni_banokvci);
            values.put("sis_ostalo", sis_ostalo);
            values.put("periodika_dni", periodika_dni);
            values.put("vzdrzevanje_redno", vzdrzevanje_redno);
            values.put("vzdrzevanje_izredno", vzdrzevanje_izredno);
            values.put("tip_elementov", tip_elementov);
            values.put("datum_zadnjega", datum_zadnjega);
            values.put("datum_naslednjega", datum_naslednjega);
            values.put("kontrolor_linije", kontrolor_linije);

            values.put("aku_baterije",aku_baterije);
            values.put("nacin_prenosa", nacin_prenosa);
            values.put("institucija_prenosa", institucija_prenosa);

            values.put("preizkus1", preizkus1);
            values.put("preizkus2", preizkus2);
            values.put("preizkus3", preizkus3);
            values.put("preizkus4", preizkus4);
            values.put("preizkus5", preizkus5);
            values.put("preizkus6", preizkus6);
            values.put("preizkus7", preizkus7);
            values.put("preizkus8", preizkus8);
            values.put("preizkus9", preizkus9);
            values.put("preizkus10", preizkus10);
            values.put("preizkus11", preizkus11);
            values.put("preizkus12", preizkus12);
            values.put("preizkus13", preizkus13);
            values.put("preizkus14", preizkus14);
            values.put("preizkus15", preizkus15);
            values.put("preizkus16", preizkus16);
            values.put("preizkus17", preizkus17);
            values.put("preizkus18", preizkus18);
            values.put("podjetje" ,podjetje);
            values.put("projekt" ,projekt);
            values.put("ip_urejanja" ,ip_urejanja);
            values.put("xuser" ,xuser);
            values.put("xdatetime" ,xdatetime);
            values.put("opomba",opomba);

            //values.put("redno", redno);
            //values.put("izredno", izredno);
            //values.put("OZNACI",OZNACI);
            mDatabase.insert("sintal_teh_vz_dn_periodika",null, values);
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

    public List<String> getTehnikiFromUserInString(int userID, int svm) {
        List<String> userList = new ArrayList<String>();
        // Select All Query
        //seznam serviserjev
        String selectQuery = "";
        if (svm == 1) {
            selectQuery = "SELECT  sintal_teh_delavci.* FROM sintal_teh_upo " +
                    "left join sintal_teh_upo_delavci on sintal_teh_upo.user_id = sintal_teh_upo_delavci.user_id " +
                    "left join sintal_teh_delavci on  sintal_teh_delavci.tehnik_id = sintal_teh_upo_delavci.tehnik_id " +
                    "where sintal_teh_upo.user_id = " + String.valueOf(userID) +  " and seznam_servis = 1 order by naziv ";

        }
        else if (svm == 2)
        {
            selectQuery = "SELECT  sintal_teh_delavci.* FROM sintal_teh_upo " +
                    "left join sintal_teh_upo_delavci on sintal_teh_upo.user_id = sintal_teh_upo_delavci.user_id " +
                    "left join sintal_teh_delavci on  sintal_teh_delavci.tehnik_id = sintal_teh_upo_delavci.tehnik_id " +
                    "where sintal_teh_upo.user_id = " + String.valueOf(userID) + " and seznam_vzdrzevanje = 1 order by naziv ";


        }
        else if (svm == 3 )
        {
            selectQuery = "SELECT  sintal_teh_delavci.* FROM sintal_teh_upo " +
                    "left join sintal_teh_upo_delavci on sintal_teh_upo.user_id = sintal_teh_upo_delavci.user_id " +
                    "left join sintal_teh_delavci on  sintal_teh_delavci.tehnik_id = sintal_teh_upo_delavci.tehnik_id " +
                    "where sintal_teh_upo.user_id = " + String.valueOf(userID) + " and seznam_montaza = 1 order by naziv ";
        }

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

    public ArrayList<Serviser> GetServiserjeUporabnik(String userID){
        ArrayList<Serviser> serviserList = new ArrayList<Serviser>();
        String selectQuery = "";
               selectQuery = "select td.tehnik_id, td.naziv from sintal_teh_upo_delavci ud " +
                             "left join sintal_teh_delavci td on ud.tehnik_id = td.tehnik_id " +
                              "where ud.user_id = " + String.valueOf(userID) +  " and td.user_id <> " + String.valueOf(userID) + " order by td.naziv;";
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            do {
                Serviser s = new Serviser();
                s.setStServiser(mCursor.getString(0));
                s.setNazivServiser(mCursor.getString(1));
                serviserList.add(s);// +", " + mCursor.getString(0));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        mDatabase.close();

        return serviserList;
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

    public String GetImeVira(int TehnikID) {
        String ime_vira;
        // Select All Query
        String selectQuery = "SELECT  * FROM sintal_teh_delavci where tehnik_id = "+ String.valueOf(TehnikID)+ ";";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            do {
                ime_vira = mCursor.getString(mCursor.getColumnIndex("ime_vira"));
            } while (mCursor.moveToNext());
        }
        else{
            ime_vira = "";
        }
        mCursor.close();
        mDatabase.close();
        // return contact list
        return ime_vira;
    }

    public List<String> getTehnikiUporabnikInString(String user_id) {
        List<String> userList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "select * from sintal_teh_delavci std left join sintal_teh_upo_delavci stud on std.tehnik_id = stud.tehnik_id  where stud.user_id = "+ user_id;

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(selectQuery, null);
        String saso = "";
        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            do {
                if (mCursor.getInt(0) == 2)
                {
                    saso = mCursor.getString(1);
                }
                else {
                    userList.add(mCursor.getString(1));// +", " + mCursor.getString(0));
                }
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        mDatabase.close();
        if (userList.isEmpty()){
            ServisniNalog sn = new ServisniNalog();
            userList.add("");
        }
        if (saso.length() > 0)
        {
            userList.add(0,saso);

        }
        // return contact list
        return userList;
    }

    public ArrayList<HashMap<String, String>> GetSeznamSN(String datum, @NotNull String vodja_sn){

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

    public void updateSNServiser(String id, String serviser, String datumDodelitve, String status_akt) {
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
            values.put("STATUS_AKT", status_akt);
            values.put("VODJA_NALOGA", serviser);
            values.put("DATUM_DODELITVE", datumDodelitve);
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

    public void updateVZDNPerMes(String id, String mes_obr) {
        String GET_USER = "SELECT * FROM sintal_teh_vz_dn where st_del_naloga ='" + id+"'";
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

            values.put("leto_mes_zadnja_per", mes_obr);
            mDatabase.update("sintal_teh_vz_dn", values, "st_del_naloga=?", new String[]{id});

        }
        mCursor.close();
        mDatabase.close();
    }

    public void updateSNOznaciId(String id, String oznaci) {
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
            //values.put("id", id);

            values.put("OZNACI", oznaci);
            mDatabase.update("sintal_teh_sn", values, "id=?", new String[]{id});

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

    public void updateSNStatusZakljuci(String id, String statusAkt) {
        String GET_USER = "SELECT * FROM sintal_teh_sn where id ='" + id+"'";
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(GET_USER, null);
        //NadzorXML n;
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            String datum;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            datum = dtf.format(now);
            //test.setText(datum);
            ContentValues values = new ContentValues();
            values.put("STATUS_AKT", statusAkt);
            values.put("DATUM_KONEC",datum);
            //values.put("OZNACI", 0);
            mDatabase.update("sintal_teh_sn", values, "id=?", new String[]{id});

        }
        mCursor.close();
        mDatabase.close();
    }

    public void updateVZDNPerStatus(String id, String di, String statusAkt) {
        String GET_USER = "SELECT * FROM sintal_teh_vz_dn_periodika where  st_del_naloga = '"+id+"' and DATUM_IZVEDBE = '"+di+"'";
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(GET_USER, null);
        //NadzorXML n;
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            String datum;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            datum = dtf.format(now);
            //test.setText(datum);
            ContentValues values = new ContentValues();
            values.put("STATUS", statusAkt);
            //values.put("DATUM_KONEC",datum);
            //values.put("OZNACI", 0);
            mDatabase.update("sintal_teh_vz_dn_periodika", values, "st_del_naloga = "+id + " and DATUM_IZVEDBE =" + di,null);

        }
        mCursor.close();
        mDatabase.close();
    }

    public ArrayList<RegCasa> GetSeznamRCUporabnik(String user_id) {

        ArrayList<RegCasa> list = new ArrayList<RegCasa>();
        String query = "SELECT * FROM sintal_teh_registracija_casa where user_id = " +  user_id + "  order by datum_cas_prihod desc limit 10";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()) {
            RegCasa rc = new RegCasa();
            String datumDb = mCursor.getString(mCursor.getColumnIndex("datum_cas_prihod"));
            String datumDb2 = mCursor.getString(mCursor.getColumnIndex("datum_cas_odhod"));
            try {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                //Using parse method to convert the string to LocalDate object
                LocalDate date = LocalDate.parse(datumDb, format);
                DateTimeFormatter dateformatJava = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String date_to_string = date.format(dateformatJava);
                rc.setDatum(date_to_string);
                rc.setCasOd(datumDb.substring(11, 16));
                if (datumDb2.length() > 0) {
                    rc.setCasDo(datumDb2.substring(11, 16));
                } else
                {
                    rc.setCasDo("");
                }
                list.add(rc);
            }
            catch (IllegalArgumentException e) {
                System.out.println("Exception: " + e);
            } // If the String was unable to be parsed.
            catch (DateTimeParseException e) {
                System.out.println("Exception: " + e);
            }



                    //setid(mCursor.getInt(mCursor.getColumnIndex("id")));
        }

        return list;
    }


    public List<String> getDodaniServiser(String id) {
        List<String> list = new ArrayList<String>(2);
        String query = "select td.tehnik_id, td.naziv \n" +
                " from sintal_teh_sn_dodatni_tehnik dt left join sintal_teh_delavci td on dt.tehnik_id = td.tehnik_id \n" +
                " where sn_id = " + id +";";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()) {
            list.add(mCursor.getString(mCursor.getColumnIndex("tehnik_id")));
            list.add(mCursor.getString(mCursor.getColumnIndex("naziv")));
        }

        return list;


    }


    public ArrayList<ServisniNalog> GetSeznamSNDodelitev(String vodja_sn, String status_akt, String userid, String datum){

        ArrayList<ServisniNalog> list = new ArrayList<ServisniNalog>();
        String query = "SELECT  * FROM sintal_teh_sn where datum_dodelitve <> null and status_akt = '"+ status_akt + "' and substr(DELOVNI_NALOG,3,2) in (select podjetje from sintal_teh_upo_podjetje where user_id = "+userid+") and DATUM_ZACETEK = '"+datum+" 00:00:00' order by DATUM_ZACETEK desc";
        if (vodja_sn.equals("-1"))
        {
            query = "SELECT  * FROM sintal_teh_sn where  VODJA_NALOGA = '' and STATUS_AKT = '"+status_akt+"' and substr(DELOVNI_NALOG,3,2) in (select podjetje from sintal_teh_upo_podjetje where user_id = "+userid+") and DATUM_ZACETEK = '"+datum+" 00:00:00' order by DATUM_ZACETEK desc";
        }
        else if (vodja_sn.equals("0")) {
            query = "SELECT  * FROM sintal_teh_sn where  VODJA_NALOGA <> '' and STATUS_AKT = '"+status_akt+"'  and substr(DELOVNI_NALOG,3,2) in (select podjetje from sintal_teh_upo_podjetje where user_id = "+userid+") and DATUM_ZACETEK = '"+datum+" 00:00:00' order by DATUM_ZACETEK desc";
        }
        else if (vodja_sn.equals("1"))
        {
            query = "SELECT  * FROM sintal_teh_sn where STATUS_AKT = '"+status_akt+"'  and substr(DELOVNI_NALOG,3,2) in (select podjetje from sintal_teh_upo_podjetje where user_id = "+userid+") and DATUM_ZACETEK = '"+datum+" 00:00:00' order by DATUM_ZACETEK desc ;";
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
            sn.setDatumDodelitve(mCursor.getString(mCursor.getColumnIndex("DATUM_DODELITVE")));
            //sn.setOznacen(0);
            list.add(sn);
        }

        mCursor.close();
        mDatabase.close();
        return  list;
    }

    public ArrayList<ServisniNalog> GetSeznamSNUporabnik(String vodja_sn, String status){

        ArrayList<ServisniNalog> list = new ArrayList<ServisniNalog>();
        String query ="";// "SELECT  * FROM sintal_teh_sn where datum_zacetek = '" + datum + "' and vodja_sn = 'Null'";
        //query = "SELECT  * FROM sintal_teh_sn where strftime('%d.%m.%Y', DATUM_ZACETEK) = '" + datum + "' and VODJA_NALOGA = '"+vodja_sn+"'";
        if (status.equals("D") == true)
        {
            query = "SELECT  * FROM sintal_teh_sn where VODJA_NALOGA = '"+vodja_sn+"' and STATUS_AKT = '"+status+"' and DATUM_DODELITVE is not null ";
        }
        else if (status.equals("P") == true)
        {
            query = "SELECT  * FROM sintal_teh_sn where VODJA_NALOGA = '"+vodja_sn+"' and STATUS_AKT = '"+status+"' and DATUM_PODPISA is not null ";
        }
        else if (status.equals("Z") == true)
        {
            query = "SELECT  * FROM sintal_teh_sn where VODJA_NALOGA = '"+vodja_sn+"' and STATUS_AKT = '"+status+"'  ";
        }
        else if (status.equals("X") == true)
        {
            query = "SELECT  * FROM sintal_teh_sn where VODJA_NALOGA = '"+vodja_sn+"' and STATUS_AKT = '"+status+"'  ";
        }
        else if (status.equals("Y") == true)
        {
            query = "SELECT  * FROM sintal_teh_sn where VODJA_NALOGA = '"+vodja_sn+"' and STATUS_AKT = '"+status+"'  ";
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
            sn.setStKm(mCursor.getDouble(mCursor.getColumnIndex("STEVILO_KM")));
            sn.setUreDelo(mCursor.getDouble(mCursor.getColumnIndex("URE_DELO")));
            sn.setUrePrevoz(mCursor.getDouble(mCursor.getColumnIndex("URE_PREVOZ")));

            try{
                String datumDod = mCursor.getString(mCursor.getColumnIndex("DATUM_DODELITVE"));
                Date date_format=new SimpleDateFormat("dd/MM/yyyy").parse(datumDod);
            }
            catch (Exception e)
            {

            }
            sn.setDatumDodelitve(mCursor.getString(mCursor.getColumnIndex("DATUM_DODELITVE"))); //popravi query da bo dajal samo z datumom dodelitve
            sn.setDatumIzvedbe(mCursor.getString(mCursor.getColumnIndex("DATUM_IZVEDBE")));
            sn.setDatumPodpisa(mCursor.getString(mCursor.getColumnIndex("DATUM_PODPISA")));
            sn.setDatumKonec(mCursor.getString(mCursor.getColumnIndex("DATUM_KONEC")));
            sn.setStatus(mCursor.getString(mCursor.getColumnIndex("STATUS_AKT")));
            sn.setOpisPostopka(mCursor.getString(mCursor.getColumnIndex("OPIS_POSTOPKA")));
            sn.setOpisOkvare(mCursor.getString(mCursor.getColumnIndex("OPIS_OKVARE")));
            //sn.setOznacen(0);
            list.add(sn);
        }

        mCursor.close();
        mDatabase.close();
        return  list;
    }

    public ArrayList<DelovniNalogVZ> GetSeznamVZDNUporabnik(String vodja_sn, String status, String LetoMesec) {

        ArrayList<DelovniNalogVZ> list = new ArrayList<DelovniNalogVZ>();
        String query ="";// "SELECT  * FROM sintal_teh_sn where datum_zacetek = '" + datum + "' and vodja_sn = 'Null'";


        //query = "SELECT  * FROM sintal_teh_sn where strftime('%d.%m.%Y', DATUM_ZACETEK) = '" + datum + "' and VODJA_NALOGA = '"+vodja_sn+"'";
        /*
        if (status.equals("D") == true)
        {
            query = "select  strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as leto, strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as mesec, vz.*, case when ifnull(per.id,0) = 0 then 0 else 1 end as per_id, case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per from sintal_teh_vz_dn vz left join sintal_teh_vz_dn_periodika per on vz.id = per.id\n" +
                    "where strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+leto+"' and strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+mesec+"'";
        }
        else if (status.equals("P") == true)
        {
            query = "select  strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as leto, strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as mesec, vz.*, case when ifnull(per.id,0) = 0 then 0 else 1 end as per_id, case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per from sintal_teh_vz_dn vz left join sintal_teh_vz_dn_periodika per on vz.id = per.id\n" +
                    "where strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+leto+"' and strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+mesec+"'";
        }
        else if (status.equals("Z") == true)
        {
            query = "select  strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as leto, strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as mesec, vz.*, case when ifnull(per.id,0) = 0 then 0 else 1 end as per_id, case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per from sintal_teh_vz_dn vz left join sintal_teh_vz_dn_periodika per on vz.id = per.id\n" +
                    "where strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+leto+"' and strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+mesec+"'";
        }
        else if (status.equals("X") == true)
        {
            query = "select  strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as leto, strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as mesec, vz.*, case when ifnull(per.id,0) = 0 then 0 else 1 end as per_id, case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per from sintal_teh_vz_dn vz left join sintal_teh_vz_dn_periodika per on vz.id = per.id\n" +
                    "where strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+leto+"' and strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+mesec+"'";
        }
        else if (status.equals("Y") == true)
        {
            query = "select  strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as leto, strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) as mesec, vz.*, case when ifnull(per.id,0) = 0 then 0 else 1 end as per_id, case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per from sintal_teh_vz_dn vz left join sintal_teh_vz_dn_periodika per on vz.id = per.id\n" +
                    "where strftime('%Y',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+leto+"' and strftime('%m',(DATE(vz.datum_zadnjega,'+'||vz.periodika_dni||' days'))) = '"+mesec+"'";
        }

         */
        String leto ="";
        String mesec = "";
        if ( status.equals("A") || status.equals("D") )
        {
             leto = LetoMesec.substring(0, 4);
             mesec = LetoMesec.substring(4, 6);
        }
        query = " select  \n" +
                " strftime('%Y',(DATE(vz.datum_naslednjega))) as leto,\n" +
                " strftime('%m',(DATE(vz.datum_naslednjega))) as mesec,\n" +
                " vz.*, vz.datum_naslednjega as DATUM_IZVEDBE, '' as opomba,\n" +
                " 0  as per_id,\n" +
                " 0 as prenos_per \n" +
                " from sintal_teh_vz_dn vz \n" +
                " where  strftime('%Y',(DATE(vz.datum_naslednjega))) = '"+leto+"'  \n" +
                " and  strftime('%m',(DATE(vz.datum_naslednjega))) = '"+mesec+"'  \n" +
                " and vz.st_del_naloga not in (select per.st_del_naloga from sintal_teh_vz_dn_periodika per where strftime('%Y',(DATE(per.DATUM_IZVEDBE))) = '"+leto+"' and strftime('%m',(DATE(per.DATUM_IZVEDBE))) = '"+mesec+"' )\n" +
                " UNION\n" +
                " select  \n" +
                " strftime('%Y',(DATE(per.DATUM_IZVEDBE))) as leto,\n" +
                " strftime('%m',(DATE(per.DATUM_IZVEDBE))) as mesec,\n" +
                " vz.*, per.DATUM_IZVEDBE as DATUM_IZVEDBE, per.opomba,\n" +
                " case when ifnull(per.id,0) = 0 then 0 else per.id end as per_id,\n" +
                " case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per \n" +
                " from sintal_teh_vz_dn vz \n" +
                " left join sintal_teh_vz_dn_periodika per on vz.st_del_naloga = per.st_del_naloga \n" +
                " where strftime('%Y',(DATE(per.DATUM_IZVEDBE))) = '"+leto+"' \n" +
                " and strftime('%m',(DATE(per.DATUM_IZVEDBE))) = '"+mesec+"'" ;

        if (status.equals('Z')){
            query = /*" select  \n" +
                    " strftime('%Y',(DATE(vz.datum_naslednjega))) as leto,\n" +
                    " strftime('%m',(DATE(vz.datum_naslednjega))) as mesec,\n" +
                    " vz.*, vz.datum_naslednjega as DATUM_IZVEDBE, '' as opomba,\n" +
                    " 0  as per_id,\n" +
                    " 0 as prenos_per \n" +
                    " from sintal_teh_vz_dn vz \n" +
                    //" where  strftime('%Y',(DATE(vz.datum_naslednjega))) = '"+leto+"'  \n" +
                    //" and  strftime('%m',(DATE(vz.datum_naslednjega))) = '"+mesec+"'  \n" +
                    //" and vz.st_del_naloga not in (select per.st_del_naloga from sintal_teh_vz_dn_periodika per where strftime('%Y',(DATE(per.DATUM_IZVEDBE))) = '"+leto+"' and strftime('%m',(DATE(per.DATUM_IZVEDBE))) = '"+mesec+"' )\n" +
                    " UNION\n" +*/
                    " select  \n" +
                    " strftime('%Y',(DATE(per.DATUM_IZVEDBE))) as leto,\n" +
                    " strftime('%m',(DATE(per.DATUM_IZVEDBE))) as mesec,\n" +
                    " vz.*, per.DATUM_IZVEDBE as DATUM_IZVEDBE, per.opomba,\n" +
                    " case when ifnull(per.id,0) = 0 then 0 else per.id end as per_id,\n" +
                    " case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per \n" +
                    " from sintal_teh_vz_dn vz \n" +
                    " left join sintal_teh_vz_dn_periodika per on vz.st_del_naloga = per.st_del_naloga \n" +
                    //" where strftime('%Y',(DATE(per.DATUM_IZVEDBE))) = '"+leto+"' \n" +
                    //" and strftime('%m',(DATE(per.DATUM_IZVEDBE))) = '"+mesec+"'" ;
                    " where per.status = 'Z'";
        }
        if (status.equals('X')){
            query = /*" select  \n" +
                    " strftime('%Y',(DATE(vz.datum_naslednjega))) as leto,\n" +
                    " strftime('%m',(DATE(vz.datum_naslednjega))) as mesec,\n" +
                    " vz.*, vz.datum_naslednjega as DATUM_IZVEDBE, '' as opomba,\n" +
                    " 0  as per_id,\n" +
                    " 0 as prenos_per \n" +
                    " from sintal_teh_vz_dn vz \n" +
                    //" where  strftime('%Y',(DATE(vz.datum_naslednjega))) = '"+leto+"'  \n" +
                    //" and  strftime('%m',(DATE(vz.datum_naslednjega))) = '"+mesec+"'  \n" +
                    //" and vz.st_del_naloga not in (select per.st_del_naloga from sintal_teh_vz_dn_periodika per where strftime('%Y',(DATE(per.DATUM_IZVEDBE))) = '"+leto+"' and strftime('%m',(DATE(per.DATUM_IZVEDBE))) = '"+mesec+"' )\n" +
                    " UNION\n" +*/
                    " select  \n" +
                            " strftime('%Y',(DATE(per.DATUM_IZVEDBE))) as leto,\n" +
                            " strftime('%m',(DATE(per.DATUM_IZVEDBE))) as mesec,\n" +
                            " vz.*, per.DATUM_IZVEDBE as DATUM_IZVEDBE, per.opomba,\n" +
                            " case when ifnull(per.id,0) = 0 then 0 else per.id end as per_id,\n" +
                            " case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per \n" +
                            " from sintal_teh_vz_dn vz \n" +
                            " left join sintal_teh_vz_dn_periodika per on vz.st_del_naloga = per.st_del_naloga \n" +
                            //" where strftime('%Y',(DATE(per.DATUM_IZVEDBE))) = '"+leto+"' \n" +
                            //" and strftime('%m',(DATE(per.DATUM_IZVEDBE))) = '"+mesec+"'" ;
                            " where per.status = 'X'";
        }

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            DelovniNalogVZ sn = new DelovniNalogVZ();
            sn.setid(mCursor.getInt(mCursor.getColumnIndex("id")));
            sn.setDelovniNalog(mCursor.getString(mCursor.getColumnIndex("st_del_naloga")));
            sn.setNaziv_servisa(mCursor.getString(mCursor.getColumnIndex("naziv_servisa")));
            sn.setOpomba(mCursor.getString(mCursor.getColumnIndex("opomba")));
            sn.setKontaktna_oseba(mCursor.getString(mCursor.getColumnIndex("kontaktna_oseba")));
            sn.setDatum_zadnjega(mCursor.getString(mCursor.getColumnIndex("datum_zadnjega")));
            sn.setDatum_naslednjega(mCursor.getString(mCursor.getColumnIndex("datum_naslednjega")));
            sn.setPeridika_kreirana(mCursor.getInt(mCursor.getColumnIndex("per_id")));
            sn.setPrenos_per(mCursor.getInt(mCursor.getColumnIndex("prenos_per")));
            sn.setObjekt(mCursor.getString(mCursor.getColumnIndex("objekt")));
            sn.setObjektNaslov(mCursor.getString(mCursor.getColumnIndex("naslov_objekta")));
            sn.setOpomba(mCursor.getString(mCursor.getColumnIndex("opomba")));
            sn.setDATUM_IZVEDBE(mCursor.getString(mCursor.getColumnIndex("DATUM_IZVEDBE")));
            sn.setLetoMesObr(mCursor.getString(mCursor.getColumnIndex("leto_mes_zadnja_per")));
            sn.setperid(mCursor.getInt(mCursor.getColumnIndex("per_id")));
            //sn.setOznacen(0);
            list.add(sn);
        }

        mCursor.close();
        mDatabase.close();
        return  list;
    }

    public ArrayList<SNArtikel> GetArtikle(String vrsta, String ime_regala){

        ArrayList<SNArtikel> list = new ArrayList<SNArtikel>();
        String query = "";
        //query = "SELECT  * FROM sintal_teh_artikli";
        if (vrsta.equals("regal") == true)
        {
            query = "SELECT " +
                    "art.No_ " +
                    ",art.Naziv " +
                    ",art.Naziv_iskanje " +
                    ",sum(Kolicina) as Kolicina, art.Merska_enota " +
                    ",stas.Skladisce as Skladisce " +
                    "FROM " +
                    "sintal_teh_artikli art " +
                    "left join sintal_teh_artikli_skladisce stas " +
                    "on art.No_ = stas.St_artikla " +
                    "WHERE stas.Skladisce = '"+ime_regala+"' " +
                    "group by art.No_ " +
                    "order by 1 ";
        }
        else {
            query = "select art.No_, art.Naziv, art.Naziv_iskanje, 1 as Kolicina, art.Merska_enota, '' as Skladisce \n" +
                    "from sintal_teh_artikli art \n" +
                    "left join sintal_teh_sifre_artiklov sart on art.No_ = sart.sifra_artikla \n" +
                    "left join sintal_teh_skladisce_vrste_artiklov svart on svart.oznaka = sart.oznaka\n" +
                    "where svart.naziv_oznake = '"+vrsta+"'";
        }

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            SNArtikel sn = new SNArtikel();
            sn.setid(mCursor.getString(mCursor.getColumnIndex("No_")));
            sn.setnaziv(mCursor.getString(mCursor.getColumnIndex("Naziv")));
            sn.setnazivIskanje(mCursor.getString(mCursor.getColumnIndex("Naziv_iskanje")));
            sn.setKratkaOznaka("");
            //sn.setKratkaOznaka(mCursor.getString(mCursor.getColumnIndex("Kratka_oznaka")));
            sn.setmerskaEnota(mCursor.getString(mCursor.getColumnIndex("Merska_enota")));
            //sn.setmerskaEnota("");
            sn.setKolicina(mCursor.getFloat(mCursor.getColumnIndex("Kolicina")));
            sn.setRegal(mCursor.getString(mCursor.getColumnIndex("Skladisce")));
            //sn.setOznacen(0);
            list.add(sn);
        }

        mCursor.close();
        mDatabase.close();
        return  list;
    }

    public DelovniNalogVZ vrniVZDN(String stDelNal){

        DelovniNalogVZ vzDelN = new DelovniNalogVZ();
        String query = "SELECT * FROM sintal_teh_vz_dn where st_del_naloga ='"+stDelNal+"'";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            //ServisniNalog sn = new ServisniNalog();
            vzDelN.setid(mCursor.getInt(mCursor.getColumnIndex("id")));
            vzDelN.setDelovniNalog(mCursor.getString(mCursor.getColumnIndex("st_del_naloga")));
            vzDelN.setNaziv_servisa(mCursor.getString(mCursor.getColumnIndex("naziv_servisa")));
            vzDelN.setR_objekt_id(mCursor.getString(mCursor.getColumnIndex("r_objekt_id")));
            vzDelN.setIme(mCursor.getString(mCursor.getColumnIndex("ime")));
            vzDelN.setMc_ime_objekta(mCursor.getString(mCursor.getColumnIndex("mc_ime_objekta")));
            vzDelN.setPeriodika_dni(mCursor.getInt(mCursor.getColumnIndex("periodika_dni")));
            vzDelN.setKontaktna_oseba(mCursor.getString(mCursor.getColumnIndex("kontaktna_oseba")));
            vzDelN.setOpomba(mCursor.getString(mCursor.getColumnIndex("opomba")));

            vzDelN.setTelefon(mCursor.getString(mCursor.getColumnIndex("telefon")));
            vzDelN.setNaslov(mCursor.getString(mCursor.getColumnIndex("naslov_objekta")));
            vzDelN.setStatus(mCursor.getInt(mCursor.getColumnIndex("STATUS")));
            vzDelN.setSis_pozar(mCursor.getInt(mCursor.getColumnIndex("sis_pozar")));
            vzDelN.setSis_co(mCursor.getInt(mCursor.getColumnIndex("sis_co")));
            vzDelN.setSis_vlom(mCursor.getInt(mCursor.getColumnIndex("sis_vlom")));
            vzDelN.setSis_video(mCursor.getInt(mCursor.getColumnIndex("sis_video")));
            vzDelN.setSis_pristopna(mCursor.getInt(mCursor.getColumnIndex("sis_pristopna")));
            vzDelN.setSis_dimni_bankovci(mCursor.getInt(mCursor.getColumnIndex("sis_dimni_banokvci")));
            vzDelN.setDATUM_IZVEDBE(mCursor.getString(mCursor.getColumnIndex("DATUM_IZVEDBE")));

            vzDelN.setUrePrevoz(mCursor.getDouble(mCursor.getColumnIndex("URE_PREVOZ")));
            vzDelN.setUreDelo(mCursor.getDouble(mCursor.getColumnIndex("URE_DELO")));
            vzDelN.setStKm(mCursor.getDouble(mCursor.getColumnIndex("STEVILO_KM")));
            vzDelN.setDatumPodpisa(mCursor.getString(mCursor.getColumnIndex("DATUM_PODPISA")));
            vzDelN.setNarocnik(mCursor.getString(mCursor.getColumnIndex("narocnik")));
            vzDelN.setNarocnikNaslov(mCursor.getString(mCursor.getColumnIndex("narocnik_naslov")));
            vzDelN.setObjekt(mCursor.getString(mCursor.getColumnIndex("objekt")));
            vzDelN.setObjektNaslov(mCursor.getString(mCursor.getColumnIndex("naslov_objekta")));

            //vzDelN.setPodpis(mCursor.getBlob(mCursor.getColumnIndex("PODPIS_NAROCNIK")));
            //vzDelN.setDatumDodelitve(mCursor.getString(mCursor.getColumnIndex("DATUM_DODELITVE")));
            //vzDelN.setDatumKonec(mCursor.getString(mCursor.getColumnIndex("DATUM_KONEC")));

            //sn.setOznacen(0);

        }

        mCursor.close();
        mDatabase.close();
        return  vzDelN;
    }

    public DelovniNalogVZPeriodika vrniVZDNPre(String st_del_naloga, int periodika_prenos, String mes_obr){

        DelovniNalogVZPeriodika vzDelN = new DelovniNalogVZPeriodika();
        String query = "";
        if ( periodika_prenos == 0)
        {
             query = " SELECT *, '' as TIP_NAROCILA, 0 AS vzdrzevanje_redno, 0 AS vzdrzevanje_izredno, " +
                     " 0 AS tip_elementov, 'A' as status_per, '0' as up_per, '0' as ud_per, '0' as st_km_per,   " +
                     " '' as kontrolor_linije, '' as aku_baterije, '' as nacin_prenosa, '' as institucija_prenosa, " +
                     " 0 as preizkus1,  0 as preizkus2,  0 as preizkus3,  0 as preizkus4,  0 as preizkus5,   " +
                     " 0 as preizkus6,  0 as preizkus7,  0 as preizkus8,  0 as preizkus9,  0 as preizkus10,   " +
                     " 0 as preizkus11,  0 as preizkus12,  0 as preizkus13,  0 as preizkus14,  0 as preizkus15, 0 as preizkus16, " +
                     " 0 as preizkus17, 0 as preizkus18 " +
                     "FROM sintal_teh_vz_dn where st_del_naloga ='"+st_del_naloga+"'";
        }
        else {
             query = "select  \n" +
                     "strftime('%Y',(DATE(per.DATUM_IZVEDBE))) as leto,\n" +
                     "strftime('%m',(DATE(per.DATUM_IZVEDBE))) as mesec,\n" +
                     "vz.*, per.DATUM_IZVEDBE as DATUM_IZVEDBE, per.opomba,\n" +
                     "case when ifnull(per.id,0) = 0 then 0 else per.id end as per_id,\n" +
                     "case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per \n" +
                     " ,per.TIP_NAROCILA \n" +
                     " , per.preizkus1 \n" +
                     " , per.preizkus2 \n" +
                     " , per.preizkus3 \n" +
                     " , per.preizkus4 \n" +
                     " , per.preizkus5 \n" +
                     " , per.preizkus6 \n" +
                     " , per.preizkus7 \n" +
                     " , per.preizkus8 \n" +
                     " , per.preizkus9 \n" +
                     " , per.preizkus10 \n" +
                     " , per.preizkus11 \n" +
                     " , per.preizkus12 \n" +
                     " , per.preizkus13 \n" +
                     " , per.preizkus14 \n" +
                     " , per.preizkus15 \n" +
                     " , per.preizkus16 \n" +
                     " , per.preizkus17 \n" +
                     " , per.preizkus18 \n" +
                     " , per.vzdrzevanje_redno \n" +
                     " , per.vzdrzevanje_izredno \n" +
                     " , per.tip_elementov \n" +
                     " , per.kontrolor_linije \n" +
                     " , per.aku_baterije \n" +
                     " , per.nacin_prenosa \n" +
                     " , per.institucija_prenosa \n" +
                     " , per.URE_DELO as ud_per \n" +
                     " , per.URE_PREVOZ as up_per \n" +
                     " , per.STEVILO_KM as st_km_per \n" +
                     " , per.STATUS as status_per \n" +
                     " from sintal_teh_vz_dn vz \n" +
                     " left join sintal_teh_vz_dn_periodika per on vz.st_del_naloga = per.st_del_naloga \n" +
                     " where strftime('%Y',(DATE(per.DATUM_IZVEDBE))) = '"+mes_obr.substring(0,4)+"' \n" +
                     " and strftime('%m',(DATE(per.DATUM_IZVEDBE))) = '"+mes_obr.substring(4,6)+"'\n" +
                     " and vz.st_del_naloga = '"+st_del_naloga+"';";
        }



        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            //ServisniNalog sn = new ServisniNalog();
            vzDelN.setid(mCursor.getInt(mCursor.getColumnIndex("id")));
            vzDelN.setDelovniNalog(mCursor.getString(mCursor.getColumnIndex("st_del_naloga")));
            vzDelN.setPeriodika_dni(mCursor.getInt(mCursor.getColumnIndex("periodika_dni")));
            vzDelN.setOpomba(mCursor.getString(mCursor.getColumnIndex("OPIS_POSTOPKA")));
            vzDelN.setTipNarocila(mCursor.getInt(mCursor.getColumnIndex("TIP_NAROCILA")));

            vzDelN.setStatus(mCursor.getString(mCursor.getColumnIndex("status_per")));
            vzDelN.setSis_pozar(mCursor.getInt(mCursor.getColumnIndex("sis_pozar")));
            vzDelN.setSis_co(mCursor.getInt(mCursor.getColumnIndex("sis_co")));
            vzDelN.setSis_vlom(mCursor.getInt(mCursor.getColumnIndex("sis_vlom")));
            vzDelN.setSis_video(mCursor.getInt(mCursor.getColumnIndex("sis_video")));
            vzDelN.setSis_pristopna(mCursor.getInt(mCursor.getColumnIndex("sis_pristopna")));
            vzDelN.setSis_dimni_bankovci(mCursor.getInt(mCursor.getColumnIndex("sis_dimni_banokvci")));

            vzDelN.setRedno(mCursor.getInt(mCursor.getColumnIndex("vzdrzevanje_redno")));
            vzDelN.setIzredno(mCursor.getInt(mCursor.getColumnIndex("vzdrzevanje_izredno")));
            vzDelN.setTip_elementov(mCursor.getString(mCursor.getColumnIndex("tip_elementov")));
            vzDelN.setDatum_prejsenjega(mCursor.getString(mCursor.getColumnIndex("datum_zadnjega")));
            vzDelN.setDatum_naslednjega(mCursor.getString(mCursor.getColumnIndex("datum_naslednjega")));
            vzDelN.setkontrolor(mCursor.getString(mCursor.getColumnIndex("kontrolor_linije")));
            vzDelN.setaku_bat(mCursor.getString(mCursor.getColumnIndex("aku_baterije")));

            vzDelN.setnacin_prenosa(mCursor.getString(mCursor.getColumnIndex("nacin_prenosa")));
            vzDelN.setprenos_inst(mCursor.getString(mCursor.getColumnIndex("institucija_prenosa")));

            vzDelN.setPr1(mCursor.getInt(mCursor.getColumnIndex("preizkus1")));
            vzDelN.setPr2(mCursor.getInt(mCursor.getColumnIndex("preizkus2")));
            vzDelN.setPr3(mCursor.getInt(mCursor.getColumnIndex("preizkus3")));
            vzDelN.setPr4(mCursor.getInt(mCursor.getColumnIndex("preizkus4")));
            vzDelN.setPr5(mCursor.getInt(mCursor.getColumnIndex("preizkus5")));

            vzDelN.setPr6(mCursor.getInt(mCursor.getColumnIndex("preizkus6")));
            vzDelN.setPr7(mCursor.getInt(mCursor.getColumnIndex("preizkus7")));
            vzDelN.setPr8(mCursor.getInt(mCursor.getColumnIndex("preizkus8")));
            vzDelN.setPr9(mCursor.getInt(mCursor.getColumnIndex("preizkus9")));
            vzDelN.setPr10(mCursor.getInt(mCursor.getColumnIndex("preizkus10")));

            vzDelN.setPr11(mCursor.getInt(mCursor.getColumnIndex("preizkus11")));
            vzDelN.setPr12(mCursor.getInt(mCursor.getColumnIndex("preizkus12")));
            vzDelN.setPr13(mCursor.getInt(mCursor.getColumnIndex("preizkus13")));
            vzDelN.setPr14(mCursor.getInt(mCursor.getColumnIndex("preizkus14")));
            vzDelN.setPr15(mCursor.getInt(mCursor.getColumnIndex("preizkus15")));

            vzDelN.setPr16(mCursor.getInt(mCursor.getColumnIndex("preizkus16")));
            vzDelN.setPr17(mCursor.getInt(mCursor.getColumnIndex("preizkus17")));
            vzDelN.setPr18(mCursor.getInt(mCursor.getColumnIndex("preizkus18")));

            vzDelN.setUrePrevoz(mCursor.getDouble(mCursor.getColumnIndex("up_per")));
            vzDelN.setUreDelo(mCursor.getDouble(mCursor.getColumnIndex("ud_per")));
            vzDelN.setStKm(mCursor.getDouble(mCursor.getColumnIndex("st_km_per")));
            vzDelN.setDatumPodpisa(mCursor.getString(mCursor.getColumnIndex("DATUM_PODPISA")));


            vzDelN.setDATUM_IZVEDBE(mCursor.getString(mCursor.getColumnIndex("DATUM_IZVEDBE")));
            vzDelN.setOpomba(mCursor.getString(mCursor.getColumnIndex("opomba")));


        }

        mCursor.close();
        mDatabase.close();
        return  vzDelN;
    }

    public DelovniNalogVZPeriodika vrniVZDNPreDatumIzvedbe(String stDN, String datumIzvedbe){

        DelovniNalogVZPeriodika vzDelN = new DelovniNalogVZPeriodika();
        String query = "";
            query = "select  \n" +
                    "strftime('%Y',(DATE(per.DATUM_IZVEDBE))) as leto,\n" +
                    "strftime('%m',(DATE(per.DATUM_IZVEDBE))) as mesec,\n" +
                    "vz.*, per.DATUM_IZVEDBE as DATUM_IZVEDBE, per.opomba,\n" +
                    "case when ifnull(per.id,0) = 0 then 0 else 1 end as per_id,\n" +
                    "case when ifnull(per.prenos,0) = 0 then 0 else 1 end as prenos_per \n" +
                    " ,per.TIP_NAROCILA \n" +
                    " , per.preizkus1 \n" +
                    " , per.preizkus2 \n" +
                    " , per.preizkus3 \n" +
                    " , per.preizkus4 \n" +
                    " , per.preizkus5 \n" +
                    " , per.preizkus6 \n" +
                    " , per.preizkus7 \n" +
                    " , per.preizkus8 \n" +
                    " , per.preizkus9 \n" +
                    " , per.preizkus10 \n" +
                    " , per.preizkus11 \n" +
                    " , per.preizkus12 \n" +
                    " , per.preizkus13 \n" +
                    " , per.preizkus14 \n" +
                    " , per.preizkus15 \n" +
                    " , per.preizkus16 \n" +
                    " , per.preizkus17 \n" +
                    " , per.preizkus18 \n" +
                    " , per.vzdrzevanje_redno \n" +
                    " , per.vzdrzevanje_izredno \n" +
                    " , per.tip_elementov \n" +
                    " , per.kontrolor_linije \n" +
                    " , per.aku_baterije \n" +
                    " , per.nacin_prenosa \n" +
                    " , per.institucija_prenosa \n" +
                    " from sintal_teh_vz_dn vz \n" +
                    " left join sintal_teh_vz_dn_periodika per on vz.st_del_naloga = per.st_del_naloga \n" +
                    " where DATUM_IZVEDBE = '"+datumIzvedbe+"' \n" +
                    " and per.st_del_naloga = "+stDN+";";


        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            //ServisniNalog sn = new ServisniNalog();
            vzDelN.setid(mCursor.getInt(mCursor.getColumnIndex("id")));
            vzDelN.setDelovniNalog(mCursor.getString(mCursor.getColumnIndex("st_del_naloga")));
            vzDelN.setPeriodika_dni(mCursor.getInt(mCursor.getColumnIndex("periodika_dni")));
            vzDelN.setOpomba(mCursor.getString(mCursor.getColumnIndex("OPIS_POSTOPKA")));
            vzDelN.setTipNarocila(mCursor.getInt(mCursor.getColumnIndex("TIP_NAROCILA")));

            vzDelN.setStatus(mCursor.getString(mCursor.getColumnIndex("STATUS")));
            vzDelN.setSis_pozar(mCursor.getInt(mCursor.getColumnIndex("sis_pozar")));
            vzDelN.setSis_co(mCursor.getInt(mCursor.getColumnIndex("sis_co")));
            vzDelN.setSis_vlom(mCursor.getInt(mCursor.getColumnIndex("sis_vlom")));
            vzDelN.setSis_video(mCursor.getInt(mCursor.getColumnIndex("sis_video")));
            vzDelN.setSis_pristopna(mCursor.getInt(mCursor.getColumnIndex("sis_pristopna")));
            vzDelN.setSis_dimni_bankovci(mCursor.getInt(mCursor.getColumnIndex("sis_dimni_banokvci")));

            vzDelN.setRedno(mCursor.getInt(mCursor.getColumnIndex("vzdrzevanje_redno")));
            vzDelN.setIzredno(mCursor.getInt(mCursor.getColumnIndex("vzdrzevanje_izredno")));
            vzDelN.setTip_elementov(mCursor.getString(mCursor.getColumnIndex("tip_elementov")));
            vzDelN.setDatum_prejsenjega(mCursor.getString(mCursor.getColumnIndex("datum_zadnjega")));
            vzDelN.setDatum_naslednjega(mCursor.getString(mCursor.getColumnIndex("datum_naslednjega")));
            vzDelN.setkontrolor(mCursor.getString(mCursor.getColumnIndex("kontrolor_linije")));
            vzDelN.setaku_bat(mCursor.getString(mCursor.getColumnIndex("aku_baterije")));

            vzDelN.setnacin_prenosa(mCursor.getString(mCursor.getColumnIndex("nacin_prenosa")));
            vzDelN.setprenos_inst(mCursor.getString(mCursor.getColumnIndex("institucija_prenosa")));

            vzDelN.setPr1(mCursor.getInt(mCursor.getColumnIndex("preizkus1")));
            vzDelN.setPr2(mCursor.getInt(mCursor.getColumnIndex("preizkus2")));
            vzDelN.setPr3(mCursor.getInt(mCursor.getColumnIndex("preizkus3")));
            vzDelN.setPr4(mCursor.getInt(mCursor.getColumnIndex("preizkus4")));
            vzDelN.setPr5(mCursor.getInt(mCursor.getColumnIndex("preizkus5")));

            vzDelN.setPr6(mCursor.getInt(mCursor.getColumnIndex("preizkus6")));
            vzDelN.setPr7(mCursor.getInt(mCursor.getColumnIndex("preizkus7")));
            vzDelN.setPr8(mCursor.getInt(mCursor.getColumnIndex("preizkus8")));
            vzDelN.setPr9(mCursor.getInt(mCursor.getColumnIndex("preizkus9")));
            vzDelN.setPr10(mCursor.getInt(mCursor.getColumnIndex("preizkus10")));

            vzDelN.setPr11(mCursor.getInt(mCursor.getColumnIndex("preizkus11")));
            vzDelN.setPr12(mCursor.getInt(mCursor.getColumnIndex("preizkus12")));
            vzDelN.setPr13(mCursor.getInt(mCursor.getColumnIndex("preizkus13")));
            vzDelN.setPr14(mCursor.getInt(mCursor.getColumnIndex("preizkus14")));
            vzDelN.setPr15(mCursor.getInt(mCursor.getColumnIndex("preizkus15")));

            vzDelN.setPr16(mCursor.getInt(mCursor.getColumnIndex("preizkus16")));
            vzDelN.setPr17(mCursor.getInt(mCursor.getColumnIndex("preizkus17")));
            vzDelN.setPr18(mCursor.getInt(mCursor.getColumnIndex("preizkus18")));

            vzDelN.setUrePrevoz(mCursor.getDouble(mCursor.getColumnIndex("URE_PREVOZ")));
            vzDelN.setUreDelo(mCursor.getDouble(mCursor.getColumnIndex("URE_DELO")));
            vzDelN.setStKm(mCursor.getDouble(mCursor.getColumnIndex("STEVILO_KM")));
            vzDelN.setDatumPodpisa(mCursor.getString(mCursor.getColumnIndex("DATUM_PODPISA")));


            vzDelN.setDATUM_IZVEDBE(mCursor.getString(mCursor.getColumnIndex("DATUM_IZVEDBE")));
            vzDelN.setOpomba(mCursor.getString(mCursor.getColumnIndex("opomba")));


        }

        mCursor.close();
        mDatabase.close();
        return  vzDelN;
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
            sn.setGarancija(mCursor.getInt(mCursor.getColumnIndex("GARANCIJA")));
            sn.setDatumIzvedbe(mCursor.getString(mCursor.getColumnIndex("DATUM_IZVEDBE")));
            sn.setTipNarocila(mCursor.getInt(mCursor.getColumnIndex("TIP_NAROCILA")));
            sn.setTipVzdrzevanja(mCursor.getInt(mCursor.getColumnIndex("TIP_VZDRZEVANJA")));
            sn.setOpisOkvare(mCursor.getString(mCursor.getColumnIndex("OPIS_OKVARE")));
            sn.setOpisPostopka(mCursor.getString(mCursor.getColumnIndex("OPIS_POSTOPKA")));
            sn.setUrePrevoz(mCursor.getDouble(mCursor.getColumnIndex("URE_PREVOZ")));
            sn.setUreDelo(mCursor.getDouble(mCursor.getColumnIndex("URE_DELO")));
            sn.setStKm(mCursor.getDouble(mCursor.getColumnIndex("STEVILO_KM")));
            sn.setDatumPodpisa(mCursor.getString(mCursor.getColumnIndex("DATUM_PODPISA")));
            sn.setPodpis(mCursor.getBlob(mCursor.getColumnIndex("PODPIS_NAROCNIK")));
            sn.setDatumDodelitve(mCursor.getString(mCursor.getColumnIndex("DATUM_DODELITVE")));
            sn.setDatumKonec(mCursor.getString(mCursor.getColumnIndex("DATUM_KONEC")));
            sn.setDatumIzvedbe(mCursor.getString(mCursor.getColumnIndex("DATUM_IZVEDBE")));
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

    public void updateSNDN(String snID, int tipNarocila, int tipVzdrzevanja,String tvSNDatumMontaze,String tvSNGarancija,String tvSNNapaka,String etSNUrePrevoz,String etSNUreDelo,String etSNStKm,String tvSNNapaka2 ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        String GET_USER = "SELECT * FROM sintal_teh_sn where id ="+ snID;
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            //String UPDATE_USER ="";
            ContentValues values = new ContentValues();
            values.put("tip_narocila",tipNarocila);
            if (tipVzdrzevanja == 0){
                tipVzdrzevanja = 1;
            }
            else if (tipVzdrzevanja == 1){
                tipVzdrzevanja = 2;
            }
            else if (tipVzdrzevanja == 2){
                tipVzdrzevanja = 3;
            }
            else {
                tipVzdrzevanja = 9;
            }
            values.put("tip_vzdrzevanja",tipVzdrzevanja);
            values.put("datum_izvedbe",tvSNDatumMontaze);
            values.put("opis_okvare",tvSNNapaka);
            values.put("opis_postopka",tvSNNapaka2);
            if (tvSNGarancija.equals("0"))
            {
                values.putNull("garancija");
            }
            else if (tvSNGarancija.equals(""))
            {
                values.putNull("garancija");
            }
            else if (tvSNGarancija.toString().length() == 0)
            {
                values.putNull("garancija");
            }
            else
            {
                values.put("garancija",tvSNGarancija);
            }

            if (etSNUreDelo.equals("0"))
            {
                values.putNull("ure_delo");
            }
            else if (etSNUreDelo.equals(""))
            {
                values.putNull("ure_delo");
            }
            else if (etSNUreDelo.toString().length() == 0)
            {
                values.putNull("ure_delo");
            }
            else
            {
                values.put("ure_delo",etSNUreDelo);
            }
            //values.put("ure_delo",etSNUreDelo);

            if (etSNUrePrevoz.equals("0"))
            {
                values.putNull("ure_prevoz");
            }
            else if (etSNUrePrevoz.equals(""))
            {
                values.putNull("ure_prevoz");
            }
            else if (etSNUrePrevoz.toString().length() == 0)
            {
                values.putNull("ure_prevoz");
            }
            else
            {
                values.put("ure_prevoz",etSNUrePrevoz);
            }
            //values.put("ure_prevoz",etSNUrePrevoz);

            if (etSNStKm.equals("0"))
            {
                values.putNull("stevilo_km");
            }
            else if (etSNStKm.equals(""))
            {
                values.putNull("stevilo_km");
            }
            else if (etSNStKm.toString().length() == 0)
            {
                values.putNull("stevilo_km");
            }
            else
            {
                values.put("stevilo_km",etSNStKm);
            }
            //values.put("stevilo_km",etSNStKm);

            mDatabase.update("sintal_teh_sn", values, "id=?", new String[]{snID});

        }
        else
        {
            /*
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
            */
            //mDatabase.insert("sintal_teh_upo",null, values);
        }
        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }

    public void updatePodpis(String snID, byte[] image, String datum){
        mDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PODPIS_NAROCNIK",image);
        cv.put("DATUM_PODPISA", datum);
        cv.put("STATUS_AKT", "P");
        mDatabase.update("sintal_teh_sn",cv,"id = "+snID,null);

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();
    }

    public void updatePodpisVZDN(String stDN, byte[] image, String datum, String datum_izvedbe){
        mDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PODPIS_NAROCNIK",image);
        cv.put("DATUM_PODPISA", datum);
        cv.put("STATUS_AKT", "P");
        mDatabase.update("sintal_teh_vz_dn_periodika",cv,"st_del_naloga = "+stDN + " and DATUM_IZVEDBE =" + datum_izvedbe,null);

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();
    }

    public void updateSNPoslano(String snID, String status, String datum){
        mDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (status.equals("X") == true)
        {

            cv.put("STATUS_AKT",status);
            cv.put("DATUM_PRENOSA",status);
            cv.put("PRENOS", 1);
        }
        else
        {
            cv.put("STATUS_AKT",status);
        }

        //cv.put("DATUM_POSILJANJA", datum);
       //cv.put("STATUS_AKT", "P");
        mDatabase.update("sintal_teh_sn",cv,"id = "+snID,null);

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();
    }

    public void updateVZDNPoslano(String vzdnStDel, String status, String datumIzvedbe){
        mDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (status.equals("X") == true)
        {

            cv.put("STATUS_AKT",status);
            cv.put("DATUM_PRENOSA",status);
            cv.put("PRENOS", 1);
        }
        else
        {
            cv.put("STATUS_AKT",status);
        }

        //cv.put("DATUM_POSILJANJA", datum);
        //cv.put("STATUS_AKT", "P");
        mDatabase.update("sintal_teh_vz_per",cv,"st_del_naloga = '"+vzdnStDel +"' and DATUM_IZVEDBE = '"+datumIzvedbe+"'",null);

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();
    }

    public void deleteSNArtikelUserTehnik(int snId, String delovniNalog, String No_, int vrstaId, int userID, int tehnikID  )
    {
        mDatabase = this.getWritableDatabase();
        String query = "";
        query = "select * from sintal_teh_sn_artikli art  where art.No_ = " + No_ +
                " and art.upo_id = "+ userID +  " and art.tehnik_id = " + tehnikID + " and art.sn_id = " + snId  + " and art.vrsta_id = "+ vrstaId + " LIMIT 1";
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        int stZapisa = 0;
        while (mCursor.moveToNext()){
            stZapisa = mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id"));
        }
        if (stZapisa > 0)
        {
            query = "delete from sintal_teh_sn_artikli where sn_artikel_id = " + stZapisa;
            mDatabase.execSQL(query);
        }
        mCursor.close();
        mDatabase.close();
    }

    public void insertUpdateSNArtikelUserTehnik(int snId, String delovniNalog, String No_, int vrstaId, int userID, int tehnikID, float kolicina , String regal ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        String query = "";
        query = "select count(*) as st_vrstic from `sintal_teh_sn_artikli` " +
                "where No_ = '"+No_+"' " +
                "and sn_id = '"+String.valueOf(snId)+"' " +
                "and vrsta_id = '"+String.valueOf(vrstaId)+"' " +
                "and  upo_id = '"+String.valueOf(userID)+"' " +
                "and tehnik_id = '"+String.valueOf(tehnikID)+"' ;";
        mDatabase = this.getWritableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        int stZapisa = 0;
        while (mCursor.moveToNext()) {
            stZapisa = mCursor.getInt(mCursor.getColumnIndex("st_vrstic"));

            if (stZapisa == 0) {
                ContentValues values = new ContentValues();
                values.put("sn_id", snId);
                values.put("DELOVNI_NALOG", delovniNalog);
                values.put("No_", No_);
                values.put("kolicina", kolicina);
                values.put("vrsta_id", vrstaId);
                values.put("upo_id", userID);
                values.put("tehnik_id", tehnikID);
                values.put("REGAL", regal);

                mDatabase.insert("sintal_teh_sn_artikli", null, values);
            }
        }
        mCursor.close();



        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }


    public void insertSNArtikelUserTehnik(int snId, String delovniNalog, String No_, int vrstaId, int userID, int tehnikID, float kolicina , String regal ) {

        //ArrayList<NadzorXML> myList=new ArrayList<NadzorXML>();
        //List<Nadzor> myList=new ArrayList<Nadzor>();
        mDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sn_id",snId);
        values.put("DELOVNI_NALOG",delovniNalog);
        values.put("No_",No_);
        values.put("kolicina",kolicina);
        values.put("vrsta_id",vrstaId);
        values.put("upo_id",userID);
        values.put("tehnik_id",tehnikID);
        values.put("REGAL",regal);


        mDatabase.insert("sintal_teh_sn_artikli",null, values);

        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();

        //return myList;
    }

    public ArrayList<SNArtikel> GetSeznamArtikliDNSNUporabnik(int userId, int tehnikId, int vrstaId, int SnId)
    {
        ArrayList<SNArtikel> list = new ArrayList<SNArtikel>();
        String query = "";
        query = "select * , art.kolicina as Kolicina from sintal_teh_sn_artikli art,  sintal_teh_artikli snart where art.No_ = snart.No_ " +
                "and art.upo_id = "+ userId +  " and art.tehnik_id = " + tehnikId + " and art.sn_id = " + SnId  + " and art.vrsta_id = "+ vrstaId + " order by art.No_";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        while (mCursor.moveToNext()){
            SNArtikel sn = new SNArtikel();
            sn.setid(mCursor.getString(mCursor.getColumnIndex("No_")));
            sn.setnaziv(mCursor.getString(mCursor.getColumnIndex("Naziv")));
            sn.setnazivIskanje(mCursor.getString(mCursor.getColumnIndex("Naziv_iskanje")));
            sn.setKratkaOznaka(mCursor.getString(mCursor.getColumnIndex("Kratka_oznaka")));
            sn.setmerskaEnota(mCursor.getString(mCursor.getColumnIndex("Merska_enota")));
            sn.setSNArtikelId(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
            sn.setKolicina(mCursor.getFloat(mCursor.getColumnIndex("kolicina")));
            //sn.setOznacen(0);
            list.add(sn);
        }

        mCursor.close();
        mDatabase.close();
        return  list;

    }

    public String vrniPoslano(int snId, String userId) {
        String query = "select email || ' - '  || datum as poslano from sintal_teh_email_log where sn_id = "+ String.valueOf(snId) + " order by datum desc limit 1";
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        String vrni = "";
        while (mCursor.moveToNext()) {

            vrni = mCursor.getString(mCursor.getColumnIndex("poslano"));

            }
        mCursor.close();
        mDatabase.close();

        return vrni;
    }


    public JSONArray vrniEmailLog(int snId, String userId) {
        String query = "select * from sintal_teh_email_log where sn_id = "+ String.valueOf(snId) + " and user_id = " + userId;
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        JSONArray ja = new JSONArray();

        while (mCursor.moveToNext()) {

            try {
                JSONObject emailLog = new JSONObject();
                //int userID = ;
                emailLog.put("user_id", mCursor.getInt(mCursor.getColumnIndex("user_id")));
                emailLog.put("sn_id",mCursor.getInt(mCursor.getColumnIndex("sn_id")));
                emailLog.put("sn_dn",mCursor.getString(mCursor.getColumnIndex("sn_dn")));
                emailLog.put("email",mCursor.getString(mCursor.getColumnIndex("email")));
                emailLog.put("datum",mCursor.getString(mCursor.getColumnIndex("datum")));

                ja.put(emailLog);

            }
            catch (JSONException e)
            {

            }


        }
        mCursor.close();
        mDatabase.close();

        return ja;
    }

    public JSONArray vrniDDSN(int snId) {
        String query = "select * from sintal_teh_sn_dodatni_tehnik where sn_id = "+ String.valueOf(snId) + ";";
        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        JSONArray ja = new JSONArray();

        while (mCursor.moveToNext()) {

            try {
                JSONObject emailLog = new JSONObject();
                //int userID = ;
                emailLog.put("sn_id",mCursor.getInt(mCursor.getColumnIndex("sn_id")));
                emailLog.put("DELOVNI_NALOG",mCursor.getString(mCursor.getColumnIndex("DELOVNI_NALOG")));
                emailLog.put("tehnik_id",mCursor.getString(mCursor.getColumnIndex("tehnik_id")));
                ja.put(emailLog);
            }
            catch (JSONException e)
            {

            }


        }
        mCursor.close();
        mDatabase.close();

        return ja;
    }


    public ArrayList<SNArtikel> GetSeznamArtikliIzpisDNSNUporabnik(int userId, int tehnikId, int vrstaId, int SnId)
    {
        ArrayList<SNArtikel> list = new ArrayList<SNArtikel>();
        String query = "";
        query = "select * from sintal_teh_sn_artikli art,  sintal_teh_artikli snart where art.No_ = snart.No_ " +
                "and art.upo_id = "+ userId +  " and art.tehnik_id = " + tehnikId + " and art.sn_id = " + SnId  + " order by art.No_";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(query, null);
        //mCursor.moveToFirst();
        while (mCursor.moveToNext()){
            SNArtikel sn = new SNArtikel();
            sn.setid(mCursor.getString(mCursor.getColumnIndex("No_")));
            sn.setnaziv(mCursor.getString(mCursor.getColumnIndex("Naziv")));
            sn.setnazivIskanje(mCursor.getString(mCursor.getColumnIndex("Naziv_iskanje")));
            sn.setKratkaOznaka(mCursor.getString(mCursor.getColumnIndex("Kratka_oznaka")));
            sn.setmerskaEnota(mCursor.getString(mCursor.getColumnIndex("Merska_enota")));
            sn.setSNArtikelId(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
            sn.setZamenjanNov(mCursor.getInt(mCursor.getColumnIndex("vrsta_id")));
            sn.setKolicina(mCursor.getFloat(mCursor.getColumnIndex("kolicina")));
            sn.setRegal(mCursor.getString(mCursor.getColumnIndex("REGAL")));
            sn.setSn_id(mCursor.getInt(mCursor.getColumnIndex("sn_id")));
            sn.setSn_artikel_id(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
            sn.setDelovniNalog(mCursor.getString(mCursor.getColumnIndex("DELOVNI_NALOG")));
            sn.setUpoId(mCursor.getString(mCursor.getColumnIndex("upo_id")));
            sn.setTehnikId(mCursor.getString(mCursor.getColumnIndex("tehnik_id")));
            //sn.setOznacen(0);
            list.add(sn);
        }

        String query1 = "";
        query1 = "select * from sintal_teh_sn where  id = " + SnId  + "";
        String ime_vira = GetImeVira(tehnikId);
        //mDatabase = this.getReadableDatabase();
        mDatabase = this.getReadableDatabase();
        Cursor mCursor1;
        mCursor1 = mDatabase.rawQuery(query1, null);
        //mCursor1.moveToFirst();
        while (mCursor1.moveToNext()){
            Float ure_prevoz = mCursor1.getFloat(mCursor1.getColumnIndex("URE_PREVOZ"));
            Float ure_delo = mCursor1.getFloat(mCursor1.getColumnIndex("URE_DELO"));
            Float stevilo_km = mCursor1.getFloat(mCursor1.getColumnIndex("STEVILO_KM"));
            if (ure_prevoz > 0) {
                SNArtikel sn = new SNArtikel();
                sn.setid("S0053");
                sn.setnaziv("URA SERVISERJA NA VOŽNJI");
                sn.setnazivIskanje("");
                sn.setKratkaOznaka("");
                sn.setmerskaEnota("URA");
                sn.setSNArtikelId(9997);
                sn.setZamenjanNov(0);
                sn.setKolicina(ure_prevoz);
                sn.setRegal("");
                sn.setSn_id(mCursor1.getInt(mCursor1.getColumnIndex("id")));
                //sn.setSn_artikel_id(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
                sn.setDelovniNalog(mCursor1.getString(mCursor1.getColumnIndex("DELOVNI_NALOG")));
                sn.setUpoId(String.valueOf(userId));
                sn.setTehnikId(String.valueOf(tehnikId));
                list.add(sn);
            }

            if (ure_delo > 0) {
                SNArtikel sn = new SNArtikel();
                sn.setid("S0135");
                sn.setnaziv("URA SERVISERJA");
                sn.setnazivIskanje("");
                sn.setKratkaOznaka("");
                sn.setmerskaEnota("URA");
                sn.setSNArtikelId(9998);
                sn.setZamenjanNov(0);
                sn.setKolicina(ure_delo);
                sn.setRegal("");
                sn.setSn_id(mCursor1.getInt(mCursor1.getColumnIndex("id")));
                //sn.setSn_artikel_id(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
                sn.setDelovniNalog(mCursor1.getString(mCursor1.getColumnIndex("DELOVNI_NALOG")));
                sn.setUpoId(String.valueOf(userId));
                sn.setTehnikId(String.valueOf(tehnikId));
                list.add(sn);
            }

            if (stevilo_km > 0) {
                SNArtikel sn = new SNArtikel();
                sn.setid("S0042");
                sn.setnaziv("Kilometrina");
                sn.setnazivIskanje("");
                sn.setKratkaOznaka("");
                sn.setmerskaEnota("URA");
                sn.setSNArtikelId(9999);
                sn.setZamenjanNov(0);
                sn.setKolicina(stevilo_km);
                sn.setRegal("");
                sn.setSn_id(mCursor1.getInt(mCursor1.getColumnIndex("id")));
                //sn.setSn_artikel_id(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
                sn.setDelovniNalog(mCursor1.getString(mCursor1.getColumnIndex("DELOVNI_NALOG")));
                sn.setUpoId(String.valueOf(userId));
                sn.setTehnikId(String.valueOf(tehnikId));
                list.add(sn);
            }
            if ( (ure_delo + ure_prevoz > 0) && (ime_vira.length()>1))
            {
                SNArtikel sn = new SNArtikel();
                sn.setid(ime_vira);
                sn.setnaziv("VIR");
                sn.setnazivIskanje("");
                sn.setKratkaOznaka("");
                sn.setmerskaEnota("URA");
                sn.setSNArtikelId(9996);
                sn.setZamenjanNov(0);
                sn.setKolicina(ure_delo + ure_prevoz);
                sn.setRegal("");
                sn.setSn_id(mCursor1.getInt(mCursor1.getColumnIndex("id")));
                //sn.setSn_artikel_id(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
                sn.setDelovniNalog(mCursor1.getString(mCursor1.getColumnIndex("DELOVNI_NALOG")));
                sn.setUpoId(String.valueOf(userId));
                sn.setTehnikId(String.valueOf(tehnikId));
                list.add(sn);
            }

        }
    mCursor1.close();;


        String query2 = "";
        query2 = " select dt.tehnik_id, sn.URE_PREVOZ as URE_PREVOZ, sn.URE_DELO as URE_DELO, sn.STEVILO_KM as STEVILO_KM, sn.id,sn.DELOVNI_NALOG  as DELOVNI_NALOG from \n" +
                " sintal_teh_sn_dodatni_tehnik dt left join sintal_teh_sn sn on sn.id = dt.sn_id \n" +
                " where  dt.sn_id = "+SnId +";";

        //mDatabase = this.getReadableDatabase();
        mDatabase = this.getReadableDatabase();
        Cursor mCursor2;
        mCursor2 = mDatabase.rawQuery(query2, null);
        //mCursor2.moveToFirst();
        while (mCursor2.moveToNext()){
            int tehnik = mCursor2.getInt(mCursor2.getColumnIndex("tehnik_id"));
            String ime_vira2 = GetImeVira(tehnik);
            Float ure_prevoz = mCursor2.getFloat(mCursor2.getColumnIndex("URE_PREVOZ"));
            Float ure_delo = mCursor2.getFloat(mCursor2.getColumnIndex("URE_DELO"));
            Float stevilo_km = mCursor2.getFloat(mCursor2.getColumnIndex("STEVILO_KM"));
            /*
            if (ure_prevoz > 0) {
                SNArtikel sn = new SNArtikel();
                sn.setid("S0053");
                sn.setnaziv("URA SERVISERJA NA VOŽNJI");
                sn.setnazivIskanje("");
                sn.setKratkaOznaka("");
                sn.setmerskaEnota("URA");
                sn.setSNArtikelId(9997);
                sn.setZamenjanNov(0);
                sn.setKolicina(ure_prevoz);
                sn.setRegal("");
                sn.setSn_id(mCursor2.getInt(mCursor2.getColumnIndex("id")));
                //sn.setSn_artikel_id(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
                sn.setDelovniNalog(mCursor2.getString(mCursor2.getColumnIndex("DELOVNI_NALOG")));
                sn.setUpoId(String.valueOf(userId));
                sn.setTehnikId(String.valueOf(tehnik));
                list.add(sn);
            }

            if (ure_delo > 0) {
                SNArtikel sn = new SNArtikel();
                sn.setid("S0135");
                sn.setnaziv("URA SERVISERJA");
                sn.setnazivIskanje("");
                sn.setKratkaOznaka("");
                sn.setmerskaEnota("URA");
                sn.setSNArtikelId(9998);
                sn.setZamenjanNov(0);
                sn.setKolicina(ure_delo);
                sn.setRegal("");
                sn.setSn_id(mCursor2.getInt(mCursor2.getColumnIndex("id")));
                //sn.setSn_artikel_id(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
                sn.setDelovniNalog(mCursor2.getString(mCursor2.getColumnIndex("DELOVNI_NALOG")));
                sn.setUpoId(String.valueOf(userId));
                sn.setTehnikId(String.valueOf(tehnik));
                list.add(sn);
            }

            if (stevilo_km > 0) {
                SNArtikel sn = new SNArtikel();
                sn.setid("S0042");
                sn.setnaziv("Kilometrina");
                sn.setnazivIskanje("");
                sn.setKratkaOznaka("");
                sn.setmerskaEnota("URA");
                sn.setSNArtikelId(9999);
                sn.setZamenjanNov(0);
                sn.setKolicina(stevilo_km);
                sn.setRegal("");
                sn.setSn_id(mCursor2.getInt(mCursor2.getColumnIndex("id")));
                //sn.setSn_artikel_id(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
                sn.setDelovniNalog(mCursor2.getString(mCursor2.getColumnIndex("DELOVNI_NALOG")));
                sn.setUpoId(String.valueOf(userId));
                sn.setTehnikId(String.valueOf(tehnik));
                list.add(sn);
            }
            */
            if ( (ure_delo + ure_prevoz > 0) && (ime_vira2.length()>1))
            {
                SNArtikel sn = new SNArtikel();
                sn.setid(ime_vira2);
                sn.setnaziv("VIR");
                sn.setnazivIskanje("");
                sn.setKratkaOznaka("");
                sn.setmerskaEnota("URA");
                sn.setSNArtikelId(9996);
                sn.setZamenjanNov(0);
                sn.setKolicina(ure_delo + ure_prevoz);
                sn.setRegal("");
                sn.setSn_id(mCursor2.getInt(mCursor2.getColumnIndex("id")));
                //sn.setSn_artikel_id(mCursor.getInt(mCursor.getColumnIndex("sn_artikel_id")));
                sn.setDelovniNalog(mCursor2.getString(mCursor2.getColumnIndex("DELOVNI_NALOG")));
                sn.setUpoId(String.valueOf(userId));
                sn.setTehnikId(String.valueOf(tehnik));
                list.add(sn);
            }

        }
        mCursor2.close();

        mCursor.close();
        mDatabase.close();
        return  list;

    }

    public String getTehnikId(String tehnik)
    {
        mDatabase = this.getWritableDatabase();
        String tehnikid = "";
        String GET_USER = "SELECT * FROM sintal_teh_delavci where naziv = '" + tehnik + "'";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            tehnikid = mCursor.getString(0);
        }

        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();
        if (tehnikid.equals(""))
        {
            tehnikid = "0";
        }
        return tehnikid;
    }

    public String getTehnikImeRegala(String tehnik)
    {
        mDatabase = this.getWritableDatabase();
        String tehnikImeRegala = "";
        String GET_USER = "SELECT * FROM sintal_teh_delavci where tehnik_id = '" + tehnik + "'";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            tehnikImeRegala = mCursor.getString(6);
        }

        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();
        return tehnikImeRegala;
    }


    public String getTehnikNaziv(int tehnikID)
    {
        mDatabase = this.getWritableDatabase();
        String tehnikNaziv = "";
        String GET_USER = "SELECT * FROM sintal_teh_delavci where tehnik_id = '" + String.valueOf(tehnikID) + "'";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            tehnikNaziv = mCursor.getString(1);
        }

        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();
        if (tehnikNaziv.equals(""))
        {
            tehnikNaziv = "";
        }
        return tehnikNaziv;
    }

    public String getUporabnikNaziv(int tehnikID)
    {
        mDatabase = this.getWritableDatabase();
        String tehnikNaziv = "";
        String GET_USER = "SELECT * FROM sintal_teh_upo where user_id = '" + String.valueOf(tehnikID) + "'";
        mCursor = mDatabase.rawQuery(GET_USER,null);
        //NadzorXML n;
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        if (mCursor.moveToFirst()) {
            tehnikNaziv = mCursor.getString(2) + " " + mCursor.getString(3);
            tehnikNaziv = tehnikNaziv.toUpperCase();
        }

        mCursor.close();

        //mDatabase.execSQL(INSERT_INTO_USERS_TABLE);
        mDatabase.close();
        if (tehnikNaziv.equals(""))
        {
            tehnikNaziv = "";
        }
        return tehnikNaziv;
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


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
import java.util.List;

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


    public List<String> getTehnikiInString() {
        List<String> userList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM sintal_teh_delavci";

        mDatabase = this.getReadableDatabase();
        mCursor = mDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {
            do {
                userList.add(mCursor.getString(1) +", " + mCursor.getString(0));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        mDatabase.close();
        // return contact list
        return userList;
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

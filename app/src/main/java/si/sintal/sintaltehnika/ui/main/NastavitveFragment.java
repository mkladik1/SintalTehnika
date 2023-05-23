package si.sintal.sintaltehnika.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.GlavnoOkno;
import si.sintal.sintaltehnika.R;

public class NastavitveFragment extends Fragment {

    private NastavitveViewModel mViewModel;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;


    public static NastavitveFragment newInstance() {
        return new NastavitveFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v_nastavitve = inflater.inflate(R.layout.nastavitve_fragment, container, false);
        Intent intent= getActivity().getIntent();
        tehnikID = intent.getStringExtra("userID");
        tehnikNaziv = intent.getStringExtra("userName");
        tehnikEmail = intent.getStringExtra("email");
        tehnikAdminDostop = intent.getStringExtra("admin");
        servis = intent.getStringExtra("servis");
        montaza = intent.getStringExtra("montaza");
        vzdrzevanje = intent.getStringExtra("vzdrzevanje");

        ProgressBar pbPrenos = (ProgressBar) v_nastavitve.findViewById(R.id.progressBarPrenos);



        Button bNazaj = (Button) v_nastavitve.findViewById(R.id.bNastToGlavno);
        bNazaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GlavnoOkno.class);
                intent.putExtra("userID", tehnikID);
                intent.putExtra("userName", tehnikNaziv);
                intent.putExtra("email", tehnikEmail);
                intent.putExtra("admin",tehnikAdminDostop);
                intent.putExtra("servis",servis);
                intent.putExtra("montaza",montaza);
                intent.putExtra("vzdrzevanje",vzdrzevanje);
                startActivity(intent);
            }
        });


        Button bPrenesiTehnike = (Button) v_nastavitve.findViewById(R.id.bPrenesiTehnike);
        bPrenesiTehnike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbPrenos.setVisibility(View.VISIBLE);
                new LoadTehnikiFromWeb().execute();

            }
        });

        Button bPrenesiArtikle = (Button) v_nastavitve.findViewById(R.id.bPrenesiArtikle);
        bPrenesiArtikle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbPrenos.setVisibility(View.VISIBLE);
                new LoadArtikliFromWeb().execute();

            }
        });

        Button bPrenesiUporabnike = (Button) v_nastavitve.findViewById(R.id.bPrenesiUporabnike);
        bPrenesiUporabnike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbPrenos.setVisibility(View.VISIBLE);
                new LoadUporabnikiFromWeb().execute();
                //new LoadUporabnikiDostopiFromWeb().execute();
                //new LoadSNPripadnostFromWeb().execute();

            }
        });

        Button bPrenesiSN = (Button) v_nastavitve.findViewById(R.id.bPrenesiSN);
        bPrenesiSN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbPrenos.setVisibility(View.VISIBLE);
                new LoadSNFromWeb().execute();


            }
        });


        return v_nastavitve;
    }



    private class LoadTehnikiFromWeb extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            //MySQLDatabase.mysqlConnect();
            String result = null;
            try {
                URL url = new URL("https://www.sintal.si/tehnika/getDelavci.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getDelavci";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                conn.setRequestProperty ("Authorization", basicAuth);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }

            } catch (Exception  e) {
                e.printStackTrace();
            }
            //return result;
            if (result != null)
            {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray dataArray = obj.getJSONArray("tehniki");
                    //int pageName = obj.getJSONArray("users").length();
                    for (int i=0; i < dataArray.length() ; i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);
                        String tehnik_id = object.getString("tehnik_id");
                        String naziv = object.getString("naziv");
                        String servis = object.getString("servis");
                        String montaza = object.getString("montaza");
                        String vzdrzevanje = object.getString("vzdrzevanje");
                        String user_id = object.getString("user_id");
                        DatabaseHandler db = new DatabaseHandler(getContext());
                        db.insertUpdateTehnik(tehnik_id,naziv,servis, montaza, vzdrzevanje, user_id);
                    }
                    //getString("user_id");//.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgressBar pbPrenos1 = (ProgressBar) getView().findViewById(R.id.progressBarPrenos);
            pbPrenos1.setVisibility(View.GONE);

        }

    }


    private class LoadArtikliFromWeb extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            //MySQLDatabase.mysqlConnect();
            String result = null;
            try {
                URL url = new URL("https://www.sintal.si/tehnika/getArtikli.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getArtikli";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                conn.setRequestProperty ("Authorization", basicAuth);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }

            } catch (Exception  e) {
                e.printStackTrace();
            }
            //return result;
            if (result != null)
            {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray dataArray = obj.getJSONArray("artikli");
                    //int pageName = obj.getJSONArray("users").length();
                    for (int i=0; i < dataArray.length() ; i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);
                        String No_ = object.getString("No_");
                        String Naziv = object.getString("Naziv");
                        String Naziv_iskanje = object.getString("Naziv_iskanje");
                        String Merska_enota = object.getString("Merska_enota");
                        String Kratka_oznaka = object.getString("Kratka_oznaka");
                        //String user_id = object.getString("user_id");
                        DatabaseHandler db = new DatabaseHandler(getContext());
                        db.insertUpdateArtikel(No_,Naziv,Naziv_iskanje, Merska_enota, Kratka_oznaka);
                    }
                    //getString("user_id");//.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgressBar pbPrenos1 = (ProgressBar) getView().findViewById(R.id.progressBarPrenos);
            pbPrenos1.setVisibility(View.GONE);

        }

    }

    private class LoadUporabnikiFromWeb extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            //MySQLDatabase.mysqlConnect();
            String result = null;
            try {
                URL url = new URL("https://www.sintal.si/tehnika/getUsers.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getUsers";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                conn.setRequestProperty ("Authorization", basicAuth);
                conn.connect();


                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }
                conn.disconnect();

            } catch (Exception  e) {
                e.printStackTrace();
            }
            //return result;
            if (result != null)
            {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray dataArray = obj.getJSONArray("users");
                    //int pageName = obj.getJSONArray("users").length();
                    for (int i=0; i < dataArray.length() ; i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);
                        String user_id = object.getString("user_id");
                        String upo_ime = object.getString("upo_ime");
                        String imeNadozrnik = object.getString("ime");
                        String priimekNadzornik = object.getString("priimek");
                        String geslo = object.getString("prijava");
                        String email = object.getString("email");
                        String admin_dostop = object.getString("admin_dostop");
                        String servis = object.getString("servis");
                        String montaza = object.getString("montaza");
                        String vzdrzevanje = object.getString("vzdrzevanje");
                        DatabaseHandler db = new DatabaseHandler(getContext());
                        db.insertUpdateUser(user_id,upo_ime,imeNadozrnik,priimekNadzornik,geslo,email,admin_dostop,servis, montaza, vzdrzevanje);
                    }
                    //getString("user_id");//.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            result = null;
            try {
                URL url = new URL("https://www.sintal.si/tehnika/getUsersDostop.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getUsersDostop";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                conn.setRequestProperty ("Authorization", basicAuth);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }
                conn.disconnect();

            } catch (Exception  e) {
                e.printStackTrace();
            }
            //return result;
            if (result != null)
            {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray dataArray = obj.getJSONArray("users_dostop");
                    //int pageName = obj.getJSONArray("users").length();
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    db.deleteUpdateupoTehnik();
                    for (int i=0; i < dataArray.length() ; i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);
                        String user_id = object.getString("user_id");
                        String tehnik_id = object.getString("tehnik_id");


                        db.insertUpdateupoTehnik(user_id,tehnik_id);
                    }
                    //getString("user_id");//.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            result = null;
            try {
                //using var response = await httpClient.GetAsync(sb.ToString());
                //string apiResponse = await response.Content.ReadAsStringAsync();
                URL url = new URL("https://www.sintal.si/tehnika/getPripadnost.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getPripadnost";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                conn.setRequestProperty ("Authorization", basicAuth);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }
                conn.disconnect();

            } catch (Exception  e) {
                e.printStackTrace();
            }
            //return result;
            if (result != null)
            {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray dataArray = obj.getJSONArray("pripadnost_teh");
                    //int pageName = obj.getJSONArray("users").length();
                    for (int i=0; i < dataArray.length() ; i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);
                        String pripadnost = object.getString("pripadnost");
                        String pripadnost_vnc = object.getString("pripadnost_vnc");

                        DatabaseHandler db = new DatabaseHandler(getContext());
                        db.insertUpdatePripadnost(pripadnost,pripadnost_vnc);
                    }
                    //getString("user_id");//.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgressBar pbPrenos1 = (ProgressBar) getView().findViewById(R.id.progressBarPrenos);
            pbPrenos1.setVisibility(View.GONE);

        }
    }

    private class LoadSNFromWeb extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            //MySQLDatabase.mysqlConnect();
            //ProgressBar pbPrenos = (ProgressBar) getView().findViewById(R.id.progressBarPrenos);
            //pbPrenos.setVisibility(View.VISIBLE);
            String result = null;
            try {
                URL url = new URL("https://www.sintal.si/tehnika/getSN.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getSN";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                conn.setRequestProperty ("Authorization", basicAuth);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }
                conn.disconnect();

            } catch (Exception  e) {
                e.printStackTrace();
            }

            //return result;
            if (result != null)
            {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray dataArray = obj.getJSONArray("sn");
                    //int pageName = obj.getJSONArray("users").length();
                    for (int i=0; i < dataArray.length() ; i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);
                        String id = object.getString("id");
                        String DELOVNI_NALOG = object.getString("DELOVNI_NALOG");
                        String NAZIV = object.getString("NAZIV");
                        NAZIV = NAZIV.replaceAll("è","č");
                        NAZIV = NAZIV.replaceAll("È","Č");
                        String OE = object.getString("OE");
                        String DATUM_ZACETEK = object.getString("DATUM_ZACETEK");
                        String DATUM_KONEC = object.getString("DATUM_KONEC");

                        String OPIS = object.getString("OPIS");
                        OPIS = OPIS.replaceAll("è","č");
                        OPIS = OPIS.replaceAll("È","Č");
                        String KLIENT = object.getString("KLIENT");
                        String TIP_DEL_NAL = object.getString("TIP_DEL_NAL");
                        String STATUS_AKT = object.getString("STATUS_AKT");
                        String XUSER = object.getString("XUSER");
                        String XDATETIME = object.getString("XDATETIME");
                        String VRSTA_DN = object.getString("VRSTA_DN");
                        String STATUS_DN = object.getString("STATUS_DN");
                        String PLANIRANE_URE = object.getString("PLANIRANE_URE");
                        String DELOVNI_NALOG_IZV = object.getString("DELOVNI_NALOG_IZV");
                        String OBJEKT_FAKT = object.getString("OBJEKT_FAKT");
                        String ODGOVORNA_OSEBA = object.getString("ODGOVORNA_OSEBA");
                        ODGOVORNA_OSEBA = ODGOVORNA_OSEBA.replaceAll("è","č");
                        ODGOVORNA_OSEBA = ODGOVORNA_OSEBA.replaceAll("È","Č");
                        String IZDAJATELJ_NALOGA = object.getString("IZDAJATELJ_NALOGA");
                        IZDAJATELJ_NALOGA = IZDAJATELJ_NALOGA.replaceAll("è","č");
                        IZDAJATELJ_NALOGA = IZDAJATELJ_NALOGA.replaceAll("È","Č");
                        String TIP_NAROCILA = object.getString("TIP_NAROCILA");
                        String TIP_VZDRZEVANJA = object.getString("TIP_VZDRZEVANJA");
                        String KODA_OBJEKTA = object.getString("KODA_OBJEKTA");
                        String NAV_ERROR = object.getString("NAV_ERROR");
                        String NAV_PRIPADNOST = object.getString("NAV_PRIPADNOST");
                        String NAV_KODA_OBJ = object.getString("NAV_KODA_OBJ");
                        String PRIPADNOST = object.getString("PRIPADNOST");
                        String PRIPADNOST_VNC = object.getString("PRIPADNOST_VNC");
                        String NAV_STATUS_PODIZV = object.getString("NAV_STATUS_PODIZV");
                        String NAV_KLIENT = object.getString("NAV_KLIENT");
                        String NAV_OBJEKT_FAKT = object.getString("NAV_OBJEKT_FAKT");
                        String NAROCNIK_NAZIV = object.getString("NAROCNIK_NAZIV");
                        NAROCNIK_NAZIV = NAROCNIK_NAZIV.replaceAll("è","č");
                        NAROCNIK_NAZIV = NAROCNIK_NAZIV.replaceAll("È","Č");

                        String NAROCNIK_NASLOV = object.getString("NAROCNIK_NASLOV");
                        NAROCNIK_NASLOV = NAROCNIK_NASLOV.replaceAll("è","č");
                        NAROCNIK_NASLOV = NAROCNIK_NASLOV.replaceAll("È","Č");

                        String NAROCNIK_ULICA = object.getString("NAROCNIK_ULICA");
                        NAROCNIK_ULICA = NAROCNIK_ULICA.replaceAll("è","č");
                        NAROCNIK_ULICA = NAROCNIK_ULICA.replaceAll("È","Č");

                        String NAROCNIK_KRAJ = object.getString("NAROCNIK_KRAJ");
                        NAROCNIK_KRAJ = NAROCNIK_KRAJ.replaceAll("è","č");
                        NAROCNIK_KRAJ = NAROCNIK_KRAJ.replaceAll("È","Č");

                        String NAROCNIK_HISNA_ST = object.getString("NAROCNIK_HISNA_ST");

                        String NAROCNIK_IME_SEKTORJA = object.getString("NAROCNIK_IME_SEKTORJA");
                        NAROCNIK_IME_SEKTORJA = NAROCNIK_IME_SEKTORJA.replaceAll("è","č");
                        NAROCNIK_IME_SEKTORJA = NAROCNIK_IME_SEKTORJA.replaceAll("È","Č");

                        String SEKTOR_NASLOV  = object.getString("SEKTOR_NASLOV");
                        SEKTOR_NASLOV = SEKTOR_NASLOV.replaceAll("è","č");
                        SEKTOR_NASLOV = SEKTOR_NASLOV.replaceAll("È","Č");

                        String SEKTOR_POSTNA_ST = object.getString("SEKTOR_POSTNA_ST");
                        SEKTOR_POSTNA_ST = SEKTOR_POSTNA_ST.replaceAll("è","č");
                        SEKTOR_POSTNA_ST = SEKTOR_POSTNA_ST.replaceAll("È","Č");

                        String VODJA_NALOGA = object.getString("VODJA_NALOGA");
                        String DATUM_IZVEDBE = object.getString("DATUM_IZVEDBE");
                        String DATUM_PODPISA = object.getString("DATUM_PODPISA");
                        String DATUM_DODELITVE = object.getString("DATUM_DODELITVE");


                        DatabaseHandler db = new DatabaseHandler(getContext());
                        db.insertUpdateSN(id,
                                DELOVNI_NALOG,
                                NAZIV,
                                OE,
                                DATUM_ZACETEK,
                                DATUM_KONEC,
                                OPIS,
                                KLIENT,
                                TIP_DEL_NAL,
                                STATUS_AKT,
                                XUSER,
                                XDATETIME,
                                VRSTA_DN,
                                STATUS_DN,
                                PLANIRANE_URE,
                                DELOVNI_NALOG_IZV,
                                OBJEKT_FAKT,
                                ODGOVORNA_OSEBA,
                                IZDAJATELJ_NALOGA,
                                TIP_NAROCILA,
                                TIP_VZDRZEVANJA,
                                KODA_OBJEKTA,
                                NAV_ERROR,
                                NAV_PRIPADNOST,
                                NAV_KODA_OBJ,
                                PRIPADNOST,
                                PRIPADNOST_VNC,
                                NAV_STATUS_PODIZV,
                                NAV_KLIENT,
                                NAV_OBJEKT_FAKT,
                                VODJA_NALOGA,
                                NAROCNIK_NAZIV,
                                NAROCNIK_NASLOV,
                                NAROCNIK_ULICA,
                                NAROCNIK_KRAJ,
                                NAROCNIK_HISNA_ST,
                                NAROCNIK_IME_SEKTORJA,
                                SEKTOR_NASLOV,
                                SEKTOR_POSTNA_ST,
                                DATUM_PODPISA,
                                DATUM_DODELITVE,
                                DATUM_IZVEDBE
                        );
                    }
                    //getString("user_id");//.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgressBar pbPrenos1 = (ProgressBar) getView().findViewById(R.id.progressBarPrenos);
            pbPrenos1.setVisibility(View.GONE);

        }

    }

    private class LoadUporabnikiDostopiFromWeb extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            //MySQLDatabase.mysqlConnect();
            String result = null;
            try {
                URL url = new URL("https://www.sintal.si/tehnika/getUsersDostop.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getUsersDostop";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                conn.setRequestProperty ("Authorization", basicAuth);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }

            } catch (Exception  e) {
                e.printStackTrace();
            }
            //return result;
            if (result != null)
            {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray dataArray = obj.getJSONArray("users_dostop");
                    //int pageName = obj.getJSONArray("users").length();
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    db.deleteUpdateupoTehnik();
                    for (int i=0; i < dataArray.length() ; i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);
                        String user_id = object.getString("user_id");
                        String tehnik_id = object.getString("tehnik_id");


                        db.insertUpdateupoTehnik(user_id,tehnik_id);
                    }
                    //getString("user_id");//.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgressBar pbPrenos1 = (ProgressBar) getView().findViewById(R.id.progressBarPrenos);
            pbPrenos1.setVisibility(View.GONE);

        }
    }

    private class LoadSNPripadnostFromWeb extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            //MySQLDatabase.mysqlConnect();
            String result = null;
            try {
                //using var response = await httpClient.GetAsync(sb.ToString());
                //string apiResponse = await response.Content.ReadAsStringAsync();
                URL url = new URL("https://www.sintal.si/tehnika/getPripadnost.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getPripadnost";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                conn.setRequestProperty ("Authorization", basicAuth);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }

            } catch (Exception  e) {
                e.printStackTrace();
            }
            //return result;
            if (result != null)
            {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray dataArray = obj.getJSONArray("pripadnost_teh");
                    //int pageName = obj.getJSONArray("users").length();
                    for (int i=0; i < dataArray.length() ; i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);
                        String pripadnost = object.getString("pripadnost");
                        String pripadnost_vnc = object.getString("pripadnost_vnc");

                        DatabaseHandler db = new DatabaseHandler(getContext());
                        db.insertUpdatePripadnost(pripadnost,pripadnost_vnc);
                    }
                    //getString("user_id");//.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ProgressBar pbPrenos1 = (ProgressBar) getView().findViewById(R.id.progressBarPrenos);
            pbPrenos1.setVisibility(View.GONE);

        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NastavitveViewModel.class);
        // TODO: Use the ViewModel
    }

}
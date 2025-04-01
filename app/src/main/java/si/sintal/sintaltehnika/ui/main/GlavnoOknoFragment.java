package si.sintal.sintaltehnika.ui.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.MainActivity;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.RegistracijaCasa;
import si.sintal.sintaltehnika.ServisActivity;
import si.sintal.sintaltehnika.VZSeznamActivity;

public class GlavnoOknoFragment extends Fragment {

    private GlavnoOknoViewModel mViewModel;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;

    public static GlavnoOknoFragment newInstance() {
        return new GlavnoOknoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.glavno_okno_fragment, container, false);
        Intent intent= getActivity().getIntent();
        tehnikID = intent.getStringExtra("userID");
        tehnikNaziv = intent.getStringExtra("userName");
        tehnikEmail = intent.getStringExtra("email");
        tehnikAdminDostop = intent.getStringExtra("admin");
        servis = intent.getStringExtra("servis");
        montaza = intent.getStringExtra("montaza");
        vzdrzevanje = intent.getStringExtra("vzdrzevanje");

        Button bOdjava = (Button) view.findViewById(R.id.bOdjava);
        Button bServis = (Button) view.findViewById(R.id.bServisniNalogi);
        Button bNastavitve = (Button) view.findViewById(R.id.bNastavitve);
        Button bDodeliSN = (Button) view.findViewById(R.id.bDodeliSN);
        Button bRegCasa = (Button) view.findViewById(R.id.bRegistracija);
        Button bVzdrzevanje = (Button) view.findViewById(R.id.bVzdrzevanje);
        Button bMontaza = (Button) view.findViewById(R.id.bMontaza);

        bOdjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("userID", "");
                intent.putExtra("userName", "");
                intent.putExtra("email", "");
                intent.putExtra("admin","");
                startActivity(intent);
            }
        });

        bRegCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegistracijaCasa.class);
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

        bServis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServisActivity.class);
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


        bNastavitve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NastavitveActivity.class);
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


        bDodeliSN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tehnikAdminDostop.equals("1")) {
                    Intent intent = new Intent(getActivity(), DodeliSNActivity.class);
                    intent.putExtra("userID", tehnikID);
                    intent.putExtra("userName", tehnikNaziv);
                    intent.putExtra("email", tehnikEmail);
                    intent.putExtra("admin", tehnikAdminDostop);
                    intent.putExtra("servis", servis);
                    intent.putExtra("montaza", montaza);
                    intent.putExtra("vzdrzevanje", vzdrzevanje);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Nimate pravic do tega modula!", Toast.LENGTH_LONG).show();
                }
            }
        });


        bVzdrzevanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vzdrzevanje.equals("1")) {
                    //new LoadVZDNFromWeb().execute();
                    Intent intent = new Intent(getActivity(), VZSeznamActivity.class);
                    intent.putExtra("userID", tehnikID);
                    intent.putExtra("userName", tehnikNaziv);
                    intent.putExtra("email", tehnikEmail);
                    intent.putExtra("admin", tehnikAdminDostop);
                    intent.putExtra("servis", servis);
                    intent.putExtra("montaza", montaza);
                    intent.putExtra("vzdrzevanje", vzdrzevanje);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Nimate pravic do tega modula!", Toast.LENGTH_LONG).show();
                }
            }
        });

        bMontaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (montaza.equals("1")) {
                    //new LoadVZDNFromWeb().execute();
                    Intent intent = new Intent(getActivity(), MontazaActivity.class);
                    intent.putExtra("userID", tehnikID);
                    intent.putExtra("userName", tehnikNaziv);
                    intent.putExtra("email", tehnikEmail);
                    intent.putExtra("admin", tehnikAdminDostop);
                    intent.putExtra("servis", servis);
                    intent.putExtra("montaza", montaza);
                    intent.putExtra("vzdrzevanje", vzdrzevanje);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Nimate pravic do tega modula!", Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView tvImePriimek = (TextView) view.findViewById(R.id.tvImePriimek);
        tvImePriimek.setText("Vpisani ste kot: "+tehnikNaziv );


        return view;//inflater.inflate(R.layout.glavno_okno_fragment, container, false);
    }

    private class LoadVZDNFromWeb extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            //MySQLDatabase.mysqlConnect();
            String result = null;
            try {
                URL url = new URL("https://www.sintal.si/tehnika/getVZD.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getVZD";
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
                    JSONArray dataArray = obj.getJSONArray("vz_dn");
                    //int pageName = obj.getJSONArray("users").length();
                    for (int i=0; i < dataArray.length() ; i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);

                        String id = object.getString("id");
                        String vz_id = object.getString("sintal_vzd_dn_id");
                        String st_del_naloga = object.getString("st_del_naloga");
                        String naziv_servisa = object.getString("naziv_servisa");
                        naziv_servisa = naziv_servisa.replaceAll("è","č");
                        naziv_servisa = naziv_servisa.replaceAll("È","Č");
                        String r_objekt_id = object.getString("r_objekt_id");
                        String ime = object.getString("ime");
                        String mc_objekt_fakt = object.getString("mc_objekt_fakt");
                        String mc_klient = object.getString("mc_klient");
                        mc_klient = mc_klient.replaceAll("è","č");
                        mc_klient = mc_klient.replaceAll("È","Č");

                        String mc_ime_objekta = object.getString("mc_ime_objekta");
                        String pogodba = object.getString("pogodba");
                        String periodika_dni = object.getString("periodika_dni");
                        String kontaktna_oseba = object.getString("kontaktna_oseba");
                        String opomba = object.getString("opomba");
                        String telefon = object.getString("telefon");
                        String datum_zadnjega = object.getString("datum_zadnjega");
                        String datum_naslednjega = object.getString("datum_naslednjega");
                        String sis_pozar = object.getString("sis_pozar");
                        String sis_vlom = object.getString("sis_vlom");

                        String sis_video = object.getString("sis_video");
                        //sis_video = sis_video.replaceAll("è","č");
                        //sis_video = sis_video.replaceAll("È","Č");
                        String sis_co = object.getString("sis_co");
                        //IZDAJATELJ_NALOGA = IZDAJATELJ_NALOGA.replaceAll("è","č");
                        //IZDAJATELJ_NALOGA = IZDAJATELJ_NALOGA.replaceAll("È","Č");
                        String sis_pristopna = object.getString("sis_pristopna");
                        String sis_dimni_banokvci = object.getString("sis_dimni_banokvci");
                        String sis_ostalo = object.getString("sis_ostalo");
                        String oprema = object.getString("oprema");
                        String prenos_alarma = object.getString("prenos_alarma");
                        String Dok_BD = object.getString("Dok_BD");
                        String datum_veljavnosti_Dok_BD = object.getString("datum_veljavnosti_Dok_BD");
                        String servis_izvajalec = object.getString("servis_izvajalec");
                        String koda_objekta = object.getString("koda_objekta");
                        //String PODPIS_NAROCNIK = object.getString("PODPIS_NAROCNIK");
                        //String PODPIS_SERVISER = object.getString("PODPIS_SERVISER");
                        String STATUS = object.getString("STATUS");
                        String DATUM_DODELITVE = object.getString("DATUM_DODELITVE");
                        String DATUM_IZVEDBE = object.getString("DATUM_IZVEDBE");
                        String PRENOS = object.getString("PRENOS");
                        String DATUM_PRENOSA = object.getString("DATUM_PRENOSA");
                        String OPIS_OKVARE = object.getString("OPIS_OKVARE");
                        String OPIS_POSTOPKA  = object.getString("OPIS_POSTOPKA");
                        String URE_PREVOZ = object.getString("URE_PREVOZ");
                        String URE_DELO = object.getString("URE_DELO");
                        String STEVILO_KM = object.getString("STEVILO_KM");
                        String DATUM_PODPISA = object.getString("DATUM_PODPISA");
                        String NASLOV_OBJEKTA = object.getString("naslov_objekta");

                        String PODJETJE = object.getString("podjetje");

                        String NAROCNIK = object.getString("narocnik");
                        String NAROCNIK_NASLOV = object.getString("narocnik_naslov");
                        String OBJEKT = object.getString("objekt");

                        DatabaseHandler db = new DatabaseHandler(getContext());
                        db.insertUpdateVZDN(
                                id,
                                vz_id,
                                st_del_naloga,
                                naziv_servisa,
                                r_objekt_id,
                                ime,
                                mc_objekt_fakt,
                                mc_klient,
                                mc_ime_objekta,
                                pogodba,

                                periodika_dni,
                                kontaktna_oseba,
                                opomba,
                                telefon,
                                datum_zadnjega,
                                datum_naslednjega,
                                sis_pozar,
                                sis_vlom,
                                sis_video,
                                sis_co,

                                sis_pristopna,
                                sis_dimni_banokvci,
                                sis_ostalo,
                                oprema,
                                prenos_alarma,
                                //Dok_BD,
                                //datum_veljavnosti_Dok_BD,
                                servis_izvajalec,
                                koda_objekta,

                                STATUS,
                                DATUM_DODELITVE,
                                DATUM_IZVEDBE,
                                PRENOS,
                                DATUM_PRENOSA,
                                OPIS_OKVARE,
                                OPIS_POSTOPKA,
                                URE_PREVOZ,
                                URE_DELO,
                                STEVILO_KM,

                                DATUM_PODPISA,
                                NASLOV_OBJEKTA,
                                PODJETJE,
                                NAROCNIK,
                                NAROCNIK_NASLOV,
                                OBJEKT
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
            //ProgressBar pbPrenos1 = (ProgressBar) getView().findViewById(R.id.progressBarPrenos);
            //pbPrenos1.setVisibility(View.GONE);

        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(GlavnoOknoViewModel.class);
        //mViewModel = new ViewModelProvider(this).get(GlavnoOknoViewModel.class);
        // TODO: Use the ViewModel
    }

}
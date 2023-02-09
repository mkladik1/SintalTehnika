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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                new LoadTehnikiFromWeb().execute();
            }
        });

        Button bPrenesiUporabnike = (Button) v_nastavitve.findViewById(R.id.bPrenesiUporabnike);
        bPrenesiUporabnike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadUporabnikiFromWeb().execute();
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

    }

    private class LoadUporabnikiFromWeb extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            //MySQLDatabase.mysqlConnect();
            String result = null;
            try {
                URL url = new URL("https://www.sintal.si/tehnika/getUsers.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

            return null;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NastavitveViewModel.class);
        // TODO: Use the ViewModel
    }

}
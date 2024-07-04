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
import java.util.ArrayList;
import java.util.Base64;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.GlavnoOkno;
import si.sintal.sintaltehnika.R;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);



        TextView tvUpIme = (TextView) getActivity().findViewById(R.id.editTextTextPersonName);
        TextView tvGeslo = (TextView) getActivity().findViewById(R.id.editTextTextPassword);
        tvUpIme.setText("");
        tvGeslo.setText("");

        DatabaseHandler db = new DatabaseHandler(getContext());
        db.createTablesLogin();
        if (db.usersLogin() == 0)
        {
            new LoadFromWeb().execute();
        }

        Button login;
        login = (Button) getActivity().findViewById(R.id.bLogin);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tvUpIme = (TextView) getActivity().findViewById(R.id.editTextTextPersonName);
                TextView tvGeslo = (TextView) getActivity().findViewById(R.id.editTextTextPassword);
                DatabaseHandler db = new DatabaseHandler(getContext());
                ArrayList<String> upo = new ArrayList<String>();
                upo = db.getUserLogin(tvUpIme.getText().toString(),tvGeslo.getText().toString());
                String userID = upo.get(0);
                String nadzornik = upo.get(1) + " " + upo.get(2);
                String email =  upo.get(4);
                String admin_dostop =  upo.get(5);
                String servis = upo.get(6);
                String montaza = upo.get(7);
                String vzdrzevanje = upo.get(8);
                if (upo.get(0).equals(""))
                {
                    Toast.makeText(getContext(),"Napačno uporabniško ime", Toast.LENGTH_LONG).show();
                }
                else if (upo.get(3).equals(""))
                {
                    Toast.makeText(getContext(),"Napačno geslo", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), GlavnoOkno.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("userName", nadzornik);
                    intent.putExtra("email", email);
                    intent.putExtra("admin",admin_dostop);
                    intent.putExtra("servis",servis);
                    intent.putExtra("montaza",montaza);
                    intent.putExtra("vzdrzevanje",vzdrzevanje);
                    startActivity(intent);
                }

            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }


    private class LoadFromWeb extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            //MySQLDatabase.mysqlConnect();
            String result = null;
            try {
                URL url = new URL("https://www.sintal.si/tehnika/getUsers.php");
                HttpURLConnection connU = (HttpURLConnection) url.openConnection();
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-getUsers";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
                connU.setRequestProperty ("Authorization", basicAuth);
                connU.connect();

                if (connU.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(connU.getInputStream());
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
                connU.disconnect();

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

}
package si.sintal.sintaltehnika.ui.main;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.GlavnoOkno;
import si.sintal.sintaltehnika.R;

public class DodaliSNFragment extends Fragment {

    private DodeliSNViewModel mViewModel;
    //static List<String> seznamSN;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;
    private String userID;
    //static ListView seznamDNdodelitve;
    static DatabaseHandler db;
    static ArrayList<ServisniNalog> dodeliSNje;
    public DodelitevSNAdapter adapterSeznamSNjev;
    public ListView listView;
    public Spinner status_sn;
    public Spinner pripadnost_sn;
    public Spinner serviser_sn;
    private DatePickerDialog mDatePickerDialog;
    private EditText etSN;
    public static DodaliSNFragment newInstance() {
        return new DodaliSNFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v_dodeli_sn = inflater.inflate(R.layout.dodeli_sn_fragment, container, false);

        Intent intent= getActivity().getIntent();
        tehnikID = intent.getStringExtra("userID");
        userID = intent.getStringExtra("userID");
        tehnikNaziv = intent.getStringExtra("userName");
        tehnikEmail = intent.getStringExtra("email");
        tehnikAdminDostop = intent.getStringExtra("admin");
        servis = intent.getStringExtra("servis");
        montaza = intent.getStringExtra("montaza");
        vzdrzevanje = intent.getStringExtra("vzdrzevanje");
        db = new DatabaseHandler(getContext());

        Button bNazaj = (Button) v_dodeli_sn.findViewById(R.id.bDodeliSNToGlavno);
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        setDateTimeField();
        etSN = (EditText) v_dodeli_sn.findViewById(R.id.etDatumSN_dodeli);
        etSN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });

        etSN.setText(currentDateandTime);
        //etSN.setText("16.02.2023");

        status_sn = (Spinner) v_dodeli_sn.findViewById(R.id.spinner_status_SN_dodeli);
        status_sn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String text = status_sn.getSelectedItem().toString();
                String spVal = "";
                String status_akt = "";
                if (text.equals("NEDODELJENI"))
                {
                    spVal = "-1";
                    status_akt = "A";
                }
                else if (text.equals("DODELJENI"))
                {
                    spVal = "0";
                    status_akt = "D";

                }
                else if (text.equals("STORNO"))
                {
                    spVal = "1";
                    status_akt = "S";
                }
                //db.updateSNOznaciId(etSN.getText().toString());
                //dodeliSNje = db.GetSeznamSNDodelitev(etSN.getText().toString(), spVal);
                dodeliSNje = db.GetSeznamSNDodelitev( spVal, status_akt, tehnikID, etSN.getText().toString());
                adapterSeznamSNjev = new DodelitevSNAdapter(getActivity(), dodeliSNje, userID);
                listView.setAdapter(adapterSeznamSNjev);
                //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                adapterSeznamSNjev.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        String text = status_sn.getSelectedItem().toString();
        String spVal = "";
        String status_akt = "";
        if (text.equals("NEDODELJENI"))
        {
            spVal = "-1";
            status_akt = "A";
        }
        else if (text.equals("DODELJENI"))
        {
            spVal = "0";
            status_akt = "D";

        }
        else if (text.equals("STORNO"))
        {
            spVal = "1";
            status_akt = "S";
        }

        serviser_sn = (Spinner) v_dodeli_sn.findViewById(R.id.spinner_user_SN_dodeli);
        List<String> lables = db.getTehnikiFromUserInString(Integer.parseInt(tehnikID),1, 0);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviser_sn.setAdapter(dataAdapter);

        pripadnost_sn = (Spinner) v_dodeli_sn.findViewById(R.id.spinner_pripadnost_SN_dodeli);
        List<String> lables2 = db.getPripadnostInString();
        final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pripadnost_sn.setAdapter(dataAdapter2);

        //db.updateSNOznaciDatum(etSN.getText().toString());
        //dodeliSNje = db.GetSeznamSNDodelitev(etSN.getText().toString(), spVal);
        dodeliSNje = db.GetSeznamSNDodelitev( spVal,status_akt, tehnikID, etSN.getText().toString());
        adapterSeznamSNjev = new DodelitevSNAdapter(getActivity(), dodeliSNje, userID);
        listView = (ListView) v_dodeli_sn.findViewById(R.id.seznamSNDodelitve);
        listView.setAdapter(adapterSeznamSNjev);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapterSeznamSNjev.notifyDataSetChanged();

        Button bSpremeniServiserja = (Button) v_dodeli_sn.findViewById(R.id.bSpremeniServiserja);
        bSpremeniServiserja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stevec=0;
                ArrayList<String> parameters = new ArrayList<String>();
                for(int i=0; i<=dodeliSNje.size()-1;i++)
                {
                    if (dodeliSNje.get(i).getOznacen() == 1)
                    {
                        stevec = stevec + 1;
                        String datumDodelitve = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        db.updateSNServiser(Integer.toString(dodeliSNje.get(i).getid()),serviser_sn.getSelectedItem().toString(), datumDodelitve, "D");

                        parameters.add(Integer.toString(dodeliSNje.get(i).getid()));
                        db.updateSNOznaciId(Integer.toString(dodeliSNje.get(i).getid()),"0");
                        //parameters.add(serviser_sn.getSelectedItem().toString());
                        //parameters.add("D");
                        //updateVodjaMySql(Integer.toString(dodeliSNje.get(i).getid()),serviser_sn.getSelectedItem().toString(),"D");
                        //updateVodjaPrenosiMySql(Integer.toString(dodeliSNje.get(i).getid()),serviser_sn.getSelectedItem().toString(),"D");

                    }
                }
                if (stevec > 0)
                {
                    String text = status_sn.getSelectedItem().toString();
                    String spVal = "";
                    String status_akt = "";
                    if (text.equals("NEDODELJENI"))
                    {
                        spVal = "-1";
                        status_akt = "A";
                    }
                    else if (text.equals("DODELJENI"))
                    {
                        spVal = "0";
                        status_akt = "D";

                    }
                    else if (text.equals("STORNO"))
                    {
                        spVal = "1";
                        status_akt = "S";
                    }
                    dodeliSNje = db.GetSeznamSNDodelitev( spVal,status_akt, tehnikID, etSN.getText().toString());
                    adapterSeznamSNjev = new DodelitevSNAdapter(getActivity(), dodeliSNje, userID);
                    //listView = (ListView) v_dodeli_sn.findViewById(R.id.seznamSNDodelitve);
                    listView.setAdapter(adapterSeznamSNjev);
                    //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    adapterSeznamSNjev.notifyDataSetChanged();

                }
                new UpdateVodjaOnWeb().execute(parameters);
            }
        });

        return v_dodeli_sn;
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                etSN.setText(fdate);
                String text = status_sn.getSelectedItem().toString();
                String spVal = "";
                String status_akt = "";
                if (text.equals("NEDODELJENI"))
                {
                    spVal = "-1";
                    status_akt = "A";
                }
                else if (text.equals("DODELJENI"))
                {
                    spVal = "0";
                    status_akt = "D";

                }
                else if (text.equals("STORNO"))
                {
                    spVal = "1";
                    status_akt = "S";
                }

                //dodeliSNje = db.GetSeznamSNDodelitev(etSN.getText().toString(), spVal);
                dodeliSNje = db.GetSeznamSNDodelitev( spVal,status_akt, tehnikID, etSN.getText().toString());
                adapterSeznamSNjev = new DodelitevSNAdapter(getContext(), dodeliSNje, userID);
                listView.setAdapter(adapterSeznamSNjev);
                //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                adapterSeznamSNjev.notifyDataSetChanged();

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    public void updateVodjaMySql(String id, String vodja, String status_akt, String datum)
    {
        String result = null;
        try {
            String myUrl = "";
            if (vodja.equals(""))
            {
                 myUrl = "https://www.sintal.si/tehnika/updateSNVodjaNaloga.php?SNid="+id+"&vodjaNaloga= &status_akt="+status_akt+"&datum_dodelitve="+datum;
            }
            else
            {
                 myUrl = "https://www.sintal.si/tehnika/updateSNVodjaNaloga.php?SNid="+id+"&vodjaNaloga="+vodja+"&status_akt="+status_akt+"&datum_dodelitve="+datum;
            }

            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String username ="sintal_teh";
            String password = "mCuSTArQ*PdWAH#7-";
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

    }

    public void updateVodjaPrenosiMySql(String id, String vodja, String status_akt, String datum)
    {
        String result = null;
        try {
            String myUrl = "https://www.sintal.si/tehnika/updateSNVodjaNalogaPrenosi.php?SNid="+id+"&vodjaNaloga="+vodja+"&status_akt="+status_akt+"&datum_dodelitve="+datum;
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String username ="sintal_teh";
            String password = "mCuSTArQ*PdWAH#7-";
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

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DodeliSNViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        db = new DatabaseHandler(getContext());



    }

    private class UpdateVodjaOnWeb extends AsyncTask<ArrayList,Void,Void> {

        @Override
        protected Void doInBackground(ArrayList... arrayLists) {
            String datum = "";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            datum = dtf.format(now);
            db = new DatabaseHandler(getContext());
            for (int i = 0; i<arrayLists[0].size();i++) {
                ServisniNalog sn = new ServisniNalog();
                sn = db.vrniSN(Integer.parseInt(arrayLists[0].get(i).toString()));
                String vodja = sn.getVodjaNaloga();
                updateVodjaMySql(arrayLists[0].get(i).toString(), vodja, sn.getStatus(), datum);
                //updateVodjaMySql(arrayLists[0].get(i).toString(), serviser_sn.getSelectedItem().toString(), sn.getStatus(), datum);
                updateVodjaPrenosiMySql(arrayLists[0].get(i).toString(), vodja, sn.getStatus(), datum);
                //updateVodjaPrenosiMySql(arrayLists[0].get(i).toString(), serviser_sn.getSelectedItem().toString(), sn.getStatus(), datum);
            }
            return null;
        }
    }

}
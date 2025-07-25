package si.sintal.sintaltehnika.ui.main.SN;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class seznamSNFragment extends Fragment {

    private SeznamSNViewModel mViewModel;
    private String userID;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;
    private String SNId;
    private String SNDn;
    static DatabaseHandler db;
    static ArrayList<ServisniNalog> dodeliSNje;
    private SeznamUpoSNAdapter adapterSeznamUpoSNjev;
    private ListView listView;
    private Spinner status_sn;
    private Spinner izbraniServiser;
    private Spinner izbraniStatus;

    public static seznamSNFragment newInstance() {
        return new seznamSNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seznam_s_n_fragment, container, false);

        Intent intent= getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        tehnikID = intent.getStringExtra("tehnikID");
        tehnikNaziv = intent.getStringExtra("userName");
        tehnikEmail = intent.getStringExtra("email");
        tehnikAdminDostop = intent.getStringExtra("admin");
        servis = intent.getStringExtra("servis");
        montaza = intent.getStringExtra("montaza");
        vzdrzevanje = intent.getStringExtra("vzdrzevanje");
        if (tehnikID == null)
        {
            tehnikID = "0";
        }
        if(userID == null)
        {
            userID = "0";
        }
        db = new DatabaseHandler(getContext());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());

        List<String> lables = db.getTehnikiFromUserInString(Integer.parseInt(userID), 1, 1);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        izbraniServiser = (Spinner) v.findViewById(R.id.spinner_user_SN);
        izbraniServiser.setAdapter(dataAdapter);

        tehnikID = db.getTehnikUporabnik(userID);
        tehnikNaziv = db.getTehnikUporabnikNaziv(userID);
        int sppos = dataAdapter.getPosition(tehnikNaziv);
        if (sppos > 0) {
            izbraniServiser.setSelection(sppos);
        }


        izbraniStatus = (Spinner) v.findViewById(R.id.spinner_LM_DN_SN);


        //dodeliSNje = db.GetSeznamSNUporabnik(etSN.getText().toString(), izbraniServiser.getSelectedItem().toString());
        dodeliSNje = db.GetSeznamSNUporabnik(izbraniServiser.getSelectedItem().toString(),"D");
        adapterSeznamUpoSNjev = new SeznamUpoSNAdapter(getActivity(), dodeliSNje, Integer.parseInt(tehnikID), Integer.parseInt(userID));
        listView = (ListView) v.findViewById(R.id.seznamSNUpoLV);
        listView.setAdapter(adapterSeznamUpoSNjev);

        izbraniServiser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String status = izbraniStatus.getSelectedItem().toString();
                status = status.substring(0,1);
                dodeliSNje = db.GetSeznamSNUporabnik(izbraniServiser.getSelectedItem().toString(),status);
                tehnikID = db.getTehnikId(izbraniServiser.getSelectedItem().toString());
                adapterSeznamUpoSNjev = new SeznamUpoSNAdapter(getActivity(), dodeliSNje, Integer.parseInt(tehnikID), Integer.parseInt(userID));
                listView = (ListView) v.findViewById(R.id.seznamSNUpoLV);
                listView.setAdapter(adapterSeznamUpoSNjev);

                //Toast.makeText(getActivity(), "Something changed", LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        izbraniStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String status = izbraniStatus.getSelectedItem().toString();
                status = status.substring(0,1);
                dodeliSNje = db.GetSeznamSNUporabnik(izbraniServiser.getSelectedItem().toString(),status);
                tehnikID = db.getTehnikId(izbraniServiser.getSelectedItem().toString());
                adapterSeznamUpoSNjev = new SeznamUpoSNAdapter(getActivity(), dodeliSNje, Integer.parseInt(tehnikID), Integer.parseInt(userID));
                listView = (ListView) v.findViewById(R.id.seznamSNUpoLV);
                listView.setAdapter(adapterSeznamUpoSNjev);
                //Toast.makeText(getActivity(), "Something changed", LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        tehnikID = db.getTehnikId(izbraniServiser.getSelectedItem().toString());

        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //adapterSeznamUpoSNjev.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SeznamSNViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume(){
        super.onResume();
        db = new DatabaseHandler(getContext());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());

        List<String> lables = db.getTehnikiFromUserInString(Integer.parseInt(userID), 1, 1);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        izbraniServiser = (Spinner) getView().findViewById(R.id.spinner_user_SN);
        izbraniServiser.setAdapter(dataAdapter);

        tehnikID = db.getTehnikUporabnik(userID);
        tehnikNaziv = db.getTehnikUporabnikNaziv(userID);
        int sppos = dataAdapter.getPosition(tehnikNaziv);
        if (sppos > 0) {
            izbraniServiser.setSelection(sppos);
        }

        izbraniStatus = (Spinner) getView().findViewById(R.id.spinner_LM_DN_SN);


        //dodeliSNje = db.GetSeznamSNUporabnik(etSN.getText().toString(), izbraniServiser.getSelectedItem().toString());
        dodeliSNje = db.GetSeznamSNUporabnik(izbraniServiser.getSelectedItem().toString(),"D");
        adapterSeznamUpoSNjev = new SeznamUpoSNAdapter(getActivity(), dodeliSNje, Integer.parseInt(tehnikID), Integer.parseInt(userID));
        listView = (ListView) getView().findViewById(R.id.seznamSNUpoLV);
        listView.setAdapter(adapterSeznamUpoSNjev);

        izbraniServiser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String status = izbraniStatus.getSelectedItem().toString();
                status = status.substring(0,1);
                dodeliSNje = db.GetSeznamSNUporabnik(izbraniServiser.getSelectedItem().toString(),status);
                tehnikID = db.getTehnikId(izbraniServiser.getSelectedItem().toString());
                adapterSeznamUpoSNjev = new SeznamUpoSNAdapter(getActivity(), dodeliSNje, Integer.parseInt(tehnikID), Integer.parseInt(userID));
                listView = (ListView) getView().findViewById(R.id.seznamSNUpoLV);
                listView.setAdapter(adapterSeznamUpoSNjev);
                //Toast.makeText(getActivity(), "Something changed", LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        izbraniStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String status = izbraniStatus.getSelectedItem().toString();
                status = status.substring(0,1);
                dodeliSNje = db.GetSeznamSNUporabnik(izbraniServiser.getSelectedItem().toString(),status);
                tehnikID = db.getTehnikId(izbraniServiser.getSelectedItem().toString());
                adapterSeznamUpoSNjev = new SeznamUpoSNAdapter(getActivity(), dodeliSNje, Integer.parseInt(tehnikID), Integer.parseInt(userID));
                listView = (ListView) getView().findViewById(R.id.seznamSNUpoLV);
                listView.setAdapter(adapterSeznamUpoSNjev);
                //Toast.makeText(getActivity(), "Something changed", LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

}
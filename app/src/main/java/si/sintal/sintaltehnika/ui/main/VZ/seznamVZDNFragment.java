package si.sintal.sintaltehnika.ui.main.VZ;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZ;

public class seznamVZDNFragment extends Fragment {

    private SeznamVZDNViewModel mViewModel;
    private String userID;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;
    private String VZId;
    private String VZDn;
    static DatabaseHandler db;
    static ArrayList<DelovniNalogVZ> dodeliVZDNje;
    private SeznamUpoVZDNAdapter adaptersSeznamUpoDNjevVZD;
    private ListView listView;
    private Spinner status_sn;
    private Spinner izbraniVzdrzevalec;
    private Spinner izbraniLM;

    public static seznamVZDNFragment newInstance() {
        return new seznamVZDNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.seznam_vz_dn_fragment, container, false);;


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

        List<String> listLM = new ArrayList<String>();
        for (int year = 2024; year <= 2025; year++) {
            for (int month = 1; month <= 12; month++) {
                String yearMonth = String.format("%d%02d", year, month);
                listLM.add(yearMonth);
            }
        }
        final ArrayAdapter<String> dataAdapterLM = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,listLM);
        dataAdapterLM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        izbraniLM = (Spinner) v.findViewById(R.id.spinner_LM_DN_VZ);
        izbraniLM.setAdapter(dataAdapterLM);
        Calendar c = Calendar.getInstance();
        int yearIz = c.get(Calendar.YEAR);
        int monthIz = c.get(Calendar.MONTH) + 1;
        String yearMonthIbr = String.format("%d%02d", yearIz, monthIz);
        izbraniLM.setSelection(dataAdapterLM.getPosition(yearMonthIbr));

        List<String> lables = db.getTehnikiUporabnikInString(userID);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        izbraniVzdrzevalec = (Spinner) v.findViewById(R.id.spinner_user_VZ);
        izbraniVzdrzevalec.setAdapter(dataAdapter);

        dodeliVZDNje = db.GetSeznamVZDNUporabnik(izbraniVzdrzevalec.getSelectedItem().toString(),"D",yearMonthIbr);
        adaptersSeznamUpoDNjevVZD = new SeznamUpoVZDNAdapter(getActivity(), dodeliVZDNje, Integer.parseInt(tehnikID), Integer.parseInt(userID),yearMonthIbr);
        listView = (ListView) v.findViewById(R.id.seznamDNUpoLVVZD);
        listView.setAdapter(adaptersSeznamUpoDNjevVZD);

        izbraniVzdrzevalec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                /*
                dodeliVZDNje = db.GetSeznamVZDNUporabnik(izbraniVzdrzevalec.getSelectedItem().toString(),"D",izbraniLM.getSelectedItem().toString());
                tehnikID = db.getTehnikId(izbraniVzdrzevalec.getSelectedItem().toString());
                adaptersSeznamUpoDNjevVZD = new SeznamUpoVZDNAdapter(getActivity(), dodeliVZDNje, Integer.parseInt(tehnikID), Integer.parseInt(userID));
                listView = (ListView) v.findViewById(R.id.seznamSNUpoLV);
                listView.setAdapter(adaptersSeznamUpoDNjevVZD);
                */
                //Toast.makeText(getActivity(), "Something changed", LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        izbraniLM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String yearMonthIbr = izbraniLM.getSelectedItem().toString();
                //status = status.substring(0,1);
                dodeliVZDNje = db.GetSeznamVZDNUporabnik(izbraniVzdrzevalec.getSelectedItem().toString(),"D",yearMonthIbr);
                adaptersSeznamUpoDNjevVZD = new SeznamUpoVZDNAdapter(getActivity(), dodeliVZDNje, Integer.parseInt(tehnikID), Integer.parseInt(userID), yearMonthIbr);
                listView = (ListView) v.findViewById(R.id.seznamDNUpoLVVZD);
                listView.setAdapter(adaptersSeznamUpoDNjevVZD);
                //Toast.makeText(getActivity(), "Something changed", LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        tehnikID = db.getTehnikId(izbraniVzdrzevalec.getSelectedItem().toString());


        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SeznamVZDNViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();

        List<String> listLM = new ArrayList<String>();
        for (int year = 2024; year <= 2025; year++) {
            for (int month = 1; month <= 12; month++) {
                String yearMonth = String.format("%d%02d", year, month);
                listLM.add(yearMonth);
            }
        }
        final ArrayAdapter<String> dataAdapterLM = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,listLM);
        dataAdapterLM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        izbraniLM = (Spinner) getView().findViewById(R.id.spinner_LM_DN_VZ);
        izbraniLM.setAdapter(dataAdapterLM);
        Calendar c = Calendar.getInstance();
        int yearIz = c.get(Calendar.YEAR);
        int monthIz = c.get(Calendar.MONTH) + 1;
        String yearMonthIbr = String.format("%d%02d", yearIz, monthIz);
        izbraniLM.setSelection(dataAdapterLM.getPosition(yearMonthIbr));
        dodeliVZDNje = db.GetSeznamVZDNUporabnik(izbraniVzdrzevalec.getSelectedItem().toString(),"D",yearMonthIbr);
        adaptersSeznamUpoDNjevVZD = new SeznamUpoVZDNAdapter(getActivity(), dodeliVZDNje, Integer.parseInt(tehnikID), Integer.parseInt(userID),yearMonthIbr);
        listView = (ListView) getView().findViewById(R.id.seznamDNUpoLVVZD);
        listView.setAdapter(adaptersSeznamUpoDNjevVZD);

    }

}
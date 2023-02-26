package si.sintal.sintaltehnika.ui.main.SN;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.DodelitevSNAdapter;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class seznamSNFragment extends Fragment {

    private SeznamSNViewModel mViewModel;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;
    static DatabaseHandler db;
    static ArrayList<ServisniNalog> dodeliSNje;
    private SeznamUpoSNAdapter adapterSeznamUpoSNjev;
    private ListView listView;
    private Spinner status_sn;
    private Spinner izbraniServiser;

    public static seznamSNFragment newInstance() {
        return new seznamSNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seznam_s_n_fragment, container, false);

        Intent intent= getActivity().getIntent();
        tehnikID = intent.getStringExtra("userID");
        tehnikNaziv = intent.getStringExtra("userName");
        tehnikEmail = intent.getStringExtra("email");
        tehnikAdminDostop = intent.getStringExtra("admin");
        servis = intent.getStringExtra("servis");
        montaza = intent.getStringExtra("montaza");
        vzdrzevanje = intent.getStringExtra("vzdrzevanje");

        db = new DatabaseHandler(getContext());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());
        EditText etSN = (EditText) v.findViewById(R.id.etDatumSN);
        etSN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Toast.makeText(getContext(),((EditText) v).getId() + " has focus - " + hasFocus, Toast.LENGTH_LONG).show();
                String text = izbraniServiser.getSelectedItem().toString();
                String datum = etSN.getText().toString();
                dodeliSNje = db.GetSeznamSNUporabnik(datum, text);
                //dodeliSNje = db.GetSeznamSNUporabnik(etSN.getText().toString(), text);
                adapterSeznamUpoSNjev = new SeznamUpoSNAdapter(getActivity(), dodeliSNje);
                listView.setAdapter(adapterSeznamUpoSNjev);
                adapterSeznamUpoSNjev.notifyDataSetChanged();
            }
        });
        etSN.setText(currentDateandTime);

        List<String> lables = db.getTehnikiUporabnikInString(tehnikID);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        izbraniServiser = (Spinner) v.findViewById(R.id.spinner_user_SN);
        izbraniServiser.setAdapter(dataAdapter);

        dodeliSNje = db.GetSeznamSNUporabnik(etSN.getText().toString(), izbraniServiser.getSelectedItem().toString());
        adapterSeznamUpoSNjev = new SeznamUpoSNAdapter(getActivity(), dodeliSNje);
        listView = (ListView) v.findViewById(R.id.seznamSNUpoLV);
        listView.setAdapter(adapterSeznamUpoSNjev);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapterSeznamUpoSNjev.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SeznamSNViewModel.class);
        // TODO: Use the ViewModel
    }

}
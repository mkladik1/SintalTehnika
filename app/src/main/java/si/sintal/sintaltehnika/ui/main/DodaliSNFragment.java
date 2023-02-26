package si.sintal.sintaltehnika.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.GlavnoOkno;
import si.sintal.sintaltehnika.R;

import static android.icu.text.DisplayContext.LENGTH_SHORT;

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
    //static ListView seznamDNdodelitve;
    static DatabaseHandler db;
    static ArrayList<ServisniNalog> dodeliSNje;
    private DodelitevSNAdapter adapterSeznamSNjev;
    private ListView listView;
    private Spinner status_sn;

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

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());
        EditText etSN = (EditText) v_dodeli_sn.findViewById(R.id.etDatumSN_dodeli);
        etSN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Toast.makeText(getContext(),((EditText) v).getId() + " has focus - " + hasFocus, Toast.LENGTH_LONG).show();
                String text = status_sn.getSelectedItem().toString();
                if (text.equals("nedodeljeni"))
                {
                    dodeliSNje = db.GetSeznamSNDodelitev(etSN.getText().toString(), "-1");
                    adapterSeznamSNjev = new DodelitevSNAdapter(getActivity(), dodeliSNje);
                    listView.setAdapter(adapterSeznamSNjev);
                    //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    adapterSeznamSNjev.notifyDataSetChanged();
                }

                else
                {
                    dodeliSNje = db.GetSeznamSNDodelitev(etSN.getText().toString(), "0");
                    adapterSeznamSNjev = new DodelitevSNAdapter(getActivity(), dodeliSNje);
                    listView.setAdapter(adapterSeznamSNjev);
                    //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    adapterSeznamSNjev.notifyDataSetChanged();
                }
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
                if (text.equals("nedodeljeni"))
                {
                    spVal = "-1";
                }
                else
                {
                    spVal = "0;";

                }
                db.updateSNOznaciDatum(etSN.getText().toString());
                dodeliSNje = db.GetSeznamSNDodelitev(etSN.getText().toString(), spVal);
                adapterSeznamSNjev = new DodelitevSNAdapter(getActivity(), dodeliSNje);
                listView.setAdapter(adapterSeznamSNjev);
                //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                adapterSeznamSNjev.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        List<String> lables = db.getTehnikiInString();
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) v_dodeli_sn.findViewById(R.id.spinner_user_SN_dodeli);
        spinner.setAdapter(dataAdapter);

        String text = status_sn.getSelectedItem().toString();
        String spVal = "";
        if (text.equals("nedodeljeni"))
        {
            spVal = "-1";
        }
        else
        {
            spVal = "0;";

        }

        db.updateSNOznaciDatum(etSN.getText().toString());
        dodeliSNje = db.GetSeznamSNDodelitev(etSN.getText().toString(), spVal);
        adapterSeznamSNjev = new DodelitevSNAdapter(getActivity(), dodeliSNje);
        listView = (ListView) v_dodeli_sn.findViewById(R.id.seznamSNDodelitve);
        listView.setAdapter(adapterSeznamSNjev);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapterSeznamSNjev.notifyDataSetChanged();

        Button bSpremeniServiserja = (Button) v_dodeli_sn.findViewById(R.id.bSpremeniServiserja);
        bSpremeniServiserja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stevec=0;
                for(int i=0; i<=dodeliSNje.size()-1;i++)
                {

                    if (dodeliSNje.get(i).getOznacen() == 1)
                    {
                        stevec = stevec + 1;
                        db.updateSNServiser(Integer.toString(dodeliSNje.get(i).getid()),spinner.getSelectedItem().toString());
                    }
                }
                if (stevec > 0)
                {
                    String text = status_sn.getSelectedItem().toString();
                    String spVal = "";
                    if (text.equals("nedodeljeni"))
                    {
                        spVal = "-1";
                    }
                    else
                    {
                        spVal = "0;";

                    }
                    dodeliSNje = db.GetSeznamSNDodelitev(etSN.getText().toString(), spVal);
                    adapterSeznamSNjev = new DodelitevSNAdapter(getActivity(), dodeliSNje);
                    //listView = (ListView) v_dodeli_sn.findViewById(R.id.seznamSNDodelitve);
                    listView.setAdapter(adapterSeznamSNjev);
                    //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    adapterSeznamSNjev.notifyDataSetChanged();

                }
            }
        });

        /*
n
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(getActivity(), R.array.status_sn, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Spinner spinner2 = (Spinner) v_dodeli_sn.findViewById(R.id.spinner_status_SN_dodeli);
        spinner2.setAdapter(adapter);

        seznamDNdodelitve = (ListView) v_dodeli_sn.findViewById(R.id.seznamSNDodelitve);
        seznamDNdodelitve.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
         snList = db.GetSeznamSN(etSN.getText().toString(),"-1");
        ListAdapter adapterSN = new SimpleAdapter(getContext(), snList, R.layout.seznamdodelisn,new String[]{"stevilka_sn","narocnik","opis"}, new int[]{R.id.labelstevilkaSN, R.id.labelSNNarocnik, R.id.labelSNOpisNapke});
        seznamDNdodelitve.setAdapter(adapterSN);
        ((BaseAdapter) seznamDNdodelitve.getAdapter()).notifyDataSetChanged();

*/


        return v_dodeli_sn;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DodeliSNViewModel.class);
        // TODO: Use the ViewModel
    }

}
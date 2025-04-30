package si.sintal.sintaltehnika.ui.main.VZ;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZ;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZPeriodika;
import si.sintal.sintaltehnika.ui.main.SN.SNSeznamZakljucenihSNAdapter;
import si.sintal.sintaltehnika.ui.main.SN.obrazecSNFragment;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class VZZakljuceniDNFragment extends Fragment {

    private String userID;
    private String tehnikID;
    private Spinner izbraniServiser;
    private Spinner izbraniStatus;
    static DatabaseHandler db;
    static ArrayList<DelovniNalogVZ> dodeliSNje;
    private VZDNSeznamZakljucenihAdapter adapterSeznamZakljucenih;
    private ListView listView;

    private VZZakljuceniDNViewModel mViewModel;

    public static VZZakljuceniDNFragment newInstance() {
        return new VZZakljuceniDNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vz_zakljuceni_dn_fragment, container, false);

        Intent intent= getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        tehnikID = intent.getStringExtra("tehnikID");
        tehnikID = String.valueOf(VZPagerAdapter.getTehnikID());
        //tehnikNaziv = intent.getStringExtra("userName");
        //tehnikEmail = intent.getStringExtra("email");
        //tehnikAdminDostop = intent.getStringExtra("admin");
        //servis = intent.getStringExtra("servis");
        //montaza = intent.getStringExtra("montaza");
        //vzdrzevanje = intent.getStringExtra("vzdrzevanje");

        db = new DatabaseHandler(getContext());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());

        List<String> lables = db.getTehnikiUporabnikInString(userID);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        izbraniServiser = (Spinner) v.findViewById(R.id.spinner_user_VZ_DN_Zak);
        izbraniServiser.setAdapter(dataAdapter);
        izbraniStatus = (Spinner) v.findViewById(R.id.spinner_LM_DN_VZ_Zak);
        izbraniStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String status = izbraniStatus.getSelectedItem().toString();

                if (status.equals("ZAKLJUČENI")){
                    status = "Z";
                }
                else {
                    status = "X";
                }

                //status = status.substring(0,1);

                dodeliSNje = db.GetSeznamVZDNUporabnik(izbraniServiser.getSelectedItem().toString(),status,"");
                tehnikID = db.getTehnikId(izbraniServiser.getSelectedItem().toString());
                adapterSeznamZakljucenih = new VZDNSeznamZakljucenihAdapter(getActivity(), dodeliSNje,userID,tehnikID);
                listView = (ListView) v.findViewById(R.id.seznamVZZakljuceni);
                listView.setAdapter(adapterSeznamZakljucenih);
                //Toast.makeText(getActivity(), "Something changed", LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });





        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VZZakljuceniDNViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();


        db = new DatabaseHandler(getContext());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());

        List<String> lables = db.getTehnikiUporabnikInString(userID);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        izbraniServiser = (Spinner) getView().findViewById(R.id.spinner_user_VZ_DN_Zak);
        izbraniServiser.setAdapter(dataAdapter);
        izbraniStatus = (Spinner) getView().findViewById(R.id.spinner_LM_DN_VZ_Zak);
        izbraniStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String status = izbraniStatus.getSelectedItem().toString();

                if (status.equals("ZAKLJUČENI")){
                    status = "Z";
                }
                else {
                    status = "X";
                }

                //status = status.substring(0,1);

                dodeliSNje = db.GetSeznamVZDNUporabnik(izbraniServiser.getSelectedItem().toString(),status,"");
                tehnikID = db.getTehnikId(izbraniServiser.getSelectedItem().toString());
                adapterSeznamZakljucenih = new VZDNSeznamZakljucenihAdapter(getActivity(), dodeliSNje,userID,tehnikID);
                listView = (ListView) getView().findViewById(R.id.seznamVZZakljuceni);
                listView.setAdapter(adapterSeznamZakljucenih);
                //Toast.makeText(getActivity(), "Something changed", LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



    }

}
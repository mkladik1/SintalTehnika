package si.sintal.sintaltehnika.ui.main.VZ;

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
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZ;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZPeriodika;
import si.sintal.sintaltehnika.ui.main.SN.SNSeznamZakljucenihSNAdapter;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class VZZakljuceniDNFragment extends Fragment {

    private String userID;
    private String tehnikID;
    private Spinner izbraniServiser;
    private Spinner izbraniStatus;
    static DatabaseHandler db;
    static ArrayList<DelovniNalogVZ> dodeliSNje;
    private VZDNSeznamZakljucenihAdapter adapterSeznamUpoVZDNjev;
    private ListView listView;

    private VZZakljuceniDNViewModel mViewModel;

    public static VZZakljuceniDNFragment newInstance() {
        return new VZZakljuceniDNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vz_zakljuceni_dn_fragment, container, false);

        db = new DatabaseHandler(getContext());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());

        List<String> lables = db.getTehnikiUporabnikInString(userID);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        izbraniServiser = (Spinner) view.findViewById(R.id.spinner_user_VZ);
        izbraniServiser.setAdapter(dataAdapter);
        izbraniStatus = (Spinner) view.findViewById(R.id.spinner_LM_DN_VZ);
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
                adapterSeznamUpoVZDNjev = new VZDNSeznamZakljucenihAdapter(getActivity(), dodeliSNje,userID,tehnikID);
                listView = (ListView) view.findViewById(R.id.seznamVZZakljuceni);
                listView.setAdapter(adapterSeznamUpoVZDNjev);
                //Toast.makeText(getActivity(), "Something changed", LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VZZakljuceniDNViewModel.class);
        // TODO: Use the ViewModel
    }

}
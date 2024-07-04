package si.sintal.sintaltehnika.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;

public class RegistracijaCasaFragment extends Fragment {

    private RegistracijaCasaModel mViewModel;
    DatabaseHandler db;
    private String userID;
    static ArrayList<RegCasa> seznamRegCasaUporabnik;
    private RegistracijaCasaListViewAdapter adapterSeznamRCUpo;
    private ListView listView;

    public static RegistracijaCasaFragment newInstance() {
        return new RegistracijaCasaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View regCasa = inflater.inflate(R.layout.registracija_casa, container, false);

        db = new DatabaseHandler(getContext());
        Intent intent= getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear ; i >= thisYear - 1; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, years);
        /*
        Spinner leto = regCasa.findViewById(R.id.spinnerLeto);
        leto.setAdapter(adapter);
        */
        Button bPrijava = regCasa.findViewById(R.id.buttonPrijavaReg);
        bPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insertRc(userID,"",1,0);
                seznamRegCasaUporabnik = db.GetSeznamRCUporabnik(userID);
                adapterSeznamRCUpo = new RegistracijaCasaListViewAdapter(getActivity(), seznamRegCasaUporabnik);
                listView = (ListView) regCasa.findViewById(R.id.lvRegCasa);
                listView.setAdapter(adapterSeznamRCUpo);

            }
        });



        Button bOdjava = regCasa.findViewById(R.id.buttonOdjavaReg);
        bOdjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insertRc(userID,"",2,0);
                seznamRegCasaUporabnik = db.GetSeznamRCUporabnik(userID);
                adapterSeznamRCUpo = new RegistracijaCasaListViewAdapter(getActivity(), seznamRegCasaUporabnik);
                listView = (ListView) regCasa.findViewById(R.id.lvRegCasa);
                listView.setAdapter(adapterSeznamRCUpo);

            }
        });
        /*
        Spinner mesec = regCasa.findViewById(R.id.spinnerMesec);
        */

        seznamRegCasaUporabnik = db.GetSeznamRCUporabnik(userID);
        adapterSeznamRCUpo = new RegistracijaCasaListViewAdapter(getActivity(), seznamRegCasaUporabnik);
        listView = (ListView) regCasa.findViewById(R.id.lvRegCasa);
        listView.setAdapter(adapterSeznamRCUpo);

        return regCasa;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegistracijaCasaModel.class);
        // TODO: Use the ViewModel
    }

}
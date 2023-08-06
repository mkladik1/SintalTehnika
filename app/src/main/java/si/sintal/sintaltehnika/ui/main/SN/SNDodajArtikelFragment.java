package si.sintal.sintaltehnika.ui.main.SN;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SNArtikel;


public class SNDodajArtikelFragment extends Fragment {

    private SNDodajArtikelViewModel mViewModel;
    static DatabaseHandler db;
    static ArrayList<SNArtikel> dodeliArtikle;
    private SNArtikliAdapter adapterSeznamArtiklov;
    private SearchView mSearchView;
    private ListView lw;
    private String userId;
    private String tehnikId;
    private int SNId;
    private String SNDn;
    private int vrstaID;
    private String imeRegala;
    private Spinner spn;

    public static SNDodajArtikelFragment newInstance() {
        return new SNDodajArtikelFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View my_v =  inflater.inflate(R.layout.sn_dodaj_artikel, container, false);
        Intent intent= getActivity().getIntent();
        userId = intent.getStringExtra("userID");
        tehnikId = intent.getStringExtra("tehnikID");
        SNId = intent.getIntExtra("snID",0);
        SNDn = intent.getStringExtra("snDN");
        vrstaID = intent.getIntExtra("vrsta",-1);

        db = new DatabaseHandler(getContext());
        imeRegala = db.getTehnikImeRegala(tehnikId);
        lw = (ListView) my_v.findViewById(R.id.SNseznamArtikov);
        dodeliArtikle = db.GetArtikle("regal", imeRegala);
        adapterSeznamArtiklov = new SNArtikliAdapter(getActivity(), dodeliArtikle,SNId,SNDn,userId,tehnikId,vrstaID);
        lw.setAdapter(adapterSeznamArtiklov);
        adapterSeznamArtiklov.notifyDataSetChanged();
        lw.setTextFilterEnabled(true);
        mSearchView = (SearchView) my_v.findViewById(R.id.SNArtikelSearch);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterSeznamArtiklov.getFilter().filter(newText);
                return true;
            }
        });

        spn =(Spinner) my_v.findViewById(R.id.idSifrnatVrsteSkladisc);
        //DatabaseHandler db = new DatabaseHandler(getContext());
        List<String> labels = db.getVrsteArtikov();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(dataAdapter);
        spn.setSelection(7);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String text = spn.getSelectedItem().toString();
                dodeliArtikle = db.GetArtikle(text, imeRegala);
                adapterSeznamArtiklov = new SNArtikliAdapter(getActivity(), dodeliArtikle,SNId,SNDn,userId,tehnikId,vrstaID);
                lw.setAdapter(adapterSeznamArtiklov);
                adapterSeznamArtiklov.notifyDataSetChanged();
                lw.setTextFilterEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        Button bNazaj = (Button) my_v.findViewById(R.id.bDodajSNArtikelNazaj);
        bNazaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });


        return my_v;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SNDodajArtikelViewModel.class);
        // TODO: Use the ViewModel
    }

}
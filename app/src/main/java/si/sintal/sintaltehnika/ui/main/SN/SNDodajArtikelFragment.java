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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

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
    private String SNId;
    private String SNDn;
    private int vrstaID;

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
        SNId = intent.getStringExtra("snID");
        SNDn = intent.getStringExtra("snDN");
        vrstaID = intent.getIntExtra("vrsta",-1);

        db = new DatabaseHandler(getContext());
        lw = (ListView) my_v.findViewById(R.id.SNseznamArtikov);
        dodeliArtikle = db.GetArtikle();
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
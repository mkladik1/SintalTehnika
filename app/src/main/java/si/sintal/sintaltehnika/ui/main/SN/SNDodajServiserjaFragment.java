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
import si.sintal.sintaltehnika.ui.main.SN.SNDodajServiserjaFragment;
import si.sintal.sintaltehnika.ui.main.SNArtikel;
import si.sintal.sintaltehnika.ui.main.Serviser;

public class SNDodajServiserjaFragment extends Fragment {

    private SNDodajServiserjaViewModel mViewModel;
    static DatabaseHandler db;
    static ArrayList<Serviser> dodeliServiserje;
    private SNDNServiserAdaprter adapterSeznamServiserjev;
    private SearchView mSearchView;
    private ListView lw;
    private String userId;
    private String tehnikId;
    private int SNId;
    private String SNDn;

    public static SNDodajServiserjaFragment newInstance() {  return new SNDodajServiserjaFragment();  }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View my_v =  inflater.inflate(R.layout.sn_dodaj_serviserja_fragment, container, false);
        Intent intent= getActivity().getIntent();
        userId = intent.getStringExtra("userID");
        tehnikId = intent.getStringExtra("tehnikID");
        SNId = intent.getIntExtra("snID",0);
        SNDn = intent.getStringExtra("snDN");

        db = new DatabaseHandler(getContext());
        lw = (ListView) my_v.findViewById(R.id.SNseznamServiserjev);
        dodeliServiserje = db.GetServiserjeUporabnik(userId);
        adapterSeznamServiserjev = new SNDNServiserAdaprter(getActivity(), dodeliServiserje,SNId,SNDn,userId,tehnikId);
        lw.setAdapter(adapterSeznamServiserjev);
        adapterSeznamServiserjev.notifyDataSetChanged();


        Button bNazaj = (Button) my_v.findViewById(R.id.bDodajSNServiserjaNazaj);
        bNazaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });


        return my_v;//inflater.inflate(R.layout.sn_dodaj_serviserja_fragment, container, false);
    }

}
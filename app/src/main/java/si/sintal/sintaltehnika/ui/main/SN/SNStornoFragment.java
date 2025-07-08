package si.sintal.sintaltehnika.ui.main.SN;

import static android.app.PendingIntent.getActivity;

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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;

public class SNStornoFragment extends Fragment {

    private SNStornoViewModel mViewModel;
    public Integer snID;
    private String userId;

    public static SNStornoFragment newInstance() {
        return new SNStornoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SNStornoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sn_fragment_storno, container, false);
        Intent intent= getActivity().getIntent();
        snID = intent.getIntExtra("snID",0);
        userId = intent.getStringExtra("userID");
        Button bShrani = (Button) v.findViewById(R.id.bPotrdiSNStorno);
        bShrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(v.getContext());
                //db.updateSNStatusAkt(String.valueOf(snID),"Z");
                String opis_pos = "";
                int izbrano = 0;
                CheckBox cb = (CheckBox) getActivity().findViewById(R.id.cbPrenosDelujeSNStorno);
                if (cb.isChecked())
                {
                    opis_pos = cb.getText().toString();
                    izbrano = izbrano + 1;
                }
                cb = (CheckBox)  getActivity().findViewById(R.id.cbTauSNStorno);
                if (cb.isChecked())
                {
                    opis_pos = cb.getText().toString();
                    izbrano = izbrano + 1;
                }
                cb = (CheckBox)  getActivity().findViewById(R.id.cbDrugoSNStorno);
                if (cb.isChecked())
                {
                    TextView tw = (TextView)  getActivity().findViewById(R.id.etDrugoSNStorno);
                    opis_pos = tw.getText().toString();
                    izbrano = izbrano + 1;
                }
                if  ( (izbrano == 0) || (izbrano > 1) || (opis_pos.length() == 0) )
                {
                    Toast.makeText( getActivity(), "Označiti morate samo eno polje!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    String datum_iz;
                    datum_iz = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String tehnikNaziv = db.getTehnikNaziv(Integer.parseInt(userId));
                    db.updateSNDescriptionAkt(String.valueOf(snID),opis_pos,datum_iz, tehnikNaziv);
                    getActivity().onBackPressed();
                }
            }});
        Button bPreklici = (Button) v.findViewById(R.id.bPrekliciSNStorno);
        bPreklici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }});




        return v;
    }

}
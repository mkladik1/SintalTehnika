package si.sintal.sintaltehnika.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import si.sintal.sintaltehnika.R;

public class DialogPodatkiOSNFragment extends Fragment {

    private DialogPodatkiOSN mViewModel;

    public static DialogPodatkiOSNFragment newInstance() {
        return new DialogPodatkiOSNFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view_podatki_osn = inflater.inflate(R.layout.dialog_podatki_osn, container, false);
        Intent intent= getActivity().getIntent();
        String stSNja = intent.getStringExtra("stSNja");
        String opisSNja = intent.getStringExtra("opisSNja");
        String vodjaSNja = intent.getStringExtra("vodjaSNja");
        String datumSNja = intent.getStringExtra("datumSNja");
        String narocnikSNja = intent.getStringExtra("narocnikSNja");
        String odgOSeba =   intent.getStringExtra("odgOsebaSNja");

        TextView tvSN = (TextView) view_podatki_osn.findViewById(R.id.tvDialogStSN);
        tvSN.setText(stSNja);
        TextView tvSNDat = (TextView) view_podatki_osn.findViewById(R.id.tvDialogDatumSn);
        tvSNDat.setText(datumSNja);
        TextView tvServiser = (TextView) view_podatki_osn.findViewById(R.id.tvDialogServiser);
        tvServiser.setText(vodjaSNja);
        TextView tvSNNar = (TextView) view_podatki_osn.findViewById(R.id.tvDialogNarocnik);
        tvSNNar.setText(narocnikSNja);
        TextView tvSNOpis = (TextView) view_podatki_osn.findViewById(R.id.tvDialogOpis);
        tvSNOpis.setText(opisSNja);
        TextView tvVodjaSNja = (TextView) view_podatki_osn.findViewById(R.id.tvDialogOdgOseba);
        tvVodjaSNja.setText(odgOSeba);

        Button bDialogExit = (Button) view_podatki_osn.findViewById(R.id.bDialogExit);
        bDialogExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        return view_podatki_osn;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DialogPodatkiOSN.class);
        // TODO: Use the ViewModel
    }

}
package si.sintal.sintaltehnika.ui.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;

public class DialogPodatkiOSNFragment extends Fragment {

    private DialogPodatkiOSN mViewModel;
    private int tehnikID;
    private int userID;

    public static DialogPodatkiOSNFragment newInstance() {
        return new DialogPodatkiOSNFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view_podatki_osn = inflater.inflate(R.layout.dialog_podatki_osn, container, false);
        Intent intent= getActivity().getIntent();
        DatabaseHandler db = new DatabaseHandler(getContext());
        tehnikID= intent.getIntExtra("userID",0);
        userID = intent.getIntExtra("tehnikID",0);
        String stSNja = intent.getStringExtra("stSNja");
        int idSNja = intent.getIntExtra("idSNja", 0);
        String opisSNja = intent.getStringExtra("opisSNja");
        String vodjaSNja = intent.getStringExtra("vodjaSNja");
        String datumSNja = intent.getStringExtra("datumSNja");
        String narocnikSNja = intent.getStringExtra("narocnikSNja");
        String odgOSeba =   intent.getStringExtra("odgOsebaSNja");
        ServisniNalog sn = new ServisniNalog();
        sn = db.vrniSN(idSNja);

        TextView tvSN = (TextView) view_podatki_osn.findViewById(R.id.tvDialogStSN);
        tvSN.setText(stSNja);
        tvSN.setText(sn.getDelovniNalog().toString());
        TextView tvSNDat = (TextView) view_podatki_osn.findViewById(R.id.tvDialogDatumSn);
        tvSNDat.setText(datumSNja);
        tvSNDat.setText(sn.getDatumZacetek());
        TextView tvServiser = (TextView) view_podatki_osn.findViewById(R.id.tvDialogServiser);
        tvServiser.setText(vodjaSNja);
        tvServiser.setText(sn.getVodjaNaloga());
        TextView tvSNNar = (TextView) view_podatki_osn.findViewById(R.id.tvDialogNarocnik);
        tvSNNar.setText(narocnikSNja);
        tvSNNar.setText(sn.getNarocnikNaziv());
        TextView tvSNOpis = (TextView) view_podatki_osn.findViewById(R.id.tvDialogOpis);
        tvSNOpis.setText(opisSNja);
        tvSNOpis.setText(sn.getOpis());
        TextView tvVodjaSNja = (TextView) view_podatki_osn.findViewById(R.id.tvDialogOdgOseba);
        tvVodjaSNja.setText(odgOSeba);
        tvVodjaSNja.setText(sn.getOdgovornaOseba());
        TextView tvVodjaSNja1 = (TextView) view_podatki_osn.findViewById(R.id.tvDialogDatumZakljucka);
        tvVodjaSNja1.setText(sn.getDatumKonec());

        TextView tvVodjaSNja2 = (TextView) view_podatki_osn.findViewById(R.id.tvDialogDatumDodelitve);
        tvVodjaSNja2.setText(sn.getDatumIzvedbe());

        TextView tvVodjaSNja3 = (TextView) view_podatki_osn.findViewById(R.id.tvDialogDatumPodpisa);
        tvVodjaSNja3.setText(sn.getDatumPodpisa());

        TextView tvOpisOkvare = (TextView) view_podatki_osn.findViewById(R.id.tvDialogNatOpisOkvare);
        tvOpisOkvare.setText(sn.getOpisOkvare());

        TextView tvOpisOkvare2 = (TextView) view_podatki_osn.findViewById(R.id.tvDialogNatOpisPostopka);
        tvOpisOkvare2.setText(sn.getOpisPostopka());

        Button bDialogExit = (Button) view_podatki_osn.findViewById(R.id.bDialogExit);
        bDialogExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        View v1 = view_podatki_osn.findViewById(R.id.textView31);
        // Get params:
        LinearLayout.LayoutParams loparams1 = (LinearLayout.LayoutParams) v1.getLayoutParams();

        View v2 = view_podatki_osn.findViewById(R.id.textView18);
        LinearLayout.LayoutParams loparams2 = (LinearLayout.LayoutParams) v2.getLayoutParams();

        View v3 = view_podatki_osn.findViewById(R.id.textView20);
        LinearLayout.LayoutParams loparams3 = (LinearLayout.LayoutParams) v3.getLayoutParams();

        View v4 = view_podatki_osn.findViewById(R.id.textView26);
        LinearLayout.LayoutParams loparams4 = (LinearLayout.LayoutParams) v4.getLayoutParams();
        //loparams.weight = 1f;
        TableLayout table = view_podatki_osn.findViewById(R.id.tableSNArtikli);
        ArrayList<SNArtikel> artikliDN;
        artikliDN = db.GetSeznamArtikliIzpisDNSNUporabnik(tehnikID,userID,1,idSNja);
        for (int i = 2; i < artikliDN.size()+1; i++) {
            SNArtikel art = artikliDN.get(i-2);
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            TextView textView = new TextView(getContext());
            textView.setPadding(3,3,3,3);
            loparams1.weight = 0.05f;
            textView.setLayoutParams(loparams1);
            //textView.setWidth(200);
            textView.setBackground(Drawable.createFromPath("@xml/borders"));
            textView.setText(art.getid());
            TextView textView2 = new TextView(getContext());
            loparams2.weight = 0.8f;
            textView2.setLayoutParams(loparams2);
            textView2.setPadding(3,3,3,3);
            textView2.setText(art.getnaziv());
            TextView textView3 = new TextView(getContext());
            loparams3.weight = 0.05f;
            textView3.setLayoutParams(loparams3);
            textView3.setPadding(3,3,3,3);
            //textView3.setWidth(200);
            textView3.setText(String.valueOf(art.getKolicina()));
            TextView textView4 = new TextView(getContext());
            loparams4.weight = 0.1f;
            textView4.setLayoutParams(loparams4);
            //textView4.setWidth(350);
            textView4.setPadding(3,3,3,3);
            if (art.getZamenjanNov() == 1)
            {
                textView4.setText("N");
            }
            else if (art.getZamenjanNov() == 2)
            {
                textView4.setText("Z");
            }
            else
            {
                textView4.setText("");
            }

            row.addView(textView);
            row.addView(textView2);
            row.addView(textView3);
            row.addView(textView4);
            table.addView(row, i);
        }




        return view_podatki_osn;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DialogPodatkiOSN.class);
        // TODO: Use the ViewModel
    }

}
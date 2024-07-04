package si.sintal.sintaltehnika.ui.main.VZ;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZ;

public class obrazecVZDNFragment extends Fragment {

    private ObrazecVZDNViewModel mViewModel;
    private String tehnikID;
    private String userID;
    private int vzDNID;
    Dialog dialog;
    private String vzDelNalST;
    int serverResponseCode = 0;
    DelovniNalogVZ vzDN = new DelovniNalogVZ();

    public static obrazecVZDNFragment newInstance() {
        return new obrazecVZDNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.obrazec_vz_dn_fragment, container, false);
        DatabaseHandler db = new DatabaseHandler(getContext());
        Intent intent= getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        tehnikID = intent.getStringExtra("tehnikID");
        vzDNID = VZPagerAdapter.getParameters();

        //TextView tvDN = TextView
        vzDN = db.vrniVZDN(vzDNID);

        //vzDelNalST = vzDN.getDelovniNalogVZ();
        //String tehnik = sn.getVodjaNaloga();
        //tehnikID = db.getTehnikId(tehnik);



        return v;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ObrazecVZDNViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onResume() {
        super.onResume();
        dialog = new Dialog(getActivity());
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);

        DatabaseHandler db = new DatabaseHandler(getContext());
        vzDNID = VZPagerAdapter.getParameters();

        //TextView tvDN = TextView
        vzDN = db.vrniVZDN(vzDNID);
        //tehnikID = intent.getStringExtra("tehnikID");
        //snDN = sn.getDelovniNalog();
        //String tehnik = sn.getVodjaNaloga();
        //tehnikID = db.getTehnikId(tehnik);

        Button bSNShrani = (Button) getView().findViewById(R.id.bVZDNShrani);
        Button bPdf = (Button) getView().findViewById(R.id.bVZDNZakljuci);
        Button bSign = (Button) getView().findViewById(R.id.bVZDNPodisStranka);
        //Button bDodajVgrMat = (Button) getView().findViewById(R.id.bSNDodajVMat);
        //Button bDodajNovMat = (Button) getView().findViewById(R.id.bSNDodajZMat);
        //Button bSNShraniSpremembe = (Button) getView().findViewById(R.id.bSNShraniSNPo);

        String tehnikID = "";//this.tehnikID;
        /*
        int rgIndex = vzDN.getTipNarocila();
        RadioGroup rg1 = (RadioGroup) getView().findViewById(R.id.rbSNTipNarocila);
        if (sn.getTipNarocila() > -1)
        {
            if (sn.getTipNarocila() == 3)
            {
                ((RadioButton)rg1.getChildAt(0)).setChecked(true);
            }
            else
            {
                ((RadioButton)rg1.getChildAt(rgIndex)).setChecked(true);
                //((RadioButton)rg1.getChildAt(0)).setChecked(false);
            }
        }
        else
        {
            ((RadioButton)rg1.getChildAt(rgIndex)).setChecked(true);
            //((RadioButton)rg1.getChildAt(0)).setChecked(false);
        }
         */

        /*
        RadioGroup rg2 = (RadioGroup) getView().findViewById(R.id.rgSNVzdrzevanje);
        int tipVzdrzevanja = sn.getTipVzdzevanja();
        if (tipVzdrzevanja > -1)
        {
            if (tipVzdrzevanja == 1) {
                ((RadioButton) rg2.getChildAt(0)).setChecked(true); //redno
            }
            else if (tipVzdrzevanja == 2) {
                ((RadioButton) rg2.getChildAt(1)).setChecked(true); //naročilnica po servisu
            }
            else if (tipVzdrzevanja == 3) {
                ((RadioButton) rg2.getChildAt(2)).setChecked(true); // naročilnica pred servisom
            }
            else if (tipVzdrzevanja == 9) {
                ((RadioButton) rg2.getChildAt(3)).setChecked(true); //
            }
            else  {
                ((RadioButton)rg2.getChildAt(0)).setChecked(false);
                ((RadioButton)rg2.getChildAt(1)).setChecked(false);
                ((RadioButton)rg2.getChildAt(2)).setChecked(false);
                ((RadioButton)rg2.getChildAt(3)).setChecked(false);
            }
        }
        else
        {
            ((RadioButton)rg2.getChildAt(0)).setChecked(false);
            ((RadioButton)rg2.getChildAt(1)).setChecked(false);
            ((RadioButton)rg2.getChildAt(2)).setChecked(false);
            ((RadioButton)rg2.getChildAt(3)).setChecked(false);
        }
         */
        //tehnikID = db.getTehnikId(sn.getVodjaNaloga());
        //this.tehnikID = tehnikID;
        TextView test = (TextView) getView().findViewById(R.id.idObrazecVZDN);
        test.setText(Integer.toString(vzDN.getid()));
        //test = (TextView) getView().findViewById(R.id.idObrazecSNTehnikID);
        //test.setText(tehnikID);
        test = (TextView) getView().findViewById(R.id.tvVZDNNarocnikNaziv);
        test.setText(vzDN.getNaziv_servisa());
        test = (TextView) getView().findViewById(R.id.tvStevilkaVZDN);
        test.setText(vzDN.getDelovniNalog());
        test = (TextView) getView().findViewById(R.id.tvVZDNNarocnikNaslov);
        test.setText(vzDN.getIme()+", "+vzDN.getNaslov());

        //test = (TextView) getView().findViewById(R.id.tvSNImeSektorja);
        //test.setText(sn.getNarocnikSektor());
        //test = (TextView) getView().findViewById(R.id.tvSNAdSekt);
        //test.setText(sn.getKodaObjekta());
        test = (TextView) getView().findViewById(R.id.tvVZDNImeSekNaslov);
        test.setText(vzDN.getNaslov());

        test = (TextView) getView().findViewById(R.id.tvVZDNKontaktnaOseba);
        test.setText(vzDN.getKontaktna_oseba());
        test = (TextView) getView().findViewById(R.id.tvVZDNTelefon);
        test.setText(vzDN.getTelefon());


        test = (TextView) getView().findViewById(R.id.tvVZDNPripadnost);
        test.setText("");

        CheckBox cb;
        cb = (CheckBox) getView().findViewById(R.id.cbPozar);
        if (vzDN.getSis_pozar() == 0)
        {
            cb.setChecked(true);
        }
        else { cb.setChecked(false);}

        cb = (CheckBox) getView().findViewById(R.id.cbPlin);
        if (vzDN.getSis_co() == 0)
        {
            cb.setChecked(true);
        }
        else { cb.setChecked(false);}

        cb = (CheckBox) getView().findViewById(R.id.cbVlom);
        if (vzDN.getSis_vlom() == 0)
        {
            cb.setChecked(true);
        }
        else { cb.setChecked(false);}

        cb = (CheckBox) getView().findViewById(R.id.cbVideo);
        if (vzDN.getSis_video() == 0)
        {
            cb.setChecked(true);
        }
        else { cb.setChecked(false);}


        cb = (CheckBox) getView().findViewById(R.id.cbPristop);
        if (vzDN.getSis_pristopna() == 0)
        {
            cb.setChecked(true);
        }
        else { cb.setChecked(false);}

        cb = (CheckBox) getView().findViewById(R.id.cbDim);
        if (vzDN.getSis_dimni_bankovci() == 0)
        {
            cb.setChecked(true);
        }
        else { cb.setChecked(false);}

        int periodika = vzDN.getPeriodikaDni();
        int mesec = periodika/30;
        Spinner per = getView().findViewById(R.id.spinnObdobje);
        if (mesec == 2)
        {
            per.setSelection(0);
        }
        else if (mesec == 3)
        {
            per.setSelection(1);
        }
        else if (mesec == 4)
        {
            per.setSelection(2);
        }
        else if (mesec == 5)
        {
            per.setSelection(3);
        }
        else if (mesec == 6)
        {
            per.setSelection(4);
        }
        else if (mesec == 12)
        {
            per.setSelection(5);
        }

        test = (TextView) getView().findViewById(R.id.etVZDNDatumIzvedbe);
        test.setText(vzDN.getDATUM_IZVEDBE());
        String datum = vzDN.getDATUM_IZVEDBE();
        if ( (datum == null)
                || (datum.equals("")) )
        {
            //String datum = "";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDateTime now = LocalDateTime.now();
            datum = dtf.format(now);
            test.setText(datum);
        }



        test = (TextView) getView().findViewById(R.id.etVZDNUrePrevoz);
        if (vzDN.getUrePrevoz() == 0.0)
        {
            test.setText("");
        }
        else
        {
            test.setText(Double.toString(vzDN.getUrePrevoz()));
        }
        test = (TextView) getView().findViewById(R.id.etVZDNUreDelo);
        if (vzDN.getUreDelo() == 0.0)
        {
            test.setText("");
        }
        else {
            test.setText(Double.toString(vzDN.getUreDelo()));
        }
        test = (TextView) getView().findViewById(R.id.etVZDNStKm);
        if (vzDN.getStKm() == 0.0)
        {
            test.setText("");
        }
        else {
            test.setText(Double.toString(vzDN.getStKm()));
        }


        int statusSN = vzDN.getStatus();
        /*
        if (statusSN < 0)
        {

        }
        else if (statusSN.equals("Z")){
            bSign.setEnabled(false);
            bSNShrani.setEnabled(false);
            bPdf.setEnabled(false);
            bDodajVgrMat.setEnabled(false);
            bDodajNovMat.setEnabled(false);
            bSNShraniSpremembe.setEnabled(true);
        }
        else
        {
            bSign.setEnabled(true);
            bSNShrani.setEnabled(true);
            bPdf.setEnabled(true);
            bDodajVgrMat.setEnabled(true);
            bDodajNovMat.setEnabled(true);
            bSNShraniSpremembe.setEnabled(false);
        }
         */

        //methodA(); // this is called ...
    }


}
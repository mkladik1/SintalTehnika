package si.sintal.sintaltehnika.ui.main.VZ;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import si.sintal.sintaltehnika.BuildConfig;
import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZ;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZPeriodika;

public class obrazecVZDNFragment extends Fragment {

    private ObrazecVZDNViewModel mViewModel;
    private int tehnikID;
    private String userID;
    private int vzDNID;
    private int perPre;
    private String mes_obr;
    Dialog dialog;
    private String vzDelNalST;
    int serverResponseCode = 0;
    DelovniNalogVZ vzDN = new DelovniNalogVZ();
    Button btn_get_sign, mClear, mGetSign, mCancel;
    File file;
    //Dialog dialog;
    LinearLayout mContent;
    View view;
    obrazecVZDNFragment.signature mSignature;
    Bitmap bitmap;
    DelovniNalogVZPeriodika vzDNPer = new DelovniNalogVZPeriodika();

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
        //tehnikID = intent.getStringExtra("tehnikID");
        perPre = intent.getIntExtra("per_prenos",0);
        tehnikID = VZPagerAdapter.getTehnikID();
        perPre = VZPagerAdapter.getPer_prenos();
        vzDNID = VZPagerAdapter.getParameters();
        mes_obr = VZPagerAdapter.getMes_obr();
        //TextView tvDN = TextView
        vzDN = db.vrniVZDN(vzDNID);
        vzDNPer = db.vrniVZDNPre(vzDNID, perPre,mes_obr);
        Button bShowVZDN = (Button) v.findViewById(R.id.buttonVZDNPrikazi);
        bShowVZDN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //convertToPdf();
                            int status = vzDNPer.getStatus();
                            if (status == 0 )
                            {
                                createPdf();
                                openPdf();
                            }
                            else {
                                String path = getContext().getFilesDir().getPath();
                                File dir = new File(path);
                                File file = new File(dir, vzDNID+".pdf");
                                if(!file.exists())
                                {
                                    createPdf();
                                }

                                openPdf();
                            }
                            //db.updateSNStatusAkt(snID,"Z");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        Button bSign = (Button) v.findViewById(R.id.bVZDNPodisStranka);
        bSign.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog_action();
                    }
                }
        );


        return v;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ObrazecVZDNViewModel.class);
        // TODO: Use the ViewModel
    }

    public void openPdf() throws Exception
    {
        File file = new File(getContext().getFilesDir()+"/"+vzDNID+".pdf");
        //Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/sample.pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);


            //Uri uri = Uri.fromFile(file);

            Uri uri = FileProvider.getUriForFile(getContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);

            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(getContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }


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
        perPre = VZPagerAdapter.getPer_prenos();
        mes_obr = VZPagerAdapter.getMes_obr();


        //TextView tvDN = TextView
        vzDN = db.vrniVZDN(vzDNID);
        vzDNPer = db.vrniVZDNPre(vzDNID,perPre,mes_obr);




        Button bSNShrani = (Button) getView().findViewById(R.id.bVZDNShrani);
        Button bPdf = (Button) getView().findViewById(R.id.bVZDNZakljuci);
        Button bSign = (Button) getView().findViewById(R.id.bVZDNPodisStranka);

        if (perPre == 1)
        {
            bSNShrani.setEnabled(false);
            bPdf.setEnabled(false);
            bSign.setEnabled(false);

        }
        else {
            bSNShrani.setEnabled(true);
            bPdf.setEnabled(true);
            bSign.setEnabled(true);

        }

        String tehnikID = "";//this.tehnikID;

        //tehnikID = db.getTehnikId(sn.getVodjaNaloga());
        //this.tehnikID = tehnikID;
        TextView test = (TextView) getView().findViewById(R.id.idObrazecVZDN);
        test.setText(Integer.toString(vzDN.getid()));
        test = (TextView) getView().findViewById(R.id.tvVZDNNarocnikNaziv);
        test.setText(vzDN.getNarocnik());
        test = (TextView) getView().findViewById(R.id.tvStevilkaVZDN);
        test.setText(vzDN.getDelovniNalog());
        test = (TextView) getView().findViewById(R.id.tvVZDNNarocnikNaslov);
        test.setText(vzDN.getNarocnikNaslov());
        test = (TextView) getView().findViewById(R.id.tvVZDNImeSektorja);
        test.setText(vzDN.getObjekt());
        test = (TextView) getView().findViewById(R.id.tvVZDNImeSekNaslov);
        test.setText(vzDN.getObjektNaslov());

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


        Button bVZShrani = (Button) getView().findViewById(R.id.bVZDNShrani);
        bSNShrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView test = (TextView) getView().findViewById(R.id.idObrazecSNTehnikID);
                //tehnikID = test.getText().toString();


                test = (TextView) getView().findViewById(R.id.tvStevilkaVZDN);
                String vzDN = test.getText().toString();
                RadioGroup rg1 = (RadioGroup) getView().findViewById(R.id.rbVZDNTipNarocila);
                int tipNarocila = rg1.indexOfChild(getView().findViewById(rg1.getCheckedRadioButtonId()));
                //rg1.getCheckedRadioButtonId();
                //RadioGroup rg2 = (RadioGroup) getView().findViewById(R.id.rgSNVzdrzevanje);
                //int tipVzdrzevanja = rg2.indexOfChild(getView().findViewById(rg2.getCheckedRadioButtonId()));

                CheckBox cb1 = (CheckBox) getView().findViewById(R.id.cbRedno);
                String redno = String.valueOf('0');
                if (cb1.isChecked()){ redno = String.valueOf('1');}

                cb1 = (CheckBox) getView().findViewById(R.id.cbIzredno);
                String izredno = String.valueOf('0');
                if (cb1.isChecked()){ izredno = String.valueOf('1');}

                //test (TextView) getView().findViewById(R.id.tvVZDNNarocnikNaziv);//
                //String narocnikNaziv = test.getText().toString();

                //test = (TextView) getView().findViewById(R.id.tv);
                String tvVZDNDatumDodelitve = "";test.getText().toString();
                //tvSNDatumMontaze = vrniDatum(tvSNDatumMontaze);//

                test = (TextView) getView().findViewById(R.id.etVZDNDatumIzvedbe);
                String tvVZDNDatumIzvedbe = test.getText().toString();
                tvVZDNDatumIzvedbe = vrniDatum(tvVZDNDatumIzvedbe);


                test = (TextView) getView().findViewById(R.id.etOpombeVZD);
                String tvVZDBOpombe = test.getText().toString();

                //test = (TextView) getView().findViewById(R.id.tvSNGarancija);
                //String tvSNGarancija = test.getText().toString();
                //test = (TextView) getView().findViewById(R.id.tvSNNapaka);
                //String tvSNNapaka = test.getText().toString();
                test = (TextView) getView().findViewById(R.id.etVZDNUrePrevoz);
                String etSNUrePrevoz = test.getText().toString();
                etSNUrePrevoz = etSNUrePrevoz.replace(',','.');
                test = (TextView) getView().findViewById(R.id.etVZDNUreDelo);
                String etSNUreDelo = test.getText().toString();
                etSNUreDelo = etSNUreDelo.replace(',','.');
                test = (TextView) getView().findViewById(R.id.etVZDNStKm);
                String etSNStKm = test.getText().toString();
                etSNStKm = etSNStKm.replace(',','.');
                //test = (TextView) getView().findViewById(R.id.tvSNNapaka2);
                //String tvSNNapaka2 = test.getText().toString();

                 cb1 = (CheckBox) getView().findViewById(R.id.cbPozar);
                String sisPozar = String.valueOf('0');
                if (cb1.isChecked()){ sisPozar = String.valueOf('1');}

                cb1 = (CheckBox) getView().findViewById(R.id.cbPlin);
                String sisPlin = String.valueOf('0');
                if (cb1.isChecked()){ sisPlin = String.valueOf('1');}

                cb1 = (CheckBox) getView().findViewById(R.id.cbVlom);
                String sisVlom = String.valueOf('0');
                if (cb1.isChecked()){ sisVlom = String.valueOf('1');}

                cb1 = (CheckBox) getView().findViewById(R.id.cbVideo);
                String sisVideo = String.valueOf('0');
                if (cb1.isChecked()){ sisVideo = String.valueOf('1');}

                cb1 = (CheckBox) getView().findViewById(R.id.cbPristop);
                String sisPristop = String.valueOf('0');
                if (cb1.isChecked()){ sisPristop = String.valueOf('1');}

                cb1 = (CheckBox) getView().findViewById(R.id.cbDim);
                String sisDim = String.valueOf('0');
                if (cb1.isChecked()){ sisDim = String.valueOf('1');}

                Spinner snObdobje = (Spinner) getView().findViewById(R.id.spinnObdobje);
                String spVZDN = snObdobje.getSelectedItem().toString();



                EditText etVZ = (EditText) getView().findViewById(R.id.etTipElementov);
                String tipEl = etVZ.getText().toString();

                etVZ = (EditText) getView().findViewById(R.id.etDatumPrejsnje);
                String datumPrejsne = etVZ.getText().toString();

                etVZ = (EditText) getView().findViewById(R.id.etDatumNaslednjega);
                String datumNasl = etVZ.getText().toString();

                etVZ = (EditText) getView().findViewById(R.id.etKontrolorLinije);
                String kontrolorLinije = etVZ.getText().toString();

                etVZ = (EditText) getView().findViewById(R.id.etBaterije);
                String baterije = etVZ.getText().toString();

                etVZ = (EditText) getView().findViewById(R.id.etNacinPrenosa);
                String nacinP = etVZ.getText().toString();

                etVZ = (EditText) getView().findViewById(R.id.etPrenosInstitucija);
                String institucija = etVZ.getText().toString();

                cb1 = (CheckBox) getView().findViewById(R.id.cbInstalacija);
                String pr1 = "0";
                if (cb1.isChecked())
                { pr1 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbNapetostAku);
                String pr2 = "0";
                if (cb1.isChecked())
                { pr2 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeSiren);
                String pr3 = "0";
                if (cb1.isChecked())
                { pr3 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjePosameznihLinij);
                String pr4 = "0";
                if (cb1.isChecked())
                { pr4 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeJavljalnik);
                String pr5 = "0";
                if (cb1.isChecked())
                { pr5 = "1";}

                cb1 = (CheckBox) getView().findViewById(R.id.cbDelElZaPlin);
                String pr6 = "0";
                if (cb1.isChecked())
                { pr6 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbPrenosSigNAVNC);
                String pr7 = "0";
                if (cb1.isChecked())
                { pr7 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbPrenosGasilci);
                String pr8 = "0";
                if (cb1.isChecked())
                { pr8 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbCiscanjeElPJ);
                String pr9 = "0";
                if (cb1.isChecked())
                { pr9 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeElementovProtivlom);
                String pr10 = "0";
                if (cb1.isChecked())
                { pr10 = "1";}

                cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjePanikTipk);
                String pr11 = "0";
                if (cb1.isChecked())
                { pr11 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbCiscenjeElProtivlom);
                String pr12 = "0";
                if (cb1.isChecked())
                { pr12 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeSistDimBank);
                String pr13 = "0";
                if (cb1.isChecked())
                { pr13 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeVideoSist);
                String pr14 = "0";
                if (cb1.isChecked())
                { pr14 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbPregledArhivaVS);
                String pr15 = "0";
                if (cb1.isChecked())
                { pr15 = "1";}

                cb1 = (CheckBox) getView().findViewById(R.id.cbCiscanjeELVS);
                String pr16 = "0";
                if (cb1.isChecked())
                { pr16 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeKP);
                String pr17 = "0";
                if (cb1.isChecked())
                { pr17 = "1";}
                cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeRDC);
                String pr18 = "0";
                if (cb1.isChecked())
                { pr18 = "1";}





                if ((isValidFormat("yyyy-MM-dd",tvVZDNDatumIzvedbe) == true)
                        || (tvVZDNDatumIzvedbe.equals("")))
                {
                    String mac = getMacAddress(getContext());
                    String tehnik = tehnikID;
                    Date todayDate = Calendar.getInstance().getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                    String todayString = formatter.format(todayDate);
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    db.insertUpdateVZDNPeriodika(String.valueOf(vzDNID), vzDN,"A",tvVZDNDatumDodelitve, tvVZDNDatumIzvedbe,"0","",tvVZDBOpombe,etSNUrePrevoz,etSNUreDelo,etSNStKm,
                            "","","", String.valueOf(tipNarocila),
                            sisPozar,sisVlom,sisVideo,sisPlin,sisPristop,sisDim,"0", spVZDN, redno, izredno,
                            tipEl, datumPrejsne, datumNasl, kontrolorLinije, baterije, nacinP, institucija,
                            pr1,pr2,pr3,pr4,pr5,pr6,pr7,pr8,pr9,pr10,pr11,pr12,pr13,pr14,pr15,pr16,pr17,pr18,
                            "","",tvVZDBOpombe,mac,tehnik,todayString
                    );

                    Toast.makeText(getView().getContext(),"Podatki o servisnem nalogu uspešno shranjeni",Toast.LENGTH_LONG).show();
                }
                else
                {

                    Toast.makeText(getView().getContext(),"Napačen format datuma montaže!",Toast.LENGTH_LONG).show();
                    //DatabaseHandler db = new DatabaseHandler(getContext());
                    //db.updateSNDN(snID, tipNarocila, tipVzdrzevanja, "", tvSNGarancija, tvSNNapaka, etSNUrePrevoz, etSNUreDelo, etSNStKm, tvSNNapaka2);
                }

            }
        });


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
        DelovniNalogVZPeriodika dnvp = db.vrniVZDNPre(vzDNID, perPre,mes_obr);
        if (dnvp.getid() != 0)
        {
            azurirajPodatke(dnvp);

        }
    }

    private static void RemoveBorder(Table table)
    {
        for (IElement iElement : table.getChildren()) {
            ((Cell)iElement).setBorder(Border.NO_BORDER);
        }
    }

    public void createPdf() throws Exception
    {
        try {
            String path = getContext().getFilesDir().getPath();
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


            File file = new File(dir, vzDNID+".pdf");
            if(!file.exists())
            {
                //file.delete();
                file.createNewFile();
            }
            else if(file.exists())
            {
                file.delete();
                file.createNewFile();
            }

            DatabaseHandler db = new DatabaseHandler(getContext());
            DelovniNalogVZ vzDNPdf = new DelovniNalogVZ();
            vzDNPdf = db.vrniVZDN(vzDNID);

            DelovniNalogVZPeriodika vzDNPdfPer = new DelovniNalogVZPeriodika();
            vzDNPdfPer = db.vrniVZDNPre(vzDNID,perPre,mes_obr);

            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter pdfWriter = new PdfWriter(fOut);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);
            document.setTopMargin(20);
            document.setBottomMargin(20);
            document.setLeftMargin(30);
            document.setRightMargin(30);

            String REGULAR = "res/font/arial.ttf";
            PdfFont font = PdfFontFactory.createFont(REGULAR);

            Text tNaslov = new Text("SERVISNI NALOG");
            tNaslov.setFont(font);
            tNaslov.setFontSize(16);

            Paragraph p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.CENTER);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            document.add(p);

            Table podNaslov = new Table(UnitValue.createPercentArray(new float[]{3,5,3}));
            podNaslov.setWidth(UnitValue.createPercentValue(100));
            podNaslov.setFixedLayout();
            podNaslov.setFont(font);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("").setFontSize(12).setItalic()));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("za preizkus sistemov tehničnega varovanja").setFontSize(10).setItalic()).setTextAlignment(TextAlignment.CENTER));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("številka: "+ vzDN.getDelovniNalog())).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));//.setBorder(Border.NO_BORDER);
            RemoveBorder(podNaslov);
            document.add(podNaslov);

            Paragraph pNarocnik = new Paragraph();
            pNarocnik.setTextAlignment(TextAlignment.LEFT);
            pNarocnik.setHorizontalAlignment(HorizontalAlignment.LEFT);
            pNarocnik.setVerticalAlignment(VerticalAlignment.MIDDLE);
            pNarocnik.setFontSize(10);
            pNarocnik.add("1. opis objekta");
            document.add(pNarocnik);

            String sektorKraj = "";
            String sektorNaslov = vzDNPdf.getNaslov().split(",",2)[0];

            Table table = new Table(UnitValue.createPercentArray(new float[]{5,6,4,6}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setFixedLayout();
            table.setFont(font);
            table.addCell(new Cell(1,1).add(new Paragraph("NAROČNIK")));//.setBorder(Border.NO_BORDER);
            //table.addCell(new Cell(1,3).add(new Paragraph(vzDNPdf.getNaziv_servisa())));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,3).add(new Paragraph(vzDNPdf.getNarocnik())));//.setBorder(Border.NO_BORDER);

            table.addCell(new Cell(1,1).add(new Paragraph("ULICA (naročnik)")));//.setBorder(Border.NO_BORDER);
            //table.addCell(new Cell().add(new Paragraph(vzDNPdf.getNaslov())));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,3).add(new Paragraph(vzDNPdf.getNarocnikNaslov())));//.setBorder(Border.NO_BORDER);
            //table.addCell(new Cell().add(new Paragraph("KRAJ")));//.setBorder(Border.NO_BORDER);
            //table.addCell(new Cell().add(new Paragraph("")));//.setBorder(Border.NO_BORDER);

            table.addCell(new Cell().add(new Paragraph("IME OBJEKTA")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(vzDNPdf.getObjekt())));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("AD 1. SEKTOR")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("")));//.setBorder(Border.NO_BORDER);

            table.addCell(new Cell(1,1).add(new Paragraph("ULICA (objekt)")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,3).add(new Paragraph(vzDNPdf.getObjektNaslov())));//.setBorder(Border.NO_BORDER);
            //table.addCell(new Cell().add(new Paragraph("KRAJ")));//.setBorder(Border.NO_BORDER);
            //table.addCell(new Cell().add(new Paragraph(sektorKraj)));//.setBorder(Border.NO_BORDER);

            table.addCell(new Cell().add(new Paragraph("KONTAKTNA OSEBA")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(vzDNPdf.getKontaktna_oseba())));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("TELEFON")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("")));//.setBorder(Border.NO_BORDER);


            //table.addCell(new Cell().add(new Paragraph("PRIPADNOST")));//.setBorder(Border.NO_BORDER);
            //table.addCell(new Cell().add(new Paragraph("")));//.setBorder(Border.NO_BORDER);
            //table.addCell(new Cell().add(new Paragraph("PODIZVAJALEC")));//.setBorder(Border.NO_BORDER);
            //table.addCell(new Cell().add(new Paragraph("")));//.setBorder(Border.NO_BORDER);

            document.add(table);

            Paragraph pNarocnikServisa = new Paragraph();
            pNarocnikServisa.setTextAlignment(TextAlignment.LEFT);
            pNarocnikServisa.setHorizontalAlignment(HorizontalAlignment.LEFT);
            pNarocnikServisa.setVerticalAlignment(VerticalAlignment.MIDDLE);
            pNarocnikServisa.setFontSize(10);
            pNarocnikServisa.setFont(font);
            pNarocnikServisa.add("2. naročnik servisa");
            document.add(pNarocnikServisa);

            Table table2 = new Table(UnitValue.createPercentArray(new float[]{5,6,4,6}));
            table2.setWidth(UnitValue.createPercentValue(100));
            table2.setFixedLayout();
            table2.setFont(font);
            table2.addCell(new Cell(1,1).add(new Paragraph("SERVIS NAROČIL")));//.setBorder(Border.NO_BORDER);
            table2.addCell(new Cell(1,1).add(new Paragraph(vzDNPdf.getKontaktna_oseba())));//.setBorder(Border.NO_BORDER);
            table2.addCell(new Cell(1,1).add(new Paragraph("TELEFON")));//.setBorder(Border.NO_BORDER);
            table2.addCell(new Cell(1,1).add(new Paragraph("")));//.setBorder(Border.NO_BORDER);
            document.add(table2);

            /*
            Paragraph pOpisOkvare = new Paragraph();
            pOpisOkvare.setTextAlignment(TextAlignment.LEFT);
            pOpisOkvare.setHorizontalAlignment(HorizontalAlignment.LEFT);
            pOpisOkvare.setVerticalAlignment(VerticalAlignment.MIDDLE);
            pOpisOkvare.setFontSize(10);
            pOpisOkvare.add("3. sistem");
            document.add(pOpisOkvare);
            */
            Table table31 = new Table(UnitValue.createPercentArray(new float[]{5,10}));
            table31.setWidth(UnitValue.createPercentValue(100));
            table31.setFixedLayout();
            table31.setFontSize(10);
            table31.setFont(font);
            table31.setTextAlignment(TextAlignment.LEFT);
            table31.setVerticalAlignment(VerticalAlignment.BOTTOM);
            table31.addCell(new Cell(1,1).add(new Paragraph("3. sistem")));
            table31.addCell(new Cell(1,1).add(new Paragraph("obdobje (2,3,4,5,6,12) mesecev").setTextAlignment(TextAlignment.RIGHT)));
            //table31.addCell(new Cell(1,1).add(new Paragraph("datum prejšnjega")));
            //table31.addCell(new Cell(1,1).add(new Paragraph("datum naslednjega")));
            RemoveBorder(table31);
            document.add(table31);

            Table table3 = new Table(UnitValue.createPercentArray(new float[]{2,1,2,1,2,1,2,1,2,1,4,1,4}));
            table3.setWidth(UnitValue.createPercentValue(100));
            table3.setFixedLayout();
            table3.setFont(font);
            table3.setTextAlignment(TextAlignment.CENTER);
            table3.addCell(new Cell(1,1).add(new Paragraph("požar")));
            if (vzDNPdfPer.getSis_pozar() == 0) {table3.addCell(new Cell(1,1).add(new Paragraph("")));}
            else {table3.addCell(new Cell(1,1).add(new Paragraph("X")));}
            table3.addCell(new Cell(1,1).add(new Paragraph("plin")));
            if (vzDNPdfPer.getSis_co() == 0) {table3.addCell(new Cell(1,1).add(new Paragraph("")));}
            else {table3.addCell(new Cell(1,1).add(new Paragraph("X")));}
            table3.addCell(new Cell(1,1).add(new Paragraph("vlom")));
            if (vzDNPdfPer.getSis_vlom() == 0) {table3.addCell(new Cell(1,1).add(new Paragraph("")));}
            else {table3.addCell(new Cell(1,1).add(new Paragraph("X")));}
            table3.addCell(new Cell(1,1).add(new Paragraph("video")));
            if (vzDNPdfPer.getSis_video() == 0) {table3.addCell(new Cell(1,1).add(new Paragraph("")));}
            else {table3.addCell(new Cell(1,1).add(new Paragraph("X")));}
            table3.addCell(new Cell(1,1).add(new Paragraph("pristop")));
            if (vzDNPdfPer.getSis_pristopna() == 0) {table3.addCell(new Cell(1,1).add(new Paragraph("")));}
            else {table3.addCell(new Cell(1,1).add(new Paragraph("X")));}
            table3.addCell(new Cell(1,1).add(new Paragraph("dim. bank.")));
            if (vzDNPdfPer.getSis_dimni_bankovci() == 0) {table3.addCell(new Cell(1,1).add(new Paragraph("")));}
            else {table3.addCell(new Cell(1,1).add(new Paragraph("X")));}
            table3.addCell(new Cell(1,1).add(new Paragraph(String.valueOf("Odb: " + vzDNPdfPer.getPeriodikaDni()*30) + " dni")));;
            document.add(table3);


            Table table41 = new Table(UnitValue.createPercentArray(new float[]{6,8,4,4}));
            table41.setWidth(UnitValue.createPercentValue(100));
            table41.setFixedLayout();
            table41.setFontSize(10);
            table41.setFont(font);
            table41.setTextAlignment(TextAlignment.LEFT);
            table41.setVerticalAlignment(VerticalAlignment.BOTTOM);
            table41.addCell(new Cell(1,1).add(new Paragraph("vzdrževanje")));
            table41.addCell(new Cell(1,1).add(new Paragraph("tip elementov")));
            table41.addCell(new Cell(1,1).add(new Paragraph("datum prejšnjega")));
            table41.addCell(new Cell(1,1).add(new Paragraph("datum naslednjega")));
            RemoveBorder(table41);
            document.add(table41);


            Table table4 = new Table(UnitValue.createPercentArray(new float[]{2,1,2,1,8,4,4}));
            table4.setWidth(UnitValue.createPercentValue(100));
            table4.setTextAlignment(TextAlignment.CENTER);
            table4.setFixedLayout();
            table4.setFont(font);
            table4.addCell(new Cell(1,1).add(new Paragraph("redno")));//.setBorder(Border.NO_BORDER);
            if (vzDNPdfPer.getRedno() == 0) {table4.addCell(new Cell(1,1).add(new Paragraph("")));}
            else {table4.addCell(new Cell(1,1).add(new Paragraph("X")));}
            table4.addCell(new Cell(1,1).add(new Paragraph("izredno")));//.setBorder(Border.NO_BORDER);
            if (vzDNPdfPer.getIzredno() == 0) {table4.addCell(new Cell(1,1).add(new Paragraph("")));}
            else {table4.addCell(new Cell(1,1).add(new Paragraph("X")));}
            table4.addCell(new Cell(1,1).add(new Paragraph(vzDNPdfPer.getTip_elementov())));//.setBorder(Border.NO_BORDER);
            table4.addCell(new Cell(1,1).add(new Paragraph(vzDNPdfPer.getDatum_prejsnjega())));//.setFontSize(9)));//.setBorder(Border.NO_BORDER);
            table4.addCell(new Cell(1,1).add(new Paragraph(vzDNPdfPer.getDatum_naslednjega())));
            document.add(table4);



            Table table51 = new Table(UnitValue.createPercentArray(new float[]{1,1,1,1}));
            table51.setWidth(UnitValue.createPercentValue(100));
            table51.setFixedLayout();
            table51.setFontSize(10);
            table51.setFont(font);
            table51.setTextAlignment(TextAlignment.LEFT);
            table51.setVerticalAlignment(VerticalAlignment.BOTTOM);
            table51.addCell(new Cell(1,1).add(new Paragraph("kontrolor linije")));
            table51.addCell(new Cell(1,1).add(new Paragraph("aku-baterije (količina, U, aH)")));
            table51.addCell(new Cell(1,1).add(new Paragraph("način prenosa (modem)")));
            table51.addCell(new Cell(1,1).add(new Paragraph("na katero institucijo")));
            RemoveBorder(table51);
            document.add(table51);

            Table table5 = new Table(UnitValue.createPercentArray(new float[]{1,1,1,1}));
            table5.setWidth(UnitValue.createPercentValue(100));
            table5.setFixedLayout();
            //table5.setFontSize(10);
            table5.setFont(font);
            table5.setTextAlignment(TextAlignment.LEFT);
            table5.setVerticalAlignment(VerticalAlignment.BOTTOM);
            table5.addCell(new Cell(1,1).add(new Paragraph(vzDNPdfPer.getkontrolor())));
            table5.addCell(new Cell(1,1).add(new Paragraph(vzDNPdfPer.getaku_bat())));
            table5.addCell(new Cell(1,1).add(new Paragraph(vzDNPdfPer.getnacin_prenosa())));
            table5.addCell(new Cell(1,1).add(new Paragraph(vzDNPdfPer.getprenos_inst())));
            //RemoveBorder(table51);
            document.add(table5);

            Paragraph oPreizkusi = new Paragraph();
            oPreizkusi.setTextAlignment(TextAlignment.LEFT);
            oPreizkusi.setHorizontalAlignment(HorizontalAlignment.LEFT);
            oPreizkusi.setVerticalAlignment(VerticalAlignment.MIDDLE);
            oPreizkusi.setFontSize(10);
            oPreizkusi.add("4. opravljeni preizkusi");
            document.add(oPreizkusi);

            Table table6 = new Table(UnitValue.createPercentArray(new float[]{10,1,10,1}));
            table6.setWidth(UnitValue.createPercentValue(100));
            table6.setFixedLayout();
            table6.setFontSize(10);
            table6.setFont(font);
            table6.setTextAlignment(TextAlignment.LEFT);
            table6.setVerticalAlignment(VerticalAlignment.BOTTOM);
            table6.addCell(new Cell(1,1).add(new Paragraph("Inštalacija in pravilna namestitev elementov")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr1() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}
            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje elementov protivlomnega varovanja")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr10() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}

            table6.addCell(new Cell(1,1).add(new Paragraph("Napetost akumulatorjev in napajalnikov")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr2() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}
            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje panik stikal")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr11() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}

            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje siren")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr3() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}
            table6.addCell(new Cell(1,1).add(new Paragraph("Čiščenje elementov protivlomnega varovanja")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr12() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}

            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje posameznih linij")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr4() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}
            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje sistema dimnih bankovcev")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr13() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}

            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje javljalnikov požara")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr5() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}
            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje video sistema")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr14() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}

            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje elementov za zaznavanje plinov")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr6() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}
            table6.addCell(new Cell(1,1).add(new Paragraph("Pregled arhiva video sistema")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr15() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}

            table6.addCell(new Cell(1,1).add(new Paragraph("Prenos signala na varnostno-nadzorni center")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr7() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}
            table6.addCell(new Cell(1,1).add(new Paragraph("Čiščenje elemtnov video sistema")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr16() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}

            table6.addCell(new Cell(1,1).add(new Paragraph("Prenos signala na gasilsko brigado")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr8() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}
            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje kontrole pristopa")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr17() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}

            table6.addCell(new Cell(1,1).add(new Paragraph("Čiščenje elementov požarnega javljanja")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr9() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}
            table6.addCell(new Cell(1,1).add(new Paragraph("Delovanje registracije delovnega časa")).setTextAlignment(TextAlignment.LEFT));
            if (vzDNPdfPer.getPr18() == 1){ table6.addCell(new Cell(1,1).add(new Paragraph("X")).setTextAlignment(TextAlignment.CENTER));}
            else{table6.addCell(new Cell(1,1).add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER));}

            //RemoveBorder(table51);
            document.add(table6);

            Paragraph pObracunDela = new Paragraph();
            pObracunDela.setTextAlignment(TextAlignment.LEFT);
            pObracunDela.setHorizontalAlignment(HorizontalAlignment.LEFT);
            pObracunDela.setVerticalAlignment(VerticalAlignment.MIDDLE);
            pObracunDela.setFontSize(10);
            pObracunDela.add("5. obračun dela");
            document.add(pObracunDela);


            Table table7 = new Table(UnitValue.createPercentArray(new float[]{5,2,5,2,5,2}));
            table7.setWidth(UnitValue.createPercentValue(100));
            table7.setFixedLayout();
            //table6.setFontSize(10);
            table7.setFont(font);
            table7.setTextAlignment(TextAlignment.LEFT);
            table7.setVerticalAlignment(VerticalAlignment.BOTTOM);
            table7.addCell(new Cell(1,1).add(new Paragraph("DELO (ur)")).setTextAlignment(TextAlignment.LEFT));
            table7.addCell(new Cell(1,1).add(new Paragraph(String.valueOf(vzDNPdfPer.getUreDelo()))).setTextAlignment(TextAlignment.CENTER));
            table7.addCell(new Cell(1,1).add(new Paragraph("PREVOZ (ur)")).setTextAlignment(TextAlignment.LEFT));
            table7.addCell(new Cell(1,1).add(new Paragraph(String.valueOf(vzDNPdfPer.getUrePrevoz()))).setTextAlignment(TextAlignment.CENTER));
            table7.addCell(new Cell(1,1).add(new Paragraph("KILOMETRINA (km)")).setTextAlignment(TextAlignment.LEFT));
            table7.addCell(new Cell(1,1).add(new Paragraph(String.valueOf(vzDNPdfPer.getStKm()))).setTextAlignment(TextAlignment.CENTER));
            document.add(table7);

            Paragraph pOpombe = new Paragraph();
            pOpombe.setTextAlignment(TextAlignment.LEFT);
            pOpombe.setHorizontalAlignment(HorizontalAlignment.LEFT);
            pOpombe.setVerticalAlignment(VerticalAlignment.MIDDLE);
            pOpombe.setFontSize(10);
            pOpombe.add("6. opombe");
            document.add(pOpombe);

            Table table8 = new Table(UnitValue.createPercentArray(new float[]{10}));
            table8.setWidth(UnitValue.createPercentValue(100));
            table8.setFixedLayout();
            //table6.setFontSize(10);
            table8.setFont(font);
            table8.setTextAlignment(TextAlignment.LEFT);
            table8.setVerticalAlignment(VerticalAlignment.BOTTOM);
            table8.addCell(new Cell(1,1).add(new Paragraph(vzDNPdfPer.getOpomba())).setTextAlignment(TextAlignment.LEFT));
            document.add(table8);

            Table table91 = new Table(UnitValue.createPercentArray(new float[]{4,6,2,4}));
            table91.setWidth(UnitValue.createPercentValue(100));
            table91.setFixedLayout();
            table91.setFont(font);
            table91.addCell(new Cell(1,1).add(new Paragraph("7. podpisniki")));//.setBorder(Border.NO_BORDER);
            table91.addCell(new Cell(1,1).add(new Paragraph("ime in priimek")));//.setBorder(Border.NO_BORDER);
            table91.addCell(new Cell(1,1).add(new Paragraph("datum")));//.setBorder(Border.NO_BORDER);
            table91.addCell(new Cell(1,1).add(new Paragraph("podpis")));//.setBorder(Border.NO_BORDER);
            RemoveBorder(table91);
            document.add(table91);

            Table table9 = new Table(UnitValue.createPercentArray(new float[]{4,6,2,4}));
            table9.setWidth(UnitValue.createPercentValue(100));
            table9.setFixedLayout();
            table9.setFont(font);
            String tehnik = db.getUporabnikNaziv(tehnikID);
            table9.addCell(new Cell(1,1).add(new Paragraph("NALOG NAPISAL")));//.setBorder(Border.NO_BORDER);
            table9.addCell(new Cell(1,1).add(new Paragraph(tehnik.toUpperCase())));//.setBorder(Border.NO_BORDER);
            table9.addCell(new Cell(1,1).add(new Paragraph(vzDNPdfPer.getDATUM_IZVEDBE())));//.setBorder(Border.NO_BORDER);
            table9.addCell(new Cell(1,1).add(new Paragraph("")));//.setBorder(Border.NO_BORDER);
            table9.addCell(new Cell(1,1).add(new Paragraph("NAROČNIK")));//.setBorder(Border.NO_BORDER);
            table9.addCell(new Cell(1,1).add(new Paragraph("")));//.setBorder(Border.NO_BORDER);
            table9.addCell(new Cell(1,1).add(new Paragraph("")));//.setBorder(Border.NO_BORDER);
            table9.addCell(new Cell(1,1).add(new Paragraph("")));//.setBorder(Border.NO_BORDER);
            document.add(table9);
             /*
            String datum = "";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDateTime now = LocalDateTime.now();
            datum = dtf.format(now);



            Table table6 = new Table(UnitValue.createPercentArray(new float[]{4,6,2,4}));
            table6.setWidth(UnitValue.createPercentValue(100));
            table6.setFixedLayout();
            table6.setFont(font);
            table6.addCell(new Cell(1,1).add(new Paragraph("PODPISNIKI")));//.setBorder(Border.NO_BORDER);
            table6.addCell(new Cell(1,1).add(new Paragraph("ime in priimek")));//.setBorder(Border.NO_BORDER);
            table6.addCell(new Cell(1,1).add(new Paragraph("datum")));//.setBorder(Border.NO_BORDER);
            table6.addCell(new Cell(1,1).add(new Paragraph("podpis")));//.setBorder(Border.NO_BORDER);

            //table6.addCell(new Cell(1,1).add(new Paragraph("NALOG NAPISAL")));//.setBorder(Border.NO_BORDER);
            //table6.addCell(new Cell(1,1).add(new Paragraph(sn.getVodjaNaloga())));//.setBorder(Border.NO_BORDER);
            //table6.addCell(new Cell(1,1).add(new Paragraph(datum)));//.setBorder(Border.NO_BORDER);
            //table6.addCell(new Cell(1,1).add(new Paragraph("")));//.setBorder(Border.NO_BORDER);

            table6.addCell(new Cell(1,1).add(new Paragraph("IZVAJALEC")));//.setBorder(Border.NO_BORDER);
            table6.addCell(new Cell(1,1).add(new Paragraph(sn.getVodjaNaloga())));//.setBorder(Border.NO_BORDER);
            table6.addCell(new Cell(1,1).add(new Paragraph(datum)));//.setBorder(Border.NO_BORDER);
            table6.addCell(new Cell(1,1).add(new Paragraph("")));//.setBorder(Border.NO_BORDER);

            table6.addCell(new Cell(1,1).add(new Paragraph("NAROČNIK")).setVerticalAlignment(VerticalAlignment.MIDDLE));
            table6.addCell(new Cell(1,1).add(new Paragraph(sn.getOdgovornaOseba())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            table6.addCell(new Cell(1,1).add(new Paragraph(sn.getDatumPodpisa().toString())).setVerticalAlignment(VerticalAlignment.MIDDLE));
            byte[] podpisNarocnika = sn.getPodpis();
            if (podpisNarocnika != null) {
                Image podpis = new Image(ImageDataFactory.create(podpisNarocnika));
                table6.addCell(new Cell(1,1).add(podpis.setAutoScale(true)).setVerticalAlignment(VerticalAlignment.MIDDLE));
            }
            else
            {
                table6.addCell(new Cell(1,1).add(new Paragraph("")));
            }


            document.add(table6);
            */


            document.close();


            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //img.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //Image image = Image.getInstance(stream.toByteArray());

        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally
        {
            //.close();
        }

    }


    public void azurirajPodatke(DelovniNalogVZPeriodika dn)
    {
        RadioGroup rg1 = (RadioGroup) getView().findViewById(R.id.rbVZDNTipNarocila);
        CheckBox cb1;
        TextView tv;
        Spinner spn;
        int tipNarocila = dn.getTipNarocila();
        if (tipNarocila == 1)
        {
            ((RadioButton) rg1.getChildAt(0)).setChecked(true);
            ((RadioButton) rg1.getChildAt(1)).setChecked(false);
            ((RadioButton) rg1.getChildAt(2)).setChecked(false);
        }
        else if(tipNarocila == 2)
        {
            ((RadioButton) rg1.getChildAt(0)).setChecked(false);
            ((RadioButton) rg1.getChildAt(1)).setChecked(true);
            ((RadioButton) rg1.getChildAt(2)).setChecked(false);
        }
        else if (tipNarocila == 3)
        {
            ((RadioButton) rg1.getChildAt(0)).setChecked(false);
            ((RadioButton) rg1.getChildAt(1)).setChecked(false);
            ((RadioButton) rg1.getChildAt(2)).setChecked(true);
        }

        cb1 = (CheckBox) getView().findViewById(R.id.cbPozar);
        if (dn.getSis_pozar() == 0) { cb1.setChecked(false); }
        else { cb1.setChecked(true); }

        cb1 = (CheckBox) getView().findViewById(R.id.cbPlin);
        if (dn.getSis_co() == 0) { cb1.setChecked(false); }
        else { cb1.setChecked(true); }

        cb1 = (CheckBox) getView().findViewById(R.id.cbVlom);
        if (dn.getSis_vlom() == 0) { cb1.setChecked(false); }
        else { cb1.setChecked(true); }

        cb1 = (CheckBox) getView().findViewById(R.id.cbVideo);
        if (dn.getSis_video() == 0) { cb1.setChecked(false); }
        else { cb1.setChecked(true); }

        cb1 = (CheckBox) getView().findViewById(R.id.cbPristop);
        if (dn.getSis_pristopna() == 0) { cb1.setChecked(false); }
        else { cb1.setChecked(true); }

        cb1 = (CheckBox) getView().findViewById(R.id.cbDim);
        if (dn.getSis_dimni_bankovci() == 0) { cb1.setChecked(false); }
        else { cb1.setChecked(true); }

        cb1 = (CheckBox) getView().findViewById(R.id.cbRedno);
        if (dn.getRedno() == 0) { cb1.setChecked(false); }
        else { cb1.setChecked(true); }

        cb1 = (CheckBox) getView().findViewById(R.id.cbIzredno);
        if (dn.getIzredno() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }

        int obd = dn.getPeriodikaDni();
        spn = (Spinner) getView().findViewById(R.id.spinnObdobje);
        int myInd = getIndex(spn,String.valueOf(obd));
        spn.setSelection(myInd);

        tv = (TextView) getView().findViewById(R.id.etTipElementov);
        tv.setText(dn.getTip_elementov());

        tv = (TextView) getView().findViewById(R.id.etDatumPrejsnje);
        tv.setText(dn.getDatum_prejsnjega());

        tv = (TextView) getView().findViewById(R.id.etDatumNaslednjega);
        tv.setText(dn.getDatum_naslednjega());

        tv = (TextView) getView().findViewById(R.id.etKontrolorLinije);
        tv.setText(dn.getkontrolor());

        tv = (TextView) getView().findViewById(R.id.etBaterije);
        tv.setText(dn.getaku_bat());

        tv = (TextView) getView().findViewById(R.id.etNacinPrenosa);
        tv.setText(dn.getnacin_prenosa());

        tv = (TextView) getView().findViewById(R.id.etPrenosInstitucija);
        tv.setText(dn.getprenos_inst());


        cb1 = (CheckBox) getView().findViewById(R.id.cbInstalacija);
        if (dn.getPr1() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbNapetostAku);
        if (dn.getPr2() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeSiren);
        if (dn.getPr3() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjePosameznihLinij);
        if (dn.getPr4() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeJavljalnik);
        if (dn.getPr5() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }

        cb1 = (CheckBox) getView().findViewById(R.id.cbDelElZaPlin);
        if (dn.getPr6() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbPrenosSigNAVNC);
        if (dn.getPr7() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbPrenosGasilci);
        if (dn.getPr8() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbCiscanjeElPJ);
        if (dn.getPr9() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeElementovProtivlom);
        if (dn.getPr10() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjePanikTipk);
        if (dn.getPr11() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbCiscenjeElProtivlom);
        if (dn.getPr12() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeSistDimBank);
        if (dn.getPr13() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeVideoSist);
        if (dn.getPr14() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbPregledArhivaVS);
        if (dn.getPr15() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbCiscanjeELVS);
        if (dn.getPr16() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeKP);
        if (dn.getPr17() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }
        cb1 = (CheckBox) getView().findViewById(R.id.cbDelovanjeRDC);
        if (dn.getPr18() == 0)
        {
            cb1.setChecked(false);
        }
        else {
            cb1.setChecked(true);
        }

        tv = (TextView) getView().findViewById(R.id.etVZDNDatumIzvedbe);
        tv.setText(dn.getDATUM_IZVEDBE());

        tv = (TextView) getView().findViewById(R.id.etOpombeVZD);
        tv.setText(dn.getOpomba());

        String ure;
        tv = (TextView) getView().findViewById(R.id.etVZDNUrePrevoz);
        ure = String.valueOf(dn.getUrePrevoz());
        ure = ure.replace(',','.');
        tv.setText(ure);

        tv = (TextView) getView().findViewById(R.id.etVZDNUreDelo);
        ure = String.valueOf(dn.getUreDelo());
        ure = ure.replace(',','.');
        tv.setText(ure);

        tv = (TextView) getView().findViewById(R.id.etVZDNStKm);
        ure = String.valueOf(dn.getUreDelo());
        ure = ure.replace(',','.');
        tv.setText(ure);


    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public String vrniDatum(String datumDb)
    {
        String vrniStr = "";
        try {
            String string_format = datumDb;
            Date date_format = new SimpleDateFormat("dd.MM.yyyy").parse(string_format);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            vrniStr = dateFormat.format(date_format);
        }
        catch (Exception e)
        {
            vrniStr = " ";
        }
        return vrniStr;
    }

    private class UpdatePodpisanoOnWebVZDN extends AsyncTask<ArrayList,Void,Void> {
        private byte[] data;
        public UpdatePodpisanoOnWebVZDN(byte[] data) {
            this.data = data;
        }
        @Override
        protected Void doInBackground(ArrayList... arrayLists) {
            //String datum = "";
            //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //LocalDateTime now = LocalDateTime.now();
            //datum = dtf.format(now);
            byte[] podpis = data;
            String StoredPath = "";
            StoredPath = getContext().getFilesDir()+"/"+String.valueOf(vzDNID)+"-VZ.bmp";
            try {
                FileOutputStream fos = new FileOutputStream(StoredPath);
                fos.write(podpis);
                fos.close();
                uploadFile(StoredPath);
            }
            catch (Exception e){

            }
            updatePodpisanoVZDNMySql(arrayLists[0].get(0).toString(), arrayLists[0].get(1).toString(),  arrayLists[0].get(2).toString(), podpis) ;
            //Toast.makeText(getContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public void updatePodpisanoVZDNMySql(String id, String status_akt, String datum_podpisa, byte[] podpis)
    {
        String result = null;
        try {
            //uploadFile(StoredPath);
            String myUrl = "https://www.sintal.si/tehnika/updateVZDNPodpisDatumPodpisa.php?SNid="+id+"&status_akt="+status_akt+"&datum_podpisa="+datum_podpisa;
            URL url = new URL(myUrl);
            HttpURLConnection htconn = (HttpURLConnection) url.openConnection();
            String username ="sintal_teh";
            String password = "mCuSTArQ*PdWAH#7-updatePodpis";
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            htconn.setRequestProperty ("Authorization", basicAuth);
            htconn.connect();

            if (htconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(htconn.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String temp;

                while ((temp = reader.readLine()) != null) {
                    stringBuilder.append(temp);
                }
                result = stringBuilder.toString();
            }else  {
                result = "error";
            }
            htconn.disconnect();

        } catch (Exception  e) {
            e.printStackTrace();
        }

    }




    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection htpconn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {


            return 0;

        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("https://www.sintal.si/tehnika/uploadFile.php");
                String username ="sintal_teh";
                String password = "mCuSTArQ*PdWAH#7-uploadImage";
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));

                htpconn = (HttpURLConnection) url.openConnection();
                htpconn.setDoInput(true); // Allow Inputs
                htpconn.setDoOutput(true); // Allow Outputs
                htpconn.setUseCaches(false); // Don't use a Cached Copy
                htpconn.setRequestMethod("POST");
                htpconn.setRequestProperty("Connection", "Keep-Alive");
                htpconn.setRequestProperty("ENCTYPE", "multipart/form-data");
                //Content-Disposition: form-data; name=\"uploadedfile\"; filename=\"
                htpconn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                htpconn.setRequestProperty("uploadedfile", fileName);
                htpconn.setRequestProperty ("Authorization", basicAuth);
                dos = new DataOutputStream(htpconn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = htpconn.getResponseCode();
                String serverResponseMessage = htpconn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                }

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch(MalformedURLException ex){
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch(Exception e){

                //dialog.dismiss();
                e.printStackTrace();
/*
                    runOnUiThread(new Runnable() {
                        public void run() {
                            messageText.setText("Got Exception : see logcat ");
                            Toast.makeText(getActivity(), "Got Exception : see logcat ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    */
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            //dialog.dismiss();
            htpconn.disconnect();
            return serverResponseCode;

        } // End else block
    }


    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }


    public void dialog_action() {
        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new obrazecVZDNFragment.signature(getView().getContext(),null);
        //.signature(getContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;
        DatabaseHandler db = new DatabaseHandler(getContext());
        DelovniNalogVZPeriodika vz_per =  new DelovniNalogVZPeriodika();
        vz_per = db.vrniVZDNPre(vzDNID,perPre, mes_obr);
        mSignature.clear();

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("tag", "Panel Cleared");
                mSignature.clear();
                mSignature.setBackgroundColor(Color.WHITE);
                mGetSign.setEnabled(false);
            }
        });
        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.v("tag", "Panel Saved");
                view.setDrawingCacheEnabled(true);
                mSignature.save(view);
                mSignature.clear();
                dialog.dismiss();
                Toast.makeText(getContext(), "Uspešno podpisano", Toast.LENGTH_SHORT).show();

                // Calling the same class
                //recreate();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("tag", "Panel Cancelled");
                dialog.dismiss();
                // Calling the same class
                //recreate();
            }
        });

        if (vz_per.getPodpis() != null)
        {
            byte[] podpis = vz_per.getPodpis();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inMutable = true;
            File file = new File(getContext().getFilesDir()+"/"+vzDNID+"-VZ.bmp");
            Bitmap _scratch =  BitmapFactory.decodeByteArray(podpis, 0, podpis.length);
            Drawable d = new BitmapDrawable(_scratch);
            mSignature.setBackground(d);

        }

        dialog.show();
    }


    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void clear() {
            bitmap = null;
            path.reset();
            mSignature.setBackgroundColor(Color.WHITE);
            invalidate();
        }

        public void save(View v) {
            Log.v("tag", "Width: " + v.getWidth());
            Log.v("tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                DatabaseHandler db = new DatabaseHandler(getContext());
                //FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);
                String datum = "";
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime now = LocalDateTime.now();
                datum = dtf.format(now);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bArray = bos.toByteArray();
                db.updatePodpisVZDN(String.valueOf(vzDNID),bArray,datum);
                //new obrazecSNFragment().updatePodpisanoMySql(snID,"P",datum,bArray);//.execute();
                ArrayList<String> parameters = new ArrayList<String>();
                parameters.add(String.valueOf(vzDNID));
                parameters.add("P");
                parameters.add(datum);
                //parameters.add(bArray);

                new obrazecVZDNFragment.UpdatePodpisanoOnWebVZDN(bArray).execute(parameters);
                path.reset();
                clear();
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }


        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:
                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;
                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
            Log.v("log_tag", string);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

    }

    public String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        return macAddress;
    }
}


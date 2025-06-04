package si.sintal.sintaltehnika.ui.main.SN;

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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Image;
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
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import si.sintal.sintaltehnika.BuildConfig;
import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SNArtikel;
import si.sintal.sintaltehnika.ui.main.SNPagerAdapter;
import si.sintal.sintaltehnika.ui.main.SNPetrolActivity;
import si.sintal.sintaltehnika.ui.main.SendEmailService;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class obrazecSNFragment extends Fragment {

    private ObrazecSNViewModel mViewModel;
    //private int STSN;
    private String tehnikID;
    private String userID;
    private int snID;
    private String snDN;
    int serverResponseCode = 0;
    private SNDNArtikliAdapter adapterSeznamSNDNArtikli;
    private SNDNArtikliAdapter adapterSeznamSNDNArtikli2;
    static ArrayList<SNArtikel> artikliVgrajeni;
    static ArrayList<SNArtikel> artikliZamenjani;
    Button btn_get_sign, mClear, mGetSign, mCancel;
    File file;
    Dialog dialog;
    LinearLayout mContent;
    View view;
    obrazecSNFragment.signature mSignature;
    Bitmap bitmap;
    ServisniNalog sn = new ServisniNalog();
    //private String tehnikAdminDostop;
    //private String servis;
    //private String montaza;
    //private String vzdrzevanje;




    public static obrazecSNFragment newInstance() {
        return new obrazecSNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.obrazec_s_n_fragment, container, false);
        DatabaseHandler db = new DatabaseHandler(getContext());
        Intent intent= getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        tehnikID = intent.getStringExtra("tehnikID");
        snID = SNPagerAdapter.getParameters();

        sn = db.vrniSN(snID);
        snDN = sn.getDelovniNalog();
        String tehnik = sn.getVodjaNaloga();
        tehnikID = db.getTehnikId(tehnik);

        Button bBrisiDodatnegaServiserja = (Button) v.findViewById(R.id.bBrisiServiserja);
        bBrisiDodatnegaServiserja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.brisiSNServiserDodatni(String.valueOf(snID));
                TextView test;
                test = (TextView) getView().findViewById(R.id.twStServierja);
                test.setText("");

                test = (TextView) getView().findViewById(R.id.twNazivServiserja);
                test.setText("");
            }
        });


        Button bDodajMat = (Button) v.findViewById(R.id.bSNDodajVMat);
        bDodajMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SNDodajArtikel.class);

                intent.putExtra("userID", userID);
                intent.putExtra("tehnikID", tehnikID);
                intent.putExtra("snID",snID);
                intent.putExtra("snDN",snDN);
                intent.putExtra("vrsta",1);
                startActivity(intent);
            }
        });

        Button bDodajServiserja = (Button) v.findViewById(R.id.bDodajServiserja);
        bDodajServiserja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SNDodajServiserjaActivity.class);

                intent.putExtra("userID", userID);
                intent.putExtra("tehnikID", tehnikID);
                intent.putExtra("snID",snID);
                intent.putExtra("snDN",snDN);
                intent.putExtra("vrsta",1);
                startActivity(intent);
            }
        });

        Button bDodajMat2 = (Button) v.findViewById(R.id.bSNDodajZMat);
        bDodajMat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SNDodajArtikel.class);

                intent.putExtra("userID", userID);
                intent.putExtra("tehnikID", tehnikID);
                intent.putExtra("snID",snID);
                intent.putExtra("snDN",snDN);
                intent.putExtra("vrsta",2);
                startActivity(intent);
            }
        });

        Button bSNShrani = (Button) v.findViewById(R.id.bSNShrani);
        bSNShrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView test = (TextView) getView().findViewById(R.id.idObrazecSNTehnikID);
                tehnikID = test.getText().toString();
                RadioGroup rg1 = (RadioGroup) getView().findViewById(R.id.rbSNTipNarocila);
                int tipNarocila = rg1.indexOfChild(getView().findViewById(rg1.getCheckedRadioButtonId()));
                        //rg1.getCheckedRadioButtonId();
                RadioGroup rg2 = (RadioGroup) getView().findViewById(R.id.rgSNVzdrzevanje);
                int tipVzdrzevanja = rg2.indexOfChild(getView().findViewById(rg2.getCheckedRadioButtonId()));


                test = (TextView) getView().findViewById(R.id.tvSNDatumMontaze);
                String tvSNDatumMontaze = test.getText().toString();
                tvSNDatumMontaze = vrniDatum(tvSNDatumMontaze);
                test = (TextView) getView().findViewById(R.id.tvSNGarancija);
                String tvSNGarancija = test.getText().toString();
                test = (TextView) getView().findViewById(R.id.tvSNNapaka);
                String tvSNNapaka = test.getText().toString();
                test = (TextView) getView().findViewById(R.id.etSNUrePrevoz);
                String etSNUrePrevoz = test.getText().toString();
                etSNUrePrevoz = etSNUrePrevoz.replace(',','.');
                test = (TextView) getView().findViewById(R.id.etSNUreDelo);
                String etSNUreDelo = test.getText().toString();
                etSNUreDelo = etSNUreDelo.replace(',','.');
                test = (TextView) getView().findViewById(R.id.etSNStKm);
                String etSNStKm = test.getText().toString();
                etSNStKm = etSNStKm.replace(',','.');
                test = (TextView) getView().findViewById(R.id.tvSNNapaka2);
                String tvSNNapaka2 = test.getText().toString();

                test = (TextView) getView().findViewById(R.id.tvSNNarocil);
                String tvSNNarocilTelefon = test.getText().toString();

                test = (TextView) getView().findViewById(R.id.tvSNNarocilTelefon);
                String tvSNTelefon = test.getText().toString();

                if ((isValidFormat("yyyy-MM-dd",tvSNDatumMontaze) == true)
                    || (tvSNDatumMontaze.equals("")))
                {
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    db.updateSNDN(String.valueOf(snID), tipNarocila, tipVzdrzevanja, tvSNDatumMontaze, tvSNGarancija, tvSNNapaka,
                            etSNUrePrevoz, etSNUreDelo, etSNStKm, tvSNNapaka2, tvSNNarocilTelefon, tvSNTelefon
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


        Button bSign = (Button) v.findViewById(R.id.bSNPodisStranka);
        bSign.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog_action();
                    }
                }
        );

        Button bPdf = (Button) v.findViewById(R.id.bSNPoslji);
        bPdf.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //convertToPdf();
                            createPdf();
                            //openPdf();
                            //db.updateSNStatusAkt(snID,"Z");
                            ArrayList<String> parameters = new ArrayList<String>();
                            parameters.add(String.valueOf(snID));
                            //parameters.add("Z");
                            new UpdateZakljucenoOnWeb().execute(parameters);
                            db.updateSNStatusZakljuci(String.valueOf(snID),"Z");
                            bDodajMat.setEnabled(false);
                            bDodajMat2.setEnabled(false);
                            bSNShrani.setEnabled(false);
                            bSign.setEnabled(false);
                            bPdf.setEnabled(false);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        Button bPoslji = (Button) v.findViewById(R.id.buttonSNPoslji);
        bPoslji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView emailTw = (TextView) getView().findViewById(R.id.etSNEmail);
                String email =  emailTw.getText().toString();
                String status = sn.getStatus();
                if ((status.equals("A") == true) || (status.equals("D") == true) || (status.equals("P") == true)) {
                    try {
                        createPdf();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (email.equals("") == false) {
                    new SendEmailNarocnik().execute();
                }
                else
                {
                    Toast.makeText(getView().getContext(),"Obvezno vnesite email za pošiljanje!",Toast.LENGTH_LONG).show();
                }
            }
        });

        Button bShowSN = (Button) v.findViewById(R.id.buttonSNPrikazi);
        bShowSN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //convertToPdf();
                            String status = sn.getStatus();
                            if ((status.equals("A") == true) || (status.equals("D") == true) || (status.equals("P") == true))
                            {
                                createPdf();
                                openPdf();
                            }
                            else {
                                String path = getContext().getFilesDir().getPath();
                                File dir = new File(path);
                                File file = new File(dir, snID+".pdf");
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

        Button bSNPetrol = (Button) v.findViewById(R.id.bSNPetrol);
        bSNPetrol.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //SNPetrolFragment novFragment = new SNPetrolFragment();
                        Intent intent = new Intent(getActivity(), SNPetrolActivity.class);
                        intent.putExtra("userID", userID);
                        intent.putExtra("tehnikID",tehnikID);
                        intent.putExtra("snID",snID);
                        //intent.putExtra("userName", nadzornik);
                        //intent.putExtra("email", email);
                        //intent.putExtra("admin",admin_dostop);
                        //intent.putExtra("servis",servis);
                        //intent.putExtra("montaza",montaza);
                        //intent.putExtra("vzdrzevanje",vzdrzevanje);
                         startActivity(intent);
                    }
                }
        );

        Button bDodajVgrMat = (Button) v.findViewById(R.id.bSNDodajVMat);
        Button bDodajNovMat = (Button) v.findViewById(R.id.bSNDodajZMat);

        Button bSNShraniSpremembe = (Button) v.findViewById(R.id.bSNShraniSNPo);
        bSNShraniSpremembe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Funkcija v izdelai!", Toast.LENGTH_SHORT).show();
            }
        });
        String statusSN = sn.getStatus();
        if (statusSN == null)
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

        return v;
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


    @Override
    public void onStart() {
        super.onStart();
        //STSN = SNPagerAdapter.getParameters();
        //methodA(); // this is called ...
    }

    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
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
        snID = SNPagerAdapter.getParameters();

        sn = db.vrniSN(snID);
        snDN = sn.getDelovniNalog();
        String tehnik = sn.getVodjaNaloga();
        tehnikID = db.getTehnikId(tehnik);

        Button bSNShrani = (Button) getView().findViewById(R.id.bSNShrani);
        Button bPdf = (Button) getView().findViewById(R.id.bSNPoslji);
        Button bSign = (Button) getView().findViewById(R.id.bSNPodisStranka);
        Button bDodajVgrMat = (Button) getView().findViewById(R.id.bSNDodajVMat);
        Button bDodajNovMat = (Button) getView().findViewById(R.id.bSNDodajZMat);
        Button bSNShraniSpremembe = (Button) getView().findViewById(R.id.bSNShraniSNPo);

        String tehnikID = "";//this.tehnikID;
        int rgIndex = sn.getTipNarocila();
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

        tehnikID = db.getTehnikId(sn.getVodjaNaloga());
        this.tehnikID = tehnikID;
        TextView test = (TextView) getView().findViewById(R.id.idObrazecSN);
        test.setText(Integer.toString(sn.getid()));
        test = (TextView) getView().findViewById(R.id.idObrazecSNTehnikID);
        test.setText(tehnikID);
        test = (TextView) getView().findViewById(R.id.tvSNNarocnikNaziv);
        test.setText(sn.getNarocnikNaziv());
        test = (TextView) getView().findViewById(R.id.tvStevilkaSN);
        test.setText(sn.getDelovniNalog());
        test = (TextView) getView().findViewById(R.id.tvSNNarocnikNaslov);
        test.setText(sn.getNarocnikKraj()+", "+sn.getNarocnikNaslov());

        test = (TextView) getView().findViewById(R.id.tvSNImeSektorja);
        test.setText(sn.getNarocnikSektor());
        test = (TextView) getView().findViewById(R.id.tvSNAdSekt);
        test.setText(sn.getKodaObjekta());
        test = (TextView) getView().findViewById(R.id.tvSNImeSekNaslov);
        test.setText(sn.getSektroNaslov());

        test = (TextView) getView().findViewById(R.id.tvSNKontaktnaOseba);
        test.setText(sn.getOdgovornaOseba());

        test = (TextView) getView().findViewById(R.id.tvSNTelefon);
        test.setText("");

        test = (TextView) getView().findViewById(R.id.tvSNNarocil);
        test.setText(sn.getOdgovornaOseba());
        test = (TextView) getView().findViewById(R.id.tvSNNarocilTelefon);
        test.setText("");

        test = (TextView) getView().findViewById(R.id.tvSNPripadnost);
        test.setText(sn.getPripadnostNaziv());

        Intent intent= getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        test = (TextView) getView().findViewById(R.id.tvStevilkaSN);
        snDN = test.getText().toString();
        test = (TextView) getView().findViewById(R.id.idObrazecSN);
        snID = Integer.parseInt(test.getText().toString());
        test = (TextView) getView().findViewById(R.id.idObrazecSNTehnikID);
        tehnikID = test.getText().toString();


        test = (TextView) getView().findViewById(R.id.tvSNDatumMontaze);
        test.setText(sn.getDatumIzvedbe());
        String datum = sn.getDatumIzvedbe();
        if ( (datum == null)
        || (datum.equals("")) )
        {
            //String datum = "";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDateTime now = LocalDateTime.now();
            datum = dtf.format(now);
            test.setText(datum);
        }

        test = (TextView) getView().findViewById(R.id.tvSNGarancija);
        if (sn.getGarancija() == 0)
        {
            test.setText("");
        }
        else
        {
            test.setText(Integer.toString(sn.getGarancija()));
        }

        test = (TextView) getView().findViewById(R.id.tvSNNapaka);
        if (sn.getOpis().equals(sn.getOpisOkvare()))
        {
            test.setText(sn.getOpis());
        }
        else {
            test.setText(sn.getOpis() + " " + sn.getOpisOkvare());
        }
        test = (TextView) getView().findViewById(R.id.etSNUrePrevoz);
        if (sn.getUrePrevoz() == 0.0)
        {
            test.setText("");
        }
        else
        {
            test.setText(Double.toString(sn.getUrePrevoz()));
        }
        test = (TextView) getView().findViewById(R.id.etSNUreDelo);
        if (sn.getUreDelo() == 0.0)
        {
            test.setText("");
        }
        else {
            test.setText(Double.toString(sn.getUreDelo()));
        }
        test = (TextView) getView().findViewById(R.id.etSNStKm);
        if (sn.getStKm() == 0.0)
        {
            test.setText("");
        }
        else {
            test.setText(Double.toString(sn.getStKm()));
        }
        test = (TextView) getView().findViewById(R.id.tvSNNapaka2);
        test.setText(sn.getOpisPostopka());

        artikliVgrajeni = db.GetSeznamArtikliDNSNUporabnik(Integer.parseInt(userID),Integer.parseInt(tehnikID),1,snID);
        adapterSeznamSNDNArtikli = new SNDNArtikliAdapter(getActivity(), artikliVgrajeni,snID,snDN,userID,tehnikID,1);
        //adapterSeznamSNDNArtikli.notifyDataSetChanged();
        ListView listView = (ListView) getView().findViewById(R.id.snVgrajeniArtikli);
        listView.setAdapter(adapterSeznamSNDNArtikli);

        artikliZamenjani = db.GetSeznamArtikliDNSNUporabnik(Integer.parseInt(userID),Integer.parseInt(tehnikID),2,snID);
        adapterSeznamSNDNArtikli2 = new SNDNArtikliAdapter(getActivity(), artikliZamenjani,snID,snDN,userID,tehnikID,2);
        //adapterSeznamSNDNArtikli2.notifyDataSetChanged();
        ListView listView2 = (ListView) getView().findViewById(R.id.snZamenjaniArtikli);
        listView2.setAdapter(adapterSeznamSNDNArtikli2);


        setDynamicHeight(listView);
        setDynamicHeight(listView2);

        String statusSN = sn.getStatus();
        if (statusSN == null)
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

        List<String> dodatniServiser = db.getDodaniServiser(String.valueOf(snID));

        if (dodatniServiser.isEmpty()) {
            test = (TextView) getView().findViewById(R.id.twStServierja);
            test.setText("");

            test = (TextView) getView().findViewById(R.id.twNazivServiserja);
            test.setText("");
        }
        else{
            test = (TextView) getView().findViewById(R.id.twStServierja);
            test.setText(dodatniServiser.get(0).toString());

            test = (TextView) getView().findViewById(R.id.twNazivServiserja);
            test.setText(dodatniServiser.get(1).toString());
        }

        //methodA(); // this is called ...
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ObrazecSNViewModel.class);
        // TODO: Use the ViewModel
    }

    public void dialog_action() {
        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new obrazecSNFragment.signature(getView().getContext(),null);
                //.signature(getContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;
        DatabaseHandler db = new DatabaseHandler(getContext());
        ServisniNalog sn =  new ServisniNalog();
        sn = db.vrniSN(snID);
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

        if (sn.getPodpis() != null)
        {
            byte[] podpis = sn.getPodpis();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inMutable = true;
            File file = new File(getContext().getFilesDir()+"/"+snID+"-SN.bmp");
            Bitmap _scratch =  BitmapFactory.decodeByteArray(podpis, 0, podpis.length);
            Drawable d = new BitmapDrawable(_scratch);
            mSignature.setBackground(d);

        }

        dialog.show();
    }

    public void createPdf() throws Exception
    {
        try {
            String path = getContext().getFilesDir().getPath();
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


            File file = new File(dir, snID+".pdf");
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
            ServisniNalog sn = new ServisniNalog();
            sn = db.vrniSN(snID);

            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter pdfWriter = new PdfWriter(fOut);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

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

            Table podNaslov = new Table(UnitValue.createPercentArray(new float[]{5,5,5}));
            podNaslov.setWidth(UnitValue.createPercentValue(100));
            podNaslov.setFixedLayout();
            podNaslov.setFont(font);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("").setFontSize(12).setItalic()));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("sistemi tehničnega varovanja").setFontSize(12).setItalic()));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("številka: "+ snDN)).setTextAlignment(TextAlignment.RIGHT));//.setBorder(Border.NO_BORDER);
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
            String sektorNaslov = sn.getSektroNaslov().split(",",2)[0];
            if (sn.getSektroNaslov().length() > 0)
            {
                sektorKraj = sn.getSektroNaslov().split(",",2)[1];
            }
            else
            {
                sektorKraj = "";
            }
            Table table = new Table(UnitValue.createPercentArray(new float[]{5,6,4,6}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setFixedLayout();
            table.setFont(font);
            table.addCell(new Cell(1,1).add(new Paragraph("NAROČNIK")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,3).add(new Paragraph(sn.getNarocnikNaziv())));//.setBorder(Border.NO_BORDER);

            table.addCell(new Cell().add(new Paragraph("ULICA (naročnik)")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(sn.getNarocnikNaslov())));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("KRAJ")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(sn.getNarocnikKraj())));//.setBorder(Border.NO_BORDER);

            table.addCell(new Cell().add(new Paragraph("IME OBJEKTA")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(sn.getNarocnikSektor())));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("AD 1. SEKTOR")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(sn.getKodaObjekta())));//.setBorder(Border.NO_BORDER);

            table.addCell(new Cell().add(new Paragraph("ULICA (objekt)")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(sektorNaslov)));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("KRAJ")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(sektorKraj)));//.setBorder(Border.NO_BORDER);

            table.addCell(new Cell().add(new Paragraph("KONTAKTNA OSEBA")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(sn.getOdgovornaOseba())));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("TELEFON")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("")));//.setBorder(Border.NO_BORDER);


            table.addCell(new Cell().add(new Paragraph("PRIPADNOST")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph(sn.getPripadnostNaziv())));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("PODIZVAJALEC")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell().add(new Paragraph("")));//.setBorder(Border.NO_BORDER);

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
            table2.addCell(new Cell(1,1).add(new Paragraph(sn.getOdgovornaOseba())));//.setBorder(Border.NO_BORDER);
            table2.addCell(new Cell(1,1).add(new Paragraph("TELEFON")));//.setBorder(Border.NO_BORDER);
            table2.addCell(new Cell(1,1).add(new Paragraph("")));//.setBorder(Border.NO_BORDER);
            document.add(table2);

            Paragraph pOpisOkvare = new Paragraph();
            pOpisOkvare.setTextAlignment(TextAlignment.LEFT);
            pOpisOkvare.setHorizontalAlignment(HorizontalAlignment.LEFT);
            pOpisOkvare.setVerticalAlignment(VerticalAlignment.MIDDLE);
            pOpisOkvare.setFontSize(10);
            pOpisOkvare.add("3. opis okvare");
            document.add(pOpisOkvare);

            Table table3 = new Table(UnitValue.createPercentArray(new float[]{10}));
            table3.setWidth(UnitValue.createPercentValue(100));
            table3.setFixedLayout();
            table3.setFont(font);
            table3.addCell(new Cell(1,1).add(new Paragraph(String.valueOf(sn.getOpisOkvare()))));
            document.add(table3);

            Paragraph pSpecifikacijaMateriala = new Paragraph();
            pSpecifikacijaMateriala.setTextAlignment(TextAlignment.LEFT);
            pSpecifikacijaMateriala.setHorizontalAlignment(HorizontalAlignment.LEFT);
            pSpecifikacijaMateriala.setVerticalAlignment(VerticalAlignment.MIDDLE);
            pSpecifikacijaMateriala.setFontSize(10);
            pSpecifikacijaMateriala.setFont(font);
            pSpecifikacijaMateriala.add("4. specifikacija materiala");
            document.add(pSpecifikacijaMateriala);

            ArrayList<SNArtikel> artikliDN;
            //tehnikID = db.getTehnikId(SN.getVodjaNaloga());
            artikliDN = db.GetSeznamArtikliIzpisDNSNUporabnik(Integer.parseInt(userID),Integer.parseInt(tehnikID),1,snID);

            Table table4 = new Table(UnitValue.createPercentArray(new float[]{1,8,2,2}));
            table4.setWidth(UnitValue.createPercentValue(100));
            table4.setFixedLayout();
            table4.setFont(font);
            table4.addCell(new Cell(1,1).add(new Paragraph("ŠT.")));//.setBorder(Border.NO_BORDER);
            table4.addCell(new Cell(1,1).add(new Paragraph("NAZIV")));//.setBorder(Border.NO_BORDER);
            table4.addCell(new Cell(1,1).add(new Paragraph("KOLIČINA")));//.setBorder(Border.NO_BORDER);
            table4.addCell(new Cell(1,1).add(new Paragraph("Zamenjan/Nov").setFontSize(9)));//.setBorder(Border.NO_BORDER);
            for(int i = 1;i<artikliDN.size()+1;i++)
            {
                SNArtikel snArtikel = (SNArtikel) artikliDN.get(i-1);
                if (snArtikel.getnaziv().equals("VIR"))
                {}
                else {
                    table4.addCell(new Cell(1, 1).add(new Paragraph(Integer.toString(i))));
                    table4.addCell(new Cell(1, 1).add(new Paragraph(snArtikel.getnaziv())));
                    table4.addCell(new Cell(1, 1).add(new Paragraph(String.valueOf(snArtikel.getKolicina()))));
                    if (snArtikel.getZamenjanNov() == 1) {
                        table4.addCell(new Cell(1, 1).add(new Paragraph("N")));
                    } else if (snArtikel.getZamenjanNov() == 2) {
                        table4.addCell(new Cell(1, 1).add(new Paragraph("Z")));
                    } else {
                        table4.addCell(new Cell(1, 1).add(new Paragraph("")));
                    }
                }
            }
            document.add(table4);

            Paragraph pPostopek = new Paragraph();
            pPostopek.setTextAlignment(TextAlignment.LEFT);
            pPostopek.setHorizontalAlignment(HorizontalAlignment.LEFT);
            pPostopek.setVerticalAlignment(VerticalAlignment.MIDDLE);
            pPostopek.setFontSize(10);
            pPostopek.setFont(font);
            pPostopek.add("5. natančen opis postopka odprave napake in njenega vzroka");
            document.add(pPostopek);

            Table table5 = new Table(UnitValue.createPercentArray(new float[]{10}));
            table5.setWidth(UnitValue.createPercentValue(100));
            table5.setFixedLayout();
            table5.setFont(font);
            table5.addCell(new Cell(1,1).add(new Paragraph(sn.getOpisPostopka())));
            document.add(table5);

            Paragraph pPodpisniki = new Paragraph();
            pPodpisniki.setTextAlignment(TextAlignment.LEFT);
            pPodpisniki.setHorizontalAlignment(HorizontalAlignment.LEFT);
            pPodpisniki.setVerticalAlignment(VerticalAlignment.MIDDLE);
            pPodpisniki.setFontSize(10);
            pPodpisniki.add("6. podpisniki");
            document.add(pPodpisniki);

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

    private static void RemoveBorder(Table table)
    {
        for (IElement iElement : table.getChildren()) {
            ((Cell)iElement).setBorder(Border.NO_BORDER);
        }
    }

    public Cell getCell(Text text, TextAlignment alignment) {
        Cell cell = new Cell().add(new Paragraph(text));
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    public void convertToPdf() throws Exception
    {
        File htmlSource = new File(getContext().getFilesDir()+"/test.html");
        File pdfDest = new File(getContext().getFilesDir()+"/output.pdf");
        // pdfHTML specific code
        //HtmlConverter.convertToPdf(htmlSource,pdfDest);

    }


    public void openPdf() throws Exception
    {
        File file = new File(getContext().getFilesDir()+"/"+snID+".pdf");
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

    private class SendEmailNarocnik extends AsyncTask {
        String result = null;
        @Override
        protected Object doInBackground(Object... arg0) {
                result = "";
                SendEmailService sm = new SendEmailService(getContext());
                String fn = getContext().getFilesDir()+"/"+snID+".pdf";
                File pdfFile = new File(fn);
                /*
                if (pdfFile.exists() == false)
                {
                    try {
                        String status = sn.getStatus();
                        if ((status.equals("A") == true) || (status.equals("D") == true) || (status.equals("P") == true)) {
                            createPdf();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                TextView emailTw = (TextView) getActivity().findViewById(R.id.etSNEmail);
                String email =  emailTw.getText().toString();
                result = sm.SendEmail(fn,email);

                if (result.equals("") == true)
                {
                    String datum = "";
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    datum = dtf.format(now);
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    ServisniNalog sn = db.vrniSN(snID);
                    String st = sn.getStatus();
                    if(st.equals("Z") == true)
                    {

                    }
                    else
                    {
                        db.updateSNPoslano(String.valueOf(snID), "P", datum);
                    }
                    db.insertEmailLog(userID,snID,snDN,email,datum);
                }

                return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (result.equals("") == true)
            {
                Toast.makeText(getActivity(), "Podatki uspešno poslani!", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getActivity(), "Napaka pri pošiljanju podatkov!", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class UpdatePodpisanoOnWeb extends AsyncTask<ArrayList,Void,Void> {
        private byte[] data;
        public UpdatePodpisanoOnWeb(byte[] data) {
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
            StoredPath = getContext().getFilesDir()+"/"+snID+"-SN.bmp";
            try {
                FileOutputStream fos = new FileOutputStream(StoredPath);
                fos.write(podpis);
                fos.close();
                uploadFile(StoredPath);
            }
            catch (Exception e){

            }
            updatePodpisanoMySql(arrayLists[0].get(0).toString(), arrayLists[0].get(1).toString(),  arrayLists[0].get(2).toString(), podpis) ;
            //Toast.makeText(getContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private class UpdateZakljucenoOnWeb extends AsyncTask<ArrayList,Void,Void> {

        @Override
        protected Void doInBackground(ArrayList... arrayLists) {
            //String datum = "";
            //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //LocalDateTime now = LocalDateTime.now();
            //datum = dtf.format(now);
            for (int i = 0; i<arrayLists[0].size();i++) {
                updateZakljucenoMySql(arrayLists[0].get(i).toString(), "Z");
                //updateVodjaPrenosiMySql(arrayLists[0].get(i).toString(), serviser_sn.getSelectedItem().toString(), "D", datum);
            }
            return null;
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

    public void updatePodpisanoMySql(String id, String status_akt, String datum_podpisa, byte[] podpis)
    {
        String result = null;
        try {
            //uploadFile(StoredPath);
            String myUrl = "https://www.sintal.si/tehnika/updateSNPodpisDatumPodpisa.php?SNid="+id+"&status_akt="+status_akt+"&datum_podpisa="+datum_podpisa;
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

    public void updateZakljucenoMySql(String id, String status_akt)
    {
        String result = null;
        try {
            //uploadFile(StoredPath);
            String myUrl = "https://www.sintal.si/tehnika/updateSNStatusZakljuceno.php?SNid="+id+"&statusNaloga="+status_akt+"";
            URL url = new URL(myUrl);
            HttpURLConnection htconn = (HttpURLConnection) url.openConnection();
            String username ="sintal_teh";
            String password = "mCuSTArQ*PdWAH#7-updateSNStatus";
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
                db.updatePodpis(String.valueOf(snID),bArray,datum);
                //new obrazecSNFragment().updatePodpisanoMySql(snID,"P",datum,bArray);//.execute();
                ArrayList<String> parameters = new ArrayList<String>();
                parameters.add(String.valueOf(snID));
                parameters.add("P");
                parameters.add(datum);
                //parameters.add(bArray);

                new UpdatePodpisanoOnWeb(bArray).execute(parameters);
                path.reset();
                clear();
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            bitmap = null;
            path.reset();
            mSignature.setBackgroundColor(Color.WHITE);
            invalidate();
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





}
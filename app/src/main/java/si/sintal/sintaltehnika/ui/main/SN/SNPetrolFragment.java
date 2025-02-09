package si.sintal.sintaltehnika.ui.main.SN;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import si.sintal.sintaltehnika.BuildConfig;
import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SNArtikel;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

//import com.itextpdf.io.image.ImageDataFactory;


public class SNPetrolFragment extends Fragment {

    private SNPetrolViewModel mViewModel;
    private String userID;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;
    private Integer SNId;
    private String SNDn;
    static ArrayList<SNArtikel> artikliVgrajeni;
    static ArrayList<SNArtikel> artikliZamenjani;

    public static SNPetrolFragment newInstance() {
        return new SNPetrolFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sn_petrol_fragment, container, false);

        DatabaseHandler dbTehnika = new DatabaseHandler(getContext());


        Intent intent= getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        tehnikID = intent.getStringExtra("tehnikID");
        SNId = intent.getIntExtra("snID",0);
        ServisniNalog sn = dbTehnika.vrniSN(SNId);
        EditText etOpis = (EditText) v.findViewById(R.id.editTextTextMultiLine);
        etOpis.setText(sn.getOpisPostopka());
        artikliVgrajeni = dbTehnika.GetSeznamArtikliDNSNUporabnik(Integer.parseInt(userID),Integer.parseInt(tehnikID),1,SNId);
        artikliZamenjani = dbTehnika.GetSeznamArtikliDNSNUporabnik(Integer.parseInt(userID),Integer.parseInt(tehnikID),2,SNId);
        TableLayout table = (TableLayout) v.findViewById(R.id.tableSNPetrol);
        TableLayout table2 = (TableLayout) v.findViewById(R.id.tablePetrolPrevzemOS);
        TableLayout table3 = (TableLayout) v.findViewById(R.id.tablePremikOS);


        TextView tw = (TextView) v.findViewById(R.id.textView62);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        for (int I=0; I< artikliVgrajeni.size(); I++ )
        {
            SNArtikel art;
            art = artikliVgrajeni.get(I);
            TableRow tr = new TableRow(getContext());

            EditText et1 = new EditText(getContext());
            et1.setBackgroundResource(R.drawable.table_border_color2);
            et1.setGravity(Gravity.LEFT);
            et1.setText("   " + art.getnaziv());
            et1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            et1.setTextSize(11);
            et1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            //et1.setPaddingRelative(25,0,0,0);
            //et1.setPadding(25,0,0,0);
            //et1.setGravity(Gravity.CENTER_HORIZONTAL);

            EditText et2 = new EditText(getContext());
            et2.setBackgroundResource(R.drawable.table_border_color2);
            et2.setGravity(Gravity.LEFT);
            et2.setText(String.valueOf(art.getKolicina()));
            et2.setGravity(Gravity.CENTER_HORIZONTAL);
            et2.setTextSize(11);
            //et2.setPadding(1,0,0,0);


            EditText et3 = new EditText(getContext());
            //et3.setGravity(Gravity.LEFT);
            et3.setBackgroundResource(R.drawable.table_border_color2);
            et3.setText(art.getmerskaEnota());
            et3.setTextSize(11);
            et3.setGravity(Gravity.CENTER_HORIZONTAL);

            tr.addView(et1);
            tr.addView(et2);
            tr.addView(et3);
            table.addView(tr);
            et1.getLayoutParams().width = screenWidth - 385;
            //et2.getLayoutParams().width = screenWidth/10*2 - 5;
            //et3.getLayoutParams().width = screenWidth/10*2 -5;
            et2.setMinimumWidth(180);
            et3.setMinimumWidth(180);
            et1.getLayoutParams().height = 70;
            et2.getLayoutParams().height = 70;
            et3.getLayoutParams().height = 70;
        }
        for (int I=0; I< artikliZamenjani.size(); I++ )
        {
            SNArtikel art;
            art = artikliZamenjani.get(I);
            TableRow tr = new TableRow(getContext());

            EditText et1 = new EditText(getContext());
            et1.setBackgroundResource(R.drawable.table_border_color2);
            et1.setGravity(Gravity.LEFT);
            et1.setText("   " + art.getnaziv());
            et1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            et1.setTextSize(11);
            et1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            //et1.setPaddingRelative(25,0,0,0);
            //et1.setPadding(25,0,0,0);
            //et1.setGravity(Gravity.CENTER_HORIZONTAL);

            EditText et2 = new EditText(getContext());
            et2.setBackgroundResource(R.drawable.table_border_color2);
            et2.setGravity(Gravity.LEFT);
            et2.setText(String.valueOf(art.getKolicina()));
            et2.setGravity(Gravity.CENTER_HORIZONTAL);
            et2.setTextSize(11);
            //et2.setPadding(1,0,0,0);


            EditText et3 = new EditText(getContext());
            //et3.setGravity(Gravity.LEFT);
            et3.setBackgroundResource(R.drawable.table_border_color2);
            et3.setText(art.getmerskaEnota());
            et3.setTextSize(11);
            et3.setGravity(Gravity.CENTER_HORIZONTAL);

            tr.addView(et1);
            tr.addView(et2);
            tr.addView(et3);
            table.addView(tr);
            et1.getLayoutParams().width = screenWidth - 385;
            //et2.getLayoutParams().width = screenWidth/10*2 - 5;
            //et3.getLayoutParams().width = screenWidth/10*2 -5;
            et2.setMinimumWidth(180);
            et3.setMinimumWidth(180);
            et1.getLayoutParams().height = 70;
            et2.getLayoutParams().height = 70;
            et3.getLayoutParams().height = 70;
        }


        Button bSNPetrolDodajArt = (Button) v.findViewById(R.id.bSNPetrolDodajArt);
        bSNPetrolDodajArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tr = new TableRow(getContext());

                EditText et1 = new EditText(getContext());
                et1.setBackgroundResource(R.drawable.table_border_color2);
                et1.setGravity(Gravity.LEFT);
                et1.setTextSize(10);
                et1.setText("   ");

                EditText et2 = new EditText(getContext());
                et2.setBackgroundResource(R.drawable.table_border_color2);
                et2.setGravity(Gravity.CENTER);
                et2.setText("1.0");
                et2.setTextSize(10);

                EditText et3 = new EditText(getContext());
                et3.setGravity(Gravity.CENTER);
                et3.setBackgroundResource(R.drawable.table_border_color2);
                et3.setText("KOS");
                et3.setTextSize(10);

                tr.addView(et1);
                tr.addView(et2);
                tr.addView(et3);
                table.addView(tr);

                et1.getLayoutParams().width = screenWidth - 385;
                //et2.getLayoutParams().width = screenWidth/10*2 - 5;
                //et3.getLayoutParams().width = screenWidth/10*2 -5;
                et2.setMinimumWidth(180);
                et3.setMinimumWidth(180);
                et1.getLayoutParams().height = 70;
                et2.getLayoutParams().height = 70;
                et3.getLayoutParams().height = 70;

            }
        });


        Button bSNPetrolDodajOS1 = (Button) v.findViewById(R.id.bSNPetrolDodajOS1);
        bSNPetrolDodajOS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tr = new TableRow(getContext());

                EditText et1 = new EditText(getContext());
                et1.setBackgroundResource(R.drawable.table_border_color2);
                et1.setGravity(Gravity.LEFT);
                et1.setTextSize(11);
                et1.setText("   ");

                EditText et2 = new EditText(getContext());
                et2.setBackgroundResource(R.drawable.table_border_color2);
                et2.setGravity(Gravity.CENTER);
                et2.setText("   ");
                et2.setTextSize(11);

                EditText et3 = new EditText(getContext());
                et3.setGravity(Gravity.CENTER);
                et3.setBackgroundResource(R.drawable.table_border_color2);
                et3.setText("   ");
                et3.setTextSize(11);

                EditText et4 = new EditText(getContext());
                et4.setGravity(Gravity.CENTER);
                et4.setBackgroundResource(R.drawable.table_border_color2);
                et4.setText("   ");
                et4.setTextSize(11);

                tr.addView(et1);
                tr.addView(et2);
                tr.addView(et3);
                tr.addView(et4);
                table2.addView(tr);

                //et1.getLayoutParams().width = screenWidth/10-5;
                et2.getLayoutParams().width = screenWidth - 745;
                //et3.getLayoutParams().width = screenWidth/10*3-5;
                //et4.getLayoutParams().width = screenWidth/10*3-5;
                et1.setMinimumWidth(180);
                et3.setMinimumWidth(270);
                et4.setMinimumWidth(270);
                et1.getLayoutParams().height = 70;
                et2.getLayoutParams().height = 70;
                et3.getLayoutParams().height = 70;
                et4.getLayoutParams().height = 70;

            }
        });

        Button bSNPetrolDodajOS2 = (Button) v.findViewById(R.id.bSNPetrolDodajOS2);
        bSNPetrolDodajOS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tr = new TableRow(getContext());

                EditText et1 = new EditText(getContext());
                et1.setBackgroundResource(R.drawable.table_border_color2);
                et1.setGravity(Gravity.LEFT);
                et1.setTextSize(11);
                et1.setText("   ");

                EditText et2 = new EditText(getContext());
                et2.setBackgroundResource(R.drawable.table_border_color2);
                et2.setGravity(Gravity.CENTER);
                et2.setText("   ");
                et2.setTextSize(11);

                EditText et3 = new EditText(getContext());
                et3.setGravity(Gravity.CENTER);
                et3.setBackgroundResource(R.drawable.table_border_color2);
                et3.setText("   ");
                et3.setTextSize(11);

                EditText et4 = new EditText(getContext());
                et4.setGravity(Gravity.CENTER);
                et4.setBackgroundResource(R.drawable.table_border_color2);
                et4.setText("   ");
                et4.setTextSize(11);

                tr.addView(et1);
                tr.addView(et2);
                tr.addView(et3);
                tr.addView(et4);
                table3.addView(tr);

                //et1.getLayoutParams().width = screenWidth/10-5;
                et2.getLayoutParams().width = screenWidth -180 - 250- 250 -20;
                //et3.getLayoutParams().width = screenWidth/10 *2 -5;
                //et4.getLayoutParams().width = screenWidth/10 *2 -5;

                et1.setMinimumWidth(180);
                et3.setMinimumWidth(250);
                et4.setMinimumWidth(250);

                et1.getLayoutParams().height = 70;
                et2.getLayoutParams().height = 70;
                et3.getLayoutParams().height = 70;
                et4.getLayoutParams().height = 70;

            }
        });

        Button bSNPetrolNazaj = (Button) v.findViewById(R.id.bSNPetrolBack);
        bSNPetrolNazaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        Button bSNPetrolPDF = (Button) v.findViewById(R.id.bIzpisiPetrolPdf);
        bSNPetrolPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                try {
                    createPdf();
                    openPdf();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    public void openPdf() throws Exception
    {

        //dir, SNId+"-petrol.pdf"
        File file = new File(getContext().getFilesDir()+"/"+SNId+"-petrol.pdf");
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



    private Bitmap getBitmapFromDrawable(int drawableId) {
        return BitmapFactory.decodeResource(getResources(), drawableId);
    }

    public void createPdf() throws Exception
    {
        try {
            String path = getContext().getFilesDir().getPath();
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


            File file = new File(dir, SNId+"-petrol.pdf");
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
            sn = db.vrniSN(SNId);

            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter pdfWriter = new PdfWriter(fOut);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            String REGULAR = "res/font/arial.ttf";
            PdfFont font = PdfFontFactory.createFont(REGULAR);


            Bitmap bitmap = getBitmapFromDrawable(R.drawable.petrol); // Replace with your drawable
            // Convert bitmap to iText Image
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = new Image(ImageDataFactory.create(stream.toByteArray()));
            image.setWidth(80);
            document.add(image);


            Text tNaslov = new Text("Petrol, Slovenska energijska družba, d.d., Ljubljana");
            tNaslov.setFont(font);
            tNaslov.setFontSize(10);
            Paragraph p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.LEFT);
            p.setMarginLeft(80);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            p.setMargin(0);
            p.setMarginLeft(80);
            p.setPadding(0);
            document.add(p);

            tNaslov = new Text("Dunajska c. 50, 1527 Ljubljana");
            tNaslov.setFont(font);
            tNaslov.setFontSize(10);
            p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.LEFT);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            p.setMargin(0);
            p.setMarginLeft(80);
            p.setPadding(0);
            document.add(p);

            tNaslov = new Text("tel.: 01 47 14 234");
            tNaslov.setFont(font);
            tNaslov.setFontSize(10);
            p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.LEFT);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            p.setMargin(0);
            p.setMarginLeft(80);
            p.setPadding(0);
            document.add(p);

            tNaslov = new Text("www.petrol.si");
            tNaslov.setFont(font);
            tNaslov.setFontSize(10);
            p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.LEFT);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            p.setMargin(0);
            p.setMarginLeft(80);
            p.setPadding(0);
            document.add(p);


            tNaslov = new Text("POROČILO O OPRAVLJENEM DELU");
            tNaslov.setFont(font);
            tNaslov.setFontSize(16);
            p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.CENTER);
            //p.setMarginLeft(80);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            document.add(p);

            tNaslov = new Text("");
            tNaslov.setFont(font);
            tNaslov.setFontSize(16);
            p = new Paragraph(tNaslov);
            document.add(p);
            document.add(p);

            Table podNaslov = new Table(UnitValue.createPercentArray(new float[]{4,6,4,3}));
            podNaslov.setWidth(UnitValue.createPercentValue(100));
            podNaslov.setFixedLayout();
            podNaslov.setFont(font);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("NAROČILO").setFontSize(12).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(5)));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("").setFontSize(12).setItalic()));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("DATUM")).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(5));//.setBorder(Border.NO_BORDER);
            EditText et2 = (EditText) getActivity().findViewById(R.id.editTextDate);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph(et2.getText().toString()).setFontSize(12)));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("OBJEKT (STM)").setFontSize(12).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(5)));//.setBorder(Border.NO_BORDER);
            et2 = (EditText) getActivity().findViewById(R.id.editTextTextPersonName3);
            podNaslov.addCell(new Cell(1,3).add(new Paragraph(et2.getText().toString()).setFontSize(12)));//.setBorder(Border.NO_BORDER);

            podNaslov.addCell(new Cell(1,1).add(new Paragraph("NAROČNIK").setFontSize(12).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(5)));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,3).add(new Paragraph("PETROL d.d., Ljubljana").setFontSize(12)));//.setBorder(Border.NO_BORDER);
            EditText et = (EditText) getActivity().findViewById(R.id.editTextTextPersonName5);
            String izvajalec = et.getText().toString();
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("IZVAJALEC").setFontSize(12).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(5)));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,3).add(new Paragraph(izvajalec).setFontSize(12)));//.setBorder(Border.NO_BORDER);
            et = (EditText) getActivity().findViewById(R.id.editTextTextPersonName6);
            String serviser = et.getText().toString();
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("SERVISER").setFontSize(12).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(5)));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,3).add(new Paragraph(serviser).setFontSize(12)));//.setBorder(Border.NO_BORDER);


            podNaslov.addCell(new Cell(1,1).add(new Paragraph("URA PRIHODA").setFontSize(12).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(5)));//.setBorder(Border.NO_BORDER);
            et = (EditText) getActivity().findViewById(R.id.editTextTextPersonName7);
            String ura_pri = et.getText().toString();
            podNaslov.addCell(new Cell(1,1).add(new Paragraph(ura_pri).setFontSize(12)));//.setBorder(Border.NO_BORDER);
            podNaslov.addCell(new Cell(1,1).add(new Paragraph("URA ODHODA")).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(5));//.setBorder(Border.NO_BORDER);
            et = (EditText) getActivity().findViewById(R.id.editTextTextPersonName8);
            String ura_od = et.getText().toString();
            podNaslov.addCell(new Cell(1,1).add(new Paragraph(ura_od).setFontSize(12)));//.setBorder(Border.NO_BORDER);
            podNaslov.setMarginBottom(10);
            document.add(podNaslov);

            Table table = new Table(UnitValue.createPercentArray(new float[]{100f}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setFixedLayout();
            table.setFont(font);
            table.addCell(new Cell(1,1).add(new Paragraph("OPIS IZVEDENIH DEL")));//.setBorder(Border.NO_BORDER);
            et = (EditText) getActivity().findViewById(R.id.editTextTextMultiLine);
            ura_pri = et.getText().toString();
            table.addCell(new Cell(1,1).add(new Paragraph(ura_pri)));//.setBorder(Border.NO_BORDER);
            table.setMarginBottom(10);
            document.add(table);


            table = new Table(UnitValue.createPercentArray(new float[]{70f,10f,20f}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setFixedLayout();
            table.setFont(font);
            table.addCell(new Cell(1,3).add(new Paragraph("PORABLJENI MATERIAL")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("material").setTextAlignment(TextAlignment.CENTER)));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("količina")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("enota")));//.setBorder(Border.NO_BORDER);
            //DODAJ IZ TABELE PORABLJENI MATERIAL
            table.setMarginBottom(10);
            String rowContent = null;
            TableLayout tl = (TableLayout) getActivity().findViewById(R.id.tableSNPetrol);
            for (int i = 0; i < tl.getChildCount(); i++) {
                TableRow row = (TableRow) tl.getChildAt(i);
                int test = tl.getChildCount();
                if (tl.getChildCount() >= 1) {
                    for (int j = 0; j < row.getChildCount(); j++) {
                        TextView currentCell = (TextView) row.getChildAt(j);
                        rowContent = currentCell.getText().toString();
                        table.addCell(new Cell(1, 1).add(new Paragraph(rowContent)));
                    }
                    //Log.d("Content of Row is:", rowContent);
                }
            }
            document.add(table);

            Bitmap bitmap2 = getBitmapFromDrawable(R.drawable.petrol_footer_logo); // Replace with your drawable
            // Convert bitmap to iText Image
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
            Image image2 = new Image(ImageDataFactory.create(stream2.toByteArray()));
            image2.setWidth(80);
            image2.setFixedPosition((float) PageSize.A4.getWidth()- 120,  10 );//  setAbsolutePosition(0, (float) (PageSize.A4.getHeight() - 20.0));
            document.add(image2);

            PdfCanvas canvas = new PdfCanvas(document.getPdfDocument().getPage(1));
            canvas.beginText().setFontAndSize(font,5)
                    .moveText(36, 20)
                    .showText("Vpis v sodni register: Okrožno sodišče v Ljubljani, pod vložno štev. 1/05773/00, osnovni kapital: 52.240.977,04 EUR, ID za DDV SI80267432")
                    .endText();
            canvas.release();

/*
            for(int i = 1;i<tl.get.size()+1;i++)
            {
                SNArtikel snArtikel = (SNArtikel) artikliDN.get(i-1);
                if (snArtikel.getnaziv().equals("VIR"))
                {}
                else {
                    table.addCell(new Cell(1, 1).add(new Paragraph(Integer.toString(i))));
                    table.addCell(new Cell(1, 1).add(new Paragraph(snArtikel.getnaziv())));
                    table.addCell(new Cell(1, 1).add(new Paragraph(String.valueOf(snArtikel.getKolicina()))));
                    if (snArtikel.getZamenjanNov() == 1) {
                        table.addCell(new Cell(1, 1).add(new Paragraph("N")));
                    } else if (snArtikel.getZamenjanNov() == 2) {
                        table.addCell(new Cell(1, 1).add(new Paragraph("Z")));
                    } else {
                        table.addCell(new Cell(1, 1).add(new Paragraph("")));
                    }
                }
            }

 */



            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.add(image);

            tNaslov = new Text("Petrol, Slovenska energijska družba, d.d., Ljubljana");
            tNaslov.setFont(font);
            tNaslov.setFontSize(10);
            p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.LEFT);
            p.setMarginLeft(80);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            p.setMargin(0);
            p.setMarginLeft(80);
            p.setPadding(0);
            document.add(p);

            tNaslov = new Text("Dunajska c. 50, 1527 Ljubljana");
            tNaslov.setFont(font);
            tNaslov.setFontSize(10);
            p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.LEFT);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            p.setMargin(0);
            p.setMarginLeft(80);
            p.setPadding(0);
            document.add(p);

            tNaslov = new Text("tel.: 01 47 14 234");
            tNaslov.setFont(font);
            tNaslov.setFontSize(10);
            p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.LEFT);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            p.setMargin(0);
            p.setMarginLeft(80);
            p.setPadding(0);
            document.add(p);

            tNaslov = new Text("www.petrol.si");
            tNaslov.setFont(font);
            tNaslov.setFontSize(10);
            p = new Paragraph(tNaslov);
            p.setTextAlignment(TextAlignment.LEFT);
            p.setHorizontalAlignment(HorizontalAlignment.CENTER);
            p.setVerticalAlignment(VerticalAlignment.MIDDLE);
            p.setMargin(0);
            p.setMarginLeft(80);
            p.setPadding(0);
            document.add(p);

            tNaslov = new Text("");
            tNaslov.setFont(font);
            tNaslov.setFontSize(16);
            p = new Paragraph(tNaslov);
            document.add(p);
            document.add(p);

            Table podNaslov2 = new Table(UnitValue.createPercentArray(new float[]{6,5,3,3}));
            podNaslov2.setWidth(UnitValue.createPercentValue(100));
            podNaslov2.setFixedLayout();
            podNaslov2.setFont(font);
            podNaslov2.addCell(new Cell(1,4).add(new Paragraph("PREVZEM OSNOVNIH SREDSTEV").setFontSize(12).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(5)));//.setBorder(Border.NO_BORDER);
            //podNaslov2.addCell(new Cell(1,3).add(new Paragraph("").setFontSize(12).setItalic()));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,1).add(new Paragraph("Dobavitelj")).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(5));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,3).add(new Paragraph("").setFontSize(12)));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,1).add(new Paragraph("Datum prevzema").setFontSize(12).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(5)));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,3).add(new Paragraph("").setFontSize(12)));//.setBorder(Border.NO_BORDER);

            podNaslov2.addCell(new Cell(1,1).add(new Paragraph("Številka računa").setFontSize(12).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(5)));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,1).add(new Paragraph("").setFontSize(12).setItalic()));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,1).add(new Paragraph("Datum računa").setFontSize(12).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(5)));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,1).add(new Paragraph("").setFontSize(12).setItalic()));//.setBorder(Border.NO_BORDER);

            podNaslov2.addCell(new Cell(1,1).add(new Paragraph("Številka pogodbe oz. naročilnice").setFontSize(12)));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,3).add(new Paragraph("").setFontSize(12).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(5)));//.setBorder(Border.NO_BORDER);

            podNaslov2.addCell(new Cell(1,1).add(new Paragraph("Številka dobavnice").setFontSize(12).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(5)));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,3).add(new Paragraph("").setFontSize(12)));//.setBorder(Border.NO_BORDER);


            podNaslov2.addCell(new Cell(1,1).add(new Paragraph("Naročnik").setFontSize(12).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(5)));//.setBorder(Border.NO_BORDER);
            podNaslov2.addCell(new Cell(1,3).add(new Paragraph("").setFontSize(12)));//.setBorder(Border.NO_BORDER);

            podNaslov2.setMarginBottom(10);
            document.add(podNaslov2);

            table = new Table(UnitValue.createPercentArray(new float[]{20f,45f,45f,40f}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setFixedLayout();
            table.setFont(font);
            table.addCell(new Cell(1,1).add(new Paragraph("Invent. št")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("Naziv / Opis").setTextAlignment(TextAlignment.CENTER)));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("IP").setTextAlignment(TextAlignment.CENTER)));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("Tovarniška številka").setTextAlignment(TextAlignment.CENTER)));//.setBorder(Border.NO_BORDER);
            //DODAJ IZ TABELE PORABLJENI MATERIAL
            table.setMarginBottom(10);
            rowContent = null;
            tl = (TableLayout) getActivity().findViewById(R.id.tablePetrolPrevzemOS);
            for (int i = 0; i < tl.getChildCount(); i++) {
                TableRow row = (TableRow) tl.getChildAt(i);
                int test = tl.getChildCount();
                if (tl.getChildCount() >= 1) {
                    for (int j = 0; j < row.getChildCount(); j++) {
                        TextView currentCell = (TextView) row.getChildAt(j);
                        rowContent = currentCell.getText().toString();
                        table.addCell(new Cell(1, 1).add(new Paragraph(rowContent)));
                    }
                    //Log.d("Content of Row is:", rowContent);
                }
            }
            document.add(table);

            table = new Table(UnitValue.createPercentArray(new float[]{20f,80f,25f,25f}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setFixedLayout();
            table.setFont(font);
            table.addCell(new Cell(1,4).add(new Paragraph("PREMIK OSNOVNIH SREDSTEV")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("Invent. št")));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("Opis").setTextAlignment(TextAlignment.CENTER)));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("Od kod").setTextAlignment(TextAlignment.CENTER)));//.setBorder(Border.NO_BORDER);
            table.addCell(new Cell(1,1).add(new Paragraph("Kam").setTextAlignment(TextAlignment.CENTER)));//.setBorder(Border.NO_BORDER);
            //DODAJ IZ TABELE PORABLJENI MATERIAL
            table.setMarginBottom(10);
            rowContent = null;
            tl = (TableLayout) getActivity().findViewById(R.id.tablePremikOS);
            for (int i = 0; i < tl.getChildCount(); i++) {
                TableRow row = (TableRow) tl.getChildAt(i);
                int test = tl.getChildCount();
                if (tl.getChildCount() >= 1) {
                    for (int j = 0; j < row.getChildCount(); j++) {
                        TextView currentCell = (TextView) row.getChildAt(j);
                        rowContent = currentCell.getText().toString();
                        table.addCell(new Cell(1, 1).add(new Paragraph(rowContent)));
                    }
                    //Log.d("Content of Row is:", rowContent);
                }
            }
            document.add(table);

            Table podpisi = new Table(UnitValue.createPercentArray(new float[]{1,10,1,10,1}));
            podpisi.setWidth(UnitValue.createPercentValue(100));
            podpisi.setFixedLayout();
            podpisi.setFont(font);
            podpisi.setMarginTop(50);

            Cell ce01 = new Cell(1,1);
            ce01.add(new Paragraph(""));
            ce01.setBorder(Border.NO_BORDER);
            ce01.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce01);

            Cell ce = new Cell(1,1);
            ce.add(new Paragraph("Odgovorna oseba izvajalca"));
            ce.setBorder(Border.NO_BORDER);
            ce.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce);

            Cell ce02 = new Cell(1,1);
            ce02.add(new Paragraph(""));
            ce02.setBorder(Border.NO_BORDER);
            ce02.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce02);

            Cell ce2 = new Cell(1,1);
            ce2.add(new Paragraph("Odgovorna oseba naročnika"));
            ce2.setBorder(Border.NO_BORDER);
            ce2.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce2);

            Cell ce03 = new Cell(1,1);
            ce03.add(new Paragraph(""));
            ce03.setBorder(Border.NO_BORDER);
            ce03.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce03);

            Cell ce04 = new Cell(1,1);
            ce04.add(new Paragraph(""));
            ce04.setBorder(Border.NO_BORDER);
            ce04.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce04);

            Cell ce3 = new Cell(1,1);
            EditText etIzvajalecPodpis = (EditText) getActivity().findViewById(R.id.editTextTextPersonName13);
            ce3.add(new Paragraph(etIzvajalecPodpis.getText().toString()));
            ce3.setBorder(Border.NO_BORDER);
            ce3.setBorderBottom(new SolidBorder(0.5f));
            ce3.setMarginLeft(10);
            ce3.setMarginRight(10);
            ce3.setPaddingLeft(10);
            ce3.setPaddingRight(10);
            ce3.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce3);

            Cell ce05 = new Cell(1,1);
            ce05.add(new Paragraph(""));
            ce05.setBorder(Border.NO_BORDER);
            ce05.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce05);

            Cell ce4 = new Cell(1,1);
            EditText etNarocnikPodpis = (EditText) getActivity().findViewById(R.id.editTextTextPersonName14);

            ce4.add(new Paragraph(etNarocnikPodpis.getText().toString()));
            ce4.setBorder(Border.NO_BORDER);
            ce4.setBorderBottom(new SolidBorder(0.5f));
            ce4.setMarginLeft(10);
            ce4.setMarginRight(10);
            ce4.setPaddingLeft(10);
            ce4.setPaddingRight(10);
            ce4.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce4);

            Cell ce06 = new Cell(1,1);
            ce06.add(new Paragraph(""));
            ce06.setBorder(Border.NO_BORDER);
            ce06.setTextAlignment(TextAlignment.CENTER);
            podpisi.addCell(ce06);


            //podpisi.addCell(new Cell(1,1).add(new Paragraph("Odgovorna oseba izvajalca").setFontSize(12).setTextAlignment(TextAlignment.CENTER))).setBorder(Border.NO_BORDER);
            //podpisi.addCell(new Cell(1,1).add(new Paragraph("Odgovorna oseba naročnika").setFontSize(12).setTextAlignment(TextAlignment.CENTER))).setBorder(Border.NO_BORDER);
            //podpisi.addCell(new Cell(1,1).add(new Paragraph("_________________________").setFontSize(18).setTextAlignment(TextAlignment.CENTER))).setBorder(Border.NO_BORDER);
            //podpisi.addCell(new Cell(1,1).add(new Paragraph("_________________________").setFontSize(18).setTextAlignment(TextAlignment.CENTER))).setBorder(Border.NO_BORDER);
            podpisi.setBorder(Border.NO_BORDER);
            document.add(podpisi);

            document.add(image2);

            canvas = new PdfCanvas(document.getPdfDocument().getPage(2));
            canvas.beginText().setFontAndSize(font,5)
                    .moveText(36, 20)
                    .showText("Vpis v sodni register: Okrožno sodišče v Ljubljani, pod vložno štev. 1/05773/00, osnovni kapital: 52.240.977,04 EUR, ID za DDV SI80267432")
                    .endText();
            canvas.release();

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



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SNPetrolViewModel.class);
        // TODO: Use the ViewModel
    }





}
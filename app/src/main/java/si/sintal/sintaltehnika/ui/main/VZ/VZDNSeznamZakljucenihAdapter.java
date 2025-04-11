package si.sintal.sintaltehnika.ui.main.VZ;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.DialogPodatkiOSNActivity;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZ;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZPeriodika;
import si.sintal.sintaltehnika.ui.main.DialogPodatkiOSNFragment;
import si.sintal.sintaltehnika.ui.main.SNArtikel;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class VZDNSeznamZakljucenihAdapter extends ArrayAdapter<DelovniNalogVZ> implements Filterable {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DelovniNalogVZ> originalData;
    private ArrayList<DelovniNalogVZ> seznamDNjev;
    VZDNSeznamZakljucenihAdapter.ItemFilter mFilter;
    private int mySNid;
    private String  userID;
    private int pos;
    int serverResponseCode = 0;
    private String  tehnikID;

    //private static final String driver = "com.mysql.cj.jdbc.Driver";
    //private static final String URL = "jdbc:mysql://tehnika.sintal.si:3306/sintal87_tehnika_tablet?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
    //private static final String USER = "sintal87_tehnika";
    //private static final String PASSWORD = "64AyUev83zg78s4";

    public VZDNSeznamZakljucenihAdapter(Context context, ArrayList<DelovniNalogVZ> seznamDNjev, String userID, String tehnikID) {
        super(context, 0, seznamDNjev);
        this.context = context;
        this.seznamDNjev = new ArrayList<DelovniNalogVZ>();
        this.seznamDNjev.addAll(seznamDNjev);
        this.originalData = new ArrayList<DelovniNalogVZ>();
        this.originalData.addAll(seznamDNjev);
        this.userID = userID;
        this.tehnikID = tehnikID;

    }

    @Override
    public int getCount() {
        return seznamDNjev.size();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView stDNja;
        TextView idDNja;
        TextView nazivNarocnikSNja;
        TextView opisNapakeSNja;
        CheckBox cbOznacen;
        TextView datumZakljucenSN;
        TextView datumZakljucenoSN;
        TextView poslanoSN;

    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        VZDNSeznamZakljucenihAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vz_seznam_zakljucenih_vz, null);
            holder = new VZDNSeznamZakljucenihAdapter.ViewHolder();
            holder.stDNja = (TextView) convertView.findViewById(R.id.labelstevilkaVZDN);
            holder.idDNja = (TextView) convertView.findViewById(R.id.labelidVZ);
            holder.nazivNarocnikSNja = (TextView) convertView.findViewById(R.id.labelVZNarocnik);
            holder.opisNapakeSNja = (TextView) convertView.findViewById(R.id.labelVZOpisNapke);
            holder.datumZakljucenSN = (TextView) convertView.findViewById(R.id.labelidVZDodeljeno);
            holder.datumZakljucenoSN = (TextView) convertView.findViewById(R.id.labelidVZZakljuceno);
            holder.poslanoSN = (TextView) convertView.findViewById(R.id.labelidVZPoslano);
            //holder.cbOznacen = (CheckBox) convertView.findViewById(R.id.cbDodeli);
            //holder.continent = (TextView) convertView.findViewById(R.id.continent);
            //holder.region = (TextView) convertView.findViewById(R.id.region);

            convertView.setTag(holder);

        } else {
            holder = (VZDNSeznamZakljucenihAdapter.ViewHolder) convertView.getTag();
        }

        DelovniNalogVZ n = seznamDNjev.get(position);
        //if (n.getVodjaNaloga().equals(""))
        //{
        //    holder.stDNja.setText(n.getDelovniNalog().toString());
        //}
        //else{
            //holder.stDNja.setText(n.getDelovniNalog().toString()+"; "+n.getVodjaNaloga().toString());
        //}

        DatabaseHandler db = new DatabaseHandler(getContext());
        DelovniNalogVZPeriodika dnPer = new DelovniNalogVZPeriodika();
        dnPer = db.vrniVZDNPre(n.getDelovniNalog(),0,"");
        holder.stDNja.setText(n.getDelovniNalog().toString());
        holder.idDNja.setText(Integer.toString(n.getid()));
        //holder.nazivNarocnikSNja.setText(n.getNarocnik().toString()+", "+n.getNarocnikNaslov().toString());
        holder.nazivNarocnikSNja.setText(n.getNarocnik() +", "+n.getNarocnikNaslov());
        holder.opisNapakeSNja.setText(n.getObjekt() +", "+n.getObjektNaslov());
        //holder.opisNapakeSNja.setText(n.getOpis().toString());
        String datumDod = "Datum izvedbe: " + n.getDATUM_IZVEDBE().toString();
        holder.datumZakljucenSN.setText(datumDod);

        //datumDod = "Zaključeno: " + n.getDatumKonec().toString();
        //holder.datumZakljucenoSN.setText(datumDod);

        String poslanoStr = db.vrniPoslano(n.getid(),"");
        holder.poslanoSN.setText("Poslano : "+poslanoStr);



        Button bPodatkiODN = (Button) convertView.findViewById(R.id.bPodatkiSN);
        bPodatkiODN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nadzor n = (Nadzor) adapter.getItem(position);

                Bundle bundle = new Bundle();
                //DatabaseHandler db = new DatabaseHandler(getContext());
                //ServisniNalog sn = new ServisniNalog();
                String datumSNja = n.getDATUM_IZVEDBE();
                String stSNja = n.getDelovniNalog();
                String opisSNja = n.getOpomba();
                //String vodjaSNja = n.getVodjaNaloga();
                //String odgOsebaSNja = n.getOdgovornaOseba();
                DelovniNalogVZ dn = new DelovniNalogVZ();
                dn = db.vrniVZDN(n.getDelovniNalog());
                String narocnikSNja = dn.getNarocnik() + ", " + dn.getNarocnikNaslov();
                int id = n.getid();

                DialogPodatkiOSNFragment myFrag = new DialogPodatkiOSNFragment();
                myFrag.setArguments(bundle);
                Intent intent = new Intent(getContext(), DialogPodatkiOSNActivity.class);
                intent.putExtra("stSNja", stSNja);
                intent.putExtra("idSNja", id);
                intent.putExtra("datumSNja", datumSNja);
                intent.putExtra("opisSNja", opisSNja);
                //intent.putExtra("vodjaSNja", vodjaSNja);
                //intent.putExtra("odgOsebaSNja", odgOsebaSNja);
                intent.putExtra("narocnikSNja", narocnikSNja);
                getContext().startActivity(intent);
            }
        });
        // Return the completed view to render on screen

        Button bPosljiVMysql = (Button) convertView.findViewById(R.id.bPrenosSN);
        bPosljiVMysql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JSONObject sendSn = new JSONObject();
                if (n.getStatus() == 0) {

                    try {
/*
                        DatabaseHandler db = new DatabaseHandler(getContext());
                        ArrayList<SNArtikel> seznamSNArtikli;
                        mySNid = n.getid();
                        pos = position;
                        seznamSNArtikli = db.GetSeznamArtikliIzpisDNSNUporabnik(Integer.parseInt(userID),Integer.parseInt(tehnikID),1,n.getid());
                        String datum = "";
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        datum = dtf.format(now);
                        Gson gson = new Gson();
                        String json = ""; //gson.toJson(n);
                        //json = json.replace("\"{\\\"","")

                        json = gson.toJson(seznamSNArtikli);
                        String storePath1 = getContext().getFilesDir() + "/" + userID + "-sn-art.json";
                        File file1 = new File(storePath1);
                        if (file1.exists() == true)
                        {
                            file1.delete();
                        }
                        FileWriter fw1 = new FileWriter(file1);
                        BufferedWriter bw1 = new BufferedWriter(fw1);
                        bw1.append(json);
                        bw1.close();
                        fw1.close();
                        file1.getTotalSpace();

                        json = gson.toJson(n);
                        json = "{\"sn\":[" + json + "]}";
                        //sendSn.put("sn",json);
                        String storePath = getContext().getFilesDir() + "/" + userID + "-sn.json";
                        File file = new File(storePath);
                        if (file.exists() == true)
                        {
                            file.delete();
                        }
                        FileWriter fw = new FileWriter(file);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.append(json);
                        bw.close();
                        fw.close();

                        //json = gson.toJson(jsonArray);
                        JSONArray jsonObj = new JSONArray();
                        jsonObj = db.vrniEmailLog(n.getid(),userID);
                        json = jsonObj.toString();
                        //json = gson.toJson(jsonObj);
                        String storePath2 = getContext().getFilesDir() + "/" + userID + "-sn-email.json";
                        File file2 = new File(storePath2);
                        if (file2.exists() == true)
                        {
                            file2.delete();
                        }
                        FileWriter fw2 = new FileWriter(file2);
                        BufferedWriter bw2 = new BufferedWriter(fw2);
                        bw2.append(json);
                        bw2.close();
                        fw2.close();
                        //ArrayList<String> parameters = new ArrayList<String>();
                        //parameters.add(String.valueOf(n.getid()));
                        //parameters.add("X");
                        //parameters.add(datum);
                        //parameters.add(bArray);

                         */

                        new VZDNSeznamZakljucenihAdapter.SendOnWeb().execute();


                        //new InfoAsyncTask().execute();
                    } catch (Exception e) { //(IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getContext(), "Ta SN je že prenešen, prenos ni več mogoč!", Toast.LENGTH_LONG).show();
                }

            }
        });

        return convertView;

    }

    private class SendOnWeb extends AsyncTask {
        String vrni = "";
        /*private byte[] data;
        public SendOnWeb(byte[] data) {
            this.data = data;
        }

         */
        //
        //int idSNja =;
        //public SendOnWeb() {
            //this.data = data;
        @Override
        protected Object doInBackground(Object... arg0) {

            String StoredPath2 = "";
            StoredPath2 = getContext().getFilesDir()+"/"+userID+"-sn-art.json";
            try {
                uploadFile(StoredPath2);
                String rezultat = refreshDNArtikliMysql(userID);
                if (rezultat.equals("\tInserted all articles") == true)
                {
                    //DatabaseHandler db = new DatabaseHandler(getContext());
                    //db.updateSNPoslano(arrayLists[0].get(0).toString(),"X","");
                }
            }
            catch (Exception e){

            }


            String StoredPath = "";
            StoredPath = getContext().getFilesDir()+"/"+userID+"-sn.json";
            try {
                uploadFile(StoredPath);
                //String rezultat = "";
                String rezultat = refreshSNMysql(userID);
                if ( (rezultat.equals("\tUpdate succesful") == true)
                || (rezultat.equals("\tAlready transfered") == true) )
                {
                DatabaseHandler db = new DatabaseHandler(getContext());

                db.updateSNPoslano(String.valueOf(mySNid),"X","");
                }
            }
            catch (Exception e){

            }

            String StoredPath3 = "";
            StoredPath3 = getContext().getFilesDir()+"/"+userID+"-sn-email.json";
            try {
                uploadFile(StoredPath3);
                String rezultat = refreshDNEmailLogMysql(userID);
                if (rezultat.equals("\tInserted all logs") == true)
                {
                    //DatabaseHandler db = new DatabaseHandler(getContext());
                    //db.updateSNPoslano(arrayLists[0].get(0).toString(),"X","");
                }
            }
            catch (Exception e){

            }

            String StoredPath4 = "";
            StoredPath4 = getContext().getFilesDir()+"/"+String.valueOf(mySNid)+".bmp";
            try {
                uploadFile(StoredPath4);
                String rezultat = refreshDNPodpisBlobMysql(String.valueOf(mySNid));
                if (rezultat.equals("Update succesful") == true)
                {
                    //DatabaseHandler db = new DatabaseHandler(getContext());
                    //db.updateSNPoslano(arrayLists[0].get(0).toString(),"X","");
                }
            }
            catch (Exception e){

            }


            //notifyDataSetChanged();
            //updatePoslano() ; arrayLists[0].get(0).toString(), arrayLists[0].get(1).toString(),  arrayLists[0].get(2).toString()
            //Toast.makeText(getContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (vrni.equals("\tUpdate succesful") == true)
            {
                Toast.makeText(getContext(), "Uspešno ste prenesli podatke o SN", Toast.LENGTH_LONG).show();
                seznamDNjev.remove(seznamDNjev.get(pos));
                notifyDataSetChanged();

            }
            else if (vrni.equals("\tAlready transfered") == true)
            {
                Toast.makeText(getContext(), "Ta DN je že prenešen", Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
            }
            else
            {
                //Toast.makeText(getContext(), "Prišlo je do napake pri pošiljanju podatkov, poizkusite ponovno!", Toast.LENGTH_LONG).show();
            }
            //notifyDataSetChanged();
        }

    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;
        /*
        if (sourceFileUri.equals("") == true)
        {
            fileName = getContext().getFilesDir()+"/"+user+"-sn-art.json";
        }
        else
        {
            fileName = sourceFileUri;
        }
*/
        HttpURLConnection htpconn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(fileName);

        if (sourceFile.exists() == false) {
        //if (0 > 1) {
            return 0;
        }
        else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                //FileInputStream fileInputStream = new FileInputStream("/data/data/si.sintal.sintaltehnika/files/6-sn-artiki.json");

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
                dos.flush();
                dos.close();
                fileInputStream.close();

            } catch(MalformedURLException ex){
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch(Exception e){

                //dialog.dismiss(); /data/data/si.sintal.sintaltehnika/files/6-sn-artiki.json
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


    public String refreshSNMysql(String myUserID) {

        String result = "";
        //String fileName = sourceFileUri;

        HttpURLConnection htpconn2 = null;
        try {

            // open a URL connection to the Servlet
            //FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL("https://www.sintal.si/tehnika/postPodatkiSN.php?jsonFile=" + myUserID + "-sn.json");
            String username = "sintal_teh";
            String password = "mCuSTArQ*PdWAH#7-posodobiSN";
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));

            htpconn2 = (HttpURLConnection) url.openConnection();
            htpconn2.setDoInput(true); // Allow Inputs
            htpconn2.setDoOutput(true); // Allow Outputs
            htpconn2.setUseCaches(false); // Don't use a Cached Copy
            htpconn2.setRequestMethod("POST");
            htpconn2.setRequestProperty("Connection", "Keep-Alive");
            htpconn2.setRequestProperty("ENCTYPE", "multipart/form-data");
            htpconn2.setRequestProperty("Authorization", basicAuth);
            htpconn2.connect();

            if (htpconn2.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(htpconn2.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String temp;

                while ((temp = reader.readLine()) != null) {
                    stringBuilder.append(temp);
                }
                result = stringBuilder.toString();
            } else {
                result = "error";
            }
            htpconn2.disconnect();


        }
        catch (Exception e){
            e.printStackTrace();
        }




        return result;
    }

    public String refreshDNArtikliMysql(String myUserID) {

        String result = "";
        //String fileName = sourceFileUri;

        HttpURLConnection htpconn3 = null;
        try {

            // open a URL connection to the Servlet /data/user/0/si.sintal.sintaltehnika/files/6-sn-artiki.json
            //FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL("https://www.sintal.si/tehnika/postPodatkiArtikliSN.php?jsonFile=" + myUserID + "-sn-art.json");
            String username = "sintal_teh";
            String password = "mCuSTArQ*PdWAH#7-posodobiSN";
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));

            htpconn3 = (HttpURLConnection) url.openConnection();
            htpconn3.setDoInput(true); // Allow Inputs
            htpconn3.setDoOutput(true); // Allow Outputs
            htpconn3.setUseCaches(false); // Don't use a Cached Copy
            htpconn3.setRequestMethod("POST");
            htpconn3.setRequestProperty("Connection", "Keep-Alive");
            htpconn3.setRequestProperty("ENCTYPE", "multipart/form-data");
            htpconn3.setRequestProperty("Authorization", basicAuth);
            htpconn3.connect();

            if (htpconn3.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(htpconn3.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String temp;

                while ((temp = reader.readLine()) != null) {
                    stringBuilder.append(temp);
                }
                result = stringBuilder.toString();
            } else {
                result = "error";
            }
            htpconn3.disconnect();


        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String refreshDNEmailLogMysql(String myUserID) {

        String result = "";
        //String fileName = sourceFileUri;

        HttpURLConnection htpconn4 = null;
        try {

            // open a URL connection to the Servlet
            //FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL("https://www.sintal.si/tehnika/postPodatkiSNEmail.php?jsonFile=" + myUserID + "-sn-email.json");
            String username = "sintal_teh";
            String password = "mCuSTArQ*PdWAH#7-posodobiSN";
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));

            htpconn4 = (HttpURLConnection) url.openConnection();
            htpconn4.setDoInput(true); // Allow Inputs
            htpconn4.setDoOutput(true); // Allow Outputs
            htpconn4.setUseCaches(false); // Don't use a Cached Copy
            htpconn4.setRequestMethod("POST");
            htpconn4.setRequestProperty("Connection", "Keep-Alive");
            htpconn4.setRequestProperty("ENCTYPE", "multipart/form-data");
            htpconn4.setRequestProperty("Authorization", basicAuth);
            htpconn4.connect();

            if (htpconn4.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(htpconn4.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String temp;

                while ((temp = reader.readLine()) != null) {
                    stringBuilder.append(temp);
                }
                result = stringBuilder.toString();
            } else {
                result = "error";
            }
            htpconn4.disconnect();


        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String refreshDNPodpisBlobMysql(String snID) {

        String result = "";
        //String fileName = sourceFileUri;

        HttpURLConnection htpconn5 = null;
        try {

            // open a URL connection to the Servlet
            //FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL("https://www.sintal.si/tehnika/updateSNBlobPodpis.php?SNid="+snID);
            String username = "sintal_teh";
            String password = "mCuSTArQ*PdWAH#7-updatePodpis";
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));

            htpconn5 = (HttpURLConnection) url.openConnection();
            htpconn5.setDoInput(true); // Allow Inputs
            htpconn5.setDoOutput(true); // Allow Outputs
            htpconn5.setUseCaches(false); // Don't use a Cached Copy
            htpconn5.setRequestMethod("POST");
            htpconn5.setRequestProperty("Connection", "Keep-Alive");
            htpconn5.setRequestProperty("ENCTYPE", "multipart/form-data");
            htpconn5.setRequestProperty("Authorization", basicAuth);
            htpconn5.connect();

            if (htpconn5.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(htpconn5.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String temp;

                while ((temp = reader.readLine()) != null) {
                    stringBuilder.append(temp);
                }
                result = stringBuilder.toString();
            } else {
                result = "error";
            }
            htpconn5.disconnect();


        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public void refreshList() {
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new VZDNSeznamZakljucenihAdapter.ItemFilter();
        }
        //return valueFilter;
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<ServisniNalog> filterList = new ArrayList<ServisniNalog>();

                for (int i = 0; i < originalData.size(); i++) {
                    if ((originalData.get(i).getOpomba().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                synchronized (this) {
                    results.count = originalData.size();
                    results.values = originalData;
                }
            }
            //notifyDataSetChanged();
            return results;


        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //beanList = (ArrayList<Bean>) results.values;
            //notifyDataSetChanged();
            seznamDNjev = (ArrayList<DelovniNalogVZ>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = seznamDNjev.size(); i < l; i++)
                add(seznamDNjev.get(i));
            notifyDataSetInvalidated();
        }

    }

}
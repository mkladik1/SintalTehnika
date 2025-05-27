package si.sintal.sintaltehnika.ui.main;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
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
import java.util.Calendar;
import java.util.Date;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.GlavnoOkno;
import si.sintal.sintaltehnika.MainActivity;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SN.SNSeznamZakljucenihSNAdapter;

public class ServisFragment extends Fragment {

    private ServisViewModel mViewModel;
    private String userID;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;
    static DatabaseHandler db;
    private int mySNid;
    int serverResponseCode = 0;

    public static ServisFragment newInstance() {
        return new ServisFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.servis_fragment, container, false);
        Intent intent= getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        tehnikNaziv = intent.getStringExtra("userName");
        tehnikEmail = intent.getStringExtra("email");
        tehnikAdminDostop = intent.getStringExtra("admin");
        servis = intent.getStringExtra("servis");
        montaza = intent.getStringExtra("montaza");
        vzdrzevanje = intent.getStringExtra("vzdrzevanje");
        tehnikID = intent.getStringExtra("tehnikID");


        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.snTabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager2 viewPager =(ViewPager2) v.findViewById(R.id.SNViewPager);
        SNPagerAdapter tabsAdapter = new SNPagerAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(tabsAdapter);
        viewPager.setUserInputEnabled(false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        Button bNazaj = (Button) v.findViewById(R.id.bSNToGlavno);
        bNazaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GlavnoOkno.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userName", tehnikNaziv);
                intent.putExtra("email", tehnikEmail);
                intent.putExtra("admin",tehnikAdminDostop);
                intent.putExtra("servis",servis);
                intent.putExtra("montaza",montaza);
                intent.putExtra("vzdrzevanje",vzdrzevanje);
                startActivity(intent);
            }
        });




        return v; //inflater.inflate(R.layout.servis_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServisViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            //Toast.makeText(getContext(),"Povezavi v omrežje!",LENGTH_SHORT).show();
            db = new DatabaseHandler(getContext());
            ArrayList<ServisniNalog> zakljuceniSN;
            zakljuceniSN = db.GetSeznamSNUporabnik(userID,"Z");
            for(int i=0;i<zakljuceniSN.size();i++)
            {
                ServisniNalog sn = new ServisniNalog();
                sn = zakljuceniSN.get(i);
                mySNid = sn.getid();
                ArrayList<SNArtikel> seznamSNArtikli;
                //mySNid = sn.getid();
                //os = position;
                seznamSNArtikli = db.GetSeznamArtikliIzpisDNSNUporabnik(Integer.parseInt(userID),Integer.parseInt(tehnikID),1,sn.getid());
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
                FileWriter fw1 = null;
                try {
                    fw1 = new FileWriter(file1);
                    BufferedWriter bw1 = new BufferedWriter(fw1);
                    bw1.append(json);
                    bw1.close();
                    fw1.close();
                    file1.getTotalSpace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                json = gson.toJson(sn);
                json = "{\"sn\":[" + json + "]}";
                //sendSn.put("sn",json);
                String storePath = getContext().getFilesDir() + "/" + userID + "-sn.json";
                File file = new File(storePath);
                if (file.exists() == true)
                {
                    file.delete();
                }
                FileWriter fw = null;
                try {
                    fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.append(json);
                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                JSONArray jsonObj = new JSONArray();
                jsonObj = db.vrniEmailLog(sn.getid(),userID);
                json = jsonObj.toString();
                //json = gson.toJson(jsonObj);
                String storePath2 = getContext().getFilesDir() + "/" + userID + "-sn-email.json";
                File file2 = new File(storePath2);
                if (file2.exists() == true)
                {
                    file2.delete();
                }
                FileWriter fw2 = null;
                try {
                    fw2 = new FileWriter(file2);
                    BufferedWriter bw2 = new BufferedWriter(fw2);
                    bw2.append(json);
                    bw2.close();
                    fw2.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                //new SendOnWeb().execute();

            }

        } else {
            // display error
            Toast.makeText(getContext(),"Brez povezave!",LENGTH_SHORT).show();
        }


    }

    private class SendOnWeb extends AsyncTask {
        String vrni = "";
        @Override
        protected Object doInBackground(Object... arg0) {



            String StoredPath2 = "";
            StoredPath2 = getContext().getFilesDir()+"/"+userID+"-sn-art.json";
            try {
                uploadFile(StoredPath2);
                String rezultat = refreshSNArtikliMysql(userID);
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

            String StoredPath6 = "";
            StoredPath6 = getContext().getFilesDir()+"/"+userID+"-sn-email.json";
            try {
                uploadFile(StoredPath6);
                String rezultat = refreshSNEmailLogMysql(userID);
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
                String rezultat = refreshSNPodpisBlobMysql(String.valueOf(mySNid));
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
                //seznamSNjev.remove(seznamSNjev.get(pos));
                //notifyDataSetChanged();

            }
            else if (vrni.equals("\tAlready transfered") == true)
            {
                Toast.makeText(getContext(), "Ta SN je že prenešen", Toast.LENGTH_LONG).show();
                //notifyDataSetChanged();
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

                java.net.URL url = new URL("https://www.sintal.si/tehnika/uploadFile.php");
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
            java.net.URL url = new URL("https://www.sintal.si/tehnika/postPodatkiSN.php?jsonFile=" + myUserID + "-sn.json");
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

    public String refreshSNArtikliMysql(String myUserID) {

        String result = "";
        //String fileName = sourceFileUri;

        HttpURLConnection htpconn3 = null;
        try {

            // open a URL connection to the Servlet /data/user/0/si.sintal.sintaltehnika/files/6-sn-artiki.json
            //FileInputStream fileInputStream = new FileInputStream(sourceFile);
            java.net.URL url = new URL("https://www.sintal.si/tehnika/postPodatkiArtikliSN.php?jsonFile=" + myUserID + "-sn-art.json");
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

    public String refreshSNEmailLogMysql(String myUserID) {

        String result = "";
        //String fileName = sourceFileUri;

        HttpURLConnection htpconn4 = null;
        try {

            // open a URL connection to the Servlet
            //FileInputStream fileInputStream = new FileInputStream(sourceFile);
            java.net.URL url = new URL("https://www.sintal.si/tehnika/postPodatkiSNEmail.php?jsonFile=" + myUserID + "-sn-email.json");
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

    public String refreshSNPodpisBlobMysql(String snID) {

        String result = "";
        //String fileName = sourceFileUri;

        HttpURLConnection htpconn5 = null;
        try {

            // open a URL connection to the Servlet
            //FileInputStream fileInputStream = new FileInputStream(sourceFile);
            java.net.URL url = new URL("https://www.sintal.si/tehnika/updateSNBlobPodpis.php?SNid="+snID);
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

}
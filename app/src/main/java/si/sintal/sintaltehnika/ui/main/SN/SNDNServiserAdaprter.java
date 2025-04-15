package si.sintal.sintaltehnika.ui.main.SN;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SNArtikel;
import si.sintal.sintaltehnika.ui.main.Serviser;

public class SNDNServiserAdaprter extends ArrayAdapter<Serviser> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Serviser> seznamSNServiserji;
    private String tehnikID;
    private String userID;
    private int SNID;
    private String SNDN;

    public SNDNServiserAdaprter(@NonNull Context context, ArrayList<Serviser> seznamSNServiserji, int SNId, String SNDn, String userId, String tehnikId) {
        super(context, 0, seznamSNServiserji);
        this.context = context;
        this.seznamSNServiserji = new ArrayList<Serviser>();
        this.seznamSNServiserji.addAll(seznamSNServiserji);
        this.tehnikID = tehnikId;
        this.userID = userId;
        this.SNID = SNId;
        this.SNDN = SNDn;
    }

    @Override
    public int getCount() {
        return seznamSNServiserji.size();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView stServiser;
        TextView nazivServiser;
        Button bDodajServiser;


    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        SNDNServiserAdaprter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sn_dn_serviser, null);
            holder = new SNDNServiserAdaprter.ViewHolder();
            holder.stServiser = (TextView) convertView.findViewById(R.id.tvSNServiserSt);;
            holder.nazivServiser = (TextView) convertView.findViewById(R.id.tvSNServiserNaziv);

            convertView.setTag(holder);

        } else {
            holder = (SNDNServiserAdaprter.ViewHolder) convertView.getTag();
        }

        Serviser n = seznamSNServiserji.get(position);
        holder.stServiser.setText((n.getstServiser()));
        holder.nazivServiser.setText(n.getNazivServiser().toString());

        Button bDodajServiserja = (Button) convertView.findViewById(R.id.bSNServiserDodaj);
        bDodajServiserja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandler db = new DatabaseHandler(getContext());
                //int stSNDN = Integer.parseInt(SNID);
                //String stSNja = SNDN;
                String snNo_ = n.getstServiser();
                //int vrstaId = vrstaID; //
                int upoId = Integer.parseInt(userID);
                int tehnikId = Integer.parseInt(tehnikID);
                //db.dodajSNServiserDodatni(SNID,SNDN,stServiserja);
                seznamSNServiserji.remove(position);
                //refreshList();
            }
        });
        // Return the completed view to render on screen

        return convertView;

    }
}

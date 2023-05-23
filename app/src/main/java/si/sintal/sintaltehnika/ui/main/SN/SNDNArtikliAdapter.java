package si.sintal.sintaltehnika.ui.main.SN;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SNArtikel;

public class SNDNArtikliAdapter extends ArrayAdapter<SNArtikel>{


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SNArtikel> seznamSNArtikli;
    private String tehnikID;
    private String userID;
    private String SNID;
    private String SNDN;
    private int vrstaID;



    public SNDNArtikliAdapter(Context context, ArrayList<SNArtikel> seznamSNArtikli, String SNId, String SNDn, String userId, String tehnikId, Integer vrstaId)
    {
        super(context, 0, seznamSNArtikli);
        this.context = context;
        this.seznamSNArtikli = new ArrayList<SNArtikel>();
        this.seznamSNArtikli.addAll(seznamSNArtikli);
        this.tehnikID = tehnikId;
        this.userID = userId;
        this.SNID = SNId;
        this.SNDN = SNDn;
        this.vrstaID = vrstaId;

    }


    @Override
    public int getCount() {
        return seznamSNArtikli.size();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView idSNja;
        TextView nazivArtikel;
        TextView merkaEnotaArtikel;
        Button bDodajArtikel;

    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        SNDNArtikliAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sn_dn_artikel, null);
            holder = new SNDNArtikliAdapter.ViewHolder();
            holder.idSNja = (TextView) convertView.findViewById(R.id.tvSNArtikelSt);;
            holder.nazivArtikel = (TextView) convertView.findViewById(R.id.tvSNArtikelNaziv);
            holder.merkaEnotaArtikel = (TextView) convertView.findViewById(R.id.tvSNArtikelEnota);
            holder.bDodajArtikel = (Button) convertView.findViewById(R.id.bSNArtikelDodaj);

            convertView.setTag(holder);

        } else {
            holder = (SNDNArtikliAdapter.ViewHolder) convertView.getTag();
        }

        SNArtikel n = seznamSNArtikli.get(position);
        holder.idSNja.setText((n.getid()));
        holder.nazivArtikel.setText(n.getnaziv().toString());
        holder.merkaEnotaArtikel.setText(n.getmerskaEnota().toString());
        holder.bDodajArtikel = (Button) convertView.findViewById(R.id.bSNArtikelDodaj);

        Button bDodajArtikel = (Button) convertView.findViewById(R.id.bSNArtikelDodaj);
        bDodajArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandler db = new DatabaseHandler(getContext());
                int stSNDN = Integer.parseInt(SNID);
                String stSNja = SNDN;
                String snNo_ = n.getid();
                int vrstaId = vrstaID; //
                int upoId = Integer.parseInt(userID);
                int tehnikId = Integer.parseInt(tehnikID);
                db.deleteSNArtikelUserTehnik(stSNDN,stSNja,snNo_,vrstaId,upoId,tehnikId);
                seznamSNArtikli.remove(position);
                refreshList();
            }
        });
        // Return the completed view to render on screen

        return convertView;

    }

    public void refreshList() {
        notifyDataSetChanged();
    }



}


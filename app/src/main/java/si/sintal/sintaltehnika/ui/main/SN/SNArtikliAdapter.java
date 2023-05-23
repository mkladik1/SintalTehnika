package si.sintal.sintaltehnika.ui.main.SN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.DialogPodatkiOSNActivity;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.DialogPodatkiOSNFragment;
import si.sintal.sintaltehnika.ui.main.SNArtikel;
import si.sintal.sintaltehnika.ui.main.SNPagerAdapter;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class SNArtikliAdapter extends ArrayAdapter<SNArtikel> implements Filterable {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SNArtikel> originalData;
    private ArrayList<SNArtikel> seznamSNArtikli;
    private String tehnikID;
    private String userID;
    private String SNID;
    private String SNDN;
    private int vrstaID;
    SNArtikliAdapter.ItemFilter mFilter;


    public SNArtikliAdapter(Context context, ArrayList<SNArtikel> seznamSNArtikli, String SNId, String SNDn, String userId, String tehnikId, Integer vrstaId) {
        super(context, 0, seznamSNArtikli);
        this.context = context;
        this.seznamSNArtikli = new ArrayList<SNArtikel>();
        this.seznamSNArtikli.addAll(seznamSNArtikli);
        this.originalData = new ArrayList<SNArtikel>();
        this.originalData.addAll(seznamSNArtikli);
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
        Button  bDodajArtikel;

    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        SNArtikliAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sn_artikel, null);
            holder = new SNArtikliAdapter.ViewHolder();
            holder.idSNja = (TextView) convertView.findViewById(R.id.tvSNArtikelSt);;
            holder.nazivArtikel = (TextView) convertView.findViewById(R.id.tvSNArtikelNaziv);
            holder.merkaEnotaArtikel = (TextView) convertView.findViewById(R.id.tvSNArtikelEnota);
            holder.bDodajArtikel = (Button) convertView.findViewById(R.id.bSNArtikelDodaj);

            convertView.setTag(holder);

        } else {
            holder = (SNArtikliAdapter.ViewHolder) convertView.getTag();
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
                db.insertSNArtikelUserTehnik(stSNDN,stSNja,snNo_,vrstaId,upoId,tehnikId);

            }
        });
        // Return the completed view to render on screen

        return convertView;

    }

    public void refreshList() {
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SNArtikliAdapter.ItemFilter();
        }
        //return valueFilter;
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<SNArtikel> filterList = new ArrayList<SNArtikel>();

                for (int i = 0; i < originalData.size(); i++) {
                    if ((originalData.get(i).getnazivIskanje().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        SNArtikel sn = new SNArtikel();
                        sn.setid(originalData.get(i).getid());
                        sn.setnaziv(originalData.get(i).getnaziv());
                        sn.setnazivIskanje(originalData.get(i).getnazivIskanje());
                        sn.setmerskaEnota(originalData.get(i).getmerskaEnota());
                        sn.setKratkaOznaka(originalData.get(i).getKratkaOznaka());
                        filterList.add(sn);

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

            return results;


        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            seznamSNArtikli = (ArrayList<SNArtikel>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = seznamSNArtikli.size(); i < l; i++)
                add(seznamSNArtikli.get(i));
            notifyDataSetInvalidated();
        }



    }

}

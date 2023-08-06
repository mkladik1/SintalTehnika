package si.sintal.sintaltehnika.ui.main.SN;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SNArtikel;

public class SNArtikliAdapter extends ArrayAdapter<SNArtikel> implements Filterable {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SNArtikel> originalData;
    private ArrayList<SNArtikel> seznamSNArtikli;
    private String tehnikID;
    private String userID;
    private int SNID;
    private String SNDN;
    private int vrstaID;
    private float kolicina;
    SNArtikliAdapter.ItemFilter mFilter;


    public SNArtikliAdapter(Context context, ArrayList<SNArtikel> seznamSNArtikli, int SNId, String SNDn, String userId, String tehnikId, Integer vrstaId) {
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

    public class ViewHolder {
        TextView idSNja;
        TextView nazivArtikel;
        TextView merkaEnotaArtikel;
        Button  bDodajArtikel;
        TextView kolicinaArtikel;

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
            holder.kolicinaArtikel = (TextView) convertView.findViewById(R.id.tvSNArtikelKolicina2);

            convertView.setTag(holder);

        } else {
            holder = (SNArtikliAdapter.ViewHolder) convertView.getTag();
        }

        SNArtikel n = seznamSNArtikli.get(position);
        holder.idSNja.setText((n.getid()));
        holder.nazivArtikel.setText(n.getnaziv().toString());
        holder.merkaEnotaArtikel.setText(n.getmerskaEnota().toString());
        holder.bDodajArtikel = (Button) convertView.findViewById(R.id.bSNArtikelDodaj);
        holder.kolicinaArtikel.setText(String.valueOf(n.getKolicina()).toString());
        kolicina = n.getKolicina();
        Button bDodajArtikel = (Button) convertView.findViewById(R.id.bSNArtikelDodaj);
        View finalConvertView = convertView;
        bDodajArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //float test = Float.valueOf(holder.kolicinaArtikel.getText().toString());
                DatabaseHandler db = new DatabaseHandler(getContext());
                //int stSNDN = SNID;
                //String stSNja = SNDN;
                String snNo_ = n.getid();
                int vrstaId = vrstaID; //
                int upoId = Integer.parseInt(userID);
                int tehnikId = Integer.parseInt(tehnikID);
                TextView tv =  (TextView) finalConvertView.findViewById(R.id.tvSNArtikelKolicina2);
                String text = tv.getText().toString();
                float kolicina1 = Float.valueOf(text);
                String regal = n.getRegal();
                db.insertSNArtikelUserTehnik(SNID,SNDN,snNo_,vrstaId,upoId,tehnikId, kolicina1, regal);

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
                        sn.setKolicina(originalData.get(i).getKolicina());
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

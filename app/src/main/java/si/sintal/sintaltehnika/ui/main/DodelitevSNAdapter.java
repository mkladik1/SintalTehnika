package si.sintal.sintaltehnika.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.DialogPodatkiOSNActivity;
import si.sintal.sintaltehnika.R;

public class DodelitevSNAdapter extends ArrayAdapter<ServisniNalog> implements Filterable {

    private Context context;
    ServisniNalog seznamSN;
    private LayoutInflater inflater;
    private ArrayList<ServisniNalog> originalData;
    private ArrayList<ServisniNalog> seznamSNjev;
    ItemFilter mFilter;
    public String narocnikNaziv;
    public String narocnikNaslov;
    public int id;
    public String servnisniNalog;
    public String datumZacetek;
    public String vodjaNaloga;

/*
    public class ViewHolder{
        TextView tvStNadzora;
        TextView tvOpis;
        Button bBrisiNadzor;
        Button bUrediNadzor;
        Button bKopirajNadzor;
    }
*/


    public DodelitevSNAdapter(Context context, ArrayList<ServisniNalog> seznamSNjev) {
        super(context, 0, seznamSNjev);
        this.context = context;
        this.seznamSNjev = new ArrayList<ServisniNalog>();
        this.seznamSNjev.addAll(seznamSNjev);
        this.originalData = new ArrayList<ServisniNalog>();
        this.originalData.addAll(seznamSNjev);

    }

    @Override
    public int getCount() {
        return seznamSNjev.size();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView stSNja;
        TextView idSNja;
        TextView nazivNarocnikSNja;
        TextView opisNapakeSNja;
        CheckBox cbOznacen;
        TextView datumDodelitveDNSNja;

    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.seznamdodelisn, null);

            holder = new ViewHolder();
            holder.stSNja = (TextView) convertView.findViewById(R.id.labelstevilkaSN);
            holder.idSNja = (TextView) convertView.findViewById(R.id.labelidSN);
            holder.nazivNarocnikSNja = (TextView) convertView.findViewById(R.id.labelSNNarocnik);
            holder.opisNapakeSNja = (TextView) convertView.findViewById(R.id.labelSNOpisNapke);
            holder.cbOznacen = (CheckBox) convertView.findViewById(R.id.cbDodeli);
            holder.datumDodelitveDNSNja = (TextView) convertView.findViewById(R.id.labelidSNDatum);
            //holder.continent = (TextView) convertView.findViewById(R.id.continent);
            //holder.region = (TextView) convertView.findViewById(R.id.region);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ServisniNalog n = seznamSNjev.get(position);
        if (n.getVodjaNaloga().equals(""))
        {
            holder.stSNja.setText(n.getDelovniNalog().toString());
        }
        else{
            holder.stSNja.setText(n.getDelovniNalog().toString()+"; "+n.getVodjaNaloga().toString());
        }

        holder.idSNja.setText(n.getPripadnost() +" - " +n.getPripadnostNaziv() +", "+    Integer.toString(n.getid()));
        holder.nazivNarocnikSNja.setText(n.getKodaObjekta().toString()+ " - " +n.getNarocnikNaziv().toString()+", "+n.getNarocnikNaslov().toString());
        holder.opisNapakeSNja.setText(n.getOpis().toString());
        holder.datumDodelitveDNSNja.setText("dodeljeno: " + n.getDatumDodelitve());
        holder.cbOznacen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (buttonView.isChecked()) {
                    //if (n.getOznacen() == 1) {
                        //n.setOznacen(1);
                        DatabaseHandler db = new DatabaseHandler(getContext());
                        db.updateSNOznaci(Integer.toString(n.getid()), 1);
                        db.close();
                        n.setOznacen(1);
                    //}
                }
                else {
                    //if (n.getOznacen() == 0) {
                        //n.setOznacen(0);
                        DatabaseHandler db = new DatabaseHandler(getContext());
                        db.updateSNOznaci(Integer.toString(n.getid()), 0);
                        db.close();
                        n.setOznacen(0);
                    //}
                }
                //notifyDataSetChanged();

            }
        });

        if (n.getOznacen() == 1)
        {
            holder.cbOznacen.setChecked(true);
        }
        else {
            holder.cbOznacen.setChecked(false);
        }

        Button bStornoSN = (Button) convertView.findViewById(R.id.bStornirajSN);
        bStornoSN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    db.updateSNStatusAkt(Integer.toString(n.getid()),"S");
                    notifyDataSetChanged();
            }});


        Button bPodatkiOSN = (Button) convertView.findViewById(R.id.bPodatkiSN);
        bPodatkiOSN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nadzor n = (Nadzor) adapter.getItem(position);

                Bundle bundle = new Bundle();
                DatabaseHandler db = new DatabaseHandler(getContext());
                String datumSNja = n.getDatumZacetek();
                String stSNja = n.getDelovniNalog();
                String opisSNja = n.getOpis();
                String vodjaSNja = n.getVodjaNaloga();
                String odgOsebaSNja = n.getOdgovornaOseba();
                String narocnikSNja = n.getNarocnikNaziv() + ", " + n.getNarocnikNaslov();
                int id = n.getid();
                DialogPodatkiOSNFragment myFrag = new DialogPodatkiOSNFragment();
                myFrag.setArguments(bundle);
                Intent intent = new Intent(getContext(), DialogPodatkiOSNActivity.class);
                intent.putExtra("stSNja", stSNja );
                intent.putExtra("idSNja", id );
                intent.putExtra("datumSNja", datumSNja);
                intent.putExtra("opisSNja", opisSNja);
                intent.putExtra("vodjaSNja", vodjaSNja);
                intent.putExtra("odgOsebaSNja", odgOsebaSNja);
                intent.putExtra("narocnikSNja", narocnikSNja);
                getContext().startActivity(intent);

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
            mFilter = new ItemFilter();
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
                    if ((originalData.get(i).getOpis().toUpperCase())
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
            seznamSNjev = (ArrayList<ServisniNalog>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = seznamSNjev.size(); i < l; i++)
                add(seznamSNjev.get(i));
            notifyDataSetInvalidated();
        }



    }


}

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
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.DialogPodatkiOSNActivity;
import si.sintal.sintaltehnika.MainActivity;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ServisActivity;
import si.sintal.sintaltehnika.ui.main.DialogPodatkiOSNFragment;
import si.sintal.sintaltehnika.ui.main.SNPagerAdapter;
import si.sintal.sintaltehnika.ui.main.ServisFragment;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class SeznamUpoSNAdapter extends ArrayAdapter<ServisniNalog> implements Filterable {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ServisniNalog> originalData;
    private ArrayList<ServisniNalog> seznamSNjev;
    ItemFilter mFilter;
    public int id;

    public SeznamUpoSNAdapter(Context context, ArrayList<ServisniNalog> seznamSNjev) {
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
        TextView datumDodeljenSN;

    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.seznamuposn, null);
            holder = new ViewHolder();
            holder.stSNja = (TextView) convertView.findViewById(R.id.labelstevilkaSN);
            holder.idSNja = (TextView) convertView.findViewById(R.id.labelidSN);
            holder.nazivNarocnikSNja = (TextView) convertView.findViewById(R.id.labelSNNarocnik);
            holder.opisNapakeSNja = (TextView) convertView.findViewById(R.id.labelSNOpisNapke);
            holder.datumDodeljenSN = (TextView) convertView.findViewById(R.id.labelidSNDodeljeno);
            //holder.cbOznacen = (CheckBox) convertView.findViewById(R.id.cbDodeli);
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

        holder.idSNja.setText(Integer.toString(n.getid()));
        holder.nazivNarocnikSNja.setText(n.getNarocnikNaziv().toString()+", "+n.getNarocnikNaslov().toString());
        holder.opisNapakeSNja.setText(n.getOpis().toString());
        String datumDod = "dodeljen: " + n.getDatumDodelitve().toString();
        holder.datumDodeljenSN.setText(datumDod);

        Button bIzpolniSN = (Button) convertView.findViewById(R.id.bIzpolniSN);
        bIzpolniSN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment my = FragmentManager.findFragment(v);
                    ViewPager2 viewPager =(ViewPager2) my.getActivity().findViewById(R.id.SNViewPager);
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    int tehnik = Integer.parseInt(db.getTehnikId(n.getVodjaNaloga()));
                    SNPagerAdapter.setParameters(n.getid(),tehnik,0);
                    viewPager.setCurrentItem(1);

                            //.findFragmentById(getItemId(index).toInt())
                }
        }
        );

        Button bPodatkiOSN = (Button) convertView.findViewById(R.id.bPodatkiSN);
        bPodatkiOSN.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               //Nadzor n = (Nadzor) adapter.getItem(position);

                                               Bundle bundle = new Bundle();
                                               //DatabaseHandler db = new DatabaseHandler(getContext());
                                               String datumSNja = n.getDatumZacetek();
                                               String stSNja = n.getDelovniNalog();
                                               String opisSNja = n.getOpis();
                                               String vodjaSNja = n.getVodjaNaloga();
                                               String odgOsebaSNja = n.getOdgovornaOseba();
                                               String narocnikSNja = n.getNarocnikNaziv() + ", " + n.getNarocnikNaslov();

                                               DialogPodatkiOSNFragment myFrag = new DialogPodatkiOSNFragment();
                                               myFrag.setArguments(bundle);
                                               Intent intent = new Intent(getContext(), DialogPodatkiOSNActivity.class);
                                               intent.putExtra("stSNja", stSNja);
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
            mFilter = new SeznamUpoSNAdapter.ItemFilter();
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
/*
                        ServisniNalog n = new ServisniNalog();
                        //n.setStNadzora(filteredData.get(i).getStNadzora());
                        n.setStNadzora(Integer.parseInt(originalData.get(i).getStNadzora()));
                        n.setVrstaN(originalData.get(i).getVrstaN());
                        n.setOsebaNadzora(originalData.get(i).getOsebaNadzora());
                        n.setOsebaIzkaznica(originalData.get(i).getOsebaIzkaznica());
                        n.setKrajNadzora(originalData.get(i).getKrajNadzora());
                        n.setDatumNadzora(originalData.get(i).getDatumNadzora());
                        n.setUraZacetkaNadzora(originalData.get(i).getUraZacetkaNadzora());
                        n.setUraKoncaNadzora(originalData.get(i).getUraKoncaNadzora());
                        n.setRegStevilka(originalData.get(i).getRegStevilka());
                        n.setDelovniNalog(originalData.get(i).getDelovniNalog());
                        n.setObjekt(originalData.get(i).getObjekt());
                        n.setDodatnoPolje1(originalData.get(i).getDodatnoPolje1());
                        n.setDodatnoPolje2(originalData.get(i).getDodatnoPolje2());
                        n.setDodatnoPolje3(originalData.get(i).getDodatnoPolje3());
                        n.setOpomba(originalData.get(i).getOpomba());
                        n.setStatus(originalData.get(i).getStatus());
                        filterList.add(n);
                        */

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

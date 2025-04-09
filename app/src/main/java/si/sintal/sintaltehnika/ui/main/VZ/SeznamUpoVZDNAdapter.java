package si.sintal.sintaltehnika.ui.main.VZ;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.DelovniNalogVZ;

public class SeznamUpoVZDNAdapter extends ArrayAdapter<DelovniNalogVZ> implements Filterable {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DelovniNalogVZ> originalData;
    private ArrayList<DelovniNalogVZ> seznamDNjev;
    ItemFilter mFilter;
    public int id;
    private  int tehnikID;
    private int userID;
    private String mes_obr;

    public SeznamUpoVZDNAdapter(Context context, ArrayList<DelovniNalogVZ> seznamDNjev, int tehID, int upoId, String mesObr) {
        super(context, 0, seznamDNjev);
        this.context = context;
        this.seznamDNjev = new ArrayList<DelovniNalogVZ>();
        this.seznamDNjev.addAll(seznamDNjev);
        this.originalData = new ArrayList<DelovniNalogVZ>();
        this.originalData.addAll(seznamDNjev);
        this.tehnikID = tehID;
        this.userID = upoId;
        this.mes_obr = mesObr;


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
        TextView stSNja;
        TextView idSNja;
        TextView nazivServisa;
        TextView opomba;
        TextView narocnik;
        //CheckBox cbOznacen;
        //TextView datumDodeljenSN;
        //TextView kodaObjekta;

    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.seznamupovzdn, null);
            holder = new ViewHolder();
            holder.stSNja = (TextView) convertView.findViewById(R.id.labelstevilkaVZDN);
            holder.idSNja = (TextView) convertView.findViewById(R.id.labelidVZDN);
            holder.nazivServisa = (TextView) convertView.findViewById(R.id.labelNazivVZServisa);
            holder.narocnik = (TextView) convertView.findViewById(R.id.labelVZDNNarocnik);
            holder.opomba = (TextView) convertView.findViewById(R.id.labelVZDNOpisNapke);
            //holder.opisNapakeSNja = (TextView) convertView.findViewById(R.id.labelSNOpisNapke);
            //holder.datumDodeljenSN = (TextView) convertView.findViewById(R.id.labelidSNDodeljeno);
            //holder.cbOznacen = (CheckBox) convertView.findViewById(R.id.cbDodeli);
            //holder.continent = (TextView) convertView.findViewById(R.id.continent);
            //holder.region = (TextView) convertView.findViewById(R.id.region);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DelovniNalogVZ n = seznamDNjev.get(position);
        //if (n.getServiser_izvajalec() > 0)
        //{
        //    holder.stSNja.setText(n.getServiser_izvajalec());
        //}
        //else{
            holder.stSNja.setText(n.getDelovniNalog().toString());
        //}

        holder.idSNja.setText(Integer.toString(n.getid()));
        holder.nazivServisa.setText(n.getNaziv_servisa());
        holder.narocnik.setText(n.getObjekt() + " - " + n.getObjektNaslov());
        holder.opomba.setText("");
        if (n.getPeridika_kreirana() == 1)
        {
            convertView.setBackgroundColor(Color.parseColor("#F7F14A"));
        }
        else if(n.getPrenos_per() == 1)
        {
            convertView.setBackgroundColor(Color.parseColor("#A4CC44"));
        }
        else
        {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        //holder.kodaObjekta.setText(n.getIme().toString());
        //holder.opisNapakeSNja.setText(n.getOpis().toString());
        //String datumDod = "dodeljen: " + n.getDatumDodelitve().toString();
        //holder.datumDodeljenSN.setText(datumDod);

        Button bIzpolniSN = (Button) convertView.findViewById(R.id.bIzpolniVZDN);
        bIzpolniSN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment my = FragmentManager.findFragment(v);
                    ViewPager2 viewPager =(ViewPager2) my.getActivity().findViewById(R.id.DNVZViewPager);
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    int tehnik = 7;
                    VZPagerAdapter.setParameters(n.getid(),tehnik,0,n.getPrenos_per(),mes_obr);
                    //Intent intent = new Intent(getContext(), obrazecVZDNFragment.class);
                    //intent.putExtra("userID", userID);
                    //intent.putExtra("tehnikId", tehnikID );
                    //intent.putExtra("perPre", n.getPrenos_per());
                    //getContext().startActivity(intent);
                    //intent.putExtra("leto_mesec", );
                    //SNPagerAdapter.setParameters(n.getid(),tehnik,0);

                    viewPager.setCurrentItem(1);

                            //.findFragmentById(getItemId(index).toInt())
                }
        }
        );

        Button bPodatkiOSN = (Button) convertView.findViewById(R.id.bPodatkiVZDN);
        bPodatkiOSN.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               /*
                                               //Nadzor n = (Nadzor) adapter.getItem(position);

                                               Bundle bundle = new Bundle();
                                               //DatabaseHandler db = new DatabaseHandler(getContext());

                                               String datumSNja = n.getDatumZacetek();
                                               String stSNja = n.getDelovniNalog();
                                               String opisSNja = n.getOpis();
                                               String vodjaSNja = n.getVodjaNaloga();
                                               String odgOsebaSNja = n.getOdgovornaOseba();
                                               String narocnikSNja = n.getNarocnikNaziv() + ", " + n.getNarocnikNaslov();
                                               //String tehId = tehnikID;
                                               //String usId = userID;
                                               int id = n.getid();
                                               DialogPodatkiOSNFragment myFrag = new DialogPodatkiOSNFragment();
                                               myFrag.setArguments(bundle);
                                               Intent intent = new Intent(getContext(), DialogPodatkiOSNActivity.class);
                                               intent.putExtra("stSNja", stSNja);
                                               intent.putExtra("idSNja", id );
                                               intent.putExtra("datumSNja", datumSNja);
                                               intent.putExtra("opisSNja", opisSNja);
                                               intent.putExtra("vodjaSNja", vodjaSNja);
                                               intent.putExtra("odgOsebaSNja", odgOsebaSNja);
                                               intent.putExtra("narocnikSNja", narocnikSNja);
                                               intent.putExtra("tehnikID", tehnikID);
                                               intent.putExtra("userID", userID);
                                               getContext().startActivity(intent);

                                                */
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
            mFilter = new SeznamUpoVZDNAdapter.ItemFilter();
        }
        //return valueFilter;
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<DelovniNalogVZ> filterList = new ArrayList<DelovniNalogVZ>();

                for (int i = 0; i < originalData.size(); i++) {
                    if ((originalData.get(i).getNaziv_servisa().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        DelovniNalogVZ n = new DelovniNalogVZ();
                        //n.setStNadzora(filteredData.get(i).getStNadzora());
                        n.setid(originalData.get(i).getid());
                        n.setDelovniNalog(originalData.get(i).getDelovniNalog());
                        n.setNaziv_servisa(originalData.get(i).getNaziv_servisa());
                        //n.setVrstaN(originalData.get(i).getVrstaN());
                        //n.setOsebaNadzora(originalData.get(i).getOsebaNadzora());
                        //n.setOsebaIzkaznica(originalData.get(i).getOsebaIzkaznica());
                        //n.setKrajNadzora(originalData.get(i).getKrajNadzora());
                        //n.setDatumNadzora(originalData.get(i).getDatumNadzora());
                        //n.setUraZacetkaNadzora(originalData.get(i).getUraZacetkaNadzora());
                        //n.setUraKoncaNadzora(originalData.get(i).getUraKoncaNadzora());
                        //n.setRegStevilka(originalData.get(i).getRegStevilka());
                        //n.setDelovniNalog(originalData.get(i).getDelovniNalog());
                        //n.setObjekt(originalData.get(i).getObjekt());
                        //n.setDodatnoPolje1(originalData.get(i).getDodatnoPolje1());
                        //n.setDodatnoPolje2(originalData.get(i).getDodatnoPolje2());
                        //n.setDodatnoPolje3(originalData.get(i).getDodatnoPolje3());
                        //n.setOpomba(originalData.get(i).getOpomba());
                        //n.setStatus(originalData.get(i).getStatus());
                        filterList.add(n);


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

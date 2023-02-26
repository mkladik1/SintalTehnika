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

    public void setNarocnikNaziv(String nazivNar) {
        nazivNar = narocnikNaziv;
    }

    public void setID(int SNid) {
        SNid = id;
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
                intent.putExtra("stSNja", stSNja );
                intent.putExtra("datumSNja", datumSNja);
                intent.putExtra("opisSNja", opisSNja);
                intent.putExtra("vodjaSNja", vodjaSNja);
                intent.putExtra("odgOsebaSNja", odgOsebaSNja);
                intent.putExtra("narocnikSNja", narocnikSNja);
                getContext().startActivity(intent);
                //if (vN.equals("A")) {




/*
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), DialogPodatkiSN.class);
                    intent.putExtra("stSNja", stSNja );
                    intent.putExtra("opisSNja", opisSNja);
                    intent.putExtra("vodjaSNja", vodjaSNja);
                    intent.putExtra("narocnikSNja", narocnikSNja);
                    getContext().startActivity(intent);

 */
                //}


            }
        });


/*
        Button bDelete = (Button) convertView.findViewById(R.id.bBrisiNadzor);
        Button bEdit = (Button) convertView.findViewById(R.id.bUrediNadzor);
        Button bCopy = (Button) convertView.findViewById(R.id.bKopirajNadzor);

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nadzor nadzori = getItem(position);
                if (nadzori.getStatus() == 0) {
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    db.deleteNadzorPrevozPodKategorija(nadzori.getStNadzora());
                    db.deleteNadzorPrevozKategorija(nadzori.getStNadzora());
                    db.deleteNadzor(nadzori.getStNadzora());
                    db.close();
                    originalData.remove(position);
                    notifyDataSetChanged();
                    refreshList();
                }
            }
        });

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nadzor n = (Nadzor) adapter.getItem(position);
                Nadzor nadzori = getItem(position);
                String stN = nadzori.getStNadzora();
                int stNadzora = Integer.parseInt(stN);
                String vN = nadzori.getVrstaN();
                Bundle bundle = new Bundle();
                DatabaseHandler db = new DatabaseHandler(getContext());
                String userNaziv = db.getUserNaziv(nadzori.getDodatnoPolje3());
                bundle.putString("stNadzora", stN);
                if (vN.equals("A")) {
                    NadzorAlkotestFragment myFrag = new NadzorAlkotestFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorAlkotest.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);

                    //Bundle b = new Bundle();
                    //b.putInt("stNadzora", stNadzora); //Your id
                    //intent.putExtras(b); //Put your id to your next Intent
                    getContext().startActivity(intent);
                } else if (vN.equals("P")) {
                    NadzorPrevozFragment myFrag = new NadzorPrevozFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorPrevoz.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);
                    //Bundle b = new Bundle();
                    //b.putInt("stNadzora", stNadzora); //Your id
                    //intent.putExtras(b); //Put your id to your next Intent
                    getContext().startActivity(intent);

                } else if (vN.equals("T")) {
                    NadzorTehnikaFragment myFrag = new NadzorTehnikaFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorTehnika.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);
                    //Bundle b = new Bundle();
                    //b.putInt("stNadzora", stNadzora); //Your id
                    //intent.putExtras(b); //Put your id to your next Intent
                    getContext().startActivity(intent);
                } else if (vN.equals("V")) {
                    NadzorVarovanjeFragment myFrag = new NadzorVarovanjeFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorVarovanje.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);
                    //Bundle b = new Bundle();
                    //b.putInt("stNadzora", stNadzora); //Your id
                    //intent.putExtras(b); //Put your id to your next Intent
                    getContext().startActivity(intent);
                } else if (vN.equals("O")) {
                    NadzorVNCFragment myFrag = new NadzorVNCFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorVNC.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);
                    //Bundle b = new Bundle();
                    //b.putInt("stNadzora", stNadzora); //Your id
                    //intent.putExtras(b); //Put your id to your next Intent
                    getContext().startActivity(intent);
                }


            }
        });

        bCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nadzor n = (Nadzor) adapter.getItem(position);
                Nadzor nadzori = getItem(position);
                String stN = nadzori.getStNadzora();
                int stNadzora = Integer.parseInt(stN);
                String vN = nadzori.getVrstaN();
                DatabaseHandler db = new DatabaseHandler(getContext());
                String userNaziv = db.getUserNaziv(nadzori.getDodatnoPolje3());
                Bundle bundle = new Bundle();
                bundle.putString("stNadzora", stN);
                if (vN.equals("A")) {
                    db = new DatabaseHandler(getContext());
                    long stNad = db.kopirajNadzor(nadzori.getStNadzora());
                    stN = String.valueOf(stNad);
                    NadzorAlkotestFragment myFrag = new NadzorAlkotestFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorAlkotest.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);
                    getContext().startActivity(intent);
                } else if (vN.equals("P")) {
                    db = new DatabaseHandler(getContext());
                    long stNad = db.kopirajNadzor(nadzori.getStNadzora());
                    stN = String.valueOf(stNad);
                    NadzorPrevozFragment myFrag = new NadzorPrevozFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorPrevoz.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);
                    getContext().startActivity(intent);

                } else if (vN.equals("T")) {
                    db = new DatabaseHandler(getContext());
                    long stNad = db.kopirajNadzor(nadzori.getStNadzora());
                    stN = String.valueOf(stNad);
                    NadzorTehnikaFragment myFrag = new NadzorTehnikaFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorTehnika.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);
                    getContext().startActivity(intent);
                } else if (vN.equals("V")) {
                    db = new DatabaseHandler(getContext());
                    long stNad = db.kopirajNadzor(nadzori.getStNadzora());
                    stN = String.valueOf(stNad);
                    NadzorVarovanjeFragment myFrag = new NadzorVarovanjeFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorVarovanje.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);
                    getContext().startActivity(intent);
                } else if (vN.equals("O")) {
                    db = new DatabaseHandler(getContext());
                    long stNad = db.kopirajNadzor(nadzori.getStNadzora());
                    stN = String.valueOf(stNad);
                    NadzorVNCFragment myFrag = new NadzorVNCFragment();
                    myFrag.setArguments(bundle);
                    Intent intent = new Intent(getContext(), NadzorVNC.class);
                    intent.putExtra("stNadzora", stN);
                    intent.putExtra("userID", nadzori.getDodatnoPolje3());
                    intent.putExtra("userName", userNaziv);
                    getContext().startActivity(intent);
                }

            }
        });
*/
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

package si.sintal.sintaltehnika.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import si.sintal.sintaltehnika.R;

public class RegistracijaCasaListViewAdapter extends ArrayAdapter<RegCasa> implements Filterable {

    private Context context;
    RegCasa seznamRC;
    private LayoutInflater inflater;
    //private ArrayList<ServisniNalog> originalData;
    private ArrayList<RegCasa> seznamRCjev;
    //RegistracijaCasaListViewAdapter.ItemFilter mFilter;
    public String datumRc;
    public String casOdRc;
    public String casDoRc;


    public RegistracijaCasaListViewAdapter(Context context, ArrayList<RegCasa> seznamRCjev) {
        super(context, 0, seznamRCjev);
        this.context = context;
        this.seznamRCjev = new ArrayList<RegCasa>();
        this.seznamRCjev.addAll(seznamRCjev);


    }



    @Override
    public int getCount() {
        return seznamRCjev.size();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        TextView datum;
        TextView casod;
        TextView casdo;
        //TextView opisNapakeSNja;
        //CheckBox cbOznacen;
        //TextView datumDodelitveDNSNja;

    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {

        RegistracijaCasaListViewAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rc_vrstica, null);

            holder = new RegistracijaCasaListViewAdapter.ViewHolder();
            holder.datum = (TextView) convertView.findViewById(R.id.tvRCDatumLabel);
            holder.casod = (TextView) convertView.findViewById(R.id.tvRCCasOdLabel);
            holder.casdo = (TextView) convertView.findViewById(R.id.tvRCCasDoLabel);
            //holder.opisNapakeSNja = (TextView) convertView.findViewById(R.id.labelSNOpisNapke);
            //holder.cbOznacen = (CheckBox) convertView.findViewById(R.id.cbDodeli);
            //holder.datumDodelitveDNSNja = (TextView) convertView.findViewById(R.id.labelidSNDatum);
            //holder.continent = (TextView) convertView.findViewById(R.id.continent);
            //holder.region = (TextView) convertView.findViewById(R.id.region);

            convertView.setTag(holder);

        } else {
            holder = (RegistracijaCasaListViewAdapter.ViewHolder) convertView.getTag();
        }

        RegCasa rc = seznamRCjev.get(position);

        holder.datum.setText(rc.getDatum());
        holder.casod.setText(rc.getCasOd());
        holder.casdo.setText(rc.getCasDo());
        //holder.datumDodelitveDNSNja.setText("dodeljeno: " + n.getDatumDodelitve());

        return convertView;

    }


}

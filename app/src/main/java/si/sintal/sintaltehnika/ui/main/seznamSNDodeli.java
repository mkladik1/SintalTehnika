package si.sintal.sintaltehnika.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import si.sintal.sintaltehnika.DatabaseHandler;

public class seznamSNDodeli extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDataSource;
    private ArrayList<String> mDataSourceFilter;
    private List<String> mPregled;
    private String mDan;
    private String mMesec;
    private String mLeto;
    private String mUporabnik;
    private DatabaseHandler db;
    static ArrayList<ServisniNalog>  seznamSNDodelitev;
    static {
        seznamSNDodelitev = new ArrayList<ServisniNalog>();
    }

    public seznamSNDodeli(Context context, List<String> items, String month, String year, String user) {
        this.mContext = context;
        this.mDataSource = items;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDataSourceFilter = new ArrayList<String>();
        this.mDataSourceFilter.addAll(mDataSource);

        this.mMesec = month;
        this.mLeto = year;
        this.mUporabnik = user;
        this.db = new DatabaseHandler(mContext);
        //this.preglediHash = db.getPreglediInHashMap(mMesec,mLeto,mUporabnik);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}

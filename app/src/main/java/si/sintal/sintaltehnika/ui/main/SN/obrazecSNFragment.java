package si.sintal.sintaltehnika.ui.main.SN;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SNPagerAdapter;
import si.sintal.sintaltehnika.ui.main.ServisniNalog;

public class obrazecSNFragment extends Fragment {

    private ObrazecSNViewModel mViewModel;
    private int STSN;


    public static obrazecSNFragment newInstance() {
        return new obrazecSNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.obrazec_s_n_fragment, container, false);

       // int idSN = SNPagerAdapter.getParameters();
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        //STSN = SNPagerAdapter.getParameters();
        //methodA(); // this is called ...
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseHandler db = new DatabaseHandler(getContext());
        STSN = SNPagerAdapter.getParameters();
        ServisniNalog SN = new ServisniNalog();
        SN = db.vrniSN(STSN);
        TextView test = (TextView) getView().findViewById(R.id.idObrazecSN);
        test.setText(Integer.toString(SN.getid()));
        test = (TextView) getView().findViewById(R.id.tvStevilkaSN);
        test.setText(SN.getDelovniNalog());
        test = (TextView) getView().findViewById(R.id.tvSNNarocnikNaziv);
        test.setText(SN.getNarocnikNaziv());
        //methodA(); // this is called ...
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ObrazecSNViewModel.class);
        // TODO: Use the ViewModel
    }

}
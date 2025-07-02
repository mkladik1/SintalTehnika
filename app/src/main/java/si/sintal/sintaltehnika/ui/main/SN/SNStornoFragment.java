package si.sintal.sintaltehnika.ui.main.SN;

import static android.app.PendingIntent.getActivity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;

public class SNStornoFragment extends Fragment {

    private SNStornoViewModel mViewModel;
    public Integer snID;

    public static SNStornoFragment newInstance() {
        return new SNStornoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SNStornoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sn_fragment_storno, container, false);
        Intent intent= getActivity().getIntent();
        snID = intent.getIntExtra("snID",0);
        Button bShrani = (Button) v.findViewById(R.id.bPotrdiSNStorno);
        bShrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(v.getContext());
                db.updateSNStatusAkt(String.valueOf(snID),"S");
            }});
        Button bPreklici = (Button) v.findViewById(R.id.bPrekliciSNStorno);
        bPreklici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }});




        return v;
    }

}
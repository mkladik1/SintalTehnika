package si.sintal.sintaltehnika.ui.main.SN;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import si.sintal.sintaltehnika.R;

public class obrazecSNFragment extends Fragment {

    private ObrazecSNViewModel mViewModel;

    public static obrazecSNFragment newInstance() {
        return new obrazecSNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.obrazec_s_n_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ObrazecSNViewModel.class);
        // TODO: Use the ViewModel
    }

}
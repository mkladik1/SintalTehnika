package si.sintal.sintaltehnika.ui.main.SN;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import si.sintal.sintaltehnika.DatabaseHandler;
import si.sintal.sintaltehnika.R;

public class seznamSNFragment extends Fragment {

    private SeznamSNViewModel mViewModel;

    public static seznamSNFragment newInstance() {
        return new seznamSNFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seznam_s_n_fragment, container, false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());
        EditText etSN = (EditText) v.findViewById(R.id.etDatumSN);
        etSN.setText(currentDateandTime);

        DatabaseHandler db = new DatabaseHandler(getContext());
        List<String> lables = db.getTehnikiInString();
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.test_list_item,lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner_user_SN);
        spinner.setAdapter(dataAdapter);



        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SeznamSNViewModel.class);
        // TODO: Use the ViewModel
    }

}
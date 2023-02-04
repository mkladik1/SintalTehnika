package si.sintal.sintaltehnika.ui.main;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import si.sintal.sintaltehnika.GlavnoOkno;
import si.sintal.sintaltehnika.R;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        Intent intent= getActivity().getIntent();
        Button login;
        login = (Button) getActivity().findViewById(R.id.bLogin);
        //DatabaseHandler db = new DatabaseHandler(getContext());
        /*db.createTablesLogin();
        if (db.usersLogin() == 0)
        {

            new LoadFromWeb().execute();
        }
        */

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                TextView tvUpIme = (TextView) getActivity().findViewById(R.id.editTextTextPersonName);
                TextView tvGeslo = (TextView) getActivity().findViewById(R.id.editTextTextPassword);
                DatabaseHandler db = new DatabaseHandler(getContext());
                ArrayList<String> upo = new ArrayList<String>();
                upo = db.getUserLogin(tvUpIme.getText().toString(),tvGeslo.getText().toString());
                String userID = upo.get(0);
                String nadzornik = upo.get(1) + " " + upo.get(2);
                if (upo.get(0).equals(""))
                {
                    Toast.makeText(getContext(),"Napačno uporabniško ime", Toast.LENGTH_LONG).show();
                }
                else if (upo.get(3).equals(""))
                {
                    Toast.makeText(getContext(),"Napačno geslo", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("userName", nadzornik);
                    startActivity(intent);
                }

                 */
                Intent intent = new Intent(getActivity(), GlavnoOkno.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}
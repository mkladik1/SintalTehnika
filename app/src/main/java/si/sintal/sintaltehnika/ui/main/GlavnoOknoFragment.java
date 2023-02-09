package si.sintal.sintaltehnika.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import si.sintal.sintaltehnika.MainActivity;
import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ServisActivity;

public class GlavnoOknoFragment extends Fragment {

    private GlavnoOknoViewModel mViewModel;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;

    public static GlavnoOknoFragment newInstance() {
        return new GlavnoOknoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.glavno_okno_fragment, container, false);
        Intent intent= getActivity().getIntent();
        tehnikID = intent.getStringExtra("userID");
        tehnikNaziv = intent.getStringExtra("userName");
        tehnikEmail = intent.getStringExtra("email");
        tehnikAdminDostop = intent.getStringExtra("admin");
        servis = intent.getStringExtra("servis");
        montaza = intent.getStringExtra("montaza");
        vzdrzevanje = intent.getStringExtra("vzdrzevanje");

        Button bOdjava = (Button) view.findViewById(R.id.bOdjava);
        Button bServis = (Button) view.findViewById(R.id.bServisniNalogi);
        Button bNastavitve = (Button) view.findViewById(R.id.bNastavitve);
        Button bDodeliSN = (Button) view.findViewById(R.id.bDodeliSN);

        bOdjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("userID", "");
                intent.putExtra("userName", "");
                intent.putExtra("email", "");
                intent.putExtra("admin","");
                startActivity(intent);
            }
        });

        bServis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServisActivity.class);
                intent.putExtra("userID", tehnikID);
                intent.putExtra("userName", tehnikNaziv);
                intent.putExtra("email", tehnikEmail);
                intent.putExtra("admin",tehnikAdminDostop);
                intent.putExtra("servis",servis);
                intent.putExtra("montaza",montaza);
                intent.putExtra("vzdrzevanje",vzdrzevanje);
                startActivity(intent);
            }
        });


        bNastavitve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NastavitveActivity.class);
                intent.putExtra("userID", tehnikID);
                intent.putExtra("userName", tehnikNaziv);
                intent.putExtra("email", tehnikEmail);
                intent.putExtra("admin",tehnikAdminDostop);
                intent.putExtra("servis",servis);
                intent.putExtra("montaza",montaza);
                intent.putExtra("vzdrzevanje",vzdrzevanje);
                startActivity(intent);
            }
        });


        bDodeliSN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tehnikAdminDostop.equals("1")) {
                    Intent intent = new Intent(getActivity(), DodeliSNActivity.class);
                    intent.putExtra("userID", tehnikID);
                    intent.putExtra("userName", tehnikNaziv);
                    intent.putExtra("email", tehnikEmail);
                    intent.putExtra("admin", tehnikAdminDostop);
                    intent.putExtra("servis", servis);
                    intent.putExtra("montaza", montaza);
                    intent.putExtra("vzdrzevanje", vzdrzevanje);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Nimate pravic do tega modula!", Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView tvImePriimek = (TextView) view.findViewById(R.id.tvImePriimek);
        tvImePriimek.setText("Vpisani ste kot: "+tehnikNaziv );


        return view;//inflater.inflate(R.layout.glavno_okno_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(GlavnoOknoViewModel.class);
        //mViewModel = new ViewModelProvider(this).get(GlavnoOknoViewModel.class);
        // TODO: Use the ViewModel
    }

}
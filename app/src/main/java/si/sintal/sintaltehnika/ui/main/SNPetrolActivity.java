package si.sintal.sintaltehnika.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SN.SNPetrolFragment;

public class SNPetrolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_petrol_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SNPetrolFragment.newInstance())
                    .commitNow();
        }
    }


}
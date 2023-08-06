package si.sintal.sintaltehnika.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.SN.SNZakljuceniSNFragment;
public class SNZakljuceniSN extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_zakljuceni_sn_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SNZakljuceniSNFragment.newInstance())
                    .commitNow();
        }
    }
}
package si.sintal.sintaltehnika.ui.main.SN;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import si.sintal.sintaltehnika.R;

public class SNDodajServiserjaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_dodaj_serviserja_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SNDodajServiserjaFragment.newInstance())
                    .commitNow();
        }
    }
}
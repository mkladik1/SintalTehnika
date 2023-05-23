package si.sintal.sintaltehnika.ui.main.SN;


import androidx.appcompat.app.AppCompatActivity;
import si.sintal.sintaltehnika.R;

import android.os.Bundle;

public class SNDodajArtikel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sn_dodaj_artikel_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SNDodajArtikelFragment.newInstance())
                    .commitNow();
        }
    }
}
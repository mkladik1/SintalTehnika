package si.sintal.sintaltehnika.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.MN.MontazaFragment;

public class MontazaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.montaza_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MontazaFragment.newInstance())
                    .commitNow();
        }
    }
}
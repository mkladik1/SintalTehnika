package si.sintal.sintaltehnika.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import si.sintal.sintaltehnika.R;

public class DodeliSNActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodali_sn_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DodaliSNFragment.newInstance())
                    .commitNow();
        }
    }
}
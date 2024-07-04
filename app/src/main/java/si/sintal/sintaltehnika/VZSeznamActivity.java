package si.sintal.sintaltehnika;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import si.sintal.sintaltehnika.ui.main.VZ.VZSeznamFragment;

public class VZSeznamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vz_seznam_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, VZSeznamFragment.newInstance())
                    .commitNow();
        }
    }
}
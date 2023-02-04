package si.sintal.sintaltehnika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import si.sintal.sintaltehnika.ui.main.GlavnoOknoFragment;

public class GlavnoOkno extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glavno_okno_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, GlavnoOknoFragment.newInstance())
                    .commitNow();
        }
    }
}
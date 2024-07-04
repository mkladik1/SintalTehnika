package si.sintal.sintaltehnika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import si.sintal.sintaltehnika.ui.main.RegistracijaCasaFragment;

public class RegistracijaCasa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registracija_casa_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RegistracijaCasaFragment.newInstance())
                    .commitNow();
        }
    }
}
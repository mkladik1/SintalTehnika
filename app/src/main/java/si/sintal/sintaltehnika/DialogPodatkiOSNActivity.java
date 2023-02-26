package si.sintal.sintaltehnika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import si.sintal.sintaltehnika.ui.main.DialogPodatkiOSNFragment;

public class DialogPodatkiOSNActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_podatki_o_s_n_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DialogPodatkiOSNFragment.newInstance())
                    .commitNow();
        }
    }
}
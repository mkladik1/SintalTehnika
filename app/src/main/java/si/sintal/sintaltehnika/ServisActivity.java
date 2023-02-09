package si.sintal.sintaltehnika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import si.sintal.sintaltehnika.ui.main.ServisFragment;

public class ServisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servis_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ServisFragment.newInstance())
                    .commitNow();
        }
    }
}
package si.sintal.sintaltehnika.ui.main.SN;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import si.sintal.sintaltehnika.R;
import si.sintal.sintaltehnika.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
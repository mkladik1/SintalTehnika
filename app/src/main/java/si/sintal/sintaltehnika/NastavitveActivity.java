package si.sintal.sintaltehnika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import si.sintal.sintaltehnika.ui.main.NastavitveFragment;

public class NastavitveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nastavitve_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, NastavitveFragment.newInstance())
                    .commitNow();
        }
    }
}
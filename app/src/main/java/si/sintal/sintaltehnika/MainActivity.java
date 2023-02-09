package si.sintal.sintaltehnika;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import si.sintal.sintaltehnika.ui.main.MainFragment;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

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
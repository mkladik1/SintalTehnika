package si.sintal.sintaltehnika.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Date;

import si.sintal.sintaltehnika.GlavnoOkno;
import si.sintal.sintaltehnika.MainActivity;
import si.sintal.sintaltehnika.R;

public class ServisFragment extends Fragment {

    private ServisViewModel mViewModel;
    private String tehnikID;
    private String tehnikNaziv;
    private String tehnikEmail;
    private String tehnikAdminDostop;
    private String servis;
    private String montaza;
    private String vzdrzevanje;

    public static ServisFragment newInstance() {
        return new ServisFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.servis_fragment, container, false);
        Intent intent= getActivity().getIntent();
        tehnikID = intent.getStringExtra("userID");
        tehnikNaziv = intent.getStringExtra("userName");
        tehnikEmail = intent.getStringExtra("email");
        tehnikAdminDostop = intent.getStringExtra("admin");
        servis = intent.getStringExtra("servis");
        montaza = intent.getStringExtra("montaza");
        vzdrzevanje = intent.getStringExtra("vzdrzevanje");

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.snTabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager2 viewPager =(ViewPager2) v.findViewById(R.id.SNViewPager);
        SNPagerAdapter tabsAdapter = new SNPagerAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(tabsAdapter);
        viewPager.setUserInputEnabled(false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        Button bNazaj = (Button) v.findViewById(R.id.bSNToGlavno);
        bNazaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GlavnoOkno.class);
                intent.putExtra("userID", tehnikID);
                intent.putExtra("userName", tehnikNaziv);
                intent.putExtra("email", tehnikEmail);
                intent.putExtra("admin",tehnikAdminDostop);
                intent.putExtra("servis",servis);
                intent.putExtra("montaza",montaza);
                intent.putExtra("vzdrzevanje",vzdrzevanje);
                startActivity(intent);
            }
        });




        return v; //inflater.inflate(R.layout.servis_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServisViewModel.class);
        // TODO: Use the ViewModel
    }

}
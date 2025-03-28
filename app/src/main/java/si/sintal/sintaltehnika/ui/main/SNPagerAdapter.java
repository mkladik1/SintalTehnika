package si.sintal.sintaltehnika.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import si.sintal.sintaltehnika.ui.main.SN.obrazecSNFragment;
import si.sintal.sintaltehnika.ui.main.SN.seznamSNFragment;
import si.sintal.sintaltehnika.ui.main.SN.SNZakljuceniSNFragment;

public class SNPagerAdapter  extends FragmentStateAdapter {

    int mNoOfTabs;

    static String userName;
    static int userID;
    static int tehnikID;
    static int snID;
    static  int nalogaID;
    static int nsID;
    static int sistemID;
    static String pregledOpis;
    static int senderID;

    public SNPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    public static void setParameters(int sID, int tehID, int uID)
    {

        snID = sID;
        tehnikID = tehID;
        userID = uID;
       /* nalogaID = nid;
        nsID = nsid;
        sistemID = sid;
        userName = un;
        pregledOpis = opis;
        senderID = sd;

        */
    }

    public static int getParameters()
    {

        return snID;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position)
        {
            case 0:
                return new seznamSNFragment();
            case 1:
                return new obrazecSNFragment();
            case 2:
                return new SNZakljuceniSNFragment();
            default:
                return null;
        }
    }


}
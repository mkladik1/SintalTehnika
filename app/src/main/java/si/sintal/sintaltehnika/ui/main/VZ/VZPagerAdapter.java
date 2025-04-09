package si.sintal.sintaltehnika.ui.main.VZ;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class VZPagerAdapter extends FragmentStateAdapter {

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
    static int per_prenos;
    static String mes_obr;

    public VZPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    public static void setParameters(int sID, int tehID, int uID, int perPrenos, String mesObr)
    {

        snID = sID;
        tehnikID = tehID;
        userID = uID;
        per_prenos = perPrenos;
        mes_obr = mesObr;
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

    public static int getPer_prenos()
    {

        return per_prenos;
    }
    public static String getMes_obr()
    {
        return mes_obr;
    }
    public static int getTehnikID()
    {
        return tehnikID;
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
                return new seznamVZDNFragment();
            case 1:
               return new obrazecVZDNFragment();
            case 2:
                return new VZZakljuceniDNFragment();
            default:
                return null;
        }
    }


}
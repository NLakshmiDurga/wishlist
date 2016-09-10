package durga.wishlist.murthyavanithsa.app;

/**
 * Created by durga on 5/7/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PendingTasks pendingTasks = new PendingTasks();
//                return pendingTasks;
            case 1:
                CompletedTasks completedTasks = new CompletedTasks();
//                return completedTasks;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

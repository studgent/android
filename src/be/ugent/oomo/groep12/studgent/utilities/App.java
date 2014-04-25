package be.ugent.oomo.groep12.studgent.utilities;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context mContext;
    public static final int UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 60;
    public static final int FAST_INTERVAL_CEILING_IN_MILLISECONDS = 1000 * 10;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}

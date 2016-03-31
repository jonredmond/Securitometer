package jon.com.securitometer;

import java.util.Comparator;

/**
 * Created by jon on 01/03/16.
 */
public class AppComparator implements Comparator<App> {
    @Override
    public int compare(App a1, App a2) {
        if(a1.getScore() < a2.getScore()) {
            return -1;
        }
        if(a1.getScore() > a2.getScore()) {
            return 1;
        }
        return 0;
    }
}

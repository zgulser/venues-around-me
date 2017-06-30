package assignment.adyen.com.venuesaroundme.ui.utils;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Zeki on 29/06/2017.
 */

public class AnimationUtils {

    public static final int DURATION_MEDIUM = 3000;

    public static void runAlphaAnimationOnAView(View fab, int durationInMillis, boolean opaque){
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(fab, View.ALPHA, (opaque == true ? 0 : 1), (opaque == true ? 1 : 0)).setDuration(durationInMillis);
        alphaAnim.start();
    }
}

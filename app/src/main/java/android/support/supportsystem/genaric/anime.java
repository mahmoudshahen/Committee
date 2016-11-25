package android.support.supportsystem.genaric;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mahmoud shahen on 11/11/2016.
 */

public class anime {
    public static void animate(RecyclerView.ViewHolder holder, boolean down)
    {
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",-200,0);
        animatorTranslateY.setDuration(1000);
        animatorTranslateY.start();
    }
}

package oodesignanalysis.hiketracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Ryan on 11/21/15.
 */
public class BitmapHelper {
    public static Bitmap resizeMapIcons(Context context, String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}



package com.csci5448.hiketracker;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by diana on 11/27/15.
 */
public class SaveHikeDataDialog extends Dialog {


    // Default dialog: only display save and edit
    public SaveHikeDataDialog(Context context) {
        super(context);
        this.setContentView(R.layout.savehikedatadialog);


    }
}

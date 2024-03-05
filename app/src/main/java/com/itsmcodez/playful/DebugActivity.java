package com.itsmcodez.playful;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.InputStream;

public class DebugActivity extends AppCompatActivity {
	String[] exceptionType = {
			"StringIndexOutOfBoundsException",
			"IndexOutOfBoundsException",
			"ArithmeticException",
			"NumberFormatException",
			"ActivityNotFoundException"
	};
	String[] errMessage= {
			"Invalid string operation\n",
			"Invalid list operation\n",
			"Invalid arithmetical operation\n",
			"Invalid toNumber block operation\n",
			"Invalid intent operation"
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String errMsg = "";
		String madeErrMsg = "";
		if(intent != null){
			errMsg = intent.getStringExtra("error");
			String[] spilt = errMsg.split("\n");
			//errMsg = spilt[0];
			try {
				for (int j = 0; j < exceptionType.length; j++) {
					if (spilt[0].contains(exceptionType[j])) {
						madeErrMsg = errMessage[j];
						int addIndex = spilt[0].indexOf(exceptionType[j]) + exceptionType[j].length();
						madeErrMsg += spilt[0].substring(addIndex, spilt[0].length());
						break;
					}
				}
				if(madeErrMsg.isEmpty()) madeErrMsg = errMsg;
			}catch(Exception e){}
		}
        
        // Crash handling dialog 
        MaterialAlertDialogBuilder bld = new MaterialAlertDialogBuilder(this);
		bld.setTitle(R.string.activity_debug_dialog_title);
		bld.setMessage( madeErrMsg );
		bld.setNeutralButton(R.string.activity_debug_dialog_neutral_bt_text, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
        bld.setPositiveButton(R.string.activity_debug_dialog_positive_bt_text, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
            }
        });
		bld.create().show();
    }
}
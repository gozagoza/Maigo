package com.example.maigo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DestinationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination);
    	EditText latiText = (EditText)findViewById(R.id.destLati);
    	latiText.setText("35.604730");
    	EditText longiText = (EditText)findViewById(R.id.destLongi);
    	longiText.setText("139.683841");

	    Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	EditText latiText = (EditText)findViewById(R.id.destLati);
            	double lati = Double.parseDouble(latiText.getText().toString());
            	EditText longiText = (EditText)findViewById(R.id.destLongi);
            	double longi = Double.parseDouble(longiText.getText().toString());
            	Intent intent = new Intent();
            	intent.putExtra("lati",lati);
            	intent.putExtra("longi",longi);
            	setResult( Activity.RESULT_OK, intent );
            	finish();
            }
        });

	}
}

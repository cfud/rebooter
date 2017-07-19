package biz.cfud.rebooter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class ActivitySettings extends Activity {

    Button btnSave;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Context cnt = getApplicationContext();

        timePicker = (TimePicker) findViewById(R.id.timePickerReboot);

        if(Kernel.init(cnt)) {
            timePicker.setHour(0);
            timePicker.setMinute(0);
        }else{
            timePicker.setHour(Kernel.readIntConfig(cnt,Config.hour));
            timePicker.setMinute(Kernel.readIntConfig(cnt,Config.minute));
        }
        timePicker.is24HourView();

        final int id = Resources.getSystem().getIdentifier("ampm_layout", "id", "android");
        final View amPmLayout = timePicker.findViewById(id);
        if(amPmLayout != null) {
            amPmLayout.setVisibility(View.GONE);
        }

        btnSave = (Button) findViewById(R.id.buttonSave);
        btnSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kernel.writeIntConfig(getApplicationContext(),Config.hour,timePicker.getHour());
                Kernel.writeIntConfig(getApplicationContext(),Config.minute,timePicker.getMinute());
                Kernel.writeIntConfig(getApplicationContext(), Config.timelost,0);
                try {
                    Process p = Runtime.getRuntime().exec(Config.su);
                }catch(Exception e) {

                }
                Toast.makeText(getApplicationContext(),Config.szAllDataSave,Toast.LENGTH_LONG);
                finish();
            }
        });
        Kernel.startServices(getApplicationContext());
    }


}

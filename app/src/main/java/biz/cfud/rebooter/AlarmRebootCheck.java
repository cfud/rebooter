package biz.cfud.rebooter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmRebootCheck extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Kernel.checkReboot(context);
    }
}

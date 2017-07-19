package biz.cfud.rebooter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServiceRebootCheck extends Service{
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            return Service.START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            throw new UnsupportedOperationException("");
        }

        @Override
        public void onCreate()
        {
            Kernel.checkReboot(getApplicationContext());
        }
}

package biz.cfud.rebooter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import java.util.Calendar;


public class Kernel {
    public static boolean init(Context cnt){
        if(readIntConfig(cnt,Config.runed)==1){
            return false;
        }else {
            writeIntConfig(cnt, Config.runed, 1);
            writeIntConfig(cnt, Config.hour, 0);
            writeIntConfig(cnt, Config.minute, 0);
            writeIntConfig(cnt, Config.timelost,0);
            return true;
        }
    }

    public static void startServices(final Context context){
        if(Build.VERSION.SDK_INT >= 19) {
            Intent myAlarm = new Intent(context, AlarmRebootCheck.class);
            PendingIntent recurringAlarm = PendingIntent.getBroadcast(context, 0, myAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar updateTime = Calendar.getInstance();
            alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), 60000, recurringAlarm);
        }else {
            context.startService(new Intent(context, ServiceRebootCheck.class));
        }
    }

    public static void checkReboot(Context cnt){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int need_hour = readIntConfig(cnt,Config.hour);
        int need_min = readIntConfig(cnt,Config.minute);
        int timelost = readIntConfig(cnt,Config.timelost);

        if(need_hour==hour && need_min<=min && (need_min-min)<=1 && timelost==0){
            timelost++;
            writeIntConfig(cnt, Config.timelost,timelost);
            makeReboot();
        }

        if(timelost!=0)
            timelost++;

        if(timelost>120)
            timelost=0;

        writeIntConfig(cnt, Config.timelost,timelost);
    }

    public static void makeReboot() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{Config.su, "-c", Config.reboot});
            proc.waitFor();
        } catch (Exception ex) {

        }
    }

    public static String readStringConfig(final Context cnt, final String key){
        SharedPreferences settings = cnt.getSharedPreferences(Config.config_file, 0);
        return settings.getString(key, "");
    }

    public static void writeStringConfig(final Context cnt,final String key, final String value){
        SharedPreferences settings = cnt.getSharedPreferences(Config.config_file, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int readIntConfig(final Context cnt,final String key){
        SharedPreferences settings = cnt.getSharedPreferences(Config.config_file, 0);
        return settings.getInt(key, 0);
    }

    public static void writeIntConfig(final Context cnt,final String key, final int value){
        SharedPreferences settings = cnt.getSharedPreferences(Config.config_file, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

}

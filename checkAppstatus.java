package com.lobotus.activelog;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    ListView androidAppsList;

    ArrayList<String> appsNames = new ArrayList<>();
    PackageManager pm;
    String compleateProcess = "";
    BroadcastReceiver br;
    AppCompatButton appStatus, addDatabase, removeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidAppsList = findViewById(R.id.apps_list_android);
        appStatus = findViewById(R.id.check_app_status_id);
        addDatabase = findViewById(R.id.add_database_id);
        removeDatabase = findViewById(R.id.delete_database_id);

        addDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper db = new DbHelper(MainActivity.this);

            }
        });

        removeDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper db = new DbHelper(MainActivity.this);
                try {
                    db.deleteDatabase();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        appStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appsNames.clear();
                appsNames.add("Service Running==" + isServiceOnline());
                appsNames.add("App Uninstalled==" + !isAppInstal());
                appsNames.add("Gps Enable==" + isGpsOn());
                appsNames.add("Account ==" + isAccountExists());
                appsNames.add("Battery ==" + batterylevel(MainActivity.this));
                appsNames.add("Connection ==" + isConnected(MainActivity.this)
                        + " Wifi==" + isConnectedWifi(MainActivity.this) + " Mobile ==" + isConnectedMobile(MainActivity.this));
                try {
                    appsNames.add("App Size ==" + getApkSize(MainActivity.this, "com.lobotus.mapei") / 1000000.0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                appsNames.add("Db file Exists== " + checkDataBase());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getAllPermission();
                } else {
                    appsNames.add("Version Below Jelly Bean app permission are accepted");
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,
                        R.layout.activity_listview, appsNames);
                androidAppsList.setAdapter(adapter);
            }
        });

        /*Intent intent = new Intent(this, TrackingUninstal.class);
        startService(intent);*/

    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(Environment.getDataDirectory() + "/data/com.lobotus.mapei/databases/mapei.db", null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (Exception e) {
        }
        return checkDB != null;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void getAllPermission() {
        List<String> granted = new ArrayList<String>();
        List<String> allPermission = new ArrayList<String>();

        PackageManager p = getPackageManager();
        final List<PackageInfo> appinstall =
                p.getInstalledPackages(PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS);
        for (int i = 0; i < appinstall.size(); i++) {
            if (appinstall.get(i).packageName.equals("com.lobotus.mapei")) {
                allPermission.addAll(Arrays.asList(appinstall.get(i).requestedPermissions));
                break;
            }
        }

        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo("com.lobotus.mapei", PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < pi.requestedPermissions.length; i++) {
            if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                granted.add(pi.requestedPermissions[i]);
            }
        }

        for (String temp : allPermission) {
            if (granted.contains(temp))
                appsNames.add(temp + "== true");
//                System.out.println("vvvvv--------sort permissions---true----" + temp);
            else
                appsNames.add(temp + "== false");
//                System.out.println("vvvvv--------sort permissions---false----" + temp);
        }

    }

    private boolean isGpsOn() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }

    private boolean isAccountExists() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.lobotus.mapei");
        final boolean connected = accounts != null && accounts.length > 0;
        return connected;
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = MainActivity.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = MainActivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = MainActivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    private boolean isServiceOnline() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < services.size(); i++) {
            String packageClassName = services.get(i).service.getClassName();
            if (packageClassName.equals("com.android.location.fused.FusedLocationService")) {
                return true;
            }
        }
        return false;
    }

    public static long getApkSize(Context context, String packageName)
            throws PackageManager.NameNotFoundException {
        return new File(context.getPackageManager().getApplicationInfo(
                packageName, 0).publicSourceDir).length();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // getNames();
    }


    public String batterylevel(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = (level / (float) scale) * 100;
        int ss = Integer.valueOf((int) batteryPct);
        System.out.println("batteryPct" + ss + "%");
        if (ss == 0) {
            ss = 0;
            return ss + "%";
        } else {

            return ss + "%";
        }
    }


    void getNames() {
        pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();

        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app);
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            } else {
                installedApps.add(app);
            }
        }

        for (int i = 0; i < installedApps.size(); i++) {
            appsNames.add(installedApps.get(i).loadLabel(pm).toString());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, appsNames);
        androidAppsList.setAdapter(adapter);

    }

    boolean isAppInstal() {
        pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();

        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app);
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            } else {
                installedApps.add(app);
            }
        }

        for (int i = 0; i < installedApps.size(); i++) {
            if (installedApps.get(i).packageName.equals("com.lobotus.mapei")) {
                return true;
            }
        }
        return false;
    }



   /* private void registerGpsBroadcast() {
        registerReceiver(broadcastReceiverGpsStatus, new IntentFilter("GPS_BROADCAST_PASSING_TO_SERVICE_CLASS_ACTION_NAME"));
        Toast.makeText(this, "Register Gps", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver broadcastReceiverGpsStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Changed Gps", Toast.LENGTH_SHORT).show();
        }
    };


    private void registerWifiBroadcast() {
        registerReceiver(broadcastReceiverWIFIStatus, new IntentFilter("WIFI_BROADCAST_PASSING_TO_SERVICE_CLASS_ACTION_NAME"));
        Toast.makeText(this, "Register WIFI", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver broadcastReceiverWIFIStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Changed WIFI", Toast.LENGTH_SHORT).show();
        }
    };

    private void registerAccountsBroadcast() {
        registerReceiver(broadcastReceiverAccountsStatus, new IntentFilter("ACCOUNTS_BROADCAST_PASSING_TO_SERVICE_CLASS_ACTION_NAME"));
        Toast.makeText(this, "Register Accounts", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver broadcastReceiverAccountsStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Deleted Accounts", Toast.LENGTH_SHORT).show();
        }
    };


    private void registeCustomeBroadcast() {
        registerReceiver(broadcastReceiverCustomeStatus, new IntentFilter("CUSTOME_BROADCAST_PASSING_TO_SERVICE_CLASS_ACTION_NAME"));
        Toast.makeText(this, "Register Custome", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver broadcastReceiverCustomeStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "get Custome", Toast.LENGTH_SHORT).show();
        }
    };*/

}

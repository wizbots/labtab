package org.wizbots.labtab;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.craterzone.logginglib.manager.LoggerManager;

import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.interfaces.BaseManagerInterface;
import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.interfaces.OnLoadListener;
import org.wizbots.labtab.model.Nuggests;
import org.wizbots.labtab.model.metadata.MetaData;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.pushnotification.NotiManager;
import org.wizbots.labtab.retrofit.LabTabApiInterface;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LabTabApplication extends Application {

    private static final String TAG = LabTabApplication.class.getName();
    private static LabTabApplication _instance;
    private LabTabApiInterface labTabApiInterface;
    private boolean closed;
    private ArrayList registeredManagers;
    private Map<Class<? extends BaseManagerInterface>, Collection<? extends BaseManagerInterface>> managerInterfaces;
    private Map<Class<? extends BaseUIListener>, Collection<? extends BaseUIListener>> uiListeners;
    private MetaData[] metaDatas;
    private final Handler handler;
    protected int totalProjects;
    protected int labTime;
    protected int countCompletedProjects;
    protected int countSkippedProjects;
    protected int countPendingProjects;
    private String location = null;
    private String year = null;
    private String season = null;
    private int locationPos = 0;
    private int yearPos = 0;
    private int seasonPos = 0;
    private boolean isSeasonEnable = false;
    private boolean isLocationEnable = false;
    private boolean isYearEnable = false;

    public LabTabApplication() {
        _instance = this;
        closed = false;
        registeredManagers = new ArrayList<>();
        managerInterfaces = new HashMap<>();
        uiListeners = new HashMap<>();
        handler = new Handler();
    }

    public static LabTabApplication getInstance() {
        if (_instance == null) {
            _instance = new LabTabApplication();
        }
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application onCreate()");
        uriCheckForAPi25();
        initManagers();
        initDB();
        loadManagers();
        initRetrofit();
        LoggerManager.getInstance(getApplicationContext()).init();
        metaDatas = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getProjectsMetaData();
    }

    /**
     * this method help for
     * for API 24+, to access file:// intents,to disable the runtime check
     */
    private void uriCheckForAPi25(){
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void initManagers() {
        TypedArray clienttables = getResources().obtainTypedArray(
                R.array.labtab_managers);
        for (int index = 0; index < clienttables.length(); index++) {
            try {
                Class.forName(clienttables.getString(index));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        clienttables.recycle();
    }

    private void initDB() {
        TypedArray clienttables = getResources().obtainTypedArray(
                R.array.labtabtables);
        for (int index = 0; index < clienttables.length(); index++) {
            try {
                Class.forName(clienttables.getString(index));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        clienttables.recycle();
    }

    public void setLocation(String location, int locationPos) {
        this.location = location;
        this.locationPos = locationPos;
    }

    public void setYear(String year, int yearPos) {
        this.year = year;
        this.yearPos = yearPos;
    }

    public void setSeason(String season, int seasonPos) {
        this.season = season;
        this.seasonPos = seasonPos;
    }

    public String getLocation() {
        return location;
    }

    public String getYear() {
        return year;
    }

    public String getSeason() {
        return season;
    }

    public int getLocationPos() {
        return locationPos;
    }

    public int getYearPos() {
        return yearPos;
    }

    public int getSeasonPos() {
        return seasonPos;
    }

    public boolean isSeasonEnable() {
        return isSeasonEnable;
    }

    public void setSeasonEnable(boolean seasonEnable) {
        isSeasonEnable = seasonEnable;
    }

    public boolean isLocationEnable() {
        return isLocationEnable;
    }

    public void setLocationEnable(boolean locationEnable) {
        isLocationEnable = locationEnable;
    }

    public boolean isYearEnable() {
        return isYearEnable;
    }

    public void setYearEnable(boolean yearEnable) {
        isYearEnable = yearEnable;
    }

    public void addManager(Object manager) {
        registeredManagers.add(manager);
    }

    private void loadManagers() {
        for (OnLoadListener listener : getManagers(OnLoadListener.class)) {
            listener.onLoad();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseManagerInterface> Collection<T> getManagers(
            Class<T> cls) {
        if (closed)
            return Collections.emptyList();
        Collection<T> collection = (Collection<T>) managerInterfaces.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            for (Object manager : registeredManagers)
                if (cls.isInstance(manager))
                    collection.add((T) manager);
            collection = Collections.unmodifiableCollection(collection);
            managerInterfaces.put(cls, collection);
        }
        return collection;
    }

    public void initRetrofit() {
       /* OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(7, TimeUnit.MINUTES)
                .writeTimeout(7, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)

                .readTimeout(7, TimeUnit.MINUTES);
        OkHttpClient client = builder.build();*/
        OkHttpClient okHttpClient = null;
        try {
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(getSSLConfig().getSocketFactory())
                    .readTimeout(300, TimeUnit.SECONDS)
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(3, 300, TimeUnit.NANOSECONDS))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LabTabApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        labTabApiInterface = retrofit.create(LabTabApiInterface.class);
    }

    public LabTabApiInterface getLabTabApiInterface() {
        return labTabApiInterface;
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseUIListener> Collection<T> getOrCreateUIListeners(
            Class<T> cls) {
        Collection<T> collection = (Collection<T>) uiListeners.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            uiListeners.put(cls, collection);
        }
        return collection;
    }

    public <T extends BaseUIListener> Collection<T> getUIListeners(Class<T> cls) {
        if (closed)
            return Collections.emptyList();
        return Collections.unmodifiableCollection(getOrCreateUIListeners(cls));
    }

    public <T extends BaseUIListener> void addUIListener(Class<T> cls,
                                                         T listener) {
        getOrCreateUIListeners(cls).add(listener);
    }

    public <T extends BaseUIListener> void removeUIListener(Class<T> cls,
                                                            T listener) {
        getOrCreateUIListeners(cls).remove(listener);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setMetaDatas(MetaData[] metaDatas) {
        this.metaDatas = metaDatas;
    }

    public MetaData[] getMetaDatas() {
        return metaDatas;
    }

    public String[] getKnowledgeNuggets(String labLevel) {
/*        if (labLevel.toUpperCase().equals(LabTabConstants.LabLevels.NOVICE)) {
            labLevel = LabTabConstants.LabLevels.LAB_CERTIFIED;
        }*/
        String[] knowledgeNuggets = null;
        ArrayList<String> kn = new ArrayList<>();
        if (metaDatas == null) {
            return null;
        } else {
            for (int i = 0; i < metaDatas.length; i++) {
//                if (labLevel.toUpperCase().equals(metaDatas[i].getName().toUpperCase())) {
                kn.addAll(Arrays.asList(metaDatas[i].getNuggets()));
/*                    break;
                }*/
            }
            if (!kn.isEmpty()) {
                knowledgeNuggets = kn.toArray(new String[kn.size()]);
            }
        }
        return knowledgeNuggets;
    }

    public String[] getKnowledgeNuggetsByStudent(ArrayList<Student> studentList) {
        String[] knowledgeNuggets = null;
        Set<String> kn = new HashSet<>();
        ArrayList<String> knStudent = new ArrayList<>();
        if (metaDatas == null || studentList == null || studentList.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < metaDatas.length; i++) {
                for (int j = 0; j < studentList.size(); j++) {
//                    if (metaDatas[i].getName().toUpperCase().equalsIgnoreCase(studentList.get(j).getLevel())){
                    kn.addAll(Arrays.asList(metaDatas[i].getNuggets()));
//                    }
                }
            }
            if (!kn.isEmpty()) {
                knowledgeNuggets = kn.toArray(new String[kn.size()]);
            }
        }
        return knowledgeNuggets;
    }

    public LinkedHashMap<String, ArrayList<Nuggests>> getKnowledgeNuggetHashsByStudent(ArrayList<Student> studentList, String[] selected) {
//        String[] knowledgeNuggets = null;
        //      Set<String> kn = new HashSet<>();
        LinkedHashMap<String, ArrayList<Nuggests>> list = new LinkedHashMap<>();
        ArrayList<Nuggests> knStudent = new ArrayList<>();
        if (metaDatas == null || studentList == null || studentList.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < metaDatas.length; i++) {
                // for (int j = 0; j < studentList.size(); j++) {
//                    if (metaDatas[i].getName().toUpperCase().equalsIgnoreCase(studentList.get(j).getLevel())){
                //   kn.addAll(Arrays.asList(metaDatas[i].getNuggets()));
                if (list.containsKey(metaDatas[i].getName())) {
                    knStudent = list.get(metaDatas[i].getName());

                    knStudent.addAll(getNuggets(Arrays.asList(metaDatas[i].getNuggets()), selected == null ? null : Arrays.asList(selected)));
                } else {
                    knStudent = new ArrayList<>();
                    knStudent.addAll(getNuggets(Arrays.asList(metaDatas[i].getNuggets()), selected == null ? null : Arrays.asList(selected)));
                }
//                    }
                list.put(metaDatas[i].getName(), knStudent);
                //}
            }
           /* if (!kn.isEmpty()) {
                knowledgeNuggets = kn.toArray(new String[kn.size()]);
            }*/
        }
        return list;
    }

    private ArrayList<Nuggests> getNuggets(List<String> nuggrst, List<String> selected) {
        ArrayList<Nuggests> nuggestses = new ArrayList<>();
        for (String string : nuggrst) {
            boolean isAlreadySelected = false;

            if (selected != null) {
                for (String select : selected) {
                    if (select.trim().equalsIgnoreCase(string.trim())) {
                        isAlreadySelected = true;
                        break;
                    }
                }
            }
            nuggestses.add(new Nuggests(string, isAlreadySelected));
        }
        return nuggestses;
    }

    public void runOnUiThread(final Runnable runnable) {
        handler.post(runnable);
    }

    public void setCount(int countCompletedProjects, int countPendingProjects, int countSkippedProjects, int totalProjects, int noLab) {
        this.totalProjects += totalProjects;
        this.countCompletedProjects += countCompletedProjects;
        this.countSkippedProjects += countSkippedProjects;
        this.countPendingProjects += countPendingProjects;
        this.labTime += noLab;

    }

    public void setCountToZero() {
        this.totalProjects = 0;
        this.countCompletedProjects = 0;
        this.countSkippedProjects = 0;
        this.countPendingProjects = 0;
        this.labTime = 0;
    }

    public int getLabTime() {
        return labTime;
    }

    public int getTotalProjects() {
        return totalProjects;
    }

    public int getCompletedProjects() {
        return countCompletedProjects;
    }

    public int getPendingProjects() {
        return countPendingProjects;
    }

    public int getSkippedProjects() {
        return countSkippedProjects;
    }

    //Labtab Android/<Version Name> (Version Code: <>)
    public String getUserAgent(){
        String versionName = null;
        String versionCode = null;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = String.valueOf(pInfo.versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "Labtab Android/"+versionName+" (Version Code: "+versionCode+")";
    }

    private InputStream getCertificate() throws IOException {

        if((LabTabApiInterface.BASE_URL).contains("test")) {
            return this.getResources().openRawResource(R.raw.cert2);
        } else {
            return this.getResources().openRawResource(R.raw.c8e4def44eba0609);
        }
    }



    private  SSLContext getSSLConfig() throws CertificateException, IOException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // Loading CAs from an InputStream
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");

        Certificate ca = null;
        // I'm using Java7. If you used Java6 close it manually with finally.
        try {
            InputStream cert = getCertificate();
            ca = cf.generateCertificate(cert);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        // Creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }
}

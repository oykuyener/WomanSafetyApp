package com.example.iddetar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    Button bt_help,bt_kayitlar;
    String address_line;
    FusedLocationProviderClient fusedLocationProviderClient;
    FeedReaderDbHelper db;
    DbHelper dbHelper;
    String ctname,ctsur,cttc,ctaddress;
    String dbname,dbsur,dblocation;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        bt_help = (Button) v.findViewById(R.id.bt_help);
        bt_kayitlar = (Button) v.findViewById(R.id.bt_kayitlar);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(v.getContext());
        db = new FeedReaderDbHelper(getContext());
        dbHelper = new DbHelper(getContext());
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);



        bt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Onayla");
                builder.setMessage("Şiddete uğradığınızı polise haber vermek istediğinizden emin misiniz?");

                builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            getLocation();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        bt_kayitlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";
                Cursor cursor = dbHelper.viewData();
                if (cursor.moveToFirst()){
                    do {
                        // Passing values
                        dbname = cursor.getString(0);
                        dbsur = cursor.getString(1);
                        dblocation = cursor.getString(2);
                        text = text + (dbname + ", " + dbsur + ", " + dblocation + "\n");
                        // Do something Here with values
                    } while(cursor.moveToNext());
                    Dialog dialog = new Dialog(getContext());
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage(text);
                    alertDialogBuilder.setNegativeButton("tamam", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });
        return v;
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);
                        address_line = addressList.get(0).getAddressLine(0);
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                sendSms();
                            } else {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 0);
                            }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(getContext(),"Konum Alınamadı",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendSms(){
        Cursor cursor = db.viewData();
        if (cursor.moveToFirst()){
            do {
                // Passing values
                ctname = cursor.getString(0);
                ctsur = cursor.getString(1);
                cttc = cursor.getString(2);
                ctaddress = cursor.getString(3);
                // Do something Here with values
            } while(cursor.moveToNext());
            Intent intent=new Intent(getContext(),getClass());
            PendingIntent pi=PendingIntent.getActivity(getContext(), 0, intent,0);
            SmsManager sms= SmsManager.getDefault();
            sms.sendTextMessage("155", null, "Ismim " + ctname +" " +  ctsur + " siddete ugramaktayim lutfen yardim edin bulundugum adres: " + address_line, pi,null);
            dbHelper.insertData(ctname, ctsur,address_line);
            Toast.makeText(getContext(),"Mesaj Gönderildi",Toast.LENGTH_LONG).show();
        }else{
            TabLayout tabs = (TabLayout)((MainActivity)getActivity()).findViewById(R.id.tabs);
            tabs.getTabAt(0).select();
            Toast.makeText(getContext(),"Yardım çağırmadan önce bilgilerinizi almamız gerekiyor.",Toast.LENGTH_LONG).show();
        }
    }
}
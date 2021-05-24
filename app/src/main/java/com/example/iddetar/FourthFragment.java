package com.example.iddetar;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourthFragment extends Fragment {

    EditText et_name,et_surname,et_tc,et_address;
    Button bt_register,bt_delete;
    FeedReaderDbHelper db;
    String name,surname,tc,address,ctname,ctsur,cttc,ctaddress,kontrol;
    int ctid;
    int i;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FourthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FourthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FourthFragment newInstance(String param1, String param2) {
        FourthFragment fragment = new FourthFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fourth, container, false);
        db = new FeedReaderDbHelper(getContext());
        et_name = (EditText) v.findViewById(R.id.et_name);
        et_surname = (EditText) v.findViewById(R.id.et_surname);
        et_tc = (EditText) v.findViewById(R.id.et_tc);
        et_address = (EditText) v.findViewById(R.id.et_address);
        bt_register = (Button) v.findViewById(R.id.bt_register);
        bt_delete = (Button) v.findViewById(R.id.bt_delete);

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
        }

        if(i == 0 && !(ctname == null)){
            i++;
            TabLayout tabs = (TabLayout)((MainActivity)getActivity()).findViewById(R.id.tabs);
            tabs.getTabAt(1).select();
        }

        if(ctname == null){

        }else{
            et_name.setText(ctname);
            et_surname.setText(ctsur);
            et_tc.setText(cttc);
            et_address.setText(ctaddress);
        }

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = db.viewData();
                name = et_name.getText().toString();
                surname = et_surname.getText().toString();
                tc = et_tc.getText().toString();
                address = et_address.getText().toString();
                if (c.moveToFirst()){
                    do {
                        // Passing values
                        kontrol = c.getString(0);
                        ctid = c.getInt(4);
                        // Do something Here with values
                    } while(c.moveToNext());
                }

                if(name.isEmpty() || surname.isEmpty() || tc.isEmpty() || address.isEmpty()){
                    Toast.makeText(getContext(),"Lütfen tüm bilgileri giriniz.",Toast.LENGTH_LONG).show();
                }else{
                    if(kontrol == null){
                        if(db.insertData(name,surname,tc,address,1)){
                            Toast.makeText(getContext(), "Bilgiler Girildi!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Hata!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(ctid == 1){
                            if(db.updateData(name,surname,tc,address)){
                                Toast.makeText(getContext(), "Bilgiler Güncellendi!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Hata!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            if(db.insertData(name,surname,tc,address,1)){
                                Toast.makeText(getContext(), "Bilgiler Girildi!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Hata!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAll();
                et_name.setText(null);
                et_surname.setText(null);
                et_tc.setText(null);
                et_address.setText(null);
                ctid = 0;
            }
        });

        return v;
    }
}
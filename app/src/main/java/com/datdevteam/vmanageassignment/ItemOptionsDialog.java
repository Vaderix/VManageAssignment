package com.datdevteam.vmanageassignment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ItemOptionsDialog extends DialogFragment {

    Button closeButton, deleteButton, editButton, shareButton;
    VRecords vRecords, cRecord;
    String plateNum;

    MyDBHandler handler;
    SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_item_options_dialog, container, false);

        closeButton = (Button) view.findViewById(R.id.closeButton);
        deleteButton = (Button) view.findViewById(R.id.deleteButton);
        editButton = (Button) view.findViewById(R.id.editButton);
        shareButton = (Button) view.findViewById(R.id.shareButton);

        handler = new MyDBHandler(getContext(),null,null,1);
        db = handler.getWritableDatabase();

        if(!LoginSession.isActiveSession())
        {
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        }


        closeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                handler.deleteVRecord(CurrentRecord.getPlateNum());
                getDialog().dismiss();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                Intent i = new Intent(getContext(), EditVehicleScreen.class);
                startActivity(i);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                cRecord = CurrentRecord.getvRecords();

                String shareBody = "Please find the vehicle details for plate number "
                        +CurrentRecord.getPlateNum()+" below:\n"
                        +"Owner Name: "+cRecord.getOwnerName()
                        +"\nVehicle Type: "+cRecord.getVehicleType()
                        +"\nAddress: "+cRecord.getAddress()
                        +"\nMobile No: "+cRecord.getMobNum();


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Vehicle Details");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Using"));
            }
        });

        return view;
    }

    public void setRecord(VRecords vRecords, String plateNum)
    {
        this.vRecords = vRecords;
        this.plateNum = plateNum;
    }
}

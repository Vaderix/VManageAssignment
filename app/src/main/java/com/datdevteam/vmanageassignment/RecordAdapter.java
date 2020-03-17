package com.datdevteam.vmanageassignment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordAdapter extends CursorAdapter {

    ImageView plateIMG;
    TextView plateNumValue, vehicleTypeValue, ownerNameValue, mobNumValue, addressValue;

    public RecordAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.vrecords_row, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        plateNumValue = (TextView)view.findViewById(R.id.plateNumValue);
        vehicleTypeValue = (TextView)view.findViewById(R.id.vehicleTypeValue);
        ownerNameValue = (TextView)view.findViewById(R.id.ownerNameValue);
        mobNumValue = (TextView)view.findViewById(R.id.mobNumValue);
        addressValue = (TextView)view.findViewById(R.id.addressValue);
        plateIMG = (ImageView)view.findViewById(R.id.imageView);

        String plateNumValSTR = cursor.getString(cursor.getColumnIndexOrThrow("numPlate"));
        String vehicleTypeValSTR = cursor.getString(cursor.getColumnIndexOrThrow("vehicleType"));
        String ownerNameValSTR = cursor.getString(cursor.getColumnIndexOrThrow("ownerName"));
        String mobNumValSTR = cursor.getString(cursor.getColumnIndexOrThrow("mobileNum"));
        String addressValSTR = cursor.getString(cursor.getColumnIndexOrThrow("address"));
        byte[] imgByte = cursor.getBlob(cursor.getColumnIndexOrThrow("plateIMG"));

        plateNumValue.setText(plateNumValSTR);
        vehicleTypeValue.setText(vehicleTypeValSTR);
        ownerNameValue.setText(ownerNameValSTR);
        mobNumValue.setText(mobNumValSTR);
        addressValue.setText(addressValSTR);

        Bitmap bmpIMG = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        plateIMG.setImageBitmap(bmpIMG);

    }
}

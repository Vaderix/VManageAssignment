package com.datdevteam.vmanageassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class EditVehicleScreen extends AppCompatActivity {

    TextView pleaseCapText;
    ImageView capturedImage;
    EditText plateNumText;
    ImageButton editNumButton;
    RadioButton radioTwoWh, radioFourWh;
    EditText ownerValue, addressValue, mobNoValue;
    Bitmap numPlatePhoto;

    MyDBHandler dbHandler;
    VRecords cRecord;

    static final int CAM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle_screen);

        cRecord = CurrentRecord.getvRecords();

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pleaseCapText = (TextView) findViewById(R.id.pleaseCapText);
        capturedImage = (ImageView) findViewById(R.id.capturedImage);
        plateNumText = (EditText) findViewById(R.id.plateNumText);
        editNumButton = (ImageButton) findViewById(R.id.editNumButton);

        radioTwoWh = (RadioButton) findViewById(R.id.radioTwoWh);
        radioFourWh = (RadioButton) findViewById(R.id.radioFourWh);

        ownerValue = (EditText) findViewById(R.id.ownerValue);
        addressValue = (EditText) findViewById(R.id.addressValue);
        mobNoValue = (EditText) findViewById(R.id.mobNoValue);

        dbHandler= new MyDBHandler(this,null,null,1);

        putInValues();

        plateNumText.setEnabled(false);

        InputFilter[] editFilters = plateNumText.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        plateNumText.setFilters(newFilters);
    }

    public void putInValues()
    {
        plateNumText.setText(cRecord.getNumPlate());
        ownerValue.setText(cRecord.getOwnerName());
        addressValue.setText(cRecord.getAddress());
        mobNoValue.setText(cRecord.getMobNum());
        numPlatePhoto = cRecord.getPlateIMG();

        String vType = cRecord.getVehicleType();

        if(vType.equals("4-Wheeler"))
        {
            radioFourWh.setChecked(true);
        }
        else
            radioTwoWh.setChecked(true);

        capturedImage.setImageBitmap(numPlatePhoto);

    }

    public void scanImage(Bitmap pic)
    {
        final StringBuilder endResult = new StringBuilder(10);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(pic);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {

                                for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();
                                    endResult.append(text);
                                }
                                //
                                int j = 0;
                                for(int i = 0; i < endResult.length(); i++) {
                                    if (!Character.isWhitespace(endResult.charAt(i))) {
                                        endResult.setCharAt(j++, endResult.charAt(i));
                                    }
                                }
                                endResult.delete(j, endResult.length());
                                //
                                plateNumText.setText(endResult.toString());
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

    }

    public void onCapture(View v)
    {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent,CAM_REQUEST);
    }

    public void onEdit(View v)
    {
        if(plateNumText.isEnabled())
        {
            plateNumText.setEnabled(false);
            editNumButton.setImageResource(R.drawable.edit_icon_image);
            plateNumText.setTextColor(Color.parseColor("#e53935"));
            plateNumText.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }

        else
        {
            plateNumText.setEnabled(true);
            editNumButton.setImageResource(R.drawable.edit_confirm_icon_image);
            plateNumText.setTextColor(Color.parseColor("#43A047"));
            plateNumText.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void onSave(View v)
    {
        if((!plateNumText.getText().toString().equals(CurrentRecord.getPlateNum()))&&dbHandler.plateExists(plateNumText.getText().toString()))
        {
            Toast.makeText(this, "Record for plate number "+plateNumText.getText().toString()+" already exists!", Toast.LENGTH_LONG).show();
        }
        else
        {
            String vhType;
            Boolean selectedRadio = true;
            Boolean plateNotNull= true;

            if(radioFourWh.isChecked())
                vhType="4-Wheeler";
            else if(radioTwoWh.isChecked())
                vhType="2-Wheeler";
            else
            {
                vhType="Error";
                selectedRadio= false;
            }

            if(plateNumText.getText().toString().equals(""))
            {
                Toast.makeText(this, "Please enter plate number!", Toast.LENGTH_LONG).show();
                plateNotNull= false;
            }

            if(selectedRadio && plateNotNull)
            {
                VRecords vRecords= new VRecords(
                        plateNumText.getText().toString(),
                        vhType,
                        ownerValue.getText().toString(),
                        addressValue.getText().toString(),
                        mobNoValue.getText().toString(),
                        numPlatePhoto
                );
                dbHandler.updateVRecord(vRecords);

                Toast.makeText(this, "Record Successfully Edited!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, ViewAllRecords.class);
                startActivity(i);
            }

            else
                Toast.makeText(this, "Please Select Vehicle Type!", Toast.LENGTH_SHORT).show();

        }
    }

    public void onBack(View v)
    {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAM_REQUEST)
        {
            try
            {
                Bundle extras= data.getExtras();
                numPlatePhoto= (Bitmap)extras.get("data");
                capturedImage.setImageBitmap(numPlatePhoto);
                scanImage(numPlatePhoto);

                if(plateNumText.getText().toString().equals(""))
                    plateNumText.setEnabled(true);
                else
                    plateNumText.setEnabled(false);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

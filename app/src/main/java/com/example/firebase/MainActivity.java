package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Step 1: Create object of Firebase Firestore
    private FirebaseFirestore objectFirebaseFirestore;
    private Dialog objectDialog;

    private static final String CollectionName = "NewCities";

    private EditText documentET, cityNameET, cityDetailsET;
    private TextView valuetextbox;

    DocumentReference objectDocumentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Step 2: Initialize Firebase Firestore Object
        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        objectDialog = new Dialog(this);

        documentET = findViewById(R.id.DocumentIDET);
        cityNameET = findViewById(R.id.CityName);

        cityDetailsET = findViewById(R.id.DetailsET);
        valuetextbox = findViewById(R.id.GetDataTV);

        objectDialog.setContentView(R.layout.please_wait_layout);
    }

    public void addValues(View v) {
        try {
            if (!documentET.getText().toString().isEmpty()
                    &&
                    !cityNameET.getText().toString().isEmpty()
                    &&
                    !cityDetailsET.getText().toString().isEmpty()) {
                objectDialog.show();
                // Hash Table is known as Map having key and hash vale
                // Parent data type of all data types is Object
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("city_name", cityNameET.getText().toString());

                objectMap.put("city_details", cityDetailsET.getText().toString());
                objectFirebaseFirestore.collection(CollectionName)
                        .document(documentET.getText().toString()).set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to add data", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Data should not be null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            objectDialog.dismiss();
            Toast.makeText(this, "addValues" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getData(View view) {
        try {
            objectDialog.show();
            if (!documentET.getText().toString().isEmpty()) {
                objectDocumentReference = objectFirebaseFirestore.collection("NewCities").
                        document(documentET.getText().toString());
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        objectDialog.dismiss();
                        if (documentSnapshot.exists()) {
                            valuetextbox.setText("");
                            documentET.setText("");

                            documentET.requestFocus();
                            String City = documentSnapshot.getString("city_name");
                            String Details = documentSnapshot.getString("city_details");
                            valuetextbox.setText("\nCity Name: " + City + "\n\n" + "City Details: " + Details);
                        } else {
                            Toast.makeText(MainActivity.this, "No Documents Retrieved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed To Get Values Back", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Get Data Error" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void update(View V){
        try{
            objectDialog.show();
            objectDocumentReference=objectFirebaseFirestore.collection(CollectionName).document(documentET.getText().toString());
            Map<String, Object> objectMap=new HashMap<>();
            objectMap.put("city_name", cityNameET.getText().toString());
            objectMap.put("city_details", cityDetailsET.getText().toString());

            objectDocumentReference.update(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    objectDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    objectDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Data Failed To Updated" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(Exception ex){
            Toast.makeText(this, "Update Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Remove(View V){
        try{
            objectDialog.show();
            objectDocumentReference=objectFirebaseFirestore.collection(CollectionName).document(documentET.getText().toString());
            Map<String, Object> objectMap=new HashMap<>();
//            objectMap.put("city_name", FieldValue.delete());
//            objectMap.put("city_details", FieldValue.delete());

            objectDocumentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    objectDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    objectDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Data Failed To Delete" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(Exception ex){
            Toast.makeText(this, "Update Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

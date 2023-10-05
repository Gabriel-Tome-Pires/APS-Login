package com.example.atividadeaps;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateItensFragment extends Fragment {
    EditText et_nomeObj, et_local, et_telefone, et_data;
    ImageButton imageButton;
    Button btn_enviar;

    StorageReference storageReference;
    String nomeObj, local, telefone, data;
    Bitmap imageBitmap;
    Uri imageUri;

    private static final int REQUEST_IMAGE_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_itens, container, false);



        et_data = view.findViewById(R.id.et_date);
        et_local = view.findViewById(R.id.et_local);
        et_telefone = view.findViewById(R.id.et_telefone);
        et_nomeObj = view.findViewById(R.id.et_nome_obj);
        imageButton = view.findViewById(R.id.img_btn_add);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Objeto");

        btn_enviar = view.findViewById(R.id.btn_enviar_item);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
                startActivityForResult(intent, REQUEST_IMAGE_CODE);
            }
        });

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    nomeObj = et_nomeObj.getText().toString();
                    local = et_local.getText().toString();
                    telefone = et_telefone.getText().toString();
                    data = et_data.getText().toString();

                    //String imageName = saveImage();

                    String key = databaseReference.push().getKey(); // Obtém uma chave única para o novo nó

                    databaseReference.child(key).child("name").setValue(nomeObj);
                    databaseReference.child(key).child("local").setValue(local);
                    databaseReference.child(key).child("telefone").setValue(telefone);
                    databaseReference.child(key).child("data").setValue(data);
                    //databaseReference.child(key).child("image").setValue(imageName);

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    String saveImage(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String fileName = simpleDateFormat.format(now);

        storageReference = FirebaseStorage.getInstance().getReference(fileName);

        /*storageReference.putFile(imageBitmap).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Sucesso ao enviar imagem", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Falha ao enviar imagem", Toast.LENGTH_SHORT).show();
            }
        });*/

        return fileName;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CODE){
            try {
                imageBitmap = (Bitmap) data.getExtras().get("data");
                imageButton.setImageBitmap(imageBitmap);

                WeakReference<Bitmap> reference = new WeakReference<>(Bitmap.createScaledBitmap(imageBitmap,
                        imageBitmap.getHeight(),imageBitmap.getWidth(), false)
                        .copy(Bitmap.Config.RGB_565, true));

                Bitmap bm = reference.get();
                imageUri = changeForUri(bm, getContext());

            }catch (Exception e){

            }


        }

    }

    Uri changeForUri(Bitmap bm, Context context){
        File imagesFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;

        try {
           imagesFolder.mkdirs();
           File file= new File(imagesFolder, "captured_image.jpg");
           FileOutputStream fileOutputStream= new FileOutputStream(file);
           bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
           fileOutputStream.flush();
           fileOutputStream.close();

           uri= FileProvider.getUriForFile(context.getApplicationContext(),
                   "com.example.atividadeaps"+".provider", file);


        }catch (Exception e){

        }

        return uri;
    }
}
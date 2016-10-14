package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maaaarckyo.donagoandroid.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PublicUserActivity extends Activity
{
    String txtFName, txtLName, txtMan, txtWoman, txtCountry;

    TextView tvCountry;

    EditText etFName, etLName;

    Button btnAddPhoto, btnNext;

    RadioButton rbMan, rbWoman;

    ImageView imgVPhoto;

    byte[] byteImg;

    private ParseObject publicUserProfile;

//
//    public static final String UserPUBLIC = "MyPrefs";
//
//    private SharedPreferences publicUserPREF;
//    private SharedPreferences.Editor PREFEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_user);

        btnNext = (Button) findViewById(R.id.btnNext);

        etFName = (EditText) findViewById(R.id.etFName);
        etLName = (EditText) findViewById(R.id.etLName);
        tvCountry = (TextView) findViewById(R.id.tvCountry);

        rbMan = (RadioButton) findViewById(R.id.rbMan);
        rbWoman = (RadioButton) findViewById(R.id.rbWoman);

        btnAddPhoto = (Button)findViewById(R.id.btnAddPhoto);

        imgVPhoto = (ImageView)findViewById(R.id.imgVPhoto);


//        publicUserPREF = getSharedPreferences(UserPUBLIC,MODE_PRIVATE);
//        PREFEditor = publicUserPREF.edit();

        // Saves last typed and reuse next time app opens
//        etFName.setText(userPREF.getString("firstname",""));


        btnAddPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


//        Bundle bundle = getIntent().getExtras();
//        final String publicUserId = bundle.getString("publicUserProfile");


        final ParseObject publicUserId = (ParseObject)ParseUser.getCurrentUser().get("publicUserProfile");

//        publicUserProfile = new ParseObject("PublicUserProfile");
//        try
//        {
//
//            publicUserProfile = ParseQuery.getQuery("PublicUserProfile").get(publicUserId);
//        } catch (ParseException e)
//        {
//            e.printStackTrace();
//        }



        btnNext.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                txtFName = etFName.getText().toString();
                txtLName = etLName.getText().toString();
                txtMan = rbMan.getText().toString();
                txtWoman = rbWoman.getText().toString();
                txtCountry = tvCountry.getText().toString();


                final ParseFile file = new ParseFile("img1.jpeg",byteImg);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("PublicUserProfile");
                query.getInBackground(publicUserId.getObjectId(), new GetCallback<ParseObject>()
                {
                    @Override
                    public void done(ParseObject parseObject, ParseException e)
                    {
                        if (rbMan.isSelected() && !rbWoman.isSelected())
                        {
                            publicUserProfile.put("gender", txtMan);
                        }
                        else if(rbWoman.isSelected() && !rbMan.isSelected())
                        {
                            publicUserProfile.put("gender", txtWoman);
                        }
                        publicUserId.put("firstname",txtFName);
                        publicUserId.put("lastname",txtLName);
                        publicUserId.put("countryString",txtCountry );
                        publicUserId.put("profileImage",file);
                        publicUserId.put("nonPublicUser",ParseUser.getCurrentUser());
                        publicUserId.saveInBackground(new SaveCallback()
                        {
                            @Override
                            public void done(ParseException e)
                            {
                                if (e == null)
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Profiloplysninger opdateret!",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Der gik noget galt:" + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    Log.e("ParseException", e.getMessage());
                                    finish();
                                }

                                Intent intent = new Intent(PublicUserActivity.this, PrivateUserActivity.class);
                                startActivity(intent);
                                finish();

                                  // Det her hører til det med at gemme f.eks login /etFName.setText(userPREF.getString("firstname",""));
//                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.hideSoftInputFromWindow(etFName.getWindowToken(),0);

//                                PREFEditor.putString("publicUserId",publicUserId);
//                                PREFEditor.putString("userId",userId);
//                                PREFEditor.commit();
                            }
                        });
                    }
                });
            }
        });
    }


    private void selectImage()
    {
        final CharSequence[] options = {"Tag nyt foto!", "Vælg fra gallery!", "Luk..."};
        AlertDialog.Builder builder = new AlertDialog.Builder(PublicUserActivity.this);
        builder.setTitle("Tilføj foto!");
        builder.setItems(options, new DialogInterface.OnClickListener()
        {
            @Override
            public void  onClick(DialogInterface dialog, int item)
            {
                if(options[item].equals("Tag nyt foto!"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if(options[item].equals("Vælg fra gallery!"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,2);
                }
                else if(options[item].equals("Luk..."))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles())
                {
                    if (temp.getName().equals("temp.jpeg"))
                    {
                        f = temp;
                        break;
                    }
                }
                try
                {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    imgVPhoto.setImageBitmap(bitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                    byteImg = stream.toByteArray();
                }
                catch (Exception e)
                {

                }
            }
            else if (requestCode == 2)
            {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                File fileP = new File(picturePath);
                if(fileP.exists())
                {
                    byteImg = bos.toByteArray();
                }



                Bitmap bmp = (BitmapFactory.decodeFile(picturePath));
                bmp.compress(Bitmap.CompressFormat.JPEG,85,bos);
                Log.w("TomHanks", picturePath + "");
                imgVPhoto.setImageBitmap(bmp);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                byteImg = stream.toByteArray();
            }
        }
    }
}

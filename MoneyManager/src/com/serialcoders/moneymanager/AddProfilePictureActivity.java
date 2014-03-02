package com.serialcoders.moneymanager;

import java.io.ByteArrayOutputStream;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AddProfilePictureActivity extends Activity{
	
	private static final int CAMERA_REQUEST = 1888;
	private ParseFile profilePhotoFile;
    private Camera camera;
    private Bitmap profilePicture;
    private ImageView profilePictureView;
    private ParseUser currentUser;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_profilepic);

		Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
		
		
		
		this.profilePictureView = (ImageView)this.findViewById(R.id.profilepicture);
        findViewById(R.id.takepicture).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 	
			}
		});
		
        
	    
        
        
        findViewById(R.id.acceptpicture).setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View view) {    		
        		if (profilePicture != null){ 
        			currentUser = ParseUser.getCurrentUser();
        			ByteArrayOutputStream stream = new ByteArrayOutputStream();
        			profilePicture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        			byte[] data = stream.toByteArray();
        			profilePhotoFile = new ParseFile("profilePhoto.jpeg", data);
        			profilePhotoFile.saveInBackground();
        			currentUser.put("profilePhoto", profilePhotoFile);
        			currentUser.saveInBackground();               
        		}
                
                Toast.makeText(AddProfilePictureActivity.this, "Profile picture updated successfully", Toast.LENGTH_LONG).show();
            	Intent i = new Intent(AddProfilePictureActivity.this, UserAccountActivity.class);
				startActivity(i);
			}
        });

			
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            profilePicture = (Bitmap) data.getExtras().get("data"); 
            profilePictureView.setImageBitmap(profilePicture);
        }  
    } 
	
}

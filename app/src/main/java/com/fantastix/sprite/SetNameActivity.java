/**
 * Copyright (c) 2013 Unclouded.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.fantastix.sprite;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.fantastix.android.sprite.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetNameActivity extends SpriteActivity {
	private static final int TAKE_AVATAR_CAMERA_REQUEST = 1;
	private static final int TAKE_AVATAR_GALLERY_REQUEST = 2;
	private SharedPreferences settings;
	private Button login;
	private TextView loading, mySelection;
	private Context mContext;
	private Activity mActivity;
	private PopupWindow mPopupWindow;
	private RelativeLayout mRelativeLayout;
	private DatePicker currentYear;
	private Spinner mAge;
	private String birthYear, nickname;
	private EditText nameField;
    private Gallery myGallery;
    private ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joinin);

		EditText nameField = (EditText) findViewById(R.id.name_field);
    
		//Get reference of widgets from XML layout
		final Spinner mGender = (Spinner)findViewById(R.id.gender_field);
		//Initializing a String Array
		String[] gender = new String[]{
				"Select an gender...",
				"Male",
				"Female"};
		final List<String>genderList = new ArrayList<>(Arrays.asList(gender));
		//Initializing an ArrayAdapter
		final ArrayAdapter<String>spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, genderList){
			@Override
			public boolean isEnabled(int position){
				if (position == 0){
					// Disable the first item from Spinner
					//First item will be used for int
					return false;
				}else {
					return true;
				}
			}
			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent){
				View view = super.getDropDownView(position, convertView, parent);
				TextView tv = (TextView)view;
				if (position == 0){
					//Set the int text color gray
					tv.setTextColor(Color.GRAY);
				}else {
					tv.setTextColor(Color.BLACK);
				}
				return view;
			}
		};
		spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
		mGender.setAdapter(spinnerArrayAdapter);
		mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selectedGender = (String)parent.getItemAtPosition(position);
				//If user changer the default selection
				//First item is disable and it is used for hint
				if (position > 0){
					//Notify the selected item gender
					//Toast.makeText(getApplicationContext(), "Oh! you're a " + selectedGender + ":)", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?>parent){}
		});

		//Get reference of widgets from XML layout
		final Spinner mAge = (Spinner)findViewById(R.id.age_field);
		//Initializing a String Array
		String[]age = new String[]{
				"Select an gender...",
				"Male",
				"Female"};
		final List<String>ageList = new ArrayList<>(Arrays.asList(age));
		//Initializing an ArrayAdapter
		final ArrayAdapter<String>ageSpinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, ageList){
			@Override
			public boolean isEnabled(int position){
				if (position == 0){
					// Disable the first item from Spinner
					//First item will be used for int
					return false;
				}else {
					return true;
				}
			}
			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent){
				View view = super.getDropDownView(position, convertView, parent);
				TextView tv = (TextView)view;
				if (position == 0){
					//Set the int text color gray
					tv.setTextColor(Color.GRAY);
				}else {
					tv.setTextColor(Color.BLACK);
				}
				return view;
			}
		};
		ageSpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
		mAge.setAdapter(ageSpinnerArrayAdapter);
		mAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Integer selectedAge = (Integer) parent.getItemAtPosition(position);
				TextView _myAgeText = (TextView)findViewById(R.id.myAge);
				//If user changer the default selection
				//First item is disable and it is used for hint
				if (position > 0){
					_myAgeText.setText("");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?>parent){}
		});

		//mAge = (Spinner)findViewById(R.id.age_field);
		mAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getBaseContext(), mAge.getSelectedItem().toString(),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

        final int[] myImageIds = {
                R.mipmap.map,
                R.mipmap.winston,
                R.mipmap.music,
                R.mipmap.setting,
                R.mipmap.tool_box,
                R.mipmap.movie_studio,
                R.mipmap.theme_googlesearch,
                R.mipmap.emoji
        };

//        mySelection = (TextView) findViewById(R.id.textView2);
//        myGallery = (Gallery) findViewById(R.id.myGallery);
//        img = (ImageView) findViewById(R.id.imageView1);

//        myGallery.setAdapter(new ImageAdapter(this));

        // take avatar
		Intent avatarCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(avatarCam, 1);

        // Pick avatar
		Intent pickPhoto = new Intent(Intent.ACTION_PICK);
		pickPhoto.setType("image/*");
		startActivityForResult(pickPhoto, TAKE_AVATAR_GALLERY_REQUEST);

        myGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                mySelection.setText(" selected option: " + position );
                img.setImageResource(myImageIds[position]);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                mySelection.setText("Nothing selected");
            }
        });


		mContext = getApplicationContext();
		mActivity = SetNameActivity.this;
//		mRelativeLayout = (RelativeLayout)findViewById(R.id.popup);

		login = (Button) findViewById(R.id.signin_button);
		login.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {

				// Set pref
				settings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
				SharedPreferences.Editor prefEditor = settings.edit();

				//---save the values in the EditText view to preferences---
				// prefEditor.putString("Nickname", nickname);
				prefEditor.putString("Nickname", "Kitten");
				//prefEditor.putInt("Age", Integer.parseInt(age.getText().toString()));
				prefEditor.putInt("Age", 19);
				//---saves the values---
				prefEditor.commit();

				Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
			
				/**if (name == null){
					LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
					//Inflate the custom layout/view
					View popover = inflater.inflate(R.layout.popover, null);
					mPopupWindow = new PopupWindow(popover, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				} else {
					startChatbot(name);
				}*/
				
				nickname = nameField.getText().toString();
				startMessenger(nickname);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_splash, menu);
		return true;
	}

	public String getCurrentUser(){
		// retrieve pref
		if (settings.contains("Nickname") == true) {
			// We have a nickname
			String user = settings.getString("Nickname", "Default");
			return user;
		}
        return null;
    }

	private void startMessenger(String name){
//		Intent i = new Intent(this, MessengerActivity.class);
//		Bundle b = new Bundle();
//		b.putString("nickname", nameField.getText().toString());
//		i.putExtra("NAME", name);
//		startActivity(i);
//		finish(); // SetNameActivity.this.finish();
	}

	private void startMainActivity(String name){
	   Intent intent = new Intent(this, MainActivity.class);
	   intent.putExtra("NAME", name);
	   startActivity(intent);
	   finish();
	}

	public static int calculateAge(int currentYear, int birthYear){
		int age = currentYear - birthYear;
		return age;
	}

	void age(){
		TextView age = (TextView)findViewById(R.id.myAge);
		birthYear = mAge.getSelectedItem().toString();
		currentYear.getYear();
		int myage = calculateAge(2017, 1999);
		age.setText(myage);

		/**
				currentYear = 2017;
				birthYear = 1999;
				age = currentYear - birthYear;
		 */
	}

    public class ImageAdapter extends BaseAdapter {
        private Context myContext;
        private int[] myImageIds = {
                R.mipmap.map,
                R.mipmap.winston,
                R.mipmap.music,
                R.mipmap.setting,
                R.mipmap.tool_box,
                R.mipmap.movie_studio,
                R.mipmap.theme_googlesearch,
                R.mipmap.emoji
        };

        public ImageAdapter(Context c) {
            this.myContext = c;
        }

        public int getCount() {
            return this.myImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
        // Returns a new ImageView to be displayed,
        public View getView(int position, View convertView,
                            ViewGroup parent) {

            ImageView iv = new ImageView(this.myContext);
            iv.setImageResource(this.myImageIds[position]);

            iv.setScaleType(ImageView.ScaleType.FIT_END);

            // Set the Width & Height of the individual images
            iv.setLayoutParams(new Gallery.LayoutParams(95, 70));

            return iv;
        }

//        public void slide(View view){
//            ImageView iv = (ImageView)findViewById(R.id.imagebrowser_iv_download);
//            Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
//            iv.startAnimation(a);
//        }
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
//		ImageView userImage = (ImageView)findViewById(R.id.current_active_user_avatar);
//		TextView userName = (TextView)findViewById(R.id.current_active_user_id);
//		Bitmap m = (Bitmap)data.getExtras().get("data");
//		String n = null;
//		userImage.setImageBitmap(m);
//		userImage.setScaleType(ImageView.ScaleType.FIT_XY);
//		userName.setText(n);

		switch (requestCode) {
			case TAKE_AVATAR_GALLERY_REQUEST:
				if (resultCode == Activity.RESULT_CANCELED) {
					// Avatar camera mode was cancelled
				} else if (resultCode == Activity.RESULT_OK) {
					// TODO: Handle photo taken

				}
				break;
			case TAKE_AVATAR_CAMERA_REQUEST:
				if (resultCode == Activity.RESULT_CANCELED) {
					// Avatar camera mode was cancelled
				} else if (resultCode == Activity.RESULT_OK) {
					// TODO: Handle photo taken

				}
				break;
			default:
				break;
		}
	}

}

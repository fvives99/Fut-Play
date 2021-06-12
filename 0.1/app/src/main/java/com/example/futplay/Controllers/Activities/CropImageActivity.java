package com.example.futplay.Controllers.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.futplay.R;
import com.fenchtose.nocropper.CropperView;

import static com.example.futplay.Controllers.Fragments.ProfileFragment.profileImage;
import static com.example.futplay.Controllers.Fragments.TeamsFragment.teamProfileImage;

public class CropImageActivity extends AppCompatActivity {

    private CropperView cropperView;

    private ImageView imgVwCropImageRotate;
    private ImageView imgVwCropImageScrn1;
    private ImageView imgVwCropImageScrn2;

    private TextView txtVwCropImageCrop;

    private Bitmap image;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        viewsMatching();
        cropperViewSetUp();
        setUpListeners();
        initAnims();
    }

    @Override
    public void onBackPressed() {
        Intent menu = new Intent(this, MenuActivity.class);
        menu.putExtra("type", type);
        startActivity(menu);
        this.finish();
    }

    private void viewsMatching() {
        cropperView = this.findViewById(R.id.cropperView);

        imgVwCropImageRotate = this.findViewById(R.id.imgVwCropImageRotate);
        imgVwCropImageScrn1 = this.findViewById(R.id.imgVwCropImageScrn1);
        imgVwCropImageScrn2 = this.findViewById(R.id.imgVwCropImageScrn2);

        txtVwCropImageCrop = this.findViewById(R.id.txtVwCropImageCrop);

        Intent cropImage = getIntent();
        type = cropImage.getStringExtra("type");
        switch (type) {
            case "profile":
                image = profileImage;
                break;
            case "teams":
                image = teamProfileImage;
                break;
        }

        cropperView.setImageBitmap(image);
    }

    private void cropperViewSetUp() {
        cropperView.setGestureEnabled(true);
        cropperView.cropToCenter();
    }

    private void setUpListeners() {
        txtVwCropImageCropOnClickListener();
        imgVwCropImageRotateOnClickListener();
    }

    private void txtVwCropImageCropOnClickListener() {
        txtVwCropImageCrop.setOnClickListener(view -> {
            cropImage();
            onBackPressed();
        });
    }

    private void imgVwCropImageRotateOnClickListener() {
        imgVwCropImageRotate.setOnClickListener(view -> cropperView.setImageBitmap(rotateBitmap(image)));
    }

    private Bitmap rotateBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        image = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return image;
    }

    private void cropImage() {
        image = cropperView.getCroppedBitmap();
    }

    private void initAnims() {
        Animation botLeftAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_left_animation);
        Animation topRightAnim = AnimationUtils.loadAnimation(this, R.anim.top_right_animation);
        imgVwCropImageScrn1.setAnimation(botLeftAnim);
        imgVwCropImageScrn2.setAnimation(topRightAnim);
    }
}
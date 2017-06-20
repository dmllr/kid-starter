package com.armedarms.kidstarter.contacts;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.armedarms.kidstarter.R;
import com.armedarms.kidstarter.model.Contact;
import com.armedarms.kidstarter.utils.MediaUtils;
import com.gc.materialdesign.views.Switch;

import java.io.File;
import java.io.IOException;

public class ContactDialog extends Dialog {

    public static final int REQ_CODE_PICK_IMAGE = 1000;
    public static final String TEMP_PHOTO_FILE = "temporary_avatar_holder.jpg";

    private static ContactDialog instance;

    private final Context context;
    private final Contact contact;

    private ResultListener listener;

    private final ImageView imageAvatar;
    private final EditText textName;
    private final EditText textPhone;
    private final Switch switchEmergency;

    public static ContactDialog getInstance() {
        return instance;
    }


    public ContactDialog(Context context, Contact contact) {
        super(context);

        instance = this;

        this.context = context;
        this.contact = contact;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_contact, null);

        imageAvatar = (ImageView)view.findViewById(android.R.id.icon);
        textName = (EditText)view.findViewById(R.id.textName);
        textPhone = (EditText)view.findViewById(R.id.textPhone);
        switchEmergency = (Switch)view.findViewById(R.id.switchEmergency);

        textName.setText(this.contact.name);
        textPhone.setText(this.contact.phoneNumber);
        switchEmergency.setChecked(this.contact.isEmergency);

        if (this.contact.avatarPath != null && !this.contact.avatarPath.equals("")) {
            switch (this.contact.avatarPath) {
                case "mama":
                    imageAvatar.setImageResource(R.drawable.mama);
                    break;
                case "papa":
                    imageAvatar.setImageResource(R.drawable.papa);
                    break;
                default:
                    imageAvatar.setImageBitmap(BitmapFactory.decodeFile(this.contact.avatarPath));
                    break;
            }
        } else {
            imageAvatar.setImageResource(R.drawable.contact);
        }

        view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactDialog.this.contact.name = textName.getText().toString();
                ContactDialog.this.contact.phoneNumber = textPhone.getText().toString();
                ContactDialog.this.contact.isEmergency = switchEmergency.isChecked();

                listener.onContactDialogInsert(ContactDialog.this.contact);
                dismiss();
            }
        });
        view.findViewById(android.R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(android.R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactDialogRemove(ContactDialog.this.contact);
                dismiss();
            }
        });
        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAvatar();
            }
        });

        setTitle(this.contact.name);
        setContentView(view);
    }

    private void pickAvatar() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra("crop", "true");
        photoPickerIntent.putExtra("aspectX", 1);
        photoPickerIntent.putExtra("aspectY", 1);
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        ((Activity)context).startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);
    }

    private File getTempFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File file = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return file;
        } else {

            return null;
        }
    }

    public ContactDialog withListener(ResultListener listener) {
        this.listener = listener;
        return this;
    }

    public void setAvatar(Bitmap bitmap) {
        String dir = String.format("%s/Android/data/%s/files/avatar", Environment.getExternalStorageDirectory().getAbsolutePath(), context.getPackageName());
        new File(dir).mkdirs();

        contact.avatarPath = String.format("%s/%s.png", dir, contact.uuid.toString());
        MediaUtils.saveBitmap(contact.avatarPath, bitmap);

        imageAvatar.setImageBitmap(bitmap);
    }

    public ContactDialog canRemove(boolean can) {
        findViewById(android.R.id.button3).setVisibility(View.GONE);
        return this;
    }

    public interface ResultListener {
        public abstract void onContactDialogInsert(Contact contact);
        public abstract void onContactDialogRemove(Contact contact);
    }
}

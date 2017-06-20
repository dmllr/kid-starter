package com.armedarms.kidstarter.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.armedarms.kidstarter.App;
import com.armedarms.kidstarter.R;
import com.armedarms.kidstarter.model.Contact;
import com.armedarms.kidstarter.model.Contacts;
import com.armedarms.kidstarter.utils.MediaUtils;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum Type {
        CONTACT,
        CONTROL_ADD
    }

    private final Context context;
    private LayoutInflater inflater;

    public ContactsAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == Type.CONTROL_ADD.ordinal())
            return getControlViewHolder(parent);
        if (viewType == Type.CONTACT.ordinal())
            return getContactViewHolder(parent);

        throw new UnsupportedOperationException(String.format("onCreateViewHolder(parent, %d) has unknown viewType", viewType));
    }

    private RecyclerView.ViewHolder getControlViewHolder(ViewGroup parent) {
        return new ControlViewHolder(inflater.inflate(R.layout.item_contact_add, parent, false));
    }

    private RecyclerView.ViewHolder getContactViewHolder(ViewGroup parent) {
        return new ContactViewHolder(inflater.inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContactViewHolder)
            bindContactViewHolder((ContactViewHolder) holder, position);
    }

    private void bindContactViewHolder(ContactViewHolder holder, int position) {
        Contact c = Contacts.all().get(position);
        holder.text.setText(c.name);
        holder.itemView.setTag(c);

        if (c.avatarPath != null && !c.avatarPath.equals("")) {
            switch (c.avatarPath) {
                case "mama":
                    holder.avatar.setImageResource(R.drawable.mama);
                    break;
                case "papa":
                    holder.avatar.setImageResource(R.drawable.papa);
                    break;
                default:
                    holder.avatar.setImageBitmap(BitmapFactory.decodeFile(c.avatarPath));
                    break;
            }
        } else {
            holder.avatar.setImageResource(R.drawable.contact);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == Contacts.all().size())
            return Type.CONTROL_ADD.ordinal();
        else
            return Type.CONTACT.ordinal();
    }

    @Override
    public int getItemCount() {
        return Contacts.all().size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView avatar;

        public ContactViewHolder(final View view) {
            super(view);

            text = (TextView)view.findViewById(android.R.id.text1);
            avatar = (ImageView) view.findViewById(android.R.id.icon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Contact contact = (Contact) v.getTag();
                    if (App.getApp().isUserParent)
                        editContact(contact);
                    else
                        callContact(contact, view);
                }
            });
        }
    }

    private void callContact(Contact contact, View view) {
        if (contact != null && contact.phoneNumber != null && !contact.phoneNumber.equals("") && !contact.phoneNumber.equals("0")) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contact.phoneNumber));
            context.startActivity(callIntent);
        } else {
            MediaUtils.animateNegative(view);
        }
    }

    private void editContact(Contact contact) {
        new ContactDialog(context, contact)
                .withListener(new ContactDialog.ResultListener() {
                    @Override
                    public void onContactDialogInsert(Contact contact) {
                        contact.save();
                        notifyItemChanged(Contacts.all().indexOf(contact));
                    }

                    @Override
                    public void onContactDialogRemove(Contact contact) {
                        int position = Contacts.all().indexOf(contact);
                        Contacts.remove(contact);
                        if (position > -1)
                            notifyItemRemoved(position);
                    }
                })
                .show();
    }

    class ControlViewHolder extends RecyclerView.ViewHolder {
        ImageView button;

        public ControlViewHolder(View view) {
            super(view);

            button = (ImageView) view.findViewById(android.R.id.icon);
        }
    }
}

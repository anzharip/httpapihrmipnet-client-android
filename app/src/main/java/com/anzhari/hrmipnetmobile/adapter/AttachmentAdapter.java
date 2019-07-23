package com.anzhari.hrmipnetmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anzhari.hrmipnetmobile.R;
import com.anzhari.hrmipnetmobile.model.Attachment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private Context context;
    private List<Attachment> attachments;
    private onItemClickListener clickListener;

    public AttachmentAdapter(Context context, List<Attachment> attachments) {
        this.context = context;
        this.attachments = attachments;
    }

    public interface onItemClickListener {
        void onItemClick(View view, int position, Attachment result);
    }

    public void setOnItemClickListener(final onItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_attachment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attachment attachment = attachments.get(position);
        holder.txtFileName.setText(attachment.getFileName());
        holder.txtDescription.setText(attachment.getComment());
        holder.txtType.setText(attachment.getType());
        holder.txtSize.setText(attachment.getSize());
        holder.txtDateAdded.setText(attachment.getDateAdded());

        holder.btnDownload.setOnClickListener(v -> {
            if (clickListener != null){
                clickListener.onItemClick(v, position, attachment);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (clickListener != null){
                clickListener.onItemClick(v, position, attachment);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (clickListener != null){
                clickListener.onItemClick(v, position, attachment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_file_name)
        TextView txtFileName;

        @BindView(R.id.txt_description)
        TextView txtDescription;

        @BindView(R.id.txt_type)
        TextView txtType;

        @BindView(R.id.txt_size)
        TextView txtSize;

        @BindView(R.id.txt_date_added)
        TextView txtDateAdded;

        @BindView(R.id.btn_download)
        Button btnDownload;

        @BindView(R.id.btn_edit)
        Button btnEdit;

        @BindView(R.id.btn_delete)
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

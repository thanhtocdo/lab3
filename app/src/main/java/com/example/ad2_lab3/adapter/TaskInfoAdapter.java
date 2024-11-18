package com.example.ad2_lab3.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad2_lab3.DAO.TaskInfoDAO;
import com.example.ad2_lab3.R;
import com.example.ad2_lab3.model.TaskInFo;

import java.util.ArrayList;

public class TaskInfoAdapter extends RecyclerView.Adapter<TaskInfoAdapter.ViewHolderInfo>{
    Context context;
    ArrayList<TaskInFo> lst;
    TaskInfoDAO taskInfoDAO;

    public TaskInfoAdapter(Context context, ArrayList<TaskInFo> lst) {
        this.context = context;
        this.lst = lst;
        taskInfoDAO = new TaskInfoDAO(context);
    }

    @NonNull
    @Override
    public ViewHolderInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tsinf,parent, false);
        return new ViewHolderInfo(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInfo holder, @SuppressLint("RecyclerView") int position) {
        holder.txtContent.setText(lst.get(position).getContent());
        holder.txtDate.setText(lst.get(position).getDate());

        if (lst.get(position).getStatus() == 0){
            holder.chkTask.setChecked(false);
            holder.txtContent.setPaintFlags(holder.txtContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }else {
            holder.chkTask.setChecked(true);
            holder.txtContent.setPaintFlags(holder.txtContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.chkTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int id = lst.get(holder.getAdapterPosition()).getId();
                boolean checkRS  = taskInfoDAO.updateTypeInfo(id, holder.chkTask.isChecked());
                boolean chk_result = taskInfoDAO.updateTypeInfo(id, holder.chkTask.isChecked());
                if (chk_result){
                    lst.clear();
                    lst = taskInfoDAO.getListInfo();
                    notifyDataSetChanged();
                }
            }
        });
        holder.imgDelete.setOnClickListener(view -> {
            int id = lst.get(holder.getAdapterPosition()).getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete information");
            builder.setIcon(R.drawable.baseline_warning_24);
            builder.setMessage("Are you sure");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    boolean checkk = taskInfoDAO.removeInfo(id);
                    if (checkk){
                        Toast.makeText(context, "Delete complete", Toast.LENGTH_SHORT).show();
                        lst.clear();
                        lst = taskInfoDAO.getListInfo();
                        notifyItemRemoved(holder.getAdapterPosition());
                    }else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        holder.imgUpdate.setOnClickListener(view -> {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View view1 = inflater.inflate(R.layout.custom_dialog, null);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setView(view1);
            EditText edtId, edttitle, edtcontent, edtdate, edttype;
            edtId = view1.findViewById(R.id.edtID);
            edttitle = view1.findViewById(R.id.edttitle);
            edtcontent = view1.findViewById(R.id.edtcontent);
            edtdate = view1.findViewById(R.id.edtdate);
            edttype = view1.findViewById(R.id.edttype);

            edtId.setText(String.valueOf(lst.get(position).getId()));
            edttitle.setText(lst.get(position).getTitle());
            edtcontent.setText(lst.get(position).getContent());
            edtdate.setText(lst.get(position).getDate());
            edttype.setText(lst.get(position).getType());
            builder.setTitle("Update Infomation");
            builder.setIcon(R.drawable.baseline_info_24);
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    TaskInFo inFo = new TaskInFo();
                    inFo.setId(Integer.parseInt(edtId.getText().toString().trim()));
                    inFo.setTitle(edttitle.getText().toString().trim());
                    inFo.setContent(edtcontent.getText().toString().trim());
                    inFo.setDate(edtdate.getText().toString().trim());
                    inFo.setType(edttype.getText().toString().trim());

                    edttype.setOnClickListener(view2 -> {
                        String[] arrType = {"Hard", "Medium", "Easy"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Choose the level");
                        builder.setIcon(R.drawable.baseline_info_24);
                        builder.setItems(arrType, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                edttype.setText(arrType[i]);
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    });

                    long check = taskInfoDAO.updateInfo(inFo);
                    if (check < 0){
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "Update complete", Toast.LENGTH_SHORT).show();
                    }

                    lst.set(position, inFo);
                    notifyItemChanged(holder.getAdapterPosition());

                }
            });
            builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    public class ViewHolderInfo extends RecyclerView.ViewHolder{
        TextView txtContent, txtDate;
        CheckBox chkTask;
        ImageView imgUpdate, imgDelete;

        public ViewHolderInfo(@NonNull View itemView){
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtDate = itemView.findViewById(R.id.txtDate);
            chkTask = itemView.findViewById(R.id.chkTask);
            imgUpdate = itemView.findViewById(R.id.imgUpdate);
            imgDelete = itemView.findViewById(R.id.imgDelete);

        }
    }
}

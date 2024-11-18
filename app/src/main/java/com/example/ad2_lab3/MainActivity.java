package com.example.ad2_lab3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad2_lab3.DAO.TaskInfoDAO;
import com.example.ad2_lab3.adapter.TaskInfoAdapter;
import com.example.ad2_lab3.model.TaskInFo;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    TaskInfoDAO dao;
    ArrayList<TaskInFo> lst;
    RecyclerView rcvTask;
    TaskInfoAdapter adapter;
    EditText edtID, edttitle, edtcontent, edtdate, edttype;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();

        dao = new TaskInfoDAO(this);
        lst = dao.getListInfo();
        adapter = new TaskInfoAdapter(MainActivity.this, lst);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(adapter);

        btnAdd.setOnClickListener(view -> {
            String title = edttitle.getText().toString().trim();
            String content =edtcontent.getText().toString().trim();
            String date = edtdate.getText().toString().trim();
            String type = edttype.getText().toString().trim();
            if (title.isEmpty() || content.isEmpty()|| date.isEmpty() || type.isEmpty()){
                Toast.makeText(this, "Input", Toast.LENGTH_SHORT).show();
                if (title.isEmpty()){
                    edttitle.setError("Enter title");
                } if (content.isEmpty()){
                    edtcontent.setError("Content title");
                } if (date.isEmpty()){
                    edtdate.setError("Date title");
                } if (type.isEmpty()){
                    edttype.setError("Type title");
                }
            }else {
                TaskInFo inFo = new TaskInFo(1, title, content, date, type, 0);
                long check = dao.addInfo(inFo);
                if (check < 0){
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Complete", Toast.LENGTH_SHORT).show();
                }
                lst = dao.getListInfo();
                adapter = new TaskInfoAdapter(MainActivity.this, lst);
                rcvTask.setLayoutManager(linearLayoutManager);
                rcvTask.setAdapter(adapter);
                reset();
            }

        });
        edttype.setOnClickListener(view -> {
            String[] arrType = {"Hard", "Medium", "Easy"};
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
    }

    public void initGui(){
        rcvTask = findViewById(R.id.rcvTask);
        edtID = findViewById(R.id.edtID);
        edttitle = findViewById(R.id.edttitle);
        edtcontent = findViewById(R.id.edtcontent);
        edtdate = findViewById(R.id.edtdate);
        edttype = findViewById(R.id.edttype);
        btnAdd = findViewById(R.id.btnADD);

    }
    public void loadData(ArrayList<TaskInFo> lst){

    }

    public void reset(){
        edttitle.setText("");
        edtcontent.setText("");
        edtdate.setText("");
        edttype.setText("");
        edtID.setText("");
    }
}
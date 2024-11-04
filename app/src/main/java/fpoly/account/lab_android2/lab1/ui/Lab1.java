package fpoly.account.lab_android2.lab1.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fpoly.account.lab_android2.R;
import fpoly.account.lab_android2.lab1.adapter.ListViewAdapter;
import fpoly.account.lab_android2.lab1.dao.ToDoDAO;
import fpoly.account.lab_android2.lab1.model.ToDo;

public class Lab1 extends AppCompatActivity {
    private ToDoDAO toDoDAO;
    private ListViewAdapter adapter;
    private ListView listView;
    private Button btnUpdate, btnDelete, btnAdd;
    private int selectedToDoId = -1;
    private EditText edtTittle, edtContent, edtDate, edtType;
    private ArrayList<ToDo> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);

        initView();
        toDoDAO = new ToDoDAO(this);
        tasks = toDoDAO.getListToDo();
        setAdapter();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ToDo selectedToDo = tasks.get(position);
            selectedToDoId = selectedToDo.getId();
            edtTittle.setText(selectedToDo.getTitle());
            edtContent.setText(selectedToDo.getContent());
            edtDate.setText(selectedToDo.getDate());
            edtType.setText(selectedToDo.getType());
        });

        addData();
        clickUpdate();
        clickDelete();
    }

    private void clickDelete() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedToDoId != -1) {
                    boolean isDeleted = toDoDAO.deleteToDo(selectedToDoId);
                    if (isDeleted) {
                        // Cập nhật lại danh sách sau khi xóa
                        tasks.clear();
                        tasks.addAll(toDoDAO.getListToDo());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Lab1.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                    } else {
                        Toast.makeText(Lab1.this, "Delete failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void clearInputFields() {
        edtTittle.setText("");
        edtContent.setText("");
        edtDate.setText("");
        edtType.setText("");
        selectedToDoId = -1;
    }

    private void clickUpdate() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedToDoId != -1) {
                    ToDo toDo = new ToDo();
                    toDo.setId(selectedToDoId);
                    toDo.setTitle(edtTittle.getText().toString());
                    toDo.setContent(edtContent.getText().toString());
                    toDo.setDate(edtDate.getText().toString());
                    toDo.setType("");
                    toDo.setStatus(1);

                    boolean isUpdated = toDoDAO.updateToDo(toDo);
                    if (isUpdated) {
                        tasks.clear();
                        tasks.addAll(toDoDAO.getListToDo());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Lab1.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Lab1.this, "Update failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addData() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTittle.getText().toString();
                String content = edtContent.getText().toString();
                String date = edtDate.getText().toString();
                String type = edtType.getText().toString();
                int status = 0;

                ToDo toDo = new ToDo(0, title, content, date, type, status);
                if (toDoDAO.addToDo(toDo)) {
                    tasks.add(toDo);
                    adapter.notifyDataSetChanged();
                }
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        if (listView != null) {
            adapter = new ListViewAdapter(tasks, this);
            listView.setAdapter(adapter);
        } else {
            System.out.println("ListView is null. Check the XML layout file for correct ID.");
        }
    }

    private void initView() {
        listView = findViewById(R.id.listView);
        edtTittle = findViewById(R.id.edtTittle);
        edtContent = findViewById(R.id.edtContent);
        edtDate = findViewById(R.id.edtDate);
        edtType = findViewById(R.id.edtType);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
    }
}
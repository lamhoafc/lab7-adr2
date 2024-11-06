package fpoly.account.lab_android2.lab1.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fpoly.account.lab_android2.R;
import fpoly.account.lab_android2.lab1.adapter.ListViewAdapter;
import fpoly.account.lab_android2.lab1.dao.ToDoDAO;
import fpoly.account.lab_android2.lab1.model.TodoModel;

public class Lab1Activity extends AppCompatActivity {
    private ToDoDAO toDoDAO;
    private ListViewAdapter adapter;
    private ListView listView;
    private Button btnUpdate, btnDelete, btnAdd;
    private int selectedToDoId = -1;
    private EditText edtTittle, edtContent, edtDate, edtType, edtImage;
    private ArrayList<TodoModel> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);

        initView();
        toDoDAO = new ToDoDAO(this);
        tasks = toDoDAO.getListToDo();
        setAdapter();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            TodoModel selectedTodoModel = tasks.get(position);
            selectedToDoId = selectedTodoModel.getId();
            edtTittle.setText(selectedTodoModel.getTitle());
            edtContent.setText(selectedTodoModel.getContent());
            edtDate.setText(selectedTodoModel.getDate());
            edtType.setText(selectedTodoModel.getType());
            edtImage.setText(selectedTodoModel.getImage());
        });

        addData();
        clickUpdate();
        clickDelete();
    }

    private void clickDelete() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Lab1Activity.this)
                        .setTitle("Xac nhan")
                        .setMessage("Ban co chac chan muon xoa?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (selectedToDoId != -1) {
                                    boolean isDeleted = toDoDAO.deleteToDo(selectedToDoId);
                                    if (isDeleted) {
                                        tasks.clear();
                                        tasks.addAll(toDoDAO.getListToDo());
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(Lab1Activity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                                        clearInputFields();
                                    } else {
                                        Toast.makeText(Lab1Activity.this, "Delete failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                builder.show();


            }
        });
    }

    private void clearInputFields() {
        edtTittle.setText("");
        edtContent.setText("");
        edtDate.setText("");
        edtType.setText("");
        edtImage.setText("");
        selectedToDoId = -1;
    }

    private boolean validateInput (String title, String content, String date, String type, String status) {
        if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty() || status.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void clickUpdate() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = edtTittle.getText().toString();
                String content = edtContent.getText().toString();
                String date = edtDate.getText().toString();
                String type = edtType.getText().toString();
                String image = edtImage.getText().toString();

                int status = 1;
                if (validateInput(title, content, date, type, status +"")) {
                    if (selectedToDoId != -1) {
                        TodoModel todoModel = new TodoModel();
                        todoModel.setId(selectedToDoId);
                        todoModel.setTitle(title);
                        todoModel.setContent(content);
                        todoModel.setDate(date);
                        todoModel.setType(type);
                        todoModel.setStatus(status);
                        todoModel.setImage(image);

                        boolean isUpdated = toDoDAO.updateToDo(todoModel);
                        if (isUpdated) {
                            tasks.clear();
                            tasks.addAll(toDoDAO.getListToDo());
                            adapter.notifyDataSetChanged();
                            Toast.makeText(Lab1Activity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Lab1Activity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
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
                String image = edtImage.getText().toString();

                int status = 0;

                if (validateInput(title, content, date, type, status +"")) {
                    TodoModel todoModel = new TodoModel(0, title, content, date, type, status, image);
                    if (toDoDAO.addToDo(todoModel)) {
                        tasks.add(todoModel);
                        adapter.notifyDataSetChanged();
                    }
                    setAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }




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
        edtImage = findViewById(R.id.edtImage);
    }
}
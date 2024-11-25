package fpoly.account.lab_android2.lab1.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fpoly.account.lab_android2.R;
import fpoly.account.lab_android2.lab1.adapter.ToDoAdapter;
import fpoly.account.lab_android2.lab1.model.TodoModel;

public class Lab1Activity extends AppCompatActivity {
    private ToDoAdapter adapter;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private EditText edtTittle, edtContent, edtDate, edtType, edtImage;
    private ArrayList<TodoModel> tasks;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);

        initView();
        db = FirebaseFirestore.getInstance();
        tasks = new ArrayList<>();
        setAdapter();
        addData();
        loadDataFromFirestore();
    }

    private void loadDataFromFirestore() {
        db.collection("todos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Lab1Activity", document.getId() + " => " + document.getData());

                                // Tạo TodoModel từ document
                                String id = document.getId();
                                String title = document.getString("title");
                                String content = document.getString("content");
                                String date = document.getString("date");
                                String type = document.getString("type");
                                int status = document.getLong("status").intValue(); // Lấy status và chuyển sang int
                                String image = document.getString("image");

                                TodoModel todoModel = new TodoModel(id, title, content, date, type, status, image);
                                tasks.add(todoModel);
                            }
                            adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                        } else {
                            Log.w("Lab1Activity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void addData() {
        btnAdd.setOnClickListener(v -> {
            String title = edtTittle.getText().toString();
            String content = edtContent.getText().toString();
            String date = edtDate.getText().toString();
            String type = edtType.getText().toString();
            String image = edtImage.getText().toString();

            int status = 0;

            if (validateInput(title, content, date, type, status + "")) {
                Map<String, Object> todo = new HashMap<>();
                todo.put("title", title);
                todo.put("content", content);
                todo.put("date", date);
                todo.put("type", type);
                todo.put("status", status);
                todo.put("image", image);

                db.collection("todos")
                        .add(todo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("Lab1Activity", "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(Lab1Activity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                clearInputFields();

                                // Thêm task vào list và cập nhật RecyclerView
                                TodoModel todoModel = new TodoModel(documentReference.getId(), title, content, date, type, status, image);
                                tasks.add(todoModel);
                                adapter.notifyItemInserted(tasks.size() - 1);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Lab1Activity", "Error adding document", e);
                                Toast.makeText(Lab1Activity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputFields() {
        edtTittle.setText("");
        edtContent.setText("");
        edtDate.setText("");
        edtType.setText("");
        edtImage.setText("");
    }

    private boolean validateInput(String title, String content, String date, String type, String status) {
        return !title.isEmpty() && !content.isEmpty() && !date.isEmpty() && !type.isEmpty() && !status.isEmpty();
    }

    private void setAdapter() {
        if (recyclerView != null) {
            adapter = new ToDoAdapter(tasks, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new ToDoAdapter.OnItemClickListener() {
                @Override
                public void onEditClick(TodoModel todoModel) {
                    // Xử lý khi click nút Edit (nếu cần)
                }

                @Override
                public void onDeleteClick(int position) {
                    TodoModel todoModel = tasks.get(position);
                    db.collection("todos").document(todoModel.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Lab1Activity", "DocumentSnapshot successfully deleted!");
                                tasks.remove(position);
                                adapter.notifyItemRemoved(position);
                                Toast.makeText(Lab1Activity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.w("Lab1Activity", "Error deleting document", e);
                                Toast.makeText(Lab1Activity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                            });
                }
            });
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.listView);
        edtTittle = findViewById(R.id.edtTittle);
        edtContent = findViewById(R.id.edtContent);
        edtDate = findViewById(R.id.edtDate);
        edtType = findViewById(R.id.edtType);
        btnAdd = findViewById(R.id.btnAdd);
        edtImage = findViewById(R.id.edtImage);
    }
}
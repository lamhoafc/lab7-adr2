package fpoly.account.lab_android2.lab1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fpoly.account.lab_android2.R;
import fpoly.account.lab_android2.lab1.model.TodoModel;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private ArrayList<TodoModel> lists;
    private Context context;
    private FirebaseFirestore db;
    private OnItemClickListener onItemClickListener;



    public ToDoAdapter(ArrayList<TodoModel> lists, Context context) {
        this.lists = lists;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new ToDoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        TodoModel todoModel = lists.get(position);

        holder.tvTittle.setText(todoModel.getTitle());
        holder.tvDate.setText(todoModel.getDate());

        // Cập nhật trạng thái checkbox dựa trên status của task
        holder.cbDone.setChecked(todoModel.getStatus() == 1);
        if (todoModel.getStatus() == 1) {
            holder.tvTittle.setPaintFlags(holder.tvTittle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvTittle.setPaintFlags(holder.tvTittle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Cập nhật status trên Firestore
            Map<String, Object> updates = new HashMap<>();
            updates.put("status", isChecked ? 1 : 0);

            db.collection("todos").document(todoModel.getId())
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("ToDoAdapter", "DocumentSnapshot successfully updated!");
                        todoModel.setStatus(isChecked ? 1 : 0);
                        notifyItemChanged(position);
                        Toast.makeText(context, "Đã cập nhật status thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.w("ToDoAdapter", "Error updating document", e);
                        Toast.makeText(context, "Cập nhật status thất bại", Toast.LENGTH_SHORT).show();
                    });
        });

        holder.ivEdit.setOnClickListener(v -> {
            // Tạo dialog để người dùng chỉnh sửa dữ liệu
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_todo, null);
            builder.setView(dialogView);

            // Ánh xạ các view trong dialog
            EditText edtTitle = dialogView.findViewById(R.id.edtTitle);
            EditText edtContent = dialogView.findViewById(R.id.edtContent);
            EditText edtDate = dialogView.findViewById(R.id.edtDate);
            EditText edtType = dialogView.findViewById(R.id.edtType);
            EditText edtImage = dialogView.findViewById(R.id.edtImage);

            // Hiển thị dữ liệu hiện tại của task lên dialog
            edtTitle.setText(todoModel.getTitle());
            edtContent.setText(todoModel.getContent());
            edtDate.setText(todoModel.getDate());
            edtType.setText(todoModel.getType());
            edtImage.setText(todoModel.getImage());

            builder.setPositiveButton("Lưu", (dialog, which) -> {
                // Lấy dữ liệu mới từ các trường nhập liệu
                String newTitle = edtTitle.getText().toString();
                String newContent = edtContent.getText().toString();
                String newDate = edtDate.getText().toString();
                String newType = edtType.getText().toString();
                String newImage = edtImage.getText().toString();

                // Cập nhật dữ liệu cho TodoModel
                todoModel.setTitle(newTitle);
                todoModel.setContent(newContent);
                todoModel.setDate(newDate);
                todoModel.setType(newType);
                todoModel.setImage(newImage);

                // Cập nhật Firestore
                Map<String, Object> updates = new HashMap<>();
                updates.put("title", newTitle);
                updates.put("content", newContent);
                updates.put("date", newDate);
                updates.put("type", newType);
                updates.put("image", newImage);

                db.collection("todos").document(todoModel.getId())
                        .update(updates)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("ToDoAdapter", "DocumentSnapshot successfully updated!");
                            notifyItemChanged(position);
                            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.w("ToDoAdapter", "Error updating document", e);
                            Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                        });
            });

            builder.setNegativeButton("Hủy", null);
            builder.show();
        });

        holder.ivDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa mục này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        // Xóa task khỏi Firestore
                        db.collection("todos").document(todoModel.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("ToDoAdapter", "DocumentSnapshot successfully deleted!");
                                    lists.remove(position);
                                    notifyItemRemoved(position);
                                    Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.w("ToDoAdapter", "Error deleting document", e);
                                    Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ToDoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTittle;
        public TextView tvDate;
        public ImageView ivEdit;
        public ImageView ivDelete;
        public AppCompatCheckBox cbDone;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTittle = itemView.findViewById(R.id.tvTittle);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            cbDone = itemView.findViewById(R.id.cbDone);
        }
    }

    public interface OnItemClickListener {
        void onEditClick(TodoModel todoModel);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
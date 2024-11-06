package fpoly.account.lab_android2.lab2.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.account.lab_android2.R;
import fpoly.account.lab_android2.lab2.Adapter.FoodAdapter;
import fpoly.account.lab_android2.lab2.model.FoodModel;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<FoodModel>foodModels;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        initView();
        addData();
    }

    private void addData() {
        foodModels = new ArrayList<>();
        foodModels.add(new FoodModel("Bún bò","100.000"));
        foodModels.add(new FoodModel("Bún lợn","1.000.000"));
        foodModels.add(new FoodModel("Bún bò","100.000"));
        foodModels.add(new FoodModel("Bún bò","100.000"));
        foodModels.add(new FoodModel("Bún bò","100.000"));
        foodModels.add(new FoodModel("Bún bò","100.000"));


        foodAdapter = new FoodAdapter(foodModels, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(foodAdapter);


    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
    }
}
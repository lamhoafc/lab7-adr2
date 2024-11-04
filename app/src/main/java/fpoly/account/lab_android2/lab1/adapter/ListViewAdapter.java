package fpoly.account.lab_android2.lab1.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fpoly.account.lab_android2.R;
import fpoly.account.lab_android2.lab1.model.ToDo;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ToDo> lists;
    private Context context;

    public ListViewAdapter(ArrayList<ToDo> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(parent.getContext(), R.layout.item_todo, null);
        }

        TextView tvTittle = convertView.findViewById(R.id.tvTittle);
        TextView tvContent = convertView.findViewById(R.id.tvContent);
        TextView tvDate = convertView.findViewById(R.id.tvDate);

        ToDo toDo = lists.get(position);
        tvTittle.setText(toDo.getTitle());
        tvContent.setText(toDo.getContent());
        tvDate.setText(toDo.getDate());

        return convertView;
    }
}

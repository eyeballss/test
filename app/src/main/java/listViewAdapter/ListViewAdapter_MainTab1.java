package listViewAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.kookmin.embedded.mobilecloudchattingapp.R;

/**
 * Created by kesl on 2016-05-22.
 */

public class ListViewAdapter_MainTab1 extends BaseAdapter
{
    Activity context;
    ArrayList<String> title;
    ArrayList<String> description;

    public ListViewAdapter_MainTab1(Activity context, ArrayList<String> title, ArrayList<String> description) {
        super();
        this.context = context;
        this.title = title;
        this.description = description;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return title.size();
//        return title.length;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDescription;
    }

    //실제로 보여주기 위한 부분. 이곳을 만져야 리스트뷰 모양이 바뀐다.
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_main_tab1_items, null); //여기서 인플레이터, 즉 객체화.
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewTitle.setText(title.get(position));
        holder.txtViewDescription.setText(description.get(position));

        return convertView;
    }

}
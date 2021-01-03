package com.eq.music;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class MyGridviewAdapter extends BaseAdapter {
    private Context context;
    private int[]iconArr;
    private String[] titleArr;
    private String[] countArr;

    public MyGridviewAdapter(Context context, int[] iconArr, String[] titleArr, String[] countArr) {
        this.context = context;
        this.iconArr = iconArr;
        this.titleArr = titleArr;
        this.countArr = countArr;
    }

    //确定item个数
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null){
            convertView=inflater.inflate(R.layout.item_gridview,null);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.iv_item_icon);
            holder.tvCount= (TextView) convertView.findViewById(R.id.tv_count);
            holder.tvTitle= (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(iconArr[position]);
        holder.tvTitle.setText(titleArr[position]);
        holder.tvCount.setText(countArr[position]);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView tvTitle;
        TextView tvCount;
    }
}


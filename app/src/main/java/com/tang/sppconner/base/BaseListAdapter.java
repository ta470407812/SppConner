package com.tang.sppconner.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {

    private List<T> list;
    private int layoutId;
    private LayoutInflater layoutInflater;

    private BaseListAdapter() {
    }

    public BaseListAdapter(Context context, int layoutId, List<T> data) {
        layoutInflater = LayoutInflater.from(context);
        this.list = data;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return null == list
                ? 0
                : list.size();
    }

    @Override
    public T getItem(int position) {
        return (null == list || position >= list.size())
                ? null
                : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(layoutId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.rootView = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setViewData(viewHolder.rootView, position, getItem(position));
        return convertView;
    }

    public void updateData(List<T> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public List<T> getArrays() {
        return list;
    }

    protected abstract void setViewData(View convertView, int position, T t);

    private class ViewHolder {
        View rootView;
    }
}

package com.cliftoncraig.droidtools.droidtools.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cliftoncraig.droidtools.droidtools.R;

import java.io.File;

/**
 * Created by clifton
 * Copyright 7/26/15.
 */
public class FileListAdapter extends BaseAdapter {
    private final File[] fileList;
    private final LayoutInflater inflater;
    private final Context context;

    class ViewHolder {

        private final Context context;
        private final View view;
        private final TextView fileNameText;

        public ViewHolder(Context context, View view) {
            this.context = context;
            this.view = view;
            this.fileNameText = (TextView) view.findViewById(R.id.fileNameText);
        }

        public void setFile(File file) {
            fileNameText.setText(file.getName());
        }
    }

    public FileListAdapter(Context context, File file) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fileList = file.listFiles();
    }

    @Override
    public int getCount() {
        return fileList.length;
    }

    @Override
    public Object getItem(int position) {
        return fileList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.file_list_view, null);
            viewHolder = new ViewHolder(context, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setFile((File)getItem(position));
        return convertView;
    }
}

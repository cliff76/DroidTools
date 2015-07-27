package com.cliftoncraig.droidtools.droidtools.file;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.cliftoncraig.droidtools.droidtools.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileExplorer extends ListActivity {

    public static final String DIRECTORY = "directory";
    private FileListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        final String directory = getIntent().getStringExtra(DIRECTORY);
        adapter = new FileListAdapter(this, new File(directory));
        getListView().setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_explorer, menu);
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Intent intent = new Intent(this, FileExplorer.class);
        File file = (File) adapter.getItem(position);
        if (file.isDirectory()) {
            intent.putExtra(DIRECTORY,file.getAbsolutePath());
            startActivity(intent);
        } else {
            buildDialog(position).show();
        }
    }

    private Dialog buildDialog(final int position) {
        final File selectedFile = (File) adapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setItems(new String[]{"Open", "Copy externally"}, new DialogInterface.OnClickListener() {
                    public static final int KILOBYTE = 1024;
                    File root = Environment.getExternalStorageDirectory();
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                final Intent intent = new Intent(FileExplorer.this, FileDetailsActivity.class);
                                intent.putExtra("file", selectedFile.getAbsolutePath());
                                startActivity(intent);
                                break;
                            default:
                                //Copy externally
                                final File output = new File(root, selectedFile.getName());
                                final OutputStream fileOutputStream;
                                try { fileOutputStream = new FileOutputStream(output); }
                                catch (FileNotFoundException e) { throw new RuntimeException("File not writeable " + output.getAbsolutePath(), e); }
                                final InputStream inputStream;
                                try { inputStream = new FileInputStream(selectedFile); }
                                catch (FileNotFoundException e) { throw new RuntimeException("File not readable " + selectedFile.getAbsolutePath(), e); }
                                copyStreams(inputStream, fileOutputStream);
                                Toast.makeText(FileExplorer.this, "Copied to " + root.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                Toast.makeText(FileExplorer.this, output.getPath(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }

                    private void copyStreams(InputStream inputStream, OutputStream outputStream) {
                        byte[] buffer = new byte[2 * KILOBYTE];
                        for(int count = read(inputStream, buffer); count != -1; count = read(inputStream, buffer)) {
                            write(outputStream, buffer);
                        }
                    }

                    private void write(OutputStream outputStream, byte[] buffer) {
                        try { outputStream.write(buffer); }
                        catch (IOException e) { throw new RuntimeException("Couldn't write data for copy!", e); }
                    }

                    private int read(InputStream inputStream, byte[] buffer) {
                        try { return inputStream.read(buffer); }
                        catch (IOException e) { throw new RuntimeException("Couldn't read data for copy!", e); }
                    }
                })
                ;
        return builder.create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.vstar3D.VRPlayer.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.vstar3D.VRPlayer.R;
import com.vstar3D.VRPlayer.VPlayerActivity;
import com.vstar3D.VRPlayer.adapter.FileListAdapter;
import com.vstar3D.VRPlayer.db.MyAdb;
import com.vstar3D.VRPlayer.util.MyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@SuppressLint("NewApi")
public class CatalogFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private View mView;
    private ListView mListView;
    private TextView mTextView, textback;
    private ImageButton mImageButton;
    private EditText mEditText;
    private View myView;

    private List<String> items = null;   //items：存放显示的名称
    private List<String> paths = null;   //paths：存放文件路径
    private List<String> sizes = null;   //sizes：文件大小
    private String rootPath = "/";       //rootPath：起始文件夹
    private MyAdb db;
    private Cursor myCursor;
    private int id = 0;
    private int isZoom = 0;
    private int isOpen = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_catalog, container, false);
        mListView = (ListView) mView.findViewById(R.id.listviw);
        textback = (TextView) mView.findViewById(R.id.textback);
        mListView.setOnKeyListener(backlistener);

        mImageButton = (ImageButton) mView.findViewById(R.id.qry_button);
        mEditText = (EditText) mView.findViewById(R.id.path_edit);
        mImageButton.setOnClickListener(listener_qry);
        textback.setOnClickListener(listener_qry);
        mListView.setOnItemClickListener(this);
        db = new MyAdb(getContext());
        myCursor = db.getFileSet();
        if (myCursor.moveToFirst()) {
            id = myCursor.getInt(myCursor.getColumnIndex("_ID"));
            isZoom = myCursor.getInt(myCursor.getColumnIndex("ISZOOM"));
            isOpen = myCursor.getInt(myCursor.getColumnIndex("ISOPEN"));
        } else {
            db.insertFileSet(isZoom, isOpen);
            myCursor = db.getFileSet();
            myCursor.moveToFirst();
            id = myCursor.getInt(myCursor.getColumnIndex("_ID"));
        }
        getFileDir(rootPath);
        return mView;
    }

    private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_BACK) {  //表示按返回键 时的操作
                    File file = new File(mEditText.getText().toString());
                    if (!rootPath.equals(file) && file != null) {
                        getFileDir(file.getParent());
                        return true;
                    } //后退
                    return false;    //已处理
                }
            }
            return false;
        }
    };


    Button.OnClickListener listener_qry = new Button.OnClickListener() {
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.qry_button:
                    File file = new File(mEditText.getText().toString());
                    if (file.exists()) {
                        if (file.isFile()) {
                            openFile(file);
                        } else {
                            getFileDir(mEditText.getText().toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "找不到该位置,请确定位置是否正确!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.textback:
                    File files = new File(mEditText.getText().toString());
                    if (files.toString().equals(rootPath)||files==null){
                        textback.setVisibility(View.GONE);
                        break;
                    }
                        textback.setVisibility(View.VISIBLE);
                        getFileDir(files.getParent());
                    break;
            }
        }
    };

    /**
     * 取得文件结构的方法
     *
     * @param filePath
     */
    private void getFileDir(String filePath) {
    /* 设置目前所在路径 */
        mEditText.setText(filePath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        sizes = new ArrayList<String>();
        File f = new File(filePath);
        File[] files = f.listFiles();
        if (files != null) {
    /* 将所有文件添加ArrayList中 */
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    items.add(files[i].getName());
                    paths.add(files[i].getPath());
                    sizes.add("");
                }
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    items.add(files[i].getName());
                    paths.add(files[i].getPath());
                    sizes.add(MyUtil.fileSizeMsg(files[i]));
                }
            }
        }

    /* 使用自定义的MyAdapter来将数据传入ListActivity */
        mListView.setAdapter(new FileListAdapter(getContext(), items, paths, sizes, isZoom));
    }

    /**
     * 打开文件
     *
     * @param f
     */
    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        //跳出列表供选择
        String type = "*/*";
        if (isOpen == 0) {
            type = MyUtil.getMIMEType(f, true);
        }
        //设置intent的file与MimeType
        intent.setDataAndType(Uri.fromFile(f), type);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = new File(paths.get(position));
        getFileDir(String.valueOf(file));
        String f_type = MyUtil.getMIMEType(file, false);

        if ("video".equals(f_type)) {
            Intent intent = new Intent(getContext(), VPlayerActivity.class);
            intent.putExtra("fpath", file.toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
        textback.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean OnkeyStaus() {
        File files = new File(mEditText.getText().toString());
        if (files.toString().equals(rootPath)||files==null){
            textback.setVisibility(View.GONE);

        }else {
            textback.setVisibility(View.VISIBLE);
            getFileDir(files.getParent());
        }
        return true;
    }

}
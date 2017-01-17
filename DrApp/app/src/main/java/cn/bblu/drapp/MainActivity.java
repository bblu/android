package cn.bblu.drapp;
import java.util.*;
import java.lang.*;

import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.*;
import android.view.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    private TextView txt = null;
    private ListView lst = null ;
    private Button btn = null;
    private int idx = 0;
    long[][] rst={{1,6,10,12},{3,5,6,8,9,10}};
    String[] cap ={"一","二"};
    ArrayList<String> items=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    SoundPool sp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt=(TextView)findViewById(R.id.textView);
        //TextPaint tp = txt.getPaint();
        //tp.setFakeBoldText(true);
        btn = (Button) findViewById(R.id.button);
        lst = (ListView) findViewById(R.id.listView);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,
                items);
        lst.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lst.setAdapter(adapter);

        TextView header = new TextView(this);
        header.setBackgroundColor(0xE3CF57);
        header.setText("备选项：");
        lst.addHeaderView(header,null,false);
       loadQuestion(1);
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC,2);
        sp.load(this,R.raw.app,1);
        sp.load(this,R.raw.sha,1);
        btn.setText("提交");
        this.setTitle("闯关测试：第一题");
    }

    public void button_click(View view)
    {
        String btnText = btn.getText().toString();
        if (btnText == "重新开始"){
            loadQuestion(1);
        }else if(btnText == "提交") {
            if(lst.getCheckedItemCount()==0) {
                Toast.makeText(this,"请选择答案后提交", Toast.LENGTH_SHORT).show();
            }else {
                boolean isRight = testRight();
                if (isRight) {
                    sp.play(1, 1, 1, 0, 0, 1);
                    if (idx < 2) {
                        loadQuestion(idx + 1);
                    } else
                        btn.setText("闯关成功");
                } else {
                    sp.play(2, 1, 1, 0, 0, 1);
                    btn.setText("重新开始");
                }
                alertResult(isRight);
            }
        }else if (btnText == "闯关成功")
        {
            btn.setText("重新开始");
        }
    }

    private boolean testRight()
    {
       android.util.SparseBooleanArray chkPos = lst.getCheckedItemPositions();
        if(chkPos.size() !=rst[idx-1].length)
            return false;
        //String x="";
        for(int i=0;i<chkPos.size() ;i++)
        {
            if(chkPos.keyAt(i)!=rst[idx-1][i])
                return false;
           // x+=","+String.valueOf(chkPos.keyAt(i));
        }
       // this.setTitle(x);
        return true;
    }
    private void loadQuestion(int i) {
        idx = i;
        this.setTitle("闯关测试：第一题");
        String itm=this.getString(R.string.items1);
        String str=this.getString(R.string.question1);

        if(i==2) {
            itm = this.getString(R.string.items2);
            str=this.getString(R.string.question2);
            this.setTitle("闯关测试：第二题");
        }
        txt.setText(str);

        adapter.clear();
        String[] items = itm.split(",");
        for(String s : items)
            adapter.add(s);
        lst.clearChoices();
        //lst.setSelectionAfterHeaderView();
        //lst.smoothScrollToPosition(0);
        lst.setSelection(0);
        btn.setText("提交");
    }
    private void alertResult(boolean bool)
    {
        ImageView img =  new ImageView(this);

        String title = "回答错误";
        if(bool) {
            title = "回答正确";
            img.setImageResource(R.drawable.img0);
        }else {
            img.setImageResource(R.drawable.img1);
        }
        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle(title).
                setView(img).
                //setMessage("对话框的内容").
                setPositiveButton("确定" , null).
                //setNegativeButton().
                //setIcon(R.drawable.ic_launcher).
                create();
        alertDialog.show();
    }
}

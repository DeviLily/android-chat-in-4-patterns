package nju.androidchat.client.hw1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.java.Log;
import nju.androidchat.client.ClientMessage;
import nju.androidchat.client.R;
import nju.androidchat.client.Utils;
import nju.androidchat.client.component.ItemImageReceive;
import nju.androidchat.client.component.ItemImageSend;
import nju.androidchat.client.component.ItemTextReceive;
import nju.androidchat.client.component.ItemTextSend;
import nju.androidchat.client.component.OnRecallMessageRequested;

@Log
public class Hw1TalkActivity extends AppCompatActivity implements Hw1Contract.View, TextView.OnEditorActionListener, OnRecallMessageRequested {
    private Hw1Contract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hw1TalkModel mvp0TalkModel = new Hw1TalkModel();

        // Create the presenter
        this.presenter = new Hw1TalkPresenter(mvp0TalkModel, this, new ArrayList<>());
        mvp0TalkModel.setIMvp0TalkPresenter(this.presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showMessageList(List<ClientMessage> messages) {
        runOnUiThread(() -> {
                    LinearLayout content = findViewById(R.id.chat_content);

//                    // 删除所有已有的ItemText
//                    content.removeAllViews();
//
//                    // 增加ItemText
//                    for (ClientMessage message : messages) {
//                        String text = String.format("%s", message.getMessage());
//                        // 如果是自己发的，增加ItemTextSend
//                        if (message.getSenderUsername().equals(this.presenter.getUsername())) {
//                            content.addView(new ItemTextSend(this, text, message.getMessageId(), this));
//                        } else {
//                            content.addView(new ItemTextReceive(this, text, message.getMessageId()));
//                        }
//                    }

                    // 只加入新消息，否则会导致图片重复加载
                    for (int i = content.getChildCount(); i < messages.size(); ++i) {
                        ClientMessage message = messages.get(i);
                        String text = String.format("%s", message.getMessage());
                        Pattern pattern = Pattern.compile("^!\\[.*?\\]\\((.*?)\\)$");
                        Matcher matcher = pattern.matcher(text);
                        boolean isImage = matcher.find();
                        String url = isImage ? matcher.group(1) : null;
                        if (message.getSenderUsername().equals(this.presenter.getUsername())) {
                            if (isImage)
                                content.addView(new ItemImageSend(this, url, message.getMessageId(), this));
                            else
                                content.addView(new ItemTextSend(this, text, message.getMessageId(), this));
                        } else {
                            if (isImage)
                                content.addView(new ItemImageReceive(this, url, message.getMessageId()));
                            else
                                content.addView(new ItemTextReceive(this, text, message.getMessageId()));
                        }
                    }

                    Utils.scrollListToBottom(this);
                }
        );
    }

    @Override
    public void setPresenter(Hw1Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            return hideKeyboard();
        }
        return super.onTouchEvent(event);
    }

    private boolean hideKeyboard() {
        return Utils.hideKeyboard(this);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (Utils.send(actionId, event)) {
            hideKeyboard();
            // 异步地让Controller处理事件
            sendText();
        }
        return false;
    }

    private void sendText() {
        EditText text = findViewById(R.id.et_content);
        AsyncTask.execute(() -> {
            this.presenter.sendMessage(text.getText().toString());
        });
    }

    public void onBtnSendClicked(View v) {
        hideKeyboard();
        sendText();
    }

    // 当用户长按消息，并选择撤回消息时做什么，MVP-0不实现
    @Override
    public void onRecallMessageRequested(UUID messageId) {

    }
}

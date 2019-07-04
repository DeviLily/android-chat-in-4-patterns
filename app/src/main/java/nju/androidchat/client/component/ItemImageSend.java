package nju.androidchat.client.component;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StyleableRes;

import java.util.UUID;

import lombok.Setter;
import nju.androidchat.client.R;
import nju.androidchat.client.Utils;

public class ItemImageSend extends LinearLayout implements View.OnLongClickListener {
    @StyleableRes
    int index0 = 0;

    private TextView textView;
    private Context context;
    private UUID messageId;
    @Setter private OnRecallMessageRequested onRecallMessageRequested;

    public ItemImageSend(Context context, String src, UUID messageId, OnRecallMessageRequested onRecallMessageRequested) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_text_send, this);
        this.textView = findViewById(R.id.chat_item_content_text);
        this.messageId = messageId;
        this.onRecallMessageRequested = onRecallMessageRequested;

        this.setOnLongClickListener(this);
        setImage(src);
    }

    public void setImage(String src) {
        String html = "<img src='" + src + "'>";
        textView.setText(Html.fromHtml(html, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                return new BitmapDrawable(Utils.getNetworkImage(source));
            }
        }, null));
    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要撤回这条消息吗？")
                .setPositiveButton("是", (dialog, which) -> {
                    if (onRecallMessageRequested != null) {
                        onRecallMessageRequested.onRecallMessageRequested(this.messageId);
                    }
                })
                .setNegativeButton("否", ((dialog, which) -> {
                }))
                .create()
                .show();

        return true;


    }

}

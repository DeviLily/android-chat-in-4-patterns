package nju.androidchat.client.component;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StyleableRes;

import java.util.UUID;

import nju.androidchat.client.R;
import nju.androidchat.client.Utils;

public class ItemImageReceive extends LinearLayout {


    @StyleableRes
    int index0 = 0;

    private TextView textView;
    private Context context;
    private UUID messageId;
    private OnRecallMessageRequested onRecallMessageRequested;

    public ItemImageReceive(Context context, String src, UUID messageId) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_text_receive, this);
        this.textView = findViewById(R.id.chat_item_content_text);
        this.messageId = messageId;
        setImage(src);
    }

    public void init(Context context) {

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
}

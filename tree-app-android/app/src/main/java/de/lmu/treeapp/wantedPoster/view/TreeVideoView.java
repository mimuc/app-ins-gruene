package de.lmu.treeapp.wantedPoster.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.content.WantedPoster;
import de.lmu.treeapp.wantedPoster.WantedPosterTab;
import de.lmu.treeapp.wantedPoster.WantedPosterTextType;

import java.util.List;

public class TreeVideoView extends LinearLayout {

    private final TextView pageTitle;
    private final TextView videoDescription;
    private final YouTubePlayerView videoPlayer;

    private String title;
    private String videoId;
    private String videoInfo;

    public TreeVideoView(Context context) {
        super(context);
    }

    public TreeVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TreeVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        inflate(getContext(), R.layout.view_tree_video, this);

        pageTitle = findViewById(R.id.page_title);
        videoDescription = findViewById(R.id.description);
        videoPlayer = findViewById(R.id.youtube_player_view);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setTreeVideo(Lifecycle lifecycle, List<WantedPoster> wpList) {
        lifecycle.addObserver(videoPlayer);
        for (WantedPoster wp : wpList) {
            if (wp.tab == WantedPosterTab.VIDEO) {
                if (wp.name == WantedPosterTextType.TITLE) {
                    title = wp.description;
                } else if (wp.name == WantedPosterTextType.VIDEO_ID) {
                    videoId = wp.description;
                } else if (wp.name == WantedPosterTextType.VIDEO_INFO) {
                    videoInfo = wp.description;
                }
            }
        }
        pageTitle.setText(title);
        videoDescription.setText(videoInfo);
        videoPlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }
}

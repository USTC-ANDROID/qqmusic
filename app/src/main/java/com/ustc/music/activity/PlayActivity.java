package com.ustc.music.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ustc.music.R;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.entity.Music;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.PlayPageTransformer;
import com.ustc.music.util.RequestUtil;
import com.ustc.music.view.CDView;
import com.ustc.music.view.LrcView;
import com.ustc.music.view.PagerIndicator;
import com.ustc.music.view.PlayBgShape;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PlayActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mPlayContainer;
    private ImageView mPlayBackImageView; // back button
    private TextView mMusicTitle; // music title
    private ViewPager mViewPager; // cd or lrc
    private CDView mCdView; // cd
    private SeekBar mPlaySeekBar; // seekbar
    private ImageButton mStartPlayButton; // start or pause
    private TextView mSingerTextView; // singer
    private LrcView mLrcViewOnFirstPage; // single line lrc
    private LrcView mLrcViewOnSecondPage; // 7 lines lrc
    private PagerIndicator mPagerIndicator; // indicator
    private ImageButton mPrevPlayButton;
    private ImageButton mNextPlayButton;

    // cd view and lrc view
    private ArrayList<View> mViewPagerContent = new ArrayList<View>(2);
    private long allTme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(savedInstanceState, R.layout.activity_player);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setupViews();
//		allowBindService();
        initEvent();
    }

    private void initEvent() {
        mPrevPlayButton.setOnClickListener((v) -> {
            playService.playPrev();
            onChange(playService.getNowMusic());
        });
        mNextPlayButton.setOnClickListener((v) -> {
            playService.playNext();
            onChange(playService.getNowMusic());
        });
        mStartPlayButton.setOnClickListener(v -> {
                Log.v("playactivity", "initEvent()");
                if(playService.getStatus()) {
                    mStartPlayButton.setImageResource(R.drawable.player_btn_play_normal);
                    playService.stop();
                } else {
                    mStartPlayButton.setImageResource(R.drawable.player_btn_pause_normal);
                    playService.start();
                }
            });
    }

    /**
     * 初始化view
     */
    private void setupViews() {
        mPlayContainer =  findViewById(R.id.ll_play_container);
        mPlayBackImageView = findViewById(R.id.iv_play_back);
        mMusicTitle = findViewById(R.id.tv_music_title);
        mViewPager = findViewById(R.id.vp_play_container);
        mPlaySeekBar = findViewById(R.id.sb_play_progress);
        mStartPlayButton = findViewById(R.id.ib_play_start);
        mPagerIndicator = findViewById(R.id.pi_play_indicator);
        mPrevPlayButton = findViewById(R.id.ib_play_pre);
        mNextPlayButton = findViewById(R.id.ib_play_next);

        // 动态设置seekbar的margin
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mPlaySeekBar.getLayoutParams();
        p.leftMargin = (int) (600 * 0.1);
        p.rightMargin = (int) (1024 * 0.1);

        mPlaySeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        initViewPagerContent();
        mViewPager.setPageTransformer(true, new PlayPageTransformer());
        mPagerIndicator.create(mViewPagerContent.size());
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        mViewPager.setAdapter(mPagerAdapter);

        mPlayBackImageView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        allowBindService();
    }

    @Override
    protected void onPause() {
//        allowUnbindService();
        super.onPause();
    }

    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
//                if(mPlayService.isPlaying()) mCdView.start();
            } else {
                mCdView.pause();
            }

            mPagerIndicator.current(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * 拖动进度条
     */
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int progress = seekBar.getProgress();
                    Log.v("progress", progress + "drag");
                    playService.seek(progress);
                    mLrcViewOnFirstPage.onDrag(progress);
                    mLrcViewOnSecondPage.onDrag(progress);
                }
            };

    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mViewPagerContent.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewPagerContent.get(position));
            return mViewPagerContent.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    };

    /**
     * 初始化viewpager的内容
     */
    private void initViewPagerContent() {
        View cd = View.inflate(this, R.layout.play_page_layout1, null);
        mCdView = (CDView) cd.findViewById(R.id.play_cdview);
        mSingerTextView = (TextView) cd.findViewById(R.id.play_singer);
        mLrcViewOnFirstPage = (LrcView) cd.findViewById(R.id.play_first_lrc);

        View lrcView = View.inflate(this, R.layout.play_page_layout2, null);
        mLrcViewOnSecondPage = (LrcView) lrcView.findViewById(R.id.play_first_lrc_2);

        mViewPagerContent.add(cd);
        mViewPagerContent.add(lrcView);
    }


    private void setBackground(String url) {
//        Music currentMusic = MusicUtils.sMusicList.get(position);
//        Bitmap bgBitmap = MusicIconLoader.getInstance().load("");
        Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        mPlayContainer.setBackgroundDrawable(new ShapeDrawable(new PlayBgShape(bgBitmap)));

        Glide.with(this).load(url).bitmapTransform(new BlurTransformation(this, 25)).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                mPlayContainer.setBackground(resource);
            }
        });
        Glide.with(this).load(url).asBitmap().into(new SimpleTarget<Bitmap >() {

            @Override
            public void onResourceReady(Bitmap  resource, GlideAnimation glideAnimation) {
                mCdView.setImage(resource);
            }
        });


//		Palette.generateAsync(bgBitmap, new PaletteAsyncListener() {
//			@Override
//			public void onGenerated(Palette palette) {
//				Palette.Swatch swatch = palette.getVibrantSwatch();
//				if(swatch == null) return;
//
//				L.l("textColor", swatch.getBodyTextColor());
//				L.l("titleColor", swatch.getTitleTextColor());
//				mMusicTitle.setTextColor(swatch.getBodyTextColor());
//				mPlayContainer.setBackgroundDrawable(new ShapeDrawable(new PlayBgShape(bgBitmap)));
//			}
//		});
    }


    /**
     * 上一曲
     * @param view
     */
    public void pre(View view) {
//        mPlayService.pre(); // 上一曲
        playService.pre();
    }

    /**
     * 播放 or 暂停
     * @param view
     */
    public void play(View view) {
//        if(mPlayService.isPlaying()) {
//            mPlayService.pause(); // 暂停
//            mCdView.pause();
//            mStartPlayButton.setImageResource(R.drawable.player_btn_play_normal);
//        }else {
//            onPlay(mPlayService.resume()); // 播放
//        }
    }

    /**
     * 上一曲
     * @param view
     */
    public void next(View view) {

//        mPlayService.next(); // 上一曲
    }

    /**
     * 播放时调用 主要设置显示当前播放音乐的信息
     * @param music
     */
    private void onPlay(Music music) {
        if (playService.getStatus()) mCdView.start();
        else mCdView.pause();
//        Music music = MusicUtils.sMusicList.get(position);
//
        mMusicTitle.setText(music.getTitle());
//        mSingerTextView.setText(music.getArtist());
//        Bitmap bmp = MusicIconLoader.getInstance().load(music.getImage());
//        if(bmp == null) bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//        mCdView.setImage(ImageTools.scaleBitmap(bmp, (int)(App.sScreenWidth * 0.7)));
//
//        if(mPlayService.isPlaying()) {
//            mCdView.start();
//            mStartPlayButton.setImageResource(R.drawable.player_btn_pause_normal);
//        }else {
//            mCdView.pause();
//            mStartPlayButton.setImageResource(R.drawable.player_btn_play_normal);
//        }
    }

    private void setLrc(Music music) {
//        Music music = MusicUtils.sMusicList.get(position);
//        String lrcPath = MusicUtils.getLrcDir() + music.getTitle() + ".lrc";
        mLrcViewOnFirstPage.reset();
        mLrcViewOnSecondPage.reset();
        Map<String, String> headers = new HashMap<>();
        headers.put("referer", "https://y.qq.com/portal/player.htm");
        RequestUtil.get(music.getLrc(), headers, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject json = new JSONObject(string);
                    String lyric = json.getString("lyric");
                    byte[] decode = Base64.decode(lyric, 0);
                    Log.v("playactivity", new String(decode, "utf-8"));
                    lyric = new String(decode, "utf-8");
                    allTme = mLrcViewOnFirstPage.parseLrc(lyric);

                    mPlaySeekBar.setMax((int)allTme);
                    mLrcViewOnSecondPage.parseLrc(lyric);
                } catch (JSONException e) {


                }
            }
        });
//        mLrcViewOnFirstPage.setLrcPath("测试");
//        mLrcViewOnSecondPage.setLrcPath("测试");
    }

    public void onPublish(int progress) {
        if(allTme != 0) {
            Log.v("playactivity", "时间啊" + (int)(progress * 100  / allTme));
            mPlaySeekBar.setProgress(progress);
        }
        Log.v("playactivity", progress + "");

        if(mLrcViewOnFirstPage.hasLrc()) mLrcViewOnFirstPage.onProgress(progress);
        if(mLrcViewOnSecondPage.hasLrc()) mLrcViewOnSecondPage.onProgress(progress);
    }

    public void onChange(Music music) {
        Log.v("playcallback", "playing");
//        super.onChange(music);
        setBackground(music.getAvatar());
        onPlay(music);

        setLrc(music);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
//		allowUnbindService();
        super.onDestroy();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }



    @Override
    protected void initAdapter() {

    }



    @Override
    protected void serviceInited() {
//        System.out.println("哈哈哈");
        super.serviceInited();
        Music nowMusic = playService.getNowMusic();
        if(playService.getStatus()) {
            mStartPlayButton.setImageResource(R.drawable.player_btn_pause_normal);
        } else {
            mStartPlayButton.setImageResource(R.drawable.player_btn_play_normal);
        }
        onChange(nowMusic);
    }




}
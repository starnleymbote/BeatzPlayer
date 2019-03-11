package com.hrw.android.player.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.hrw.android.player.R;
import com.hrw.android.player.adapter.MusicListAdapter;
import com.hrw.android.player.dao.AudioDao;
import com.hrw.android.player.dao.impl.AudioDaoImpl;
import com.hrw.android.player.domain.Audio;
import com.hrw.android.player.utils.Constants;
import com.hrw.android.player.utils.Constants.PopupMenu;

public class LocalMusicListActivity extends BaseListActivity {
	private AudioDao audioDao = new AudioDaoImpl(this);
	private ImageButton back_btn;
	private List<Audio> musicList;
	private String[] choices;
	private MusicListAdapter adapter;
	Set<Integer> popUpMenu = new HashSet<Integer>();
    private InterstitialAd mInterstitialAd;
    private AdView mAdView,mAdView1;


	// @Override
	// protected void onPrepareDialog(int id, Dialog dialog) {
	// AlertDialog alertDialog = (AlertDialog) dialog;
	// ListView lv = alertDialog.getListView();
	// for (int i = 0; i < choices.length; i++) {
	// lv.setItemChecked(i, false);
	// }
	// super.onPrepareDialog(id, dialog);
	// }

	private void initPopupMenu() {
		popUpMenu.add(PopupMenu.ADD_ALL_TO.getMenu());
		popUpMenu.add(PopupMenu.CREATE_LIST.getMenu());
		popUpMenu.add(PopupMenu.EXIT.getMenu());
		popUpMenu.add(PopupMenu.HELP.getMenu());
		popUpMenu.add(PopupMenu.SETTING.getMenu());
	}

	private void initButtons() {
		final TabActivity tabActivity = (TabActivity) getParent();
		final Intent toMenuListActivity = new Intent(this,
				MenuListActivity.class);

		back_btn = (ImageButton) tabActivity.findViewById(R.id.list_back);
		back_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					TabHost.TabSpec tab_spec_menu_list = tabActivity
							.getTabHost().newTabSpec(
									Constants.TAB_SPEC_TAG.MAIN_SPEC_TAG
											.getId()).setIndicator(
									Constants.TAB_SPEC_TAG.MAIN_SPEC_TAG
											.getId());
					tab_spec_menu_list.setContent(toMenuListActivity);
					tabActivity.getTabHost().addTab(tab_spec_menu_list);
					tabActivity.getTabHost().setCurrentTabByTag(
							Constants.TAB_SPEC_TAG.MAIN_SPEC_TAG.getId());
					Intent updateUiIntent = new Intent(
							Constants.UPDATE_UI_ACTION);
					sendBroadcast(updateUiIntent);
				}
				return false;
			}
		});
	}

	private void initListAdapter() {
		musicList = audioDao.getLocalAudioList();
		adapter = new MusicListAdapter(this, musicList, null);
		setListAdapter(adapter);

	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_audio_list);
		initPopupMenu();
		initButtons();
		initListAdapter();

	}

	@Override
	protected void onResume() {
		super.onResume();
		back_btn.setVisibility(ImageButton.VISIBLE);
		TextView top_title_tv = (TextView) this.getParent().findViewById(
				R.id.top_title);
		top_title_tv.setText(R.string.menu_local_music);
		adapter.notifyDataSetChanged();
        mAdView = findViewById(R.id.adView);
//		showAds();
	}

	@Override
	protected Set<Integer> getPopUpMenu() {
		if (adapter.getCheckedBoxPositionIds().size() > 0) {
			popUpMenu.add(PopupMenu.ADD_TO.getMenu());
		} else {
			popUpMenu.remove(PopupMenu.ADD_TO.getMenu());
		}
		return popUpMenu;
	}

    public void showAds()
    {
//        AdSize adSize = new AdSize(300, 50);
        AdRequest adRequest = new AdRequest.Builder().build();
        //AdRequest adRequest1 = new  AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // mAdView1.loadAd(adRequest1);

        MobileAds.initialize(this, "ca-app-pub-4536585142676162/8223025982");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }
        else
        {
            Log.e("Inter","Error loading the ad");
        }

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //super.onAdFailedToLoad(i);
                Log.e("Inter","Error loading the ad"+errorCode);

            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                //super.onAdLoaded();
                Log.e("Inter","Error loading the ad");
                mInterstitialAd.show();

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });


    }
}

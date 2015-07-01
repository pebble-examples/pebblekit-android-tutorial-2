package com.getpebble.pkat2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;


public class MainActivity extends FragmentActivity {

    private static final UUID APP_UUID = UUID.fromString("3783cff2-5a14-477d-baee-b77bd423d079");

    private static final int KEY_BUTTON_UP = 0;
    private static final int KEY_BUTTON_DOWN = 1;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private PebbleKit.PebbleDataReceiver mDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up ViewPager and its adapter
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mDataReceiver == null) {
            mDataReceiver = new PebbleKit.PebbleDataReceiver(APP_UUID) {

                @Override
                public void receiveData(Context context, int transactionId, PebbleDictionary dict) {
                    // Always ACK
                    PebbleKit.sendAckToPebble(context, transactionId);
                    Log.i("receiveData", "Got message from Pebble!");

                    // Up received?
                    if(dict.getInteger(KEY_BUTTON_UP) != null) {
                        previousPage();
                    }

                    // Down received?
                    if(dict.getInteger(KEY_BUTTON_DOWN) != null) {
                        nextPage();
                    }
                }

            };
            PebbleKit.registerReceivedDataHandler(getApplicationContext(), mDataReceiver);
        }
    }

    private void previousPage() {
        int position = mViewPager.getCurrentItem();

        if(position > 0) {
            mViewPager.setCurrentItem(position - 1, true);
        }
    }

    private void nextPage() {
        int position = mViewPager.getCurrentItem();

        if(position < PagerAdapter.NUM_PAGES - 1) {
            mViewPager.setCurrentItem(position + 1, true);
        }
    }
}

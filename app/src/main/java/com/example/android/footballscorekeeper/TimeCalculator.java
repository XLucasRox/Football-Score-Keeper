package com.example.android.footballscorekeeper;

import android.os.AsyncTask;

/**
 * Created by dobry on 10.03.17.
 */

public class TimeCalculator extends AsyncTask<Void, String, Void> {
    long start;
    private StopWatchInterface stopWatchInterface;
    private boolean turnedOn;
    private int currentTime = 0;
    private int defaultTime = 10;
    private int stopwatchAdditionalTime = 0;
    private int destinationTime = 0;
    private boolean defaultTimeDone = false;

    public TimeCalculator(StopWatchInterface swInterface) {
        stopWatchInterface = swInterface;
        start = System.currentTimeMillis();
        turnedOn = true;
    }

    public TimeCalculator(StopWatchInterface swInterface, long start) {
        stopWatchInterface = swInterface;
        this.start = start;
        turnedOn = true;
    }

    public long GetStartTime() {
        return start;
    }

    public boolean IsStarted() {
        return turnedOn;
    }

    public void Stop() {
        turnedOn = false;
    }


    @Override
    protected Void doInBackground(Void... params) {

        while (turnedOn == true) {

            long elapsedTime = System.currentTimeMillis() - start;
            elapsedTime = elapsedTime / 1000;

            String seconds = Integer.toString((int) (elapsedTime % 60));
            String minutes = Integer.toString((int) ((elapsedTime % 3600) / 60));
            currentTime = Integer.parseInt(seconds);

            if (seconds.length() < 2) {
                seconds = "0" + seconds;
            }

            if (minutes.length() < 2) {
                minutes = "0" + minutes;
            }

            String writeThis = minutes + ":" + seconds;
            publishProgress(writeThis);
            setAdditionalTime();
            checkTimeRule();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void setAdditionalTime() {
        stopwatchAdditionalTime = MainActivity.additionalTime;
        destinationTime = defaultTime + stopwatchAdditionalTime;
    }

    public void checkTimeRule() {
        if (currentTime <= defaultTime && defaultTimeDone == false) {
            defaultTimeDone = false;
        } else if (currentTime >= defaultTime) {
            defaultTimeDone = true;
            if (currentTime >= destinationTime && defaultTimeDone == true) {
                turnedOn = false;
            }
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if (stopWatchInterface != null) {
            if (values != null && values.length > 0) {
                if (turnedOn) stopWatchInterface.displayStopwatchTime(values[0]);
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (stopWatchInterface != null) {
            stopWatchInterface.reportFinish();
        }
        super.onPostExecute(aVoid);
    }
}
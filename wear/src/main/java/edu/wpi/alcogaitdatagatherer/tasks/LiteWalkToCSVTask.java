package edu.wpi.alcogaitdatagatherer.tasks;

import android.os.AsyncTask;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.alcogaitdatagatherer.ui.WearHomeActivity;
import edu.wpi.alcogaitdatagatherer.models.LiteWalk;

/**
 * Created by Adonay on 1/1/2018.
 */

public class LiteWalkToCSVTask extends AsyncTask<LiteWalk, Void, Boolean> {
    private File file;
    private final String[] space = {""};
    private WearHomeActivity activity;

    public LiteWalkToCSVTask(File file, WearHomeActivity activity) {
        this.file = file;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        activity.startProgressBar();
    }

    @Override
    protected Boolean doInBackground(LiteWalk... walks) {
        try {
            CSVWriter writer;
            FileWriter mFileWriter;
            if (file.exists() && !file.isDirectory()) {
                mFileWriter = new FileWriter(file, false);
            } else {
                mFileWriter = new FileWriter(file);
            }

            writer = new CSVWriter(mFileWriter);

            for (LiteWalk walk : walks) {
                if (walk != null) {
                    if (walk.getSampleSize() > 0) {
                        writer.writeNext(new String[]{walk.getWalkType().toString()});
                        writer.writeNext(space);
                        for (String[] data : walk.toCSVFormat()) {
                            writer.writeNext(data);
                            //publishProgress(++savedSamples);
                        }

                        writer.writeNext(space);
                        writer.writeNext(space);
                        writer.writeNext(space);
                        writer.writeNext(space);
                    }
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected void onPostExecute(Boolean result) {
        if (result) {
            activity.stopProgressBar();
            activity.showToast("SUCCESSFUL!");
            activity.sendCSVFileToPhone();
        } else {
            activity.showToast("ERROR!!");
        }
    }
}
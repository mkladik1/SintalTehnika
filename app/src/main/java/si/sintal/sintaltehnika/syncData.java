package si.sintal.sintaltehnika;

import android.app.job.JobParameters;
import android.app.job.JobService;


public class syncData extends JobService {
        @Override
        public boolean onStartJob(JobParameters params) {
           int i = 0;
            // Perform your background task here
            // Return true if your task is still running, false if it's completed
            return true;
        }

        @Override
        public boolean onStopJob(JobParameters params) {
            // Called when the system needs to cancel the job
            // Return true to reschedule the job
            return false;
        }
    }
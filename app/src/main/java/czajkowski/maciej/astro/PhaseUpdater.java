package czajkowski.maciej.astro;

import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

public class PhaseUpdater {
    private Updateable updateable;
    private int interval;

    public PhaseUpdater(int interval, Updateable updateable ) {
        this.updateable = updateable;
        this.interval = interval;
    }

    public void start() {
        Timer timer = new Timer("Time Updater", true);
        timer.scheduleAtFixedRate(new Updater(this.updateable), 1000, this.interval);
    }

    private class Updater extends TimerTask {
        private Updateable updateable;

        public Updater(Updateable updateable) {
            super();
            this.updateable = updateable;
        }

        public void run(){
            this.updateable.update();
        }

    }

}

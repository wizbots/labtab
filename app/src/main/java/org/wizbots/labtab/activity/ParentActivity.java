package org.wizbots.labtab.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.wizbots.labtab.LabTabConstants;

import java.util.Vector;

public class ParentActivity extends AppCompatActivity implements LabTabConstants{

    private PauseHandler pauseHandler = new PauseHandler();
    public final int SHOW_TOAST = 0;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void sendMessageToHandler(int what, int arg1, int arg2, Object response) {
        Message message = pauseHandler.obtainMessage();
        message.obj = response;
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        pauseHandler.sendMessage(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pauseHandler.setActivity(this);
        pauseHandler.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseHandler.pause();
    }

    private class PauseHandler extends Handler {

        protected ParentActivity activity;

        /**
         * Message Queue Buffer
         */
        final Vector<Message> messageQueueBuffer = new Vector<Message>();

        /**
         * Flag indicating the pause state
         */
        private boolean paused;

        /**
         * Resume the handler
         */
        final public void resume() {
            paused = false;

            while (messageQueueBuffer.size() > 0) {
                final Message msg = messageQueueBuffer.elementAt(0);
                messageQueueBuffer.removeElementAt(0);
                sendMessage(msg);
            }
        }

        /**
         * Pause the handler
         */
        final public void pause() {
            paused = true;
        }

        public boolean isPaused() {
            return paused;
        }

        final void setActivity(ParentActivity activity) {
            this.activity = activity;
        }

        protected boolean storeMessage(Message message) {
            return true;
        }


        @Override
        final public void handleMessage(Message msg) {
            if (paused) {
                if (storeMessage(msg)) {
                    Message msgCopy = new Message();
                    msgCopy.copyFrom(msg);
                    messageQueueBuffer.add(msgCopy);
                }
            } else {
                processMessage(msg);
            }
        }
    }

    public boolean isActivityPaused() {
        return pauseHandler.isPaused();
    }

    public void processMessage(Message message) {
        switch (message.what) {
            case SHOW_TOAST:
                String msg = (String) message.obj;
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

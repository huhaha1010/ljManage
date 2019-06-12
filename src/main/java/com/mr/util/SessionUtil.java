package com.mr.util;

import javax.servlet.http.HttpSession;
import java.util.Timer;
import java.util.TimerTask;

public class SessionUtil {
    public static void removeAttributeInSession(final HttpSession session, final String attrName) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                session.removeAttribute(attrName);
                timer.cancel();
            }
        }, 5 * 60 * 1000);
    }
}

package cn.cwj.community.tImer;

import javax.servlet.http.HttpSession;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Date 2020/2/2
 * @Version V1.0
 **/
public class CodeTimer extends TimerTask {
    public final Timer timer = new Timer();
    private HttpSession session;
    private String sessionKey;

    @Override
    public void run() {
        session.removeAttribute(sessionKey);
        timer.cancel();
    }

    public CodeTimer(HttpSession session, String sessionKey) {
        this.session = session;
        this.sessionKey = sessionKey;
    }
}

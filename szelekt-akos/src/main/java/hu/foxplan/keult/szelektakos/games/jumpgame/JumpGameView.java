package hu.foxplan.keult.szelektakos.games.jumpgame;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * JumpGame vezérlő class
 */

public class JumpGameView extends SurfaceView {

    Thread gameThread = null;
    SurfaceHolder surfaceHolder;
    Context viewContext;
    Handler viewHandler;


    public JumpGameView(Context context) {
        super(context);
        initView(context);
    }

    public JumpGameView(Context context,
                        AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public JumpGameView(Context context,
                        AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        viewContext = context;

        // Ez tárolja a grafika alapvető méreteit, és egyéb paramétereit
        surfaceHolder = getHolder();

        viewHandler = new Handler();

    }

    // Az Activity hívja meg az onPause metódusból.
    public void pause() {

        GameThread.isGameRunning = false;

        while (true) {
            try {
                gameThread.join(); // Game thread leállítása
            } catch (InterruptedException e) {
                e.printStackTrace(); // Ha nem sikerült elsőre leállítani
            }
            break;
        }
        // Leállt a thread, nincs mire hivatkozni
        gameThread = null;

    }

    public void resume() {
        // Megjelenítés előtt játék inicializálása, indítása új thread-ben
        GameThread.isGameRunning = true;
        gameThread = new GameThread(viewContext, viewHandler, surfaceHolder);
        gameThread.start();
    }

    public void click() {
        if (gameThread != null) {
            GameThread.jumpRequest();
        }
    }
}

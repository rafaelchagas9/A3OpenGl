package com.anhembi.a3;

import com.anhembi.a3.ui.MenuInicial;
import com.anhembi.a3.ui.TelaRegras;
import com.anhembi.a3.ui.TelaObjetivo;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.anhembi.a3.input.KeyBoard;

import static java.lang.System.gc;

/**
 *
 * @author Kakugawa
 */
public class Renderer {
    private static GLWindow window = null;
    private static GLEventListener scene;
    private static KeyListener keyListener;

    //Cria a janela de rendeziração do JOGL
    public static void init(){
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        window = GLWindow.create(caps);
        window.setFullscreen(true);
        window.setResizable(false);


        sceneManager("");

        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start(); //inicia o loop de animação

        //encerrar a aplicacao adequadamente
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });

        window.setVisible(true);
    }

    public static void sceneManager(String sceneName){
        System.out.println("Mudando para cena: " + sceneName);
        window.disposeGLEventListener(scene, false);
        window.removeGLEventListener(scene);
        window.removeKeyListener(keyListener);

        gc();

        switch (sceneName) {
            case "fase1":
                Cena cena = new Cena();
                scene = cena;
                keyListener = new KeyBoard(cena);
                break;
            case "regras":
                scene = new TelaRegras();
                keyListener = new KeyBoard();
                break;
            case "objetivo":
                scene = new TelaObjetivo();
                keyListener = new KeyBoard();
            default:
                keyListener = new KeyBoard();
                scene = new MenuInicial();
                break;
        }

        window.addGLEventListener(scene);
        window.addKeyListener(keyListener);
    }

    public static void main(String[] args) {
        init();
    }
}

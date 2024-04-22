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
import com.anhembi.a3.input.Keyboard;

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

        Cena cena = new Cena();
        window.addGLEventListener(cena); //adiciona a Cena a Janela
        //Habilita o teclado : cena
        window.addKeyListener(new Keyboard(cena));

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

    public static void main(String[] args) {
        init();
    }
}

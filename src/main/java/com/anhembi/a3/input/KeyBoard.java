package com.anhembi.a3.input;

import com.anhembi.a3.Cena;
import com.anhembi.a3.Renderer;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

/**
 *
 * @author Kakugawa
 */
public class KeyBoard implements KeyListener{
    private Cena cena;

    public KeyBoard(){

    }

    public KeyBoard(Cena cena){
        this.cena = cena;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;

            case KeyEvent.VK_W:
                System.out.println("Pressionou tecla w");
                if (this.cena != null){
                    cena.addSpeed(0.005f, 0);
                }
                break;

            case KeyEvent.VK_A:
                System.out.println("Pressionou tecla a");
                if (this.cena != null) {
                    cena.addSpeed(0, -0.005f);
                }
                break;

            case KeyEvent.VK_S:
                System.out.println("Pressionou tecla s");
                if (this.cena != null) {
                    cena.addSpeed(-0.005f, 0);
                }
                break;

            case KeyEvent.VK_D:
                System.out.println("Pressionou tecla d");
                if (this.cena != null) {
                    cena.addSpeed(0, 0.005f);
                }
                break;

            case KeyEvent.VK_R:
                System.out.println("Mudando para tela de regras");
                Renderer.sceneManager("regras");
                break;

            case KeyEvent.VK_G:
                System.out.println("Mudando para tela de objetivos");
                Renderer.sceneManager("objetivo");
                break;

            case KeyEvent.VK_M:
                Renderer.sceneManager("");
                break;

            case KeyEvent.VK_P:
                Renderer.sceneManager("fase1");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

}

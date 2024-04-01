package com.anhembi.a3.ui;

import com.anhembi.a3.utils.TextureUtils;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;

public class MenuInicial implements GLEventListener, MouseListener {

    private Texture playButtonTexture;
    private Texture rulesButtonTexture;
    private Texture goalButtonTexture;
    private Texture exitButtonTexture;
    private final TextureUtils textureUtils = new TextureUtils();
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        // Load button textures (do this only once in init())
        playButtonTexture = textureUtils.loadTexture("play_button.png");
        rulesButtonTexture = textureUtils.loadTexture("rules_button.png");
        goalButtonTexture = textureUtils.loadTexture("goal_button.png");
        exitButtonTexture = textureUtils.loadTexture("exit_button.png");
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        playButtonTexture.destroy(glAutoDrawable.getGL().getGL2());
        rulesButtonTexture.destroy(glAutoDrawable.getGL().getGL2());
        goalButtonTexture.destroy(glAutoDrawable.getGL().getGL2());
        exitButtonTexture.destroy(glAutoDrawable.getGL().getGL2());
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        if (playButtonTexture != null){
            // Render buttons
            drawButton(gl, playButtonTexture, -0.1f, 0.5f); // Button 1
        }

        if (rulesButtonTexture != null){
            // Render buttons
            drawButton(gl, rulesButtonTexture, -0.1f, 0.3f); // Button 2
        }

        if (goalButtonTexture != null){
            // Render buttons
            drawButton(gl, goalButtonTexture, -0.1f, 0.1f); // Button 3
        }

        if (exitButtonTexture != null){
            drawButton(gl, exitButtonTexture, -0.1f, -0.1f);
        }
    }

    private void drawButton(GL2 gl, Texture texture, float x, float y) {
        // Specify button dimensions
        float buttonWidth = 0.2f;
        float buttonHeight = 0.1f;

        // Enable texturing
        gl.glEnable(GL.GL_TEXTURE_2D);
        texture.bind(gl);

        // Begin drawing a textured quad
        gl.glBegin(GL2.GL_QUADS);

        // Top-left vertex
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex2f(x, y);

        // Top-right vertex
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex2f(x + buttonWidth, y);

        // Bottom-right vertex
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex2f(x + buttonWidth, y - buttonHeight);

        // Bottom-left vertex
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex2f(x, y - buttonHeight);

        gl.glEnd();

        // Disable texturing
        gl.glDisable(GL.GL_TEXTURE_2D);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println("Cliquei");
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent mouseEvent) {

    }
}

package com.anhembi.a3.ui;

import com.anhembi.a3.utils.TextureUtils;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TelaObjetivo extends GLCanvas implements GLEventListener {
    private GLU glu;
    private TextRenderer titleTextRenderer;
    private TextRenderer rulesTextRenderer;
    private Texture backgroundTexture;
    private final TextureUtils textureUtils = new TextureUtils();

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        glu = new GLU();
        titleTextRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 24));
        rulesTextRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

        // Load background texture
        backgroundTexture = textureUtils.loadTexture("empty_textbox.png");
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        // Clean up resources here
        titleTextRenderer.dispose();
        rulesTextRenderer.dispose();
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Draw background image
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        backgroundTexture.enable(gl);
        backgroundTexture.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0); gl.glVertex2f(-1, -1);
        gl.glTexCoord2f(1, 0); gl.glVertex2f(1, -1);
        gl.glTexCoord2f(1, 1); gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 1); gl.glVertex2f(-1, 1);
        gl.glEnd();
        backgroundTexture.disable(gl);

        // Draw text
        // Center the text
        String[] regras = {
                "Ganhar",
                "Não perder"
        };
        int textX = 0;
        int textY = 0;
        Rectangle2D textBounds = rulesTextRenderer.getBounds(regras[0]);

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        rulesTextRenderer.beginRendering(glAutoDrawable.getSurfaceWidth(), glAutoDrawable.getSurfaceHeight());
        int lineHeight = rulesTextRenderer.getFont().getSize() * 2;
        int y = (glAutoDrawable.getSurfaceHeight() - (int) textBounds.getHeight()) / 2;
        for (String line : regras) {
            textBounds = rulesTextRenderer.getBounds(line);

            textX = (glAutoDrawable.getSurfaceWidth() - (int) textBounds.getWidth()) / 2;
            textY = (glAutoDrawable.getSurfaceHeight() - (int) textBounds.getHeight()) / 2;
            rulesTextRenderer.draw(line, textX, y);
            y -= lineHeight; // Move to the next line
        }
        rulesTextRenderer.endRendering();

        // Add a bold title
        String title = "Objetivo";
        titleTextRenderer.beginRendering(glAutoDrawable.getSurfaceWidth(), glAutoDrawable.getSurfaceHeight());
        titleTextRenderer.setColor(Color.WHITE);
        titleTextRenderer.setSmoothing(true);
        // textRenderer.setFont(new Font("SansSerif", Font.BOLD, 36));
        Rectangle2D titleBounds = titleTextRenderer.getBounds(title);
        int titleX = (glAutoDrawable.getSurfaceWidth() - (int) titleBounds.getWidth()) / 2;
        int titleY = textY + (int) textBounds.getHeight() + 20;
        titleTextRenderer.draw(title, titleX, titleY);
        titleTextRenderer.endRendering();

        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        if (height == 0) height = 1;
        float aspect = (float) width / height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}

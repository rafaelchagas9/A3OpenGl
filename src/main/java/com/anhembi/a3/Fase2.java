package com.anhembi.a3;

import com.anhembi.a3.utils.TextureUtils;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;

public class Fase2 implements GLEventListener {
    private GLU glu;  // OpenGL utility object
    private GLUT glut; // GLUT utility object
    private float paddleY = -0.9f; // Y-coordinate of the paddle
    private float paddleX = 0f; // X- coordinate of the paddle
    private float paddleHeight = 0.06f; // Height of the paddle
    private float paddleWidth = 0.28f;
    private float ballX, ballY; // Position of the ball
    private float ballSize = 0.05f; // Size of the ball
    private float ballSpeedX = 0f; // Speed of the ball in X direction
    private float ballSpeedY = 0f; // Speed of the ball in Y direction
    private float paddleSpeedX = 0f;
    private float paddleSpeedY = 0f;
    private int playerScore = 0;
    private int playerLives = 5;
    private TextRenderer scoreTextRenderer;
    private final TextureUtils textureUtils = new TextureUtils();
    private Texture lifeTexture;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set the clear color to black

        // Randomizes initial ball speed
        float randomBallSpeedX = ThreadLocalRandom.current().nextFloat(0.002f, 0.004f);
        int randomBallXDirection = ThreadLocalRandom.current().nextInt(0, 1); // 0 = esquerda, 1 = direita
        float randomBallSpeedY = ThreadLocalRandom.current().nextFloat(0.008f, 0.02f);


        ballSpeedX = randomBallXDirection == 0 ? -randomBallSpeedX : randomBallSpeedX;
        ballSpeedY = -randomBallSpeedY;


        // Game HUD
        scoreTextRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 32));
        lifeTexture = textureUtils.loadTexture("life.png");
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Limpa a tela para deixar ela preta
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // Desenha as vidas
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        lifeTexture.enable(gl);
        lifeTexture.bind(gl);
        float heartSize = 0.08f;
        float heartCoordinateX = -0.95f;
        float heartCoordinateY = 0.95f;
        for (int i =0; i < this.playerLives; i++){
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0, 0); gl.glVertex2f(heartCoordinateX - heartSize / 2, heartCoordinateY - heartSize / 2);
            gl.glTexCoord2f(1, 0); gl.glVertex2f(heartCoordinateX + heartSize / 2, heartCoordinateY - heartSize / 2);
            gl.glTexCoord2f(1, 1); gl.glVertex2f(heartCoordinateX + heartSize / 2, heartCoordinateY + heartSize / 2);
            gl.glTexCoord2f(0, 1); gl.glVertex2f(heartCoordinateX - heartSize / 2, heartCoordinateY + heartSize / 2);
            gl.glEnd();

            heartCoordinateX += heartSize;
        }
        lifeTexture.disable(gl);

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        scoreTextRenderer.setColor(Color.WHITE);
        scoreTextRenderer.setSmoothing(true);
        Rectangle2D bounds = scoreTextRenderer.getBounds(String.valueOf(playerScore));
        int canvasWidth = drawable.getSurfaceWidth();
        int canvasHeight = drawable.getSurfaceHeight();
        int textWidth = (int) bounds.getWidth();
        int textHeight = (int) bounds.getHeight();
        int x = canvasWidth - textWidth - 10; // 10 pixels margin
        int y = canvasHeight - textHeight - 10; // 10 pixels margin
        scoreTextRenderer.beginRendering(canvasWidth, canvasHeight);
        scoreTextRenderer.draw(String.valueOf(playerScore), x, y);
        scoreTextRenderer.endRendering();


        // Desenha o paddçe
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Set color to white
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(paddleX - paddleWidth / 2, paddleY - paddleHeight / 2); // Change the x-coordinate here
        gl.glVertex2f(paddleX + paddleWidth / 2, paddleY - paddleHeight / 2); // Change the x-coordinate here
        gl.glVertex2f(paddleX + paddleWidth / 2, paddleY + paddleHeight / 2); // Change the x-coordinate here
        gl.glVertex2f(paddleX - paddleWidth / 2, paddleY + paddleHeight / 2); // Change the x-coordinate here
        gl.glEnd();

        // Desenha a bola
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Set color to white
        gl.glPushMatrix();
        gl.glTranslatef(ballX, ballY, 0.0f);
        glut.glutSolidSphere(ballSize, 50, 50); // Use glut function to draw sphere
        gl.glPopMatrix();

        // Desenha o obstáculo no centro da tela
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Set color to white
        gl.glBegin(GL2.GL_QUADS);
        // Vertex coordinates for the triangles forming the diamond
        gl.glVertex2d(-0.1f, -0.1);     // Top point
        gl.glVertex2d(0.1f, -0.1f);    // Left point
        gl.glVertex2d(0.1f, 0.1);   // Bottom point
        gl.glVertex2d(-0.1f, 0.1f);   // Bottom point

        gl.glEnd();

        // Update ball position
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Update paddle position
        paddleX += paddleSpeedX;
        paddleY +=  paddleSpeedY;

        // Check for collision with window borders
        if (ballX + ballSize >= 1.0f || ballX - ballSize <= -1.0f) {
            ballSpeedX = -ballSpeedX; // Reverse ball's X direction
        }
        if (ballY + ballSize >= 1.0f) {
            ballSpeedY = -ballSpeedY; // Reverse ball's Y direction
        }
        if (ballY - ballSize <= -1.0f){
            ballSpeedY = -ballSpeedY; // Reverse ball's Y direction
            this.playerLives--;
        }

        // Check for paddles collision with window borders
        // Check for collision with window borders
        if (paddleX + paddleWidth / 2 >= 1.0f || paddleX - paddleWidth / 2 <= -1.0f) {
            paddleSpeedX = 0; // Reverse ball's X direction
        }


        if ((ballY + ballSize) <= (paddleY + paddleHeight * 2.5f) && ballX + ballSize / 2 <= paddleX + paddleWidth / 2 && ballX + ballSize / 2 >= paddleX - paddleWidth / 2){
            // Determine the point of contact on the paddle
            float relativeIntersectY = (paddleY + paddleHeight / 2) - ballY;
            float normalizedIntersectY = relativeIntersectY / (paddleHeight / 2);

            // Calculate the bounce angle
            float bounceAngle = normalizedIntersectY * (float) Math.toRadians(45);

            // Reverse ball's X direction and adjust its Y direction based on bounce angle
            ballSpeedX = -ballSpeedX;
            float bounceSpeed = ballSpeedX * (float) Math.tan(bounceAngle);
            System.out.println(bounceSpeed);
            ballSpeedY = bounceSpeed;

            // Adds a value to ballY to stop the collision and prevent the ball from getting stuck
            ballY += paddleHeight * 1.5f / 2;

            this.playerScore += 10;
        }

        // Collision detection with obstacle
        if ((ballY + ballSize) <= 0.1f && (ballY + ballSize) >= -0.1f && ballX + ballSize / 2 <= 0.1f && ballX + ballSize / 2 >= 0.1f){
            System.out.println("Bateu");
        }
        gl.glFlush();

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-1.0, 1.0, -1.0, 1.0); // Set up the coordinate system
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    public void setPaddleSpeedX(float ballSpeedX) {
        this.paddleSpeedX = ballSpeedX;
    }
}




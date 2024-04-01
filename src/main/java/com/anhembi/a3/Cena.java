package com.anhembi.a3;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public class Cena implements GLEventListener {
    private GLU glu;  // OpenGL utility object
    private GLUT glut; // GLUT utility object
    private float paddleY; // Y-coordinate of the paddle
    private float paddleHeight = 0.2f; // Height of the paddle
    private float ballX, ballY; // Position of the ball
    private float ballSize = 0.1f; // Size of the ball
    private float ballSpeedX = 0f; // Speed of the ball in X direction
    private float ballSpeedY = 0f; // Speed of the ball in Y direction

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set the clear color to black
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-1.0, 1.0, -1.0, 1.0); // Set up the coordinate system
        paddleY = 0; // Initialize paddle position
        ballX = ballY = 0; // Initialize ball position
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // Draw the paddle
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Set color to white
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(0.95f, paddleY - paddleHeight / 2); // Change the x-coordinate here
        gl.glVertex2f(0.95f, paddleY + paddleHeight / 2); // Change the x-coordinate here
        gl.glEnd();

        // Draw the ball
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Set color to white
        gl.glPushMatrix();
        gl.glTranslatef(ballX, ballY, 0.0f);
        glut.glutSolidSphere(ballSize, 50, 50); // Use glut function to draw sphere
        gl.glPopMatrix();

        // Update ball position
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Check for collision with window borders
        if (ballX + ballSize >= 1.0f || ballX - ballSize <= -1.0f) {
            ballSpeedX = -ballSpeedX; // Reverse ball's X direction
        }
        if (ballY + ballSize >= 1.0f || ballY - ballSize <= -1.0f) {
            ballSpeedY = -ballSpeedY; // Reverse ball's Y direction
        }

        // Collision detection and response with the paddle goes here
        if (ballX + ballSize >= 0.95f && ballY >= paddleY - paddleHeight / 2 && ballY <= paddleY + paddleHeight / 2) {
            // Determine the point of contact on the paddle
            float relativeIntersectY = (paddleY + paddleHeight / 2) - ballY;
            float normalizedIntersectY = relativeIntersectY / (paddleHeight / 2);

            // Calculate the bounce angle
            float bounceAngle = normalizedIntersectY * (float) Math.toRadians(45);

            // Reverse ball's X direction and adjust its Y direction based on bounce angle
            ballSpeedX = -ballSpeedX;
            ballSpeedY = ballSpeedX * (float) Math.tan(bounceAngle);
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // Unused in this example
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    public float getBallSpeedX() {
        return ballSpeedX;
    }

    public float getBallSpeedY() {
        return ballSpeedY;
    }

    public void setBallSpeedX(float ballSpeedX) {
        this.ballSpeedX = ballSpeedX;
    }

    public void setBallSpeedY(float ballSpeedY) {
        this.ballSpeedY = ballSpeedY;
    }

    public void addSpeed(float ballSpeedY, float ballSpeedX){
        System.out.println("Adicionando: " + ballSpeedX + ", " + ballSpeedY );
        this.ballSpeedX += ballSpeedX;
        this.ballSpeedY += ballSpeedY;
        System.out.println("Velocidade: " + this.ballSpeedX + ", " + this.ballSpeedY );
    }
}




package com.anhembi.a3;

import com.anhembi.a3.utils.TextureUtils;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Cena implements GLEventListener {
    private String currentScene = "menu";
    private GLU glu;  // OpenGL utility object
    private GLUT glut; // GLUT utility object
    int canvasWidth = 0;
    int canvasHeight = 0;
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
    private Texture backgroundTexture;
    private Texture playButtonTexture;
    private Texture rulesButtonTexture;
    private Texture goalButtonTexture;
    private Texture exitButtonTexture;
    private boolean isPendingChangeScene = false;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set the clear color to black

        // Game HUD
        scoreTextRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 32));
        lifeTexture = textureUtils.loadTexture("life.png");
        backgroundTexture = textureUtils.loadTexture("background.jpg");
        playButtonTexture = textureUtils.loadTexture("play_button.png");
        rulesButtonTexture = textureUtils.loadTexture("rules_button.png");
        goalButtonTexture = textureUtils.loadTexture("goal_button.png");
        exitButtonTexture = textureUtils.loadTexture("exit_button.png");

        setScene(this.currentScene);

        canvasWidth = drawable.getSurfaceWidth();
        canvasHeight = drawable.getSurfaceHeight();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Limpa a tela para deixar ela preta
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        sceneManager(gl);
    }

    private void DrawMainMenu(GL2 gl) {

        // Habilita o uso de texturas
        gl.glEnable(GL2.GL_TEXTURE_2D);
        backgroundTexture.bind(gl);

        // Desenha um quadrado ocupando toda a janela com a textura da imagem de fundo
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-1, -1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, -1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(-1, 1);
        gl.glEnd();

        // Desabilita o uso de texturas
        gl.glDisable(GL2.GL_TEXTURE_2D);

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

    private void drawFaseBase(GL2 gl){
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
        int textWidth = (int) bounds.getWidth();
        int textHeight = (int) bounds.getHeight();
        int x = canvasWidth - textWidth - 10; // 10 pixels margin
        int y = canvasHeight - textHeight - 10; // 10 pixels margin
        scoreTextRenderer.beginRendering(canvasWidth, canvasHeight);
        scoreTextRenderer.draw(String.valueOf(playerScore), x, y);
        scoreTextRenderer.endRendering();


        // Desenha o paddle
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
            this.ballX = 0;
            this.ballY = 0;
            this.ballSpeedX = 0;
            this.ballSpeedY = 0;
            this.playerLives--;
        }

        // Check for paddles collision with window borders
        // Check for collision with window borders
        if (paddleX + paddleWidth / 2 >= 1.0f || paddleX - paddleWidth / 2 <= -1.0f) {
            paddleSpeedX = 0; // Reverse ball's X direction
        }


        if ((ballY + ballSize) <= (paddleY + paddleHeight * 2.5f) && ballX + ballSize / 2 <= paddleX + paddleWidth / 2 && ballX + ballSize / 2 >= paddleX - paddleWidth / 2){
            // Adds a value to ballY to stop the collision and prevent the ball from getting stuck

            // Reverse ball's X direction and adjust its Y direction based on bounce angle
            ballSpeedX = ballSpeedX * -1;
            ballSpeedY =  ballSpeedY * -1;

            this.playerScore += 40;

            if (this.playerScore >= 200){
                this.currentScene = "fase2";
            }
        }

        if (playerLives <= 0){

        }
    }

    public void drawFase1(GL2 gl){
    }

    public void drawFase2(GL2 gl){
        float obstacleHeight = 0.02f;
        float obstacleWidth = 0.3f;
        float obstacleSpacing = 0.5f;

        // Position of the first obstacle
        float obstacle1X = 0.0f;
        float obstacle1Y = 0.0f;

        // Position of the second obstacle
        float obstacle2X = 0.0f;
        float obstacle2Y = obstacleSpacing;

        // Desenha o obstáculo de baixo
        gl.glColor3f(1.0f, 0.0f, 1.0f); // Set color to white
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(-obstacleWidth, -obstacleHeight);     // Top point
        gl.glVertex2d(obstacleWidth, -obstacleHeight);    // Left point
        gl.glVertex2d(obstacleWidth, obstacleHeight);   // Bottom point
        gl.glVertex2d(-obstacleWidth, obstacleHeight);   // Bottom point
        gl.glEnd();

        // Desenha o obstáculo de cima
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(-obstacleWidth, -obstacleHeight + obstacleSpacing);     // Top point
        gl.glVertex2d(obstacleWidth, -obstacleHeight + obstacleSpacing);    // Left point
        gl.glVertex2d(obstacleWidth, obstacleHeight + obstacleSpacing);   // Bottom point
        gl.glVertex2d(-obstacleWidth, obstacleHeight + obstacleSpacing);   // Bottom point
        gl.glEnd();

        boolean collision1 = checkCollision(ballX, ballY, ballSize / 2, obstacle1X, obstacle1Y, obstacleWidth, obstacleHeight);
        boolean collision2 = checkCollision(ballX, ballY, ballSize / 2, obstacle2X, obstacle2Y, obstacleWidth, obstacleHeight);

        if (collision1){
            ballSpeedX = ballSpeedX * -1;
            ballSpeedY =  ballSpeedY * -1;
        }
    }

    public void drawRegras(GL2 gl){

    }

    public void drawObjetivo(GL2 gl){

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

    public boolean checkCollision(float ballX, float ballY, float ballRadius,
                                  float obstacleX, float obstacleY, float obstacleWidth, float obstacleHeight) {
        // Calculate distance between ball center and obstacle center
        float dx = ballX - obstacleX;
        float dy = ballY - obstacleY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Check if distance is less than the sum of their radii
        float sumRadii = ballRadius + Math.max(obstacleWidth, obstacleHeight) / 2; // Approximation of obstacle diagonal
        return distance < sumRadii;
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

    public void setScene(String sceneName){
        this.currentScene = sceneName;
        this.isPendingChangeScene = true;
    }

    private void sceneManager(GL2 gl){
        float randomBallSpeedX = 0;
        int randomBallXDirection = 0;
        float randomBallSpeedY = 0;

        if (isPendingChangeScene){
            clearSceneVariables();

            float difficultyMultiplier = 1.2f;

            if (this.currentScene == "fase1"){
                difficultyMultiplier = 1.2f;
            } else if (this.currentScene == "fase2"){
                difficultyMultiplier = 2.2f;
            }

            randomBallSpeedX = ThreadLocalRandom.current().nextFloat(0.001f, 0.004f);
            randomBallXDirection = ThreadLocalRandom.current().nextInt(-1, 1); // 0 = esquerda, 1 = direita
            randomBallSpeedY = ThreadLocalRandom.current().nextFloat(0.005f, 0.01f);
            ballSpeedX = randomBallXDirection == 0 ? -randomBallSpeedX * difficultyMultiplier : randomBallSpeedX * difficultyMultiplier;
            ballSpeedY = -randomBallSpeedY;
            this.isPendingChangeScene = false;
        }

        switch (this.currentScene) {
            case "fase1":
                drawFaseBase(gl);
                drawFase1(gl);
                break;
            case "regras":
                drawRegras(gl);
                break;
            case "objetivo":
                drawObjetivo(gl);
                break;
            case "fase2":
                drawFaseBase(gl);
                drawFase2(gl);
                break;
            default:
                DrawMainMenu(gl);
                break;
        }



    }

    private void clearSceneVariables(){
        paddleY = -0.9f; // Y-coordinate of the paddle
        paddleX = 0f; // X- coordinate of the paddle
        paddleHeight = 0.06f; // Height of the paddle
        paddleWidth = 0.28f;

        ballSize = 0.05f; // Size of the ball
        ballSpeedX = 0f; // Speed of the ball in X direction
        ballSpeedY = 0f; // Speed of the ball in Y direction
        paddleSpeedX = 0f;
        paddleSpeedY = 0f;
        ballY = 0;
        ballX = 0;

        if (!Objects.equals(this.currentScene, "fase2")){
            playerScore = 0;
            playerLives = 5;
        } else {
            this.ballY = 0.4f;
        }

    }
}




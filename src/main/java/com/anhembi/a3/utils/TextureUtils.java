package com.anhembi.a3.utils;

import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.IOException;
import java.io.InputStream;

public class TextureUtils {

    public Texture loadTexture(String imagePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imagePath);

            return TextureIO.newTexture(inputStream, true, TextureIO.PNG);
        } catch (IOException | GLException e) {
            System.err.println("Error loading texture: " + e.getMessage());
            return null;
        }
    }
}

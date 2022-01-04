package com.digitail.changeColor.layer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Layer {

    protected BufferedImage layer;
    private int width;
    private int height;
    private int type;

    public Layer(File layerPath) {
        try {
            layer = ImageIO.read(layerPath);
        } catch (IOException e) {
            System.out.println("Image reading went wrong!!! Probably incorrect path.");
            e.printStackTrace();
        }

        width = layer.getWidth();
        height = layer.getHeight();
        type = layer.getType();
    }

    public BufferedImage getLayer() {
        return layer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getType() { return type; }
}

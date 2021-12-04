package com.digitail.changeColor.layer;

import java.awt.*;
import java.io.File;

public class ColoredLayer extends Layer {

    public ColoredLayer(File path) {
        super(path);
    }

    public void ChangeColor(int red, int green, int blue) {
        for (int x = 0; x < getWidth(); x++)
            for (int y = 0; y < getHeight(); y++) {
                int alpha = (layer.getRGB(x, y)>>24) & 0xff;
                if(alpha == 0)
                    continue;
                float lumaAdjusted = GetLuma(x, y)/255/255;

                layer.setRGB(x, y,
                        new Color(red * lumaAdjusted, green * lumaAdjusted, blue * lumaAdjusted).getRGB());
            }
    }

    public void ChangeColor(int rgb) {
        int[] rgbA = GetRGB(rgb);

        ChangeColor(rgbA[0], rgbA[1], rgbA[2]);
    }

    private float GetLuma(int x, int y) {
        int rgb = layer.getRGB(x, y);
        int[] rgbA = GetRGB(rgb);

        return  0.2126f*rgbA[0] + 0.7152f*rgbA[1] + 0.0722f*rgbA[2];
    }

    private static int[] GetRGB(int rgb) {
        int r = (rgb >> 16) & 0x000000FF;
        int g = (rgb >> 8) & 0x000000FF;
        int b = (rgb) & 0x000000FF;

        return new int[] {r, g, b};
    }
}

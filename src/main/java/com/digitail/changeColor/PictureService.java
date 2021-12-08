package com.digitail.changeColor;

import com.digitail.changeColor.layer.ColoredLayer;
import com.digitail.changeColor.layer.Layer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class PictureService implements IPicture {

    private String name;
    private String path;
    private Layer[] layers;
    private int width;
    private int height;
    private int type = -1;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * <p>Create colored, or layered, or both types of images
     * Requires picture folder to have layers named as:</p>
     * <p>(name)_l(layerNumber).(type) - for non-colorable layers</p>
     * <p>(name)_l(layerNumber)<b>c</b>.(type) - for colorable layers</p>
     *
     * If there is a need to create an image as single layer,
     * you still have to name file in this convention or
     * program won't find it.
     *  name of an image file in pictures folder
     */

    //Создает массив с layers
    public void CreateLayers() {
        var dir = new File(path);
        if(!dir.isDirectory()) throw new IllegalStateException("Wrong path for directory." + dir.getPath());

        var layersList = new ArrayList<Layer>();
        var files = dir.listFiles();
        Arrays.sort(files);

        AddLayers(layersList, files);

        if(layersList.size() == 0) throw new IllegalStateException("Wrong name for an image.");
        layers = layersList.toArray(new Layer[0]);
    }

    private void AddLayers(ArrayList<Layer> layersList, File[] files) {
        for(File file : files) {
            Layer layer = null;
            var fileName = file.getName();

            if(fileName.startsWith(name) && fileName.contains("c.")) {
                layer = new ColoredLayer(file);
            }
            else if (fileName.startsWith(name) && !fileName.equals(name + "_combined.png"))
                layer = new Layer(file);

            if (layer != null) {
                CheckIntegrity(layer);
                layersList.add(layer);
            }
        }
    }

    private void CheckIntegrity(Layer layer) {
        if (layer.getWidth() > width)
            width = layer.getWidth();
        if (layer.getHeight() > height)
            height = layer.getHeight();

        if (type != -1 && type != layer.getType())
            throw new IllegalStateException("Layers have different types");
        type = layer.getType();
    }

    /**
     * <p>Combines all layers of a picture into one.
     * Creates combined file as:</p>
     * <p>(name)_combined.png</p>
     */
    public void CombineLayers() {
        var combined = new BufferedImage(getWidth(), getHeight(), getType());

        for (Layer layer: layers) {
            combined.createGraphics().drawImage(layer.getLayer(), null, 0, 0);
        }

        try {
            var f = new File(path + "/" + name + "_combined.png");
            ImageIO.write(combined, "png", f);
        } catch (IOException e) {
            System.out.println("Something went wrong creating image");
            e.printStackTrace();
        }
    }

    /**
     * Get image width.
     * @return image width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get image height.
     * @return image height in pixels
     */
    public int getHeight() {
        if (height != 0)
            return height;

        height = layers[0].getHeight();
        return height;
    }

    /**
     * Get type for image.
     * @return image type as int, can be used for BufferedImage
     */
    public int getType() {
        return type;
    }

    /**
     * Change color of a layer
     * @param layerNumber layer number in folder, starts with 0
     * @param rgb rgb value, you can use outside tools or Color.getRGB() to create one
     */
    public void ChangeColorOfLayer(int layerNumber, int rgb) {
        if(!(layers[layerNumber] instanceof  ColoredLayer))
            throw new IllegalArgumentException("layerNumber given is not colorable");

        ((ColoredLayer) layers[layerNumber]).ChangeColor(rgb);
    }

    /**
     * Change color of a layer
     * @param layerNumber layer number in folder, starts with 0
     * @param red value for color red; 0 <= red <= 255
     * @param green value for color green; 0 <= green <= 255
     * @param blue value for color blue; 0 <= blue <= 255
     */
    public void ChangeColorOfLayer(int layerNumber, int red, int green, int blue) {
        if(!(layers[layerNumber] instanceof  ColoredLayer))
            throw new IllegalArgumentException("layerNumber given is not colorable");

        ((ColoredLayer) layers[layerNumber]).ChangeColor(red, green, blue);
    }
}

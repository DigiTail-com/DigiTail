package com.digitail.changeColor;

import com.digitail.changeColor.layer.ColoredLayer;
import com.digitail.changeColor.layer.Layer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@Service
public class PictureService {

    private String path;

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
    private Layer[] CreateLayers(File[] files) {
        var layersList = new ArrayList<Layer>();
        Arrays.sort(files);

        AddLayers(layersList, files);

        if(layersList.size() == 0) throw new IllegalStateException("Wrong name for an image.");
        return layersList.toArray(new Layer[0]);
    }

    private void AddLayers(ArrayList<Layer> layersList, File[] files) {
        for(File file : files) {
            Layer layer = null;

            if(file.getName().contains("c.")) {
                layer = new ColoredLayer(file);
            }
            else if (file.getName().contains("_"))
                layer = new Layer(file);

            if (layer != null) {
                layersList.add(layer);
            }
        }
    }

    /**
     * <p>Combines all layers of a picture into one.
     * Creates combined file as:</p>
     * <p>(path)/(uuid).png</p>
     */
    public void CombineLayers(File[] files, String uuid) {
        var layers = CreateLayers(files);
        var combined = new BufferedImage(getWidth(layers), getHeight(layers), getType(layers));

        for (Layer layer: layers) {
            combined.createGraphics().drawImage(layer.getLayer(), null, 0, 0);
        }

        try {
            var f = new File( path + "/" + uuid + ".png");
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
    private int getWidth(Layer[] layers) {
        var width = 0;
        for (Layer layer: layers) {
            if (width < layer.getWidth())
                width = layer.getWidth();
        }

        return width;
    }

    /**
     * Get image height.
     * @return image height in pixels
     */
    private int getHeight(Layer[] layers) {
        var height = 0;
        for (Layer layer: layers) {
            if (height < layer.getHeight())
                height = layer.getHeight();
        }

        return height;
    }

    /**
     * Get type for image.
     * @return image type as int, can be used for BufferedImage
     */
    public int getType(Layer[] layers) {

        return layers[0].getType();
    }

    /**
     * Change color of a layer
     * @param rgb rgb value, you can use outside tools or Color.getRGB() to create one
     */
    public void ChangeColorOfLayer(File file, int rgb) {
        var layers = CreateLayers(new File[] {file});
        if(!(layers[0] instanceof  ColoredLayer))
            throw new IllegalArgumentException("layerNumber given is not colorable");

        ((ColoredLayer) layers[0]).ChangeColor(rgb);
    }

    /**
     * Change color of a layer
     * @param red value for color red; 0 <= red <= 255
     * @param green value for color green; 0 <= green <= 255
     * @param blue value for color blue; 0 <= blue <= 255
     */
    public void ChangeColorOfLayer(File file, int red, int green, int blue) {
        var layers = CreateLayers(new File[] {file});
        if(!(layers[0] instanceof  ColoredLayer))
            throw new IllegalArgumentException("layerNumber given is not colorable");

        ((ColoredLayer) layers[0]).ChangeColor(red, green, blue);
    }
}

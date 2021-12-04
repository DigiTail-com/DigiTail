package com.digitail.changeColor;

public interface IPicture {
    void CombineLayers();
    int getWidth();
    int getHeight();
    int getType();
    void ChangeColorOfLayer(int layerNumber, int rgb);
    void ChangeColorOfLayer(int layerNumber, int red, int green, int blue);
}

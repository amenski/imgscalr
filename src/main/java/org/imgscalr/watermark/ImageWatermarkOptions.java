package org.imgscalr.watermark;

import org.imgscalr.util.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class ImageWatermarkOptions {
    private final int width;
    private final int height;
    /**
     * Apply size percentage relative to base image.
     * Will not be used in conjunction with width and height
     */
    private int ratio;
    private Pair<Integer, Integer> baseWidthAndHeight;

    private final File file;

    private BufferedImage bufferedImage = null;

    public ImageWatermarkOptions(File file, int ratio, Pair<Integer, Integer> baseWidthAndHeight) {
        validate(file, ratio, baseWidthAndHeight);
        this.ratio = ratio;
        this.baseWidthAndHeight = baseWidthAndHeight;
        this.width = baseWidthAndHeight.getLeft() * ratio / 100;
        this.height = baseWidthAndHeight.getRight() * ratio / 100;
        this.file = file;

        validate();
    }

    private void validate(File file, int ratio, Pair<Integer, Integer> baseWidthAndHeight) {
        if (file == null) {
            throw new RuntimeException("File can't be null.");
        }
        if (ratio <= 0 ) {
            throw new RuntimeException("Invalid ratio value.");
        }
        if (baseWidthAndHeight == null) {
            throw new RuntimeException("Watermark image/baseWidthAndHeight can't be null");
        }
    }

    public ImageWatermarkOptions(int width, int height, File file) {
        this.width = width;
        this.height = height;
        this.file = file;

        validate();
        if (file == null) {
            throw new RuntimeException("Watermark image can't be null");
        }
    }

    public ImageWatermarkOptions(File file) throws IOException {
        this.file = file;
        if (file == null) {
            throw new RuntimeException("Watermark image can't be null");
        }
        BufferedImage image = ImageIO.read(file);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.bufferedImage = image;
        validate();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRatio() {
        return ratio;
    }

    public Pair<Integer, Integer> getBaseWidthAndHeight() {
        return baseWidthAndHeight;
    }

    public File getFile() {
        return file;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void validate() {
        if (this.width < 1 || this.height < 1) {
            throw new RuntimeException("Invalid config");
        }
    }
}
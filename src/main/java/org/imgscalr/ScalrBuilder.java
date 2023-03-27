package org.imgscalr;

import org.imgscalr.Scalr.Mode;
import org.imgscalr.WaterMark.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScalrBuilder {

    private int width;
    private int height;
    private String outputFormat;
    private BufferedImage bfi;

    public ScalrBuilder of(final String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name can not be null.");
        }
        bfi = ImageIO.read(Files.newInputStream(Paths.get(fileName)));
        return this;
    }

    public ScalrBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ScalrBuilder outputFormat(final String format) {
        this.outputFormat = format;
        return this;
    }

    public ScalrBuilder resize(Mode mode) {
        resize(mode);
        return this;
    }

    public ScalrBuilder resize() {
        Scalr.resize(bfi, Mode.AUTOMATIC, width, height);
        return this;
    }

    public ScalrBuilder addTextWaterMark(final String text, Position position, float opacity) {
        WaterMark waterMark = new WaterMark(position, bfi, opacity);
        waterMark.addTextWatermark(text, Color.RED);
        return this;
    }

    public void toFile(final String fileName) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("File name can not be empty");
            }
            if (outputFormat == null || outputFormat.isEmpty()) {
                String format = fileName.substring(fileName.lastIndexOf('.') + 1);
                outputFormat = !format.isEmpty() ? format : "png";
            }
            ImageIO.write(bfi, outputFormat, outputStream);
            outputStream.flush();
            Files.write(Paths.get(fileName), outputStream.toByteArray());
        } catch (Exception e) {
            throw e;
        }
    }

    public BufferedImage toBufferedImage() {
        return this.bfi;
    }
}

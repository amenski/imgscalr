package org.imgscalr;

import org.imgscalr.Scalr.Mode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScalrBuilder {

    private int width;
    private int height;
    private String extension;
    private BufferedImage bfi;


    public ScalrBuilder of(final String fileName) throws IOException {
        bfi = ImageIO.read(Files.newInputStream(Paths.get(fileName)));
        return this;
    }

    public ScalrBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ScalrBuilder extension(final String extension) {
        this.extension = extension;
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

    public void toFile(final String fileName) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            // TODO if file doesnt have extension, put jpg?
            ImageIO.write(bfi, extension, outputStream);
            outputStream.flush();
            Files.write(Paths.get(""), outputStream.toByteArray());
        } catch (Exception e) {
            throw e;
        }
    }
}

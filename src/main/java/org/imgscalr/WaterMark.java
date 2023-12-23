package org.imgscalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.imgscalr.Scalr.log;

public class WaterMark {

    private final Position position;
    private final BufferedImage bfi;
    private final float opacity;

    public WaterMark(Position position, BufferedImage bfi, float opacity) {
        this.position = position;
        this.bfi = bfi;
        this.opacity = opacity;
    }

    /**
     * Embeds a textual watermark over a source image
     *
     * @param text The text to be embedded as watermark.
     */
    public void addTextWatermark(String text, TextWatermarkOptions options) {
        Graphics2D g2d = (Graphics2D) bfi.getGraphics();

        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        g2d.setComposite(alphaChannel);
        g2d.setFont(options.getFont());
        g2d.setColor(options.getColor());
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

        position.calculate(
                bfi.getWidth(),
                bfi.getHeight(),
                (int) rect.getWidth(),
                (int) rect.getHeight()
        );

        g2d.drawString(text, position.getX(), position.getY());
        g2d.dispose();
    }

    /**
     * Embeds an image watermark over a source image
     *
     * @param watermarkImageFile The image file used as the watermark.
     */
    public BufferedImage addImageWatermark(File watermarkImageFile) throws IOException {
        try {
            BufferedImage watermarkImage = ImageIO.read(watermarkImageFile);

            // initializes necessary graphic properties
            Graphics2D g2d = (Graphics2D) bfi.getGraphics();
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2d.setComposite(alphaChannel);

            position.calculate(
                    bfi.getWidth(),
                    bfi.getHeight(),
                    watermarkImage.getWidth(),
                    watermarkImage.getHeight()
            );

            // paints the image watermark
            g2d.drawImage(watermarkImage, position.getX(), position.getY(), null);
            g2d.dispose();
            return bfi;
        } catch (IOException ex) {
            log(0, "Error adding water mark %s", ex.getMessage());
            throw ex;
        }
    }

    public enum Position {
        TOP_LEFT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                result = Pair.of(0 ,0);
            }
        },
        TOP_CENTER {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                result = Pair.of(
                        (imageWidth - waterMarkWidth) / 2,
                        imageHeight - (imageHeight - waterMarkHeight)
                );
            }
        },
        TOP_RIGHT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                result = Pair.of(
                        imageWidth - waterMarkWidth,
                        0
                );
            }
        },
        CENTER_LEFT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                result = Pair.of(
                        0,
                        (imageHeight - waterMarkHeight) / 2
                );
            }
        },
        CENTER {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                result = Pair.of(
                        (imageWidth - waterMarkWidth) / 2,
                        (imageHeight - waterMarkHeight) / 2
                );
            }
        },
        CENTER_RIGHT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                result = Pair.of(
                        imageWidth - waterMarkWidth,
                        imageHeight / 2
                );
            }
        },
        BOTTOM_LEFT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                result = Pair.of(
                        0,
                        imageHeight - waterMarkHeight
                );
            }
        },
        BOTTOM_CENTER {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                result = Pair.of(
                        (imageWidth - waterMarkWidth) / 2,
                        imageHeight - waterMarkHeight
                );
            }
        },
        BOTTOM_RIGHT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                result = Pair.of(
                        imageWidth - waterMarkWidth,
                        imageHeight - waterMarkHeight
                );
            }
        };

        protected Pair<Integer, Integer> result;

        int getX() {return result.getLeft();}
        int getY() {return result.getRight();}
        public abstract void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight);

    }


    public static class TextWatermarkOptions {
        private final String text;
        private final String fontFamily;
        private final int fontSize;
        private final int style;
        private final Color color;

        public TextWatermarkOptions(String text, String fontFamily, int fontSize, int style, Color color) {
            this.text = text;
            this.fontFamily = fontFamily;
            this.fontSize = fontSize;
            this.style = style;
            this.color = color;
        }

        public String getText() {
            return this.text;
        }
        public Color getColor() {
            return this.color;
        }
        public Font getFont() {
            return new Font(this.fontFamily, this.style, this.fontSize);
        }
    }

    public static class Pair<L, R> {
        private final L left;
        private final R right;

        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public static <L, R> Pair<L, R> of(L left, R right) {
            return new Pair<>(left, right);
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right);
        }
    }
}

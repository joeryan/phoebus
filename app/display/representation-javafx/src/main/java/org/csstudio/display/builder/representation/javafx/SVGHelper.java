/*
 * Copyright (C) 2019 European Spallation Source ERIC.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package org.csstudio.display.builder.representation.javafx;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Helper class to load SVG files. It is based on the following post on Stackoverflow:
 * https://stackoverflow.com/questions/12436274/svg-image-in-javafx-2-2.
 *
 * The dependency to Apache Batik inflates the size of the lib folder by ~3 MB.
 *
 * Testing shows that gradients may not always render correctly. Maybe a Batik issue?
 */
public class SVGHelper {

    /**
     * Loads SVG file as an {@link Image}. The resolution of the generated image is determined by the
     * width and height parameters. Consequently scaling to a much larger size will result in "pixelation".
     * Client code is hence advised to reload the SVG resource using the new width and height when
     * the container widget is resized.
     *
     * @param fileStream Non-null input stream to SVG file.
     * @param width The wanted width of the image.
     * @param height The wanted height of the image.
     * @return A {@link Image} object if the input stream can be parsed and transcoded.
     */
    public static Image loadSVG(InputStream fileStream, double width, double height) throws Exception{
        BufferedImageTranscoder bufferedImageTranscoder = new BufferedImageTranscoder();
        TranscoderInput input = new TranscoderInput(fileStream);
        try{
            bufferedImageTranscoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float)width);
            bufferedImageTranscoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float)height);
            bufferedImageTranscoder.transcode(input, null);

            return SwingFXUtils.toFXImage(bufferedImageTranscoder.getBufferedImage(), null);
        } catch (TranscoderException e) {
            throw new Exception("Unable to transcode SVG file", e);
        }
    }

    private static class BufferedImageTranscoder extends ImageTranscoder {

        private BufferedImage bufferedImage = null;

        @Override
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        @Override
        public void writeImage(BufferedImage bufferedImage, TranscoderOutput transcoderOutput) {
            this.bufferedImage = bufferedImage;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }
    }
}

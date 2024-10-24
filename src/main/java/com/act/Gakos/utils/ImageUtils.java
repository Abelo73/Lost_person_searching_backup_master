package com.act.Gakos.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.DataFormatException;

public class ImageUtils {

    // Compress the image data
    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();  // Ensure compression is finished

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while (!deflater.finished()) {
                int size = deflater.deflate(buffer);  // Compress the data
                outputStream.write(buffer, 0, size);
            }
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Error while compressing image: " + e.getMessage());
        }

        return outputStream.toByteArray();
    }

    // Decompress the image data
    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int size = inflater.inflate(buffer);  // Decompress the data
                outputStream.write(buffer, 0, size);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            System.err.println("Error while decompressing image: " + e.getMessage());
        }

        return outputStream.toByteArray();
    }
}

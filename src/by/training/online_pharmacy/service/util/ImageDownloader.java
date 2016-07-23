package by.training.online_pharmacy.service.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vladislav on 23.07.16.
 */
public class ImageDownloader {
    private URL url;
    public ImageDownloader(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public byte[] download() throws IOException {
        try(InputStream is = url.openStream ();ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] byteChunk = new byte[4096];
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
            return baos.toByteArray();
        }
    }
}

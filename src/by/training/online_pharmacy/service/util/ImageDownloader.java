package by.training.online_pharmacy.service.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by vladislav on 15.08.16.
 */
public final class ImageDownloader {

    public static void download(String sourceUrl, String destinationFolder, String fileName) throws IOException {
        URL website = new URL(sourceUrl);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        File uploads = new File(destinationFolder);
        File file = new File(uploads, fileName+ImageConstant.IMAGE_JPG);
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}

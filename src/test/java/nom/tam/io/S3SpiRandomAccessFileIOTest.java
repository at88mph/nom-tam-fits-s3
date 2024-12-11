package nom.tam.io;

/*-
 * #%L
 * nom-tam-fits-s3
 * %%
 * Copyright (C) 2024 nom-tam-fits-s3
 * %%
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * #L%
 */

import junit.framework.TestCase;
import nom.tam.fits.BasicHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.Header;
import nom.tam.fits.HeaderCard;
import nom.tam.util.Cursor;
import org.junit.Test;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;


public class S3SpiRandomAccessFileIOTest {

    @Test
    public void S3FileSystem() throws IOException, URISyntaxException {

        String endpoint = "xxxxx.com";
        String spath = "bucket_name/xxxx.fits";
        String path = String.format("s3x://%s/%s", endpoint, spath);
        System.setProperty("aws.region", "ap-northeast-1");
        System.setProperty("s3.spi.endpoint-protocol", "http");
        System.setProperty("aws.accessKeyId", "xxxxxxx");
        System.setProperty("aws.secretAccessKey", "xxxxxx");
        S3SpiRandomAccessFileIO fileIO = new S3SpiRandomAccessFileIO(Paths.get(new URI(path)));
        Fits fits = new Fits(fileIO);
        BasicHDU<?> hdu;
        int hduIndex = 0;
        while ((hdu = fits.readHDU()) != null) {
            Header header = hdu.getHeader();
            if (header != null) {
                Cursor<String, HeaderCard> iter = header.iterator();
                while (iter.hasNext()) {
                    HeaderCard card = iter.next();
                    String key1 = "HEADER_" + hduIndex + "_" + card.getKey();
                    String value = card.getValue() != null ? card.getValue() : "null";
                    System.out.printf("key: %s, value: %s%n", key1, value);
                }
            }
            hduIndex++;
        }
    }

}

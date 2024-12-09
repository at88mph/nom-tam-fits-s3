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

import nom.tam.util.RandomAccessFileIO;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Uri;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;


/**
 * This class supports random read-only access to FITS files using the S3 protocol.  In order to ensure configuration
 * like authentication to the S3 service is handled correctly, the S3Client is passed in as a parameter.
 */
public class S3RandomAccessFileIO implements RandomAccessFileIO {
    private final S3Client client;
    private final S3Uri uri;
    private long length;

    private long position = 0L;


    public S3RandomAccessFileIO(S3Client client, S3Uri uri, long length) {
        this.client = client;
        this.uri = uri;
        this.length = length;
    }


    @Override
    public void setLength(long length) throws IOException {
        this.length = length;
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }

    @Override
    public long position() throws IOException {
        return this.position;
    }

    @Override
    public void position(long n) throws IOException {
        this.position = n;
    }

    @Override
    public long length() throws IOException {
        return this.length;
    }

    @Override
    public int read(byte[] b, int from, int readLength) throws IOException {
        final long available = Math.min(readLength, this.length - this.position);
        if (available == 0L) {
            return -1;
        }

        try (InputStream inputStream = openStream()) {
            final byte[] result = inputStream.readAllBytes();
            System.arraycopy(result, from, b, 0, (int) available);

            this.position += available;
            return (int) available;
        }
    }

    /**
     * Open an input stream to the S3 object.  Tests can override this to avoid making network calls.
     * @return  InputStream to the S3 object.  Never null.
     */
    InputStream openStream() {
        final GetObjectRequest.Builder getObjectRequestBuilder =
                GetObjectRequest.builder()
                                .range(String.format("bytes=%d-%d", this.position, (this.length - 1L)))
                                .bucket(this.uri.bucket().orElseThrow(
                                        () -> new IllegalArgumentException("Bucket is required")))
                                .key(this.uri.key().orElseThrow(
                                        () -> new IllegalArgumentException("Key (filename) is required")));

        return client.getObject(getObjectRequestBuilder.build());
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("read()");
    }

    @Override
    public String readUTF() throws IOException {
        throw new UnsupportedOperationException("readUTF()");
    }

    @Override
    public void writeUTF(String s) throws IOException {
        throw new UnsupportedOperationException("writeUTF()");
    }

    @Override
    public void write(int b) throws IOException {
        throw new UnsupportedOperationException("write(int)");
    }

    @Override
    public void write(byte[] b, int from, int writeLength) throws IOException {
        throw new UnsupportedOperationException("write(byte[], int, int)");
    }

    @Override
    public FileChannel getChannel() {
        throw new UnsupportedOperationException("getChannel()");
    }

    @Override
    public FileDescriptor getFD() throws IOException {
        throw new UnsupportedOperationException("getFD()");
    }
}

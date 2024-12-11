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

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class S3SpiRandomAccessFileIO implements RandomAccessFileIO {

    private final Path filePath;
    private final FileChannel fileChannel;

    public S3SpiRandomAccessFileIO(Path filePath) throws IOException {
        this.filePath = filePath;
        this.fileChannel = FileChannel.open(filePath, StandardOpenOption.READ);
    }

    // Read UTF string (mock implementation)
    @Override
    public String readUTF() throws IOException {
        throw new UnsupportedOperationException("readUTF is not implemented for S3 paths.");

    }

    @Override
    public FileChannel getChannel() {
        return this.fileChannel;
    }

    @Override
    public FileDescriptor getFD() throws IOException {

        throw new UnsupportedOperationException("FileDescriptor is not supported for S3 paths.");
    }

    @Override
    public void setLength(long l) throws IOException {
        throw new UnsupportedOperationException("Setting file length is not supported for S3 paths.");
    }

    @Override
    public void writeUTF(String s) throws IOException {
        throw new UnsupportedOperationException("Write operation is not implemented for S3 paths.");
    }

    @Override
    public void close() throws IOException {
        if (fileChannel != null) {
            fileChannel.close();
        }
    }

    @Override
    public long position() throws IOException {
        return fileChannel.position();
    }

    @Override
    public void position(long l) throws IOException {
        fileChannel.position(l);
    }

    @Override
    public long length() throws IOException {
        return Files.size(filePath);
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("read()");
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        return fileChannel.read(buffer);
    }

    @Override
    public void write(int i) throws IOException {
        throw new UnsupportedOperationException("Write operation is not implemented for S3 paths.");
    }

    @Override
    public void write(byte[] bytes, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("Write operation is not implemented for S3 paths.");
    }


}
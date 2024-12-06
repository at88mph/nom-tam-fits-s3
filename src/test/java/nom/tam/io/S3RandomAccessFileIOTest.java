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


import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class S3RandomAccessFileIOTest {

    @Test
    public void testReadUnsupported() throws Exception {
        try (S3RandomAccessFileIO testSubject = new S3RandomAccessFileIO(null, null, 88L)) {
            testSubject.read();
            Assert.fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException unsupportedOperationException) {
            // Good!
        }

        try {
            try (S3RandomAccessFileIO testSubject = new S3RandomAccessFileIO(null, null, 88L)) {
                testSubject.readUTF();
                Assert.fail("Expecting exception: UnsupportedOperationException");
            }
        } catch (UnsupportedOperationException unsupportedOperationException) {
            // Good!
        }

        try {
            try (S3RandomAccessFileIO testSubject = new S3RandomAccessFileIO(null, null, 88L)) {
                testSubject.getChannel();
                Assert.fail("Expecting exception: UnsupportedOperationException");
            }
        } catch (UnsupportedOperationException unsupportedOperationException) {
            // Good!
        }

        try {
            try (S3RandomAccessFileIO testSubject = new S3RandomAccessFileIO(null, null, 88L)) {
                testSubject.getFD();
                Assert.fail("Expecting exception: UnsupportedOperationException");
            }
        } catch (UnsupportedOperationException unsupportedOperationException) {
            // Good!
        }

        try {
            try (S3RandomAccessFileIO testSubject = new S3RandomAccessFileIO(null, null, 88L)) {
                testSubject.write(0);
                Assert.fail("Expecting exception: UnsupportedOperationException");
            }
        } catch (UnsupportedOperationException unsupportedOperationException) {
            // Good!
        }

        try {
            try (S3RandomAccessFileIO testSubject = new S3RandomAccessFileIO(null, null, 88L)) {
                testSubject.writeUTF("");
                Assert.fail("Expecting exception: UnsupportedOperationException");
            }
        } catch (UnsupportedOperationException unsupportedOperationException) {
            // Good!
        }

        try {
            try (S3RandomAccessFileIO testSubject = new S3RandomAccessFileIO(null, null, 88L)) {
                testSubject.write(new byte[0], 0, 0);
                Assert.fail("Expecting exception: UnsupportedOperationException");
            }
        } catch (UnsupportedOperationException unsupportedOperationException) {
            // Good!
        }
    }

    @Test
    public void testRead() throws Exception {
        final byte[] input = new byte[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        };

        try (S3RandomAccessFileIO testSubject = new S3RandomAccessFileIO(null, null, input.length) {
            @Override
            InputStream openStream() {
                return new ByteArrayInputStream(input);
            }
        }) {
            final byte[] buffer = new byte[8192];
            testSubject.read(buffer, 2, 4);

            final byte[] expected = new byte[] {
                2, 3, 4, 5
            };
            Assert.assertArrayEquals("Wrong result", expected, buffer);
        }
    }
}
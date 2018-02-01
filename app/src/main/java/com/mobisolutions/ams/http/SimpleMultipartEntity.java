package com.mobisolutions.ams.http;

import android.graphics.Bitmap;

import com.mobisolutions.ams.utils.AppUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by vkilari on 12/18/17.
 */

public class SimpleMultipartEntity implements HttpEntity {

    private static final String TAG = SimpleMultipartEntity.class.getSimpleName();
//private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private String boundary = null;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    boolean isSetLast = false;
    boolean isSetFirst = false;

    public SimpleMultipartEntity() {
   /* final StringBuffer buf = new StringBuffer();
    final Random rand = new Random();
    for (int i = 0; i < 30; i++) {
        buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
    }*/
        this.boundary = "----WebKitFormBoundaryp7MA4YWxkTrZu0gW";

    }

    public void writeFirstBoundaryIfNeeds() {
        if (!isSetFirst) {
            try {
                out.write(("--" + boundary + "\r\n").getBytes());
            } catch (final IOException e) {
                AppUtils.debugThrowable(TAG, e);
            }
        }
        isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if (isSetLast) {
            return;
        }
        try {
            out.write(("\r\n--" + boundary + "--\r\n").getBytes());
        } catch (final IOException e) {
            AppUtils.debugThrowable(TAG, e);
        }
        isSetLast = true;
    }

    public void addPart(final String key, final String value) {
        writeFirstBoundaryIfNeeds();
        try {
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n")
                    .getBytes());
            out.write("Content-Type: text/plain; charset=UTF-8\r\n".getBytes());
            out.write("Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes());
            out.write(value.getBytes());
            out.write(("\r\n--" + boundary + "\r\n").getBytes());
        } catch (final IOException e) {
            AppUtils.debugThrowable(TAG, e);
        }
    }

    public void addPart(final String key, final String fileName,
                        final InputStream fin) {
        addPart(key, fileName, fin, "application/octet-stream");
    }

    public void addPart(final String key, final String fileName,
                        final InputStream fin, String type) {
        writeFirstBoundaryIfNeeds();
        try {
            type = "Content-Type: " + type + "\r\n";
            out.write(("Content-Disposition: form-data; name=\"" + key
                    + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

            final byte[] tmp = new byte[4096];
            int l = 0;
            while ((l = fin.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            out.flush();
        } catch (final IOException e) {
            AppUtils.debugThrowable(TAG, e);
        } finally {
            try {
                fin.close();
            } catch (final IOException e) {
                AppUtils.debugThrowable(TAG, e);
            }
        }
    }

    public void addPart(final String key, final File value) {
        try {
            addPart(key, value.getName(), new FileInputStream(value));
        } catch (final FileNotFoundException e) {
            AppUtils.debugThrowable(TAG, e);
        }
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    @Override
    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return out.toByteArray().length;
    }

    @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary="
                + boundary);
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        outstream.write(out.toByteArray());
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public void consumeContent() throws IOException,
            UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
                    "Streaming entity does not implement #consumeContent()");
        }
    }

    @Override
    public InputStream getContent() throws IOException,
            UnsupportedOperationException {
        return new ByteArrayInputStream(out.toByteArray());
    }

    public void addPart(String fileKey, String fileName, Bitmap imageBitmap, String type) {
        writeFirstBoundaryIfNeeds();
        try {
            type = "Content-Type: " + type + "\r\n";
            out.write(("Content-Disposition: form-data; name=\"" + fileKey
                    + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
            out.write(AppUtils.getByteArray(imageBitmap));

            out.flush();
        } catch (final IOException e) {
            AppUtils.debugThrowable(TAG, e);
        } finally {
            try {

            } catch (final Exception e) {
                AppUtils.debugThrowable(TAG, e);
            }
        }
    }

    public void addPart(final String key, final String fileName,
                        final Bitmap fin) {
        addPart(key, fileName, fin, "application/octet-stream");
    }

}
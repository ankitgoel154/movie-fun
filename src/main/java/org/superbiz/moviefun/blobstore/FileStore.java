package org.superbiz.moviefun.blobstore;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.util.Optional;

import static java.lang.String.format;

public class FileStore implements BlobStore {

    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.name);
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            this.copy(blob.inputStream,outputStream);
        }
        // ...
    }

    public void copy(InputStream in, OutputStream out) throws IOException {

        byte[] buffer = new byte[1024];
        while (true) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1)
                break;
            out.write(buffer, 0, bytesRead);
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        // ...
        return null;
    }

    @Override
    public void deleteAll() {
        // ...
    }
}
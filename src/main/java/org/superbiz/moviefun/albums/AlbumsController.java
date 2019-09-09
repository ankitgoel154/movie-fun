package org.superbiz.moviefun.albums;

import com.amazonaws.util.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Controller
@RequestMapping("/albums")
public class AlbumsController {

    private final AlbumsBean albumsBean;

    @Autowired
    private BlobStore blobStore;

    public AlbumsController(AlbumsBean albumsBean) {
        this.albumsBean = albumsBean;
    }


    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("albums", albumsBean.getAlbums());
        return "albums";
    }

    @GetMapping("/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        model.put("album", albumsBean.find(albumId));
        return "albumDetails";
    }

    @PostMapping("/{albumId}/cover")
    public String uploadCover(@PathVariable long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {
        String coverFileName = format("covers/%d", albumId);
        Blob blob = new Blob(coverFileName,uploadedFile.getInputStream(),new Tika().detect(uploadedFile.getInputStream()));
        blobStore.put(blob);
        return format("redirect:/albums/%d", albumId);
    }

    @GetMapping("/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) throws IOException, URISyntaxException {
        String coverFileName = format("covers/%d", albumId);
        System.out.println("1");
        Optional<Blob> optionalBlob = blobStore.get(coverFileName);
        HttpHeaders headers = createImageHttpHeaders(optionalBlob.get());
        byte[] bytes = IOUtils.toByteArray(optionalBlob.get().inputStream);
        headers.setContentLength(bytes.length);
        headers.forEach((k,v) -> {System.out.println(k+v);});
        return new HttpEntity<>(bytes,headers);
    }

    private HttpHeaders createImageHttpHeaders(Blob blob) throws IOException {
        String contentType = blob.contentType;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));

        return headers;
    }



}

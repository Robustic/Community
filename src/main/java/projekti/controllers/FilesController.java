package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import projekti.entities.FileObject;
import projekti.repositories.FileObjectRepository;

@Controller
public class FilesController {
    
    @Autowired
    private FileObjectRepository fileObjectRepository;
    
    @GetMapping("/files/{filename}")
    public ResponseEntity<byte[]> viewFile(@PathVariable String filename) {
        FileObject fileObject = fileObjectRepository.findByFilename(filename);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileObject.getContentType()));
        headers.setContentLength(fileObject.getContentLength());
        headers.add("Content-Disposition", "attachment; filename=" + fileObject.getFilename());

        return new ResponseEntity<>(fileObject.getContent(), headers, HttpStatus.CREATED);
    }
}

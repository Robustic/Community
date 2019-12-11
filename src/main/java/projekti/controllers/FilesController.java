package projekti.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import projekti.entities.FileObject;
import projekti.repositories.FileObjectRepository;
import projekti.services.FileObjectService;

@Controller
public class FilesController {
    
    @Autowired
    private FileObjectRepository fileObjectRepository;
    
    @Autowired
    private FileObjectService fileObjectService;
    
    @GetMapping("/files/{filename}")
    public ResponseEntity<byte[]> viewFile(@PathVariable String filename) {
        FileObject fileObject = fileObjectRepository.findByFilename(filename);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileObject.getContentType()));
        headers.setContentLength(fileObject.getContentLength());
        headers.add("Content-Disposition", "attachment; filename=" + fileObject.getFilename());

        return new ResponseEntity<>(fileObject.getContent(), headers, HttpStatus.CREATED);
    }
    
    @GetMapping("/defaultfiles")
    public String setDefaultPicture() {
        return "files";
    }

    @PostMapping("/defaultfiles/defaultpicture")
    public String savePicture(@RequestParam("file") MultipartFile file, @RequestParam String text) {
        try {
            FileObject fo = new FileObject();

            fo.setFilename(file.getOriginalFilename());
            fo.setContentType(file.getContentType());
            fo.setContentLength(file.getSize());
            
            fo.setDescription(text);
            
            fo.setContent(file.getBytes());

            fileObjectRepository.save(fo);
        } catch (IOException e) {
            System.out.println(e);
        }
        return "redirect:/files";
    }
}

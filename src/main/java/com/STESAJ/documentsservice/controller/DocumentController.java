package com.STESAJ.documentsservice.controller;

import com.STESAJ.documentsservice.entity.Document;
import com.STESAJ.documentsservice.entity.File;
import com.STESAJ.documentsservice.entity.Response;
import com.STESAJ.documentsservice.service.DocumentService;
import com.STESAJ.documentsservice.service.FileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import javax.validation.Valid;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "http://localhost:4200")

public class DocumentController {
    @Autowired
    private FileService fileService;

    @Autowired
    private DocumentService documentService;

    @GetMapping ("/all")
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.findAllDocuments();
        if (documents.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }

        return  ResponseEntity.ok(documents);
    }


    @GetMapping
    public ResponseEntity<List<File>> getAllFiles() throws Exception {


        List<File> files = fileService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(DocumentController.class, "getFiles", path.getFileName().toString()).build().toString();
            return new File(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }


    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(@Valid @RequestParam("files") MultipartFile file) throws Exception {
        Document document = new Document();
        document.setAutor("Autor default");
        Document documentCreate =  documentService.createDocument(document);
        documentCreate.setFile(fileService.save(file,documentCreate.getDocument_id()));
        MediaType mediaType = MediaType.parseMediaType(file.getContentType());


        System.out.println(file.getContentType());
        System.out.println(mediaType);
        System.out.println(mediaType.getType());
//        if(!"application/pdf".equals(mediaType.getType())) {
//            throw new IllegalArgumentException("Incorrect file type, PDF required.");
//        }

        return ResponseEntity.status(HttpStatus.CREATED).body(documentCreate);

    }


    @GetMapping(value = "/{id}")
    @Validated
    public ResponseEntity<Document> getDocument(@PathVariable("id") Long id) throws Exception {


        Document document =  documentService.getDocument(id);
        System.out.println(document.getDocument_id());

        if (null==document){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(document);

    }


    @GetMapping(value = "/id/{filename:.+}", produces={"application/pdf"})
    public ResponseEntity<Resource> getFiles( @PathVariable String filename) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(filename);
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=" + filename);
        Resource resource = fileService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String>  error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }





}

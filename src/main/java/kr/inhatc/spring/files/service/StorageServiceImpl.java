package kr.inhatc.spring.files.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService {

  private final Path root = Paths.get("uploads");

  @Override
  public void init() {
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      throw new RuntimeException("업로드를 위한 폴더를 초기화 할 수 없습니다!");
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());

  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
    } catch (IOException e) {
      throw new RuntimeException("파일을 로드 할 수 없습니다!");
    }
  }

  @Override
  public void store(MultipartFile file) {
    try {
      Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
    } catch (Exception e) {
      throw new RuntimeException("파일을 저장할 수 없습니다. 오류(Error): " + e.getMessage());
    }
    
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("파일을 읽을 수 없습니다!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("오류(Error): " + e.getMessage());
    }
  }

  @Override
  public Path load(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return file;
      } else {
        throw new RuntimeException("파일을 읽을 수 없습니다!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("오류(Error): " + e.getMessage());
    }
  }

}

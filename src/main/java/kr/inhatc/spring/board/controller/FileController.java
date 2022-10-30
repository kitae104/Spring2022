package kr.inhatc.spring.board.controller;

import kr.inhatc.spring.board.entity.File;
import kr.inhatc.spring.board.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@Log4j2
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;

    @GetMapping(value = {"/board/fileDownload/{fileIdx}"})
    @ResponseBody
    public void downloadFile(HttpServletResponse res, @PathVariable Long fileIdx) throws UnsupportedEncodingException {
        
        //파일 조회
        File fileInfo = fileRepository.findById(fileIdx).get();
        log.info(">>>> 파일 정보 : " + fileInfo);
        
        //파일 경로
        Path saveFilePath = Paths.get(fileInfo.getUploadDir() + java.io.File.separator + fileInfo.getSavedFileName());

        //해당 경로에 파일이 없으면
        if(!saveFilePath.toFile().exists()) {
            throw new RuntimeException("file not found");
        }

        //파일 헤더 설정
        setFileHeader(res, fileInfo);

        //파일 복사
        fileCopy(res, saveFilePath);
    }

    private void setFileHeader(HttpServletResponse res, File fileInfo) throws UnsupportedEncodingException {
        res.setHeader("Content-Disposition", "attachment; filename=\"" +  URLEncoder.encode((String) fileInfo.getOriginFileName(), "UTF-8") + "\";");
        res.setHeader("Content-Transfer-Encoding", "binary");
        res.setHeader("Content-Type", "application/download; utf-8");
        res.setHeader("Pragma", "no-cache;");
        res.setHeader("Expires", "-1;");
    }

    private void fileCopy(HttpServletResponse res, Path saveFilePath) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(saveFilePath.toFile());
            FileCopyUtils.copy(fis, res.getOutputStream());
            res.getOutputStream().flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                fis.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

package com.oriseq.controller.basis;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.FileInfo;
import com.oriseq.dtm.vo.FileDownloadRequestVO;
import com.oriseq.service.IFileInfoService;
import com.oriseq.service.SysConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Description: 文件管理
 * 上传、下载、删除
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/27 11:42
 */
@RestController
@RequestMapping("file")
@EnableLogging
@Slf4j
public class FileController {


    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private LoginTool loginTool;
    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 生成随机文件名
     *
     * @param originalFilename
     * @return
     */
    private static String generateRandomFilename(String originalFilename) {
        // 生成随机文件名的逻辑
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String randomName = UUID.randomUUID().toString().replace("-", "") + extension;
        return randomName;
    }

    /**
     * 附件上传，记录所属具体的样本ID
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<Object> uploadFile(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.defaultErrorByMessage("文件为空");
            }

            // 取用户手机号
            LoginUser userInfo = loginTool.getUserInfo();
            String phoneNumber = userInfo.getPhone();

            // 获取操作系统名称
            String osName = System.getProperty("os.name").toLowerCase();
            // 构建上传文件的路径
            String uploadPath;
            String originalFilename = file.getOriginalFilename();
            String randomFilename = generateRandomFilename(file.getOriginalFilename());

            if (osName.contains("win")) {
                uploadPath = Paths.get("C://", uploadDir, phoneNumber, randomFilename).toString();
            } else {
                uploadPath = Paths.get(uploadDir, phoneNumber, randomFilename).toString();
            }

            // 判断是否存在路径， 不存在就创建
            Path path = Paths.get(uploadPath).getParent();
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            // 保存文件
            Files.copy(file.getInputStream(), Paths.get(uploadPath), StandardCopyOption.REPLACE_EXISTING);
            // 获取文件大小
            long fileSize = file.getSize();


            // 保存数据库
            FileInfo fileInfo = new FileInfo();
            fileInfo.setOriginalFileName(originalFilename);
            fileInfo.setPath(uploadPath);
            fileInfo.setFileSize(fileSize);
            // 设置附件类型
            fileInfo.setAttachmentType();
            fileInfoService.save(fileInfo);

            Result<Object> result = Result.defaultSuccessByMessage("文件上传成功");

            // 返回数据
            // 文件信息
            JSONObject fileJson = JSONUtil.createObj()
                    .put("id", fileInfo.getId().toString())
                    .put("originalFileName", fileInfo.getOriginalFileName())
                    .put("fileSize", fileInfo.getFileSize())
                    .put("creationTime", DateUtil.formatLocalDateTime(fileInfo.getCreationTime()));


            // 将 JSONObject 转换为 Object
            Object o = JSONUtil.toBean(fileJson, Object.class);
            return Result.defaultSuccessByMessageAndData("文件上传成功", o);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.defaultErrorByMessage("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 获取文件
     *
     * @param fileId
     * @param request
     * @return
     * @throws MalformedURLException
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable("id") String fileId, HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
        FileInfo fileInfo = fileInfoService.getById(fileId);
        if (fileInfo == null) {
            return ResponseEntity.notFound().build();
        }

        // 获取文件路径
        String filePath = fileInfo.getPath();
        File file = new File(filePath);

        // 判断文件是否存在
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 构建文件资源
        Resource resource = new UrlResource(file.toURI());

        // 设置响应头信息
        String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header("Access-Control-Expose-Headers", "Content-Disposition")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" +
                        URLEncoder.encode(fileInfo.getOriginalFileName(), StandardCharsets.UTF_8.name()) + "\"")
                .body(resource);
    }


    /**
     * 获取多个文件,压缩返回
     *
     * @return
     * @throws MalformedURLException
     */
    @PostMapping("/getFiles")
    public void getFile(@RequestBody @Validated FileDownloadRequestVO fileDownloadRequestVO, HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/zip");
        String dateTime = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "files_" + dateTime + ".zip";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 创建 ZIP 输出流
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            Set<String> addedEntries = new HashSet<>(); // 跟踪已添加的条目

            for (String fileId : fileDownloadRequestVO.getFileIds()) {
                FileInfo fileInfo = fileInfoService.getById(fileId);
                if (fileInfo == null || fileInfo.getPath() == null) {
                    log.error("文件记录不存在: " + fileId);
                    continue;
                }

                File file = new File(fileInfo.getPath());
                if (!file.exists() || !file.isFile()) {
                    log.error("文件不存在: " + fileId);
                    continue;
                }

                try (InputStream inputStream = new FileInputStream(file)) {
                    String originalName = fileInfo.getOriginalFileName();
                    String uniqueName = generateUniqueName(originalName, addedEntries);

                    // 添加 ZIP 条目
                    zipOut.putNextEntry(new ZipEntry(uniqueName));
                    // 写入文件内容
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) >= 0) {
                        zipOut.write(buffer, 0, length);
                    }
                    zipOut.closeEntry();
                    addedEntries.add(uniqueName); // 记录已添加的条目
                } catch (IOException e) {
                    log.error("读取文件失败: " + fileId, e);
                }
            }
        }
    }

    private String generateUniqueName(String originalName, Set<String> addedEntries) {
        if (!addedEntries.contains(originalName)) {
            return originalName;
        }

        int suffix = 1;
        String baseName = FileUtil.mainName(originalName);
        String extension = FileUtil.extName(originalName);

        String candidateName;
        do {
            candidateName = extension.isEmpty()
                    ? baseName + "_" + suffix
                    : baseName + "_" + suffix + "." + extension;
            suffix++;
        } while (addedEntries.contains(candidateName));

        return candidateName;
    }


    /**
     * 返回流量器标签图标资源
     */
    @GetMapping("/open/{type}")
    public ResponseEntity<Resource> getOpenResource(@PathVariable("type") String type, HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
        // 校验type参数
        if ("favicon".equals(type)) {
            // 返回图标资源
            // 获取文件路径
            String fileId = sysConfigService.getByName("site.browser.favicon").getConfigValue();
            if (fileId == null) {
                return ResponseEntity.notFound().build();
            }
            FileInfo fileInfo = fileInfoService.getById(fileId);
            if (fileInfo == null) {
                return ResponseEntity.notFound().build();
            }

            // 获取文件路径
            String filePath = fileInfo.getPath();
            File file = new File(filePath);
            // 判断文件是否存在
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 构建文件资源
            Resource resource = new UrlResource(file.toURI());

            // 设置响应头信息
            String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }


            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header("Access-Control-Expose-Headers", "Content-Disposition")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" +
                            URLEncoder.encode(fileInfo.getOriginalFileName(), StandardCharsets.UTF_8.name()) + "\"")
                    .body(resource);
        }

        return ResponseEntity.notFound().build();
    }
}

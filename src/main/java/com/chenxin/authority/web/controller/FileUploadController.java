package com.chenxin.authority.web.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chenxin.authority.common.jackjson.Jackson;
import com.chenxin.authority.common.utils.FileDigest;
import com.chenxin.authority.pojo.ExtReturn;

@Controller
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@RequestMapping("/fileupload")
	public void processUpload2(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response,
			PrintWriter writer) {
		try {
			//文件的MD5
			logger.info("start");
			String fileMD5=FileDigest.getFileMD5(file.getInputStream());
			logger.info(fileMD5);
			// 保存的地址
			String savePath = request.getSession().getServletContext().getRealPath("/upload");
			// 上传的文件名 //需要保存
			String uploadFileName = file.getOriginalFilename();
			// 获取文件后缀名 //需要保存
			String fileType = StringUtils.substringAfterLast(uploadFileName, ".");
			logger.debug("文件的MD5：{},上传的文件名：{},文件后缀名：{},文件大小：{}",
					new Object[] {fileMD5, org.springframework.util.StringUtils.getFilenameExtension(uploadFileName), fileType, file.getSize() });
			// 以年月/天的格式来存放
			String dataPath = DateFormatUtils.format(new Date(), "yyyy-MM" + File.separator + "dd");
			// uuid来保存不会重复
			String saveName = UUID.randomUUID().toString();
			// 最终相对于upload的路径，解决没有后缀名的文件 //需要保存
			// 为了安全，不要加入后缀名
			// \2011-12\01\8364b45f-244d-41b6-bbf4-8df32064a935，等下载的的时候在加入后缀名
			String finalPath = File.separator + dataPath + File.separator + saveName + ("".equals(fileType) ? "" : "." + fileType);
			logger.debug("savePath:{},finalPath:{}", new Object[] { savePath, finalPath });
			File saveFile = new File(savePath + finalPath);
			// 判断文件夹是否存在，不存在则创建
			if (!saveFile.getParentFile().exists()) {
				saveFile.getParentFile().mkdirs();
			}
			// 写入文件
			FileUtils.writeByteArrayToFile(saveFile, file.getBytes());
			// 保存文件的基本信息到数据库
			// 上传的文件名（带不带后缀名？）；文件后缀名；存放的相对路径
			String returnMsg = Jackson.objToJson(new ExtReturn(true, "磁盘空间已经满了！"));
			logger.debug("{}", returnMsg);
			writer.print(returnMsg);
		} catch (Exception e) {
			logger.error("Exception: ", e);
		} finally {
			writer.flush();
			writer.close();
		}
	}

	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		InputStream input = null;
		ServletOutputStream output = null;
		try {
			// 保存的地址
			String savePath = request.getSession().getServletContext().getRealPath("/upload");
			// 保存到数据库的后缀名
			String fileType = ".log";
			// 保存到数据库的文件名
			String dbFileName = "83tomcat日志测试哦";
			String downloadFileName = dbFileName + fileType;
			// 保存到数据库的相对路径
			String finalPath = "\\2011-12\\01\\8364b45f-244d-41b6-bbf48df32064a935";
			// 下载的文件名,处理乱码
			downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");

			File downloadFile = new File(savePath + finalPath);
			// 判断文件夹是否存在，不存在则创建
			if (!downloadFile.getParentFile().exists()) {
				downloadFile.getParentFile().mkdirs();
			}
			// 判断是否存在这个文件
			if (!downloadFile.isFile()) {
				// 创建一个
				FileUtils.touch(downloadFile);
			}
			response.setContentType("application/vnd.ms-excel ;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-disposition", "attachment; filename=" + downloadFileName);
			input = new FileInputStream(downloadFile);
			output = response.getOutputStream();
			IOUtils.copy(input, output);
			output.flush();
		} catch (Exception e) {
			logger.error("Exception: ", e);
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(input);
		}
	}
}

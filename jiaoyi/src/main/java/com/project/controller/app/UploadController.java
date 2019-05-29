package com.project.controller.app;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.project.utils.ResultVOUtil;
import com.project.vo.ResultVO;

@Controller
public class UploadController {

	@Value("${path.filelocation}")
	private String filelocation;

	@RequestMapping(value = "/upload/uploadBase64", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO<?> base64UpLoad(@RequestParam String base64Data) {
		try {
			String dataPrix = "";
			String data = "";
			if (base64Data == null || "".equals(base64Data)) {
				throw new Exception("上传失败，上传图片数据为空");
			} else {
				String[] d = base64Data.split("base64,");
				if (d != null && d.length == 2) {
					dataPrix = d[0];
					data = d[1];
				} else {
					throw new Exception("上传失败，数据不合法");
				}
			}
			String suffix = "";
			if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {// data:image/jpeg;base64,base64编码的jpeg图片数据
				suffix = ".jpg";
			} else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {// data:image/x-icon;base64,base64编码的icon图片数据
				suffix = ".ico";
			} else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {// data:image/gif;base64,base64编码的gif图片数据
				suffix = ".gif";
			} else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {// data:image/png;base64,base64编码的png图片数据
				suffix = ".png";
			} else {
				throw new Exception("上传图片格式不合法");
			}
//			String tempFileName = UUID.randomUUID().toString() + suffix;
			String tempFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + suffix;
			System.out.println(tempFileName);
			// 因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
			byte[] bs = Base64Utils.decodeFromString(data);
			try {
				// 使用apache提供的工具类操作流
				FileUtils.writeByteArrayToFile(new File(filelocation, tempFileName), bs);
			} catch (Exception ee) {
				throw new Exception("上传失败，写入文件失败，" + ee.getMessage());
			}
			return ResultVOUtil.success(tempFileName);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultVOUtil.error(500, "系统异常");
		}
	}

	@PostMapping(value = "/fileUpload")
	@ResponseBody
	public ResultVO<?> fileUpload(@RequestParam(value = "file") MultipartFile file) {
		if (file.isEmpty()) {
			System.out.println("文件为空");
			return null;
		}
		String fileName = file.getOriginalFilename(); // 文件名
		String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 后缀名
		String tempFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + suffixName;
		File dest = new File(filelocation + tempFileName);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {
			file.transferTo(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResultVOUtil.success(tempFileName);
	}

	/*
	 * 上传照片
	 * 
	 * @return
	 * 
	 */

	@RequestMapping(value = "/uploadphoto", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO<?> uploadPhoto(HttpServletRequest request) {
		// 创建一个通用的多部分解析器.
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 设置编码
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		// 判断 request 是否有文件上传,即多部分请求..
		if (commonsMultipartResolver.isMultipart(request)) {
			try {
				MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request;
				List<MultipartFile> files = mulReq.getFiles("file");
				String fileName = files.get(0).getOriginalFilename(); // 文件名
				String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 后缀名
				String tempFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + suffixName;
				File dest = new File(filelocation + tempFileName);
				if (!dest.getParentFile().exists()) {
					dest.getParentFile().mkdirs();
				}
				try {
					files.get(0).transferTo(dest);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ResultVOUtil.success(tempFileName);
			} catch (Exception e) {
				e.printStackTrace();
				return ResultVOUtil.error("上传失败");
			}
		} else {
			return ResultVOUtil.error("上传照片不能为空");
		}
	}
}

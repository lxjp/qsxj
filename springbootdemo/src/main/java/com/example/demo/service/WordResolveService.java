package com.example.demo.service;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by hzlvxiaojia on 2017-12-28.
 */
@Service
public class WordResolveService {
    public static String readWord(String filePath){
        File file = new File(filePath);
        if(!file.getName().endsWith(".docx")) {//2007
            System.out.println("version error, please upload 2007 word file.");
            return "";
        }
        try {
            OPCPackage oPCPackage = POIXMLDocument.openPackage(filePath);
            XWPFDocument xwpf = new XWPFDocument(oPCPackage);
            List<XWPFParagraph> paragraphs = xwpf.getParagraphs();
            if (paragraphs == null) {
                System.out.println("file is empty.");
                return "";
            }
            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText().trim();
                if (!text.startsWith("[") && !text.startsWith("<")) {
                    System.out.println("file error.");
                    return "";
                }
                int index = text.indexOf("]") > 0 ? text.indexOf("]") : text.indexOf(">");
                if (index <= 0) {
                    System.out.println("file error.");
                    return "";
                }
                List<XWPFRun> runs = paragraph.getRuns();
                if (runs != null) {
                    for (XWPFRun run : runs) {
                        List<XWPFPicture> pics = run.getEmbeddedPictures();
                        if (pics == null || pics.size() <= 0) {
                            continue;
                        }
                        //图片替换为nos链接(多张图以逗号分隔)
                        String imgUrls = "";
                        for (XWPFPicture pic : pics) {
                            byte[] datas = pic.getPictureData().getData();
                            String fileName = pic.getPictureData().getFileName();
                            imgUrls = "D:"+ File.separator + fileName;
                            IOUtils.copy(new ByteArrayInputStream(datas), new FileOutputStream(new File(imgUrls)));
                            //上传nos
                        }
                        run.setText(imgUrls);
                    }
                }
                System.out.println(paragraph.getText());
            }
            System.out.println("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        readWord("C:\\Users\\hzlvxiaojia\\Desktop\\对话体小说内容导入.docx");
//        if(file.getName().endsWith(".doc")){
//            try {
//                FileInputStream stream = new FileInputStream(file);
//                WordExtractor word = new WordExtractor(stream);
//                text = word.getText();
//                stream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}

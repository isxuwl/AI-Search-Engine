package com.searchengine.springboot.segmentation;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.RecordSegDao;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.entity.Record;
import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import com.searchengine.service.RecordService;
import com.searchengine.service.SegmentationService;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 扫描data表把所有内容分词并加入分词库
 */
@SpringBootTest
public class addAllSeg {
    @Autowired
    private RecordService recordService;
    @Autowired
    private SegmentationService segmentationService;
    @Autowired
    private SegmentationDao segmentationDao;
    @Autowired
    private RecordSegDao recordSegDao;


    TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
    JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();

    @Test
    public void addAllSeg(){
        for (Record record : recordService.queryAllRecord()) {
            String sentence = record.getCaption();
            List<SegToken> segTokens = jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);

            Long recordId = record.getId();
            for (SegToken segToken : segTokens) {

                segmentationService.addSeg(segToken.word,recordId);
            }

        }

    }

//    /**
//     * 试试用hashset判断分词会不会快一点
//     * 分词存到2w2左右会报错
//     */
//    @Test
//    public void addAllSeg(){
//
//        HashSet<String> hs = new HashSet<>();
//        for (Record record : recordService.queryAllRecord()) {
//            String sentence = record.getCaption();
//            List<SegToken> segTokens = jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
//
//            Long recordId = record.getId();
//            for (SegToken segToken : segTokens) {
//                String word = segToken.word;
//                if (!hs.contains(word)){
//                    //加入分词表
//                    segmentationDao.insertSeg(word);
//                    hs.add(segToken.word);
//                }
//                Long segId = segmentationDao.selectOneSeg(word).getId();
//                //加入关系表
//                RecordSeg recordSeg = new RecordSeg();
//                recordSeg.setSegId(segId);
//                recordSeg.setDataId(recordId);
//                if (recordSegDao.selectOneRecordSeg(recordId,segId)==null) {
//                    recordSeg.setCount(1);
//                    recordSegDao.insertRecordSeg(recordSeg);
//                }
//                else {
//                    //文中出现次数>1
//                    recordSeg.setCount(2);
//                    recordSegDao.updateRecordSeg(recordSeg);
//                }
//
//            }
//
//        }
//
//    }
}

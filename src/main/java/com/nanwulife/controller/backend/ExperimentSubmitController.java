package com.nanwulife.controller.backend;

import com.nanwulife.common.Const;
import com.nanwulife.common.ServerResponse;
import com.nanwulife.experimentRank.PhotoeletricExperiment;
import com.nanwulife.pojo.Score;
import com.nanwulife.pojo.User;
import com.nanwulife.service.IExperimentService;
import com.nanwulife.service.IScoreService;
import com.nanwulife.util.WordToNewWordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sub")
public class ExperimentSubmitController {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentManageController.class);

    @Autowired
    IExperimentService iExperimentService;

    @Autowired
    IScoreService iScoreService;
    
    /**
     * 提交广电实验答案
     * @param selectval
     * @return
     */
    //TODO:未完成
    @RequestMapping(value = "Exp_01.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse submitExp(@RequestParam(value = "selectval[]") String[] selectval, HttpSession session, 
                                    @RequestParam(value = "chart1[]") Integer[] chart1, @RequestParam(value = "table2[]") Integer[] table2,
                                    @RequestParam(value = "table3[]") Integer[] table3, @RequestParam(value = "table4[]") Integer[] table4){
        int rank;
        Map<String, Object> params = new HashMap<String, Object>();
        for (int i = 0; i < 11; i++) {
            if (i + 1 <= 9)
                params.put("choice_" + "0" + (i+1) + "", selectval[i]);
            else
                params.put("choice_" + (i+1), selectval[i]);
        }
        
        for (int i = 0; i < 5; i++) {
            params.put("blank_01_0" + (i+1) + "", chart1[i]);
        }

        for (int i = 0; i < 22; i++) {
            if (i + 1 <= 9)
                params.put("blank_03_" + "0" + (i+1) + "", table2[i]);
            else
                params.put("blank_03_" + (i+1), table2[i]);
        }

        for (int i = 0; i < 22; i++) {
            if (i + 1 <= 9)
                params.put("blank_04_" + "0" + (i+1) + "", table3[i]);
            else
                params.put("blank_04_" + (i+1), table3[i]);
        }

        for (int i = 0; i < 22; i++) {
            if (i + 1 <= 9)
                params.put("blank_05_" + "0" + (i+1) + "", table4[i]);
            else
                params.put("blank_05_" + (i+1), table4[i]);
        }
        
        
        try {
            WordToNewWordUtil.templateWrite2("/home/creams/桌面/光电效应实验模板.docx", params, "/home/creams/桌面/test1.docx");
        } catch (Exception e) {
            System.out.println("写入模板异常");
            e.printStackTrace();
        }
        rank = new PhotoeletricExperiment(selectval[0], selectval[1], selectval[2], selectval[3], selectval[4], selectval[5],
                selectval[6], selectval[7], selectval[8], selectval[9], selectval[10], 0.45).getRank();
        Score score = new Score();
        score.setStuId(((User)session.getAttribute(Const.CURRENT_USER)).getStuNum());
        score.setExpId(1);
        score.setScore(rank);
        return iScoreService.submit(score);
    }
}
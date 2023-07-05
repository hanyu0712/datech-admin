package com.datech.admin.controller;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 销售回款统计
 */
@Controller
public class SalesStatisticsController {


    @GetMapping("/salesStatistics")
    public String getList(HttpSession session, Model model) {
        model.addAttribute("dataList", getData());
        return "sales_statistics";
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> infoMaps = SqlRunner.db().selectList("SELECT id,contract_no,customer_name,project_name,sales_subject,sales_person,contract_amount,sign_date,tax_rate,business_category,product_category,pay_mode,quality_guarantee_ratio,delivery_month,receiving_month,logistics_no,rebate_amount,invoice_amount,create_user_id,create_user_name,company,audit_user_id,audit_user_name,create_time,status,rejection_content FROM sales_info WHERE `status`=2 order by contract_no desc");
        List<Map<String, Object>> statusMaps = SqlRunner.db().selectList("SELECT id,contract_no,customer_name,project_name,contract_status,receiving_time,receiving,acceptance,final_acceptance,shelf_life,shelf_life_start,shelf_life_end,create_user_id,create_user_name,company,audit_user_id,audit_user_name,create_time,status,rejection_content FROM sales_status WHERE `status`=2");
        //累计回款
        List<Map<String, Object>> collectionTotal = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 GROUP BY contract_no");
        //本年度回款
        List<Map<String, Object>> collectionYear = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-01-01') and collection_date < CONCAT(YEAR(CURDATE())+1,'-01-01')  GROUP BY contract_no");
        //本年度1月回款
//        List<Map<String, Object>> collection1 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-01-01') and collection_date < CONCAT(YEAR(CURDATE()),'-02-01') GROUP BY contract_no");
        //本年度2月回款
//        List<Map<String, Object>> collection2 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-02-01') and collection_date < CONCAT(YEAR(CURDATE()),'-03-01') GROUP BY contract_no");
        //本年度3月回款
//        List<Map<String, Object>> collection3 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-03-01') and collection_date < CONCAT(YEAR(CURDATE()),'-04-01') GROUP BY contract_no");
        //本年度4月回款
//        List<Map<String, Object>> collection4 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-04-01') and collection_date < CONCAT(YEAR(CURDATE()),'-05-01') GROUP BY contract_no");
        //本年度5月回款
//        List<Map<String, Object>> collection5 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-05-01') and collection_date < CONCAT(YEAR(CURDATE()),'-06-01') GROUP BY contract_no");
        //本年度6月回款
//        List<Map<String, Object>> collection6 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-06-01') and collection_date < CONCAT(YEAR(CURDATE()),'-07-01') GROUP BY contract_no");
        //本年度7月回款
//        List<Map<String, Object>> collection7 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-07-01') and collection_date < CONCAT(YEAR(CURDATE()),'-08-01') GROUP BY contract_no");
        //本年度8月回款
//        List<Map<String, Object>> collection8 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-08-01') and collection_date < CONCAT(YEAR(CURDATE()),'-09-01') GROUP BY contract_no");
        //本年度9月回款
//        List<Map<String, Object>> collection9 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-09-01') and collection_date < CONCAT(YEAR(CURDATE()),'-10-01') GROUP BY contract_no");
        //本年度10月回款
//        List<Map<String, Object>> collection10 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-10-01') and collection_date < CONCAT(YEAR(CURDATE()),'-11-01') GROUP BY contract_no");
        //本年度11月回款
//        List<Map<String, Object>> collection11 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-11-01') and collection_date < CONCAT(YEAR(CURDATE()),'-12-01') GROUP BY contract_no");
        //本年度12月回款
//        List<Map<String, Object>> collection12 = SqlRunner.db().selectList("SELECT contract_no, SUM(collection_amount) amount FROM sales_collection where `status`=2 AND collection_date >= CONCAT(YEAR(CURDATE()),'-12-01') and collection_date < CONCAT(YEAR(CURDATE())+1,'-01-01') GROUP BY contract_no");
        //本年度开票
        List<Map<String, Object>> invoicedYear = SqlRunner.db().selectList("SELECT contract_no, SUM(no_tax_amount+tax_amount) amount FROM sales_income_confirm where `status`=2 AND invoice_date >= CONCAT(YEAR(CURDATE()),'-01-01') and invoice_date < CONCAT(YEAR(CURDATE())+1,'-01-01') GROUP BY contract_no");
        //累计开票
        List<Map<String, Object>> invoicedTotal = SqlRunner.db().selectList("SELECT contract_no, SUM(no_tax_amount+tax_amount) amount FROM sales_income_confirm where `status`=2 GROUP BY contract_no");

        for (Map<String, Object> info : infoMaps) {
            //合同状态
            for (Map<String, Object> status : statusMaps) {
                if (info.get("contract_no").toString().trim().equals(status.get("contract_no").toString().trim())) {
                    info.putAll(status);
                    break;
                }
            }

            //本年度回款
            for (Map<String, Object> collection : collectionYear) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collectionYear", collection.get("amount"));
                    break;
                }
            }
            /*for (Map<String, Object> collection : collection1) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection1", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection2) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection2", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection3) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection3", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection4) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection4", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection5) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection5", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection6) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection6", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection7) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection7", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection8) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection8", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection9) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection9", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection10) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection10", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection11) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection11", collection.get("amount"));
                    break;
                }
            }
            for (Map<String, Object> collection : collection12) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("collection12", collection.get("amount"));
                    break;
                }
            }*/
            //累计回款
            BigDecimal initRebateAmount = info.get("rebate_amount") != null ? (BigDecimal) info.get("rebate_amount") : BigDecimal.ZERO;
            info.put("collectionTotal", initRebateAmount);
            for (Map<String, Object> collection : collectionTotal) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    BigDecimal amount = collection.get("amount") != null ? (BigDecimal) collection.get("amount") : BigDecimal.ZERO;
                    info.put("collectionTotal", initRebateAmount.add(amount));
                    break;
                }
            }
            //累计开票
            BigDecimal initInvoiceAmount = info.get("invoice_amount") != null ? (BigDecimal) info.get("invoice_amount") : BigDecimal.ZERO;
            info.put("invoicedTotal", initInvoiceAmount);
            for (Map<String, Object> invoiced : invoicedTotal) {
                if (info.get("contract_no").toString().trim().equals(invoiced.get("contract_no").toString().trim())) {
                    BigDecimal amount = invoiced.get("amount") != null ? (BigDecimal) invoiced.get("amount") : BigDecimal.ZERO;
                    info.put("invoicedTotal", initInvoiceAmount.add(amount));
                    break;
                }
            }
            //本年度开票
            for (Map<String, Object> invoiced : invoicedYear) {
                if (info.get("contract_no").toString().trim().equals(invoiced.get("contract_no").toString().trim())) {
                    info.put("invoicedYear", invoiced.get("amount"));
                    break;
                }
            }
        }
        for (Map<String, Object> info : infoMaps) {
            //合同金额
            BigDecimal contractAmount = (BigDecimal) info.get("contract_amount");
            //累计回款
            BigDecimal collectionTotalAmount = (BigDecimal) info.get("collectionTotal");
            //合同欠款 = 合同金额 - 累计回款
            BigDecimal debt = contractAmount.subtract(collectionTotalAmount);
            info.put("debt", debt);

            //累计开票
            BigDecimal invoicedTotalAmount = (BigDecimal) info.get("invoicedTotal");
            //未开票 = 合同金额 - 累计开票
            BigDecimal unInvoiced = contractAmount.subtract(invoicedTotalAmount);
            info.put("unInvoiced", unInvoiced);

            //应收账款 = 累计开票 - 累计回款
            BigDecimal receivable = invoicedTotalAmount.subtract(collectionTotalAmount);
            info.put("receivable", receivable);
        }
        return infoMaps;
    }


    // 导出成excel
    @RequestMapping("/salesStatisticsExport")
    public void exportCarByExcel(HttpServletResponse response) {
        HSSFWorkbook workbook = exportExcel();
        // 获取输出流
        OutputStream os = null;
        try {
            // 获取输出流
            os = response.getOutputStream();
            // 重置输出流
            response.reset();
            // 设定输出文件头
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String("销售回款总统计表".getBytes("GB2312"), "8859_1") + ".xls");
            // 定义输出类型
            response.setContentType("application/msexcel");
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                assert os != null;
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HSSFWorkbook exportExcel() {
        // 创建Execl工作薄
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 在Excel工作簿中建一工作表
        HSSFSheet sheet = hssfWorkbook.createSheet("car");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue(new HSSFRichTextString("合同号"));
        row.createCell(1).setCellValue(new HSSFRichTextString("客户名称"));
        row.createCell(2).setCellValue(new HSSFRichTextString("项目名称"));
        row.createCell(3).setCellValue(new HSSFRichTextString("销售负责人"));
        row.createCell(4).setCellValue(new HSSFRichTextString("签订时间"));
        row.createCell(5).setCellValue(new HSSFRichTextString("税率"));
        row.createCell(6).setCellValue(new HSSFRichTextString("业务类别"));
        row.createCell(7).setCellValue(new HSSFRichTextString("产品类别"));
        row.createCell(8).setCellValue(new HSSFRichTextString("付款方式"));
        row.createCell(9).setCellValue(new HSSFRichTextString("质保金比例"));
        row.createCell(10).setCellValue(new HSSFRichTextString("预计发货月份"));
        row.createCell(11).setCellValue(new HSSFRichTextString("预计到货签收月份"));
        row.createCell(12).setCellValue(new HSSFRichTextString("物流单号"));
        row.createCell(13).setCellValue(new HSSFRichTextString("合同执行状态"));
        row.createCell(14).setCellValue(new HSSFRichTextString("到货时间"));
        row.createCell(15).setCellValue(new HSSFRichTextString("签收单"));
        row.createCell(16).setCellValue(new HSSFRichTextString("验收单"));
        row.createCell(17).setCellValue(new HSSFRichTextString("终验单"));
        row.createCell(18).setCellValue(new HSSFRichTextString("质保期限"));
        row.createCell(19).setCellValue(new HSSFRichTextString("质保开始日"));
        row.createCell(20).setCellValue(new HSSFRichTextString("质保到期日"));
        row.createCell(21).setCellValue(new HSSFRichTextString("合同金额"));
        row.createCell(22).setCellValue(new HSSFRichTextString("以前年度回款"));
        /*row.createCell(23).setCellValue(new HSSFRichTextString("1月回款"));
        row.createCell(24).setCellValue(new HSSFRichTextString("2月回款"));
        row.createCell(25).setCellValue(new HSSFRichTextString("3月回款"));
        row.createCell(26).setCellValue(new HSSFRichTextString("4月回款"));
        row.createCell(27).setCellValue(new HSSFRichTextString("5月回款"));
        row.createCell(28).setCellValue(new HSSFRichTextString("6月回款"));
        row.createCell(29).setCellValue(new HSSFRichTextString("7月回款"));
        row.createCell(30).setCellValue(new HSSFRichTextString("8月回款"));
        row.createCell(31).setCellValue(new HSSFRichTextString("9月回款"));
        row.createCell(32).setCellValue(new HSSFRichTextString("10月回款"));
        row.createCell(33).setCellValue(new HSSFRichTextString("11月回款"));
        row.createCell(34).setCellValue(new HSSFRichTextString("12月回款"));*/
        row.createCell(23).setCellValue(new HSSFRichTextString("当年回款"));
        row.createCell(24).setCellValue(new HSSFRichTextString("累计回款"));
        row.createCell(25).setCellValue(new HSSFRichTextString("合同欠款"));
        row.createCell(26).setCellValue(new HSSFRichTextString("以前年度开票"));
        row.createCell(27).setCellValue(new HSSFRichTextString("本年度开票"));
        row.createCell(28).setCellValue(new HSSFRichTextString("累计开票"));
        row.createCell(29).setCellValue(new HSSFRichTextString("未开票"));
        row.createCell(30).setCellValue(new HSSFRichTextString("应收账款"));

        List<Map<String, Object>> infoMaps = getData();
        Iterator<Map<String, Object>> iterator = infoMaps.iterator();
        int num = 1;
        while (iterator.hasNext()) {
            Map<String, Object> info = iterator.next();
            HSSFRow rowNum = sheet.createRow(num);
            rowNum.createCell(0).setCellValue(new HSSFRichTextString(info.get("contract_no") != null ? info.get("contract_no").toString() : ""));
            rowNum.createCell(1).setCellValue(new HSSFRichTextString(info.get("customer_name") != null ? info.get("customer_name").toString() : ""));
            rowNum.createCell(2).setCellValue(new HSSFRichTextString(info.get("project_name") != null ? info.get("project_name").toString() : ""));
            rowNum.createCell(3).setCellValue(new HSSFRichTextString(info.get("sales_person") != null ? info.get("sales_person").toString() : ""));
            rowNum.createCell(4).setCellValue(new HSSFRichTextString(info.get("sign_date") != null ? info.get("sign_date").toString() : ""));
            rowNum.createCell(5).setCellValue(new HSSFRichTextString(info.get("tax_rate") != null ? info.get("tax_rate").toString() : ""));
            rowNum.createCell(6).setCellValue(new HSSFRichTextString(info.get("business_category") != null ? info.get("business_category").toString() : ""));
            rowNum.createCell(7).setCellValue(new HSSFRichTextString(info.get("product_category") != null ? info.get("product_category").toString() : ""));
            rowNum.createCell(8).setCellValue(new HSSFRichTextString(info.get("pay_mode") != null ? info.get("pay_mode").toString() : ""));
            rowNum.createCell(9).setCellValue(new HSSFRichTextString(info.get("quality_guarantee_ratio") != null ? info.get("quality_guarantee_ratio").toString() : ""));
            rowNum.createCell(10).setCellValue(new HSSFRichTextString(info.get("delivery_month") != null ? info.get("delivery_month").toString() : ""));
            rowNum.createCell(11).setCellValue(new HSSFRichTextString(info.get("receiving_month") != null ? info.get("receiving_month").toString() : ""));
            rowNum.createCell(12).setCellValue(new HSSFRichTextString(info.get("logistics_no") != null ? info.get("logistics_no").toString() : ""));
            rowNum.createCell(13).setCellValue(new HSSFRichTextString(info.get("contract_status") != null ? info.get("contract_status").toString() : ""));
            rowNum.createCell(14).setCellValue(new HSSFRichTextString(info.get("receiving_time") != null ? info.get("receiving_time").toString() : ""));
            rowNum.createCell(15).setCellValue(new HSSFRichTextString(info.get("receiving") != null ? info.get("receiving").toString() : ""));
            rowNum.createCell(16).setCellValue(new HSSFRichTextString(info.get("acceptance") != null ? info.get("acceptance").toString() : ""));
            rowNum.createCell(17).setCellValue(new HSSFRichTextString(info.get("final_acceptance") != null ? info.get("final_acceptance").toString() : ""));
            rowNum.createCell(18).setCellValue(new HSSFRichTextString(info.get("shelf_life") != null ? info.get("shelf_life").toString() : ""));
            rowNum.createCell(19).setCellValue(new HSSFRichTextString(info.get("shelf_life_start") != null ? info.get("shelf_life_start").toString() : ""));
            rowNum.createCell(20).setCellValue(new HSSFRichTextString(info.get("shelf_life_end") != null ? info.get("shelf_life_end").toString() : ""));
            rowNum.createCell(21).setCellValue(new HSSFRichTextString(info.get("contract_amount") != null ? info.get("contract_amount").toString() : "0"));
            rowNum.createCell(22).setCellValue(new HSSFRichTextString(info.get("rebate_amount") != null ? info.get("rebate_amount").toString() : "0"));
            /*rowNum.createCell(23).setCellValue(new HSSFRichTextString(info.get("collection1") != null ? info.get("collection1").toString() : "0"));
            rowNum.createCell(24).setCellValue(new HSSFRichTextString(info.get("collection2") != null ? info.get("collection2").toString() : "0"));
            rowNum.createCell(25).setCellValue(new HSSFRichTextString(info.get("collection3") != null ? info.get("collection3").toString() : "0"));
            rowNum.createCell(26).setCellValue(new HSSFRichTextString(info.get("collection4") != null ? info.get("collection4").toString() : "0"));
            rowNum.createCell(27).setCellValue(new HSSFRichTextString(info.get("collection5") != null ? info.get("collection5").toString() : "0"));
            rowNum.createCell(28).setCellValue(new HSSFRichTextString(info.get("collection6") != null ? info.get("collection6").toString() : "0"));
            rowNum.createCell(29).setCellValue(new HSSFRichTextString(info.get("collection7") != null ? info.get("collection7").toString() : "0"));
            rowNum.createCell(30).setCellValue(new HSSFRichTextString(info.get("collection8") != null ? info.get("collection8").toString() : "0"));
            rowNum.createCell(31).setCellValue(new HSSFRichTextString(info.get("collection9") != null ? info.get("collection9").toString() : "0"));
            rowNum.createCell(32).setCellValue(new HSSFRichTextString(info.get("collection10") != null ? info.get("collection10").toString() : "0"));
            rowNum.createCell(33).setCellValue(new HSSFRichTextString(info.get("collection11") != null ? info.get("collection11").toString() : "0"));
            rowNum.createCell(34).setCellValue(new HSSFRichTextString(info.get("collection12") != null ? info.get("collection12").toString() : "0"));*/
            rowNum.createCell(23).setCellValue(new HSSFRichTextString(info.get("collectionYear") != null ? info.get("collectionYear").toString() : "0"));
            rowNum.createCell(24).setCellValue(new HSSFRichTextString(info.get("collectionTotal") != null ? info.get("collectionTotal").toString() : "0"));
            rowNum.createCell(25).setCellValue(new HSSFRichTextString(info.get("debt") != null ? info.get("debt").toString() : "0"));
            rowNum.createCell(26).setCellValue(new HSSFRichTextString(info.get("invoice_amount") != null ? info.get("invoice_amount").toString() : "0"));
            rowNum.createCell(27).setCellValue(new HSSFRichTextString(info.get("invoicedYear") != null ? info.get("invoicedYear").toString() : "0"));
            rowNum.createCell(28).setCellValue(new HSSFRichTextString(info.get("invoicedTotal") != null ? info.get("invoicedTotal").toString() : "0"));
            rowNum.createCell(29).setCellValue(new HSSFRichTextString(info.get("unInvoiced") != null ? info.get("unInvoiced").toString() : "0"));
            rowNum.createCell(30).setCellValue(new HSSFRichTextString(info.get("receivable") != null ? info.get("receivable").toString() : "0"));

            num++;
        }
        return hssfWorkbook;
    }


}

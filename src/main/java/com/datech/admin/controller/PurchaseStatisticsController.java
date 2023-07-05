package com.datech.admin.controller;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import lombok.extern.slf4j.Slf4j;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 采购合同统计表
 */
@Slf4j
@Controller
public class PurchaseStatisticsController {

    @GetMapping("/purchaseStatistics")
    public String getList(HttpSession session, Model model) {
        model.addAttribute("dataList", getData());
        return "purchase_statistics";
    }

    private List<Map<String, Object>> getData() {
        String sql = "SELECT id,sales_contract_no,contract_no,supplier_name,project_name,sign_subject,sign_date,contract_amount,budget_amount,purchase_category,purchase_material,pay_mode,receiving_date,payment_date,is_all,invoice_type,tax_rate,init_invoice_amount,init_payment_amount,huawei_amount,create_user_id,create_user_name,company,audit_user_id,audit_user_name,create_time,status,rejection_content FROM purchase_info  order by sales_contract_no desc, contract_no desc ";

        List<Map<String, Object>> infoMaps = SqlRunner.db().selectList(sql);

        //累计已收票
        List<Map<String, Object>> accumulateInvoiceList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(invoice_amount,0)) amount from purchase_invoice where `status`=2 GROUP BY contract_no");

        //本年度已收票
        List<Map<String, Object>> yearInvoiceList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(invoice_amount,0)) amount from purchase_invoice where `status`=2 AND invoice_date >= CONCAT(YEAR(CURDATE()),'-01-01') and invoice_date < CONCAT(YEAR(CURDATE())+1,'-01-01') GROUP BY contract_no");

        //累计已付款
        List<Map<String, Object>> accumulatePaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 GROUP BY contract_no");

        //本年度已付款
        List<Map<String, Object>> yearAmountList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-01-01') and payment_date < CONCAT(YEAR(CURDATE())+1,'-01-01') GROUP BY contract_no");

        //本年度1月已付款
//        List<Map<String, Object>> month1PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-01-01') and payment_date < CONCAT(YEAR(CURDATE()),'-02-01') GROUP BY contract_no");

        //本年度2月已付款
//        List<Map<String, Object>> month2PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-02-01') and payment_date < CONCAT(YEAR(CURDATE()),'-03-01') GROUP BY contract_no");

        //本年度3月已付款
//        List<Map<String, Object>> month3PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-03-01') and payment_date < CONCAT(YEAR(CURDATE()),'-04-01') GROUP BY contract_no");

        //本年度4月已付款
//        List<Map<String, Object>> month4PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-04-01') and payment_date < CONCAT(YEAR(CURDATE()),'-05-01') GROUP BY contract_no");

        //本年度5月已付款
//        List<Map<String, Object>> month5PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-05-01') and payment_date < CONCAT(YEAR(CURDATE()),'-06-01') GROUP BY contract_no");

        //本年度6月已付款
//        List<Map<String, Object>> month6PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-06-01') and payment_date < CONCAT(YEAR(CURDATE()),'-07-01') GROUP BY contract_no");

        //本年度7月已付款
//        List<Map<String, Object>> month7PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-07-01') and payment_date < CONCAT(YEAR(CURDATE()),'-08-01') GROUP BY contract_no");

        //本年度8月已付款
//        List<Map<String, Object>> month8PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-08-01') and payment_date < CONCAT(YEAR(CURDATE()),'-09-01') GROUP BY contract_no");

        //本年度9月已付款
//        List<Map<String, Object>> month9PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-09-01') and payment_date < CONCAT(YEAR(CURDATE()),'-10-01') GROUP BY contract_no");

        //本年度10月已付款
//        List<Map<String, Object>> month10PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-10-01') and payment_date < CONCAT(YEAR(CURDATE()),'-11-01') GROUP BY contract_no");

        //本年度11月已付款
//        List<Map<String, Object>> month11PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-11-01') and payment_date < CONCAT(YEAR(CURDATE()),'-12-01') GROUP BY contract_no");

        //本年度12月已付款
//        List<Map<String, Object>> month12PaymentList = SqlRunner.db().selectList("select contract_no, SUM(IFNULL(payment_amount,0)) amount from purchase_payment where `status`=2 AND payment_date >= CONCAT(YEAR(CURDATE()),'-12-01') and payment_date < CONCAT(YEAR(CURDATE())+1,'-01-01') GROUP BY contract_no");

        for (Map<String, Object> info : infoMaps) {

            //累计年度已收票
            info.put("accumulateInvoice", BigDecimal.ZERO);
            for (Map<String, Object> collection : accumulateInvoiceList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("accumulateInvoice", collection.get("amount"));
                    break;
                }
            }
            //本年度已收票
            info.put("yearInvoice", BigDecimal.ZERO);
            for (Map<String, Object> collection : yearInvoiceList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("yearInvoice", collection.get("amount"));
                    break;
                }
            }
            //累计年度已付款
            info.put("accumulatePayment", BigDecimal.ZERO);
            for (Map<String, Object> collection : accumulatePaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("accumulatePayment", collection.get("amount"));
                    break;
                }
            }
            //本年度已付款
            info.put("yearAmount", BigDecimal.ZERO);
            for (Map<String, Object> collection : yearAmountList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("yearAmount", collection.get("amount"));
                    break;
                }
            }
            //本年度1--12月已付款
            /*info.put("month1Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month1PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month1Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month2Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month2PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month2Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month3Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month3PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month3Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month4Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month4PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month4Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month5Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month5PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month5Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month6Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month6PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month6Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month7Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month7PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month7Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month8Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month8PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month8Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month9Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month9PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month9Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month10Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month10PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month10Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month11Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month11PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month11Payment", collection.get("amount"));
                    break;
                }
            }
            info.put("month12Payment", BigDecimal.ZERO);
            for (Map<String, Object> collection : month12PaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("contract_no").toString().trim())) {
                    info.put("month12Payment", collection.get("amount"));
                    break;
                }
            }*/
        }
        for (Map<String, Object> info : infoMaps) {

            info.put("audit_user_id", info.get("audit_user_id") != null ? info.get("audit_user_id") : " ");

//            BigDecimal season1 = ((BigDecimal) info.get("month1Payment")).add((BigDecimal) info.get("month2Payment")).add((BigDecimal) info.get("month3Payment"));
//            info.put("season1", season1);
//            BigDecimal season2 = ((BigDecimal) info.get("month4Payment")).add((BigDecimal) info.get("month5Payment")).add((BigDecimal) info.get("month6Payment"));
//            info.put("season2", season2);
//            BigDecimal season3 = ((BigDecimal) info.get("month7Payment")).add((BigDecimal) info.get("month8Payment")).add((BigDecimal) info.get("month9Payment"));
//            info.put("season3", season3);
//            BigDecimal season4 = ((BigDecimal) info.get("month10Payment")).add((BigDecimal) info.get("month11Payment")).add((BigDecimal) info.get("month12Payment"));
//            info.put("season4", season4);
            //采购合同金额
            BigDecimal contract_amount = info.get("contract_amount") != null ? (BigDecimal) info.get("contract_amount") : BigDecimal.ZERO;
            //累计收票 = 以前年度已开票 + 累计已开票
            BigDecimal initInvoice = info.get("init_invoice_amount") != null ? (BigDecimal) info.get("init_invoice_amount") : BigDecimal.ZERO;
            BigDecimal accumulateInvoice = (BigDecimal) info.get("accumulateInvoice");
            accumulateInvoice = initInvoice.add(accumulateInvoice);
            info.put("accumulateInvoice", accumulateInvoice);

            //欠票 = 采购合同金额 - 累计收票
            BigDecimal unInvoiced = contract_amount.subtract(accumulateInvoice);
            info.put("unInvoice", unInvoiced);


            //累计已付款 = 以前年度已付款 + 累计已付款 + 华为激励
            BigDecimal initPayment = info.get("init_payment_amount") != null ? (BigDecimal) info.get("init_payment_amount") : BigDecimal.ZERO;
            BigDecimal huawei = info.get("huawei_amount") != null ? (BigDecimal) info.get("huawei_amount") : BigDecimal.ZERO;
            BigDecimal accumulatePayment = (BigDecimal) info.get("accumulatePayment");
            accumulatePayment = initPayment.add(accumulatePayment).add(huawei);
            info.put("accumulatePayment", accumulatePayment);

            //合同欠款 = 采购合同金额 - 累计已付款
            BigDecimal arrears = contract_amount.subtract(accumulatePayment);
            info.put("arrears", arrears);

            //未付款金额 = 累计收票 - 累计已付款
            BigDecimal unPayment = accumulateInvoice.subtract(accumulatePayment);
            info.put("unPayment", unPayment);
        }
        return infoMaps;
    }

    // 导出成excel
    @RequestMapping("/purchaseStatisticsExport")
    public void purchaseStatisticsExport(HttpServletResponse response) {
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
                    "attachment; filename=" + new String("采购合同总统计信息".getBytes("GB2312"), "8859_1") + ".xls");
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
        row.createCell(0).setCellValue(new HSSFRichTextString("主合同号"));
        row.createCell(1).setCellValue(new HSSFRichTextString("项目名称"));
        row.createCell(2).setCellValue(new HSSFRichTextString("所属区域"));
        row.createCell(3).setCellValue(new HSSFRichTextString("采购合同号"));
        row.createCell(4).setCellValue(new HSSFRichTextString("供应商名称"));
        row.createCell(5).setCellValue(new HSSFRichTextString("签订合同主体"));
        row.createCell(6).setCellValue(new HSSFRichTextString("签订时间"));
        row.createCell(7).setCellValue(new HSSFRichTextString("采购类别"));
        row.createCell(8).setCellValue(new HSSFRichTextString("采购物料名称"));
        row.createCell(9).setCellValue(new HSSFRichTextString("单项合同预算金额"));
        row.createCell(10).setCellValue(new HSSFRichTextString("采购合同金额"));
        row.createCell(11).setCellValue(new HSSFRichTextString("账期/付款方式"));
        row.createCell(12).setCellValue(new HSSFRichTextString("到货/服务日期"));
        row.createCell(13).setCellValue(new HSSFRichTextString("到货/付款日期"));
        row.createCell(14).setCellValue(new HSSFRichTextString("除发票外单据是否齐全"));
        row.createCell(15).setCellValue(new HSSFRichTextString("发票类别"));
        row.createCell(16).setCellValue(new HSSFRichTextString("税率"));
        row.createCell(17).setCellValue(new HSSFRichTextString("累计收票"));
        row.createCell(18).setCellValue(new HSSFRichTextString("以前年度已收票"));
        row.createCell(19).setCellValue(new HSSFRichTextString("本年收票"));
        row.createCell(20).setCellValue(new HSSFRichTextString("欠票"));
        row.createCell(21).setCellValue(new HSSFRichTextString("累计已付款"));
        row.createCell(22).setCellValue(new HSSFRichTextString("以前年度已付款"));
        row.createCell(23).setCellValue(new HSSFRichTextString("华为激励"));
        row.createCell(24).setCellValue(new HSSFRichTextString("本年度已付款"));
        row.createCell(25).setCellValue(new HSSFRichTextString("合同欠款"));
        row.createCell(26).setCellValue(new HSSFRichTextString("未付款金额"));
        List<Map<String, Object>> infoMaps = getData();
        Iterator<Map<String, Object>> iterator = infoMaps.iterator();
        int num = 1;
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd");
        while (iterator.hasNext()) {
            Map<String, Object> info = iterator.next();
            HSSFRow rowNum = sheet.createRow(num);
            rowNum.createCell(0).setCellValue(new HSSFRichTextString(info.get("sales_contract_no") != null ? info.get("sales_contract_no").toString() : ""));
            rowNum.createCell(1).setCellValue(new HSSFRichTextString(info.get("project_name") != null ? info.get("project_name").toString() : ""));
            rowNum.createCell(2).setCellValue(new HSSFRichTextString(info.get("company") != null ? info.get("company").toString() : ""));
            rowNum.createCell(3).setCellValue(new HSSFRichTextString(info.get("contract_no") != null ? info.get("contract_no").toString() : ""));
            rowNum.createCell(4).setCellValue(new HSSFRichTextString(info.get("supplier_name") != null ? info.get("supplier_name").toString() : ""));
            rowNum.createCell(5).setCellValue(new HSSFRichTextString(info.get("sign_subject") != null ? info.get("sign_subject").toString() : ""));
            rowNum.createCell(6).setCellValue(new HSSFRichTextString(info.get("sign_date") != null ? dateTimeFormatter.format((Date) info.get("sign_date")) : ""));
            rowNum.createCell(7).setCellValue(new HSSFRichTextString(info.get("purchase_category") != null ? info.get("purchase_category").toString() : ""));
            rowNum.createCell(8).setCellValue(new HSSFRichTextString(info.get("purchase_material") != null ? info.get("purchase_material").toString() : ""));
            rowNum.createCell(9).setCellValue(new HSSFRichTextString(info.get("budget_amount") != null ? info.get("budget_amount").toString() : "0"));
            rowNum.createCell(10).setCellValue(new HSSFRichTextString(info.get("contract_amount") != null ? info.get("contract_amount").toString() : "0"));
            rowNum.createCell(11).setCellValue(new HSSFRichTextString(info.get("pay_mode") != null ? info.get("pay_mode").toString() : ""));
            rowNum.createCell(12).setCellValue(new HSSFRichTextString(info.get("receiving_date") != null ? info.get("receiving_date").toString() : ""));
            rowNum.createCell(13).setCellValue(new HSSFRichTextString(info.get("payment_date") != null ? info.get("payment_date").toString() : ""));
            rowNum.createCell(14).setCellValue(new HSSFRichTextString(info.get("is_all") != null ? info.get("is_all").toString() : ""));
            rowNum.createCell(15).setCellValue(new HSSFRichTextString(info.get("invoice_type") != null ? info.get("invoice_type").toString() : ""));
            rowNum.createCell(16).setCellValue(new HSSFRichTextString(info.get("tax_rate") != null ? info.get("tax_rate").toString() : ""));
            rowNum.createCell(17).setCellValue(new HSSFRichTextString(info.get("accumulateInvoice") != null ? info.get("accumulateInvoice").toString() : "0"));
            rowNum.createCell(18).setCellValue(new HSSFRichTextString(info.get("init_invoice_amount") != null ? info.get("init_invoice_amount").toString() : "0"));
            rowNum.createCell(19).setCellValue(new HSSFRichTextString(info.get("yearInvoice") != null ? info.get("yearInvoice").toString() : "0"));
            rowNum.createCell(20).setCellValue(new HSSFRichTextString(info.get("unInvoice") != null ? info.get("unInvoice").toString() : "0"));
            rowNum.createCell(21).setCellValue(new HSSFRichTextString(info.get("accumulatePayment") != null ? info.get("accumulatePayment").toString() : "0"));
            rowNum.createCell(22).setCellValue(new HSSFRichTextString(info.get("init_payment_amount") != null ? info.get("init_payment_amount").toString() : "0"));
            rowNum.createCell(23).setCellValue(new HSSFRichTextString(info.get("huawei_amount") != null ? info.get("huawei_amount").toString() : "0"));
            rowNum.createCell(24).setCellValue(new HSSFRichTextString(info.get("yearAmount") != null ? info.get("yearAmount").toString() : "0"));
            rowNum.createCell(25).setCellValue(new HSSFRichTextString(info.get("arrears") != null ? info.get("arrears").toString() : "0"));
            rowNum.createCell(26).setCellValue(new HSSFRichTextString(info.get("unPayment") != null ? info.get("unPayment").toString() : "0"));
            num++;
        }
        return hssfWorkbook;
    }




}

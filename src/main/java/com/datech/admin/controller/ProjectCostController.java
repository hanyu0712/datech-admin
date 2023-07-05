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
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 采购成本统计
 */
@Controller
public class ProjectCostController {

    @GetMapping("/projectCost")
    public String getList(HttpSession session, Model model) {
        model.addAttribute("dataList", getData());
        return "project_cost";
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> infoMaps = SqlRunner.db().selectList("SELECT id,contract_no,customer_name,project_name,sales_subject,sales_person,contract_amount,sign_date,company FROM sales_info WHERE `status`=2 order by contract_no desc");
        //预算金额-硬件
        List<Map<String, Object>> budgetHardware = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(budget_amount,0)) amount from purchase_info where `status`=2 and  purchase_category in ('硬件', '设备、材料') GROUP BY sales_contract_no");
        //预算金额-软件
        List<Map<String, Object>> budgetSoftware = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(budget_amount,0)) amount from purchase_info where `status`=2 and  purchase_category='软件' GROUP BY sales_contract_no");
        //预算金额-服务
        List<Map<String, Object>> budgetService = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(budget_amount,0)) amount from purchase_info where `status`=2 and  purchase_category='服务' GROUP BY sales_contract_no");
        //预算金额-质保
        List<Map<String, Object>> budgetWarranty = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(budget_amount,0)) amount from purchase_info where `status`=2 and  purchase_category='质保' GROUP BY sales_contract_no");
        //合同金额-硬件
        List<Map<String, Object>> contractHardware = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(contract_amount,0)) amount from purchase_info where `status`=2 and  purchase_category in ('硬件', '设备、材料') GROUP BY sales_contract_no");
        //合同金额-软件
        List<Map<String, Object>> contractSoftware = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(contract_amount,0)) amount from purchase_info where `status`=2 and  purchase_category='软件' GROUP BY sales_contract_no");
        //合同金额-服务
        List<Map<String, Object>> contractService = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(contract_amount,0)) amount from purchase_info where `status`=2 and  purchase_category='服务' GROUP BY sales_contract_no");
        //合同金额-质保
        List<Map<String, Object>> contractWarranty = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(contract_amount,0)) amount from purchase_info where `status`=2 and  purchase_category='质保' GROUP BY sales_contract_no");
        //以前年度已收票
        List<Map<String, Object>> initInvoiceList = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(init_invoice_amount,0)) amount from purchase_info where `status`=2 GROUP BY sales_contract_no");
        //累计已收票
        List<Map<String, Object>> accumulateInvoiceList = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(pinvoice.invoice_amount,0)) amount from purchase_info pinfo left join purchase_invoice pinvoice on pinfo.contract_no = pinvoice.contract_no and pinvoice.`status`=2 where pinfo.`status`=2 GROUP BY sales_contract_no");
        //本年度已收票
        List<Map<String, Object>> yearInvoiceList = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(pinvoice.invoice_amount,0)) amount from purchase_info pinfo left join purchase_invoice pinvoice on pinfo.contract_no = pinvoice.contract_no and pinvoice.`status`=2 AND pinvoice.invoice_date >= CONCAT(YEAR(CURDATE()),'-01-01') and pinvoice.invoice_date < CONCAT(YEAR(CURDATE())+1,'-01-01') where pinfo.`status`=2 GROUP BY sales_contract_no");
        //以前年度已付款
        List<Map<String, Object>> initPaymentList = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(init_payment_amount,0)) amount from purchase_info where `status`=2 GROUP BY sales_contract_no");
        //华为激励
        List<Map<String, Object>> huaweiList = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(huawei_amount,0)) amount from purchase_info where `status`=2 GROUP BY sales_contract_no");
        //累计已付款
        List<Map<String, Object>> accumulatePaymentList = SqlRunner.db().selectList("select sales_contract_no, SUM(IFNULL(ppayment.payment_amount,0)) amount from purchase_info pinfo left join purchase_payment ppayment on pinfo.contract_no = ppayment.contract_no and ppayment.`status`=2 where pinfo.`status`=2 GROUP BY sales_contract_no");
        for (Map<String, Object> info : infoMaps) {

            //预算金额-硬件
            info.put("budgetHardware", BigDecimal.ZERO);
            for (Map<String, Object> collection : budgetHardware) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("budgetHardware", collection.get("amount"));
                    break;
                }
            }
            //预算金额-软件
            info.put("budgetSoftware", BigDecimal.ZERO);
            for (Map<String, Object> collection : budgetSoftware) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("budgetSoftware", collection.get("amount"));
                    break;
                }
            }
            //预算金额-服务
            info.put("budgetService", BigDecimal.ZERO);
            for (Map<String, Object> collection : budgetService) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("budgetService", collection.get("amount"));
                    break;
                }
            }
            //预算金额-质保
            info.put("budgetWarranty", BigDecimal.ZERO);
            for (Map<String, Object> collection : budgetWarranty) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("budgetWarranty", collection.get("amount"));
                    break;
                }
            }
            //合同金额-硬件
            info.put("contractHardware", BigDecimal.ZERO);
            for (Map<String, Object> collection : contractHardware) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("contractHardware", collection.get("amount"));
                    break;
                }
            }
            //合同金额-软件
            info.put("contractSoftware", BigDecimal.ZERO);
            for (Map<String, Object> collection : contractSoftware) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("contractSoftware", collection.get("amount"));
                    break;
                }
            }
            //合同金额-服务
            info.put("contractService", BigDecimal.ZERO);
            for (Map<String, Object> collection : contractService) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("contractService", collection.get("amount"));
                    break;
                }
            }
            //合同金额-质保
            info.put("contractWarranty", BigDecimal.ZERO);
            for (Map<String, Object> collection : contractWarranty) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("contractWarranty", collection.get("amount"));
                    break;
                }
            }
            //以前年度已收票
            info.put("initInvoice", BigDecimal.ZERO);
            for (Map<String, Object> collection : initInvoiceList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("initInvoice", collection.get("amount"));
                    break;
                }
            }
            //累计年度已收票
            info.put("accumulateInvoice", BigDecimal.ZERO);
            for (Map<String, Object> collection : accumulateInvoiceList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("accumulateInvoice", collection.get("amount"));
                    break;
                }
            }
            //累计年度已收票
            info.put("yearInvoice", BigDecimal.ZERO);
            for (Map<String, Object> collection : yearInvoiceList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("yearInvoice", collection.get("amount"));
                    break;
                }
            }
            //以前年度已付款
            info.put("initPayment", BigDecimal.ZERO);
            for (Map<String, Object> collection : initPaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("initPayment", collection.get("amount"));
                    break;
                }
            }
            //华为激励
            info.put("huawei", BigDecimal.ZERO);
            for (Map<String, Object> collection : huaweiList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("huawei", collection.get("amount"));
                    break;
                }
            }
            //累计年度已付款
            info.put("accumulatePayment", BigDecimal.ZERO);
            for (Map<String, Object> collection : accumulatePaymentList) {
                if (info.get("contract_no").toString().trim().equals(collection.get("sales_contract_no").toString().trim())) {
                    info.put("accumulatePayment", collection.get("amount"));
                    break;
                }
            }
        }
        for (Map<String, Object> info : infoMaps) {
            //预算总成本（含税）= 单项合同预算金额（硬件+软件+服务+质保）
            BigDecimal budgetCost = ((BigDecimal) info.get("budgetHardware")).add((BigDecimal) info.get("budgetSoftware")).add((BigDecimal) info.get("budgetService")).add((BigDecimal) info.get("budgetWarranty"));
            info.put("budgetCost", budgetCost);

            BigDecimal contractAmount = info.get("contract_amount") != null ? (BigDecimal) info.get("contract_amount") : BigDecimal.ZERO;

            //预算成本率 = 预算总成本（含税）/ 合同金额
            BigDecimal budgetCostRate = BigDecimal.ZERO;
            if (contractAmount.intValue() != 0) {
                budgetCostRate = budgetCost.divide(contractAmount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100L)).setScale(2, RoundingMode.HALF_UP);
            }
            info.put("budgetCostRate", budgetCostRate);

            //执行总成本（含税）= 采购合同金额（硬件+软件+服务+质保）
            BigDecimal executionCost = ((BigDecimal) info.get("contractHardware")).add((BigDecimal) info.get("contractSoftware")).add((BigDecimal) info.get("contractService")).add((BigDecimal) info.get("contractWarranty"));
            info.put("executionCost", executionCost);
            //执行成本率 = 执行总成本（含税）/ 合同金额
            BigDecimal executionCostRate = BigDecimal.ZERO;
            if (contractAmount.intValue() != 0) {
                executionCostRate = executionCost.divide(contractAmount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100L)).setScale(2, RoundingMode.HALF_UP);
            }
            info.put("executionCostRate", executionCostRate);

            //累计收票 = 以前年度已开票 + 累计已开票
            BigDecimal initInvoice = (BigDecimal) info.get("initInvoice");
            BigDecimal accumulateInvoice = (BigDecimal) info.get("accumulateInvoice");
            accumulateInvoice = initInvoice.add(accumulateInvoice);
            info.put("accumulateInvoice", accumulateInvoice);

            //欠票 = 执行成本（含税） - 累计收票
            BigDecimal unInvoiced = executionCost.subtract(accumulateInvoice);
            info.put("unInvoiced", unInvoiced);

            //累计已付款 = 以前年度已付款 + 累计已付款 + 华为激励
            BigDecimal initPayment = (BigDecimal) info.get("initPayment");
            BigDecimal huawei = (BigDecimal) info.get("huawei");
            BigDecimal accumulatePayment = (BigDecimal) info.get("accumulatePayment");
            accumulatePayment = initPayment.add(accumulatePayment).add(huawei);
            info.put("accumulatePayment", accumulatePayment);

            //合同欠款 = 执行成本（含税）- 累计已付款
            BigDecimal arrears = executionCost.subtract(accumulatePayment);
            info.put("arrears", arrears);

        }
        return infoMaps;
    }


    // 导出成excel
    @RequestMapping("/projectCostExport")
    public void projectCostExport(HttpServletResponse response) {
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
                    "attachment; filename=" + new String("项目成本汇总分析".getBytes("GB2312"), "8859_1") + ".xls");
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
        row.createCell(0).setCellValue(new HSSFRichTextString("签约公司"));
        row.createCell(1).setCellValue(new HSSFRichTextString("合同号"));
        row.createCell(2).setCellValue(new HSSFRichTextString("销售负责人"));
        row.createCell(3).setCellValue(new HSSFRichTextString("签订时间"));
        row.createCell(4).setCellValue(new HSSFRichTextString("项目名称"));
        row.createCell(5).setCellValue(new HSSFRichTextString("客户名称"));
        row.createCell(6).setCellValue(new HSSFRichTextString("合同金额"));
        row.createCell(7).setCellValue(new HSSFRichTextString("预算成本率"));
        row.createCell(8).setCellValue(new HSSFRichTextString("预算总成本（含税）"));
        row.createCell(9).setCellValue(new HSSFRichTextString("预算硬件"));
        row.createCell(10).setCellValue(new HSSFRichTextString("预算软件"));
        row.createCell(11).setCellValue(new HSSFRichTextString("预算服务"));
        row.createCell(12).setCellValue(new HSSFRichTextString("预算质保"));
        row.createCell(13).setCellValue(new HSSFRichTextString("执行成本率"));
        row.createCell(14).setCellValue(new HSSFRichTextString("执行成本（含税）"));
        row.createCell(15).setCellValue(new HSSFRichTextString("执行成本—硬件"));
        row.createCell(16).setCellValue(new HSSFRichTextString("执行成本—软件"));
        row.createCell(17).setCellValue(new HSSFRichTextString("执行成本—服务"));
        row.createCell(18).setCellValue(new HSSFRichTextString("执行成本—质保"));
        row.createCell(19).setCellValue(new HSSFRichTextString("累计收票"));
        row.createCell(20).setCellValue(new HSSFRichTextString("欠票"));
        row.createCell(21).setCellValue(new HSSFRichTextString("累计已付款"));
        row.createCell(22).setCellValue(new HSSFRichTextString("合同欠款"));
        List<Map<String, Object>> infoMaps = getData();
        Iterator<Map<String, Object>> iterator = infoMaps.iterator();
        int num = 1;
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd");
        while (iterator.hasNext()) {
            Map<String, Object> info = iterator.next();
            HSSFRow rowNum = sheet.createRow(num);
            rowNum.createCell(0).setCellValue(new HSSFRichTextString("迪科"));
            rowNum.createCell(1).setCellValue(new HSSFRichTextString(info.get("contract_no") != null ? info.get("contract_no").toString() : ""));
            rowNum.createCell(2).setCellValue(new HSSFRichTextString(info.get("sales_person") != null ? info.get("sales_person").toString() : ""));
            rowNum.createCell(3).setCellValue(new HSSFRichTextString(info.get("sign_date") != null ? dateTimeFormatter.format((Date) info.get("sign_date")) : ""));
            rowNum.createCell(4).setCellValue(new HSSFRichTextString(info.get("project_name") != null ? info.get("project_name").toString() : ""));
            rowNum.createCell(5).setCellValue(new HSSFRichTextString(info.get("customer_name") != null ? info.get("customer_name").toString() : ""));
            rowNum.createCell(6).setCellValue(new HSSFRichTextString(info.get("contract_amount") != null ? info.get("contract_amount").toString() : "0"));
            rowNum.createCell(7).setCellValue(new HSSFRichTextString(info.get("budgetCostRate") != null ? info.get("budgetCostRate").toString() + "%" : "0"));
            rowNum.createCell(8).setCellValue(new HSSFRichTextString(info.get("budgetCost") != null ? info.get("budgetCost").toString() : "0"));
            rowNum.createCell(9).setCellValue(new HSSFRichTextString(info.get("budgetHardware") != null ? info.get("budgetHardware").toString() : "0"));
            rowNum.createCell(10).setCellValue(new HSSFRichTextString(info.get("budgetSoftware") != null ? info.get("budgetSoftware").toString() : "0"));
            rowNum.createCell(11).setCellValue(new HSSFRichTextString(info.get("budgetService") != null ? info.get("budgetService").toString() : "0"));
            rowNum.createCell(12).setCellValue(new HSSFRichTextString(info.get("budgetWarranty") != null ? info.get("budgetWarranty").toString() : "0"));
            rowNum.createCell(13).setCellValue(new HSSFRichTextString(info.get("executionCostRate") != null ? info.get("executionCostRate").toString() + "%" : "0"));
            rowNum.createCell(14).setCellValue(new HSSFRichTextString(info.get("executionCost") != null ? info.get("executionCost").toString() : "0"));
            rowNum.createCell(15).setCellValue(new HSSFRichTextString(info.get("contractHardware") != null ? info.get("contractHardware").toString() : "0"));
            rowNum.createCell(16).setCellValue(new HSSFRichTextString(info.get("contractSoftware") != null ? info.get("contractSoftware").toString() : "0"));
            rowNum.createCell(17).setCellValue(new HSSFRichTextString(info.get("contractService") != null ? info.get("contractService").toString() : "0"));
            rowNum.createCell(18).setCellValue(new HSSFRichTextString(info.get("contractWarranty") != null ? info.get("contractWarranty").toString() : "0"));
            rowNum.createCell(19).setCellValue(new HSSFRichTextString(info.get("accumulateInvoice") != null ? info.get("accumulateInvoice").toString() : "0"));
            rowNum.createCell(20).setCellValue(new HSSFRichTextString(info.get("unInvoiced") != null ? info.get("unInvoiced").toString() : "0"));
            rowNum.createCell(21).setCellValue(new HSSFRichTextString(info.get("accumulatePayment") != null ? info.get("accumulatePayment").toString() : "0"));
            rowNum.createCell(22).setCellValue(new HSSFRichTextString(info.get("arrears") != null ? info.get("arrears").toString() : "0"));
            num++;
        }
        return hssfWorkbook;
    }



}

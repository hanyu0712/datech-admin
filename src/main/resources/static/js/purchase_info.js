
$(document).ready(function() {

    let contract;
    $.ajax({
        url: document.getElementById("serverUrl").value +'/salesInfoSelect',
        type: 'get',
        dataType: "json",
        success: function (data) {
            contract = data;
        },
        error: function (xhr, status, error) {
            // 处理请求失败后的操作，例如提示错误信息或保留模态框
            console.log(error);
        }
    });
    $("#salesContractNo").change(function(){
        let selectVal = $("#salesContractNo").val();
        for (let i = 0; i < contract.length; i++) {
            if (contract[i].contractNo === selectVal){
                $("#projectName").val(contract[i].projectName);
                $("#company").val(contract[i].company);
                break
            }
        }
    });

    $('#salesContractModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget); // 触发事件的按钮
        let id = button.data('rowid');// 解析出data-whatever内容
        let method = button.data('method');// 解析出data-whatever内容
        let modal = $(this);
        let select = $("#salesContractNo");
        select.html("");//合同号下拉框
        if ("insert" === method) {
            select.append("<option value=''>请选择</option>");
            for (let i = 0; i < contract.length; i++) {
                select.append("<option value='" + contract[i].contractNo + "'>" + contract[i].contractNo + "</option>");
            }
            modal.find('#saveDiv').show();
            modal.find('#checkDiv').hide();
            modal.find('#rollbackDiv').hide();
            modal.find('#rejectionContentDiv').hide();
            modal.find('#rejectionContent').val("");
        } else {
            $.ajax({
                url: document.getElementById("serverUrl").value +'/purchaseInfo/'+id,
                type: 'get',
                dataType: "json",
                success: function (data) {
                    modal.find('#id').val(data.id);
                    for (let i = 0; i < contract.length; i++) {
                        select.append("<option value='" + contract[i].contractNo + "'>" + contract[i].contractNo + "</option>");
                    }
                    modal.find('#salesContractNo').val(data.salesContractNo);
                    modal.find('#contractNo').val(data.contractNo);
                    modal.find('#supplierName').val(data.supplierName);
                    modal.find('#projectName').val(data.projectName);
                    modal.find('#signSubject').val(data.signSubject);
                    modal.find('#signDate').val(data.signDateStr);
                    modal.find('#receivingDate').val(data.receivingDate);
                    modal.find('#paymentDate').val(data.paymentDate);
                    modal.find('#budgetAmount').val(data.budgetAmount);
                    modal.find('#contractAmount').val(data.contractAmount);
                    modal.find('#purchaseCategory').val(data.purchaseCategory);
                    modal.find('#purchaseMaterial').val(data.purchaseMaterial);
                    modal.find('#payMode').val(data.payMode);
                    modal.find('#isAll').val(data.isAll);
                    modal.find('#invoiceType').val(data.invoiceType);
                    modal.find('#taxRate').val(data.taxRate);
                    modal.find('#initInvoiceAmount').val(data.initInvoiceAmount);
                    modal.find('#initPaymentAmount').val(data.initPaymentAmount);
                    modal.find('#huaweiAmount').val(data.huaweiAmount);
                    modal.find('#rejectionContent').val(data.rejectionContent);
                    modal.find('#company').val(data.company);
                    if ("update" === method) {
                        modal.find('#saveDiv').show();
                        modal.find('#checkDiv').hide();
                        modal.find('#rollbackDiv').hide();
                        modal.find('#rejectionContentDiv').show();
                        $("#rejectionContent").attr("disabled",true);
                    }
                    if ("check" === method) {
                        modal.find('#saveDiv').hide();
                        modal.find('#checkDiv').show();
                        modal.find('#rollbackDiv').hide();
                        modal.find('#rejectionContentDiv').show();
                        $("#rejectionContent").attr("disabled",false);
                    }
                    if ("rollback" === method) {
                        modal.find('#saveDiv').hide();
                        modal.find('#checkDiv').hide();
                        modal.find('#rollbackDiv').show();
                        modal.find('#rejectionContentDiv').show();
                    }
                },
                error: function (xhr, status, error) {
                    // 处理请求失败后的操作，例如提示错误信息或保留模态框
                    console.log(error);
                }
            });
        }
    })


    $(document).on('click', '#button-save', function () {
        let contract_data = {};
        $.each($("#contract-form").serializeArray(), function(i, field){
            contract_data[field.name]=field.value;
        });
        contract_data["method"] = "save";
        saveOrUpdate(contract_data)
    });
    $(document).on('click', '#button-submit', function () {
        let contract_data = {};
        $.each($("#contract-form").serializeArray(), function(i, field){
            contract_data[field.name]=field.value;
        });
        contract_data["method"] = "submit";
        if (!contract_data.salesContractNo) {
            $.gritter.add({
                title: '系统消息',
                text: "请选择主合同号"
            });
            return false;
        }
        if (!contract_data.signDate) {
            $.gritter.add({
                title: '系统消息',
                text: "请选择合同签订日期"
            });
            return false;
        }
        if (!(!isNaN(parseFloat(contract_data.contractAmount)) && isFinite(contract_data.contractAmount))) {
            $.gritter.add({
                title: '系统消息',
                text: "采购合同金额只能输入数字"
            });
            return false;
        }
        if (!(!isNaN(parseFloat(contract_data.budgetAmount)) && isFinite(contract_data.budgetAmount))) {
            $.gritter.add({
                title: '系统消息',
                text: "单项合同预算金额只能输入数字"
            });
            return false;
        }
        if (!(!isNaN(parseFloat(contract_data.initInvoiceAmount)) && isFinite(contract_data.initInvoiceAmount))) {
            $.gritter.add({
                title: '系统消息',
                text: "以前年度已收票只能输入数字"
            });
            return false;
        }
        if (!(!isNaN(parseFloat(contract_data.initPaymentAmount)) && isFinite(contract_data.initPaymentAmount))) {
            $.gritter.add({
                title: '系统消息',
                text: "以前年度已付款只能输入数字"
            });
            return false;
        }
        saveOrUpdate(contract_data)
    });
    $(document).on('click', '#button-approved', function () {
        let contract_data = {};
        contract_data["id"] = $.trim($("#id").val());
        contract_data["method"] = "approved";
        saveOrUpdate(contract_data)
    });
    $(document).on('click', '#button-reject', function () {
        let contract_data = {};
        contract_data["id"] = $.trim($("#id").val());
        contract_data["rejectionContent"] = $.trim($("#rejectionContent").val());
        contract_data["method"] = "reject";
        saveOrUpdate(contract_data)
    });
    $(document).on('click', '#button-rollback', function () {
        let contract_data = {};
        contract_data["id"] = $.trim($("#id").val());
        contract_data["rejectionContent"] = $.trim($("#rejectionContent").val());
        contract_data["method"] = "rollback";
        saveOrUpdate(contract_data)
    });
    $('#salesContractModal').on('hide.bs.modal', function () {
        let modal = $(this);
        modal.find('#contractNo').val("");
        modal.find('#salesContractNo').val("");
        modal.find('#supplierName').val("");
        modal.find('#projectName').val("");
        modal.find('#signSubject').val("");
        modal.find('#signDate').val("");
        modal.find('#contractAmount').val("");
        modal.find('#budgetAmount').val("");
        modal.find('#purchaseCategory').val("");
        modal.find('#purchaseMaterial').val("");
        modal.find('#payMode').val("");
        modal.find('#receivingDate').val("");
        modal.find('#paymentDate').val("");
        modal.find('#isAll').val("");
        modal.find('#invoiceType').val("");
        modal.find('#taxRate').val("");
        modal.find('#initInvoiceAmount').val("");
        modal.find('#initPaymentAmount').val("");
        modal.find('#huaweiAmount').val("");
        modal.find('#rejectionContent').val("");
        modal.find('#rejectionContent').val("");
        modal.find('#company').val("");
    });

    // $('#salesContractModal').on('hidden.bs.modal', function () {
    //     alert('模态框关闭了hidden');
    // });
} );
function saveOrUpdate(contract_data)
{
    $.ajax({
        url: document.getElementById("serverUrl").value + "/updatePurchaseInfo",
        type: 'post',
        dataType: "json",
        data: contract_data,
        success: function (data) {
            // 处理请求成功后的操作，例如更新页面内容或关闭模态框
            $.gritter.add({
                title: '系统消息',
                text: data.msg
            });
            setTimeout(() => {
                window.location.reload();
            }, 2000)
        },
        error: function (xhr, status, error) {
            // 处理请求失败后的操作，例如提示错误信息或保留模态框
            console.log(error);
            $.gritter.add({
                title: '系统消息',
                text: error
            });
        }
    });
    return true;
}
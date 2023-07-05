
$(document).ready(function() {
    let contract;
    $.ajax({
        url: document.getElementById("serverUrl").value +'/purchaseInfoSelect',
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
    $("#contractNo").change(function(){
        let selectVal = $("#contractNo").val();
        for (let i = 0; i < contract.length; i++) {
            if (contract[i].contractNo === selectVal){
                $("#projectName").val(contract[i].projectName);
                $("#supplierName").val(contract[i].supplierName);
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
        if ("insert" === method) {
            modal.find('#saveDiv').show();
            modal.find('#checkDiv').hide();
            modal.find('#rollbackDiv').hide();
            modal.find('#rejectionContentDiv').hide();
            modal.find('#rejectionContent').val("");
        } else {
            $.ajax({
                url: document.getElementById("serverUrl").value +'/purchasePayment/'+id,
                type: 'get',
                dataType: "json",
                success: function (data) {
                    modal.find('#id').val(data.id);
                    modal.find('#contractNo').val(data.contractNo);
                    modal.find('#supplierName').val(data.supplierName);
                    modal.find('#projectName').val(data.projectName);
                    modal.find('#paymentDate').val(data.paymentDateStr);
                    modal.find('#paymentAmount').val(data.paymentAmount);
                    modal.find('#paymentBank').val(data.paymentBank);
                    modal.find('#paymentMode').val(data.paymentMode);
                    modal.find('#receiveCompany').val(data.receiveCompany);
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
        saveOrUpdate(contract_data, "/updatePurchasePayment")
    });
    $(document).on('click', '#button-submit', function () {
        let contract_data = {};
        $.each($("#contract-form").serializeArray(), function(i, field){
            contract_data[field.name]=field.value;
        });
        contract_data["method"] = "submit";
        if (!contract_data.contractNo) {
            $.gritter.add({
                title: '系统消息',
                text: "请选择合同号"
            });
            return false;
        }
        if (!contract_data.paymentDate) {
            $.gritter.add({
                title: '系统消息',
                text: "请选择交易日期"
            });
            return false;
        }
        if (!(!isNaN(parseFloat(contract_data.paymentAmount)) && isFinite(contract_data.paymentAmount))) {
            $.gritter.add({
                title: '系统消息',
                text: "金额只能输入数字"
            });
            return false;
        }
        saveOrUpdate(contract_data, "/updatePurchasePayment")
    });
    $(document).on('click', '#button-approved', function () {
        let contract_data = {};
        contract_data["id"] = $.trim($("#id").val());
        contract_data["method"] = "approved";
        saveOrUpdate(contract_data, "/updatePurchasePayment")
    });
    $(document).on('click', '#button-reject', function () {
        let contract_data = {};
        contract_data["id"] = $.trim($("#id").val());
        contract_data["rejectionContent"] = $.trim($("#rejectionContent").val());
        contract_data["method"] = "reject";
        saveOrUpdate(contract_data, "/updatePurchasePayment")
    });
    $(document).on('click', '#button-rollback', function () {
        let contract_data = {};
        contract_data["id"] = $.trim($("#id").val());
        contract_data["rejectionContent"] = $.trim($("#rejectionContent").val());
        contract_data["method"] = "rollback";
        saveOrUpdate(contract_data, "/updatePurchasePayment")
    });
    $('#salesContractModal').on('hide.bs.modal', function () {
        let modal = $(this);
        modal.find('#id').val("");
        modal.find('#contractNo').val("");
        modal.find('#supplierName').val("");
        modal.find('#projectName').val("");
        modal.find('#paymentDate').val("");
        modal.find('#paymentAmount').val("");
        modal.find('#paymentBank').val("");
        modal.find('#paymentMode').val("");
        modal.find('#receiveCompany').val("");
        modal.find('#rejectionContent').val("");
        modal.find('#company').val("");
    });

    // $('#salesContractModal').on('hidden.bs.modal', function () {
    //     alert('模态框关闭了hidden');
    // });
} );
function saveOrUpdate(contract_data, method)
{
    $.ajax({
        url: document.getElementById("serverUrl").value + method,
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
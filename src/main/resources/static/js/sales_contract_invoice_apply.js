
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
    $("#contractNo").change(function(){
        let selectVal = $("#contractNo").val();
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
        // let select = $("#contractNo");
        // select.html("");//合同号下拉框
        if ("insert" === method) {
            // select.attr("disabled",false);
            // select.append("<option value=''>请选择</option>");
            // for (let i = 0; i < contract.length; i++) {
            //     select.append("<option value='" + contract[i].contractNo + "'>" + contract[i].contractNo + "</option>");
            // }
            modal.find('#saveDiv').show();
            modal.find('#checkDiv').hide();
            modal.find('#rollbackDiv').hide();
            modal.find('#rejectionContentDiv').hide();
            modal.find('#rejectionContent').val("");
        } else {
            $.ajax({
                url: document.getElementById("serverUrl").value +'/salesInvoiceApply/'+id,
                type: 'get',
                dataType: "json",
                success: function (data) {
                    modal.find('#id').val(data.id);
                    // for (let i = 0; i < contract.length; i++) {
                    //     select.append("<option value='" + contract[i].contractNo + "'>" + contract[i].contractNo + "</option>");
                    // }
                    modal.find('#contractNo').val(data.contractNo);
                    modal.find('#projectName').val(data.projectName);
                    modal.find('#service').val(data.service);
                    modal.find('#serviceTaxRate').val(data.serviceTaxRate);
                    modal.find('#equipment').val(data.equipment);
                    modal.find('#equipmentTaxRate').val(data.equipmentTaxRate);
                    modal.find('#invoicedAmount').val(data.invoicedAmount);
                    modal.find('#thisTimeAmount').val(data.thisTimeAmount);
                    modal.find('#returnedAmount').val(data.returnedAmount);
                    modal.find('#expectedReturnAmount').val(data.expectedReturnAmount);
                    modal.find('#expectedReturnTime').val(data.expectedReturnTime);
                    modal.find('#arrivalInfo').val(data.arrivalInfo);
                    modal.find('#receiptInfo').val(data.receiptInfo);
                    modal.find('#invoiceContent').val(data.invoiceContent);
                    modal.find('#companyName').val(data.companyName);
                    modal.find('#taxpayerNo').val(data.taxpayerNo);
                    modal.find('#address').val(data.address);
                    modal.find('#bank').val(data.bank);
                    modal.find('#remark').val(data.remark);
                    modal.find('#invoiceSigner').val(data.invoiceSigner);
                    modal.find('#logisticsNo').val(data.logisticsNo);
                    modal.find('#goods').val(data.goods);
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
        if (!contract_data.contractNo) {
            $.gritter.add({
                title: '系统消息',
                text: "请选择合同号"
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
        modal.find('#id').val("");
        modal.find('#contractNo').val("");
        modal.find('#projectName').val("");
        modal.find('#invoicedAmount').val("");
        modal.find('#thisTimeAmount').val("");
        modal.find('#returnedAmount').val("");
        modal.find('#expectedReturnAmount').val("");

        modal.find('#expectedReturnTime').val("");
        modal.find('#arrivalInfo').val("");
        modal.find('#receiptInfo').val("");
        modal.find('#invoiceContent').val("");
        modal.find('#companyName').val("");
        modal.find('#taxpayerNo').val("");
        modal.find('#address').val("");
        modal.find('#bank').val("");
        modal.find('#remark').val("");
        modal.find('#invoiceSigner').val("");
        modal.find('#logisticsNo').val("");
        modal.find('#goods').val("");
        modal.find('#company').val("");
    });

    // $('#salesContractModal').on('hidden.bs.modal', function () {
    //     alert('模态框关闭了hidden');
    // });

} );

function saveOrUpdate(contract_data)
{
    $.ajax({
        url: document.getElementById("serverUrl").value +'/updateSalesInvoiceApply',
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



$(document).ready(function() {

    $('#salesContractModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // 触发事件的按钮
        var id = button.data('rowid');// 解析出data-whatever内容
        var method = button.data('method');// 解析出data-whatever内容
        var modal = $(this);
        if ("insert" === method) {
            modal.find('#saveDiv').show();
            modal.find('#checkDiv').hide();
            modal.find('#rollbackDiv').hide();
            modal.find('#rejectionContentDiv').hide();
            modal.find('#rejectionContent').val("");
        } else {
            $.ajax({
                url: document.getElementById("serverUrl").value +'/salesInfo/'+id,
                type: 'get',
                dataType: "json",
                success: function (data) {
                    modal.find('#id').val(data.id);
                    modal.find('#contractNo').val(data.contractNo);
                    modal.find('#customerName').val(data.customerName);
                    modal.find('#projectName').val(data.projectName);
                    let salesSubject = document.getElementById("salesSubject");
                    for (let i=0; i<salesSubject.options.length; ++i) {
                        if (salesSubject.options[i]?.value === data.salesSubject) {
                            salesSubject.options[i].selected = true;
                        }
                    }
                    modal.find('#salesPerson').val(data.salesPerson);
                    modal.find('#contractAmount').val(data.contractAmount);
                    modal.find('#signDate').val(data.signDateStr);
                    modal.find('#signStatus').val(data.signStatus);
                    let taxRate = document.getElementById("taxRate");
                    for (i=0; i<taxRate.options.length; ++i) {
                        if (taxRate.options[i]?.value === data.taxRate) {
                            taxRate.options[i].selected = true;
                        }
                    }
                    let businessCategory = document.getElementById("businessCategory");
                    for (i=0; i<businessCategory.options.length; ++i) {
                        if (businessCategory.options[i]?.value === data.businessCategory) {
                            businessCategory.options[i].selected = true;
                        }
                    }
                    let productCategory = document.getElementById("productCategory");
                    for (i=0; i<productCategory.options.length; ++i) {
                        if (productCategory.options[i]?.value === data.productCategory) {
                            productCategory.options[i].selected = true;
                        }
                    }
                    modal.find('#payMode').val(data.payMode);
                    modal.find('#qualityGuaranteeRatio').val(data.qualityGuaranteeRatio);
                    modal.find('#deliveryMonth').val(data.deliveryMonth);
                    modal.find('#receivingMonth').val(data.receivingMonth);
                    modal.find('#logisticsNo').val(data.logisticsNo);
                    modal.find('#rebateAmount').val(data.rebateAmount);
                    modal.find('#invoiceAmount').val(data.invoiceAmount);
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
                text: "请输入合同号"
            });
            return false;
        }
        if (!(!isNaN(parseFloat(contract_data.contractAmount)) && isFinite(contract_data.contractAmount))) {
            $.gritter.add({
                title: '系统消息',
                text: "合同金额只能输入数字"
            });
            return false;
        }
        if (!(!isNaN(parseFloat(contract_data.rebateAmount)) && isFinite(contract_data.rebateAmount))) {
            $.gritter.add({
                title: '系统消息',
                text: "以前年度回款只能输入数字"
            });
            return false;
        }
        if (!(!isNaN(parseFloat(contract_data.invoiceAmount)) && isFinite(contract_data.invoiceAmount))) {
            $.gritter.add({
                title: '系统消息',
                text: "以前年度开票只能输入数字"
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
    $(document).on('click','#dynamic-table tbody td a.button-del',function () {
        if(confirm('确定删除吗？')){
            let contract_data = {};
            contract_data["id"] = $(this)[0].getAttribute("data-rowId");
            $.ajax({
                url: document.getElementById("serverUrl").value +'/delSalesInfo',
                type: 'post',
                dataType: "json",
                data: contract_data,
                success: function (data) {
                    // 处理请求成功后的操作，例如更新页面内容或关闭模态框
                    $.gritter.add({
                        title: '系统消息',
                        text: data.msg
                    });
                    if (data.code !== 500){
                        setTimeout(() => {
                            window.location.reload();
                        }, 2000)
                    }
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
        }
        return true;
    } );
    $('#salesContractModal').on('hide.bs.modal', function () {
        let modal = $(this);
        modal.find('#id').val("");
        modal.find('#contractNo').val("");
        modal.find('#customerName').val("");
        modal.find('#projectName').val("");
        modal.find('#salesSubject').val("");
        modal.find('#salesPerson').val("");
        modal.find('#contractAmount').val("");
        modal.find('#signDate').val("");
        modal.find('#signStatus').val("");
        modal.find('#taxRate').val("");
        modal.find('#businessCategory').val("");
        modal.find('#productCategory').val("");
        modal.find('#payMode').val("");
        modal.find('#qualityGuaranteeRatio').val("");
        modal.find('#deliveryMonth').val("");
        modal.find('#receivingMonth').val("");
        modal.find('#logisticsNo').val("");
        modal.find('#rebateAmount').val("");
        modal.find('#invoiceAmount').val("");
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
        url: document.getElementById("serverUrl").value +'/updateSalesInfo',
        type: 'post',
        dataType: "json",
        data: contract_data,
        success: function (data) {
            // 处理请求成功后的操作，例如更新页面内容或关闭模态框
            $.gritter.add({
                title: '系统消息',
                text: data.msg
            });
            if (data.code !== 500){
                setTimeout(() => {
                    window.location.reload();
                }, 2000)
            }
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
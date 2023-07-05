
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
                url: document.getElementById("serverUrl").value +'/salesStatus/'+id,
                type: 'get',
                dataType: "json",
                success: function (data) {
                    // var str = JSON.stringify(data)
                    modal.find('#id').val(data.id);
                    modal.find('#contractNo').val(data.contractNo);
                    modal.find('#customerName').val(data.customerName);
                    modal.find('#projectName').val(data.projectName);
                    modal.find('#contractStatus').val(data.contractStatus);
                    modal.find('#receivingTime').val(data.receivingTime);
                    modal.find('#receiving').val(data.receiving);
                    modal.find('#acceptance').val(data.acceptance);
                    modal.find('#finalAcceptance').val(data.finalAcceptance);
                    modal.find('#shelfLife').val(data.shelfLife);
                    modal.find('#shelfLifeStart').val(data.shelfLifeStart);
                    modal.find('#shelfLifeEnd').val(data.shelfLifeEnd);
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
        modal.find('#customerName').val("");
        modal.find('#projectName').val("");
        modal.find('#contractStatus').val("");
        modal.find('#receivingTime').val("");
        modal.find('#receiving').val("");
        modal.find('#acceptance').val("");
        modal.find('#finalAcceptance').val("");
        modal.find('#shelfLife').val("");
        modal.find('#shelfLifeStart').val("");
        modal.find('#shelfLifeEnd').val("");
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
        url: document.getElementById("serverUrl").value +'/updateSalesStatus',
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
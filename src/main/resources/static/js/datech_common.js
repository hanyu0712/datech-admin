
$(document).ready(function() {

    //初始化表格
    $('#dynamic-table').dataTable( {
        "aaSorting": [[ 0, "desc" ]],
        "oLanguage": { //国际化配置
            "sProcessing" : "正在获取数据，请稍后...",
            "sLengthMenu" : "显示 _MENU_ 条",
            "sZeroRecords" : "没有您要搜索的内容",
            "sInfo" : "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
            "sInfoEmpty" : "记录数为0",
            "sInfoFiltered" : "(全部记录数 _MAX_ 条)",
            "sInfoPostFix" : "",
            "sSearch" : "搜索",
            "sUrl" : "",
            "oPaginate": {
                "sFirst" : "第一页",
                "sPrevious" : "上一页",
                "sNext" : "下一页",
                "sLast" : "最后一页"
            }
        }
    } );

    //展示操作日志的模态框
    $('#syslogModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // 触发事件的按钮
        var id = button.data('rowid');// 解析出data-whatever内容
        var table = button.data('table');// 解析出data-whatever内容
        var modal = $(this);
        $.ajax({
            url: document.getElementById("serverUrl").value +'/syslog/'+table+'/'+id,
            type: 'get',
            dataType: "json",
            success: function (data) {
                // 处理请求成功后的操作，例如更新页面内容或关闭模态框
                modal.find('#sysLogContent').html(data.msg);
            },
            error: function (xhr, status, error) {
                // 处理请求失败后的操作，例如提示错误信息或保留模态框
                console.log(error);
                alert(error)
            }
        });
    })



});
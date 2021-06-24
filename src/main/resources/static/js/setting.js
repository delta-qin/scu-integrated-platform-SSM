
// 页面加载完之后，调用这个函数，绑定提交事件
$(function(){
    $("#uploadForm").submit(upload);
});

function upload() {
    $.ajax({
        // 华南客户端上传
        url: "http://upload-z2.qiniup.com",
        method: "post",
        processData: false,
        contentType: false,
        data: new FormData($("#uploadForm")[0]),
        success: function(data) {
            if(data && data.code == 0) {
                // 更新后台的头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName":$("input[name='key']").val()},
                    function(data) {
                        data = $.parseJSON(data);
                        if(data.code == 0) {
                            // 刷新当前页面
                            window.location.reload();
                        } else {
                            alert(data.msg);
                        }
                    }
                );
            } else {
                alert("上传失败!");
            }
        }
    });
    // 最后还是会提交表单，这里就不会往下执行了
    return false;
}

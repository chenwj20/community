

$(document).ready(function(){
    layui.use(['layer'], function(){
        var layer = layui.layer;//获得layer模块

        $('#LAY_delallmsg').on('click', function(){
            var othis = $(this);
            layer.confirm('确定清空吗？', function(){
                $.ajax({
                    url:'/notification/remove/',
                    type:'get',
                    dataType:'json',
                    success:function (res) {
                        if (res.code == 1003){
                            layer.msg(res.msg);
                            othis.addClass('layui-hide');

                        }else {
                            layer.msg("删除失败");
                        }
                    }
                });
            });
        })
    })
})



//删除全部
/*
$('#LAY_delallmsg').on('click', function(){
    var othis = $(this);
    layer.confirm('确定清空吗？', function(index){
        fly.json('/message/remove/', {
            all: true
        }, function(res){
            if(res.status === 0){
                layer.close(index);
                othis.addClass('layui-hide');
                delEnd(true);
            }
        });
    });
});*/

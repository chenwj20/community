

$(document).ready(function(){
    layui.use(['layer'], function(){
        var layer = layui.layer //获得layer模块
        signStatus();
        newNotification();


$(document).on('click',"#LAY_signin",function(){
        signIn()
    });

function newNotification() {
    var elemUser = $('.fly-nav-user');
    $.ajax({
        url:'/notification/nums/',
        type:'get',
        dataType:'json',
        success:function (res) {
            if (res.code == 2003 || res.data == 0){
                return;
            } else {
                var msg = $('<a class="fly-nav-msg" href="/notification/all/page/1">'+ res.data +'</a>');
                elemUser.append(msg);
                layer.tips('你有 '+ res.data +' 条未读消息', msg, {
                    tips: 3
                    ,tipsMore: true
                    ,fixed: true
                });
                msg.on('mouseenter', function(){
                    layer.closeAll('tips');
                })
            }
        }
    })
}

function signStatus() {
    $.ajax({
        url:'/sign/status',
        type:'get',
        dataType:'json',
        success:function (res) {
            if (res.code == 2003) {
                var tplSignin = '<button id="LAY_signin"  class="layui-btn layui-btn-danger" >今日签到</button>';
                $("#sign").html(tplSignin);
            }else {
                $("#days").text(res.data.days);
                if (res.data.signed){
                    var tplSignin = '<button class="layui-btn layui-btn-disabled">今日已签到</button>'
                        +'<span>获得了<cite> '+res.data.miCoin +'</cite>米币</span>';
                    $("#sign").html(tplSignin);
                    // layer.msg(res.msg, {shift: 6});
                }else {
                    var tplSignin = '<button class="layui-btn layui-btn-danger"  id="LAY_signin">今日签到</button>'
                        +'<span>可获得<cite id="experience"> '+res.data.miCoin +'</cite>米币</span>';
                    $("#sign").html(tplSignin);
                }
            }

        }
    })
}

//签到点击方法
function signIn(){
    $.ajax({
        url: '/sign/in',
        type: 'get',
        dataType: 'json',
        success: function (res) {
            if (res.code == 2003) {
                layer.msg(res.msg);

            } else if (res.data.signed) {
                $("#days").text(res.data.days);
                var tplSignin = '<button class="layui-btn layui-btn-disabled">今日已签到</button>'
                    +'<span>获得了<cite> '+res.data.miCoin +'</cite>米币</span>';
                $("#sign").html(tplSignin);
            } else {
                layer.msg("请求错误");
            }
        }

    })

}


});

});


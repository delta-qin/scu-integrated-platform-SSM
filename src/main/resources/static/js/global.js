var CONTEXT_PATH = "";

window.alert = function(message) {
	if(!$(".alert-box").length) {
		$("body").append(
			'<div class="modal alert-box" tabindex="-1" role="dialog">'+
				'<div class="modal-dialog" role="document">'+
				'<div class="modal-content">'+
					'<div class="modal-header">'+
						'<h5 class="modal-title">提示</h5>'+
						'<button type="button" class="close" data-dismiss="modal" aria-label="Close">'+
							'<span aria-hidden="true">&times;</span>'+
						'</button>'+
					'</div>'+
					'<div class="modal-body">'+
						'<p></p>'+
					'</div>'+
					'<div class="modal-footer">'+
						'<button type="button" class="btn btn-secondary" data-dismiss="modal">确定</button>'+
					'</div>'+
					'</div>'+
				'</div>'+
			'</div>'
		);
	}

    var h = $(".alert-box").height();
	var y = h / 2 - 100;
	if(h > 600) y -= 100;
    $(".alert-box .modal-dialog").css("margin", (y < 0 ? 0 : y) + "px auto");

	$(".alert-box .modal-body p").text(message);
	$(".alert-box").modal("show");
}


var socket;

// 登录之后调用这个函数建立ws连接
function connect() {
	// connStatus.value = "正在连接 ......";

	if(!window.WebSocket){
		// window.WebSocket = window.MozWebSocket;
	}
	if(window.WebSocket){

		socket = new WebSocket("ws://127.0.0.1:1234/ws");

		socket.onmessage = function(event){
			// respText.scrollTop = respText.scrollHeight;
			// data += "\r\n" + event.data;
			var sysData = JSON.parse(event.data).content;

			var username = JSON.parse(event.data).username;
			alert(username + " 有新消息 : " + sysData)

		};
		socket.onopen = function(event){
			console.log("已经建立连ws接")
			alert("已经建立连ws接")
			// var id='<%=request.getAttribute("userId")%>';
			// console.log(id)
			// let userID = $("#userId").val();
			// console.log(userID + "")
			// send()
		};
		socket.onclose = function(event){
			console.log("已经关闭ws连接")
		};

	} else {
		alert("您的浏览器不支持WebSocket协议");
	}
}

function disconnect() {
	socket.close();
}

function send(userId){

	if(!window.WebSocket){return;}
	// if (socket.readyState == WebSocket.CLOSED) {
	// 	alert("WebSocket 连接没有建立成功！");
	// }
	if (socket == null) {
		alert("WebSocket 连接没有建立成功！");
		return;
	}
	if(socket.readyState == WebSocket.OPEN){
		// let userID = $("#userId").val();
		console.log(userId + "")
		var s = JSON.stringify({"userId":userId+""})
		socket.send(s);
		alert("用户上线成功！");

	}else{
		alert("WebSocket 连接没有建立成功！");
	}
}


function getWebSocketConnection(uname, page, optionalID) {
    var socket = new WebSocket(
                        "ws://" + window.location.host + getPageContextPath() + "/actions/" + uname + "/" + page + "/" + optionalID);
                
    return socket;
}


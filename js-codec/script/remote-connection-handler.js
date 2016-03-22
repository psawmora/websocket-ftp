define(['require'], function (require) {
    var ws = new WebSocket("ws://localhost:9995/file");
    ws.binaryType = 'arraybuffer';

    ws.onopen = function (event) {
        console.log("WS connection opened");
    };
    ws.close = function (event) {
        console.log("WS connection closed. | " + event);
    };

    ws.onmessage = function (msg) {
        var responseData = msg.data;
        require('state-machine').dispatcher.dispatchInbound(responseData);

        /*
         if (msg.data.byteLength > 0) {
         console.log("Received File Part. File Part Size : " + msg.data.size);
         processFilePartPdu(msg.data);
         } else {
         console.log("File Transmission ended.");
         var fileContent = new Blob(dataParts, {type: "application/octet-stream"});
         var url = URL.createObjectURL(fileContent);
         var link = document.createElement('a');
         link.href = url;
         link.download = fileName;
         link.click();
         URL.revokeObjectURL(url);
         //window.open(url);
         //fileContent.close();
         ws.close();
         }
         */

    };
    return {
        sendRequest: function (fileContext, data) {
            ws.send(data);
        }
    }
});

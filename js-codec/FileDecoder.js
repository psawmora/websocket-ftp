(function ($) {

    $(document).ready(function () {
        var elementList = $("#file-download");
        elementList[0].addEventListener("click", function () {
            downloadFile();
        });
    });

    function downloadFile() {
        console.log("Creating the websocket connection");
        var ws = new WebSocket("ws://172.16.1.74:9995/file");
        ws.binaryType = 'arraybuffer';
        //var fileName = "call_busy.mp3";
        var fileName = "Priscilla_Ahn_Wallflower.mp3";
        //var fileName = "test-text.txt";

        ws.onopen = function (event) {
            console.log("WS connection opened");
            initProtocol();
        };

        function initProtocol() {
            console.log("Started sending messages.");
            var encoder = new TextEncoder();
            var encodedName = encoder.encode(fileName).buffer;
            var encodedNameLength = encodedName.byteLength;
            var nameLengthBuffer = new ArrayBuffer(4);
            var nameLengthBufferView = new Int32Array(nameLengthBuffer);
            nameLengthBufferView[0] = encodedNameLength;
            var requestBuffer = new Uint8Array(encodedNameLength + 4);
            requestBuffer.set(new Uint8Array(nameLengthBuffer), 0);
            requestBuffer.set(new Uint8Array(encodedName), 4);
            console.log(new Uint8Array(nameLengthBuffer));
            console.log(new Uint8Array(encodedName));
            console.log(requestBuffer);
            ws.send(requestBuffer.buffer);
        }

        var dataParts = [];

        ws.onmessage = function (msg) {
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
        };

        function processFilePartPdu(pduBuffer) {
            console.log("Got File Part");
            var pduView = new DataView(pduBuffer);
            var fileDataLength = pduView.getUint32(0, true);
            var filePartIndex = pduView.getUint32(4, true);
            console.log("File Part Length : " + fileDataLength + " | Index : " + filePartIndex);
            var content = new Uint8Array(pduBuffer);
            dataParts.push(content.subarray(8, 8 + fileDataLength));
            sendAck(pduBuffer);
        }

        function sendAck(pduBuffer) {

        }

    }

})(jQuery);
/**
 * The default PDU encoder.
 * Types of PDUs - INIT, ACK
 *
 */
define(function () {
    return {
        init: function ($) {
            scope = this;
            scope.encoders = {
                init: function (fileContext) {
                    var pduTypeBuffer = new Uint8Array(1);
                    pduTypeBuffer[0] = 1;

                    var encodedId = encodePaddedId(fileContext.id, 8);

                    var encoder = new TextEncoder();
                    var encodedName = encoder.encode(fileContext.fileName).buffer;
                    var lengthOfEncodedName = encodedName.byteLength;
                    var nameLengthBuffer = new Uint32Array(1);
                    nameLengthBuffer[0] = lengthOfEncodedName;

                    var requestBuffer = new Uint8Array(lengthOfEncodedName + 13);
                    requestBuffer.set(pduTypeBuffer, 0);
                    requestBuffer.set(new Uint8Array(encodedId), 1);
                    requestBuffer.set(new Uint8Array(nameLengthBuffer.buffer), 9);
                    requestBuffer.set(new Uint8Array(encodedName), 13);
                    return requestBuffer;
                },
                ack: function (fileContext) {
                    var pduTypeBuffer = new Uint8Array(1);
                    pduTypeBuffer[0] = 3;

                    var encodedId = encodePaddedId(fileContext.id, 8);

                    var nextFilePartIndex = new Uint32Array(1);
                    nextFilePartIndex[0] = fileContext.filePartIndex;

                    var requestBuffer = new Uint8Array(13);
                    requestBuffer.set(pduTypeBuffer, 0);
                    requestBuffer.set(new Uint8Array(encodedId), 1);
                    requestBuffer.set(new Uint8Array(nextFilePartIndex.buffer), 9);
                    return requestBuffer;
                }
            };

            scope.decoders = {
                data: function (responseData) {
                    var responseView = new DataView(responseData);
                    var fileDataLength = responseView.getUint32(9, true);

                    console.log("File Part Length : " + fileDataLength + "");
                    if (fileDataLength == 0) {
                        return {
                            isEnd: 1
                        };
                    }
                    var content = new Uint8Array(responseData);
                    var fileDataPart = content.subarray(13, 13 + fileDataLength);
                    return {
                        isEnd: 0,
                        dataPart: fileDataPart
                    };
                },
                ack: function (responseData) {
                    var responseView = new DataView(responseData);
                    var isExisits = responseView.getUint8(9);

                    return {
                        "exists": isExisits
                    };
                },
                end: function (data) {

                }
            };

            scope.findId = function (responseData) {
                var textDecoder = new TextDecoder();
                var content = new Uint8Array(responseData);
                return textDecoder.decode(content.subarray(1, 9).subarray(0, 5)).trim();
            };

            scope.findType = function (responseData) {
                var responseBytes = new Uint8Array(responseData);
                return responseBytes[1];
            };

            function encodePaddedId(generatedId, maxLength) {
                var encoder = new TextEncoder();
                var encodedId = encoder.encode(generatedId);
                var paddedId = new Uint8Array(maxLength);
                paddedId.set(encodedId, 0);
                return paddedId;
            }

            scope.fileContextConfig = {
                fileContextProto: {
                    id: null,
                    fileURI: null,
                    fileName: null,
                    state: 1,
                    size: -1,
                    filePartIndex: 0,
                    dataBufferArray: []
                },
                create: function (fileName, filePath) {
                    var fileContext = Object.create(this.fileContextProto);
                    fileContext.id = Math.random().toString(36).substr(2, 5);
                    fileContext.fileName = fileName;
                    fileContext.fileURI = filePath;
                    return fileContext;
                }
            };

        }
    };
});
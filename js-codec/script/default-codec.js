/**
 * The default PDU encoder.
 * Types of PDUs - INIT, ACK, END
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

                    var encoder = new TextEncoder();
                    var encodedId = encoder.encode(fileContext.id).buffer;
                    var lengthOfEncodedId = encodedId.byteLength;
                    var lengthOfEncodedIdBuffer = new Uint32Array(1);
                    lengthOfEncodedIdBuffer[0] = lengthOfEncodedId;

                    var encodedName = encoder.encode(fileContext.fileName).buffer;
                    var lengthOfEncodedName = encodedName.byteLength;
                    var nameLengthBuffer = new Uint32Array(1);
                    nameLengthBuffer[0] = lengthOfEncodedName;

                    var requestBuffer = new Uint8Array(lengthOfEncodedId + lengthOfEncodedName + 9);
                    requestBuffer.set(pduTypeBuffer, 0);
                    requestBuffer.set(new Uint8Array(lengthOfEncodedIdBuffer.buffer), 1);
                    requestBuffer.set(new Uint8Array(encodedId), 5);
                    requestBuffer.set(new Uint8Array(nameLengthBuffer.buffer), 5 + lengthOfEncodedId);
                    requestBuffer.set(new Uint8Array(encodedName), 5 + lengthOfEncodedId + 4);
                    return requestBuffer;
                },
                ack: function (fileContext) {
                    var pduTypeBuffer = new Uint8Array(1);
                    pduTypeBuffer[0] = 3;

                    var encoder = new TextEncoder();
                    var encodedId = encoder.encode(fileContext.id).buffer;
                    var lengthOfEncodedId = encodedId.byteLength;
                    var lengthOfEncodedIdBuffer = new Uint32Array(1);
                    lengthOfEncodedIdBuffer[0] = lengthOfEncodedId;

                    var nextFilePartIndex = new Uint32Array(1);
                    nextFilePartIndex[0] = fileContext.filePartIndex;

                    var requestBuffer = new Uint8Array(lengthOfEncodedId + 9);
                    requestBuffer.set(pduTypeBuffer, 0);
                    requestBuffer.set(new Uint8Array(lengthOfEncodedIdBuffer.buffer), 1);
                    requestBuffer.set(new Uint8Array(encodedId), 5);
                    requestBuffer.set(new Uint8Array(nextFilePartIndex.buffer), 5 + lengthOfEncodedId);
                    return requestBuffer;
                }
            };

            scope.decoders = {
                /**
                 * @return {Uint8Array}
                 */
                data: function (responseData) {
                    var responseView = new DataView(responseData);
                    var lengthOfTheIdField = responseView.getUint32(1, true);
                    var fileDataLength = responseView.getUint32(1 + 4 + lengthOfTheIdField, true);

                    console.log("File Part Length : " + fileDataLength + "");
                    if (fileDataLength == 0) {
                        return {
                            isEnd: 1
                        };
                    }
                    var content = new Uint8Array(responseData);
                    var fileDataPart = content.subarray(9 + lengthOfTheIdField, 9 + lengthOfTheIdField + fileDataLength);
                    return {
                        isEnd: 0,
                        dataPart: fileDataPart
                    };
                },
                ack: function (responseData) {
                    var responseView = new DataView(responseData);
                    var lengthOfTheIdField = responseView.getUint32(1, true);
                    var isExisits = responseView.getUint8(5 + lengthOfTheIdField);

                    return {
                        "exists": isExisits
                    };
                },
                end: function (data) {

                }
            };

            scope.findId = function (responseData) {
                var responseView = new DataView(responseData);
                var lengthOfTheIdField = responseView.getUint32(1, true);
                var textDecoder = new TextDecoder();
                var content = new Uint8Array(responseData);
                return textDecoder.decode(content.subarray(5, 5 + lengthOfTheIdField));
            };

            scope.findType = function (responseData) {
                var responseBytes = new Uint8Array(responseData);
                return responseBytes[1];
            };

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
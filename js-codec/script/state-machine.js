/**
 * Contains the state machine.
 * States - INIT, INIT_SENT, INIT_ACK_RECEIVED, SEND_START_SENT,PACKET_RECEIVED,PACKET_ACK_SENT,FAILURE_OCCURRED,END_OF_FILE_RECEIVED
 **/

define(['default-codec', 'remote-connection-handler'], function (codec, sender) {
    var fileContextMap = new Map();
    var stateMachine = {
        init: {
            actOnOutbound: function (fileContext) {
                fileContextMap.set(fileContext.id, fileContext);
                var requestData = codec.encoders.init(fileContext);
                sender.sendRequest(fileContext, requestData);
            },
            actOnInbound: function (fileContext) {
            }
        },
        serverAck: {
            actOnInbound: function (responseData) {
                var id = codec.findId(responseData);
                var fileContext = fileContextMap.get(id);
                if (fileContext != null) {
                    var isExists = codec.decoders.ack(responseData).exists;
                    if (isExists) {
                        //sending client ack;
                        var requestData = codec.encoders.ack(fileContext);
                        sender.sendRequest(fileContext, requestData);
                    } else {
                        console.log("File " + id + " does not exists.");
                    }
                }
            }

        }, data: {
            actOnInbound: function (responseData) {
                var id = codec.findId(responseData);
                var fileContext = fileContextMap.get(id);
                if (fileContext != null) {
                    var dataDetail = codec.decoders.data(responseData);
                    if (!dataDetail.isEnd) {
                        fileContext.dataBufferArray.push(dataDetail.dataPart);
                        fileContext.filePartIndex = fileContext.filePartIndex + 1;
                        var requestData = codec.encoders.ack(fileContext);
                        sender.sendRequest(fileContext, requestData);
                    } else {
                        console.log("File " + id + " at the end.");
                        console.log("File Transmission ended.");
                        var fileContent = new Blob(fileContext.dataBufferArray, {type: "application/octet-stream"});
                        var url = URL.createObjectURL(fileContent);
                        var link = document.createElement('a');
                        link.href = url;
                        link.download = fileContext.fileName;
                        link.click();
                        URL.revokeObjectURL(url);
                    }
                }
            }

        }
    };
    var smMap = {
        1: stateMachine.init,
        2: stateMachine.serverAck,
        4: stateMachine.data
    };

    var dispatcher = {
        dispatchOutbound: function (fileContext) {
            var state = smMap[fileContext.state];
            state.actOnOutbound(fileContext);
        },
        dispatchInbound: function (responseData) {
            var responseView = new DataView(responseData);
            var stateKey = responseView.getUint8(0);
            var state = smMap[stateKey];
            state.actOnInbound(responseData);
        }
    };

    return {
        dispatcher: dispatcher
    }
})
;
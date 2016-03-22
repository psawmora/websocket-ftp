/** State machine -
 * PreInit(1) - The state of the initial file context. The next action is to send the init request to the server.
 *            - Upon receiving a successful response, change the state to ready and sends init_send request.
 * Ready(2)   - If all goes well, this would be the state that receives the first data packet.
 *              Upon receiving the first data packet, changes the state to Receiving(3).
 * Receiving(3) - Updates the number of blocks received.
 **/
requirejs(['jquery', 'default-codec', 'state-machine'], function ($, codec, stateMachine) {
    codec.init($);
    var fileName = "Priscilla_Ahn_Wallflower.mp3";
    var filePath = "Priscilla_Ahn_Wallflower.mp3";

    //var fileName = "call_busy.mp3";
    //var filePath = "call_busy.mp3";
    var fileContext = codec.fileContextConfig.create(fileName, filePath);
    $(document).ready(function () {
        var elementList = $("#file-download");
        elementList[0].addEventListener("click", function () {
            stateMachine.dispatcher.dispatchOutbound(fileContext);
        });
    });
});


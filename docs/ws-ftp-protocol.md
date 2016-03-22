**__WebSocket-File-Transferring-Protocol (Version 1.0.0 -Alpha)__**
-----------------------------------------------------------------
   PDU-Types ->
   1 - init
   2 - data
   3 - terminate
   
### File Transfer Meta Data Initial Request

 - |PDU-Type (1 byte)|Unique_ID_for_the_file(8Bytes)|Length_of_the_file_URI (4Bytes)|File_URI(Encoded in utf-8)|

### File Part Request - Carries actual file parts

 - |PDU-Type (1 byte)|Unique_ID_for_the_file(8Bytes)|Length_of_the_file_part(4Bytes)|part_index(4Bytes)|file_part|

### File Part Ack

 - |PDU-Type (1 byte)|Unique_ID_for_the_file(8Bytes)|next_required_part_index(4Bytes)|  
 
### Ack From Server

 - |PDU-Type (1 byte)|Unique_ID_for_the_file(8Bytes)|is_file_exists(1 byte)|
 
### Handshake
 
 INIT   ----> 
       <----   ACK(1)
 ACK(2) ---->     
       <----   DATA
 ACK(3) ---->     
       <----   DATA
          |
          |
          |
          |
       <----   END          
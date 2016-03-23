**__WebSocket-File-Transferring-Protocol (Version 1.0.0 -Alpha)__**
-----------------------------------------------------------------
   PDU-Types ->
   1 - INIT
   2 - SERVER-ACK
   3 - CLIENT-ACK
   4 - DATA
   
### INIT - Contains meta data related to the file.

 - |PDU-Type (1 byte)|Unique_file_ID_field_length (4 bytes)|Unique_ID_for_the_file|Length_of_the_file_relative_URL (4Bytes)|File_relative_URI(Encoded in utf-8)|

### SERVER

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